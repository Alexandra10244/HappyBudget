package com.hbadget.happy_budget.repositories;

import com.hbadget.happy_budget.models.entities.Income;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface IncomeRepository extends JpaRepository<Income, Long> {

//    @Query("SELECT SUM(i.amount) AS income_sum, i.category AS income_category, i.date AS income_date FROM Income i GROUP BY i.category, i.date")
//    List<ReportProjection> getIncomeReportProjection();

    @Query(value = "SELECT incomes.income_sum, incomes.income_category, incomes.income_date" +
            "FROM incomes" +
            "WHERE DATE(incomes.income_date) = :date" +
            "AND users.id = :userId" ,
            nativeQuery = true)
    List<Income> findAllExpensesIncomesByDate(@Param("date") LocalDate date, @Param("userId") Long userId);
}
