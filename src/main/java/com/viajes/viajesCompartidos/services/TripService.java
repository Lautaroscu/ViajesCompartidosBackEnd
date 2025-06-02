package com.viajes.viajesCompartidos.services;

import java.math.BigDecimal;
import java.text.Normalizer;

import com.viajes.viajesCompartidos.DTO.OutputTripPassengerDTO;
import com.viajes.viajesCompartidos.DTO.GenericResponseDTO;
import com.viajes.viajesCompartidos.DTO.TripPassengerDTO;
import com.viajes.viajesCompartidos.DTO.location.InputLocationDTO;
import com.viajes.viajesCompartidos.DTO.trip.*;
import com.viajes.viajesCompartidos.DTO.user.OutputUserDTO;
import com.viajes.viajesCompartidos.DTO.wallet.TransactionDTO;
import com.viajes.viajesCompartidos.clients.NotificationsClient;
import com.viajes.viajesCompartidos.entities.*;

import com.viajes.viajesCompartidos.enums.TransactionType;
import com.viajes.viajesCompartidos.enums.TripStatus;
import com.viajes.viajesCompartidos.exceptions.BadRequestException;
import com.viajes.viajesCompartidos.exceptions.JoinRequestNotFoundException;
import com.viajes.viajesCompartidos.exceptions.location.InvalidLocationException;
import com.viajes.viajesCompartidos.exceptions.trips.TripContainsPassangersException;
import com.viajes.viajesCompartidos.exceptions.trips.TripNotFoundException;
import com.viajes.viajesCompartidos.exceptions.users.UserNotFoundException;
import com.viajes.viajesCompartidos.models.NotificationModel;
import com.viajes.viajesCompartidos.models.NotificationType;
import com.viajes.viajesCompartidos.repositories.*;


import com.viajes.viajesCompartidos.services.payments.WalletService;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.time.*;
import java.time.format.DateTimeFormatter;
import java.time.format.TextStyle;
import java.util.*;

@Service
public class TripService {
    private final TripRepository tripRepository;
    private final UserRepository userRepository;
    private final NotificationsClient notificationsClient;
    private final JoinRequestRepository joinRequestRepository;
    private final LocationRepository locationRepository;
    private final WalletService walletService;
    private final double tolerance;
    private final TripAlertRepository tripAlertRepository;
    private static final Map<Integer, Double> REFUND_POLICY = Map.of(
            24, 1.0,  // 100% de reembolso
            12, 0.75, // 75% de reembolso
            6, 0.60,  // 60% de reembolso
            2, 0.20   // 20% de reembolso
    );

    @Value("${url.front-end.domain}")
    private String URL;

    @Autowired

    public TripService(TripRepository tripRepository, UserRepository userRepository, NotificationsClient notificationsClient, JoinRequestRepository joinRequestRepository, LocationRepository locationRepository, WalletService walletService, TripAlertRepository tripAlertRepository) {
        this.tripRepository = tripRepository;
        this.userRepository = userRepository;
        this.notificationsClient = notificationsClient;
        this.joinRequestRepository = joinRequestRepository;
        this.locationRepository = locationRepository;
        this.walletService = walletService;
        this.tripAlertRepository = tripAlertRepository;
        this.tolerance = 10.0;

    }

    public List<OutputTripPreviewDTO> findAll(FilterTripDTO filterTripDTO, String sort, String direction) {
        boolean isValidSort = Arrays.stream(Trip.class.getDeclaredFields())
                .anyMatch(field -> field.getName().equals(sort));

        Sort.Direction sortDirection = "desc".equalsIgnoreCase(direction) ? Sort.Direction.DESC : Sort.Direction.ASC;

        Specification<Trip> originFilter = TripSpecifications.isEqualOrigin(filterTripDTO.getOrigin());
        Specification<Trip> destinationFilter = TripSpecifications.isEqualDestination(filterTripDTO.getDestination());
        Specification<Trip> passengersFilter = TripSpecifications.atLeastPassengers(filterTripDTO.getMax_passengers());
        Specification<Trip> dateFilter = TripSpecifications.isDateInRange(filterTripDTO.getStartDate(), filterTripDTO.getEndDate());
        Specification<Trip> availabilityFilter = TripSpecifications.isAvailableForUser(filterTripDTO.getUserId());
        Specification<Trip> maxPrice = TripSpecifications.maxPrice(filterTripDTO.getMaxPrice());
        Specification<Trip> tripType = TripSpecifications.tripType(filterTripDTO.getTripType());
        Specification<Trip> spec = Specification.where(null);

        if (filterTripDTO.getStrict().equals("false")) {
            Specification<Trip> locationFilter = Specification.where(originFilter).or(destinationFilter);

            spec = spec.and(locationFilter)
                    .and(availabilityFilter);
        } else {
            spec = spec
                    .and(originFilter)
                    .and(destinationFilter)
                    .and(passengersFilter)
                    .and(dateFilter)
                    .and(availabilityFilter)
                    .and(maxPrice)
                    .and(tripType);
        }


        return tripRepository

                .findAll(spec, isValidSort ? Sort.by(sortDirection, sort) : Sort.unsorted())
                .stream()
                .map(OutputTripPreviewDTO::new)
                .toList();

    }

