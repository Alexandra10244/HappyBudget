package com.hbadget.happy_budget.models.dtos;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.hbadget.happy_budget.models.enums.BudgetCategory;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.Data;

import java.time.LocalDateTime;


@Data
public class BudgetDTO {
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private Long id;
    private double budgetSum;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private LocalDateTime budgetDate;

    @Enumerated(EnumType.STRING)
    private BudgetCategory budgetCategory;
}
