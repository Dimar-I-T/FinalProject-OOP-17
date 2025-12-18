package com.dimar.frontend.strategies;

public class MediumDifficulty implements DifficultyStrategy {
    @Override
    public float getKecepatanLava() {
        return 210f;
    }

    @Override
    public float getBatasNaikDifficulty() {
        return 500f;
    }

    @Override
    public DifficultyStrategy getNextDifficulty() {
        return new HardDifficulty();
    }

    @Override
    public String getMode() {
        return "Medium";
    }

    @Override
    public float getTimerToNext() {
        return 20f;
    }
}
