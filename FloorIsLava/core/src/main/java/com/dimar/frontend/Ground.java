package com.dimar.frontend;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector;
import com.badlogic.gdx.math.Vector2;

public class Ground {
    private float width;
    private float height;
    private Vector2 position;
    private Rectangle collider;
    private boolean acuan = false;
    private boolean active;

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
        shapeRenderer.setColor(0.5f, 0.5f, 0.5f, 1f);
        shapeRenderer.rect(collider.x, collider.y, collider.width, collider.height);
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
