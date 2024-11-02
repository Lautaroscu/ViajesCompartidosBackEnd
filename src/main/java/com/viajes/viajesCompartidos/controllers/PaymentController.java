package com.viajes.viajesCompartidos.controllers;

import com.mercadopago.exceptions.MPException;
import com.mercadopago.resources.preference.Preference;
import com.viajes.viajesCompartidos.DTO.payments.RequestPayment;
import com.viajes.viajesCompartidos.DTO.payments.RequestPayment;
import com.viajes.viajesCompartidos.entities.payments.Payment;
import com.viajes.viajesCompartidos.services.payments.PaymentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/payments")
public class PaymentController {
    private final PaymentService paymentService;
    @Autowired
    public PaymentController(PaymentService paymentService) {
        this.paymentService = paymentService;
    }
    @PostMapping("/webhook")
    public ResponseEntity<?> paymentWebhook() {
        return ResponseEntity.ok().body("");
    }
    @PostMapping("/createAndRedirect")
    public ResponseEntity<?> createAndRedirect(@RequestBody RequestPayment requestPayment) {
        try {
    return ResponseEntity.status(HttpStatus.CREATED).body(paymentService.createPaymentPreference(requestPayment));
        }catch (MPException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

}
