package com.dimar.frontend.strategies;

import com.dimar.frontend.Coin;
import com.dimar.frontend.factories.CoinFactory;

import java.util.List;

public interface CoinPattern {
    public List<Coin> spawn(CoinFactory factory, float spawnX, float spawnY, int banyakKoin);
    public String getName();
}
