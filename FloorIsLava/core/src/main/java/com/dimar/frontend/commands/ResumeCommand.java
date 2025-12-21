package com.dimar.frontend.commands;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.dimar.frontend.states.GameStateManager;
import com.dimar.frontend.states.PlayingState;

public class ResumeCommand implements Command {
    private final GameStateManager gsm;
    private PlayingState playingState;

    public ResumeCommand(GameStateManager gsm, PlayingState playingState) {
        this.gsm = gsm;
        this.playingState = playingState;
    }

    @Override
    public void execute() {
        if (Gdx.input.isKeyJustPressed(Input.Keys.ESCAPE)) {
            playingState.setPaused(false);
            gsm.pop();
        }
    }
}
