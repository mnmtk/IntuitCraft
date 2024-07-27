package org.intuit.scoreservice.services;

import org.intuit.scoreservice.exceptions.PlayerNotFoundException;
import org.intuit.scoreservice.models.entity.Player;
import org.intuit.scoreservice.models.entity.Score;
import org.intuit.scoreservice.repositories.PlayerRepository;
import org.intuit.scoreservice.repositories.ScoreRepository;
import org.intuit.scoreservice.messagepublishers.MessageQueuePublisher;
import org.intuit.scoreservice.rankingstrategy.RankingStrategy;
import org.intuit.scoreservice.views.RankResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.List;
import java.util.concurrent.CompletableFuture;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class ScoreServiceTests {

    @Mock
    private ScoreRepository scoreRepository;

    @Mock
    private PlayerRepository playerRepository;

    @Mock
    private MessageQueuePublisher messageQueuePublisher;

    @Mock
    private RankingStrategy rankingStrategy;

    @InjectMocks
    private ScoreService scoreService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testSubmitScore_Success() throws PlayerNotFoundException {
        // Given
        Long playerId = 1L;
        Score score = new Score(playerId, 100L);
        Player player = new Player(playerId, "Player1");

        when(playerRepository.getReferenceById(playerId)).thenReturn(player);
        when(scoreRepository.getScoreForPlayerId(playerId)).thenReturn(50L);

        // When
        scoreService.submitScore(score);

        // Then
        verify(scoreRepository, times(1)).submitScore(score);
    }

    @Test
    void testSubmitScore_PlayerNotFound() {
        // Given
        Long playerId = 1L;
        Score score = new Score(playerId, 100L);

        when(playerRepository.getReferenceById(playerId)).thenThrow(new PlayerNotFoundException(playerId));

        // When & Then
        assertThrows(PlayerNotFoundException.class, () -> scoreService.submitScore(score));
    }

    @Test
    void testPublishScore() {
        // Given
        Score score = new Score(1L, 100L);
        CompletableFuture<Boolean> future = CompletableFuture.completedFuture(true);

        when(messageQueuePublisher.publish(score)).thenReturn(future);

        // When
        CompletableFuture<Boolean> result = scoreService.publishScore(score);

        // Then
        assertEquals(future, result);
        verify(messageQueuePublisher, times(1)).publish(score);
    }

    @Test
    void testGetTopScores() {
        // Given
        int start = 0;
        int end = 10;
        Score score1 = new Score(1L, 100L);
        Score score2 = new Score(2L, 200L);
        List<Score> topScores = List.of(score1, score2);

        Player player1 = new Player(1L, "Player1");
        Player player2 = new Player(2L, "Player2");
        List<Player> players = List.of(player1, player2);

        RankResponse rankResponse1 = new RankResponse( player1.getUsername(), player1.getId());
        RankResponse rankResponse2 = new RankResponse(player2.getUsername(), player2.getId());
        List<RankResponse> rankResponses = List.of(rankResponse1, rankResponse2);

        when(scoreRepository.getTopScores(start, end)).thenReturn(topScores);
        when(playerRepository.getReferenceById(player1.getId())).thenReturn(player1);
        when(playerRepository.getReferenceById(player2.getId())).thenReturn(player2);
        when(rankingStrategy.rank(topScores, players)).thenReturn(rankResponses);

        // When
        List<RankResponse> result = scoreService.getTopScores(start, end, rankingStrategy);

        // Then
        assertEquals(rankResponses, result);
    }
}