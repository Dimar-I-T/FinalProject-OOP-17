package com.dimar.frontend;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;

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
}
