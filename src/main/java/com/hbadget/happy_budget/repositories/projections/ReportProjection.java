package com.hbadget.happy_budget.repositories.projections;

import java.time.LocalDateTime;

public interface ReportProjection {

    Double getExpense_sum();
    Double getIncome_sum();
    String getIncome_category();
    String getExpense_category();
    LocalDateTime getIncome_date();
    LocalDateTime getExpense_date();

}
