package com.hbadget.happy_budget.services.interfaces;

import com.hbadget.happy_budget.models.dtos.ExpenseDTO;

public interface ExpenseService {

    ExpenseDTO createExpense(ExpenseDTO expenseDTO);
    ExpenseDTO updateExpense(ExpenseDTO expenseDTO, Long id);
    ExpenseDTO deleteIncome(Long id);
}
