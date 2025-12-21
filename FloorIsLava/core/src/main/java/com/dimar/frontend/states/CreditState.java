package com.dimar.frontend.states;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music; // Import Music
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Cursor;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.dimar.frontend.backgrounds.LoginBackground;

public class CreditState implements GameState {
    private final GameStateManager gsm;
    private final Stage stage;
    private LoginBackground background;
    private Skin skin;
    private Sound clickSound;
    private Sound hoverSound;
    private Music bgMusic; // Variabel Music

    public CreditState(GameStateManager gsm) {
        this.gsm = gsm;
        this.stage = new Stage(new ScreenViewport());
        this.background = new LoginBackground();

        Gdx.input.setInputProcessor(stage);

        // Load Sounds
        clickSound = Gdx.audio.newSound(Gdx.files.internal("audio/sound/click.wav"));
        hoverSound = Gdx.audio.newSound(Gdx.files.internal("audio/sound/hover.wav"));

        // --- LOAD & PLAY MUSIC ---
        bgMusic = Gdx.audio.newMusic(Gdx.files.internal("audio/music/title-screen.wav"));
        bgMusic.setLooping(true);
        bgMusic.setVolume(0.5f);
        bgMusic.play();

        createSkin();
        buildUI();
    }

    private void createSkin() {
        skin = new Skin();

        BitmapFont pixelFont = new BitmapFont(Gdx.files.internal("04b30.fnt"));
        pixelFont.getRegion().getTexture().setFilter(Texture.TextureFilter.Nearest, Texture.TextureFilter.Nearest);
        skin.add("pixelFont", pixelFont);

        Color navyColor = new Color(0.1f, 0.1f, 0.45f, 1f);
        Color titleColor = new Color(0.9f, 0.3f, 0.0f, 1f);

        Label.LabelStyle titleStyle = new Label.LabelStyle();
        titleStyle.font = pixelFont;
        titleStyle.fontColor = titleColor;
        skin.add("titleStyle", titleStyle);

        Label.LabelStyle textStyle = new Label.LabelStyle();
        textStyle.font = pixelFont;
        textStyle.fontColor = navyColor;
        skin.add("textStyle", textStyle);

        Texture menuTexture = new Texture(Gdx.files.internal("pause/menu-button.png"));
        Texture menuHoverTexture = new Texture(Gdx.files.internal("pause/menu-button-hover.png"));

        menuTexture.setFilter(Texture.TextureFilter.Nearest, Texture.TextureFilter.Nearest);
        menuHoverTexture.setFilter(Texture.TextureFilter.Nearest, Texture.TextureFilter.Nearest);

        skin.add("menuTexture", menuTexture);
        skin.add("menuHoverTexture", menuHoverTexture);
    }

    private void buildUI() {
        Table mainTable = new Table();
        mainTable.setFillParent(true);
        mainTable.center();
        stage.addActor(mainTable);

        Label titleLabel = new Label("Development Team:", skin, "titleStyle");
        titleLabel.setFontScale(1.0f);
        titleLabel.setAlignment(Align.center);

        String teamText =
            "Dimar Ilham Tamara\n(Game Designer, Lead Programmer, Backend Developer)\n\n" +
                "Raihan Muhammad Nafis Al-Kautsar\n(Character Animator, UI/UX Designer, UI Programmer)\n\n" +
                "Naufal Rahman\n(UI/UX Designer, UI Programmer, Audio Designer)\n\n" +
                "Ahmad Malik Prasetyo\n(UI/UX Designer, UI Programmer)";

        Label membersLabel = new Label(teamText, skin, "textStyle");
        membersLabel.setFontScale(0.5f);
        membersLabel.setAlignment(Align.center);

        TextureRegionDrawable menuUp = new TextureRegionDrawable(new TextureRegion(skin.get("menuTexture", Texture.class)));
        TextureRegionDrawable menuOver = new TextureRegionDrawable(new TextureRegion(skin.get("menuHoverTexture", Texture.class)));

        ImageButton.ImageButtonStyle menuStyle = new ImageButton.ImageButtonStyle();
        menuStyle.imageUp = menuUp;
        menuStyle.imageOver = menuOver;

        ImageButton menuButton = new ImageButton(menuStyle);

        menuButton.setTransform(true);
        menuButton.setOrigin(180f, 45f);

        menuButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                clickSound.play();
                Gdx.graphics.setSystemCursor(Cursor.SystemCursor.Arrow);
                gsm.set(new MenuState(gsm));
            }

            @Override
            public void enter(InputEvent event, float x, float y, int pointer, Actor fromActor) {
                if (pointer == -1) {
                    hoverSound.play();
                    menuButton.clearActions();
                    menuButton.addAction(Actions.scaleTo(1.1f, 1.1f, 0.1f));
                    Gdx.graphics.setSystemCursor(Cursor.SystemCursor.Hand);
                }
                super.enter(event, x, y, pointer, fromActor);
            }

            @Override
            public void exit(InputEvent event, float x, float y, int pointer, Actor toActor) {
                if (pointer == -1) {
                    menuButton.clearActions();
                    menuButton.addAction(Actions.scaleTo(1.0f, 1.0f, 0.1f));
                    Gdx.graphics.setSystemCursor(Cursor.SystemCursor.Arrow);
                }
                super.exit(event, x, y, pointer, toActor);
            }
        });

        mainTable.add(titleLabel).padBottom(30f).row();
        mainTable.add(membersLabel).padBottom(50f).row();
        mainTable.add(menuButton).width(360f).height(90f);
    }

    @Override
    public void update(float delta) {
        background.update(delta);
        stage.act(delta);
    }

    @Override
    public void render(ShapeRenderer shapeRenderer, SpriteBatch spriteBatch) {
        background.render(spriteBatch, this);
        stage.draw();
    }

    @Override
    public void resize(int width, int height) {
        stage.getViewport().update(width, height, true);
    }

    @Override
    public void dispose() {
        Gdx.graphics.setSystemCursor(Cursor.SystemCursor.Arrow);
        background.dispose();
        stage.dispose();
        skin.dispose();
        clickSound.dispose();
        hoverSound.dispose();
        if (bgMusic != null) {
            bgMusic.stop();
            bgMusic.dispose();
        }
    }
}
