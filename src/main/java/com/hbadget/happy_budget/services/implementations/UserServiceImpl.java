package com.hbadget.happy_budget.services.implementations;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.hbadget.happy_budget.exceptions.InvalidEmailFormatException;
import com.hbadget.happy_budget.exceptions.InvalidPhoneNumberException;
import com.hbadget.happy_budget.exceptions.UserNotFoundException;
import com.hbadget.happy_budget.models.dtos.ChangePasswordRequest;
import com.hbadget.happy_budget.models.dtos.UserDTO;
import com.hbadget.happy_budget.models.entities.User;
import com.hbadget.happy_budget.repositories.UserRepository;
import com.hbadget.happy_budget.services.interfaces.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.security.Principal;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final ObjectMapper objectMapper;
    private final PasswordEncoder passwordEncoder;

    @Override
    public UserDTO updateUser(Principal connectedUser, UserDTO userDTO) {
        User user = (User) ((UsernamePasswordAuthenticationToken) connectedUser).getPrincipal();

        if (userDTO.getUsername() != null && !userDTO.getUsername().isEmpty()) {
            user.setUsername(userDTO.getUsername());
        }

        if (userDTO.getPhoneNumber() != null && !userDTO.getPhoneNumber().isEmpty()) {
            user.setPhoneNumber(userDTO.getPhoneNumber());
        }

        if (userDTO.getEmail() != null && !userDTO.getEmail().isEmpty()) {
            user.setEmail(userDTO.getEmail());
        }

        if (userDTO.getBirthday() != null ) {
            user.setBirthday(userDTO.getBirthday());
        }
            return objectMapper.convertValue(user, UserDTO.class);

    }

    private void validatedUserDTO(UserDTO userDTO) {
        String email = userDTO.getEmail();
        String phoneNumber = userDTO.getPhoneNumber();

        if (email == null || !email.matches("^[A-Za-z0-9+_.-]+@(.+)$")) {
            throw new InvalidEmailFormatException("Invalid email.");
        }

        if (phoneNumber == null || !phoneNumber.matches("^(\\+\\d{1,3}( )?)?((\\(\\d{3}\\))|\\d{3})[- .]?\\d{3}[- .]?\\d{4}$"
                + "|^(\\+\\d{1,3}( )?)?(\\d{3}[ ]?){2}\\d{3}$"
                + "|^(\\+\\d{1,3}( )?)?(\\d{3}[ ]?)(\\d{2}[ ]?){2}\\d{2}$")) {
            throw new InvalidPhoneNumberException("Invalid phone number.");
        }
    }

    @Override
    public void changePassword(ChangePasswordRequest request, Principal connectedUser) {

        var user = (User) ((UsernamePasswordAuthenticationToken) connectedUser).getPrincipal();

        if (!passwordEncoder.matches(request.getCurrentPassword(), user.getPassword())) {
            throw new IllegalStateException("Wrong password");
        }

        if (!request.getNewPassword().equals(request.getConfirmationPassword())) {
            throw new IllegalStateException("Password are not the same");
        }

        user.setPassword(passwordEncoder.encode(request.getNewPassword()));

        userRepository.save(user);
    }
}
