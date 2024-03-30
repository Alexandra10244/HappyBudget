package com.hbadget.happy_budget.controllers;

import com.hbadget.happy_budget.models.dtos.IncomeDTO;
import com.hbadget.happy_budget.models.enums.IncomeCategory;
import com.hbadget.happy_budget.services.interfaces.IncomeService;
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
@RequestMapping("/api/incomes")
public class IncomeController {

    private final IncomeService incomeService;

    @PostMapping
    public ResponseEntity<IncomeDTO> createIncome(@RequestBody IncomeDTO incomeDTO,Principal connectedUser) {
        return ResponseEntity.ok(incomeService.createIncome(incomeDTO, connectedUser));
    }

    @PatchMapping("/{id}")
    public ResponseEntity<IncomeDTO> updateIncome(@Valid @RequestBody IncomeDTO incomeDTO, @PathVariable Long id, Principal connectedUser) {
        return ResponseEntity.ok(incomeService.updateIncome(incomeDTO, id, connectedUser));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteIncome(@PathVariable Long id, Principal connectedUser) {
        return ResponseEntity.ok(incomeService.deleteIncome(id, connectedUser));
    }

    @GetMapping("/{incomeCategory}")
    public ResponseEntity<List<IncomeDTO>> getIncomeByCategory(@PathVariable IncomeCategory incomeCategory, Principal connectedUser) {
        return ResponseEntity.ok(incomeService.getIncomeByCategory(incomeCategory, connectedUser));
    }
}
