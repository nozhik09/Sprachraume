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
						description = "Production server"
				)
		}
)
public class SprachraumeApplication {

	public static void main(String[] args) {
		SpringApplication.run(SprachraumeApplication.class, args);
	}
}
