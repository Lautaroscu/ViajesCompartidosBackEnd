package com.viajes.viajesCompartidos.controllers;

import com.viajes.viajesCompartidos.DTO.user.InputUserDTO;
import com.viajes.viajesCompartidos.DTO.user.OutputUserDTO;
import com.viajes.viajesCompartidos.DTO.vehicles.PredeterminedDTO;
import com.viajes.viajesCompartidos.DTO.vehicles.VehicleDTO;
import com.viajes.viajesCompartidos.DTO.wallet.TransactionDTO;
import com.viajes.viajesCompartidos.enums.TransactionType;
import com.viajes.viajesCompartidos.exceptions.BadRequestException;
import com.viajes.viajesCompartidos.exceptions.EntityNotFoundException;
import com.viajes.viajesCompartidos.exceptions.users.UserNotFoundException;
import com.viajes.viajesCompartidos.services.UserService;
import com.viajes.viajesCompartidos.services.payments.WalletService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


import java.util.List;

@RestController
@RequestMapping("/api/users")

public class UserController {
    private final UserService userService;
    private final WalletService walletService;

    @Autowired
    public UserController(UserService userService, WalletService walletService) {
        this.userService = userService;
        this.walletService = walletService;
    }

    @PostMapping
    public ResponseEntity<OutputUserDTO> createUser(@RequestBody InputUserDTO userDTO) {
        return ResponseEntity.status(HttpStatus.CREATED).body(userService.createUser(userDTO));
    }
    @GetMapping
    public ResponseEntity<List<OutputUserDTO>> findAll() {
        return ResponseEntity.status(HttpStatus.OK).body(userService.getUsers());
    }

    @GetMapping("/id/{userId}")
    public ResponseEntity<?> findById(@PathVariable Integer userId) {

        return ResponseEntity.status(HttpStatus.OK).body(userService.getUser(userId));

    }

    @GetMapping("/exists-by-email/{userEmail}")
    public ResponseEntity<?> existsByEmail(@PathVariable String userEmail) {

        return ResponseEntity.status(HttpStatus.OK).body(userService.existsByEmail(userEmail));

    }

    @GetMapping("/email/{userEmail}")
    public ResponseEntity<?> findByEmail(@PathVariable String userEmail) {

        return ResponseEntity.status(HttpStatus.OK).body(userService.getUser(userEmail));

    }

    @PutMapping("/{userId}")
    public ResponseEntity<?> update(@PathVariable int userId, @RequestBody InputUserDTO user) {

        return ResponseEntity.status(HttpStatus.OK).body(userService.updateUser(userId, user));

    }

    @DeleteMapping("/{userId}")
    public ResponseEntity<?> delete(@PathVariable int userId) {

        return ResponseEntity.status(HttpStatus.OK).body(userService.deleteUser(userId));

    }

    @GetMapping("/{userId}/vehicles")
    public ResponseEntity<List<VehicleDTO>> findAllVehicles(@PathVariable int userId) {

        return ResponseEntity.ok(userService.findAllVehicles(userId));

    }

    @PostMapping("/owner/{ownerId}/vehicles/add")
    public ResponseEntity<?> addVehicle(@PathVariable Integer ownerId, @RequestBody VehicleDTO vehicleDTO) {

        return ResponseEntity.status(HttpStatus.CREATED).body(userService.addVehicle(ownerId, vehicleDTO));

    }

    @PutMapping("/owner/{ownerId}/vehicles/{plate}/edit")
    public ResponseEntity<?> editVehicle(@PathVariable Integer ownerId, @PathVariable String plate, @RequestBody VehicleDTO vehicleDTO) {

        return ResponseEntity.status(HttpStatus.CREATED).body(userService.editVehicle(ownerId, plate, vehicleDTO));

    }

    @PatchMapping("/owner/{ownerId}/vehicles/{plate}/set-pred")
    public ResponseEntity<?> setPred(@PathVariable Integer ownerId, @PathVariable String plate) {

        return ResponseEntity.ok(userService.setVehiclePredetermined(ownerId, plate));

    }

    @DeleteMapping("/owner/{ownerId}/vehicles/{plate}/delete")
    public ResponseEntity<?> deleteVehicle(@PathVariable Integer ownerId, @PathVariable String plate) {

        return ResponseEntity.status(HttpStatus.CREATED).body(userService.deleteVehicle(ownerId, plate));

    }

    @GetMapping("/{userId}/wallet")
    public ResponseEntity<?> getUserWallet(@PathVariable Integer userId) {
        return ResponseEntity.ok(walletService.getWallet(userId));

    }

    @PostMapping("/{userId}/wallet/transactions")
    public ResponseEntity<?> addWalletTransaction(@PathVariable Integer userId, @RequestBody TransactionDTO transactionDTO) {

        return ResponseEntity.status(HttpStatus.CREATED).body(walletService.addTransaction(userId, transactionDTO));

    }

    @GetMapping("/{userId}/wallet/transactions")
    public ResponseEntity<?> getWalletTransactions(
            @PathVariable Integer userId,
            @RequestParam(required = false, defaultValue = "10") Integer maxResults,
            @RequestParam(required = false) TransactionType type

    ) {

        return ResponseEntity.ok(walletService.getTopXTransactionsByType(userId, type, maxResults));

    }


}
