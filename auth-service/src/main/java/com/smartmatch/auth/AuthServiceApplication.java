package com.smartmatch.auth;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;

@SpringBootApplication(scanBasePackages = "com.smartmatch")
public class AuthServiceApplication {
	public static void main(String[] args) {
		System.out.println("--- ПОПЫТКА ЗАПУСКА. СКАН ПАКЕТА: com.smartmatch ---");
		SpringApplication.run(AuthServiceApplication.class, args);
	}
	@Bean
	public CommandLineRunner commandLineRunner(org.springframework.context.ApplicationContext ctx) {
		return args -> {
			System.out.println("--- ПРОВЕРКА ЗАГРУЗКИ КОНТРОЛЛЕРА ---");
			try {
				Object controller = ctx.getBean(com.smartmatch.auth.controller.AuthController.class);
				System.out.println("Контроллер найден: " + controller.getClass().getName());
				ctx.getBeansWithAnnotation(org.springframework.web.bind.annotation.RestController.class)
						.forEach((name, bean) -> System.out.println("RestController bean: " + name));
			} catch (Exception e) {
				System.err.println("ОШИБКА: Контроллер не загружен! Причина: " + e.getMessage());
			}
		};
	}
}
