package com.viajes.viajesCompartidos.services;

import java.math.BigDecimal;
import java.text.Normalizer;

import com.viajes.viajesCompartidos.DTO.OutputTripPassengerDTO;
import com.viajes.viajesCompartidos.DTO.TripPassengerDTO;
import com.viajes.viajesCompartidos.DTO.chat.ChatDTO;
import com.viajes.viajesCompartidos.DTO.trip.CompleteTripDTO;
import com.viajes.viajesCompartidos.DTO.trip.FilterTripDTO;
import com.viajes.viajesCompartidos.DTO.trip.InputTripDTO;
import com.viajes.viajesCompartidos.DTO.trip.OutputTripDTO;
import com.viajes.viajesCompartidos.DTO.user.OutputUserDTO;
import com.viajes.viajesCompartidos.entities.Chat;
import com.viajes.viajesCompartidos.entities.Trip;

import com.viajes.viajesCompartidos.enums.TripStatus;
import com.viajes.viajesCompartidos.exceptions.BadRequestException;
import com.viajes.viajesCompartidos.exceptions.trips.InvalidLocationException;
import com.viajes.viajesCompartidos.exceptions.trips.TripContainsPassangersException;
import com.viajes.viajesCompartidos.exceptions.trips.TripNotFoundException;
import com.viajes.viajesCompartidos.exceptions.users.NotEnoughBalanceException;
import com.viajes.viajesCompartidos.exceptions.users.UserNotFoundException;
import com.viajes.viajesCompartidos.repositories.ChatRepository;
import com.viajes.viajesCompartidos.repositories.TripRepository;
import com.viajes.viajesCompartidos.repositories.TripSpecifications;
import com.viajes.viajesCompartidos.repositories.UserRepository;


import com.viajes.viajesCompartidos.repositories.payments.PaymentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import com.viajes.viajesCompartidos.entities.User;

@Service
public class TripService {
    private final TripRepository tripRepository;
    private final UserRepository userRepository;
    private final ChatRepository chatRepository;
    private final double tolerance;
    private static final Map<Integer, Double> REFUND_POLICY = Map.of(
            24, 1.0,  // 100% de reembolso
            12, 0.75, // 75% de reembolso
            6, 0.60,  // 60% de reembolso
            2, 0.20   // 20% de reembolso
    );

    @Autowired

    public TripService(TripRepository tripRepository, UserRepository userRepository, PaymentRepository PaymentRepository, ChatRepository chatRepository) {
        this.tripRepository = tripRepository;
        this.userRepository = userRepository;
        this.chatRepository = chatRepository;
        this.tolerance = 5.0;

    }

    public List<OutputTripDTO> findAll(FilterTripDTO filterTripDTO, String sort, String direction) {
        boolean isValidSort = Arrays.stream(Trip.class.getDeclaredFields())
                .anyMatch(field -> field.getName().equals(sort));

        Sort.Direction sortDirection = "desc".equalsIgnoreCase(direction) ? Sort.Direction.DESC : Sort.Direction.ASC;


        Specification<Trip> originFilter = TripSpecifications.isEqualOrigin(filterTripDTO.getOrigin());
        Specification<Trip> destinationFilter = TripSpecifications.isEqualDestination(filterTripDTO.getDestination());
        Specification<Trip> passengersFilter = TripSpecifications.atLeastPassengers(filterTripDTO.getMax_passengers());
        Specification<Trip> dateFilter = TripSpecifications.isDateInRange(filterTripDTO.getStartDate(), filterTripDTO.getEndDate());
        Specification<Trip> availabilityFilter = TripSpecifications.isAvailableForUser(filterTripDTO.getUserId());


        Specification<Trip> spec = Specification.where(originFilter)
                .and(destinationFilter)
                .and(passengersFilter)
                .and(dateFilter)
                .and(availabilityFilter);
        return tripRepository

                .findAll(spec, isValidSort ? Sort.by(sortDirection, sort) : Sort.unsorted())
                .stream()
                .map(OutputTripDTO::new)
                .toList();

    }

