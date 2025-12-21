package com.dimar.frontend.states;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.dimar.frontend.GameManager;

public class ControlState implements GameState {
    private GameStateManager gsm;
    private Texture texture;
    private final float width, height;

    public ControlState(GameStateManager gsm){
        this.gsm = gsm;
        this.texture = new Texture("controls.png");
        this.width = Gdx.graphics.getWidth();
        this.height = Gdx.graphics.getHeight();
    }

    @Override
    public void update(float delta) {
        if (Gdx.input.isKeyJustPressed(Input.Keys.SPACE)) {
            gsm.set(new PlayingState(gsm));
        }
        else if (Gdx.input.isKeyJustPressed(Input.Keys.ESCAPE)){
            GameManager.getInstance().endGame();
            gsm.set(new MenuState(gsm));
        }
    }

    @Override
    public void render(ShapeRenderer shapeRenderer, SpriteBatch spriteBatch) {
        spriteBatch.begin();
        spriteBatch.draw(texture, 0, 0, width, height);
        spriteBatch.end();
    }

    @Override
    public void dispose() {
        if (texture != null) texture.dispose();
    }

    @Override
    public void resize(int width, int height) {

    }
}
