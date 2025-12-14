package com.dimar.frontend.states.playerStates;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.dimar.frontend.Arah;

public abstract class PlayerState {
    Animation<Texture> KANAN;
    Animation<Texture> KIRI;
    private Arah arah;

    public PlayerState(Arah arah){
        this.arah = arah;
    }

    public Arah getArah() {
        return arah;
    }
}
