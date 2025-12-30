package com.alexey.finuch2;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.jdbc.autoconfigure.DataSourceAutoConfiguration;

@SpringBootApplication(exclude = {DataSourceAutoConfiguration.class})
public class Finuch2Application {

	public static void main(String[] args) {
		SpringApplication.run(Finuch2Application.class, args);
	}

}
