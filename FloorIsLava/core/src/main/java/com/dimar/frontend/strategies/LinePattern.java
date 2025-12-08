package com.dimar.frontend.strategies;

import com.badlogic.gdx.Gdx;
import com.dimar.frontend.Coin;
import com.dimar.frontend.factories.CoinFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class LinePattern implements CoinPattern{
    private static float SPACING = 30f;
    private Random random = new Random();

    @Override
    public List<Coin> spawn(CoinFactory factory, float spawnX, float spawnY, int banyakKoin) {
        List<Coin> hasil = new ArrayList<>();
        for (int x = 0; x < banyakKoin; x++) {
            Coin coin = factory.coinPool.obtain(spawnX + x * SPACING, spawnY);
            hasil.add(coin);
        }

        return hasil;
    }

    @Override
    public String getName() {
        return "Line";
    }
}
