package com.dimar.frontend.factories;

import com.dimar.frontend.Coin;
import com.dimar.frontend.pools.CoinPool;

import java.util.List;

public class CoinFactory {
    public final CoinPool coinPool = new CoinPool();

    public void release(Coin coin) {
        coinPool.release(coin);
    }

    public void releaseAll() {
        coinPool.releaseAll();
    }

    public List<Coin> getInUse() {
        return coinPool.getInUse();
    }
}
