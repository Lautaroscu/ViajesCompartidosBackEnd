package com.viajes.viajesCompartidos.controllers;

import com.mercadopago.exceptions.MPException;
import com.mercadopago.resources.preference.Preference;
import com.viajes.viajesCompartidos.DTO.payments.*;
import com.viajes.viajesCompartidos.DTO.payments.RequestPayment;
import com.viajes.viajesCompartidos.entities.payments.Payment;
import com.viajes.viajesCompartidos.exceptions.BadRequestException;
import com.viajes.viajesCompartidos.services.payments.PaymentService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import reactor.core.publisher.Mono;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/payments")
public class PaymentController {
    private final PaymentService paymentService;
    @Autowired
    public PaymentController(PaymentService paymentService) {
        this.paymentService = paymentService;
    }
    @PostMapping("/webhook")
    public ResponseEntity<?> paymentWebhook(@RequestBody WebhookPayload payload) {
        System.out.println("Webhook payload received: " + payload);
        // Lógica para manejar la notificación de Mercado Pago
        return ResponseEntity.ok().body("Webhook processed successfully");
    }
    @PostMapping("/createAndRedirect")
    public ResponseEntity<?> createAndRedirect(@RequestBody RequestPayment requestPayment) {
        try {
    return ResponseEntity.status(HttpStatus.CREATED).body(paymentService.createPaymentPreference(requestPayment));
        }catch (MPException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }
    @GetMapping("/failure")
    public ResponseEntity<?> failure(
            @RequestParam("collection_id") String collectionId,
            @RequestParam("collection_status") String collectionStatus,
            @RequestParam("external_reference") String externalReference,
            @RequestParam("payment_type") String paymentType,
            @RequestParam("merchant_order_id") String merchantOrderId,
            @RequestParam("preference_id") String preferenceId,
            @RequestParam("site_id") String siteId,
            @RequestParam("processing_mode") String processingMode,
            @RequestParam("merchant_account_id") String merchantAccountId
    ) {
        Map<String, String> response = new HashMap<>();
        response.put("collection_id", collectionId);
        response.put("collection_status", collectionStatus);
        response.put("external_reference", externalReference);
        response.put("payment_type", paymentType);
        response.put("merchant_order_id", merchantOrderId);
        response.put("preference_id", preferenceId);
        response.put("site_id", siteId);
        response.put("processing_mode", processingMode);
        response.put("merchant_account_id", merchantAccountId);
        response.put("message" , "Tu pago no se pudo procesar");
        response.put("status","failed");

        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @GetMapping("/success")
    public ResponseEntity<?> success(
            @RequestParam("collection_id") String collectionId,
            @RequestParam("external_reference") String externalReference
    ) {
        Map<String, String> response = Map.of(
                "status", "success",
                "message", "¡Pago realizado con éxito!",
                "collectionId", collectionId,
                "externalReference", externalReference
        );
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @GetMapping("/pending")
    public ResponseEntity<?> pending(
            @RequestParam("collection_id") String collectionId,
            @RequestParam("external_reference") String externalReference
    ) {
        Map<String, String> response = Map.of(
                "status", "pending",
                "message", "Tu pago está en proceso.",
                "collectionId", collectionId,
                "externalReference", externalReference
        );
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }
    @PostMapping("/transfer")
    public ResponseEntity<Mono<TransferResponseDTO>> transferMoney(@RequestBody TransferRequest transferRequest) {
        try {
            return ResponseEntity.status(HttpStatus.OK).body(paymentService.transferMoney(transferRequest));
        }catch (BadRequestException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
    }


}
