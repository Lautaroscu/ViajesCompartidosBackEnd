package com.viajes.viajesCompartidos.services.payments;

import com.viajes.viajesCompartidos.entities.payments.PaymentMethod;
import com.viajes.viajesCompartidos.repositories.payments.PaymentMethodRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PaymentMethodService {

    private final PaymentMethodRepository paymentMethodRepository;
    @Autowired
    public PaymentMethodService(PaymentMethodRepository paymentMethodRepository) {
        this.paymentMethodRepository = paymentMethodRepository;
    }


    public List<PaymentMethod> findAll() {
        return paymentMethodRepository.findAll();
    }

    public PaymentMethod findById(Long id) {
        return paymentMethodRepository.findById(id).orElse(null);
    }

    public PaymentMethod save(PaymentMethod paymentMethod) {
        return paymentMethodRepository.save(paymentMethod);
    }

    public void delete(Long id) {
        paymentMethodRepository.deleteById(id);
    }
}