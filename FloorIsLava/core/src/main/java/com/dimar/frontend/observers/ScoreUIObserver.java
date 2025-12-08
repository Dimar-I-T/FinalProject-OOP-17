package com.dimar.frontend.observers;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class ScoreUIObserver implements Observer {
    private BitmapFont font, font1;
    private SpriteBatch batch;
    private int score;
    private float width = Gdx.graphics.getWidth(), height = Gdx.graphics.getHeight();
    private float widthAwal = Gdx.graphics.getWidth(), heightAwal = Gdx.graphics.getHeight();
    public ScoreUIObserver() {
        font = new BitmapFont(Gdx.files.internal("arial.fnt"));
        font.setColor(Color.WHITE);
        font1 = new BitmapFont(Gdx.files.internal("arial.fnt"));
        font1.setColor(Color.WHITE);
        batch = new SpriteBatch();
    }

    @Override
    public void update(int score) {
        this.score = score;
    }

    public void render(int score, int coins) {
        batch.begin();
        String teks = "Score: " + score;
        String teks1 = "Coins: " + coins;
        GlyphLayout layout = new GlyphLayout(font, teks);
        font.draw(batch, teks, width - layout.width - (width - widthAwal) - 10, height - (height - heightAwal) - 10);
        font1.draw(batch, teks1, width - layout.width - (width - widthAwal) - 10, height - (height - heightAwal) - 20 - layout.height);
        batch.end();
    }

    public void dispose() {
        batch.dispose();
        font.dispose();
    }

    public void updateWH(int width, int height) {
        this.width = width;
        this.height = height;
    }

    public int getScore() {
        return this.score;
    }
}
