package org.intuit.scoreservice.repositories;

import io.lettuce.core.api.sync.RedisCommands;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.intuit.scoreservice.models.entity.Score;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Repository;
import java.util.ArrayList;
import java.util.List;

@Repository
@AllArgsConstructor
@Slf4j
public class ScoreRepository {
    @Autowired
    @Qualifier("RedisCommands")
    private final RedisCommands<String, String> commands;


    public void submitScore(Score score) {
        Double currentScore = commands.zscore("leaderboard", score.getPlayerId().toString());
        if (currentScore == null || score.getScore() > currentScore) {
            commands.zadd("leaderboard", score.getScore(), score.getPlayerId().toString());
            log.info("Updated new high score for player " + score.getPlayerId() + ": " + score);
        }
    }
    public List<Score> getTopScores(int start, int end) {
        List<Score> scoreMessages = new ArrayList<>();
        try {
            var scores = commands.zrevrangeWithScores("leaderboard", start, end);
            //out of bound exception ?
            scores.forEach(tuple -> {
                Score score = new Score();
                score.setScore((long) tuple.getScore());
                score.setPlayerId(Long.parseLong(tuple.getValue()));
                scoreMessages.add(score);
            });
        } catch (Exception e) {

        } finally {
            return scoreMessages;
        }
    }

    public Long getScoreForPlayerId(Long playerId) {
        try {
            var score = commands.get(String.valueOf(playerId));
            return Long.parseLong(score);
        } catch (Exception e) {
            return null;
        }
    }
}