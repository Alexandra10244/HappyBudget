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
    @Query(value = "SELECT * " +
            "FROM incomes " +
            "WHERE DATE(incomes.income_date) >= :startDate " +
            "AND DATE(incomes.income_date) <= :endDate " +
            "AND user_id = :userId" ,
            nativeQuery = true)
    List<Income> findAllIncomesByDate(@Param("startDate") LocalDate startDate,@Param("endDate") LocalDate endDate,@Param("userId") Long userId);

    @Query(value = "SELECT * " +
            "FROM incomes " +
            "WHERE income_category = :incomeCategory " +
            "AND user_id = :userId",
            nativeQuery = true)
    List<Income> findIncomesByCategory(@Param("incomeCategory") String incomeCategory, @Param("userId") Long userId);

    @Query(value = "SELECT * " +
            "FROM incomes " +
            "WHERE user_id = :userId",
            nativeQuery = true)
    List<Income> findAllIncomesForUser( @Param("userId") Long userId);
}
