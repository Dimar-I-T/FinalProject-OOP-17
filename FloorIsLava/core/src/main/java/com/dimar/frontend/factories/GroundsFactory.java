package com.dimar.frontend.factories;

import com.dimar.frontend.Grounds;
import com.dimar.frontend.pools.GroundsPool;

import java.util.List;

public class GroundsFactory {
    public final GroundsPool groundsPool = new GroundsPool();

    public void release(Grounds ground) {
        groundsPool.release(ground);
    }

    public void releaseAll() {
        groundsPool.releaseAll();
    }

    public List<Grounds> getInUse() {
        return groundsPool.getInUse();
    }
}
