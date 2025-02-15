package com.onedreamus.project;

import jakarta.annotation.PostConstruct;
import java.util.TimeZone;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableJpaAuditing
@EnableScheduling
public class ProjectApplication {

	public static void main(String[] args) {
		SpringApplication.run(ProjectApplication.class, args);
	}


	@PostConstruct
	public void init() {
		// JVM의 기본 타임존을 Asia/Seoul로 설정
		TimeZone.setDefault(TimeZone.getTimeZone("Asia/Seoul"));
	}
}
