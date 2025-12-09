package com.dimar.frontend.commands;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.dimar.frontend.Player;

public class DashKiriCommand implements Command{
    private Player player;
    public DashKiriCommand(Player player) {
        this.player = player;
    }

    @Override
    public void execute() {
        if (Gdx.input.isKeyJustPressed(Input.Keys.Q)) {
            player.startDashKiri();
        }
    }
}
