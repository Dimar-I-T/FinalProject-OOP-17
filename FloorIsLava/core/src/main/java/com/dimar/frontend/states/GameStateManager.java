package com.dimar.frontend.states;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;

import java.util.Stack;

public class GameStateManager {
    private final Stack<GameState> states;

    public GameStateManager() {
        states = new Stack<>();
    }

    public void push(GameState state) {
        states.push(state);
    }

    public void pop() {
        if (!states.isEmpty()) {
            states.pop().dispose();
        }
    }

    public void set(GameState state) {
        if (!states.isEmpty()) {
            states.pop().dispose();
        }
        states.push(state);
    }

    public void update(float delta) {
        if (!states.isEmpty()) {
            states.peek().update(delta);
        }
    }

    public void render(ShapeRenderer shapeRenderer, SpriteBatch batch) {
        if (!states.isEmpty()) {
            states.peek().render(shapeRenderer, batch);
        }
    }

    public void resize(int width, int height) {
        if (!states.isEmpty()) {
            states.peek().resize(width, height);
        }
    }

    public void clear() {
        while (!states.isEmpty()) {
            states.pop().dispose();
        }
    }
}
