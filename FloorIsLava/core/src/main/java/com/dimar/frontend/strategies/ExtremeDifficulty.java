package com.dimar.frontend.strategies;

public class ExtremeDifficulty implements DifficultyStrategy {
    @Override
    public float getKecepatanLava() {
        return 240f;
    }

    @Override
    public float getBatasNaikDifficulty() {
        return 9999999f;
    }

    @Override
    public DifficultyStrategy getNextDifficulty() {
        return new ExtremeDifficulty();
    }

    @Override
    public String getMode() {
        return "Extremely Fast";
    }

    @Override
    public float getTimerToNext() {
        return 60f;
    }
}
