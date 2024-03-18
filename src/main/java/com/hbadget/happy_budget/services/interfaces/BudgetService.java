package com.hbadget.happy_budget.services.interfaces;

import com.hbadget.happy_budget.models.dtos.BudgetDTO;
import com.hbadget.happy_budget.models.enums.BudgetCategory;

public interface BudgetService {

    BudgetDTO createBudget(BudgetDTO budgetDTO);
    BudgetDTO updateBudget(BudgetDTO budgetDTO, Long id);
    BudgetDTO deleteBudget(Long id);
    BudgetDTO getBudgetByCategory(BudgetCategory budgetCategory);

}
