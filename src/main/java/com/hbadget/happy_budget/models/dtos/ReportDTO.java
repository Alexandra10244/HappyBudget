package com.hbadget.happy_budget.models.dtos;

import com.hbadget.happy_budget.models.entities.Expense;
import com.hbadget.happy_budget.models.entities.Income;
import com.hbadget.happy_budget.models.enums.ExpenseCategory;
import com.hbadget.happy_budget.models.enums.IncomeCategory;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class ReportDTO {

    private Long id;
    private double incomeSum;
    private LocalDateTime incomeDate;
    private IncomeCategory incomeCategory;
    private double expenseSum;
    private LocalDateTime expenseDate;
    private ExpenseCategory expenseCategory;
}
