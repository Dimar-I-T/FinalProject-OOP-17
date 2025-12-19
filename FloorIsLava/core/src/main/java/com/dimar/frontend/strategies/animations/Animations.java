package com.dimar.frontend.strategies.animations;


import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.dimar.frontend.Arah;

import java.util.List;

public abstract class Animations {
    protected List<Animation<TextureRegion>> animations;
    protected Texture texture;
    protected TextureRegion textureRegion;
    protected int banyakFrame;
    protected String Kanan, Kiri;
    protected Animation.PlayMode playMode;
    boolean loop;

    protected void initiateAnimation(String arah, int index){
        TextureRegion[] animasi = new TextureRegion[banyakFrame];

        for (int i = 0; i < banyakFrame; i++){
            String internalPath = arah + (i + 1) + ".png";
            animasi[i] = new TextureRegion(new Texture(internalPath));
        }

        animations.add(index, new Animation<>(1f/12f, animasi));
        animations.get(index).setPlayMode(playMode);
    }

    public TextureRegion getCurrentFrame(float stateTime, Arah arah) {
        switch (arah){
            case KANAN -> {
                return animations.get(0).getKeyFrame(stateTime, loop);
            }
            case KIRI -> {
                return animations.get(1).getKeyFrame(stateTime, loop);
            }
            default -> throw new RuntimeException();
        }
    }
}
