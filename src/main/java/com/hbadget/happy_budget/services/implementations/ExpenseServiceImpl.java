package com.hbadget.happy_budget.services.implementations;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.hbadget.happy_budget.exceptions.ExpenseNotFoundException;
import com.hbadget.happy_budget.models.dtos.ExpenseDTO;
import com.hbadget.happy_budget.models.entities.Expense;
import com.hbadget.happy_budget.models.enums.ExpenseCategory;
import com.hbadget.happy_budget.repositories.ExpenseRepository;
import com.hbadget.happy_budget.services.interfaces.ExpenseService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ExpenseServiceImpl implements ExpenseService {

    private final ExpenseRepository expenseRepository;
    private final ObjectMapper objectMapper;

    @Override
    public ExpenseDTO createExpense(ExpenseDTO expenseDTO) {
        Expense expense = expenseRepository.save(objectMapper.convertValue(expenseDTO, Expense.class));

        return objectMapper.convertValue(expense, ExpenseDTO.class);
    }

    @Override
    public ExpenseDTO updateExpense(ExpenseDTO expenseDTO, Long id) {
        Expense expense = expenseRepository.findById(id).orElseThrow(() -> new ExpenseNotFoundException("Expense not found."));
        if (expenseDTO.getExpenseSum() != 0) {
            expenseDTO.setExpenseSum(expenseDTO.getExpenseSum());
        }

        if (expenseDTO.getExpenseDate() != null) {
            expenseDTO.setExpenseDate(expenseDTO.getExpenseDate());
        }

        if (expenseDTO.getExpenseCategory() != null) {
            expenseDTO.setExpenseCategory(expenseDTO.getExpenseCategory());
        }

        return objectMapper.convertValue(expense, ExpenseDTO.class);
    }

    @Override
    public String deleteIncome(Long id) {
        if (expenseRepository.existsById(id)) {
            expenseRepository.deleteById(id);
        }
        return "The expense was deleted.";
    }

    @Override
    public List<ExpenseDTO> getExpenseByCategory(ExpenseCategory expenseCategory) {
        List<Expense> expenses = expenseRepository.findExpenseByCategory(expenseCategory);
        if (expenses.isEmpty()) {
            return Collections.emptyList();
        }
        return expenses.stream()
                .map(this::convertExpenseToDTO)
                .collect(Collectors.toList());
    }

    private ExpenseDTO convertExpenseToDTO(Expense expense) {
        ExpenseDTO expenseDTO = new ExpenseDTO();
        expenseDTO.setExpenseSum(expense.getExpenseSum());
        expenseDTO.setExpenseCategory(expense.getExpenseCategory());
        expenseDTO.setExpenseDate(expense.getExpenseDate());
        return expenseDTO;
    }
}
