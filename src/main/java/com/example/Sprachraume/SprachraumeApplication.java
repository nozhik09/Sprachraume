package com.example.Sprachraume;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationListener;
import org.springframework.boot.context.event.ApplicationFailedEvent;

@SpringBootApplication
public class SprachraumeApplication {

	public static void main(String[] args) {

		SpringApplication app = new SpringApplication(SprachraumeApplication.class);
		app.addListeners((ApplicationListener<ApplicationFailedEvent>) event -> {
			System.err.println("❌ Application failed to start: " + event.getException().getMessage());
			event.getException().printStackTrace();
		});

		System.out.println(">>>>> Starting Sprachraume...");
		app.run(args);
		System.out.println("!!!!!!!!!!!!!!!END");
		System.out.println("✅ PORT: " + System.getenv("PORT"));
	}
}
