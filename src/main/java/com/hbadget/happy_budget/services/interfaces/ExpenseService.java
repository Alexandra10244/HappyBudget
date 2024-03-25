package com.hbadget.happy_budget.services.interfaces;

import com.hbadget.happy_budget.models.dtos.ExpenseDTO;
import com.hbadget.happy_budget.models.enums.ExpenseCategory;

import java.util.List;

public interface ExpenseService {

    ExpenseDTO createExpense(ExpenseDTO expenseDTO);

    ExpenseDTO updateExpense(ExpenseDTO expenseDTO, Long id);

    String deleteExpense(Long id);

   List<ExpenseDTO> getExpenseByCategory(ExpenseCategory expenseCategory);
}
