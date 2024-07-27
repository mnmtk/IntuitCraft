package org.intuit.scoreservice.rankingstrategy;

import org.intuit.scoreservice.models.entity.Player;
import org.intuit.scoreservice.models.entity.Score;
import org.intuit.scoreservice.views.RankResponse;

import java.util.List;

public interface RankingStrategy {
    List<RankResponse> rank(List<Score> scores, List<Player> players);
}
