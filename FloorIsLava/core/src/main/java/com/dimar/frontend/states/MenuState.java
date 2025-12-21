package com.dimar.frontend.states;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music; // Import Music
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Cursor;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.dimar.frontend.GameManager;
import com.dimar.frontend.Leaderboard;
import com.dimar.frontend.MenuBackground;

import java.util.List;

public class MenuState implements GameState {
    private MenuBackground background;
    private final GameStateManager gsm;
    private final Stage stage;
    private Skin skin;
    private Sound clickSound; // suara Klik
    private Sound hoverSound; // suara Hover
    private Music bgMusic;    // Musik Background

    public MenuState(GameStateManager gsm) {
        background = new MenuBackground();
        this.gsm = gsm;
        stage = new Stage(new ScreenViewport());
        Gdx.input.setInputProcessor(stage);

        // Load Sounds
        clickSound = Gdx.audio.newSound(Gdx.files.internal("audio/sound/click.wav"));
        hoverSound = Gdx.audio.newSound(Gdx.files.internal("audio/sound/hover.wav"));

        // Load & Play Music Loop
        bgMusic = Gdx.audio.newMusic(Gdx.files.internal("audio/music/title-screen.wav"));
        bgMusic.setLooping(true);
        bgMusic.setVolume(0.5f); // Set volume 50%
        bgMusic.play();

        createBasicSkin();
        buildUI();
    }

    private void createBasicSkin() {
        skin = new Skin();

        // Load Assets Gambar
        Texture playTexture = new Texture(Gdx.files.internal("menu/play-button.png"));
        Texture loginTexture = new Texture(Gdx.files.internal("menu/login-button.png"));
        Texture playHoverTexture = new Texture(Gdx.files.internal("menu/play-button-hover.png"));
        Texture loginHoverTexture = new Texture(Gdx.files.internal("menu/login-button-hover.png"));

        // Filter Pixel
        playTexture.setFilter(Texture.TextureFilter.Nearest, Texture.TextureFilter.Nearest);
        loginTexture.setFilter(Texture.TextureFilter.Nearest, Texture.TextureFilter.Nearest);
        playHoverTexture.setFilter(Texture.TextureFilter.Nearest, Texture.TextureFilter.Nearest);
        loginHoverTexture.setFilter(Texture.TextureFilter.Nearest, Texture.TextureFilter.Nearest);

        skin.add("playTexture", playTexture);
        skin.add("loginTexture", loginTexture);
        skin.add("playHoverTexture", playHoverTexture);
        skin.add("loginHoverTexture", loginHoverTexture);

        // Font
        BitmapFont defaultFont = new BitmapFont();
        skin.add("default", defaultFont);
        BitmapFont pixelFont = new BitmapFont(Gdx.files.internal("04b30.fnt"));
        pixelFont.getRegion().getTexture().setFilter(Texture.TextureFilter.Nearest, Texture.TextureFilter.Nearest);
        skin.add("pixelFont", pixelFont);

        // Warna Background Text
        Pixmap pixmapDG = new Pixmap(1,1, Pixmap.Format.RGBA8888);
        pixmapDG.setColor(Color.DARK_GRAY);
        pixmapDG.fill();
        skin.add("dark_gray", new Texture(pixmapDG));
        pixmapDG.dispose();

        // Palet Warna
        Color titleColor = new Color(0.9f, 0.3f, 0.0f, 1f);
        Color skyTextColor = new Color(0.1f, 0.1f, 0.35f, 1f);
        Color groundTextColor = new Color(1.0f, 0.85f, 0.25f, 1f);

        // Styles
        Label.LabelStyle titleStyle = new Label.LabelStyle();
        titleStyle.font = pixelFont;
        titleStyle.fontColor = titleColor;
        skin.add("titleStyle", titleStyle);

        Label.LabelStyle infoStyle = new Label.LabelStyle();
        infoStyle.font = pixelFont;
        infoStyle.fontColor = skyTextColor;
        skin.add("infoStyle", infoStyle);

        Label.LabelStyle leaderboardStyle = new Label.LabelStyle();
        leaderboardStyle.font = pixelFont;
        leaderboardStyle.fontColor = groundTextColor;
        skin.add("leaderboard", leaderboardStyle);

        Label.LabelStyle defaultStyle = new Label.LabelStyle();
        defaultStyle.font = defaultFont;
        defaultStyle.fontColor = Color.WHITE;
        skin.add("default", defaultStyle);
    }

