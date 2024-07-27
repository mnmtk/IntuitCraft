package org.intuit.scoreservice.exceptions;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class InvalidRequestException extends RuntimeException {
    public InvalidRequestException(String message) {
        super(message + "invalid request");
    }
}
