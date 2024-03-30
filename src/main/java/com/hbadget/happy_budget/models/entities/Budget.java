package com.hbadget.happy_budget.models.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.hbadget.happy_budget.models.enums.BudgetCategory;
import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "budgets")
public class Budget {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY )
    private Long id;

    @Column(name = "budget_sum")
    private double budgetSum;

    @CreationTimestamp
    @Column(name = "budget_date")
    private LocalDateTime budgetDate;

    @Column(name = "budget_category")
    @Enumerated(EnumType.STRING)
    private BudgetCategory budgetCategory;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    @JsonIgnore
    private User user;
}
