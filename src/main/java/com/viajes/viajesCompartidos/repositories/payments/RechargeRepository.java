package com.viajes.viajesCompartidos.repositories.payments;

import com.viajes.viajesCompartidos.entities.payments.Recharge;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.awt.print.Pageable;
import java.util.List;
@Repository
public interface RechargeRepository extends JpaRepository<Recharge, Long> {
    @Query("SELECT r FROM Recharge r WHERE r.user.userId = :userId ORDER BY r.dateCreated DESC")
    List<Recharge> findTopRecharges(@Param("userId") Integer userId);

}
