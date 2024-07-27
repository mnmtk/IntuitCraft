package org.intuit.scoreservice.exceptions;

public class InvalidScoreException extends RuntimeException {
    public InvalidScoreException(Long score) {
        super("Invalid score: " + score);
    }
}
