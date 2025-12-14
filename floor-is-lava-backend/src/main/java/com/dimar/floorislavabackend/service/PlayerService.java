package com.dimar.floorislavabackend.service;

import com.dimar.floorislavabackend.repository.PlayerRepository;
import com.dimar.floorislavabackend.model.Player;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class PlayerService {
    @Autowired
    private PlayerRepository playerRepository;

//    public PlayerService(PlayerRepository playerRepository) {
//        this.playerRepository = playerRepository;
//    }
    public boolean isUsernameExists(String username) {
        Optional<Player> player = playerRepository.findByUsername(username);
        if (player.isPresent()){
            return true;
        }else {
            return false;
        }
    }

    public Player createPlayer(Player player) {
        Optional<Player> player1 = playerRepository.findByUsername(player.getUsername());
        if (playerRepository.existsByUsername(player.getUsername())) {
            throw new RuntimeException("Username already exists: " + player.getUsername());
        } else {
            return playerRepository.save(player);
        }
    }

    public Optional<Player> getPlayerById(UUID playerId) {
        return playerRepository.findAll().stream().filter(player -> player.getPlayerId().equals(playerId)).findFirst();
    }

    public Optional<Player> getPlayerByUsername(String username) {
        return playerRepository.findByUsername(username);
    }

    public List<Player> getAllPlayers() {
        return playerRepository.findAll();
    }

    public Player updatePlayer(UUID playerId, Player updatedPlayer) {
        Player existingPlayer = playerRepository.findById(playerId)
                .orElseThrow(() -> new RuntimeException("Player not found with ID: " + playerId));

        // Update username jika berbeda dan tersedia
        if (updatedPlayer.getUsername() != null &&
                !updatedPlayer.getUsername().equals(existingPlayer.getUsername())) {

            if (isUsernameExists(updatedPlayer.getUsername())) {
                throw new RuntimeException("Username already exists: " + updatedPlayer.getUsername());
            }
            existingPlayer.setUsername(updatedPlayer.getUsername());
        }

        // Update high score jika lebih tinggi
        if (updatedPlayer.getHighScore() != null) {
            existingPlayer.setHighScore(updatedPlayer.getHighScore());
        }

        // Update fields lainnya (cara sama)
        if (updatedPlayer.getTotalCoins() > existingPlayer.getTotalCoins()) {
            existingPlayer.setTotalCoins(updatedPlayer.getTotalCoins());
        }

        if (updatedPlayer.getTotalDistance() > existingPlayer.getTotalDistance()) {
            existingPlayer.setTotalDistance(updatedPlayer.getTotalDistance());
        }

        playerRepository.save(existingPlayer);
        return existingPlayer;
    }

    public void deletePlayer(UUID playerId) {
        if (!playerRepository.existsById(playerId)) {
            throw new RuntimeException("Player tersebut tidak ada");
        }

        Player player = playerRepository.findById(playerId)
                .orElseThrow(() -> new RuntimeException("Player not found with ID: " + playerId));
        playerRepository.deleteById(playerId);
    }

    public void deletePlayerByUsername(String username) {

        Player player = playerRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("Player not found with username: " + username));
        playerRepository.delete(player);
    }

    public Player updatePlayerStats(UUID playerId, Integer scoreValue, Integer coinsCollected, Integer distanceTravelled) {
        Player player = playerRepository.findById(playerId)
                .orElseThrow(() -> new RuntimeException("Player not found with ID: " + playerId));

        // Update high score if this score is higher
        player.updateHighScore(scoreValue);

        // Add coins and distance to totals
        player.addCoins(coinsCollected);
        player.addDistance(distanceTravelled);

        playerRepository.save(player);
        return player;
    }

    public List<Player> getLeaderboardByHighScore(int limit) {
        return playerRepository.findTopPlayersByHighScore(limit);
    }

    public List<Player> getLeaderboardByTotalCoins() {
        return playerRepository.findAllByOrderByTotalCoinsDesc();
    }

    public List<Player> getLeaderboardByTotalDistance() {
        return playerRepository.findAllByOrderByTotalDistanceDesc();
    }
}
