package com.viajes.viajesCompartidos.services;

import com.viajes.viajesCompartidos.DTO.user.BalanceDTO;
import com.viajes.viajesCompartidos.DTO.user.InputUserDTO;
import com.viajes.viajesCompartidos.DTO.user.OutputUserDTO;
import com.viajes.viajesCompartidos.exceptions.BadRequestException;
import com.viajes.viajesCompartidos.exceptions.users.UserNotFoundException;
import com.viajes.viajesCompartidos.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.stereotype.Service;

import com.viajes.viajesCompartidos.entities.User;

import java.math.BigDecimal;
import java.util.List;

@Service
public class UserService {
    private final UserRepository userRepository;

    @Autowired
    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }


    public List<OutputUserDTO> getUsers() {
        return userRepository
                .findAll()
                .stream()
                .map(OutputUserDTO::new)
                .toList();
    }


    public OutputUserDTO getUser(int id) {
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

    public OutputUserDTO updateBalance(int userId, BalanceDTO userBalance) {
        User userById = userRepository.findById(userId).orElseThrow(() -> new UserNotFoundException("User not found"));
        if (userBalance.getBalance().compareTo(BigDecimal.ZERO) < 0) {
            throw new BadRequestException("Balance could not be negative");
        }
        userById.setBalance(userBalance.getBalance());
        userRepository.save(userById);
        return new OutputUserDTO(userById);
    }
    public void subtractBalance(int userId, BalanceDTO userBalance) {
        User userById = userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException("User not found"));

        BigDecimal currentBalance = userById.getBalance();
        BigDecimal amountToSubtract = userBalance.getBalance();

        if (amountToSubtract.compareTo(BigDecimal.ZERO) <= 0) {
            throw new BadRequestException("Amount to subtract must be positive");
        }

        if (currentBalance.compareTo(amountToSubtract) < 0) {
            throw new BadRequestException("Insufficient balance");
        }

        userById.setBalance(currentBalance.subtract(amountToSubtract));
        userRepository.save(userById);
    }

    public void addBalance(int userId, BalanceDTO userBalance) {
        User userById = userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException("User not found"));

        BigDecimal currentBalance = userById.getBalance();
        BigDecimal amountToAdd = userBalance.getBalance();

        if (amountToAdd.compareTo(BigDecimal.ZERO) <= 0) {
            throw new BadRequestException("Amount to add must be positive");
        }

        userById.setBalance(currentBalance.add(amountToAdd));
        userRepository.save(userById);
    }



}