    public OutputTripPassengerDTO addPassengerToTrip(TripPassengerDTO tripPassengerDTO) {
        Trip trip = tripRepository.findById(tripPassengerDTO.getTripId())
                .orElseThrow(() -> new TripNotFoundException("Trip not found"));
        User user = userRepository.findById(tripPassengerDTO.getUserId())
                .orElseThrow(() -> new UserNotFoundException("User not found"));

//
//        // Intenta agregar el pasajero; si falla, se lanzará una excepción específica
        trip.addPassenger(user);
//        BigDecimal tripPrice = BigDecimal.valueOf(trip.getPrice());
//        TransactionDTO expense = new TransactionDTO();
//        expense.setAmount(tripPrice);
//        expense.setTransactionType(TransactionType.EXPENSE);
//        expense.setWalletId(user.getWallet().getId());
//
//        walletService.addTransaction(user.getUserId(), expense);

        // Persistir los cambios en la base de datos
        tripRepository.save(trip);
        return new OutputTripPassengerDTO(user, trip);
    }


    public OutputTripDTO findById(int id) {

        Trip trip = tripRepository.findById(id).orElseThrow(() -> new TripNotFoundException("Trip not found"));
        return new OutputTripDTO(trip);
    }

    private Location findOrCreateLocation(InputLocationDTO inputLocationDTO) {
        Optional<Location> maybeLocation = locationRepository.findByCityAndExactPlace(
                inputLocationDTO.getCityName(),
                inputLocationDTO.getExactPlace()
        );
        return maybeLocation.orElseGet(() -> {
            Location loc = new Location();
            loc.setCity(inputLocationDTO.getCityName());
            loc.setLatitude(inputLocationDTO.getCityLatitude());
            loc.setLongitude(inputLocationDTO.getCityLongitude());
            loc.setExactPlace(inputLocationDTO.getExactPlace());
            return locationRepository.save(loc);
        });
    }

    public OutputTripDTO saveTrip(InputTripDTO trip) {
        User owner = userRepository.findById(trip.getOwnerId()).orElseThrow(() -> new UserNotFoundException("User not found"));
        Location origin = this.findOrCreateLocation(trip.getOrigin());
        Location destination = this.findOrCreateLocation(trip.getDestination());

        Trip newTrip = new Trip(origin, destination, trip.getDate(), owner, trip.getMaxPassengers(), trip.getPrice(), trip.getComment(), trip.getTripType());

        newTrip = tripRepository.save(newTrip);

        checkPossibleMatches(newTrip);
        return new OutputTripDTO(newTrip);


    }

    public Location createCopyOfLocation(InputLocationDTO newLocationData) {

        Location newLocation = new Location();
        newLocation.setCity(newLocationData.getCityName());
        newLocation.setExactPlace(newLocationData.getExactPlace());
        newLocation.setLatitude(newLocationData.getCityLatitude());
        newLocation.setLongitude(newLocationData.getCityLongitude());

        return locationRepository.save(newLocation);


    }


