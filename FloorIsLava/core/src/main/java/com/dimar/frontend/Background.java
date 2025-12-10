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
        // Load background image (2688x1536)
        backgroundTexture = new Texture(Gdx.files.internal("background.jpg"));
        backgroundRegion = new TextureRegion(backgroundTexture);

        this.width = 512f; // Full width of background image
        this.height = 512f;
    }

    public void update(float cameraX) {
        // Store camera position for use in renderShape
        this.currentCameraY = cameraX;
    }

    public void render(SpriteBatch batch) {
        // Scale to fit screen height while maintaining aspect ratio
        float scale = 384f / height;
        float scaledWidth = width * scale;
        float scaledHeight = height * scale;

        // Calculate starting position based on stored camera position

        batch.begin();
        // Draw background tiles to cover entire screen and beyond
        for (float y = 0; y < currentCameraY + Gdx.graphics.getHeight(); y += scaledHeight) {
            for (float x = 0; x < Gdx.graphics.getWidth(); x += scaledWidth){
                batch.draw(backgroundRegion, x, y, scaledWidth, scaledHeight);
            }
        }
        batch.end();
    }

    public void dispose() {
        if (backgroundTexture != null) {
            backgroundTexture.dispose();
        }
    }
}
