package com.dimar.frontend.strategies.animations;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.dimar.frontend.Arah;
import com.dimar.frontend.states.playerStates.*;

import java.util.ArrayList;
import java.util.List;

public class AnimationSelector {
    private List<Animations> animations;

    public AnimationSelector(){
        animations = new ArrayList<>();
        animations.add(0, new IdleAnimation());
        animations.add(1, new RunAnimation());
        animations.add(2, new JumpAnimation());
        animations.add(3, new FallAnimation());
    }

    public TextureRegion getCurrentFrame(PlayerState state, float stateTime, Arah arah){
        int n = 0;
        if (state instanceof JumpState) {
            n = 2;
        } else if (state instanceof FallState) {
            n = 3;
        } else if (state instanceof RunningState) {
            n = 1;
        }
        return animations.get(n).getCurrentFrame(stateTime, arah);
    }
}
