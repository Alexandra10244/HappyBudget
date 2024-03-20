package com.hbadget.happy_budget.services.interfaces;

import com.hbadget.happy_budget.models.dtos.BudgetDTO;
import com.hbadget.happy_budget.models.enums.BudgetCategory;

import java.util.List;

public interface BudgetService {

    BudgetDTO createBudget(BudgetDTO budgetDTO);

    BudgetDTO updateBudget(BudgetDTO budgetDTO, Long id);

    String deleteBudget(Long id);

    List<BudgetDTO> getBudgetByCategory(BudgetCategory budgetCategory);

}
