package com.hbadget.happy_budget.models.dtos;

import com.hbadget.happy_budget.models.enums.ExpenseCategory;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class ExpenseDTO {

    private Long id;
    private double expenseSum;
    private LocalDateTime expenseDate;

    @Enumerated(EnumType.STRING)
    private ExpenseCategory expenseCategory;
}
