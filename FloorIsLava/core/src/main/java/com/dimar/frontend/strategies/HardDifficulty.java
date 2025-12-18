package com.dimar.frontend.strategies;

public class HardDifficulty implements DifficultyStrategy {
    @Override
    public float getKecepatanLava() {
        return 260f;
    }

    @Override
    public float getBatasNaikDifficulty() {
        return 9999999f;
    }

    @Override
    public DifficultyStrategy getNextDifficulty() {
        return new HardDifficulty();
    }

    @Override
    public String getMode() {
        return "Fast";
    }
}
