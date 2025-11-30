package com.dimar.frontend.pools;

import com.badlogic.gdx.math.Vector2;
import com.dimar.frontend.Grounds;

public class GroundsPool extends ObjectPool<Grounds> {
    @Override
    public Grounds createObject() {
        return new Grounds(0, 0, 100f, 1);
    }

    @Override
    public void resetObject(Grounds object) {
        object.setActive(false);
    }

    public Grounds obtain(float posisiAcuan, float posisiY, float widthAcuan, int kiriKanan) {
        Grounds object = super.obtain();
        object.initialize(posisiAcuan, posisiY, widthAcuan, kiriKanan);
        object.setActive(true);
        return object;
    }
}
