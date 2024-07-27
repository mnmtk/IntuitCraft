package org.intuit.scoreservice.exceptions;

public class PlayerNotFoundException extends RuntimeException {
    public PlayerNotFoundException(Long playerId) {
        super("Player not found with ID: " + playerId + "invalid id was recieved");
    }
}
