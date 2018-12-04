package com.paas.web;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;

//@SpringBootApplication(exclude = {DataSourceAutoConfiguration.class})
@SpringBootApplication
public class PaasWebApplication {

	public static void main(String[] args) {
		SpringApplication.run(PaasWebApplication.class, args);
	}
}
