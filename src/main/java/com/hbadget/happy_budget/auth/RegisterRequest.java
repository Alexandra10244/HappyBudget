package com.hbadget.happy_budget.auth;

import jakarta.persistence.Column;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class RegisterRequest {

    private String username;
    private String email;
    private String password;
    private LocalDate birthday;
    private String phoneNumber;
}
