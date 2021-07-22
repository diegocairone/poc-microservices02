package com.cairone.notificationservice;

import java.util.function.Consumer;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.messaging.Message;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@SpringBootApplication
public class NotificationServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(NotificationServiceApplication.class, args);
	}
/*
	@Bean
	public Consumer<String> notificationEventSupplier() {
	    return message -> {
	        log.info("Message recieved: {}", message);
	    };
	}*/

    @Bean
    public Consumer<Message<String>> notificationEventSupplier() {
        return message -> log.info("Message recieved: {}", message.getPayload());
    }
}
