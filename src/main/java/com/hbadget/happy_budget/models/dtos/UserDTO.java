package com.hbadget.happy_budget.models.dtos;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;
import java.time.LocalDate;

@Data
public class UserDTO {
    private Long id;

    @NotBlank(message = "This field must not be empty!")
    @Size(min = 2, message = "Invalid name")
    private String username;

    @NotBlank(message = "This field must not be empty!")
    private String phoneNumber;

    @NotBlank(message = "This filed must not be empty!")
    private LocalDate birthday;

    @NotBlank(message = "This field must not be empty!")
    private String email;
}
