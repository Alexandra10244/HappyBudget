package com.hbadget.happy_budget.models.dtos;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class IncomeDTO {
    private Long id;
    private int incomeSum;
    private LocalDateTime incomeDate;
    private String incomeCategory;
}

