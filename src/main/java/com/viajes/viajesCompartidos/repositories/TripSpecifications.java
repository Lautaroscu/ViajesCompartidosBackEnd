package com.viajes.viajesCompartidos.repositories;

import com.viajes.viajesCompartidos.entities.Location;
import com.viajes.viajesCompartidos.entities.Trip;
import com.viajes.viajesCompartidos.enums.TripStatus;
import com.viajes.viajesCompartidos.enums.TripType;
import jakarta.persistence.criteria.*;
import org.springframework.data.jpa.domain.Specification;

import java.time.LocalDateTime;

public class TripSpecifications {

    public static Specification<Trip> isEqualOrigin(String origin) {
        return (Root<Trip> root, CriteriaQuery<?> query, CriteriaBuilder builder) -> {
            if (origin == null || origin.isEmpty()) {
                return builder.conjunction(); // No aplica filtro
            }
            // Realizamos un join con la tabla Location
            Join<Trip, Location> locationJoin = root.join("origin", JoinType.INNER);

            // Aplicamos el filtro a la propiedad "name" de la tabla Location
            return builder.like( builder.function("unaccent", String.class, builder.lower(locationJoin.get("city"))),
                    "%" + origin.toLowerCase() + "%");

        };
    }

    public static Specification<Trip> isAvailableForUser(Integer userId) {
        return (root, query, criteriaBuilder) -> {
            if (userId == null) {
                return criteriaBuilder.conjunction();
            }

            // Excluir los viajes donde el propietario sea el usuario
            Predicate ownerPredicate = criteriaBuilder.notEqual(root.get("owner").get("userId"), userId);

            // Excluir los viajes que están cancelados
            Predicate statusPredicate = criteriaBuilder.notEqual(root.get("status"), TripStatus.CANCELED);

            // Subconsulta para comprobar si el userId ya es pasajero en el viaje
            Subquery<Long> subquery = query.subquery(Long.class);
            Root<Trip> subRoot = subquery.from(Trip.class);
            Join<Object, Object> subPassengers = subRoot.join("passengers");

            subquery.select(criteriaBuilder.literal(1L)) // no importa el valor seleccionado
                    .where(
                            criteriaBuilder.equal(subRoot.get("id"), root.get("id")), // mismo viaje
                            criteriaBuilder.equal(subPassengers.get("userId"), userId) // user es pasajero
                    );

            Predicate passengersPredicate = criteriaBuilder.not(criteriaBuilder.exists(subquery));

            return criteriaBuilder.and(ownerPredicate, statusPredicate, passengersPredicate);
        };
    }


    public static Specification<Trip> isEqualDestination(String destination) {
        return (Root<Trip> root, CriteriaQuery<?> query, CriteriaBuilder builder) -> {
            if (destination == null || destination.isEmpty()) {
                return builder.conjunction(); // No aplica filtro
            }
            // Realizamos un join con la tabla Location
            Join<Trip, Location> locationJoin = root.join("destination", JoinType.INNER);

            // Aplicamos el filtro a la propiedad "name" de la tabla Location
            return builder.like( builder.function("unaccent", String.class, builder.lower(locationJoin.get("city"))),
                    "%" + destination.toLowerCase() + "%");


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

    public static  Specification<Trip> isEqualOwnerId(Integer ownerId) {
        return (Root<Trip> root, CriteriaQuery<?> query, CriteriaBuilder builder) -> {
            if (ownerId == null) {
                return builder.conjunction();  // No aplica filtro si cualquiera de las fechas es nula
            }
            return builder.equal(root.get("owner").get("userId"), ownerId);
        };
    }
    public static Specification<Trip> isEqualStatus(TripStatus status) {
        return (Root<Trip> root, CriteriaQuery<?> query, CriteriaBuilder builder) -> {
            if (status == null || status.toString().isEmpty()) {
                return builder.conjunction();  // No aplica filtro si cualquiera de las fechas es nula
            }
            return builder.equal(root.get("status"), status);
        };
    }
    public static Specification<Trip> maxPrice(Double maxPrice) {
        return (Root<Trip> root, CriteriaQuery<?> query, CriteriaBuilder builder) -> {
            if (maxPrice == null || maxPrice < 0) {
                return builder.conjunction();
            }
            return builder.lessThanOrEqualTo(root.get("price"), maxPrice);
        };
    }
    public static Specification<Trip> tripType(TripType tripType) {
        return (Root<Trip> root, CriteriaQuery<?> query, CriteriaBuilder builder) -> {
            if (tripType == null)
                return builder.conjunction();


            return builder.equal(root.get("tripType"), tripType);
        };
    }





}