    public OutputTripDTO updateTrip(InputTripDTO trip, int tripId) {

        Trip tripUpdated = tripRepository.findById(tripId)
                .orElseThrow(() -> new TripNotFoundException("Trip not found"));
    boolean destinationChanged = !trip.getOrigin().getCityName().equals(tripUpdated.getOrigin().getCity()) || !trip.getDestination().getCityName().equals(tripUpdated.getDestination().getCity());
    boolean  dateChanged = !trip.getDate().equals(tripUpdated.getDate());
    boolean priceChanged = trip.getPrice() != tripUpdated.getPrice();

        if (isBadRequest(trip)) {
            throw new BadRequestException("Bad Request, check the fields and try again");
        }

        if (locationChanged(trip.getOrigin(), tripUpdated.getOrigin())) {
            Location newOrigin = createCopyOfLocation(trip.getOrigin());
            tripUpdated.setOrigin(newOrigin);
        }

        if (locationChanged(trip.getDestination(), tripUpdated.getDestination())) {
            Location newDestination = createCopyOfLocation(trip.getDestination());
            tripUpdated.setDestination(newDestination);
        }

        tripUpdated.setDate(trip.getDate());

        if (trip.getMaxPassengers() < tripUpdated.getCountPassengers()) {
            throw new IllegalArgumentException("Max passengers could not be lower than the count of passengers on board");
        }

        tripUpdated.setMaxPassengers(trip.getMaxPassengers());
        tripUpdated.setComment(trip.getComment());
        tripUpdated.setPrice(trip.getPrice());
        tripUpdated.setTripType(trip.getTripType());

        tripUpdated = tripRepository.save(tripUpdated);

        if(destinationChanged || dateChanged || priceChanged) {
            checkPossibleMatches(tripUpdated);
        }

        return new OutputTripDTO(tripUpdated);
    }

    private boolean locationChanged(InputLocationDTO newLoc, Location currentLoc) {
        if (currentLoc == null) return true;
        return !Objects.equals(newLoc.getCityName(), currentLoc.getCity())
                || !Objects.equals(newLoc.getExactPlace(), currentLoc.getExactPlace())
                || !Objects.equals(newLoc.getCityLatitude(), currentLoc.getLatitude())
                || !Objects.equals(newLoc.getCityLongitude(), currentLoc.getLongitude());
    }


    @Transactional
    public GenericResponseDTO deleteTrip(int id) {
        Trip trip = tripRepository.findById(id).orElseThrow(() -> new TripNotFoundException("Trip not found"));
        if (trip.getCountPassengers() > 0) {
            throw new TripContainsPassangersException("El viaje contiene al menos un pasajero");
        }
        joinRequestRepository.deleteByTrip_TripId(trip.getTripId());
        tripRepository.deleteById(id);

        return new GenericResponseDTO(true, "Trip deleted successfully");
    }

    private boolean isBadRequest(InputTripDTO trip) {

        return trip.getDate() == null || trip.getMaxPassengers() == 0;
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
        JoinRequest joinRequest = joinRequestRepository.findByTrip_TripIdAndUser_UserId(tripId, userId).orElseThrow(() -> new JoinRequestNotFoundException("Join request not found"));

        trip.removePassenger(user);
//        long hoursDiff = TripService.calculateHoursDifference(LocalDateTime.now(), trip.getDate());
//        double refund = TripService.calculateRefund(trip.getPrice(), hoursDiff);
//        BigDecimal refundAmount = new BigDecimal(refund);
//        TransactionDTO refundTransactionDTO = new TransactionDTO();
//        refundTransactionDTO.setAmount(refundAmount);
//        refundTransactionDTO.setTransactionType(TransactionType.RECHARGE);
//        refundTransactionDTO.setWalletId(user.getWallet().getId());
//        walletService.addTransaction(user.getUserId(), refundTransactionDTO);
        tripRepository.save(trip);
        joinRequestRepository.delete(joinRequest);
        joinRequestRepository.flush();

        return new OutputTripPassengerDTO(user, trip);

    }

    public GenericResponseDTO cancelTrip(int tripId) {
        Trip trip = tripRepository.findById(tripId).orElseThrow(() -> new TripNotFoundException("Trip not found"));
        trip.setStatus(TripStatus.CANCELED);
        tripRepository.save(trip);
        return new GenericResponseDTO(true, "Trip cancelled successfully");
    }

