package com.hbadget.happy_budget.controllers;

import com.hbadget.happy_budget.models.dtos.ChangePasswordRequest;
import com.hbadget.happy_budget.models.dtos.UserDTO;
import com.hbadget.happy_budget.services.interfaces.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;

@Validated
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/users")
public class UserController {

    private final UserService userService;
    @PatchMapping("/details")
    public ResponseEntity<UserDTO> updateUser(Principal connectedUser,@Valid @RequestBody UserDTO userDTO) {
        return ResponseEntity.ok(userService.updateUser(connectedUser, userDTO));
    }

    @PatchMapping
    public ResponseEntity<?> changePassword(
            @RequestBody ChangePasswordRequest request,
            Principal connectedUser
    ) {
        userService.changePassword(request, connectedUser);
        return ResponseEntity.ok().build();
    }
}
