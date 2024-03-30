package com.hbadget.happy_budget.repositories;

import com.hbadget.happy_budget.models.entities.Budget;
import com.hbadget.happy_budget.models.enums.BudgetCategory;
import com.hbadget.happy_budget.repositories.projections.ReportProjection;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface BudgetRepository extends JpaRepository<Budget, Long> {

    @Query(value = "SELECT expenses.expense_sum, expenses.expense_category, expenses.expense_date, " +
            "incomes.income_sum, incomes.income_category, incomes.income_date" +
            "FROM ((users " +
            "INNER JOIN expenses ON users.id = expenses.user_id) " +
            "INNER JOIN incomes ON users.id = incomes.user_id) " +
            "WHERE DATE(expenses.expense_date) >= :startDate " +
            "AND DATE(incomes.income_date) <= :endDate" +
            "AND users.id = :userId",
            nativeQuery = true)
    List<ReportProjection> findAllExpensesIncomesByDate(@Param("startDate") LocalDate startDate, @Param("endDate") LocalDate endDate, @Param("userId") Long userId);

    @Query(value = "SELECT *" +
            "FROM budgets" +
            "WHERE budget_category = :budgetCategory" +
            "AND user_id = :userId",
            nativeQuery = true)
    List<Budget> findBudgetByCategory(@Param("budgetCategory") BudgetCategory budgetCategory, @Param("userId") Long userId);

    @Query(value = "SELECT budget.budget_sum, budgets.budget_category, budgets.budget_date" +
            "FROM budgets" +
            "WHERE DATE(budgets.budget_date) >= :startDate" +
            "AND DATE(budgets.budget_date) <= :endDate" +
            "AND user_id = :userId",
            nativeQuery = true)
    List<Budget> findAllBudgetsByDate(@Param("startDate") LocalDate startDate, @Param("endDate") LocalDate endDate, @Param("userId") Long userId);

}
