package org.intuit.scoreservice.views;

import lombok.Data;

import java.util.List;

@Data
public class ScoresView {
    private List<RankResponse> topScores;

    public ScoresView(List<RankResponse> topScores) {
        this.topScores = topScores;
    }

}
