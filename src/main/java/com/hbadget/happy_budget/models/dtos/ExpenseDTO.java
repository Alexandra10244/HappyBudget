package com.hbadget.happy_budget.models.dtos;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class ExpenseDTO {

    private Long id;
    private int expenseSum;
    private LocalDateTime expenseDate;
    private String expenseCategory;
}
