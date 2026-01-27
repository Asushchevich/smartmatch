package com.smartmatch.streaming_service.service;

import com.smartmatch.streaming_service.client.MatchClient;
import com.smartmatch.streaming_service.dto.StreamRequest;
import com.smartmatch.streaming_service.exception.ResourceNotFoundException;
import com.smartmatch.streaming_service.model.Stream;
import com.smartmatch.streaming_service.repository.StreamRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class StreamService {

    private final StreamRepository streamRepository;
    private final MatchClient matchClient;

    @Transactional(readOnly = true)
    public List<Stream> getStreamsForMatch(UUID matchId) {
        return streamRepository.findByMatchId(matchId);
    }

    @Transactional
    public Stream createStream(StreamRequest request) {
        boolean exists = matchClient.checkMatchExists(request.matchId());
        if (!exists) {
            throw new ResourceNotFoundException("Матч с ID " + request.matchId() + " не найден!");
        }
        Stream stream = Stream.builder()
                .matchId(request.matchId())
                .streamUrl(request.streamUrl())
                .provider(request.provider())
                .active(true)
                .build();

        return streamRepository.save(stream);
    }

    @Transactional
    public void toggleStreamStatus(UUID streamId, boolean status) {
        Stream stream = streamRepository.findById(streamId)
                .orElseThrow(() -> new RuntimeException("Stream not found"));
        stream.setActive(status);
        streamRepository.save(stream);
    }

    public void deactivateStreamsForMatch(UUID matchId) {
        List<Stream> streams = streamRepository.findByMatchId(matchId);
        streams.forEach(stream -> {
            stream.setActive(false);
        });
        streamRepository.saveAll(streams);
        System.out.println("Все стримы для матча " + matchId + " деактивированы.");
    }

    @org.springframework.amqp.rabbit.annotation.RabbitListener(queues = "stream.queue") // Убедись, что такая очередь есть в RabbitConfig
    public void handleMatchEvent(com.smartmatch.common.dto.MatchEvent event) {
        if ("FINISHED".equals(event.getStatus()) || "CANCELLED".equals(event.getStatus())) {
            deactivateStreamsForMatch(event.getMatchId());
            System.out.println(" [x] Стримы для матча " + event.getMatchId() + " выключены автоматически.");
        }
    }
}