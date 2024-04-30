package be.one16.cerbainterview;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@EnableJpaAuditing
public class CerbaInterviewApplication {

	public static void main(String[] args) {
		SpringApplication.run(CerbaInterviewApplication.class, args);
	}

}