    public OutputTripPassengerDTO addPassengerToTrip(TripPassengerDTO tripPassengerDTO) {
        Trip trip = tripRepository.findById(tripPassengerDTO.getTripID())
                .orElseThrow(() -> new TripNotFoundException("Trip not found"));
        User user = userRepository.findById(tripPassengerDTO.getPassengerID())
                .orElseThrow(() -> new UserNotFoundException("User not found"));


        // Intenta agregar el pasajero; si falla, se lanzará una excepción específica
        trip.addPassenger(user);
        BigDecimal tripPrice = BigDecimal.valueOf(trip.getPrice());
        user.setBalance(user.getBalance().subtract(tripPrice));
        userRepository.save(user);
        // Persistir los cambios en la base de datos
        tripRepository.save(trip);
        return new OutputTripPassengerDTO(user, trip);
    }


    public OutputTripDTO findById(int id) {
        Chat chat = chatRepository.findById(id).orElse(null);
        assert chat != null;
        ChatDTO chatDTO = new ChatDTO(chat);
        Trip trip = tripRepository.findById(id).orElseThrow(() -> new TripNotFoundException("Trip not found"));
        OutputTripDTO outputTripDTO = new OutputTripDTO(trip);
        outputTripDTO.setChat(chatDTO);
        return outputTripDTO;
    }

    public OutputTripDTO saveTrip(InputTripDTO trip) {
        User owner = userRepository.findById(trip.getOwnerId()).orElseThrow(() -> new UserNotFoundException("User not found"));
        if (isBadRequest(trip)) {
            throw new BadRequestException("Bad Request , check the fields and try again");
        }


        Trip newTrip = new Trip(trip.getOrigin(), trip.getDestination(), trip.getDate(), owner, trip.getMaxPassengers(), trip.getPrice(), trip.getComment(), trip.isPrivate());

        newTrip = tripRepository.save(newTrip);
        Chat newChat = new Chat();
        newChat.setTrip(newTrip);
        newChat = chatRepository.save(newChat);
        OutputTripDTO outputTripDTO = new OutputTripDTO(newTrip);
        outputTripDTO.setChat(new ChatDTO(newChat));
        return outputTripDTO;


    }

    public OutputTripDTO updateTrip(InputTripDTO trip, int tripId) {

        Trip tripUpdated = tripRepository.findById(tripId).orElseThrow(() -> new TripNotFoundException("Trip not found"));
        if (isBadRequest(trip)) {
            throw new BadRequestException("Bad Request , check the fields and try again");
        }
        tripUpdated.setOrigin(trip.getOrigin());
        tripUpdated.setDestination(trip.getDestination());
        tripUpdated.setDate(trip.getDate());
        if (trip.getMaxPassengers() < tripUpdated.getCountPassengers())
            throw new IllegalArgumentException("Max passengers could not be lower than the count of passengers on board");
        tripUpdated.setMaxPassengers(trip.getMaxPassengers());
        tripUpdated.setComment(trip.getComment());
        tripUpdated.setPrice(trip.getPrice());
        tripUpdated.setPrivate(trip.isPrivate());
        tripUpdated = tripRepository.save(tripUpdated);

        return new OutputTripDTO(tripUpdated);
    }

    public void deleteTrip(int id) {
        Trip trip = tripRepository.findById(id).orElseThrow(() -> new TripNotFoundException("Trip not found"));
        if (trip.getCountPassengers() > 0) {
            throw new TripContainsPassangersException("El viaje contiene al menos un pasajero");
        }

        tripRepository.deleteById(id);
    }

    private boolean isBadRequest(InputTripDTO trip) {
        return trip.getOrigin() == null || trip.getDestination() == null || trip.getDate() == null || trip.getMaxPassengers() == 0;
    }

    public List<OutputUserDTO> getPassengers(int tripId) {
        Trip trip = tripRepository.findById(tripId).orElseThrow(() -> new TripNotFoundException("Trip not found"));
        return trip
                .getPassengers()
                .stream()
                .map(OutputUserDTO::new)
                .toList();
    }

