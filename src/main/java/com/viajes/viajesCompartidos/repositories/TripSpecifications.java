package com.viajes.viajesCompartidos.repositories;

import com.viajes.viajesCompartidos.entities.Trip;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Root;
import org.springframework.data.jpa.domain.Specification;

import java.time.LocalDateTime;

public class TripSpecifications {

    public static Specification<Trip> isEqualOrigin(String origin) {
        return (Root<Trip> root, CriteriaQuery<?> query, CriteriaBuilder builder) -> {
            if (origin == null || origin.isEmpty()) {
                return builder.conjunction();  // No aplica filtro
            }
            return builder.like(builder.lower(root.get("origin")), "%" + origin.toLowerCase() + "%");
        };
    }

    public static Specification<Trip> isEqualDestination(String destination) {
        return (Root<Trip> root, CriteriaQuery<?> query, CriteriaBuilder builder) -> {
            if (destination == null || destination.isEmpty()) {
                return builder.conjunction();  // No aplica filtro
            }
            return builder.like(builder.lower(root.get("destination")), "%" + destination.toLowerCase() + "%");
        };
    }

    public static Specification<Trip> atLeastPassengers(Integer passengers) {
        return (Root<Trip> root, CriteriaQuery<?> query, CriteriaBuilder builder) -> {
            if (passengers == null) {
                return builder.conjunction();  // No aplica filtro
            }
            return builder.greaterThanOrEqualTo(root.get("countPassengers"), passengers);
        };
    }

    public static Specification<Trip> isDateInRange(LocalDateTime startDate, LocalDateTime endDate) {
        return (Root<Trip> root, CriteriaQuery<?> query, CriteriaBuilder builder) -> {
            if (startDate == null || endDate == null) {
                return builder.conjunction();  // No aplica filtro si cualquiera de las fechas es nula
            }
            return builder.and(
                    builder.greaterThanOrEqualTo(root.get("date"), startDate),  // Fecha mayor o igual a la fecha de inicio
                    builder.lessThanOrEqualTo(root.get("date"), endDate)       // Fecha menor o igual a la fecha de fin
            );
        };
    }

}
