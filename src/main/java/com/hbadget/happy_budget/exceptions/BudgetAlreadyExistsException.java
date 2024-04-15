package com.hbadget.happy_budget.exceptions;

public class BudgetAlreadyExistsException extends RuntimeException{

    public BudgetAlreadyExistsException(String message){
        super(message);
    }
}
