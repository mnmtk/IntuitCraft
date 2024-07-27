package org.intuit.scoreservice.rankingstrategy;

import org.intuit.scoreservice.models.entity.Player;
import org.intuit.scoreservice.models.entity.Score;
import org.intuit.scoreservice.views.RankResponse;

import java.util.*;

public class DefaultRankingStrategy implements RankingStrategy {
    @Override
    public List<RankResponse> rank(List<Score> scores, List<Player> players) {
        Map<Long, List<Score>> scoreMap = new HashMap<>();

        // Group scores by playerId
        for (Score score : scores) {
            scoreMap.computeIfAbsent(score.getPlayerId(), k -> new ArrayList<>()).add(score);
        }

        List<RankResponse> rankResponses = new ArrayList<>();
        for (Map.Entry<Long, List<Score>> entry : scoreMap.entrySet()) {
            Long playerId = entry.getKey();
            List<Score> playerScores = entry.getValue();
            Long score = playerScores.stream().mapToLong(Score::getScore).sum();

            Player player = players.stream()
                    .filter(p -> p.getId().equals(playerId))
                    .findFirst()
                    .orElse(null);

            if (player != null && player.getUsername() != null) {
                rankResponses.add(RankResponse.builder()
                        .username(player.getUsername())
                        .score(score)
                        .build());
            }

        }
        //redis sorts lexicographically by default
        return rankResponses;
    }
}
