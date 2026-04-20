package school.sptech.sistema_xingu_ia;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@EnableFeignClients
public class SistemaXinguIaApplication {
	public static void main(String[] args) {
		SpringApplication.run(SistemaXinguIaApplication.class, args);
	}
}
