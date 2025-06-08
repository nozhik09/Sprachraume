package com.example.Sprachraume;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

//@EnableScheduling
@SpringBootApplication
public class SprachraumeApplication {

	public static void main(String[] args) {
		System.out.println(">>>>> Starting Sprachraume...");
		try {
			SpringApplication.run(SprachraumeApplication.class, args);
		} catch (Exception e) {
			System.err.println("❌ Ошибка запуска приложения: " + e.getMessage());
			e.printStackTrace();
		}
		System.out.println("!!!!!!!!!!!!!!!END");
	}

}
