package com.viajes.viajesCompartidos.services;

import com.viajes.viajesCompartidos.DTO.JoinRequestDTO;
import com.viajes.viajesCompartidos.DTO.RequestStatusDTO;
import com.viajes.viajesCompartidos.clients.NotificationsClient;
import com.viajes.viajesCompartidos.entities.JoinRequest;
import com.viajes.viajesCompartidos.entities.Trip;
import com.viajes.viajesCompartidos.entities.User;
import com.viajes.viajesCompartidos.enums.RequestStatus;
import com.viajes.viajesCompartidos.exceptions.InvalidJoinRequestException;
import com.viajes.viajesCompartidos.exceptions.JoinRequestNotFoundException;
import com.viajes.viajesCompartidos.models.NotificationModel;
import com.viajes.viajesCompartidos.models.NotificationType;
import com.viajes.viajesCompartidos.repositories.JoinRequestRepository;
import com.viajes.viajesCompartidos.repositories.TripRepository;
import com.viajes.viajesCompartidos.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class JoinRequestService {


    private final JoinRequestRepository joinRequestRepository;

    private final UserRepository userRepository;

    private final TripRepository tripRepository;

    @Value("${url.front-end.domain}")
    private String URL;
    private final NotificationsClient notificationsClient;



    @Autowired
    public JoinRequestService(JoinRequestRepository joinRequestRepository , UserRepository userRepository, TripRepository tripRepository, NotificationsClient notificationsClient) {
        this.joinRequestRepository = joinRequestRepository;
        this.userRepository = userRepository;
        this.tripRepository = tripRepository;
        this.notificationsClient = notificationsClient;
    }

            public JoinRequestDTO sendJoinRequest(JoinRequestDTO joinRequestDTO)  {
        Integer userId = joinRequestDTO.getUserId();
        Integer tripId = joinRequestDTO.getTripId();
        if(joinRequestRepository.existsByTrip_TripId(tripId) && joinRequestRepository.existsByUser_UserId(userId)) {
            throw new InvalidJoinRequestException("Ya has solicitado unirte a este viaje");
        }
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        Trip trip = tripRepository.findById(tripId)
                .orElseThrow(() -> new RuntimeException("Viaje no encontrado"));

        JoinRequest request = new JoinRequest();
        request.setUser(user);
        request.setTrip(trip);
        request.setMessage(joinRequestDTO.getMessage());
        request.setStatus(RequestStatus.PENDING);



        String tripUrl = URL + "/viajes/" + trip.getTripId();
                String email = request.getTrip().getOwner().getEmail();
                String subject = "Nueva solicitud para tu viaje";
                String message = "El usuario " + request.getUser().getFirstName() + " A solicitado unirse a tu viaje " + trip.getOrigin().getCity() + " -> " + trip.getDestination().getCity();
                NotificationModel notificationModel = new NotificationModel();

                notificationModel.setTitle(subject);
                notificationModel.setMessage(message);
                notificationModel.setNotificationType(NotificationType.JOIN_REQUEST);
                notificationModel.setTripId(tripId);
                notificationModel.setUserId(userId);
                notificationModel.setRecipientEmails(List.of(email));
                notificationModel.setButtonTitle("Ver solicitudes");
                notificationModel.setActionData(tripUrl);
                notificationsClient.sendNotification(notificationModel);



        request = joinRequestRepository.save(request);
        return new JoinRequestDTO(request);

    }

    public JoinRequestDTO updateRequestStatus(Long requestId, RequestStatusDTO status) {
        JoinRequest request = joinRequestRepository.findById(requestId)
                .orElseThrow(() -> new RuntimeException("Solicitud no encontrada"));

        request.setStatus(status.getStatus());
        request = joinRequestRepository.save(request);
        return new JoinRequestDTO(request);
    }

    public List<JoinRequestDTO> getRequestsForTrip(Integer tripId) {
        return joinRequestRepository.findByTrip_TripId(tripId)
                .stream()
                .map(JoinRequestDTO::new)
                .toList();
    }

    public List<JoinRequestDTO> getRequestsByUser(Integer userId) {
        return joinRequestRepository.findByUser_UserId(userId)
                .stream()
                .map(JoinRequestDTO::new)
                .toList();
    }

    public JoinRequestDTO getByTripAndUser(Integer tripId, Integer userId) {
        JoinRequest joinRequest = joinRequestRepository.findByTrip_TripIdAndUser_UserId(tripId , userId).orElseThrow(() -> new JoinRequestNotFoundException("Solicitud para unirse al viaje no encontrada"));

        return new JoinRequestDTO(joinRequest);
    }
}
