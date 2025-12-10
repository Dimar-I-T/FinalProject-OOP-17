package com.dimar.frontend.states;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;

public class GameOverState implements GameState {
    private final GameStateManager gsm;
    private final BitmapFont font, font1;
    private final OrthographicCamera camera;
    private float width;
    private final float widthAwal;
    private float height;
    private final float heightAwal;

    public GameOverState(GameStateManager gsm) {
        System.out.println("You are dead.");
        camera = new OrthographicCamera();
        camera.setToOrtho(false, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        width = Gdx.graphics.getWidth();
        height = Gdx.graphics.getHeight();
        widthAwal = Gdx.graphics.getWidth();
        heightAwal = Gdx.graphics.getHeight();
        this.gsm = gsm;
        font = new BitmapFont(Gdx.files.internal("arial.fnt"));
        font.setColor(Color.RED);
        font1 = new BitmapFont(Gdx.files.internal("arial.fnt"));
        font1.setColor(Color.WHITE);
    }

    @Override
    public void update(float delta) {
        //System.out.println("You are dead.");
        if (Gdx.input.isKeyJustPressed((Input.Keys.SPACE))) {
            gsm.set(new PlayingState(gsm));
        }
    }

    @Override
    public void render(ShapeRenderer shapeRenderer, SpriteBatch batch) {
        batch.setProjectionMatrix(camera.combined);
        batch.begin();
        GlyphLayout layout = new GlyphLayout(font, "GAME OVER");
        GlyphLayout layout1 = new GlyphLayout(font1, "Press SPACE to restart");
        float x = (width - (width - widthAwal) - layout.width) / 2f;
        float y = (height + layout.height - (height - heightAwal)) / 2f;
        float x1 = (width - (width - widthAwal) - layout1.width) / 2f;
        float y1 = (height + layout1.height - (height - heightAwal)) / 2f - layout.height - 20;
        font.draw(batch, layout, x, y);
        font1.draw(batch, layout1, x1, y1);
        batch.end();
    }

    @Override
    public void resize(int width, int height) {
        this.width = width;
        this.height = height;
    }

    @Override
    public void dispose() {
        font.dispose();
    }
}
