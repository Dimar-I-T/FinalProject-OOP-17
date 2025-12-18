package com.dimar.frontend;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;

public class Lava {
    private final float width;
    private final float height;
    private final Vector2 position;
    private final Vector2 velocity;
    private final Rectangle collider;

    private Texture asset;
    private boolean active;
    private float kecepatan;

    public Lava(Vector2 startPosition, float width, float height) {
        velocity = new Vector2(0, kecepatan);
        this.width = width;
        this.height = height;
        this.position = startPosition;
        collider = new Rectangle(startPosition.x, startPosition.y, width, height);
        asset = new Texture("lava.jpg");
    }

    public void reset(Vector2 startPosition) {
        collider.setPosition(startPosition.x, startPosition.y);
        position.set(startPosition.x, startPosition.y);
    }

    public boolean isCollidingWithPlayer(Rectangle playerCollider) {
        return playerCollider.overlaps(collider);
    }

    public boolean isCollidingWithGround(Rectangle groundCollider) {
        return groundCollider.overlaps(collider);
    }

    public boolean isColliding(Rectangle collider) {
        return collider.overlaps(this.collider);
    }

    public void update(float delta) {
        position.y += velocity.y * delta;
        collider.setPosition(position.x, position.y);
        //System.out.println(position.y);
    }

    public void render(ShapeRenderer shapeRenderer) {
        shapeRenderer.setColor(1, 0.4f, 0, 1f);
        shapeRenderer.rect(collider.x, collider.y, collider.width, collider.height);
    }

    public void renderTexture(SpriteBatch batch){
        float scaledSize = 1000f / 4f;
        for (float y = position.y; y < height + position.y - 0.1; y += scaledSize){
            for (float x = 0; x < width; x += scaledSize){
                batch.draw(asset, x, y, scaledSize, scaledSize);
            }
        }
    }

    public void setKecepatan(float kecepatan) {
        this.kecepatan = kecepatan;
        velocity.set(0, kecepatan);
    }

    public float getKecepatan() {
        return this.kecepatan;
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
}
