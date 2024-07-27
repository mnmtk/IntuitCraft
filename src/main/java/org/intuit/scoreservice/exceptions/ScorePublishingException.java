package org.intuit.scoreservice.exceptions;

public class ScorePublishingException extends RuntimeException {
    public ScorePublishingException(String message, Throwable cause) {
        super(message, cause);
    }
}
