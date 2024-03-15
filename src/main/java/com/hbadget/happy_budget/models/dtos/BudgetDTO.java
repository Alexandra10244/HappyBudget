package com.hbadget.happy_budget.models.dtos;

import lombok.Data;


@Data
public class BudgetDTO {
    private Long id;
    private int budgetSum;
    private String budgetCategory;
}
