package com.roima.examinationSystem;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.security.config.annotation.authentication.configuration.EnableGlobalAuthentication;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@EnableGlobalMethodSecurity(prePostEnabled = true)
@EnableTransactionManagement
@SpringBootApplication
public class ExaminationSystemApplication {

	public static void main(String[] args) {
		SpringApplication.run(ExaminationSystemApplication.class, args);
	}

}
