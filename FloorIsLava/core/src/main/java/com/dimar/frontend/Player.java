package com.dimar.frontend;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;

public class Player {
    private Vector2 velocity;
    private Vector2 position;
    private Vector2 startPosition;
    private Rectangle collider;
    public static float speed = 350f;
    public static float lompatan = 1000f;
    public static float gravity = 2000f;
    private float WIDTH = 64f;
    private float HEIGHT = 64f;
    private float Delta;
    private boolean isColliding = false;
    private float widthAwal;
    private boolean isDead;
    private float verticalDistanceTravelled = 0f;

    public Player(Vector2 startPosition) {
        widthAwal = Gdx.graphics.getWidth();
        this.position = new Vector2(startPosition.x, startPosition.y);
        this.startPosition = new Vector2(startPosition.x, startPosition.y);
        velocity = new Vector2(0, 0);
        collider = new Rectangle(startPosition.x, startPosition.y, WIDTH, HEIGHT);
    }

    public void render(ShapeRenderer shapeRenderer) {
        shapeRenderer.setColor(Color.BLUE);
        shapeRenderer.rect(position.x, position.y, WIDTH, HEIGHT);
    }

    public void update(float delta) {
        if (!isDead) {
            applyGravity(delta);
            updatePositionY(delta);
            updateVerticalDistance();
            Delta = delta;
        }

        //System.out.println(isDead);
        updateCollider();
    }

    private void updateVerticalDistance() {
        if (verticalDistanceTravelled <= position.y) {
            verticalDistanceTravelled = position.y;
        }
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

    public void Kiri() {
        position.x -= speed * Delta;
    }

    public void Kanan() {
        position.x += speed * Delta;
    }

    private void updatePositionY(float delta) {
        position.y += velocity.y * delta;
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

    public void checkBoundaries(float batasKiri) {
//        System.out.println(batasKiri - (widthAwal - batasKiri) / 2f);
//        System.out.println("x = " + position.x);
        if (position.x > (batasKiri + widthAwal) / 2f - WIDTH) {
            position.x = (batasKiri + widthAwal) / 2f - WIDTH;
            velocity.x = 0;
        }

        if (position.x < (widthAwal - batasKiri) / 2f) {
            position.x = (widthAwal - batasKiri) / 2f;
            velocity.x = 0;
        }

        updateCollider();
    }

    public void die() {
        if (!isDead) {
            isDead = true;
        }

        velocity.set(0, 0);
    }

    public void reset() {
        if (isDead) {
            isDead = false;
        }

        position.set(startPosition);
        velocity.set(0, 0);
        verticalDistanceTravelled = 0f;
    }

    public boolean getIsDead() {
        return isDead;
    }

    public void setIsDead(boolean isDead) {
        this.isDead = isDead;
    }

    public Rectangle getCollider() {
        return collider;
    }

    public Vector2 getPosition() {
        return position;
    }

    public float getVerticalDistanceTravelled() {
        return verticalDistanceTravelled;
    }
}
