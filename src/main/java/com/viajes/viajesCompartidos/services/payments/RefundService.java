package com.viajes.viajesCompartidos.services.payments;

import com.viajes.viajesCompartidos.entities.payments.Refund;
import com.viajes.viajesCompartidos.repositories.payments.RefundRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RefundService {

    private final RefundRepository refundRepository;
    @Autowired
    public RefundService(RefundRepository refundRepository) {
        this.refundRepository = refundRepository;
    }
    public Refund getRefundById(Long id) {
        return refundRepository.findById(id).orElseThrow(()-> new RuntimeException("Refund not found"));
    }
    public List<Refund> getAllRefunds() {
        return refundRepository.findAll();
    }
    public Refund save(Refund refund) {
        return refundRepository.save(refund);
    }

}
