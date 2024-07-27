package org.intuit.scoreservice.exceptions;

public class ScoreConsumptionException extends RuntimeException {
    public ScoreConsumptionException(String message, Throwable cause) {
        super(message, cause);
    }
}
