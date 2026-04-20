package com.example.Sprachraume;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.servers.Server;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@OpenAPIDefinition(
		servers = {
				@Server(
						url = "https://sprachraume-production.up.railway.app/api",
						description = "Production server"
				)
		}
)
public class SprachraumeApplication {

	public static void main(String[] args) {
		SpringApplication.run(SprachraumeApplication.class, args);
	}
}