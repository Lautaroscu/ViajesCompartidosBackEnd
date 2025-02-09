package com.viajes.viajesCompartidos.services;

import com.viajes.viajesCompartidos.DTO.JoinRequestDTO;
import com.viajes.viajesCompartidos.DTO.RequestStatusDTO;
import com.viajes.viajesCompartidos.entities.JoinRequest;
import com.viajes.viajesCompartidos.entities.Trip;
import com.viajes.viajesCompartidos.entities.User;
import com.viajes.viajesCompartidos.enums.RequestStatus;
import com.viajes.viajesCompartidos.exceptions.InvalidJoinRequestException;
import com.viajes.viajesCompartidos.repositories.JoinRequestRepository;
import com.viajes.viajesCompartidos.repositories.TripRepository;
import com.viajes.viajesCompartidos.repositories.UserRepository;
import jakarta.mail.MessagingException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class JoinRequestService {


    private final JoinRequestRepository joinRequestRepository;

    private final UserRepository userRepository;

    private final TripRepository tripRepository;

    private final EmailService emailService;
    @Autowired
    public JoinRequestService(JoinRequestRepository joinRequestRepository , UserRepository userRepository, TripRepository tripRepository, EmailService emailService) {
        this.joinRequestRepository = joinRequestRepository;
        this.userRepository = userRepository;
        this.tripRepository = tripRepository;
        this.emailService = emailService;
    }

            public JoinRequestDTO sendJoinRequest(Integer userId, Integer tripId) throws MessagingException {
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
        request.setStatus(RequestStatus.PENDING);
        // Construir los enlaces de aceptar/rechazar
        String URL = "http://localhost:5173/trip/join-requests";


                // Crear el contenido HTML del email
                String email = request.getTrip().getOwner().getEmail();
                String subject = "Nueva solicitud para tu viaje";
                String htmlContent = String.format(
                        "<div style='font-family: Arial, sans-serif; padding: 20px; text-align: center;'>"
                                + "<h1 style='color: #333;'>Solicitud para unirse a tu viaje</h1>"
                                + "<p style='font-size: 16px;'>El usuario <strong>%s</strong> ha solicitado unirse a tu viaje hacia <strong>%s</strong>.</p>"
                                + "<p style='font-size: 16px;'>Haz clic en el botón de abajo para gestionar las solicitudes.</p>"
                                + "<div style='margin-top: 20px;'>"
                                + "    <a href='%s' style='text-decoration: none; padding: 12px 24px; color: white; "
                                + "        background-color: #007BFF; border-radius: 5px; font-weight: bold; font-size: 16px; "
                                + "        display: inline-block;'>Ver Solicitudes</a>"
                                + "</div>"
                                + "</div>",
                        request.getUser().getFirstName(),  // Nombre del usuario que hizo la solicitud
                        request.getTrip().getDestination(),  // Destino del viaje
                        URL   // URL donde se pueden ver las solicitudes
                );

                emailService.sendHtmlEmail(email , subject , htmlContent);



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
        JoinRequest joinRequest = joinRequestRepository.findByTrip_TripIdAndUser_UserId(tripId , userId).orElseThrow(() -> new InvalidJoinRequestException("Solicitud para unirse al viaje no encontrada"));

        return new JoinRequestDTO(joinRequest);
    }
}
