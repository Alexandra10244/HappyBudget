package com.hbadget.happy_budget.exceptions;


public class BudgetNotFoundException extends RuntimeException{
    public BudgetNotFoundException(String message){
        super(message);
    }
}
