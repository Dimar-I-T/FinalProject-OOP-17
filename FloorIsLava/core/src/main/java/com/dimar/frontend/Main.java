package com.dimar.frontend;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.ScreenUtils;
import com.dimar.frontend.commands.Command;
import com.dimar.frontend.commands.LompatCommand;
import com.dimar.frontend.commands.KananCommand;
import com.dimar.frontend.commands.KiriCommand;
import com.dimar.frontend.factories.GroundsFactory;
import com.dimar.frontend.observers.ScoreUIObserver;
import com.dimar.frontend.states.GameStateManager;
import com.dimar.frontend.states.MenuState;
import com.dimar.frontend.states.PlayingState;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * {@link com.badlogic.gdx.ApplicationListener} implementation shared by all platforms.
 */
public class Main extends ApplicationAdapter {
    private GameStateManager gsm;
    private SpriteBatch spriteBatch;
    private ShapeRenderer shapeRenderer;

    @Override
    public void create() {
        spriteBatch = new SpriteBatch();
        shapeRenderer = new ShapeRenderer();
        gsm = new GameStateManager();
        gsm.push(new MenuState(gsm));
    }

    @Override
    public void render() {
        ScreenUtils.clear(0.15f, 0.15f, 0.2f, 1f);
        gsm.update(Gdx.graphics.getDeltaTime());
        gsm.render(shapeRenderer, spriteBatch);
    }

    @Override
    public void resize(int width, int height) {
        gsm.resize(width, height);
    }

    @Override
    public void dispose() {
        super.dispose();
        if (spriteBatch != null) {
            spriteBatch.dispose();
        }
    }
}
