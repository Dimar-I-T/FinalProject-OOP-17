package com.dimar.frontend.strategies;

public class VeryEasyDifficulty implements DifficultyStrategy {
    @Override
    public float getKecepatanLava() {
        return 150f;
    }

    @Override
    public float getBatasNaikDifficulty() {
        return 600f;
    }

    @Override
    public DifficultyStrategy getNextDifficulty() {
        return new EasyDifficulty();
    }

    @Override
    public String getMode() {
        return "Very Slow";
    }

    @Override
    public float getTimerToNext() {
        return 5f;
    }
}
