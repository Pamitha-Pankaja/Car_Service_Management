package com.example.carService;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;



@SpringBootApplication
@EnableScheduling
public class carServiceApplication {

	public static void main(String[] args) {
    SpringApplication.run(carServiceApplication.class, args);
	}

}
