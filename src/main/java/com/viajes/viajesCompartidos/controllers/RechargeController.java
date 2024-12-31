package com.viajes.viajesCompartidos.controllers;

import com.viajes.viajesCompartidos.DTO.payments.RechargeDTO;
import com.viajes.viajesCompartidos.DTO.payments.WebhookPayload;
import com.viajes.viajesCompartidos.services.payments.RechargeService;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/recharges")
public class RechargeController {
    private final RechargeService rechargeService;
    @Autowired
    public RechargeController(RechargeService rechargeService) {
        this.rechargeService = rechargeService;
    }
    @PostMapping("/webhook")
    public ResponseEntity<?> paymentWebhook(@RequestBody WebhookPayload payload) {
        // Extraer información relevante
        String type =  payload.getType();
        String action = payload.getAction();
        Map<String, Object> data = payload.getData();
        String paymentIdObject = (String) data.get("id");


        Long paymentId = Long.parseLong(paymentIdObject);
        System.out.println(paymentId);


        // Procesar la notificación
        if ("payment".equals(type)) {
            // Lógica para manejar pagos
            rechargeService.processPayment(paymentId);
        }

        return ResponseEntity.ok().build();
    }

@GetMapping("/topRecharges")
    public ResponseEntity<List<RechargeDTO>> getTopRecharges(
            @RequestParam(required = false) Integer limit ,
            @RequestParam(required = true) Integer userId
) {
        return ResponseEntity.status(HttpStatus.OK).body(rechargeService.getRechargesLimitedAndSortByDate(limit , userId));
}


}
