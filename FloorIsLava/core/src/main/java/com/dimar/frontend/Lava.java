package com.dimar.frontend;

import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;

public class Lava {
    private float width;
    private float height;
    private Vector2 position;
    private Vector2 velocity;
    private Rectangle collider;
    private boolean active;
    private float kecepatan = 220f;

    public Lava(Vector2 startPosition, float width, float height) {
        velocity = new Vector2(0, kecepatan);
        this.width = width;
        this.height = height;
        this.position = startPosition;
        collider = new Rectangle(startPosition.x, startPosition.y, width, height);
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
