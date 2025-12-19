package com.dimar.frontend.strategies.animations;
import com.badlogic.gdx.graphics.g2d.Animation;

import java.util.ArrayList;

public class RunAnimation extends Animations{

    public RunAnimation(){
        this.animations = new ArrayList<>(2);
        this.Kanan = "player/RUN/KANAN/RUN";
        this.Kiri = "player/RUN/KIRI/RUN";
        this.playMode = Animation.PlayMode.NORMAL;
        this.banyakFrame = 8;
        this.loop = true;

        initiateAnimation(Kanan, 0);
        initiateAnimation(Kiri, 1);
    }
}
