package com.viajes.viajesCompartidos.controllers;

import com.viajes.viajesCompartidos.DTO.user.InputUserDTO;
import com.viajes.viajesCompartidos.DTO.user.OutputUserDTO;
import com.viajes.viajesCompartidos.exceptions.BadRequestException;
import com.viajes.viajesCompartidos.exceptions.users.UserNotFoundException;
import com.viajes.viajesCompartidos.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


import java.util.List;

@RestController
@RequestMapping("/api/users")

public class UserController {
    @Autowired
    private UserService userService;
   @GetMapping
    public ResponseEntity<List<OutputUserDTO>> findAll() {
       return ResponseEntity.status(HttpStatus.OK).body(userService.getUsers());
   }
   @GetMapping("/{userId}")
   public ResponseEntity<?> findById(int userId) {
       try {
           return ResponseEntity.status(HttpStatus.OK).body(userService.getUser(userId));
       }catch (UserNotFoundException e) {
           return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
       }
   }

   @PutMapping("/{userId}")
    public ResponseEntity<?> update(@PathVariable int userId, @RequestBody InputUserDTO user) {
       try {
       return ResponseEntity.status(HttpStatus.OK).body(userService.updateUser(userId, user));
       }catch (UserNotFoundException  e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
       }catch (BadRequestException e){
           return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
       }
   }
   @DeleteMapping("/{userId}")
    public ResponseEntity<?> delete(@PathVariable int userId) {
       try {
           return ResponseEntity.status(HttpStatus.OK).body(userService.deleteUser(userId));
       }catch (UserNotFoundException e) {
           return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
       }
   }


}
