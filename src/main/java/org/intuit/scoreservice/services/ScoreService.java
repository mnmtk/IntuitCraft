package org.intuit.scoreservice.services;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.intuit.scoreservice.exceptions.PlayerNotFoundException;
import org.intuit.scoreservice.messagepublishers.MessageQueuePublisher;
import org.intuit.scoreservice.models.entity.Player;
import org.intuit.scoreservice.models.entity.Score;
import org.intuit.scoreservice.rankingstrategy.RankingStrategy;
import org.intuit.scoreservice.repositories.PlayerRepository;
import org.intuit.scoreservice.repositories.ScoreRepository;
import org.intuit.scoreservice.views.RankResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import java.util.List;
import org.intuit.scoreservice.validators.ValidatorUtils;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
@Lazy
@Slf4j
public class ScoreService {

    private final ScoreRepository scoreRepository;
    private final PlayerRepository playerRepository;

    @Autowired
    private final MessageQueuePublisher messageQueuePublisher;



    @Async
    public CompletableFuture<Boolean> publishScore(Score score) {
        return messageQueuePublisher.publish(score);
    }


    @Async
    public void submitScore(Score score) throws PlayerNotFoundException {
        try {
            Player player = getPlayerInfo(score.getPlayerId());
            Long existingScore = scoreRepository.getScoreForPlayerId(score.getPlayerId());
            if (existingScore == null || score.getScore() > existingScore) {
                scoreRepository.submitScore(score);
            }
        }
        catch (Exception e) {
           throw e;
        }
    }


    public List<RankResponse> getTopScores(int start, int end, RankingStrategy rankingStrategy) {
        List<Score> topScores = scoreRepository.getTopScores(start, end);

        List<Long> playerIds = topScores.stream()
                .map(Score::getPlayerId)
                .distinct()
                .collect(Collectors.toList());

        List<Player> players = playerIds.stream()
                .map(this::getPlayerInfo)
                .collect(Collectors.toList());

        return rankingStrategy.rank(topScores, players);
    }


    private Player getPlayerInfo(Long playerId) {
        return playerRepository.findById(playerId)
                .map(ValidatorUtils::validatePlayer)
                .orElseThrow(() -> new PlayerNotFoundException(playerId));
    }


}
