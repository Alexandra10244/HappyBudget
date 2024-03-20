package com.hbadget.happy_budget.repositories.projections;

import com.hbadget.happy_budget.models.enums.ExpenseCategory;
import com.hbadget.happy_budget.models.enums.IncomeCategory;

import java.time.LocalDateTime;

public interface ReportProjection {

    Double getExpense_sum();
    Double getIncome_sum();
    IncomeCategory getIncome_category();
    ExpenseCategory getExpense_category();
    LocalDateTime getIncome_date();
    LocalDateTime getExpense_date();

}
