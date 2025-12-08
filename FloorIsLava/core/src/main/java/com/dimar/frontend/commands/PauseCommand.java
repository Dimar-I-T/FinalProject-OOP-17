package com.dimar.frontend.commands;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.dimar.frontend.states.GameStateManager;
import com.dimar.frontend.states.PauseState;
import com.dimar.frontend.states.PlayingState;

public class PauseCommand implements Command {
    private GameStateManager gsm;
    private PlayingState playingState;

    public PauseCommand(GameStateManager gsm, PlayingState playingState) {
        this.gsm = gsm;
        this.playingState = playingState;
    }

    @Override
    public void execute() {
        if (Gdx.input.isKeyJustPressed(Input.Keys.ESCAPE)) {
            gsm.push(new PauseState(gsm, playingState));
        }
    }
}
