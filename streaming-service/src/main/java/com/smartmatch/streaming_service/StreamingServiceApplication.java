package com.smartmatch.streaming_service;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients; // Импортируем это
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@EnableFeignClients(basePackages = "com.smartmatch.streaming_service.client") // Указываем, где лежат клиенты
@ComponentScan(basePackages = {"com.smartmatch.streaming_service", "com.smartmatch.common"})
public class StreamingServiceApplication {
	public static void main(String[] args) {
		SpringApplication.run(StreamingServiceApplication.class, args);
	}
}