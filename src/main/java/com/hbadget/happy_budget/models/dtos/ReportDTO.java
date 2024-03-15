package com.hbadget.happy_budget.models.dtos;

import com.hbadget.happy_budget.models.entities.Expense;
import com.hbadget.happy_budget.models.entities.Income;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class ReportDTO {

    private Long id;
    private LocalDateTime date;
    private Income incomeSum;
    private Expense expenseSum;
}
