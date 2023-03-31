package org.zerock.bimmovie;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@EnableJpaAuditing
public class BimmovieApplication {

	public static void main(String[] args) {

		SpringApplication.run(BimmovieApplication.class, args);
	}

}
