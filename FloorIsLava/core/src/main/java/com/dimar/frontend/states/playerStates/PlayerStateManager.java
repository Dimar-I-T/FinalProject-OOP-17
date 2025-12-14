package com.dimar.frontend.states.playerStates;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.dimar.frontend.Arah;

import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

public class PlayerStateManager {
    private Stack<PlayerState> states = new Stack<>();
    private List<Animation<Texture>> animations = new ArrayList<>();

    public PlayerStateManager(){
        states.push(new IdleState(Arah.KANAN));
    }

    public void running(Arah arah){
        removeState();
        states.push(new RunningState(arah));
    }

    public void jump(Arah arah){
        removeState();
        states.push(new JumpState(arah));
    }

    public void fall(Arah arah){
        if(!(states.peek() instanceof IdleState)){
            removeState();
            states.push(new FallState(arah));
        }
    }

    public void dash(Arah arah){
        removeState();
        states.push(new DashState(arah));
    }

    public void idle(Arah arah){
        removeState();
        states.push(new IdleState(arah));
    }

    private void removeState(){
        states.pop();
    }

    public PlayerState getCurrentState(){
        return states.peek();
    }
}
