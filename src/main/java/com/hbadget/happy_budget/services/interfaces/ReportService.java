package com.hbadget.happy_budget.services.interfaces;

import com.hbadget.happy_budget.models.dtos.BudgetDTO;
import com.hbadget.happy_budget.models.dtos.ExpenseDTO;
import com.hbadget.happy_budget.models.dtos.IncomeDTO;
import com.hbadget.happy_budget.models.dtos.ReportDTO;
import org.springframework.cglib.core.Local;

import java.time.LocalDate;
import java.util.List;

public interface ReportService {

    List<IncomeDTO> getAllIncomesByDate(LocalDate date, Long userId);
    List<ExpenseDTO> getAllIExpensesByDate(LocalDate date, Long userId);
    List<BudgetDTO> getAllBudgetsByDate(LocalDate date, Long userId);

    List<ReportDTO> getAllExpensesIncomesByDate(LocalDate date, Long userId);

}
