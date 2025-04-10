package com.viajes.viajesCompartidos.services;

import com.viajes.viajesCompartidos.DTO.user.BalanceDTO;
import com.viajes.viajesCompartidos.DTO.user.InputUserDTO;
import com.viajes.viajesCompartidos.DTO.user.OutputUserDTO;
import com.viajes.viajesCompartidos.DTO.vehicles.VehicleDTO;
import com.viajes.viajesCompartidos.entities.Vehicle;
import com.viajes.viajesCompartidos.exceptions.BadRequestException;
import com.viajes.viajesCompartidos.exceptions.users.UserNotFoundException;
import com.viajes.viajesCompartidos.repositories.UserRepository;
import com.viajes.viajesCompartidos.repositories.VehicleRepository;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.stereotype.Service;

import com.viajes.viajesCompartidos.entities.User;

import java.math.BigDecimal;
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
        if (!userRepository.existsById(id)) {
            throw new UserNotFoundException("User not found");
        }
        return new OutputUserDTO(userRepository.findById(id).get());
    }

    public OutputUserDTO getUser(String email) {
        if (!userRepository.existsByEmail(email)) {
            throw new UserNotFoundException("User not found");
        }
        return new OutputUserDTO(userRepository.findByEmail(email).get());
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
        if (!userRepository.existsById(id)) {
            throw new UserNotFoundException("User not found");
        }
        User userById = userRepository.findById(id).get();
        userRepository.deleteById(id);
        return new OutputUserDTO(userById);
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
        vehicle.setColor(vehicleDTO.getColor());

        owner.addVehicle(vehicle);
        userRepository.save(owner);
        vehicleRepository.save(vehicle);
        return new VehicleDTO(vehicle);




    }
}
