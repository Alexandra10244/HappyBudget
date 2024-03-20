package com.hbadget.happy_budget.repositories;

import com.hbadget.happy_budget.models.entities.Expense;
import com.hbadget.happy_budget.models.enums.ExpenseCategory;
import com.hbadget.happy_budget.repositories.projections.ReportProjection;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;


@Repository
public interface ExpenseRepository extends JpaRepository<Expense, Long> {
    @Query(value = "SELECT *" +
            "FROM expenses" +
            "WHERE expense_category = :expenseCategory",
            nativeQuery = true)
    List<Expense> findExpenseByCategory(@Param("expenseCategory") ExpenseCategory expenseCategory);

    @Query(value = "SELECT expenses.expense_sum, expenses.expense_category, expenses.expense_date" +
            "FROM expenses" +
            "WHERE DATE(expenses.expense_date) = :date" +
            "AND users.id = :userId",
            nativeQuery = true)
    List<Expense> findAllExpensesByDate(@Param("date") LocalDate date, @Param("userId") Long userId);
}
