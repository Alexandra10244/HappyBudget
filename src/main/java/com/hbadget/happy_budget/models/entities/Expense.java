package com.hbadget.happy_budget.models.entities;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "expenses")
public class Expense {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "expense_sum")
    private int expenseSum;

    @Column(name = "expense_date")
    private LocalDateTime expenseDate;

    @Column(name = "expense_category")
    private String expenseCategory;
}