    private void buildUI() {
        // Setup Tables
        Table mainTable = new Table();
        mainTable.setFillParent(true);
        mainTable.center();
        stage.addActor(mainTable);

        Table leaderboardTable = new Table();
        leaderboardTable.setFillParent(true);
        leaderboardTable.bottom().right().pad(20f);
        stage.addActor(leaderboardTable);

        // Judul dengan Animasi
        Label judul = new Label("FLOOR IS LAVA", skin, "titleStyle");
        judul.setFontScale(2.0f); // Default scale
        judul.addAction(Actions.forever(
            Actions.sequence(
                Actions.moveBy(0, 15f, 1f, Interpolation.sineOut),
                Actions.moveBy(0, -15f, 1f, Interpolation.sineIn)
            )
        ));
        mainTable.add(judul).padBottom(50f).colspan(2);
        mainTable.row();

        // Info Player
        Label playerLabel = new Label("Hello, Guest!", skin, "infoStyle");
        playerLabel.setFontScale(1.0f);
        mainTable.add(playerLabel).padBottom(10f).colspan(2);
        mainTable.row();

        Label skorLabel = new Label("", skin, "infoStyle");
        skorLabel.setFontScale(1.0f);
        mainTable.add(skorLabel).padBottom(10f).colspan(2);
        mainTable.row();

        Label coinsCollectedLabel = new Label("", skin, "infoStyle");
        coinsCollectedLabel.setFontScale(1.0f);
        mainTable.add(coinsCollectedLabel).padBottom(50f).colspan(2);
        mainTable.row();

        // Setup Tombol
        TextureRegionDrawable playUp = new TextureRegionDrawable(new TextureRegion(skin.get("playTexture", Texture.class)));
        TextureRegionDrawable playOver = new TextureRegionDrawable(new TextureRegion(skin.get("playHoverTexture", Texture.class)));

        TextureRegionDrawable loginUp = new TextureRegionDrawable(new TextureRegion(skin.get("loginTexture", Texture.class)));
        TextureRegionDrawable loginOver = new TextureRegionDrawable(new TextureRegion(skin.get("loginHoverTexture", Texture.class)));

        ImageButton.ImageButtonStyle playStyle = new ImageButton.ImageButtonStyle();
        playStyle.imageUp = playUp;
        playStyle.imageOver = playOver;

        ImageButton.ImageButtonStyle loginStyle = new ImageButton.ImageButtonStyle();
        loginStyle.imageUp = loginUp;
        loginStyle.imageOver = loginOver;

        ImageButton playButton = new ImageButton(playStyle);
        ImageButton loginButton = new ImageButton(loginStyle);

        // Set Origin Center buat scaling/perbesaran
        playButton.setTransform(true);
        playButton.setOrigin(Align.center);
        loginButton.setTransform(true);
        loginButton.setOrigin(Align.center);

        // Listener Play Button
        playButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                clickSound.play(); // Play Click Sound
                Gdx.graphics.setSystemCursor(Cursor.SystemCursor.Arrow);
                gsm.set(new PlayingState(gsm));
            }

            @Override
            public void enter(InputEvent event, float x, float y, int pointer, Actor fromActor) {
                if (pointer == -1) {
                    hoverSound.play(); // Play Hover Sound
                    playButton.clearActions();
                    playButton.addAction(Actions.scaleTo(1.1f, 1.1f, 0.1f));
                    Gdx.graphics.setSystemCursor(Cursor.SystemCursor.Hand);
                }
                super.enter(event, x, y, pointer, fromActor);
            }

