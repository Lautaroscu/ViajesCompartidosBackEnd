package com.viajes.viajesCompartidos;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import com.mercadopago.*;

@SpringBootApplication
public class ViajesCompartidosApplication implements CommandLineRunner {

	public static void main(String[] args) {
		SpringApplication.run(ViajesCompartidosApplication.class, args);
	}

	@Override
	public void run(String[] args) throws Exception {

		MercadoPagoConfig.setAccessToken(System.getenv("MP_ACCESS_TOKEN"));
	}
}

