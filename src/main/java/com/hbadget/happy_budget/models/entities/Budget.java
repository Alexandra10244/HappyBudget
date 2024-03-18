package com.hbadget.happy_budget.models.entities;

import com.hbadget.happy_budget.models.enums.BudgetCategory;
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
    private double budgetSum;

    @Column(name = "budget_category")
    @Enumerated(EnumType.STRING)
    private BudgetCategory budgetCategory;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;
}
