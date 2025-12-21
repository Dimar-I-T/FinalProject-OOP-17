package com.dimar.frontend.states;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Cursor;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.scenes.scene2d.*;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.Scaling;
import com.badlogic.gdx.utils.viewport.ExtendViewport;
import com.dimar.frontend.GameManager;
import com.dimar.frontend.commands.Command;
import com.dimar.frontend.commands.ResumeCommand;

import java.util.ArrayList;
import java.util.List;

public class PauseState implements GameState {

    private GameStateManager gsm;
    private Stage stage;
    private PlayingState playingState;
    private List<Command> commands = new ArrayList<>();

    // Assets
    private Texture titleTexture;
    private Texture resumeNormal, resumeHover;
    private Texture menuNormal, menuHover;

    // Sound Effects
    private Sound clickSound;
    private Sound hoverSound;

    public PauseState(GameStateManager gsm, PlayingState playingState) {
        this.gsm = gsm;
        this.playingState = playingState;
        commands.add(new ResumeCommand(gsm, playingState));

        // game dipause dan musiknya berhenti
        playingState.setPaused(true);
        // -----------------------------

        stage = new Stage(new ExtendViewport(1280, 720));
        Gdx.input.setInputProcessor(stage); // Set input ke UI Stage

        Gdx.input.setCursorCatched(false);
        loadAssets();
        buildUI();
    }

    private void loadAssets() {
        // Load Textures
        titleTexture = new Texture(Gdx.files.internal("pause/game-is-paused.png"));
        resumeNormal = new Texture(Gdx.files.internal("pause/resume-button.png"));
        resumeHover  = new Texture(Gdx.files.internal("pause/resume-button-hover.png"));
        menuNormal   = new Texture(Gdx.files.internal("pause/menu-button.png"));
        menuHover    = new Texture(Gdx.files.internal("pause/menu-button-hover.png"));

        // Load Sounds
        clickSound = Gdx.audio.newSound(Gdx.files.internal("audio/sound/click.wav"));
        hoverSound = Gdx.audio.newSound(Gdx.files.internal("audio/sound/hover.wav"));
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
        overlay.getColor().a = 0f;
        overlay.addAction(Actions.fadeIn(0.2f));
        stage.addActor(overlay);

        // ===== TABLE UI =====
        Table table = new Table();
        table.setFillParent(true);
        table.center();
        table.getColor().a = 0f;
        table.addAction(Actions.fadeIn(0.2f));
        stage.addActor(table);

        // ===== TITLE =====
        Image titleImage = new Image(titleTexture);
        titleImage.setScaling(Scaling.fit);

        // ===== BUTTONS =====
        Button resumeButton = createImageButton(
            resumeNormal,
            resumeHover,
            () -> {
                // Resume Game: Unpause PlayingState (Musik nyala lagi)
                playingState.setPaused(false);
                gsm.pop();
            }
        );

        Button menuButton = createImageButton(
            menuNormal,
            menuHover,
            () -> {
                // Ke Menu: End Game (Musik PlayingState akan didispose otomatis)
                GameManager.getInstance().endGame();
                gsm.clear();
                gsm.push(new MenuState(gsm));
            }
        );

        // ===== LAYOUT =====
        table.add(titleImage).width(600f).height(120f).padBottom(120f).row();
        table.add(resumeButton).width(360f).height(90f).padBottom(40f).row();
        table.add(menuButton).width(360f).height(90f);
    }

    private Button createImageButton(Texture normal, Texture hover, Runnable onClick) {
        Button.ButtonStyle style = new Button.ButtonStyle();
        style.up   = new TextureRegionDrawable(normal);
        style.over = new TextureRegionDrawable(hover);
        style.down = new TextureRegionDrawable(hover);

        Button button = new Button(style);

        button.setTransform(true);
        button.setOrigin(180f, 45f);

        // Listener Hover
        button.addListener(new InputListener() {
            @Override
            public void enter(InputEvent event, float x, float y, int pointer, Actor fromActor) {
                if (pointer == -1) {
                    hoverSound.play();
                    button.clearActions();
                    button.addAction(Actions.scaleTo(1.05f, 1.05f, 0.1f));
                    Gdx.graphics.setSystemCursor(Cursor.SystemCursor.Hand);
                }
            }

            @Override
            public void exit(InputEvent event, float x, float y, int pointer, Actor toActor) {
                if (pointer == -1) {
                    button.clearActions();
                    button.addAction(Actions.scaleTo(1f, 1f, 0.1f));
                    Gdx.graphics.setSystemCursor(Cursor.SystemCursor.Arrow);
                }
            }
        });

        // Listener Click
        button.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                clickSound.play();
                Gdx.graphics.setSystemCursor(Cursor.SystemCursor.Arrow);
                onClick.run();
            }
        });

        return button;
    }

    @Override
    public void resize(int width, int height) {
        stage.getViewport().update(width, height, true);
        playingState.resize(width, height);
    }

    @Override
    public void update(float delta) {
        for (Command command : commands) {
            command.execute();
        }
        stage.act(delta);
    }

    @Override
    public void render(ShapeRenderer shapeRenderer, SpriteBatch batch) {
        playingState.render(shapeRenderer, batch);
        stage.draw();
    }

    @Override
    public void dispose() {
        Gdx.input.setInputProcessor(null);
        Gdx.graphics.setSystemCursor(Cursor.SystemCursor.Arrow);

        stage.dispose();
        titleTexture.dispose();
        resumeNormal.dispose();
        resumeHover.dispose();
        menuNormal.dispose();
        menuHover.dispose();
        clickSound.dispose();
        hoverSound.dispose();
    }
}
