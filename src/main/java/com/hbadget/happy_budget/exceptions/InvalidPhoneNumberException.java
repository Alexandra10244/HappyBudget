package com.hbadget.happy_budget.exceptions;

public class InvalidPhoneNumberException extends RuntimeException{

    public InvalidPhoneNumberException(String message){
        super(message);
    }
}
