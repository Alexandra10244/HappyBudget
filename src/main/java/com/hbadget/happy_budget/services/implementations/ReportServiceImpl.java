package com.hbadget.happy_budget.services.implementations;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.hbadget.happy_budget.models.dtos.BudgetDTO;
import com.hbadget.happy_budget.models.dtos.ExpenseDTO;
import com.hbadget.happy_budget.models.dtos.IncomeDTO;
import com.hbadget.happy_budget.models.dtos.ReportDTO;
import com.hbadget.happy_budget.models.entities.Budget;
import com.hbadget.happy_budget.models.entities.Expense;
import com.hbadget.happy_budget.models.entities.Income;
import com.hbadget.happy_budget.repositories.BudgetRepository;
import com.hbadget.happy_budget.repositories.ExpenseRepository;
import com.hbadget.happy_budget.repositories.IncomeRepository;
import com.hbadget.happy_budget.repositories.projections.ReportProjection;
import com.hbadget.happy_budget.services.interfaces.ReportService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ReportServiceImpl implements ReportService {

    private final IncomeRepository incomeRepository;
    private final ExpenseRepository expenseRepository;
    private final BudgetRepository budgetRepository;

    @Override
    public List<IncomeDTO> getAllIncomesByDate(LocalDate startDate, LocalDate endDate, Long userId) {
        List<Income> incomes = incomeRepository.findAllIncomesByDate(startDate, endDate, userId);
        if (incomes.isEmpty()) {
            return Collections.emptyList();
        }
        return incomes.stream()
                .map(this::convertIncomeToDTO)
                .collect(Collectors.toList());
    }

    @Override
    public List<ExpenseDTO> getAllExpensesByDate(LocalDate startDate, LocalDate endDate, Long userId) {
        List<Expense> expenses = expenseRepository.findAllExpensesByDate(startDate, endDate, userId);
        if (expenses.isEmpty()) {
            return Collections.emptyList();
        }
        return expenses.stream()
                .map(this::convertExpenseToDTO)
                .collect(Collectors.toList());
    }

    @Override
    public List<BudgetDTO> getAllBudgetsByDate(LocalDate startDate, LocalDate endDate, Long userId) {
        List<Budget> budgets = budgetRepository.findAllBudgetsByDate(startDate, endDate, userId);

        if (budgets.isEmpty()) {
            return Collections.emptyList();
        }

        return budgets.stream()
                .map(this::convertBudgetToDTO)
                .collect(Collectors.toList());
    }

    @Override
    public List<ReportDTO> getAllExpensesIncomesByDate(LocalDate startDate, LocalDate endDate, Long userId) {
        List<ReportProjection> reports = budgetRepository.findAllExpensesIncomesByDate(startDate, endDate, userId);
        List<ReportDTO> reportDTOS = new ArrayList<>();

        for (ReportProjection reportProjection : reports) {
            ReportDTO reportDTO = new ReportDTO();
            reportDTO.setExpenseCategory(reportProjection.getExpense_category());
            reportDTO.setExpenseDate(reportProjection.getExpense_date());
            reportDTO.setExpenseSum(reportProjection.getExpense_sum());
            reportDTO.setIncomeCategory(reportProjection.getIncome_category());
            reportDTO.setIncomeDate(reportProjection.getIncome_date());
            reportDTO.setIncomeDate(reportProjection.getIncome_date());

            reportDTOS.add(reportDTO);
        }

        return reportDTOS;
    }

    private IncomeDTO convertIncomeToDTO(Income income) {
        IncomeDTO incomeDTO = new IncomeDTO();
        incomeDTO.setIncomeSum(income.getIncomeSum());
        incomeDTO.setIncomeCategory(income.getIncomeCategory());
        incomeDTO.setIncomeDate(income.getIncomeDate());
        return incomeDTO;
    }

    private ExpenseDTO convertExpenseToDTO(Expense expense) {
        ExpenseDTO expenseDTO = new ExpenseDTO();
        expenseDTO.setExpenseSum(expense.getExpenseSum());
        expenseDTO.setExpenseCategory(expense.getExpenseCategory());
        expenseDTO.setExpenseDate(expense.getExpenseDate());
        return expenseDTO;
    }

    private BudgetDTO convertBudgetToDTO(Budget budget) {
        BudgetDTO budgetDTO = new BudgetDTO();
        budgetDTO.setBudgetDate(budget.getBudgetDate());
        budgetDTO.setBudgetCategory(budget.getBudgetCategory());
        budgetDTO.setBudgetSum(budget.getBudgetSum());

        return budgetDTO;
    }

}
