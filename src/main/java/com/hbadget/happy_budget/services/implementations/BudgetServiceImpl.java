package com.hbadget.happy_budget.services.implementations;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.hbadget.happy_budget.exceptions.BudgetNotFoundException;
import com.hbadget.happy_budget.models.dtos.BudgetDTO;
import com.hbadget.happy_budget.models.entities.Budget;
import com.hbadget.happy_budget.models.enums.BudgetCategory;
import com.hbadget.happy_budget.repositories.BudgetRepository;
import com.hbadget.happy_budget.services.interfaces.BudgetService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class BudgetServiceImpl implements BudgetService {

    private final BudgetRepository budgetRepository;
    private final ObjectMapper objectMapper;

    @Override
    public BudgetDTO createBudget(BudgetDTO budgetDTO) {
        Budget budget = budgetRepository.save(objectMapper.convertValue(budgetDTO, Budget.class));

        return objectMapper.convertValue(budget, BudgetDTO.class);
    }

    @Override
    public BudgetDTO updateBudget(BudgetDTO budgetDTO, Long id) {
        Budget budget = budgetRepository.findById(id).orElseThrow(() -> new BudgetNotFoundException("Budget not found."));

        if (budgetDTO.getBudgetSum() != 0) {
            budgetDTO.setBudgetSum(budgetDTO.getBudgetSum());
        }

        if (budgetDTO.getBudgetCategory() != null) {
            budgetDTO.setBudgetCategory(budgetDTO.getBudgetCategory());
        }

        if (budgetDTO.getBudgetDate() != null) {
            budgetDTO.setBudgetDate(budgetDTO.getBudgetDate());
        }

        return objectMapper.convertValue(budget, BudgetDTO.class);
    }

    @Override
    public String deleteBudget(Long id) {

        if (budgetRepository.existsById(id)) {
            budgetRepository.deleteById(id);

            return "Budget deleted.";
        } else {
            throw new BudgetNotFoundException("Budget not found.");
        }
    }

    @Override
    public List<BudgetDTO> getBudgetByCategory(BudgetCategory budgetCategory) {
        List<Budget> budgets = budgetRepository.findBudgetByCategory(budgetCategory);

        if (budgets.isEmpty()) {
            return Collections.emptyList();
        }
        return budgets.stream()
                .map(this::convertBudgetToDTO)
                .collect(Collectors.toList());
    }

    private BudgetDTO convertBudgetToDTO(Budget budget) {
        BudgetDTO budgetDTO = new BudgetDTO();
        budgetDTO.setBudgetCategory(budget.getBudgetCategory());
        budgetDTO.setBudgetSum(budget.getBudgetSum());

        return budgetDTO;
    }
}
