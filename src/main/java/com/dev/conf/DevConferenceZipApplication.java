package com.dev.conf;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@EnableJpaAuditing
@SpringBootApplication
public class DevConferenceZipApplication {

	public static void main(String[] args) {
		SpringApplication.run(DevConferenceZipApplication.class, args);
	}

}
