package com.dimar.frontend;

public class Leaderboard {
    private String username;
    private int score;
    private int coinsCollected;

    public Leaderboard(String username, int score, int coinsCollected) {
        this.username = username;
        this.score = score;
        this.coinsCollected = coinsCollected;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }

    public int getCoinsCollected() {
        return coinsCollected;
    }

    public void setCoinsCollected(int coinsCollected) {
        this.coinsCollected = coinsCollected;
    }
}
