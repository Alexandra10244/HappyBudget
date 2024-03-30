package com.hbadget.happy_budget.services.interfaces;

import com.hbadget.happy_budget.models.dtos.BudgetDTO;
import com.hbadget.happy_budget.models.enums.BudgetCategory;

import java.security.Principal;
import java.util.List;

public interface BudgetService {

    BudgetDTO createBudget(BudgetDTO budgetDTO, Principal connectedUser);

    BudgetDTO updateBudget(BudgetDTO budgetDTO, Long id, Principal connectedUser);

    String deleteBudget(Long id, Principal connectedUser);

    List<BudgetDTO> getBudgetByCategory(BudgetCategory budgetCategory, Principal connectedUser);

}
