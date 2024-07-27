package org.intuit.scoreservice.controllers;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.intuit.scoreservice.models.entity.Score;
import org.intuit.scoreservice.models.dto.ScoreDTO;
import org.intuit.scoreservice.rankingstrategy.DefaultRankingStrategy;
import org.intuit.scoreservice.rankingstrategy.RankingStrategy;
import org.intuit.scoreservice.validators.ScoreValidator;
import org.intuit.scoreservice.views.RankResponse;
import org.intuit.scoreservice.views.ScoresView;
import org.intuit.scoreservice.services.ScoreService;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.io.IOException;
import java.net.Socket;
import java.util.List;
import java.util.concurrent.CompletableFuture;

@RestController
@AllArgsConstructor
@RequestMapping("/api/leaderboard")
@Slf4j
public class ScoreController {
    private final ScoreService scoreService;
    private final ScoreValidator scoreValidatorImpl;
    private final ModelMapper modelMapper;

    @PostMapping("/scores")
    public ResponseEntity<Void> publishScore(@RequestBody ScoreDTO scoreDTO) {
        try {
            Score score = modelMapper.map(scoreDTO, Score.class);
            scoreValidatorImpl.validate(score);
            CompletableFuture<Boolean> publishFuture = scoreService.publishScore(score); //mapper validation ?

            // Return a response indicating that the score publishing started for your req/intitalised.
            return ResponseEntity.accepted().build(); // 202 Accepted

        } catch (Exception e) {
            // Handle any other exceptions
            log.error("Error processing request: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping("/top")
    public ResponseEntity<ScoresView> getTopScores() {
        try {
            RankingStrategy rankingStrategy = new DefaultRankingStrategy(); //we can pass as request also
            List<RankResponse> topScores = scoreService.getTopScores(0, 5, rankingStrategy);
            for(RankResponse rank : topScores) {
                log.info(String.valueOf(rank));
            }
            return ResponseEntity.ok(new ScoresView(topScores));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping("/health")
    public ResponseEntity<String> healthCheck() {
        log.info("Health check requested");


        boolean isPortHealthy = isPortOpen(8080);

        if (!isPortHealthy) {
            log.error("Service is not healthy: No service running on port 8080");
            return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE)
                    .body("Service is down: No service running on port 8080.");
        }
        log.info("HEALTHY");
        return ResponseEntity.ok("Service is up and running!");
    }
    // Method to check if a specific port is open
    private boolean isPortOpen(int port) {
        try (Socket socket = new Socket("localhost", port)) {
            return true; // Port is open
        } catch (IOException e) {
            log.error("Port " + port + " is not open: " + e.getMessage());
            return false; // Port is not open
        }
    }
}
