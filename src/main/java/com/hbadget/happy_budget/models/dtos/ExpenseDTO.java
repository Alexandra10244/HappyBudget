package com.hbadget.happy_budget.models.dtos;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.hbadget.happy_budget.models.enums.ExpenseCategory;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class ExpenseDTO {
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private Long id;
    private double expenseSum;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private LocalDateTime expenseDate;

    @Enumerated(EnumType.STRING)
    private ExpenseCategory expenseCategory;
}
