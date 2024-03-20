package com.hbadget.happy_budget.services.interfaces;

import com.hbadget.happy_budget.models.dtos.IncomeDTO;
import com.hbadget.happy_budget.models.enums.IncomeCategory;

import java.util.List;

public interface IncomeService {

    IncomeDTO createIncome(IncomeDTO incomeDTO);

    IncomeDTO updateIncome(IncomeDTO incomeDTO, Long id);

    String deleteIncome(Long id);

    List<IncomeDTO> getIncomeByCategory(IncomeCategory incomeCategory);
}
