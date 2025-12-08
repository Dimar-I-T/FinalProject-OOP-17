package com.dimar.frontend;

import com.dimar.frontend.observers.Observer;
import com.dimar.frontend.observers.ScoreManager;

public class GameManager {
    private static GameManager instance;
    private ScoreManager scoreManager;
    private boolean gameActive;
    int coinsCollected = 0;

    private GameManager() {
        scoreManager = new ScoreManager();
        scoreManager.setScore(0);
        gameActive = false;
    }

    public static GameManager getInstance() {
        if (instance == null) {
            instance = new GameManager();
        }

        return instance;
    }

    public void startGame() {
        scoreManager.setScore(0);
        gameActive = true;
        System.out.println("Game Started!");
    }

    public void setScore(int distance) {
        if (gameActive) {
            scoreManager.setScore(distance);
        }
    }

    public void addCoin() {
        coinsCollected++;
    }

    public int getCoinsCollected() {
        return this.coinsCollected;
    }

    public int getScore() {
        return scoreManager.getScore();
    }

    public void setCoinsCollected(int coins) {
        this.coinsCollected = coins;
    }

    public void addObserver(Observer observer) {
        scoreManager.addObserver(observer);
    }

    public void removeObserver(Observer observer) {
        scoreManager.removeObserver(observer);
    }
}
