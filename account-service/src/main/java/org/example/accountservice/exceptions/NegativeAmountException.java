package org.example.accountservice.exceptions;

public class NegativeAmountException extends Throwable {
    public NegativeAmountException(String message) {
        super(message);
    }
}
