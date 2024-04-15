package com.hbadget.happy_budget.exceptions;

public class InsufficientFundsException extends RuntimeException{

    public InsufficientFundsException(String message){
        super(message);
    }
}
