package com.dimar.frontend.commands;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.dimar.frontend.Player;

public class KananCommand implements Command {
    private Player player;
    public KananCommand(Player player) {
        this.player = player;
    }

    @Override
    public void execute() {
        if (!player.getIsDead() && Gdx.input.isKeyPressed(Input.Keys.D)) {
            player.Kanan();
        }
    }
}
