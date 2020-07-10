package com.lifecode;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.data.jpa.convert.threeten.Jsr310JpaConverters;

import com.lifecode.security.AppProperties;

import javax.annotation.PostConstruct;
import java.util.TimeZone;

@SpringBootApplication
@EntityScan(basePackageClasses = { 
		LifeCodeApplication.class,
		Jsr310JpaConverters.class 
})
@EnableConfigurationProperties(AppProperties.class)
public class LifeCodeApplication {

	@PostConstruct
	void init() {
		TimeZone.setDefault(TimeZone.getTimeZone("UTC"));
	}

	public static void main(String[] args) {
		SpringApplication.run(LifeCodeApplication.class, args);
	}
}