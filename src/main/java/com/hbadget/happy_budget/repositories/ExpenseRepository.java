package com.hbadget.happy_budget.repositories;

import com.hbadget.happy_budget.models.entities.Expense;
import com.hbadget.happy_budget.repositories.projections.ReportProjection;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public interface ExpenseRepository extends JpaRepository<Expense, Long> {
//    @Query("SELECT SUM(e.amount) AS expense_sum, e.category AS expense_category, e.date AS expense_date FROM Expense e GROUP BY e.category, e.date")
//    List<ReportProjection> getExpenseReportProjection();
}
