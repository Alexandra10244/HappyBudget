package com.hbadget.happy_budget.services.interfaces;

import com.hbadget.happy_budget.models.dtos.BudgetDTO;
import com.hbadget.happy_budget.models.dtos.ExpenseDTO;
import com.hbadget.happy_budget.models.dtos.IncomeDTO;
import com.hbadget.happy_budget.models.dtos.ReportDTO;
import org.springframework.cglib.core.Local;

import java.time.LocalDate;
import java.util.List;

public interface ReportService {

    List<IncomeDTO> getAllIncomesByDate(LocalDate startDate, LocalDate endDate, Long userId);

    List<ExpenseDTO> getAllExpensesByDate(LocalDate startDate, LocalDate endDate, Long userId);

    List<BudgetDTO> getAllBudgetsByDate(LocalDate startDate, LocalDate endDate, Long userId);

    List<ReportDTO> getAllExpensesIncomesByDate(LocalDate startDate, LocalDate endDate, Long userId);

}
