package com.dimar.frontend;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.dimar.frontend.states.playerStates.*;

import java.util.ArrayList;
import java.util.List;

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
    private boolean isDead;
    private float verticalDistanceTravelled = 0f;
    private final float waktuDash = 0.1f;
    private float jarakDash = 500f;

    private final PlayerStateManager psm;

    private List<Animation<TextureRegion>> animations;
    private TextureRegion currentFrame;
    private float stateTime;
    private Arah arah;


    float dashTimeLeft = 0f;
    float kecepatanDash;

    int arahDash = 0;

    private Ground groundSekarang;

    public Player(Vector2 startPosition) {
        widthAwal = Gdx.graphics.getWidth();
        this.position = new Vector2(startPosition.x, startPosition.y);
        this.startPosition = new Vector2(startPosition.x, startPosition.y);
        velocity = new Vector2(0, 0);
        collider = new Rectangle(startPosition.x, startPosition.y, WIDTH, HEIGHT);
        psm = new PlayerStateManager();
        arah = psm.getCurrentState().getArah();
        animations = new ArrayList<>();

        initializeAnimation();
    }

    private void initializeAnimation(){
        TextureRegion[] idleTextureKanan = new TextureRegion[7];
        TextureRegion[] idleTextureKiri = new TextureRegion[7];
        TextureRegion[] runTextureKanan = new TextureRegion[8];
        TextureRegion[] runTextureKiri = new TextureRegion[8];
        TextureRegion[] jumpTextureKanan = new TextureRegion[3];
        TextureRegion[] jumpTextureKiri = new TextureRegion[3];
        TextureRegion[] fallTextureKanan = new TextureRegion[2];
        TextureRegion[] fallTextureKiri = new TextureRegion[2];


        for (int i = 0; i < 7; i++){
            String internalPath = "player/IDLE/KANAN/IDLE" + (i + 1) + ".png";
            idleTextureKanan[i] = new TextureRegion(new Texture(internalPath));
        }

        animations.add(0, new Animation<>(1f/12f, idleTextureKanan));
        animations.get(0).setPlayMode(Animation.PlayMode.LOOP);

        for (int i = 0; i < 7; i++){
            String internalPath = "player/IDLE/KIRI/IDLE" + (i + 1) + ".png";
            idleTextureKiri[i] = new TextureRegion(new Texture(internalPath));
        }

        animations.add(1, new Animation<>(1f/12f, idleTextureKiri));
        animations.get(1).setPlayMode(Animation.PlayMode.LOOP);

        for (int i = 0; i < 8; i++){
            String internalPath = "player/RUN/KANAN/RUN" + (i + 1) + ".png";
            runTextureKanan[i] = new TextureRegion(new Texture(internalPath));
        }

        animations.add(2, new Animation<>(1f/12f, runTextureKanan));
        animations.get(2).setPlayMode(Animation.PlayMode.LOOP);

        for (int i = 0; i < 8; i++){
            String internalPath = "player/RUN/KIRI/RUN" + (i + 1) + ".png";
            runTextureKiri[i] = new TextureRegion(new Texture(internalPath));
        }

        animations.add(3, new Animation<>(1f/12f, runTextureKiri));
        animations.get(3).setPlayMode(Animation.PlayMode.LOOP);

        for (int i = 0; i < 3; i++){
            String internalPath = "player/JUMP/KANAN/JUMP" + (i + 1) + ".png";
            jumpTextureKanan[i] = new TextureRegion(new Texture(internalPath));
        }

        animations.add(4, new Animation<>(1f/12f, jumpTextureKanan));
        animations.get(4).setPlayMode(Animation.PlayMode.NORMAL);

        for (int i = 0; i < 3; i++){
            String internalPath = "player/JUMP/KIRI/JUMP" + (i + 1) + ".png";
            jumpTextureKiri[i] = new TextureRegion(new Texture(internalPath));
        }

        animations.add(5, new Animation<>(1f/12f, jumpTextureKiri));
        animations.get(5).setPlayMode(Animation.PlayMode.NORMAL);

        for (int i = 0; i < 2; i++){
            String internalPath = "player/FALL/KANAN/FALL" + (i + 1) + ".png";
            fallTextureKanan[i] = new TextureRegion(new Texture(internalPath));
        }

        animations.add(6, new Animation<>(1f/12f, fallTextureKanan));
        animations.get(6).setPlayMode(Animation.PlayMode.NORMAL);

        for (int i = 0; i < 2; i++){
            String internalPath = "player/FALL/KIRI/FALL" + (i + 1) + ".png";
            fallTextureKiri[i] = new TextureRegion(new Texture(internalPath));
        }

        animations.add(7, new Animation<>(1f/12f, fallTextureKiri));
        animations.get(7).setPlayMode(Animation.PlayMode.NORMAL);

        currentFrame = idleTextureKanan[0];
        stateTime = 0f;
    }

    public void setSize(float size){
        this.WIDTH = size;
        this.HEIGHT = size;
    }

    private void updateAnimation(float delta){
        stateTime += delta;
        if (psm.getCurrentState() instanceof JumpState) {
            if(psm.getCurrentState().getArah() == Arah.KIRI) {
                currentFrame = animations.get(5).getKeyFrame(stateTime, false);
            }
            else {
                currentFrame = animations.get(4).getKeyFrame(stateTime, false);
            }
        } else if(psm.getCurrentState() instanceof FallState){
            if(psm.getCurrentState().getArah() == Arah.KIRI) {
                currentFrame = animations.get(7).getKeyFrame(stateTime, false);
            }
            else {
                currentFrame = animations.get(6).getKeyFrame(stateTime, false);
            }
        } else if (psm.getCurrentState() instanceof DashState) {
            // Tambahin partikel dash nanti
        } else if (psm.getCurrentState() instanceof RunningState) {
            if(psm.getCurrentState().getArah() == Arah.KIRI) {
                currentFrame = animations.get(3).getKeyFrame(stateTime, true);
            }
            else {
                currentFrame = animations.get(2).getKeyFrame(stateTime, true);
            }
        } else {
            if(psm.getCurrentState().getArah() == Arah.KIRI) {
                currentFrame = animations.get(1).getKeyFrame(stateTime, true);
            }
            else {
                currentFrame = animations.get(0).getKeyFrame(stateTime, true);
            }
        }
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
        if (psm.getCurrentState() instanceof DashState) {
            position.x += arahDash * kecepatanDash * delta;
            dashTimeLeft -= delta;

            if (dashTimeLeft <= 0f) {
                psm.idle(arah);
                arahDash = 0;
            }
        }

        if (!isDead) {
            applyGravity(delta);
            updatePositionY(delta);
            updateVerticalDistance();
            Delta = delta;
        }
        //System.out.println(isDead);
        if (velocity.y >= 0 && !isColliding)psm.jump(arah); // Masih ada bug
        else if (velocity.y < 0) psm.fall(arah); // Masih ada bug

        updateAnimation(delta);
        updateCollider();
        if (velocity.x == 0 && isColliding) psm.idle(arah); // Masih ada bug
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
        if (isColliding) {
            velocity.y = lompatan;
            isColliding = false;
        }
    }

    public void startDashKiri() {
        if (!(psm.getCurrentState() instanceof DashState)) {
            psm.dash(Arah.KIRI);
            dashTimeLeft = waktuDash;
            kecepatanDash = jarakDash / waktuDash;
            arahDash = -1;
        }
    }

    public void startDashKanan() {
        if (!(psm.getCurrentState() instanceof DashState)) {
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
}
