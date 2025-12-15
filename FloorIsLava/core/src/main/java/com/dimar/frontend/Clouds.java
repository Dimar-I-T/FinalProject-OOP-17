package com.dimar.frontend;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;

public class Clouds {
    private TextureRegion texture;
    private float width = Gdx.graphics.getWidth() * 0.75f;
    private float height = Gdx.graphics.getHeight() * 0.75f;
    private float gap;
    private Vector2 position;
    private float velocityX;

    public Clouds(float x, float y, float velocityX, float gap){
        this.gap = gap;
        this.texture = new TextureRegion();
        this.velocityX = velocityX;
        this.position = new Vector2(x, y);
    }

    public void setTexture(Texture texture) {
        this.texture.setRegion(texture);
    }

    public void update(float delta){
        this.position.x -= velocityX * delta;
    }

    public void render(SpriteBatch batch){
        for (float x = position.x; x < Gdx.graphics.getWidth() + (gap * Gdx.graphics.getWidth()); x += width){
            batch.draw(texture, x, position.y, width, height);
        }
    }

    public void dispose(){
        texture.getTexture().dispose();
    }
}
