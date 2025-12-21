package com.dimar.frontend;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.dimar.frontend.states.GameState;
import com.dimar.frontend.states.MenuState;
import com.dimar.frontend.states.PlayingState;

public class Ground {
    private final float width;
    private final float height;
    private final Vector2 position;
    private final Rectangle collider;
    private boolean acuan = false;
    private boolean active;

    private Texture texture;

    public Ground(Vector2 startPosition, float width, float height, boolean acuan) {
        this.width = width;
        this.acuan = acuan;
        this.height = height;
        this.position = startPosition;
        collider = new Rectangle(startPosition.x, startPosition.y, width, height);
    }

    public boolean isColliding(Rectangle playerCollider) {
        return playerCollider.overlaps(collider);
    }

    public void render(ShapeRenderer shapeRenderer) {
        if (acuan) {
            shapeRenderer.setColor(0.5f, 0.5f, 0.5f, 1f);
        }else {
            shapeRenderer.setColor(0.5f, 0.5f, 0.5f, 1f);
        }

        shapeRenderer.rect(collider.x, collider.y, collider.width, collider.height);
    }

    public void renderTexture(SpriteBatch batch, float tileSize, boolean grass, Texture grassTexture){
//        renderer.begin(ShapeRenderer.ShapeType.Filled);
//        renderer.setColor(Color.BROWN);
//        renderer.rect(position.x, position.y, width,height);
//        renderer.end();
        float startY = collider.y;
        if (!grass) startY -= 100f;
        for (float y = startY; y < this.collider.height + this.collider.y; y += tileSize){
            for (float x = 0f; x < collider.width; x += tileSize){
                batch.draw(texture, x, y, tileSize, tileSize);
            }
        }

        if(grass){
            for (float x = 0; x < Gdx.graphics.getWidth(); x += 1920f / 2f){
                batch.draw(grassTexture, x, height + position.y - 15f, 1920f / 2f, 1080f / 2f);
            }
        }
    }

    public float getHeight() {
        return height;
    }

    public float getWidth() {
        return width;
    }

    public Vector2 getPosition() {
        return position;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public void setTexture(Texture texture) {
        this.texture = texture;
    }

    public void dispose(){
        if(texture != null) texture.dispose();
    }
}
