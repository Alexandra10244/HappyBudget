package com.hbadget.happy_budget.models.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.hbadget.happy_budget.models.enums.IncomeCategory;
import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;

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

    @CreationTimestamp
    @Column(name = "income_date")
    private LocalDateTime incomeDate;

    @Column(name = "income_category")
    @Enumerated(EnumType.STRING)
    private IncomeCategory incomeCategory;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    @JsonIgnore
    private User user;
}
