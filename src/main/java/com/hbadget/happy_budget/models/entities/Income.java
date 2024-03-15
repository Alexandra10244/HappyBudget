package com.hbadget.happy_budget.models.entities;

import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "incomes")
public class Income {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "income_sum")
    private int incomeSum;

    @Column(name = "income_date")
    private LocalDateTime incomeDate;

    @Column(name = "income_category")
    private String incomeCategory;
}
