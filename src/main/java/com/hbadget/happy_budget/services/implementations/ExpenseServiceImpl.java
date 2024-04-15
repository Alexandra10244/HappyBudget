package com.hbadget.happy_budget.services.implementations;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.hbadget.happy_budget.exceptions.BudgetNotFoundException;
import com.hbadget.happy_budget.exceptions.ExpenseNotFoundException;
import com.hbadget.happy_budget.exceptions.InsufficientFundsException;
import com.hbadget.happy_budget.models.dtos.ExpenseDTO;
import com.hbadget.happy_budget.models.entities.Budget;
import com.hbadget.happy_budget.models.entities.Expense;
import com.hbadget.happy_budget.models.entities.User;
import com.hbadget.happy_budget.models.enums.BudgetCategory;
import com.hbadget.happy_budget.models.enums.ExpenseCategory;
import com.hbadget.happy_budget.repositories.BudgetRepository;
import com.hbadget.happy_budget.repositories.ExpenseRepository;
import com.hbadget.happy_budget.services.interfaces.ExpenseService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Service;

import java.security.Principal;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ExpenseServiceImpl implements ExpenseService {

    private final ExpenseRepository expenseRepository;
    private final ObjectMapper objectMapper;
    private final BudgetRepository budgetRepository;

    @Override
    public ExpenseDTO createExpense(ExpenseDTO expenseDTO, Principal connectedUser) {
        User user = (User) ((UsernamePasswordAuthenticationToken) connectedUser).getPrincipal();

        Budget totalBudget = budgetRepository.findTotalBudgetForUser(user.getId()).orElseThrow(() -> new BudgetNotFoundException("Budget not found."));
        List<Budget> allBudgets = budgetRepository.findAllBudgetsForUser(user.getId());
        double allCategorySum = 0;
        for (Budget budget : allBudgets) {
            if (!budget.getBudgetCategory().equals(BudgetCategory.TOTAL)) {
                allCategorySum += budget.getBudgetSum();
            }
        }

        Optional<Budget> selectedCategoryBudgetOptional = budgetRepository.findBudgetByCategory(expenseDTO.getExpenseCategory().toString(), user.getId());
        if (selectedCategoryBudgetOptional.isPresent()) {
            Budget selectedCategoryBudget = selectedCategoryBudgetOptional.get();
            if (selectedCategoryBudget.getBudgetSum() >= expenseDTO.getExpenseSum()) {
                selectedCategoryBudget.setBudgetSum(selectedCategoryBudget.getBudgetSum() - expenseDTO.getExpenseSum());
                totalBudget.setBudgetSum(totalBudget.getBudgetSum() - expenseDTO.getExpenseSum());
                budgetRepository.save(selectedCategoryBudget);
                budgetRepository.save(totalBudget);
            } else {
                throw new InsufficientFundsException("Insufficient Funds.");
            }
        } else {
            if (totalBudget.getBudgetSum() - allCategorySum >= expenseDTO.getExpenseSum()) {
                totalBudget.setBudgetSum(totalBudget.getBudgetSum() - expenseDTO.getExpenseSum());

                budgetRepository.save(totalBudget);
            } else {
                throw new InsufficientFundsException("Insufficient Funds.");
            }
        }

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
        } else {
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
    public ExpenseDTO getExpenseByCategory(ExpenseCategory expenseCategory, Principal connectedUser) {
        User user = (User) ((UsernamePasswordAuthenticationToken) connectedUser).getPrincipal();
        Expense expense = expenseRepository.findExpenseByCategory(expenseCategory.toString(), user.getId()).orElseThrow(() -> new ExpenseNotFoundException("Expense not found."));

        return objectMapper.convertValue(expense, ExpenseDTO.class);
    }

    private ExpenseDTO convertExpenseToDTO(Expense expense) {
        ExpenseDTO expenseDTO = new ExpenseDTO();
        expenseDTO.setExpenseSum(expense.getExpenseSum());
        expenseDTO.setExpenseCategory(expense.getExpenseCategory());
        expenseDTO.setExpenseDate(expense.getExpenseDate());
        return expenseDTO;
    }

}
