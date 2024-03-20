package com.hbadget.happy_budget.exceptions;

public class IncomeNotFoundException extends RuntimeException{

    public IncomeNotFoundException(String message){
        super(message);
    }
}
