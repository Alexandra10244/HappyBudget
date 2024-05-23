package com.hbadget.happy_budget.controllers;

import com.hbadget.happy_budget.models.dtos.BudgetDTO;
import com.hbadget.happy_budget.models.enums.BudgetCategory;
import com.hbadget.happy_budget.services.interfaces.BudgetService;
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
@RequestMapping("/api/budgets")
public class BudgetController {

    private final BudgetService budgetService;

    @PostMapping
    public ResponseEntity<BudgetDTO> createBudget(@RequestBody BudgetDTO budgetDTO, Principal connectedUser) {
        return ResponseEntity.ok(budgetService.createBudget(budgetDTO,connectedUser));
    }

    @PatchMapping("/{id}")
    public ResponseEntity<BudgetDTO> updateBudget(@Valid @RequestBody BudgetDTO budgetDTO, @PathVariable Long id,Principal connectedUser) {
        return ResponseEntity.ok(budgetService.updateBudget(budgetDTO, id,connectedUser));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteBudget(@PathVariable Long id,Principal connectedUser) {
        return ResponseEntity.ok(budgetService.deleteBudget(id,connectedUser));
    }

    @GetMapping("/{budgetCategory}")
    public ResponseEntity<List<BudgetDTO>> getBudgetByCategory(@PathVariable BudgetCategory budgetCategory,Principal connectedUser) {
        return ResponseEntity.ok(budgetService.getBudgetByCategory(budgetCategory,connectedUser));
    }

    @GetMapping()
    public ResponseEntity<List<BudgetDTO>> getBudgetsForUser(Principal connectedUser) {
        return ResponseEntity.ok(budgetService.getBudgetsForUser(connectedUser));
    }

}

