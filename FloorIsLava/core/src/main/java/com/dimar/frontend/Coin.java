package com.dimar.frontend;

import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;

public class Coin {
    private final Vector2 position;
    private final Rectangle collider;
    private final float radius = 10f;
    private boolean active;

    private float bobOffset;

    public Coin(Vector2 startPosition) {
        position = startPosition;
        collider = new Rectangle(startPosition.x, startPosition.y, radius * 2, radius * 2);
    }

    public void update(float delta) {
        float bobSpeed = 10f;
        bobOffset += bobSpeed * delta;
        float drawY = position.y + (float)(Math.sin(bobOffset) * 5f);
        collider.setPosition(position.x - radius, drawY - radius);
    }

    public void renderShape(ShapeRenderer shapeRenderer) {
        float drawY = position.y + (float)(Math.sin(bobOffset) * 5f);
        shapeRenderer.setColor(1f, 1f, 0f,  1f);
        shapeRenderer.circle(position.x, drawY, radius);
    }

    public boolean isColliding(Rectangle playerCollider) {
        return active && playerCollider.overlaps(collider);
    }

    public void initialize(float x, float y) {
        this.position.set(x, y);
        this.collider.setPosition(x - radius, y - radius);
        this.active = true;
    }

    public Vector2 getPosition() {
        return position;
    }

    public Rectangle getCollider() {
        return this.collider;
    }

    public void setActive(boolean newBool) {
        this.active = newBool;
    }
}

