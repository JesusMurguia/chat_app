package com.jesusmurguia.chat_app;

import com.jesusmurguia.chat_app.configuration.RsaKeyProperties;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

@EnableConfigurationProperties(RsaKeyProperties.class)
@SpringBootApplication
public class ChatAppSpringApplication {

	public static void main(String[] args) {
		SpringApplication.run(ChatAppSpringApplication.class, args);
	}

}
