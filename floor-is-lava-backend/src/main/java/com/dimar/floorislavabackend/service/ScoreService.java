package com.dimar.floorislavabackend.service;

import com.dimar.floorislavabackend.repository.ScoreRepository;
import com.dimar.floorislavabackend.repository.PlayerRepository;
import com.dimar.floorislavabackend.model.Player;
import com.dimar.floorislavabackend.model.Score;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class ScoreService {
    @Autowired
    private ScoreRepository scoreRepository;

    @Autowired
    private PlayerRepository playerRepository;

    @Autowired
    private PlayerService playerService;

    @Transactional
    public Score createScore(Score score) {
        Optional<Player> player = playerService.getPlayerById(score.getPlayerId());
        if (player.isEmpty()) {
            throw new RuntimeException("Player doesn't exist");
        }else {
            Score scoreTersimpan = scoreRepository.save(score);
            playerService.updatePlayerStats(player.get().getPlayerId(), score.getValue(), score.getCoinsCollected(), score.getDistanceTravelled());
            return scoreTersimpan;
        }
    }

    public Optional<Score> getScoreById(UUID score_id) {
        return scoreRepository.findById(score_id);
    }

    public List<Score> getAllScores() {
        return scoreRepository.findAll();
    }

    public List<Score> getScoresByPlayerId(UUID player_id) {
        return scoreRepository.findByPlayerId(player_id);
    }

    public List<Score> getScoresByPlayerIdOrderByValue(UUID player_id) {
        return scoreRepository.findHighestScoreByPlayerId(player_id);
    }

    public List<Score> getLeaderboard(int limit) {
        return scoreRepository.findTopScores(limit);
    }

    public Optional<Score> getHighestScoreByPlayerId(UUID playerId) {
        return scoreRepository.getHighestScoreByPlayerId1(playerId);
    }

    public List<Score> getScoresAboveValue(Integer minValue) {
        return scoreRepository.findByValueGreaterThan(minValue);
    }

    public List<Score> getRecentScores() {
        return scoreRepository.findAllByOrderByCreatedAtDesc();
    }

    public Integer getTotalCoinsByPlayerId(UUID playerId) {
        return scoreRepository.getTotalCoinsByPlayerId(playerId);
    }

    public Integer getTotalDistanceByPlayerId(UUID playerId) {
        return scoreRepository.getTotalDistanceByPlayerId(playerId);
    }

    public Score updateScore(UUID scoreId, Score updatedScore) {
        Score score = scoreRepository.findById(scoreId).get();
        score.setValue(updatedScore.getValue());
        score.setCoinsCollected(updatedScore.getCoinsCollected());
        score.setDistanceTravelled(updatedScore.getDistanceTravelled());
        return score;
    }

    public void deleteScore(UUID scoreId) {
        if (!scoreRepository.existsById(scoreId)) {
            throw new RuntimeException("Score tersebut tidak ada");
        }

        Score score = scoreRepository.findById(scoreId)
                .orElseThrow(() -> new RuntimeException("Score not found with ID: " + scoreId));
        scoreRepository.deleteById(scoreId);
    }

    public void deleteScoresByPlayerId(UUID playerId) {
       List<Score> scores = scoreRepository.findByPlayerId(playerId);
       for (Score s : scores) {
           scoreRepository.deleteById(s.getScoreId());
       }
    }


}
