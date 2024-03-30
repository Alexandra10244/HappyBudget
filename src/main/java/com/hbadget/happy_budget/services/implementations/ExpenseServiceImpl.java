package com.hbadget.happy_budget.services.implementations;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.hbadget.happy_budget.exceptions.ExpenseNotFoundException;
import com.hbadget.happy_budget.models.dtos.ExpenseDTO;
import com.hbadget.happy_budget.models.entities.Expense;
import com.hbadget.happy_budget.models.entities.User;
import com.hbadget.happy_budget.models.enums.ExpenseCategory;
import com.hbadget.happy_budget.repositories.ExpenseRepository;
import com.hbadget.happy_budget.services.interfaces.ExpenseService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Service;

import java.security.Principal;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ExpenseServiceImpl implements ExpenseService {

    private final ExpenseRepository expenseRepository;
    private final ObjectMapper objectMapper;

    @Override
    public ExpenseDTO createExpense(ExpenseDTO expenseDTO, Principal connectedUser) {
        User user = (User) ((UsernamePasswordAuthenticationToken) connectedUser).getPrincipal();
        Expense expense = objectMapper.convertValue(expenseDTO, Expense.class);
        expense.setUser(user);
        Expense responseExpense = expenseRepository.save(expense);

        return objectMapper.convertValue(responseExpense, ExpenseDTO.class);
    }

    @Override
    public ExpenseDTO updateExpense(ExpenseDTO expenseDTO, Long id, Principal connectedUser) {
        User user = (User) ((UsernamePasswordAuthenticationToken) connectedUser).getPrincipal();
        Expense expense = expenseRepository.findById(id).orElseThrow(() -> new ExpenseNotFoundException("Expense not found."));

        if (user.getId().equals(expense.getUser().getId())) {
            if (expenseDTO.getExpenseSum() != 0) {
                expenseDTO.setExpenseSum(expenseDTO.getExpenseSum());
            }

            if (expenseDTO.getExpenseDate() != null) {
                expenseDTO.setExpenseDate(expenseDTO.getExpenseDate());
            }

            if (expenseDTO.getExpenseCategory() != null) {
                expenseDTO.setExpenseCategory(expenseDTO.getExpenseCategory());
            }
        } else{
            throw new ExpenseNotFoundException("Expense not found.");
        }

        return objectMapper.convertValue(expense, ExpenseDTO.class);
    }

    @Override
    public String deleteExpense(Long id, Principal connectedUser) {
        User user = (User) ((UsernamePasswordAuthenticationToken) connectedUser).getPrincipal();
        Expense expense = expenseRepository.findById(id).orElseThrow(() -> new ExpenseNotFoundException("Expense not found."));
        if (user.getId().equals(expense.getUser().getId())) {
            expenseRepository.deleteById(id);
        }
        return "The expense was deleted.";
    }

    @Override
    public List<ExpenseDTO> getExpenseByCategory(ExpenseCategory expenseCategory, Principal connectedUser) {
        User user = (User) ((UsernamePasswordAuthenticationToken) connectedUser).getPrincipal();
        List<Expense> expenses = expenseRepository.findExpenseByCategory(expenseCategory,user.getId());
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
