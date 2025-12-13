package com.dimar.frontend;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

public class Background {
    private Texture backgroundTexture;
    private TextureRegion backgroundRegion;
    private float width;
    private float height;
    private float currentCameraY = 0f;

    public Background() {
        backgroundTexture = new Texture(Gdx.files.internal("background.png"));
        backgroundRegion = new TextureRegion(backgroundTexture);

        this.width = 512f;
        this.height = 512f;
    }

    public void update(float cameraY) {
        this.currentCameraY = cameraY;
    }

    public void render(SpriteBatch batch) {
        float scale = 384f / height;
        float scaledWidth = width * scale;
        float scaledHeight = height * scale;

        for (float y = 0; y < currentCameraY + Gdx.graphics.getHeight(); y += scaledHeight) {
            for (float x = 0; x < Gdx.graphics.getWidth(); x += scaledWidth){
                batch.draw(backgroundRegion, x, y, scaledWidth, scaledHeight);
            }
        }
    }

    public void dispose() {
        if (backgroundTexture != null) {
            backgroundTexture.dispose();
        }
    }
}
