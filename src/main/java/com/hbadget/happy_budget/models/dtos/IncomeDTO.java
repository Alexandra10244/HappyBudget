package com.hbadget.happy_budget.models.dtos;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.hbadget.happy_budget.models.enums.IncomeCategory;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class IncomeDTO {
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private Long id;
    private String title;
    private double incomeSum;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private LocalDateTime incomeDate;

    @Enumerated(EnumType.STRING)
    private IncomeCategory incomeCategory;
}

