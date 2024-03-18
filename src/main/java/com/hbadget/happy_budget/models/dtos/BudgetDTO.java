package com.hbadget.happy_budget.models.dtos;

import com.hbadget.happy_budget.models.enums.BudgetCategory;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.Data;


@Data
public class BudgetDTO {
    private Long id;
    private double budgetSum;

    @Enumerated(EnumType.STRING)
    private BudgetCategory budgetCategory;
}
