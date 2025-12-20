package com.dimar.frontend.strategies;

public class HardDifficulty implements DifficultyStrategy {
    @Override
    public float getKecepatanLava() {
        return 220f;
    }

    @Override
    public float getBatasNaikDifficulty() {
        return 800f;
    }

    @Override
    public DifficultyStrategy getNextDifficulty() {
        return new VeryHardDifficulty();
    }

    @Override
    public String getMode() {
        return "Fast";
    }

    @Override
    public float getTimerToNext() {
        return 30f;
    }
}
