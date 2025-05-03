package com.viajes.viajesCompartidos.services;

import com.viajes.viajesCompartidos.DTO.user.InputUserDTO;
import com.viajes.viajesCompartidos.DTO.user.OutputUserDTO;
import com.viajes.viajesCompartidos.DTO.vehicles.PredeterminedDTO;
import com.viajes.viajesCompartidos.DTO.vehicles.VehicleDTO;
import com.viajes.viajesCompartidos.DTO.vehicles.VehicleDeleteResponseDTO;
import com.viajes.viajesCompartidos.entities.Vehicle;
import com.viajes.viajesCompartidos.exceptions.BadRequestException;
import com.viajes.viajesCompartidos.exceptions.EntityNotFoundException;
import com.viajes.viajesCompartidos.exceptions.users.UserNotFoundException;
import com.viajes.viajesCompartidos.repositories.UserRepository;
import com.viajes.viajesCompartidos.repositories.VehicleRepository;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.stereotype.Service;

import com.viajes.viajesCompartidos.entities.User;

import java.util.List;

@Service
public class UserService {
    private final UserRepository userRepository;
    private final VehicleRepository vehicleRepository;

    @Autowired
    public UserService(UserRepository userRepository, VehicleRepository vehicleRepository) {
        this.userRepository = userRepository;
        this.vehicleRepository = vehicleRepository;
    }


    public List<OutputUserDTO> getUsers() {
        return userRepository
                .findAll()
                .stream()
                .map(OutputUserDTO::new)
                .toList();
    }


    public OutputUserDTO getUser(Integer id) {
       User user = userRepository.findById(id).orElseThrow( () -> new UserNotFoundException("User not found" ) );
        return new OutputUserDTO(user);
    }

    public OutputUserDTO getUser(String email) {
        System.out.println(email);
        User user = userRepository.findByEmail(email).orElseThrow( () -> new UserNotFoundException("User not found" ) );
        return new OutputUserDTO(user);
    }


    public OutputUserDTO updateUser(int id, InputUserDTO userDTO) {
        if (isBadRequest(userDTO)) {
            throw new BadRequestException("Invalid user, check the fields and try again");
        }
        if (!userRepository.existsById(id)) {
            throw new UserNotFoundException("User not found");
        }
        User userById = userRepository.findById(id).get();
        userById.setFirstName(userDTO.getName());
        userById.setLastName(userDTO.getLastName());
        userById.setPhone(userDTO.getPhoneNumber());
        userById = userRepository.save(userById);
        return new OutputUserDTO(userById);
    }

    public OutputUserDTO deleteUser(int id) {
       User user = userRepository.findById(id).orElseThrow( () -> new UserNotFoundException("User not found")) ;
        userRepository.deleteById(id);
        return new OutputUserDTO(user);
    }

    private boolean isBadRequest(InputUserDTO dto) {
        return dto.getName() == null || dto.getLastName() == null || dto.getPhoneNumber() == null;
    }


    public VehicleDTO addVehicle(Integer ownerId, VehicleDTO vehicleDTO) {
        User owner = userRepository.findById(ownerId).orElseThrow(() -> new UserNotFoundException("User not found"));
        Vehicle vehicle = new Vehicle();
        vehicle.setOwner(owner);
        vehicle.setPlate(vehicleDTO.getPlate());
        vehicle.setModel(vehicleDTO.getModel());
        vehicle.setYear(vehicleDTO.getYear());
        vehicle.setAvailable(vehicleDTO.isAvailable());
        vehicle.setBrand(vehicleDTO.getBrand());
        vehicle.setSeatingCapacity(vehicleDTO.getSeatingCapacity());
        vehicle.setColor(vehicleDTO.getColor());
        if(owner.getVehicles().isEmpty()){
            vehicle.setPredetermined(true);
        }

        owner.addVehicle(vehicle);
        userRepository.save(owner);
        vehicleRepository.save(vehicle);
        return new VehicleDTO(vehicle);


    }

