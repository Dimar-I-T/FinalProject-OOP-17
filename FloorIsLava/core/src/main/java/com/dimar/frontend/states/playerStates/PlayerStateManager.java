package com.dimar.frontend.states.playerStates;
import com.dimar.frontend.Arah;

import java.util.Stack;

public class PlayerStateManager {
    private Stack<PlayerState> states = new Stack<>();

    public PlayerStateManager(){
        states.push(new IdleState(Arah.KANAN));
    }

    public void running(Arah arah){
        if (!(states.peek() instanceof JumpState)){
            removeState();
            states.push(new RunningState(arah));
        }
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
