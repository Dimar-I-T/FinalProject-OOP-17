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
        System.out.println("Score has been updated to " + score);
    }

    public void render(int score, int coins) {
        batch.begin();
        String teks = "Score: " + score;
        //String teks1 = "Coins: " + coins;
        GlyphLayout layout = new GlyphLayout(font, teks);
        font.draw(batch, teks, Gdx.graphics.getWidth() - layout.width - 10, Gdx.graphics.getHeight()  - 10);
        //font1.draw(batch, teks1, Gdx.graphics.getWidth() - layout.width - 10,Gdx.graphics.getHeight() - 20 - layout.height);
        batch.end();
    }

    public void dispose() {
        batch.dispose();
        font.dispose();
    }

    public int getScore() {
        return this.score;
    }
}
