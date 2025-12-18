package com.dimar.frontend.strategies;

public class VeryHardDifficulty implements DifficultyStrategy {
    @Override
    public float getKecepatanLava() {
        return 230f;
    }

    @Override
    public float getBatasNaikDifficulty() {
        return 750f;
    }

    @Override
    public DifficultyStrategy getNextDifficulty() {
        return new ExtremeDifficulty();
    }

    @Override
    public String getMode() {
        return "Very Fast";
    }

    @Override
    public float getTimerToNext() {
        return 60f;
    }
}
