package com.hbadget.happy_budget.services.interfaces;

import com.hbadget.happy_budget.models.dtos.IncomeDTO;

public interface IncomeService {

    IncomeDTO createIncome(IncomeDTO incomeDTO);
    IncomeDTO updateIncome(IncomeDTO incomeDTO, Long id);
    IncomeDTO deleteIncome(Long id);
}