    public OutputTripPassengerDTO removePassengerFromTrip(int tripId, int userId) {
        Trip trip = tripRepository.findById(tripId).orElseThrow(() -> new TripNotFoundException("Trip not found"));
        User user = userRepository.findById(userId).orElseThrow(() -> new UserNotFoundException("User not found"));
        trip.removePassenger(user);
        long hoursDiff = TripService.calculateHoursDifference(LocalDateTime.now(), trip.getDate());
        double refund = TripService.calculateRefund(trip.getPrice(), hoursDiff);
        BigDecimal refundAmount = new BigDecimal(refund);
        user.setBalance(user.getBalance().add(refundAmount));
        userRepository.save(user);
        tripRepository.save(trip);

        return new OutputTripPassengerDTO(user, trip);

    }

    public void cancelTrip(int tripId) {
        Trip trip = tripRepository.findById(tripId).orElseThrow(() -> new TripNotFoundException("Trip not found"));
        trip.setStatus(TripStatus.CANCELED);
        tripRepository.save(trip);
    }

    public void activateTrip(int tripId) {
        Trip trip = tripRepository.findById(tripId).orElseThrow(() -> new TripNotFoundException("Trip not found"));
        trip.setStatus(TripStatus.ACTIVE);
        tripRepository.save(trip);
    }

    private static String normalizeString(String input) {
        if (input == null) {
            return null;
        }
        // Normaliza el texto a la forma de descomposición (NFD)
        String normalized = Normalizer.normalize(input, Normalizer.Form.NFD);
        // Elimina los caracteres no ASCII (incluidos los acentos)
        return normalized.replaceAll("\\p{M}", "");
    }


    public List<OutputTripDTO> getTripsByOwnerId(int userId) {
        return tripRepository
                .findByOwner_UserId(userId)
                .stream()
                .map(OutputTripDTO::new)
                .toList();
    }

    public List<OutputTripDTO> getTripsByPassengerId(int userId) {
        return tripRepository
                .findByPassengers_UserId(userId)
                .stream()
                .map(OutputTripDTO::new)
                .toList();
    }

    public void completeTrip(Integer tripId, CompleteTripDTO completeTripDTO) {
        Trip trip = tripRepository.findById(tripId).orElseThrow(() -> new TripNotFoundException("Trip not found"));
        if (!isUserInCity(completeTripDTO)) {
            throw new InvalidLocationException("Invalid location");
        }
        trip.setStatus(TripStatus.COMPLETED);

        tripRepository.save(trip);

    }

    private boolean isUserInCity(CompleteTripDTO completeTripDTO) {
        final double EARTH_RADIUS = 6371.0; // Radio de la Tierra en kilómetros

        double userLatitude = Math.toRadians(completeTripDTO.getUserLatitude());
        double a = getA(completeTripDTO, userLatitude);

        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));

        // Distancia entre los dos puntos
        double distance = EARTH_RADIUS * c;
        System.out.println("Distance: " + distance);

        // Comparar con la tolerancia
        return distance <= tolerance;
    }

    private static double getA(CompleteTripDTO completeTripDTO, double userLatitude) {
        double userLongitude = Math.toRadians(completeTripDTO.getUserLongitude());
        double cityLatitude = Math.toRadians(completeTripDTO.getCityLatitude());
        double cityLongitude = Math.toRadians(completeTripDTO.getCityLongitude());

        double deltaLat = cityLatitude - userLatitude;
        double deltaLon = cityLongitude - userLongitude;

        // Fórmula de Haversine
        return Math.pow(Math.sin(deltaLat / 2), 2)
                + Math.cos(userLatitude) * Math.cos(cityLatitude) * Math.pow(Math.sin(deltaLon / 2), 2);
    }

    private static double calculateRefund(double totalAmount, long hoursBefore) {
        return REFUND_POLICY.entrySet()
                .stream()
                .filter(entry -> hoursBefore >= entry.getKey())
                .map(Map.Entry::getValue)
                .findFirst()
                .orElse(0.0) * totalAmount; // Si no coincide, no hay reembolso (0%)
    }

    public static long calculateHoursDifference(LocalDateTime start, LocalDateTime end) {
        Duration duration = Duration.between(start, end);
        return duration.toHours(); // Diferencia en horas
    }


}
