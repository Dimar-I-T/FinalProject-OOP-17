package com.dimar.frontend;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.OrthographicCamera;
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
        backgroundTexture = new Texture(Gdx.files.internal("background.jpg"));
        backgroundRegion = new TextureRegion(backgroundTexture);

        this.width = 512f;
        this.height = 512f;
    }

    public void update(float cameraY) {
        this.currentCameraY = cameraY;
    }

    public void render(SpriteBatch batch, OrthographicCamera camera) {
        float scale = 384f / height;
        float scaledWidth = width * scale;
        float scaledHeight = height * scale;

        float startY = camera.position.y - camera.viewportHeight / 2f;
        float endY   = camera.position.y + camera.viewportHeight / 2f;

        float startX = camera.position.x - camera.viewportWidth / 2f;
        float endX   = camera.position.x + camera.viewportWidth / 2f;

        startY -= startY % scaledHeight;
        startX -= startX % scaledWidth;

        for (float y = startY; y < endY; y += scaledHeight) {
            for (float x = startX; x < endX; x += scaledWidth) {
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
