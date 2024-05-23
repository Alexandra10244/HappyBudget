package com.hbadget.happy_budget.models.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.hbadget.happy_budget.models.enums.ExpenseCategory;
import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "expenses")
public class Expense {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;

    @Column(name = "expense_sum")
    private double expenseSum;

    @CreationTimestamp
    @Column(name = "expense_date")
    private LocalDateTime expenseDate;

    @Column(name = "expense_category")
    private ExpenseCategory expenseCategory;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    @JsonIgnore
    private User user;
}
