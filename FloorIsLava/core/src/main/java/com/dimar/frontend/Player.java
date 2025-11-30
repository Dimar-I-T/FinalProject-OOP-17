package com.dimar.frontend;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;

public class Player {
    private Vector2 velocity;
    private Vector2 position;
    private Rectangle collider;
    private float speed = 300f;
    private float lompatan = 1000f;
    private float gravity = 2000f;
    private float WIDTH = 64f;
    private float HEIGHT = 64f;
    private boolean kiri = false;
    private boolean kanan = false;
    private boolean isColliding = false;

    public Player(Vector2 startPosition) {
        this.position = startPosition;
        velocity = new Vector2(0, 0);
        collider = new Rectangle(startPosition.x, startPosition.y, WIDTH, HEIGHT);
    }

    public void render(ShapeRenderer shapeRenderer) {
        shapeRenderer.setColor(Color.BLUE);
        shapeRenderer.rect(position.x, position.y, WIDTH, HEIGHT);
    }

    public void update(float delta) {
        applyGravity(delta);
        updatePosition(delta);
        updateCollider();
    }

    public void setKiri(boolean kiri) {
        this.kiri = kiri;
    }

    public void setKanan(boolean kanan) {
        this.kanan = kanan;
    }

    private void applyGravity(float delta) {
        if (!isColliding) {
            velocity.y -= gravity * delta;
            if (velocity.y < -700f) {
                velocity.y = -700f;
            }
        }
    }

    public void Lompat() {
        if (isColliding) {
            velocity.y = lompatan;
            isColliding = false;
        }
    }

    private void updatePosition(float delta) {
        position.y += velocity.y * delta;
        if (kiri) {
            position.x -= speed * delta;
        }

        if (kanan) {
            position.x += speed * delta;
        }
    }

    public void updateCollider() {
        collider.x = position.x;
        collider.y = position.y;
    }

    public void handleGroundCollision(Ground ground) {
        if (!ground.isColliding(collider)) return;
        float gX = ground.getPosition().x;
        float gY = ground.getPosition().y;
        float gW = ground.getWidth();
        float gH = ground.getHeight();

        float pX = position.x;
        float pY = position.y;
        float pW = WIDTH;
        float pH = HEIGHT;

        float playerCenterX = pX + pW / 2f;
        float playerCenterY = pY + pH / 2f;
        float groundCenterX = gX + gW / 2f;
        float groundCenterY = gY + gH / 2f;

        float dx = playerCenterX - groundCenterX;
        float dy = playerCenterY - groundCenterY;

        float combinedHalfWidth = (pW / 2f) + (gW / 2f);
        float combinedHalfHeight = (pH / 2f) + (gH / 2f);

        float overlapX = combinedHalfWidth - Math.abs(dx);
        float overlapY = combinedHalfHeight - Math.abs(dy);

        if (overlapX < overlapY) {
            if (dx > 0) {
                position.x += overlapX;
            } else {
                position.x -= overlapX;
            }
            velocity.x = 0;
        } else {
            if (dy > 0) {
                position.y += overlapY;
                velocity.y = 0;
                isColliding = true;
            } else {
                position.y -= overlapY;
                if (velocity.y > 0) velocity.y = 0;
            }
        }

        updateCollider();
    }

    public void setGrounded(boolean grounded) {
        this.isColliding = grounded;
    }

    public void checkBoundaries() {
        if (position.x > Gdx.graphics.getWidth() - WIDTH) {
            position.x = Gdx.graphics.getWidth() - WIDTH;
            velocity.x = 0;
        }

        if (position.x < 0) {
            position.x = 0;
            velocity.x = 0;
        }

        updateCollider();
    }

    public Vector2 getPosition() {
        return position;
    }

    public float getGravity() {
        return gravity;
    }

    public float getLompatan() {
        return lompatan;
    }

    public float getSpeed() {
        return speed;
    }
}
