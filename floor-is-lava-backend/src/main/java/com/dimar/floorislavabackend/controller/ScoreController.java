package com.dimar.floorislavabackend.controller;

import com.dimar.floorislavabackend.model.Player;
import com.dimar.floorislavabackend.model.Score;
import com.dimar.floorislavabackend.repository.PlayerRepository;
import com.dimar.floorislavabackend.service.ScoreService;
import com.dimar.floorislavabackend.service.PlayerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@RestController
@RequestMapping("/api/scores")
public class ScoreController {
    @Autowired
    private ScoreService scoreService;
    @Autowired
    private PlayerService playerService;

    public ScoreController(ScoreService scoreService) {
        this.scoreService = scoreService;
    }

    @GetMapping
    ResponseEntity<List<Score>> getAllScores() {
        List<Score> semuaScore = scoreService.getAllScores();
        return ResponseEntity.status(HttpStatus.OK).body(semuaScore);
    }

    @PostMapping
    ResponseEntity<?> createScore(@RequestBody Score score) {
        try {
            Optional<Player> player = playerService.getPlayerById(score.getPlayerId());
            if (player.isPresent()) {
                Score scoreTerbuat = scoreService.createScore(score);
                return ResponseEntity.status(HttpStatus.CREATED).body(scoreTerbuat);
            }else {
                throw new RuntimeException("Player doesn't exist");
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    @GetMapping("/{scoreId}")
    ResponseEntity<?> getScoreById(@PathVariable UUID scoreId) {
        try {
            Optional<Score> score = scoreService.getScoreById(scoreId);
            return ResponseEntity.status(HttpStatus.OK).body(score.get());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

    @PutMapping("/{scoreId}")
    ResponseEntity<?> updateScore(@PathVariable UUID scoreId,  @RequestBody Score score) {
        try {
            Score updatedScore = scoreService.updateScore(scoreId, score);
            return ResponseEntity.status(HttpStatus.OK).body(updatedScore);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    @DeleteMapping("/{scoreId}")
    ResponseEntity<?> deleteScore(@PathVariable UUID scoreId) {
        try {
            scoreService.deleteScore(scoreId);
            return ResponseEntity.status(HttpStatus.OK).build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    @GetMapping("/player/{playerId}")
    ResponseEntity<?> getScoresByPlayerId(@PathVariable UUID playerId) {
        System.out.println("cek " + playerId);
        try {
            List<Score> scores = scoreService.getScoresByPlayerId(playerId);
            return ResponseEntity.status(HttpStatus.OK).body(scores);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

    @GetMapping("/player/{playerId}/ordered")
    ResponseEntity<?> getScoresByPlayerIdOrdered(@PathVariable UUID playerId) {
        try {
            List<Score> scores = scoreService.getScoresByPlayerIdOrderByValue(playerId);
            return ResponseEntity.status(HttpStatus.OK).body(scores);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

    @GetMapping("/leaderboard")
    ResponseEntity<?> getLeaderboard(@RequestParam(defaultValue = "10") int limit) {
        try {
            List<Score> scores = scoreService.getLeaderboard(limit);
            return ResponseEntity.status(HttpStatus.OK).body(scores);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

    @GetMapping("/player/{playerId}/highest")
    ResponseEntity<?> getHighestScoreByPlayerId(@PathVariable UUID playerId) {
        try {
            Optional<Score> scores = scoreService.getHighestScoreByPlayerId(playerId);
            return ResponseEntity.status(HttpStatus.OK).body(scores);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

    @GetMapping("/above/{minValue}")
    ResponseEntity<List<Score>> getScoresAboveValue(@PathVariable Integer minValue) {
        try {
            List<Score> scores = scoreService.getScoresAboveValue(minValue);
            return ResponseEntity.status(HttpStatus.OK).body(scores);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }

    @GetMapping("/recent")
    ResponseEntity<List<Score>> getRecentScores() {
        try {
            List<Score> scores = scoreService.getRecentScores();
            return ResponseEntity.status(HttpStatus.OK).body(scores);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }

    @GetMapping("/player/{playerId}/total-coins")
    ResponseEntity<?> getTotalCoinsByPlayerId(@PathVariable UUID playerId) {
        try {
            Integer total = scoreService.getTotalCoinsByPlayerId(playerId);
            return ResponseEntity.status(HttpStatus.OK).body(total);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }

    @GetMapping("/player/{playerId}/total-distance")
    ResponseEntity<?> getTotalDistanceByPlayerId(@PathVariable UUID playerId) {
        try {
            Integer total = scoreService.getTotalDistanceByPlayerId(playerId);
            return ResponseEntity.status(HttpStatus.OK).body(total);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }

    @DeleteMapping("/player/{playerId}")
    ResponseEntity<?> deleteScoresByPlayerId(@PathVariable UUID playerId) {
        try {
            scoreService.deleteScoresByPlayerId(playerId);
            return ResponseEntity.status(HttpStatus.OK).build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }
}
