package com.hbadget.happy_budget.services.interfaces;

import com.hbadget.happy_budget.models.dtos.ChangePasswordRequest;
import com.hbadget.happy_budget.models.dtos.UserDTO;

import java.security.Principal;

public interface UserService {
    UserDTO updateUser(Principal connectedUser, UserDTO userDTO);

    void changePassword(ChangePasswordRequest request, Principal connectedUser);
}
