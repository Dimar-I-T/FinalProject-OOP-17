package com.dimar.frontend.commands;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.dimar.frontend.states.GameStateManager;

public class ResumeCommand implements Command {
    private final GameStateManager gsm;

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
