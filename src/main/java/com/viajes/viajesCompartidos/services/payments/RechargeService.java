package com.viajes.viajesCompartidos.services.payments;

import com.mercadopago.resources.payment.Payment;
import com.viajes.viajesCompartidos.DTO.payments.RechargeDTO;
import com.viajes.viajesCompartidos.entities.User;
import com.viajes.viajesCompartidos.entities.payments.Recharge;
import com.viajes.viajesCompartidos.enums.PaymentStatus;
import com.viajes.viajesCompartidos.enums.RechargeStatus;
import com.viajes.viajesCompartidos.exceptions.users.UserNotFoundException;
import com.viajes.viajesCompartidos.repositories.UserRepository;
import com.viajes.viajesCompartidos.repositories.payments.RechargeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.awt.print.Pageable;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class RechargeService {
    private final RechargeRepository rechargeRepository;
    private final UserRepository userRepository;
    private final WebClient webClient;

    @Autowired
    public RechargeService(RechargeRepository rechargeRepository , UserRepository userRepository, WebClient webClient) {
        this.rechargeRepository = rechargeRepository;
        this.userRepository = userRepository;
        this.webClient = webClient;
    }

    public Recharge createRecharge(RechargeDTO rechargeDTO) {
        User user = userRepository.findById(rechargeDTO.getUserId())
                .orElseThrow(() -> new IllegalArgumentException("Usuario no encontrado"));

        Recharge recharge = new Recharge();
        recharge.setUser(user);
        recharge.setAmount(rechargeDTO.getAmount());
        recharge.setStatus(RechargeStatus.PENDING);
        recharge.setDateCreated(LocalDateTime.now());
        recharge.setPaymentMethod(rechargeDTO.getPaymentType());

        return rechargeRepository.save(recharge);
    }
    public Mono<Payment> getPaymentDetails(Long paymentId) {
        String accessToken = System.getenv("MP_ACCESS_TOKEN"); // Accedes al token desde variables de entorno

        return webClient.get()
                .uri("https://api.mercadopago.com/v1/payments/" + paymentId)
                .header("Authorization", "Bearer " + accessToken)
                .retrieve()
                .bodyToMono(com.mercadopago.resources.payment.Payment.class) // Cambia a una clase personalizada si parseas la respuesta
                .doOnError(error -> System.err.println("Error al obtener detalles de pago: " + error.getMessage()));
    }

    public void updateRechargeStatus(Long rechargeId, RechargeStatus status) {
        Recharge recharge = rechargeRepository.findById(rechargeId)
                .orElseThrow(() -> new IllegalArgumentException("Recarga no encontrada"));

        recharge.setStatus(status);
        rechargeRepository.save(recharge);

        if (status == RechargeStatus.COMPLETED) {
            User user = userRepository.findById(recharge.getUser().getUserId()).orElseThrow(() -> new UserNotFoundException("Usuario no encontrado"));
            user.setBalance(user.getBalance().add(recharge.getAmount())); // Actualiza el saldo
            userRepository.save(user);
        }
    }


    public void processPayment(Long paymentId) {
        // Llamada a la API de MercadoPago para obtener detalles del pago
        getPaymentDetails(paymentId)
                .doOnError(error -> System.err.println("Error en la API de Mercado Pago: " + error.getMessage()))
                .doOnTerminate(() -> System.out.println("Procesamiento del pago finalizado"))
                .subscribe(paymentDetails -> {
                   Recharge recharge = new Recharge();
                   recharge.setStatus(RechargeStatus.PENDING);
                   recharge.setAmount(paymentDetails.getTransactionAmount());
                   recharge.setDateCreated(LocalDateTime.now());
                   recharge.setPaymentMethod("Mercado Pago");
                    User user = userRepository.findByEmail(paymentDetails.getPayer().getEmail()).orElseThrow(()->new UserNotFoundException("user not found"));
                   recharge.setUser(user);
                    rechargeRepository.save(recharge);
                    if("approved".equals(paymentDetails.getStatus())) {
                       updateRechargeStatus(recharge.getId(), RechargeStatus.COMPLETED);
                   }


                });
    }
    public List<RechargeDTO> getRechargesLimitedAndSortByDate(Integer limit , Integer userId) {
        int limitCheck = limit != null ? limit : 5;
        List<Recharge> recharges = rechargeRepository.findTopRecharges(userId);
        return recharges
                .stream()
                .map(RechargeDTO::new)
                .limit(limitCheck)
                .toList();

    }
}
