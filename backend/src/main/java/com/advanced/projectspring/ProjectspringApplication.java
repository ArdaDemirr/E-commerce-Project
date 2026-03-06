package com.advanced.projectspring;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.PropertySource;

@SpringBootApplication
@PropertySource(value = "classpath:.env", ignoreResourceNotFound = true)
public class ProjectspringApplication {

	public static void main(String[] args) {
		SpringApplication.run(ProjectspringApplication.class, args);
	}

}
