package com.hbadget.happy_budget.models.entities;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name = "budgets")
public class Budget {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY )
    private Long id;

    @Column(name = "budget_sum")
    private int budgetSum;

    @Column(name = "budget_category")
    private String budgetCategory;
}
