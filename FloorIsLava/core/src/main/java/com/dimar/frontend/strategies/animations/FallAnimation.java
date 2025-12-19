package com.dimar.frontend.strategies.animations;

import com.badlogic.gdx.graphics.g2d.Animation;

import java.util.ArrayList;

public class FallAnimation extends Animations{
    public FallAnimation(){
        this.animations = new ArrayList<>(2);
        this.Kanan = "player/FALL/KANAN/FALL";
        this.Kiri = "player/FALL/KIRI/FALL";
        this.playMode = Animation.PlayMode.NORMAL;
        this.banyakFrame = 2;
        this.loop = false;

        initiateAnimation(Kanan, 0);
        initiateAnimation(Kiri, 1);
    }
}
