package com.dimar.frontend;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;

public class Coin {
    private final Vector2 position;
    private final Rectangle collider;
    private final float radius = 10f;
    private boolean active;

    private float bobOffset;
    private Animation<TextureRegion> animation;
    private TextureRegion currentFrame;
    private float stateTime;

    public Coin(Vector2 startPosition) {
        position = startPosition;
        collider = new Rectangle(startPosition.x, startPosition.y, radius * 2, radius * 2);
        initiateAnimation();
    }

    private void initiateAnimation(){
        Texture coin = new Texture("coins.png");

        TextureRegion[][] coinFrames = TextureRegion.split(coin, 16, 16);
        TextureRegion[] coinAnimation = new TextureRegion[6];
        for (int i = 0; i < 6; i++){
            coinAnimation[i] = coinFrames[0][i];
        }

        animation = new Animation<>(1/12f, coinAnimation);
        animation.setPlayMode(Animation.PlayMode.LOOP);

        currentFrame = coinAnimation[0];
        stateTime = 0f;
    }

    private void updateAnimation(float delta){
        stateTime += delta;
        currentFrame = animation.getKeyFrame(stateTime, true);
    }

    public void update(float delta) {
        float bobSpeed = 10f;
        bobOffset += bobSpeed * delta;
        float drawY = position.y + (float)(Math.sin(bobOffset) * 5f);
        collider.setPosition(position.x - radius, drawY - radius);
        updateAnimation(delta);
    }

    public void renderShape(ShapeRenderer shapeRenderer) {
        float drawY = position.y + (float)(Math.sin(bobOffset) * 5f);
        shapeRenderer.setColor(1f, 1f, 0f,  1f);
        shapeRenderer.circle(position.x, drawY, radius);
    }

    public void render(SpriteBatch batch) {
        float renderSize = 3.5f * radius;
        float drawY = position.y + (float)(Math.sin(bobOffset) * 5f);
        batch.draw(currentFrame, position.x, drawY, renderSize, renderSize);
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

