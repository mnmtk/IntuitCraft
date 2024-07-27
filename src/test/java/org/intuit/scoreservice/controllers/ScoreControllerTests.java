package org.intuit.scoreservice.controllers;

import junit.framework.Assert;
import org.intuit.scoreservice.models.dto.ScoreDTO;
import org.intuit.scoreservice.models.entity.Score;
import org.intuit.scoreservice.rankingstrategy.DefaultRankingStrategy;
import org.intuit.scoreservice.services.ScoreService;
import org.intuit.scoreservice.views.RankResponse;
import org.intuit.scoreservice.views.ScoresView;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.ArrayList;
import java.util.List;


@ExtendWith(MockitoExtension.class)
class ScoreControllerTests {
    @Mock
    private ScoreService scoreService;

    @Mock
    private ModelMapper modelMapper;

    @InjectMocks
    private ScoreController scoreController;

    @Test
    void testPublishScore() {
        ScoreDTO scoreDTO = new ScoreDTO();
        scoreDTO.setPlayerId(1L);
        scoreDTO.setScoreValue(100L);

        Score score = new Score();
        score.setPlayerId(1L);
        score.setScore(100L);

        Mockito.when(modelMapper.map(scoreDTO, Score.class)).thenReturn(score);

        ResponseEntity<Void> response = scoreController.publishScore(scoreDTO);

        Assert.assertEquals(HttpStatus.ACCEPTED, response.getStatusCode());
        Mockito.verify(scoreService.publishScore(score));
    }

    @Test
    void testGetTopScores() {
        List<RankResponse> topScores = new ArrayList<>();
        topScores.add(new RankResponse("testuser1", 200L));
        topScores.add(new RankResponse("testuser2", 150L));

        Mockito.when(scoreService.getTopScores(0, 5, new DefaultRankingStrategy())).thenReturn(topScores);

        ResponseEntity<ScoresView> response = scoreController.getTopScores();

        Assert.assertEquals(HttpStatus.OK, response.getStatusCode());
        Assert.assertEquals(topScores, response.getBody().getTopScores());
    }
}
