package org.intuit.scoreservice.validators;

import org.intuit.scoreservice.exceptions.InvalidRequestException;
import org.intuit.scoreservice.models.entity.Player;
import org.springframework.stereotype.Component;

@Component
public class PlayerValidatorImpl implements PlayerValidator {

    @Override
    public void validate(Player player) throws Exception {
        if (player == null) {
            throw new InvalidRequestException("Player cannot be null");
        }

        String username = player.getUsername();
        if (username == null || username.isEmpty() || containsNumbers(username)) {
            throw new InvalidRequestException("Player name cannot be null, empty, or numeric");
        }

        // Add additional validations as needed
    }

    // Helper method to check if a string contains numeric characters
    private boolean containsNumbers(String str) {
        return str.matches(".*\\d.*"); // Regex to check for any digit
    }

}
