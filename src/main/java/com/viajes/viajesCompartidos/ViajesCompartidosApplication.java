package com.viajes.viajesCompartidos;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@EnableFeignClients
public class ViajesCompartidosApplication  {

	public static void main(String[] args) {
		SpringApplication.run(ViajesCompartidosApplication.class, args);
	}

}

