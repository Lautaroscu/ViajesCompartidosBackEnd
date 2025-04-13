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

    @GetMapping
    public ResponseEntity<List<OutputUserDTO>> findAll() {
        return ResponseEntity.status(HttpStatus.OK).body(userService.getUsers());
    }

    @GetMapping("/id/{userId}")
    public ResponseEntity<?> findById(@PathVariable      Integer userId) {
        try {
            return ResponseEntity.status(HttpStatus.OK).body(userService.getUser(userId));
        } catch (UserNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }
    @GetMapping("/exists-by-email/{userEmail}")
    public ResponseEntity<?> existsByEmail(@PathVariable String userEmail) {
        try {
            return ResponseEntity.status(HttpStatus.OK).body(userService.existsByEmail(userEmail));
        } catch (UserNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

    @GetMapping("/email/{userEmail}")
    public ResponseEntity<?> findByEmail(@PathVariable String userEmail) {
        try {
            return ResponseEntity.status(HttpStatus.OK).body(userService.getUser(userEmail));
        } catch (UserNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

    @PutMapping("/{userId}")
    public ResponseEntity<?> update(@PathVariable int userId, @RequestBody InputUserDTO user) {
        try {
            return ResponseEntity.status(HttpStatus.OK).body(userService.updateUser(userId, user));
        } catch (UserNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        } catch (BadRequestException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    @DeleteMapping("/{userId}")
    public ResponseEntity<?> delete(@PathVariable int userId) {
        try {
            return ResponseEntity.status(HttpStatus.OK).body(userService.deleteUser(userId));
        } catch (UserNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

@GetMapping("/{userId}/vehicles")
public ResponseEntity<List<VehicleDTO>> findAllVehicles(@PathVariable int userId) {
    try {
        return ResponseEntity.ok(userService.findAllVehicles(userId));
    }catch (UserNotFoundException e) {
        return ResponseEntity.notFound().build();
    }
}

    @PostMapping("/owner/{ownerId}/vehicles/add")
    public ResponseEntity<?> addVehicle(@PathVariable Integer ownerId , @RequestBody VehicleDTO vehicleDTO) {
        try {
            return ResponseEntity.status(HttpStatus.CREATED).body(userService.addVehicle(ownerId ,vehicleDTO));
        }catch (UserNotFoundException e) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }catch (BadRequestException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    @PutMapping("/owner/{ownerId}/vehicles/{plate}/edit")
    public ResponseEntity<?> editVehicle(@PathVariable Integer ownerId ,@PathVariable String plate, @RequestBody VehicleDTO vehicleDTO) {
        try {
            return ResponseEntity.status(HttpStatus.CREATED).body(userService.editVehicle(ownerId ,plate,vehicleDTO));
        }catch (UserNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }catch (BadRequestException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }
    @PatchMapping("/owner/{ownerId}/vehicles/{plate}/set-pred")
    public ResponseEntity<?> setPred(@PathVariable Integer ownerId ,@PathVariable String plate) {
        try {
            return ResponseEntity.ok(userService.setVehiclePredetermined(ownerId ,plate));
        }catch (UserNotFoundException | EntityNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }
    @DeleteMapping("/owner/{ownerId}/vehicles/{plate}/delete")
    public ResponseEntity<?> deleteVehicle(@PathVariable Integer ownerId ,@PathVariable String plate) {
        try {
            return ResponseEntity.status(HttpStatus.CREATED).body(userService.deleteVehicle(ownerId ,plate));
        }catch (UserNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }catch (BadRequestException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }
    @GetMapping("/{userId}/wallet")
    public ResponseEntity<?> getUserWallet(@PathVariable Integer userId) {
        try {
            return ResponseEntity.ok(walletService.getWallet(userId));
        }catch (UserNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

    @PostMapping("/{userId}/wallet/transactions")
    public ResponseEntity<?> addWalletTransaction(@PathVariable Integer userId, @RequestBody TransactionDTO transactionDTO) {
            try {
                return ResponseEntity.status(HttpStatus.CREATED).body(walletService.addTransaction(userId ,transactionDTO));
            }catch (UserNotFoundException e) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
            }
    }
    @GetMapping("/{userId}/wallet/transactions")
    public ResponseEntity<?> getWalletTransactions(
            @PathVariable Integer userId ,
            @RequestParam(required = false , defaultValue = "10") Integer maxResults,
            @RequestParam(required = false)TransactionType type

            ) {
        try {
            return ResponseEntity.ok(walletService.getTopXTransactionsByType(userId,type, maxResults));
        }catch (UserNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }




}
