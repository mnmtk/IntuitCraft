package org.intuit.scoreservice.validators;

import org.intuit.scoreservice.exceptions.InvalidScoreException;
import org.intuit.scoreservice.exceptions.PlayerNotFoundException;
import org.intuit.scoreservice.models.entity.Player;
import org.intuit.scoreservice.models.entity.Score;
import org.intuit.scoreservice.repositories.PlayerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class ScoreValidatorImpl implements ScoreValidator {

    @Autowired
    PlayerRepository playerRepository;

    @Override
    public void validate(Score score) throws Exception {

        try {
            Player player = getPlayerInfo(score.getPlayerId());
        } catch (Exception e) {
            throw new PlayerNotFoundException(score.getPlayerId());
        }

        if (score.getScore() == null) {
            throw new InvalidScoreException(score.getScore());
        }

    }


    private Player getPlayerInfo(Long playerId) {
        return playerRepository.findById(playerId)
                .map(ValidatorUtils::validatePlayer)
                .orElseThrow(() -> new PlayerNotFoundException(playerId));
    }
}