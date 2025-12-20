package com.dimar.frontend.strategies.animations;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.dimar.frontend.Arah;

import java.util.ArrayList;

public class JumpAnimation extends Animations{
    public JumpAnimation(){
        this.animations = new ArrayList<>(2);
        this.Kanan = "player/JUMP/KANAN/JUMP";
        this.Kiri = "player/JUMP/KIRI/JUMP";
        this.playMode = Animation.PlayMode.NORMAL;
        this.banyakFrame = 2;
        this.loop = false;

        initiateAnimation(Kanan, 0);
        initiateAnimation(Kiri, 1);
    }
}
