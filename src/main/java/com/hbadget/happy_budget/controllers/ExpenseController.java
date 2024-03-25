package com.hbadget.happy_budget.controllers;

import com.hbadget.happy_budget.models.dtos.ExpenseDTO;
import com.hbadget.happy_budget.models.enums.ExpenseCategory;
import com.hbadget.happy_budget.services.interfaces.ExpenseService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Validated
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/expenses")
public class ExpenseController {

    private ExpenseService expenseService;

    @PostMapping
    public ResponseEntity<ExpenseDTO> createExpense(@RequestBody ExpenseDTO expenseDTO) {
        return ResponseEntity.ok(expenseService.createExpense(expenseDTO));
    }

    @PatchMapping("/{id}")
    public ResponseEntity<ExpenseDTO> updateExpense(@Valid @RequestBody ExpenseDTO expenseDTO, @PathVariable Long id) {
        return ResponseEntity.ok(expenseService.updateExpense(expenseDTO, id));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteExpense(@PathVariable Long id) {
        return ResponseEntity.ok(expenseService.deleteExpense(id));
    }

    @GetMapping("/{expenseCategory}")
    public ResponseEntity<List<ExpenseDTO>> getExpenseByCategory(@PathVariable ExpenseCategory expenseCategory) {
        return ResponseEntity.ok(expenseService.getExpenseByCategory(expenseCategory));
    }
}

