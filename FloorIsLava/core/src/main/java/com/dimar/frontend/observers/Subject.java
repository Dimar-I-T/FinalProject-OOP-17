package com.dimar.frontend.observers;

public interface Subject {
    void addObserver(Observer observers);
    void removeObserver(Observer observers);
    void notifyObservers(int score);
}
