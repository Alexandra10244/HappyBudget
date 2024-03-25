package com.hbadget.happy_budget.controllers;

import com.hbadget.happy_budget.models.dtos.BudgetDTO;
import com.hbadget.happy_budget.models.dtos.ExpenseDTO;
import com.hbadget.happy_budget.models.dtos.IncomeDTO;
import com.hbadget.happy_budget.models.dtos.ReportDTO;
import com.hbadget.happy_budget.services.interfaces.ExpenseService;
import com.hbadget.happy_budget.services.interfaces.IncomeService;
import com.hbadget.happy_budget.services.interfaces.ReportService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.util.List;

@Validated
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/reports")
public class ReportController {
    private IncomeService incomeService;
    private ExpenseService expenseService;
    private ReportService reportService;

    @GetMapping("/income/{date}/{id}")
    public ResponseEntity<List<IncomeDTO>> getAllIncomesByDate(@PathVariable LocalDate startDate,@PathVariable LocalDate endDate, @PathVariable Long id){
        return ResponseEntity.ok(reportService.getAllIncomesByDate(startDate, endDate, id));
    }

    @GetMapping("/expense/{date}/{id}")
    public ResponseEntity<List<ExpenseDTO>> getAllExpensesByDate(@PathVariable LocalDate startDate,@PathVariable LocalDate endDate, @PathVariable Long id){
        return ResponseEntity.ok(reportService.getAllExpensesByDate(startDate, endDate, id));
    }

    @GetMapping("/budget/{date}/{id}")
    public ResponseEntity<List<BudgetDTO>> getAllBudgetsByDate(@PathVariable LocalDate startDate,@PathVariable LocalDate endDate, @PathVariable Long id){
        return ResponseEntity.ok(reportService.getAllBudgetsByDate(startDate, endDate, id));
    }

    @GetMapping("/report/{date}/{id}")
    public ResponseEntity<List<ReportDTO>> getAllExpensesIncomesByDate(@PathVariable LocalDate startDate,@PathVariable LocalDate endDate, @PathVariable Long id){
        return ResponseEntity.ok(reportService.getAllExpensesIncomesByDate(startDate, endDate, id));
    }

}
