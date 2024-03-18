package com.hbadget.happy_budget.models.entities;

import com.hbadget.happy_budget.models.enums.IncomeCategory;
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
    private double incomeSum;

    @Column(name = "income_date")
    private LocalDateTime incomeDate;

    @Column(name = "income_category")
    @Enumerated(EnumType.STRING)
    private IncomeCategory incomeCategory;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;
}
