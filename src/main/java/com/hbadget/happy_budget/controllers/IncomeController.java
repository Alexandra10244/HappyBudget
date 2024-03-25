package com.hbadget.happy_budget.controllers;

import com.hbadget.happy_budget.models.dtos.IncomeDTO;
import com.hbadget.happy_budget.models.enums.IncomeCategory;
import com.hbadget.happy_budget.services.interfaces.IncomeService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Validated
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/incomes")
public class IncomeController {

    private IncomeService incomeService;

    @PostMapping
    public ResponseEntity<IncomeDTO> createIncome(@RequestBody IncomeDTO incomeDTO) {
        return ResponseEntity.ok(incomeService.createIncome(incomeDTO));
    }

    @PatchMapping("/{id}")
    public ResponseEntity<IncomeDTO> updateIncome(@Valid @RequestBody IncomeDTO incomeDTO, @PathVariable Long id) {
        return ResponseEntity.ok(incomeService.updateIncome(incomeDTO, id));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteIncome(@PathVariable Long id) {
        return ResponseEntity.ok(incomeService.deleteIncome(id));
    }

    @GetMapping("/{incomeCategory}")
    public ResponseEntity<List<IncomeDTO>> getIncomeByCategory(@PathVariable IncomeCategory incomeCategory) {
        return ResponseEntity.ok(incomeService.getIncomeByCategory(incomeCategory));
    }
}
