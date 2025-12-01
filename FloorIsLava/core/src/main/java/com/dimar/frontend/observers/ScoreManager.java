package com.dimar.frontend.observers;

import java.util.ArrayList;
import java.util.List;

public class ScoreManager implements Subject {
    private List<Observer> observers;
    private int score = 0;

    public ScoreManager() {
        observers = new ArrayList<>();
        score = 0;
    }

    @Override
    public void addObserver(Observer observer) {
        observers.add(observer);
    }

    @Override
    public void removeObserver(Observer observer) {
        observers.remove(observer);
    }

    @Override
    public void notifyObservers(int newScore) {
        for (Observer observer : observers) {
            observer.update(newScore);
        }
    }

    public void setScore(int newScore) {
        if (newScore != score) {
            this.score = newScore;
        }

        notifyObservers(score);
    }

    public int getScore() {
        return this.score;
    }
}
