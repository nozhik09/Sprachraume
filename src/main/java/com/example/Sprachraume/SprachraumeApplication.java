//package com.example.Sprachraume;
//
//import org.springframework.boot.SpringApplication;
//import org.springframework.boot.autoconfigure.SpringBootApplication;
//import org.springframework.scheduling.annotation.EnableScheduling;
//
//@EnableScheduling
//@SpringBootApplication
//public class SprachraumeApplication {
//
//	public static void main(String[] args) {
//		System.out.println(">>>>> Starting Sprachraume...");
//		SpringApplication.run(SprachraumeApplication.class, args);
//		System.out.println(">>>>> УТВ Sprachraume...");
//	}
//
//}
//
//

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
						description = "UNIQUE_SWAGGER_TEST_999"
				)
		}
)
public class SprachraumeApplication {

	public static void main(String[] args) {
		System.out.println("=== START UNIQUE_SWAGGER_TEST_999 ===");
		SpringApplication.run(SprachraumeApplication.class, args);
	}
}

