package com.hbadget.happy_budget.exceptions;

public class InvalidEmailFormatException extends RuntimeException{

    public InvalidEmailFormatException(String message){
        super(message);
    }
}