            @Override
            public void exit(InputEvent event, float x, float y, int pointer, Actor toActor) {
                if (pointer == -1) {
                    playButton.clearActions();
                    playButton.addAction(Actions.scaleTo(1.0f, 1.0f, 0.1f));
                    Gdx.graphics.setSystemCursor(Cursor.SystemCursor.Arrow);
                }
                super.exit(event, x, y, pointer, toActor);
            }
        });

        // Listener Login Button
        loginButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                clickSound.play(); // Play Click Sound
                Gdx.graphics.setSystemCursor(Cursor.SystemCursor.Arrow);
                gsm.set(new LoginState(gsm));
            }

            @Override
            public void enter(InputEvent event, float x, float y, int pointer, Actor fromActor) {
                if (pointer == -1) {
                    hoverSound.play(); // Play Hover Sound
                    loginButton.clearActions();
                    loginButton.addAction(Actions.scaleTo(1.1f, 1.1f, 0.1f));
                    Gdx.graphics.setSystemCursor(Cursor.SystemCursor.Hand);
                }
                super.enter(event, x, y, pointer, fromActor);
            }

            @Override
            public void exit(InputEvent event, float x, float y, int pointer, Actor toActor) {
                if (pointer == -1) {
                    loginButton.clearActions();
                    loginButton.addAction(Actions.scaleTo(1.0f, 1.0f, 0.1f));
                    Gdx.graphics.setSystemCursor(Cursor.SystemCursor.Arrow);
                }
                super.exit(event, x, y, pointer, toActor);
            }
        });

        mainTable.add(playButton).width(360f).height(90f).padRight(-45f);
        mainTable.add(loginButton).width(360f).height(90f).padLeft(-45f);
        mainTable.row();

        // Update Origin setelah layouting
        playButton.setOrigin(180f, 45f);
        loginButton.setOrigin(180f, 45f);

        // Leaderboard
        Label leaderboardLabel = new Label("Loading...", skin, "leaderboard");
        leaderboardLabel.setFontScale(0.4f);
        leaderboardTable.add(leaderboardLabel);

        GameManager.getInstance().fetchUsername(new GameManager.UsernameCallback() {
            @Override
            public void onFetched(String fetchedUsername, int skor, int coinsCollected, List<Leaderboard> leaderboardList) {
                Gdx.app.postRunnable(() -> {
                    if (fetchedUsername != null) {
                        playerLabel.setText("Hello, " + fetchedUsername);
                    }
                    skorLabel.setText("High Score: " + skor);
                    coinsCollectedLabel.setText("Coins Collected: " + coinsCollected);

                    int i = 1;
                    StringBuilder leaderboardString = new StringBuilder();
                    leaderboardString.append("LEADERBOARD:\n");

                    for (Leaderboard leaderboard : leaderboardList) {
                        leaderboardString.append(i).append(". ").append(leaderboard.getUsername())
                            .append("\n   Score: ").append(leaderboard.getScore())
                            .append("\n");
                        i++;
                    }
                    leaderboardLabel.setText(leaderboardString.toString());
                });
            }
        });
    }

    @Override
    public void resize(int width, int height) {
        stage.getViewport().update(width, height, true);
    }

    @Override
    public void update(float delta) {
        background.update(delta);
        stage.act(delta);
    }

    @Override
    public void render(ShapeRenderer shapeRenderer, SpriteBatch batch) {
        background.render(batch, this);
        stage.draw();
    }

    @Override
    public void dispose() {
        Gdx.graphics.setSystemCursor(Cursor.SystemCursor.Arrow);
        background.dispose();
        stage.dispose();
        skin.dispose();
        clickSound.dispose();
        hoverSound.dispose();
        bgMusic.stop();
        bgMusic.dispose();
    }
}
