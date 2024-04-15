package com.hbadget.happy_budget.services.implementations;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.hbadget.happy_budget.exceptions.BudgetNotFoundException;
import com.hbadget.happy_budget.exceptions.InsufficientFundsException;
import com.hbadget.happy_budget.models.dtos.BudgetDTO;
import com.hbadget.happy_budget.models.entities.Budget;
import com.hbadget.happy_budget.models.entities.User;
import com.hbadget.happy_budget.models.enums.BudgetCategory;
import com.hbadget.happy_budget.repositories.BudgetRepository;
import com.hbadget.happy_budget.services.interfaces.BudgetService;
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
public class BudgetServiceImpl implements BudgetService {

    private final BudgetRepository budgetRepository;
    private final ObjectMapper objectMapper;

    @Override
    public BudgetDTO createBudget(BudgetDTO budgetDTO, Principal connectedUser) {
        User user = (User) ((UsernamePasswordAuthenticationToken) connectedUser).getPrincipal();
        Budget budget = objectMapper.convertValue(budgetDTO, Budget.class);

        Budget totalBudget = budgetRepository.findTotalBudgetForUser(user.getId()).orElseThrow(() -> new BudgetNotFoundException("Budget not found."));
        List<Budget> allBudgets = budgetRepository.findAllBudgetsForUser(user.getId());
        double allCategorySum = 0;
        for (Budget budgetCategory : allBudgets) {
            if (!budgetCategory.getBudgetCategory().equals(BudgetCategory.TOTAL)) {
                allCategorySum += budgetCategory.getBudgetSum();
            }
        }

        if (budgetRepository.findBudgetByCategory(budgetDTO.getBudgetCategory().toString(), user.getId()).isEmpty()) {
            if (totalBudget.getBudgetSum() - allCategorySum >= budgetDTO.getBudgetSum()) {
                budget.setUser(user);
                Budget responseBudget = budgetRepository.save(budget);

                return objectMapper.convertValue(responseBudget, BudgetDTO.class);
            } else {
                throw new InsufficientFundsException("Insufficient funds for creating new budget.");
            }

        } else {
            throw new RuntimeException("Budget already exists.");
        }


    }

    @Override
    public BudgetDTO updateBudget(BudgetDTO budgetDTO, Long id, Principal connectedUser) {
        User user = (User) ((UsernamePasswordAuthenticationToken) connectedUser).getPrincipal();
        Budget budget = budgetRepository.findById(id).orElseThrow(() -> new BudgetNotFoundException("Budget not found."));

        if (user.getId().equals(budget.getUser().getId())) {
            if (budgetDTO.getBudgetSum() != 0) {
                budgetDTO.setBudgetSum(budgetDTO.getBudgetSum());
            }

            if (budgetDTO.getBudgetCategory() != null) {
                budgetDTO.setBudgetCategory(budgetDTO.getBudgetCategory());
            }

            if (budgetDTO.getBudgetDate() != null) {
                budgetDTO.setBudgetDate(budgetDTO.getBudgetDate());
            }
        } else {
            throw new BudgetNotFoundException("Budget not found.");
        }

        return objectMapper.convertValue(budget, BudgetDTO.class);
    }

    @Override
    public String deleteBudget(Long id, Principal connectedUser) {
        User user = (User) ((UsernamePasswordAuthenticationToken) connectedUser).getPrincipal();
        Budget budget = budgetRepository.findById(id).orElseThrow(() -> new BudgetNotFoundException("Budget not found."));
        if (budgetRepository.existsById(id)) {
            budgetRepository.deleteById(id);

            return "Budget deleted.";
        } else {
            throw new BudgetNotFoundException("Budget not found.");
        }
    }

    @Override
    public BudgetDTO getBudgetByCategory(BudgetCategory budgetCategory, Principal connectedUser) {
        User user = (User) ((UsernamePasswordAuthenticationToken) connectedUser).getPrincipal();
        Budget budget = budgetRepository.findBudgetByCategory(budgetCategory.toString(), user.getId()).orElseThrow(() -> new BudgetNotFoundException("Budget not found."));

        return objectMapper.convertValue(budget, BudgetDTO.class);
    }

    private BudgetDTO convertBudgetToDTO(Budget budget) {
        BudgetDTO budgetDTO = new BudgetDTO();
        budgetDTO.setBudgetCategory(budget.getBudgetCategory());
        budgetDTO.setBudgetSum(budget.getBudgetSum());

        return budgetDTO;
    }
}
