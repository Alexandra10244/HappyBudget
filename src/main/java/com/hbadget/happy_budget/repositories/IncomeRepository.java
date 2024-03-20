package com.hbadget.happy_budget.repositories;

import com.hbadget.happy_budget.models.entities.Income;
import com.hbadget.happy_budget.models.enums.IncomeCategory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface IncomeRepository extends JpaRepository<Income, Long> {
    @Query(value = "SELECT incomes.income_sum, incomes.income_category, incomes.income_date" +
            "FROM incomes" +
            "WHERE DATE(incomes.income_date) = :date" +
            "AND users.id = :userId" ,
            nativeQuery = true)
    List<Income> findAllIncomesByDate(@Param("date") LocalDate date, @Param("userId") Long userId);

    @Query(value = "SELECT *" +
            "FROM incomes" +
            "WHERE income_category = :incomeCategory",
            nativeQuery = true)
    List<Income> findIncomeByCategory(@Param("incomeCategory") IncomeCategory incomeCategory);
}
