package com.hbadget.happy_budget.services.interfaces;

import com.hbadget.happy_budget.models.dtos.UserDTO;

public interface UserService {

    UserDTO createUser(UserDTO userDTO);

    UserDTO updateUser(Long id, UserDTO userDTO);
}
