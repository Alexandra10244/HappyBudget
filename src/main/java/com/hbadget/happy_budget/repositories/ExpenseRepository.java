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
    @Query(value = "SELECT * " +
            "FROM expenses " +
            "WHERE expense_category = :expenseCategory "+
            "AND user_id = :userId",
            nativeQuery = true)
    List<Expense> findExpensesByCategory(@Param("expenseCategory") String expenseCategory,@Param("userId") Long userId);

    @Query(value = "SELECT * " +
            "FROM expenses " +
            "WHERE user_id = :userId",
            nativeQuery = true)
    List<Expense> findAllExpensesForUser(@Param("userId") Long userId);

    @Query(value = "SELECT * " +
            "FROM expenses " +
            "WHERE DATE(expenses.expense_date) >= :startDate " +
            "AND DATE(expenses.expense_date) <= :endDate " +
            "AND user_id = :userId",
            nativeQuery = true)
    List<Expense> findAllExpensesByDate(@Param("startDate") LocalDate startDate,@Param("endDate") LocalDate endDate, @Param("userId") Long userId);
}