    public GenericResponseDTO activateTrip(int tripId) {
        Trip trip = tripRepository.findById(tripId).orElseThrow(() -> new TripNotFoundException("Trip not found"));
        trip.setStatus(TripStatus.ACTIVE);
        tripRepository.save(trip);
        return new GenericResponseDTO(true, "Trip activated successfully");
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


    public List<OutputTripPreviewDTO> getTripsByOwnerId(int userId, TripStatus status) {
        if (!userRepository.existsById(userId)) {
            throw new UserNotFoundException("User not found");
        }
        Specification<Trip> specOwner = TripSpecifications.isEqualOwnerId(userId);
        Specification<Trip> specStatus = TripSpecifications.isEqualStatus(status);
        Specification<Trip> spec = Specification.where(specOwner);
        return tripRepository
                .findAll(spec)
                .stream()
                .map(OutputTripPreviewDTO::new)
                .toList();
    }

    public List<OutputTripPreviewDTO> getTripsOfUser(Integer userId) {

        if (!userRepository.existsById(userId)) {
            throw new UserNotFoundException("User not found");
        }
        return tripRepository
                .findTripsOfUser(userId)
                .stream()
                .map(OutputTripPreviewDTO::new)
                .toList();
    }

    public List<OutputTripPreviewDTO> getTripsByPassengerId(Integer userId) {
        if (!userRepository.existsById(userId)) {
            throw new UserNotFoundException("User not found");
        }
        return tripRepository
                .findByPassengers_UserId(userId)
                .stream()
                .map(OutputTripPreviewDTO::new)
                .toList();
    }

    public List<OutputTripPreviewDTO> getTripByUserIdRolAndStatus(Integer userId, String rol, TripStatus status) {
        List<OutputTripPreviewDTO> trips = null;
        if ("driver".equalsIgnoreCase(rol)) {
            trips = this.getTripsByOwnerId(userId, status);
        } else if ("passenger".equalsIgnoreCase(rol)) {
            trips = this.getTripsByPassengerId(userId);
        }
        if (trips == null) {
            throw new BadRequestException("No trips found , check your parameters");
        }

        return trips;

    }

    public GenericResponseDTO completeTrip(Integer tripId, CompleteTripDTO completeTripDTO) {
        Trip trip = tripRepository.findById(tripId).orElseThrow(() -> new TripNotFoundException("Trip not found"));
        if (!isUserInCity(completeTripDTO)) {
            throw new InvalidLocationException("Invalid location");
        }
        trip.setStatus(TripStatus.COMPLETED);

        tripRepository.save(trip);

        return new GenericResponseDTO(true, "Trip completed successfully");

    }

    private boolean isUserInCity(CompleteTripDTO completeTripDTO) {
        final double EARTH_RADIUS = 6371.0; // Radio de la Tierra en kilómetros

        double a = getA(completeTripDTO);
        System.out.print(completeTripDTO);
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));

        // Distancia entre los dos puntos
        double distance = EARTH_RADIUS * c;
        System.out.println("Distance: " + distance);

        // Comparar con la tolerancia

