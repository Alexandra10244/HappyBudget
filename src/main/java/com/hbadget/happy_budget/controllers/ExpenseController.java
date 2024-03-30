package com.hbadget.happy_budget.controllers;

import com.hbadget.happy_budget.models.dtos.ExpenseDTO;
import com.hbadget.happy_budget.models.enums.ExpenseCategory;
import com.hbadget.happy_budget.services.interfaces.ExpenseService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;

@Validated
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/expenses")
public class ExpenseController {

    private final ExpenseService expenseService;

    @PostMapping
    public ResponseEntity<ExpenseDTO> createExpense(@RequestBody ExpenseDTO expenseDTO,Principal connectedUser) {
        return ResponseEntity.ok(expenseService.createExpense(expenseDTO,connectedUser));
    }

    @PatchMapping("/{id}")
    public ResponseEntity<ExpenseDTO> updateExpense(@Valid @RequestBody ExpenseDTO expenseDTO, @PathVariable Long id, Principal connectedUser) {
        return ResponseEntity.ok(expenseService.updateExpense(expenseDTO, id,connectedUser));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteExpense(@PathVariable Long id,Principal connectedUser) {
        return ResponseEntity.ok(expenseService.deleteExpense(id,connectedUser));
    }

    @GetMapping("/{expenseCategory}")
    public ResponseEntity<List<ExpenseDTO>> getExpenseByCategory(@PathVariable ExpenseCategory expenseCategory,Principal connectedUser) {
        return ResponseEntity.ok(expenseService.getExpenseByCategory(expenseCategory,connectedUser));
    }
}

