package com.dimar.frontend.states;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.scenes.scene2d.*;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.dimar.frontend.GameManager;

public class PauseState implements GameState {

    private GameStateManager gsm;
    private Stage stage;
    private PlayingState playingState;

    // Assets
    private Texture titleTexture;
    private Texture resumeNormal, resumeHover;
    private Texture menuNormal, menuHover;

    public PauseState(GameStateManager gsm, PlayingState playingState) {
        this.gsm = gsm;
        this.playingState = playingState;

        stage = new Stage(new ScreenViewport());
        Gdx.input.setInputProcessor(stage);

        loadAssets();
        buildUI();
    }

    private void loadAssets() {
        titleTexture = new Texture(Gdx.files.internal("pause/game-is-paused.png"));

        resumeNormal = new Texture(Gdx.files.internal("pause/resume-button.png"));
        resumeHover  = new Texture(Gdx.files.internal("pause/resume-button-hover.png"));

        menuNormal   = new Texture(Gdx.files.internal("pause/menu-button.png"));
        menuHover    = new Texture(Gdx.files.internal("pause/menu-button-hover.png"));
    }

    private void buildUI() {

        // ===== OVERLAY HITAM TRANSPARAN =====
        Pixmap pm = new Pixmap(1, 1, Pixmap.Format.RGBA8888);
        pm.setColor(new Color(0, 0, 0, 0.5f));
        pm.fill();
        Texture overlayTexture = new Texture(pm);
        pm.dispose();

        Image overlay = new Image(overlayTexture);
        overlay.setFillParent(true);
        overlay.getColor().a = 0f; // start invisible
        overlay.addAction(Actions.fadeIn(0.2f)); // fade-in 0.2s
        stage.addActor(overlay);

        // ===== TABLE UI =====
        Table table = new Table();
        table.setFillParent(true);
        table.getColor().a = 0f;
        table.addAction(Actions.fadeIn(0.2f));
        stage.addActor(table);

        // ===== TITLE =====
        Image titleImage = new Image(titleTexture);

        // ===== BUTTONS =====
        Button resumeButton = createImageButton(
            resumeNormal,
            resumeHover,
            () -> gsm.pop()
        );

        Button menuButton = createImageButton(
            menuNormal,
            menuHover,
            () -> {
                GameManager.getInstance().endGame();
                gsm.set(new MenuState(gsm));
            }
        );

        // ===== LAYOUT =====
        table.add(titleImage)
            .padBottom(150)
            .row();

        table.add(resumeButton)
            .padBottom(30)
            .row();

        table.add(menuButton);
    }

    private Button createImageButton(Texture normal, Texture hover, Runnable onClick) {
        Button.ButtonStyle style = new Button.ButtonStyle();
        style.up   = new TextureRegionDrawable(normal);
        style.over = new TextureRegionDrawable(hover);
        style.down = new TextureRegionDrawable(hover);

        Button button = new Button(style);

        // hover scale effect
        button.addListener(new InputListener() {
            @Override
            public void enter(InputEvent event, float x, float y, int pointer, Actor fromActor) {
                button.addAction(Actions.scaleTo(1.05f, 1.05f, 0.1f));
            }

            @Override
            public void exit(InputEvent event, float x, float y, int pointer, Actor toActor) {
                button.addAction(Actions.scaleTo(1f, 1f, 0.1f));
            }
        });

        button.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                onClick.run();
            }
        });

        return button;
    }

    @Override
    public void resize(int width, int height) {
        stage.getViewport().update(width, height, true);
    }

    @Override
    public void update(float delta) {
        stage.act(delta);
    }

    @Override
    public void render(ShapeRenderer shapeRenderer, SpriteBatch batch) {
        playingState.render(shapeRenderer, new SpriteBatch());
        stage.draw();
    }

    @Override
    public void dispose() {
        stage.dispose();

        titleTexture.dispose();
        resumeNormal.dispose();
        resumeHover.dispose();
        menuNormal.dispose();
        menuHover.dispose();
    }
}
