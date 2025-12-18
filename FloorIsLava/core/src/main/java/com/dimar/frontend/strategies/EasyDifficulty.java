package com.dimar.frontend.strategies;

public class EasyDifficulty implements DifficultyStrategy {
    @Override
    public float getKecepatanLava() {
        return 200f;
    }

    @Override
    public float getBatasNaikDifficulty() {
        return 1000f;
    }

    @Override
    public DifficultyStrategy getNextDifficulty() {
        return new MediumDifficulty();
    }

    @Override
    public String getMode() {
        return "Slow";
    }
}
