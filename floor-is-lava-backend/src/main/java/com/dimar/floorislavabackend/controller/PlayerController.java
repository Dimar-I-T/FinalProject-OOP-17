package com.dimar.floorislavabackend.controller;

import com.dimar.floorislavabackend.model.Player;
import com.dimar.floorislavabackend.repository.PlayerRepository;
import com.dimar.floorislavabackend.service.AuthenticationService;
import com.dimar.floorislavabackend.service.PlayerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@RestController
@RequestMapping("/api/players")
public class PlayerController {
    @Autowired
    private final PlayerService playerService;

    public PlayerController(PlayerService playerService) {
        System.out.println("PlayerController initialized");
        this.playerService = playerService;
    }

    @GetMapping
    ResponseEntity<List<Player>> getAllPlayers() {
        List<Player> allPlayers = playerService.getAllPlayers();
        return ResponseEntity.ok(allPlayers);
    }

    @GetMapping("/me")
    public ResponseEntity<Player> getMyPlayer(Authentication authentication) {
        String username = authentication.getName();
        Optional<Player> player = playerService.getPlayerByUsername(username);
        return player.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_FOUND).build());
    }

    @GetMapping("/username/{username}")
    ResponseEntity<?> getPlayerByUsername(@PathVariable String username) {
        Optional<Player> player = playerService.getPlayerByUsername(username);
        if (player.isPresent()) {
            return ResponseEntity.ok(player);
        }else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }

    @GetMapping("/check-username/{username}")
    boolean checkUsername(@PathVariable String username) {
        return playerService.isUsernameExists(username);
    }

    @PostMapping
    public ResponseEntity<?> createPlayer(@RequestBody Player playerRequest) {
        System.out.println("POST /players called with username: " + playerRequest.getUsername());
        try {
            Player newPlayer = new Player();
            newPlayer.setUsername(playerRequest.getUsername());
            Player savedPlayer = playerService.createPlayer(newPlayer);
            return ResponseEntity.status(HttpStatus.CREATED).body(savedPlayer);
        } catch (Exception e){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    @PutMapping("/{playerId}")
    public ResponseEntity<?> updatePlayer(@PathVariable UUID playerId, Player playerRequest) {
        System.out.println("POST /players called with username: " + playerRequest.getUsername());
        try {
            Player updatedP = playerService.updatePlayer(playerId, playerRequest);
            return ResponseEntity.status(HttpStatus.CREATED).body(updatedP);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

    @DeleteMapping("/{playerId}")
    public ResponseEntity<?> deletePlayer(@PathVariable UUID playerId) {
        try {
            playerService.deletePlayer(playerId);
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

    @GetMapping("/leaderboard/high-score")
    public ResponseEntity<List<Player>> getLeaderboardByHighScore(@RequestParam(defaultValue = "10") int limit) {
        List<Player> players = playerService.getLeaderboardByHighScore(limit);
        return ResponseEntity.ok(players);
    }

    @GetMapping("/leaderboard/total-coins")
    public ResponseEntity<List<Player>> getLeaderboardByTotalCoins() {
        List<Player> players = playerService.getLeaderboardByTotalCoins();
        return ResponseEntity.ok(players);
    }

    @GetMapping("/leaderboard/total-distance")
    public ResponseEntity<List<Player>> getLeaderboardByTotalDistance() {
        List<Player> players = playerService.getLeaderboardByTotalDistance();
        return ResponseEntity.ok(players);
    }

    // kesan: MANTAP PISAN, PESAN: soal diperdikit :)

//    public PlayerController(PlayerRepository playerService) {
//        System.out.println("PlayerController initialized");
//        this.playerService = playerService;
//    }
//
//    @GetMapping("/{username}")
//    public Optional<Player> getPlayerByUsername(@PathVariable  String username) {
//        return playerService.findByUsername(username);
//    }
//
//    @GetMapping("/top")
//    public List<Player> getTopPlayersByHighScore(@RequestParam(defaultValue = "10") int limit) {
//        return playerService.findTopPlayersByHighScore(limit);
//    }
//
//    @PostMapping
//    public ResponseEntity<Player> createPlayer(@RequestBody Player playerRequest) {
//        System.out.println("POST /players called with username: " + playerRequest.getUsername());
//        Player newPlayer = new Player();
//        newPlayer.setUsername(playerRequest.getUsername());
//        Player savedPlayer = playerService.save(newPlayer);
//        return ResponseEntity.ok(savedPlayer);
//    }
//
//    @PutMapping("/{id}/highscore")
//    public ResponseEntity<Player> updateHighScore(
//            @PathVariable UUID id,
//            @RequestParam int newScore
//    ) {
//        return playerService.findById(id)
//                .map(player -> {
//                    player.updateHighScore(newScore);
//                    Player updated = playerService.save(player);
//                    return ResponseEntity.ok(updated);
//                })
//                .orElse(ResponseEntity.notFound().build());
//    }
}
