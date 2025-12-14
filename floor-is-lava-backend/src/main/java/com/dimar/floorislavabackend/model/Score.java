package com.dimar.floorislavabackend.model;

import jakarta.persistence.*;
import org.hibernate.annotations.CreationTimestamp;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonIgnore;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "scores")
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class Score {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "score_id")
    private UUID scoreId;

    @Column(name = "player_id", nullable = false)
    private UUID playerId;

    @Column(name = "score", nullable = false)
    private Integer value;

    @Column(name = "coins_collected")
    private Integer coinsCollected = 0;

    @Column(name = "distance_travelled")
    private Integer distanceTravelled = 0;

    @CreationTimestamp
    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    public Score() {}

    public Score(UUID playerId, Integer value, Integer coinsCollected, Integer distanceTravelled) {
        this.playerId = playerId;
        this.value = value;
        this.coinsCollected = coinsCollected;
        this.distanceTravelled = distanceTravelled;
    }

    public UUID getScoreId() {
        return scoreId;
    }

    public void setScoreId(UUID scoreId) {
        this.scoreId = scoreId;
    }

    public UUID getPlayerId() {
        return playerId;
    }

    public void setPlayerId(UUID playerId) {
        this.playerId = playerId;
    }

    public Integer getValue() {
        return value;
    }

    public void setValue(Integer value) {
        this.value = value;
    }

    public Integer getCoinsCollected() {
        return coinsCollected;
    }

    public void setCoinsCollected(Integer coinsCollected) {
        this.coinsCollected = coinsCollected;
    }

    public Integer getDistanceTravelled() {
        return distanceTravelled;
    }

    public void setDistanceTravelled(Integer distanceTravelled) {
        this.distanceTravelled = distanceTravelled;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }
}
