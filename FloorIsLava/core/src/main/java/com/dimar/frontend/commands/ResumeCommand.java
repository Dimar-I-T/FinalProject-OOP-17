package com.dimar.frontend.commands;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.dimar.frontend.states.GameStateManager;
import com.dimar.frontend.states.PauseState;

public class ResumeCommand implements Command {
    private GameStateManager gsm;

    public ResumeCommand(GameStateManager gsm) {
        this.gsm = gsm;
    }

    @Override
    public void execute() {
        if (Gdx.input.isKeyJustPressed(Input.Keys.ESCAPE)) {
            gsm.pop();
        }
    }
}
