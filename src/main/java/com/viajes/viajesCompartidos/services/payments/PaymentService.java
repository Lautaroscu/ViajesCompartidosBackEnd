// PaymentService.java
package com.viajes.viajesCompartidos.services.payments;

import com.mercadopago.exceptions.MPException;

import com.mercadopago.net.MPResource;
import com.mercadopago.net.MPResponse;
import com.mercadopago.resources.preference.Preference;
import com.viajes.viajesCompartidos.DTO.payments.RequestPayment;

import com.viajes.viajesCompartidos.entities.payments.Payment;

import com.viajes.viajesCompartidos.repositories.TripRepository;
import com.viajes.viajesCompartidos.repositories.UserRepository;
import com.viajes.viajesCompartidos.repositories.payments.PaymentRepository;
import jakarta.annotation.PostConstruct;
import lombok.Value;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PaymentService {
private final PaymentRepository paymentRepository;
private final UserRepository userRepository;
private final TripRepository tripRepository;
    @Autowired
    public PaymentService(PaymentRepository paymentRepository , UserRepository userRepository , TripRepository tripRepository) {
        this.paymentRepository = paymentRepository;
        this.userRepository = userRepository;
        this.tripRepository = tripRepository;
    }



    public String createPaymentPreference(RequestPayment request) throws MPException {
      return     "";
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


