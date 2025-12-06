package com.dimar.frontend.states;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;

public interface GameState {
    void update(float delta);
    void render(ShapeRenderer shapeRenderer, SpriteBatch spriteBatch);
    void dispose();
    void resize(int width, int height);
}
