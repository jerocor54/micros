package com.service.curstomers;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;

@EnableEurekaClient
@SpringBootApplication
public class ServiceCustomersApplication {

	public static void main(String[] args) {
		SpringApplication.run(ServiceCustomersApplication.class, args);
	}

}
