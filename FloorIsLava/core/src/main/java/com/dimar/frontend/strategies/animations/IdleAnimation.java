package com.dimar.frontend.strategies.animations;
import com.badlogic.gdx.graphics.g2d.Animation;

import java.util.ArrayList;

public class IdleAnimation extends Animations{

    public IdleAnimation(){
        this.animations = new ArrayList<>(2);
        this.Kanan = "player/IDLE/KANAN/IDLE";
        this.Kiri = "player/IDLE/KIRI/IDLE";
        this.playMode = Animation.PlayMode.LOOP;
        this.banyakFrame = 7;
        this.loop = true;

        initiateAnimation(Kanan, 0);
        initiateAnimation(Kiri, 1);
    }
}
