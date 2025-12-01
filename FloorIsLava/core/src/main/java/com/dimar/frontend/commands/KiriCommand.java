package com.dimar.frontend.commands;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.dimar.frontend.Player;

public class KiriCommand implements Command {
    private Player player;
    public KiriCommand(Player player) {
        this.player = player;
    }

    @Override
    public void execute() {
        if (!player.getIsDead() && Gdx.input.isKeyPressed(Input.Keys.A)) {
            player.Kiri();
        }
    }
}
