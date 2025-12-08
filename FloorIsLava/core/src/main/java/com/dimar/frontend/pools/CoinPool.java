package com.dimar.frontend.pools;

import com.badlogic.gdx.math.Vector2;
import com.dimar.frontend.Coin;

public class CoinPool extends ObjectPool<Coin> {
    @Override
    public Coin createObject() {
        return new Coin(new Vector2(0, 0));
    }

    @Override
    public void resetObject(Coin object) {
        object.setActive(false);
    }

    public Coin obtain(float x, float y) {
        Coin object = super.obtain();
        object.initialize(x, y);
        object.setActive(true);
        return object;
    }
}
