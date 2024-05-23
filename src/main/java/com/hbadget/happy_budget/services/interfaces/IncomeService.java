package com.hbadget.happy_budget.services.interfaces;

import com.hbadget.happy_budget.models.dtos.IncomeDTO;
import com.hbadget.happy_budget.models.enums.IncomeCategory;

import java.security.Principal;
import java.util.List;

public interface IncomeService {

    IncomeDTO createIncome(IncomeDTO incomeDTO, Principal connectedUser);

    IncomeDTO updateIncome(IncomeDTO incomeDTO, Long id, Principal connectedUser);

    String deleteIncome(Long id,Principal connectedUser);

    List<IncomeDTO> getIncomeByCategory(IncomeCategory incomeCategory, Principal connectedUser);

    List<IncomeDTO> getAllIncomesForUser(Principal connectedUser);
}
