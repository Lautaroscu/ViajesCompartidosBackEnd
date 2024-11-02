package com.viajes.viajesCompartidos.repositories;

import com.viajes.viajesCompartidos.entities.Trip;
import com.viajes.viajesCompartidos.enums.TripStatus;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
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
    // Excluir viajes del propietario y cancelados
    public static Specification<Trip> isAvailableForUser(Integer userId) {

        return (root, query, criteriaBuilder) -> {
            if(userId == null) {
                return criteriaBuilder.conjunction();
            }
            // Excluir los viajes donde el propietario sea el usuario
            Predicate ownerPredicate = criteriaBuilder.notEqual(root.get("owner").get("userId"), userId);

            // Excluir los viajes que están cancelados
            Predicate statusPredicate = criteriaBuilder.notEqual(root.get("status"), TripStatus.CANCELED);

            // Excluir los viajes donde el usuario ya es un pasajero
            Predicate passengersPredicate = criteriaBuilder.not(
                    criteriaBuilder.exists(
                            // Subconsulta para comprobar si el userId está en la lista de pasajeros
                            query.subquery(Integer.class).select(root.get("passengers"))
                                    .where(criteriaBuilder.equal(root.get("passengers").get("userId"), userId))
                    )
            );

            // Combina todas las condiciones
            return criteriaBuilder.and(ownerPredicate, statusPredicate, passengersPredicate);
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

    public static Specification<Trip> atLeastPassengers(Integer max_passengers) {
        return (Root<Trip> root, CriteriaQuery<?> query, CriteriaBuilder builder) -> {
            if (max_passengers == null) {
                return builder.conjunction();  // No aplica filtro
            }
            return builder.greaterThanOrEqualTo(root.get("maxPassengers"), max_passengers);
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
