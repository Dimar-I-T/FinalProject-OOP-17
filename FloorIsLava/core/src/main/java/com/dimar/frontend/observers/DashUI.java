package com.dimar.frontend.observers;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class DashUI {
    private final BitmapFont font;
    private final SpriteBatch batch;
    private float width = Gdx.graphics.getWidth(), height = Gdx.graphics.getHeight();
    private float widthAwal = Gdx.graphics.getWidth(), heightAwal = Gdx.graphics.getHeight();
    public DashUI() {
        font = new BitmapFont(Gdx.files.internal("arial.fnt"));
        font.setColor(Color.WHITE);
        batch = new SpriteBatch();
    }

    public void render() {
        batch.begin();
        String teks = "DASH!";
        font.draw(batch, teks, width / 2f, 0.75f * height);
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
}
