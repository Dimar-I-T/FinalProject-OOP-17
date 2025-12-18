package com.dimar.frontend.strategies;

public class EasyDifficulty implements DifficultyStrategy {
    @Override
    public float getKecepatanLava() {
        return 180f;
    }

    @Override
    public float getBatasNaikDifficulty() {
        return 500f;
    }

    @Override
    public DifficultyStrategy getNextDifficulty() {
        return new MediumDifficulty();
    }

    @Override
    public String getMode() {
        return "Slow";
    }

    @Override
    public float getTimerToNext() {
        return 5f;
    }
}
