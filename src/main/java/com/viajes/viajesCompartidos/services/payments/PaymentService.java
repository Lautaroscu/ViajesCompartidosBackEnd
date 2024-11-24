// PaymentService.java
package com.viajes.viajesCompartidos.services.payments;

import com.mercadopago.MercadoPagoConfig;
import com.mercadopago.client.preference.*;
import com.mercadopago.exceptions.MPApiException;
import com.mercadopago.exceptions.MPException;

import com.mercadopago.net.MPResource;
import com.mercadopago.net.MPResponse;
import com.mercadopago.resources.preference.Preference;
import com.viajes.viajesCompartidos.DTO.payments.RequestPayment;

import com.viajes.viajesCompartidos.DTO.payments.TransferRequest;
import com.viajes.viajesCompartidos.DTO.payments.TransferResponseDTO;
import com.viajes.viajesCompartidos.entities.Trip;
import com.viajes.viajesCompartidos.entities.User;
import com.viajes.viajesCompartidos.entities.payments.Payment;

import com.viajes.viajesCompartidos.exceptions.trips.TripNotFoundException;
import com.viajes.viajesCompartidos.exceptions.users.UserNotFoundException;
import com.viajes.viajesCompartidos.repositories.TripRepository;
import com.viajes.viajesCompartidos.repositories.UserRepository;
import com.viajes.viajesCompartidos.repositories.payments.PaymentMethodRepository;
import com.viajes.viajesCompartidos.repositories.payments.PaymentRepository;
import jakarta.annotation.PostConstruct;
import lombok.Value;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.math.BigDecimal;
import java.util.List;

@Service
public class PaymentService {
private final PaymentRepository paymentRepository;
private final UserRepository userRepository;
private final TripRepository tripRepository;
private final PaymentMethodRepository paymentMethodRepository;
private final WebClient webClient;
    @Autowired
    public PaymentService(PaymentRepository paymentRepository , UserRepository userRepository , TripRepository tripRepository , PaymentMethodRepository paymentMethodRepository , WebClient webClient ) {
        this.paymentRepository = paymentRepository;
        this.userRepository = userRepository;
        this.tripRepository = tripRepository;
        this.paymentMethodRepository = paymentMethodRepository;
        this.webClient = webClient;
    }



    public String createPaymentPreference(RequestPayment request) throws MPException {

        User payer = userRepository.findById(request.getUserId()).orElseThrow(() -> new UserNotFoundException("User not found"));
        Trip trip = tripRepository.findById(request.getTripId()).orElseThrow(() -> new TripNotFoundException("Trip not found"));
        PreferenceClient client = new PreferenceClient();

        // Configurar el ítem de pago
        PreferenceItemRequest item = PreferenceItemRequest.builder()
                .title("Pago de "  +payer.getFirstName()  + " al viaje " + trip.getOrigin() + " - " + trip.getDestination() )
                .quantity(1)
                .unitPrice(request.getPaymentAmount())
                .build();

        // Crear y guardar el pago en la base de datos
        Payment payment = new Payment(payer , trip , request.getPaymentAmount() , request.getCurrency() ,paymentMethodRepository.findById(1L).get() );

        paymentRepository.save(payment);

        // Configurar la preferencia de pago
        PreferenceRequest requestPref = PreferenceRequest.builder()
                .items(List.of(item))
                .payer(PreferencePayerRequest.builder().email(payer.getEmail()).build())
                .backUrls(PreferenceBackUrlsRequest.builder()
                        .success("http://localhost:5173/payment/status/success")
                        .failure("http://localhost:5173/payment/status/failure")
                        .pending("http://localhost:5173/payment/status/pending")
                        .build())
                        .autoReturn("approved") // Redirige automáticamente al usuario en caso de éxito
                .build();
        try {
            Preference preference = client.create(requestPref);
            return preference.getInitPoint();
        } catch (MPApiException ex) {
            // Imprimir el mensaje de error para depuración
            ex.printStackTrace();
            return ex.getMessage();
        }
    }

    public Mono<TransferResponseDTO> transferMoney(TransferRequest transferRequest)  {
        return webClient.post()
                .uri("https://h.api-comp.redlink.com.ar/redlink/homologacion/transferenciasinmediatas/2/0/0/")
                .bodyValue(transferRequest) // Cuerpo de la petición
                .retrieve()
                .bodyToMono(TransferResponseDTO.class); // Procesar la respuesta como String
    }




    public List<Payment> findAll() {
        return paymentRepository.findAll();
    }

    public Payment findById(Long id) {
        return paymentRepository.findById(id).orElse(null);
    }

    public Payment save(Payment payment) {
        return paymentRepository.save(payment);
    }

    public void delete(Long id) {
        paymentRepository.deleteById(id);
    }

}