    public VehicleDTO editVehicle(Integer ownerId, String plate, VehicleDTO vehicleDTO) {
        User owner = userRepository.findById(ownerId)
                .orElseThrow(() -> new UserNotFoundException("User not found"));

        Vehicle vehicle = vehicleRepository.findById(plate)
                .orElseThrow(() -> new EntityNotFoundException("Vehicle not found"));

        // Validar que el vehículo pertenezca al usuario
        if (!owner.getVehicles().contains(vehicle)) {
            throw new EntityNotFoundException("Este vehículo no pertenece al usuario");
        }

        // Actualizar campos
        vehicle.setModel(vehicleDTO.getModel());
        vehicle.setYear(vehicleDTO.getYear());
        vehicle.setColor(vehicleDTO.getColor());
        vehicle.setAvailable(vehicleDTO.isAvailable());
        vehicle.setBrand(vehicleDTO.getBrand());
        vehicle.setSeatingCapacity(vehicleDTO.getSeatingCapacity());
        vehicle.setPredetermined(vehicleDTO.isPredetermined());

        // Si este vehículo fue marcado como predeterminado
        if (vehicleDTO.isPredetermined()) {
            // Desmarcar el anterior (si lo hay y es distinto)
            owner.getVehicles().stream()
                    .filter(Vehicle::isPredetermined)
                    .filter(v -> !v.getPlate().equals(plate))
                    .findFirst()
                    .ifPresent(v -> {
                        v.setPredetermined(false);
                        vehicleRepository.save(v);
                    });

            vehicle.setPredetermined(true);
        }

        vehicleRepository.save(vehicle);
        return new VehicleDTO(vehicle);
    }

    public VehicleDeleteResponseDTO deleteVehicle(Integer ownerId, String plate) {
        User owner = userRepository.findById(ownerId)
                .orElseThrow(() -> new UserNotFoundException("User not found"));

        Vehicle vehicleToDelete = vehicleRepository.findById(plate)
                .orElseThrow(() -> new EntityNotFoundException("Vehicle not found"));

        if (!owner.deleteVehicle(vehicleToDelete)) {
            throw new EntityNotFoundException("Vehicle not found in user's list");
        }

        vehicleRepository.delete(vehicleToDelete);
        VehicleDeleteResponseDTO responseDTO = new VehicleDeleteResponseDTO();
        responseDTO.setPlateOld(vehicleToDelete.getPlate());
        responseDTO.setDeleted(true);
        if (vehicleToDelete.isPredetermined()) {
            List<Vehicle> remainingVehicles = owner.getVehicles();
            if (!remainingVehicles.isEmpty()) {
                Vehicle nuevoPred = remainingVehicles.getFirst();
                nuevoPred.setPredetermined(true);
                responseDTO.setPlateNewPred(nuevoPred.getPlate());
                responseDTO.setWasLastVehicle(false);
                vehicleRepository.save(nuevoPred); // Persistimos el cambio

            }else {
                responseDTO.setPlateNewPred(null);
                responseDTO.setWasLastVehicle(true);
            }
        }

    return responseDTO;
    }

    public List<VehicleDTO> findAllVehicles(int userId) {
        User user = userRepository.findById(userId).orElseThrow(() -> new UserNotFoundException("User not found"));
        List<Vehicle> vehicles = user.getVehicles();

        return vehicles
                .stream()
                .map(VehicleDTO::new)
                .toList();
    }

    public boolean existsByEmail(String userEmail) {
        return userRepository.existsByEmail(userEmail);
    }

    public PredeterminedDTO setVehiclePredetermined(Integer ownerId, String plate) {
        User owner = userRepository.findById(ownerId).orElseThrow(() -> new UserNotFoundException("User not found"));

        boolean isPred = owner.setVehiclePredetermined(plate);
        if(isPred){
        userRepository.save(owner);
        }
        return new PredeterminedDTO(isPred);

    }

    public OutputUserDTO createUser(InputUserDTO userDTO) {
        User user = new User();
        user.setFirstName(userDTO.getName());
        user.setLastName(userDTO.getLastName());
        user.setPhone(userDTO.getPhoneNumber());
        user.setEmail(userDTO.getEmail());
        user.setPassword(userDTO.getPassword());
        user.setResidenceCity(userDTO.getResidenceCity());

        user = userRepository.save(user);
       return new OutputUserDTO(user);
    }
}

