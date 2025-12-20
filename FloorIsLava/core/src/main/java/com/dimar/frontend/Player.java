package com.dimar.frontend;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.dimar.frontend.strategies.animations.AnimationSelector;
import com.dimar.frontend.states.playerStates.*;

public class Player {
    private final Vector2 velocity;
    private final Vector2 position;
    private final Vector2 startPosition;
    private final Rectangle collider;
    public static float speed = 350f;
    public static float lompatan = 1000f;
    public static float gravity = 2000f;
    private float WIDTH = 64f;
    private float HEIGHT = 64f;
    private float Delta;
    private boolean isColliding = false;
    private final float widthAwal;
    private boolean isDead = false;
    private float verticalDistanceTravelled = 0f;
    private final float waktuDash = 0.1f;
    private float jarakDash = 500f;
    boolean isDashing = false;

    private final PlayerStateManager psm;

    private final AnimationSelector as;
    private Animation<TextureRegion> animation;
    private TextureRegion currentFrame;
    private float stateTime;
    private Arah arah;


    float dashTimeLeft = 0f;
    float kecepatanDash;

    int arahDash = 0;

    private Ground groundSekarang;

    private Sound dieSound;

    private float jumpBufferTimer = 0f;

    public Player(Vector2 startPosition) {
        widthAwal = Gdx.graphics.getWidth();
        this.position = new Vector2(startPosition.x, startPosition.y);
        this.startPosition = new Vector2(startPosition.x, startPosition.y);
        velocity = new Vector2(0, 0);
        collider = new Rectangle(startPosition.x, startPosition.y, WIDTH, HEIGHT);
        psm = new PlayerStateManager();
        arah = psm.getCurrentState().getArah();
        as = new AnimationSelector();
        stateTime = 0f;

        dieSound = Gdx.audio.newSound(Gdx.files.internal("audio/sound/die.wav"));
    }

    public void setSize(float size){
        this.WIDTH = size;
        this.HEIGHT = size;
    }

    private void updateAnimation(float delta){
        stateTime += delta;
        currentFrame = as.getCurrentFrame(psm.getCurrentState(), stateTime, arah);

    }

    public void render(ShapeRenderer shapeRenderer) {
        shapeRenderer.setColor(Color.BLUE);
        shapeRenderer.rect(position.x, position.y, WIDTH, HEIGHT);
    }

    public void renderTexture(SpriteBatch batch){
        if (currentFrame != null) {
            batch.draw(currentFrame.getTexture(), position.x, position.y, HEIGHT * HEIGHT / WIDTH + 10f, HEIGHT + 10f);
        }
    }

    public void update(float delta) {
        if (isDashing) {
            position.x += arahDash * kecepatanDash * delta;
            dashTimeLeft -= delta;

            if (dashTimeLeft <= 0f) {
                isDashing = false;
                arahDash = 0;
            }
        }

        if (jumpBufferTimer > 0) {
            jumpBufferTimer -= delta;
        }

        if (!isDead) {
            if (jumpBufferTimer > 0 && isColliding) {
                velocity.y = lompatan;
                isColliding = false;
                jumpBufferTimer = 0;
            }

            applyGravity(delta);
            updatePositionY(delta);
            updateVerticalDistance();
            Delta = delta;
        }

        if (velocity.y >= 0 && !isColliding)psm.jump(arah);
        else if (velocity.y < -40 && !isColliding) psm.fall(arah);

        updateAnimation(delta);
        updateCollider();
        if (velocity.x == 0 && isColliding) psm.idle(arah);
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
        } else velocity.y = 0f;
    }

    public void Lompat() {
        jumpBufferTimer = 0.1f;
    }

    public void startDashKiri() {
        if (!isDashing) {
            isDashing = true;
            psm.dash(Arah.KIRI);
            dashTimeLeft = waktuDash;
            kecepatanDash = jarakDash / waktuDash;
            arahDash = -1;
        }
    }

    public void startDashKanan() {
        if (!isDashing) {
            isDashing = true;
            psm.dash(Arah.KANAN);
            dashTimeLeft = waktuDash;
            kecepatanDash = jarakDash / waktuDash;
            arahDash = 1;
        }
    }

    public void setJarakDash(float jarakDash) {
        this.jarakDash = jarakDash;
    }

    public void Kiri() {
        position.x -= speed * Delta;
        arah = Arah.KIRI;
        psm.running(arah);
    }

    public void Kanan() {
        position.x += speed * Delta;
        arah = Arah.KANAN;
        psm.running(arah);
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
                groundSekarang = ground;
            } else {
                position.y -= overlapY;
                if (velocity.y > 0) velocity.y = 0;
            }
        }

        updateCollider();
    }

    public void setGroundSekarang(Ground groundSekarang) {
        this.groundSekarang = groundSekarang;
    }

    public void setGrounded(boolean grounded) {
        this.isColliding = grounded;
    }

    public void checkBoundaries(float batasKiri) {
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
            dieSound.play(1.0f);
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

    public Ground getGroundSekarang() {
        return this.groundSekarang;
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

    public void dispose() {
        dieSound.dispose();
    }
}
