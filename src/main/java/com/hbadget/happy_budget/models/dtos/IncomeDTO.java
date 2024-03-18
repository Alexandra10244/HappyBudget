package com.hbadget.happy_budget.models.dtos;

import com.hbadget.happy_budget.models.enums.IncomeCategory;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class IncomeDTO {
    private Long id;
    private double incomeSum;
    private LocalDateTime incomeDate;

    @Enumerated(EnumType.STRING)
    private IncomeCategory incomeCategory;
}

