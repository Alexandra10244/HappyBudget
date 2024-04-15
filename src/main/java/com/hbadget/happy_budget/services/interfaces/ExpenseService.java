package com.hbadget.happy_budget.services.interfaces;

import com.hbadget.happy_budget.models.dtos.ExpenseDTO;
import com.hbadget.happy_budget.models.enums.ExpenseCategory;

import java.security.Principal;
import java.util.List;

public interface ExpenseService {

    ExpenseDTO createExpense(ExpenseDTO expenseDTO, Principal connectedUser);

    ExpenseDTO updateExpense(ExpenseDTO expenseDTO, Long id,Principal connectedUser);

    String deleteExpense(Long id, Principal connectedUser);

   ExpenseDTO getExpenseByCategory(ExpenseCategory expenseCategory, Principal connectedUser);
}
