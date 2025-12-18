package com.dimar.frontend.strategies;

public interface DifficultyStrategy {
    public float getKecepatanLava();
    public float getBatasNaikDifficulty();
    public DifficultyStrategy getNextDifficulty();
    public String getMode();
}
