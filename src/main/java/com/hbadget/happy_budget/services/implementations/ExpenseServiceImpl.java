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

        Optional<Budget> selectedCategoryBudgetOptional = budgetRepository.findBudgetByCategory(expenseDTO.getExpenseCategory().toString(), user.getId());
        if (selectedCategoryBudgetOptional.isPresent()) {
            Budget selectedCategoryBudget = selectedCategoryBudgetOptional.get();
            if (selectedCategoryBudget.getBudgetSum() >= expenseDTO.getExpenseSum()) {
                selectedCategoryBudget.setBudgetSum(selectedCategoryBudget.getBudgetSum() - expenseDTO.getExpenseSum());
                budgetRepository.save(selectedCategoryBudget);
            } else {
                throw new InsufficientFundsException("Insufficient Funds. Expense exceeds Budget!");
            }
        } else {
            throw new BudgetNotFoundException("Create a budget with the same category as this Expense");
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
        Budget selectedCategoryBudget = budgetRepository.findBudgetByCategory(expenseDTO.getExpenseCategory().toString(), user.getId()).orElseThrow(() -> new BudgetNotFoundException("No budget for this expense category"));

        if (user.getId().equals(expense.getUser().getId())) {
            if (expenseDTO.getTitle() != null && !expenseDTO.getTitle().isEmpty()) {
                expense.setTitle(expenseDTO.getTitle());
            }
            if (expenseDTO.getExpenseSum() != 0) {
                if(expenseDTO.getExpenseSum() < expense.getExpenseSum()){
                    selectedCategoryBudget.setBudgetSum(selectedCategoryBudget.getBudgetSum() + (expense.getExpenseSum() - expenseDTO.getExpenseSum()));
                    budgetRepository.save(selectedCategoryBudget);
                    expense.setExpenseSum(expenseDTO.getExpenseSum());
                }else if (expenseDTO.getExpenseSum() > expense.getExpenseSum()){
                    if(selectedCategoryBudget.getBudgetSum() >= (expenseDTO.getExpenseSum() - expense.getExpenseSum())){
                        selectedCategoryBudget.setBudgetSum(selectedCategoryBudget.getBudgetSum() - (expenseDTO.getExpenseSum() - expense.getExpenseSum()));
                        budgetRepository.save(selectedCategoryBudget);
                        expense.setExpenseSum(expenseDTO.getExpenseSum());
                    }else {
                        throw new InsufficientFundsException("Insufficient funds in Budget!");
                    }
                }
            }
            if (expenseDTO.getExpenseDate() != null) {
                expense.setExpenseDate(expenseDTO.getExpenseDate());
            }

            if (expenseDTO.getExpenseCategory() != null) {
                expense.setExpenseCategory(expenseDTO.getExpenseCategory());
            }
            expenseRepository.save(expense);
        } else {
            throw new ExpenseNotFoundException("Expense not found.");
        }
        return objectMapper.convertValue(expense, ExpenseDTO.class);
    }

    @Override
    public String deleteExpense(Long id, Principal connectedUser) {
        User user = (User) ((UsernamePasswordAuthenticationToken) connectedUser).getPrincipal();
        Expense expense = expenseRepository.findById(id).orElseThrow(() -> new ExpenseNotFoundException("Expense not found."));
        Budget selectedCategoryBudget = budgetRepository.findBudgetByCategory(expense.getExpenseCategory().toString(), user.getId()).orElseThrow(() -> new BudgetNotFoundException("No budget for this expense category"));
        if (user.getId().equals(expense.getUser().getId())) {
            selectedCategoryBudget.setBudgetSum(selectedCategoryBudget.getBudgetSum() + expense.getExpenseSum());
            budgetRepository.save(selectedCategoryBudget);
            expenseRepository.deleteById(id);
        }
        return "The expense was deleted.";
    }

    @Override
    public List<ExpenseDTO> getExpenseByCategory(ExpenseCategory expenseCategory, Principal connectedUser) {
        User user = (User) ((UsernamePasswordAuthenticationToken) connectedUser).getPrincipal();
        List<Expense> expenses = expenseRepository.findExpensesByCategory(expenseCategory.toString(), user.getId());

        return expenses.stream().map(this::convertExpenseToDTO).toList();
    }

    @Override
    public List<ExpenseDTO> getExpenses(Principal connectedUser) {
        User user = (User) ((UsernamePasswordAuthenticationToken) connectedUser).getPrincipal();
        List<Expense> expenseList = expenseRepository.findAllExpensesForUser(user.getId());
        return expenseList.stream().map(this::convertExpenseToDTO).toList();
    }
    private ExpenseDTO convertExpenseToDTO(Expense expense) {
        ExpenseDTO expenseDTO = new ExpenseDTO();
        expenseDTO.setId(expense.getId());
        expenseDTO.setTitle(expense.getTitle());
        expenseDTO.setExpenseSum(expense.getExpenseSum());
        expenseDTO.setExpenseCategory(expense.getExpenseCategory());
        expenseDTO.setExpenseDate(expense.getExpenseDate());
        return expenseDTO;
    }

}