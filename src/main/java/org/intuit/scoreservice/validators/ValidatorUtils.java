package org.intuit.scoreservice.validators;

import org.intuit.scoreservice.exceptions.PlayerNotFoundException;
import org.intuit.scoreservice.models.entity.Player;

public final class ValidatorUtils {

    // Private constructor to prevent instantiation
    private ValidatorUtils() {
        throw new IllegalStateException("Utility class should not be instantiated");
    }

    public static Player validatePlayer(Player player) {
        if (player == null) {
            throw new IllegalArgumentException("Player cannot be null");
        }
        if (player.getUsername() == null || player.getUsername().isEmpty()) {
            throw new IllegalArgumentException("Player name cannot be null or empty");
        }
        if (player.getId() == null) {
            throw new IllegalArgumentException("Player ID cannot be null");
        }
        // Add additional validations as needed
        return player;
    }

}