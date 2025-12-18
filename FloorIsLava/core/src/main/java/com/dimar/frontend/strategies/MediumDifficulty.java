package com.dimar.frontend.strategies;

public class MediumDifficulty implements DifficultyStrategy {
    @Override
    public float getKecepatanLava() {
        return 230f;
    }

    @Override
    public float getBatasNaikDifficulty() {
        return 650f;
    }

    @Override
    public DifficultyStrategy getNextDifficulty() {
        return new HardDifficulty();
    }

    @Override
    public String getMode() {
        return "Medium";
    }
}
