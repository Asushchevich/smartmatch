package com.smartmatch.streaming_service.service;

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

    @Transactional(readOnly = true)
    public List<Stream> getStreamsForMatch(UUID matchId) {
        return streamRepository.findByMatchId(matchId);
    }

    @Transactional
    public Stream createStream(Stream stream) {
        stream.setActive(true);
        return streamRepository.save(stream);
    }

    @Transactional
    public void toggleStreamStatus(UUID streamId, boolean status) {
        Stream stream = streamRepository.findById(streamId)
                .orElseThrow(() -> new RuntimeException("Stream not found"));
        stream.setActive(status);
        streamRepository.save(stream);
    }
}