package school.sptech.sistema_xingu_ia;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.scheduling.annotation.EnableAsync;

@SpringBootApplication
@EnableFeignClients
@EnableAsync
public class SistemaXinguIaApplication {
	public static void main(String[] args) {
		SpringApplication.run(SistemaXinguIaApplication.class, args);
	}
}