        return distance <= tolerance;
    }

    private static double getA(CompleteTripDTO completeTripDTO) {
        double userLongitude = Math.toRadians(completeTripDTO.getUserLongitude());
        double userLatitude = Math.toRadians(completeTripDTO.getUserLatitude());

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


    public TripAlertDTO createTripAlert(TripAlertDTO tripAlertDTO) {
        User user = userRepository.findById(tripAlertDTO.getUserId()).orElseThrow(() -> new UserNotFoundException("User not found"));

        TripAlert tripAlert = new TripAlert();
        tripAlert.setUser(user);
        tripAlert.setCreatedAt(LocalDateTime.now());
        tripAlert.setOrigin(tripAlertDTO.getOrigin());
        tripAlert.setDestination(tripAlertDTO.getDestination());
        tripAlert.setActive(true);
        tripAlert.setStartDate(tripAlertDTO.getDateFrom());
        tripAlert.setEndDate(tripAlertDTO.getDateTo());
        tripAlert.setMaxPrice(tripAlertDTO.getMaxPrice());
        this.tripAlertRepository.save(tripAlert);

        return new TripAlertDTO(tripAlert);


    }

    public List<TripAlertDTO> getAllTripsAlertsOfUser(int userid) {
        if (!userRepository.existsById(userid)) {
            throw new UserNotFoundException("User not found");
        }
        return tripAlertRepository
                .findAllByUserId(userid)
                .stream()
                .map(TripAlertDTO::new)
                .toList();
    }

    public void checkPossibleMatches(Trip trip) {

        List<TripAlert> tripAlerts = tripAlertRepository.findAllByActiveTrueOrderByCreatedAtAsc();
        Map<User, List<Trip>> userMatches = new HashMap<>();


        for (TripAlert tripAlert : tripAlerts) {
            if (this.tripAlertMatch(tripAlert, trip)) {
                userMatches
                        .computeIfAbsent(tripAlert.getUser(), k -> new ArrayList<>())
                        .add(trip);
                tripAlert.setActive(false);
            }
        }

        for (Map.Entry<User, List<Trip>> entry : userMatches.entrySet()) {
            User user = entry.getKey();
            List<Trip> matchedTrips = entry.getValue();
            String notificationMessage = buildNotificationMessage(matchedTrips);

            NotificationModel notificationModel = new NotificationModel();
            notificationModel.setTitle("Alerta de viajes");
            notificationModel.setMessage(notificationMessage);
            notificationModel.setButtonTitle("Ver viajes");
            notificationModel.setActionData(this.URL + "/viajes/alertas"); // Podrías mandarlo a su sección de alertas o algo similar
            notificationModel.setRecipientEmails(List.of(user.getEmail()));
            notificationsClient.sendNotification(notificationModel);
        }

    }

    private String getDayOfWeek(LocalDateTime dateTime) {
        DayOfWeek dayOfWeek = dateTime.getDayOfWeek();
        return TripService.capitalizeStr(dayOfWeek.getDisplayName(TextStyle.FULL, Locale.forLanguageTag("es")));
    }

    private String getMonthOfYear(LocalDateTime dateTime) {
        Month month = dateTime.getMonth();
        return TripService.capitalizeStr(month.getDisplayName(TextStyle.FULL, Locale.forLanguageTag("es")));
    }

    private String buildNotificationMessage(List<Trip> matchedTrips) {
        StringBuilder message = new StringBuilder();
        for (Trip matchedTrip : matchedTrips) {

            LocalTime time = matchedTrip.getDate().toLocalTime();
            String timeFormatted = time.format(DateTimeFormatter.ofPattern("HH:mm"));

            message
                    .append("Viaje ")
                    .append(matchedTrip.getOrigin().getCity())
                    .append(" -> ")
                    .append(matchedTrip.getDestination().getCity())
                    .append(" el ")
                    .append(getDayOfWeek(matchedTrip.getDate()))
                    .append(" a las ")
                    .append(timeFormatted)
                    .append("\n");
        }

        return message.toString();
    }


    private boolean tripAlertMatch(TripAlert tripAlert, Trip trip) {
        boolean matchOrigin = tripAlert.getOrigin().contains(trip.getOrigin().getCity());
        boolean matchDestination = tripAlert.getDestination().contains(trip.getDestination().getCity());
        boolean matchRangeDate = (!trip.getDate().isBefore(tripAlert.getStartDate())) &&
                (!trip.getDate().isAfter(tripAlert.getEndDate()));
        boolean matchMaxPrice = trip.getPrice() <= tripAlert.getMaxPrice();

        return matchOrigin && matchDestination && matchRangeDate && matchMaxPrice;


    }

    public void sendTripReminder(int tripId) {
        Trip trip = tripRepository.findById(tripId).orElseThrow(() -> new TripNotFoundException("Trip not found"));
        NotificationModel notificationModel = new NotificationModel();
        notificationModel.setTitle("Recordatorio de viaje");

        notificationModel.setMessage("El conductor " + trip.getOwner().getFirstName() + " del viaje " + trip.getOrigin().getCity() + " -> " + trip.getDestination().getCity() + "\n"

                + getDayOfWeek(trip.getDate()) + " " + trip.getDate().getDayOfMonth() + "de " + getMonthOfYear(trip.getDate()) + " a enviado  un recordatorio.");
        notificationModel.setButtonTitle("Ver viaje");
        notificationModel.setActionData(this.URL + "/viajes/" + trip.getTripId());
        notificationModel.setNotificationType(NotificationType.REMINDER);
        notificationModel.setCreatedAt(LocalDateTime.now());
        List<String> passengers = new ArrayList<>();
        for (User user : trip.getPassengers()) {
            passengers.add(user.getEmail());
        }
        notificationModel.setRecipientEmails(passengers);

        notificationsClient.sendNotification(notificationModel);


    }
    public static String capitalizeStr(String string) {
        if (string == null || string.isEmpty()) {
            return string;
        }
        return string.substring(0, 1).toUpperCase() + string.substring(1);
    }

    public CompletedTripsQuantityDTO getCompletedTripsQuantity(int userId) {
        return new CompletedTripsQuantityDTO(tripRepository.getCompletedTripsQuantityAsOwner(userId));
    }
}


