package com.dimar.frontend.pools;
import java.util.ArrayList;
import java.util.List;

public abstract class ObjectPool<T> {
    private final List<T> available = new ArrayList<>();
    private final List<T> inUse = new ArrayList<>();

    protected abstract T createObject();
    protected abstract void resetObject(T object);

    public T obtain() {
        T object;
        if (available.isEmpty()) {
            object = createObject();
        }else {
            object = available.remove(available.size() - 1);
        }

        inUse.add(object);
        return object;
    }

    public void release(T object) {
        inUse.remove(object);
        resetObject(object);
        available.add(object);
    }

    public void releaseAll() {
        for (T obj : inUse) {
            resetObject(obj);
            available.add(obj);
        }

        inUse.clear();
    }

    public List<T> getInUse() {
        return inUse;
    }
    public int getActiveCount() {
        return inUse.size();
    }
}
