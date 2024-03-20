package com.hbadget.happy_budget.models.dtos;

import com.hbadget.happy_budget.models.enums.BudgetCategory;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.Data;

import java.time.LocalDateTime;


@Data
public class BudgetDTO {
    private Long id;
    private double budgetSum;
    private LocalDateTime budgetDate;

    @Enumerated(EnumType.STRING)
    private BudgetCategory budgetCategory;
}
