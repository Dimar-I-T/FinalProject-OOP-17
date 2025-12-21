package com.dimar.frontend.states;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;
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
import com.dimar.frontend.backgrounds.MenuBackground;

import java.util.List;

public class MenuState implements GameState {
    private MenuBackground background;
    private Texture scroll;
    private final GameStateManager gsm;
    private final Stage stage;
    private Skin skin;
    private Sound clickSound;
    private Sound hoverSound;
    private Music bgMusic;

    public MenuState(GameStateManager gsm) {
        background = new MenuBackground();
        this.gsm = gsm;
        stage = new Stage(new ScreenViewport());
        Gdx.input.setInputProcessor(stage);

        clickSound = Gdx.audio.newSound(Gdx.files.internal("audio/sound/click.wav"));
        hoverSound = Gdx.audio.newSound(Gdx.files.internal("audio/sound/hover.wav"));

        bgMusic = Gdx.audio.newMusic(Gdx.files.internal("audio/music/title-screen.wav"));
        bgMusic.setLooping(true);
        bgMusic.setVolume(0.5f);
        bgMusic.play();

        Gdx.input.setCursorCatched(false);
        createBasicSkin();
        buildUI();
    }

    private void createBasicSkin() {
        skin = new Skin();

        Texture playTexture = new Texture(Gdx.files.internal("menu/play-button.png"));
        Texture loginTexture = new Texture(Gdx.files.internal("menu/login-button.png"));
        Texture playHoverTexture = new Texture(Gdx.files.internal("menu/play-button-hover.png"));
        Texture loginHoverTexture = new Texture(Gdx.files.internal("menu/login-button-hover.png"));

        Texture creditTexture = new Texture(Gdx.files.internal("menu/credit.png"));
        Texture creditHoverTexture = new Texture(Gdx.files.internal("menu/credit-hover.png"));

        Texture exitTexture = new Texture(Gdx.files.internal("menu/EXIT.png"));
        Texture exitHoverTexture = new Texture(Gdx.files.internal("menu/EXIT_Hover.png"));

        playTexture.setFilter(Texture.TextureFilter.Nearest, Texture.TextureFilter.Nearest);
        loginTexture.setFilter(Texture.TextureFilter.Nearest, Texture.TextureFilter.Nearest);
        playHoverTexture.setFilter(Texture.TextureFilter.Nearest, Texture.TextureFilter.Nearest);
        loginHoverTexture.setFilter(Texture.TextureFilter.Nearest, Texture.TextureFilter.Nearest);

        creditTexture.setFilter(Texture.TextureFilter.Nearest, Texture.TextureFilter.Nearest);
        creditHoverTexture.setFilter(Texture.TextureFilter.Nearest, Texture.TextureFilter.Nearest);

        exitTexture.setFilter(Texture.TextureFilter.Nearest, Texture.TextureFilter.Nearest);
        exitHoverTexture.setFilter(Texture.TextureFilter.Nearest, Texture.TextureFilter.Nearest);

        skin.add("playTexture", playTexture);
        skin.add("loginTexture", loginTexture);
        skin.add("playHoverTexture", playHoverTexture);
        skin.add("loginHoverTexture", loginHoverTexture);

        skin.add("creditTexture", creditTexture);
        skin.add("creditHoverTexture", creditHoverTexture);

        skin.add("exitTexture", exitTexture);
        skin.add("exitHoverTexture", exitHoverTexture);

        BitmapFont defaultFont = new BitmapFont();
        skin.add("default", defaultFont);
        BitmapFont pixelFont = new BitmapFont(Gdx.files.internal("04b30.fnt"));
        pixelFont.getRegion().getTexture().setFilter(Texture.TextureFilter.Nearest, Texture.TextureFilter.Nearest);
        skin.add("pixelFont", pixelFont);

        Pixmap pixmapDG = new Pixmap(1,1, Pixmap.Format.RGBA8888);
        pixmapDG.setColor(Color.DARK_GRAY);
        pixmapDG.fill();
        skin.add("dark_gray", new Texture(pixmapDG));
        pixmapDG.dispose();

        Color titleColor = new Color(0.9f, 0.3f, 0.0f, 1f);
        Color skyTextColor = new Color(0.1f, 0.1f, 0.35f, 1f);

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
        leaderboardStyle.fontColor = new Color(0.2f, 0.1f, 0.05f, 1f);
        skin.add("leaderboard", leaderboardStyle);

        Label.LabelStyle defaultStyle = new Label.LabelStyle();
        defaultStyle.font = defaultFont;
        defaultStyle.fontColor = Color.WHITE;
        skin.add("default", defaultStyle);

        scroll = new Texture("menu/scroll.png");
        scroll.setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear);
        skin.add("scroll", scroll);
    }

    private void buildUI() {
        Table mainTable = new Table();
        mainTable.setFillParent(true);
        mainTable.center();
        stage.addActor(mainTable);

        Table leaderboardTable = new Table();
        leaderboardTable.setBackground(skin.getDrawable("scroll"));
        leaderboardTable.pad(60f, 80f, 60f, 80f);
        Table wrapperTable = new Table();
        wrapperTable.setFillParent(true);
        wrapperTable.bottom().right().pad(20f);
        wrapperTable.add(leaderboardTable).width(350f).height(450f);
        stage.addActor(wrapperTable);

        Table Exit = new Table();
        Exit.setFillParent(true);
        Exit.align(Align.bottomLeft).padBottom(Gdx.graphics.getHeight()/40f).padLeft(Gdx.graphics.getWidth()/ 30f);
        stage.addActor(Exit);

        Label judul = new Label("FLOOR IS LAVA", skin, "titleStyle");
        judul.setFontScale(1.5f);
        judul.addAction(Actions.forever(
            Actions.sequence(
                Actions.moveBy(0, 15f, 1f, Interpolation.sineOut),
                Actions.moveBy(0, -15f, 1f, Interpolation.sineIn)
            )
        ));
        mainTable.add(judul).padBottom(50f).colspan(2);
        mainTable.row();

        Label playerLabel = new Label("Hello, Guest!", skin, "infoStyle");
        playerLabel.setFontScale(0.5f);
        mainTable.add(playerLabel).padBottom(10f).colspan(2);
        mainTable.row();

        Label skorLabel = new Label("", skin, "infoStyle");
        skorLabel.setFontScale(0.5f);
        mainTable.add(skorLabel).padBottom(10f).colspan(2);
        mainTable.row();

        Label coinsCollectedLabel = new Label("", skin, "infoStyle");
        coinsCollectedLabel.setFontScale(0.5f);
        mainTable.add(coinsCollectedLabel).padBottom(50f).colspan(2);
        mainTable.row();

        TextureRegionDrawable playUp = new TextureRegionDrawable(new TextureRegion(skin.get("playTexture", Texture.class)));
        TextureRegionDrawable playOver = new TextureRegionDrawable(new TextureRegion(skin.get("playHoverTexture", Texture.class)));

        TextureRegionDrawable loginUp = new TextureRegionDrawable(new TextureRegion(skin.get("loginTexture", Texture.class)));
        TextureRegionDrawable loginOver = new TextureRegionDrawable(new TextureRegion(skin.get("loginHoverTexture", Texture.class)));

        TextureRegionDrawable creditUp = new TextureRegionDrawable(new TextureRegion(skin.get("creditTexture", Texture.class)));
        TextureRegionDrawable creditOver = new TextureRegionDrawable(new TextureRegion(skin.get("creditHoverTexture", Texture.class)));

        TextureRegionDrawable exitUp = new TextureRegionDrawable(new TextureRegion(skin.get("exitTexture", Texture.class)));
        TextureRegionDrawable exitOver = new TextureRegionDrawable(new TextureRegion(skin.get("exitHoverTexture", Texture.class)));

        ImageButton.ImageButtonStyle playStyle = new ImageButton.ImageButtonStyle();
        playStyle.imageUp = playUp;
        playStyle.imageOver = playOver;

        ImageButton.ImageButtonStyle loginStyle = new ImageButton.ImageButtonStyle();
        loginStyle.imageUp = loginUp;
        loginStyle.imageOver = loginOver;

        ImageButton.ImageButtonStyle creditStyle = new ImageButton.ImageButtonStyle();
        creditStyle.imageUp = creditUp;
        creditStyle.imageOver = creditOver;

        ImageButton.ImageButtonStyle exitStyle = new ImageButton.ImageButtonStyle();
        exitStyle.imageUp = exitUp;
        exitStyle.imageOver = exitOver;

        ImageButton playButton = new ImageButton(playStyle);
        ImageButton loginButton = new ImageButton(loginStyle);
        ImageButton creditButton = new ImageButton(creditStyle);
        ImageButton exitButton = new ImageButton(exitStyle);

        playButton.setTransform(true);
        playButton.setOrigin(Align.center);
        loginButton.setTransform(true);
        loginButton.setOrigin(Align.center);
        creditButton.setTransform(true);
        creditButton.setOrigin(Align.center);
        exitButton.setTransform(true);
        exitButton.setOrigin(Align.center);

        playButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                clickSound.play();
                Gdx.graphics.setSystemCursor(Cursor.SystemCursor.Arrow);
                gsm.set(new ControlState(gsm));
            }

            @Override
            public void enter(InputEvent event, float x, float y, int pointer, Actor fromActor) {
                if (pointer == -1) {
                    hoverSound.play();
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

        loginButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                clickSound.play();
                Gdx.graphics.setSystemCursor(Cursor.SystemCursor.Arrow);
                gsm.set(new LoginState(gsm));
            }

            @Override
            public void enter(InputEvent event, float x, float y, int pointer, Actor fromActor) {
                if (pointer == -1) {
                    hoverSound.play();
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

        creditButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                clickSound.play();
                Gdx.graphics.setSystemCursor(Cursor.SystemCursor.Arrow);
                gsm.set(new CreditState(gsm));
            }

            @Override
            public void enter(InputEvent event, float x, float y, int pointer, Actor fromActor) {
                if (pointer == -1) {
                    hoverSound.play();
                    creditButton.clearActions();
                    creditButton.addAction(Actions.scaleTo(1.1f, 1.1f, 0.1f));
                    Gdx.graphics.setSystemCursor(Cursor.SystemCursor.Hand);
                }
                super.enter(event, x, y, pointer, fromActor);
            }

            @Override
            public void exit(InputEvent event, float x, float y, int pointer, Actor toActor) {
                if (pointer == -1) {
                    creditButton.clearActions();
                    creditButton.addAction(Actions.scaleTo(1.0f, 1.0f, 0.1f));
                    Gdx.graphics.setSystemCursor(Cursor.SystemCursor.Arrow);
                }
                super.exit(event, x, y, pointer, toActor);
            }
        });

        exitButton.addListener(new ClickListener(){
            @Override
            public void clicked(InputEvent event, float x, float y) {
                clickSound.play();
                Gdx.graphics.setSystemCursor(Cursor.SystemCursor.Arrow);
                Gdx.app.exit();
            }

            @Override
            public void enter(InputEvent event, float x, float y, int pointer, Actor fromActor) {
                if (pointer == -1) {
                    hoverSound.play();
                    exitButton.clearActions();
                    exitButton.addAction(Actions.scaleTo(1.1f, 1.1f, 0.1f));
                    Gdx.graphics.setSystemCursor(Cursor.SystemCursor.Hand);
                }
                super.enter(event, x, y, pointer, fromActor);
            }

            @Override
            public void exit(InputEvent event, float x, float y, int pointer, Actor toActor) {
                if (pointer == -1) {
                    exitButton.clearActions();
                    exitButton.addAction(Actions.scaleTo(1.0f, 1.0f, 0.1f));
                    Gdx.graphics.setSystemCursor(Cursor.SystemCursor.Arrow);
                }
                super.exit(event, x, y, pointer, toActor);
            }
        });

        mainTable.add(playButton).width(360f).height(90f).padRight(-250f);
        mainTable.add(loginButton).width(360f).height(90f).padLeft(-250f);
        mainTable.row();

        mainTable.add(creditButton).width(300f).height(75f).colspan(2).padTop(10f);
        mainTable.row();

        playButton.setOrigin(180f, 45f);
        loginButton.setOrigin(180f, 45f);
        creditButton.setOrigin(150f, 37.5f);

        exitButton.setOrigin(70f, 35f);

        Exit.add(exitButton).width(140f).height(70f);

        Label leaderboardLabel = new Label("Loading...", skin, "leaderboard");
        leaderboardLabel.setFontScale(0.3f);
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
                        String rawName = leaderboard.getUsername();
                        String displayName = (rawName.length() > 10) ? rawName.substring(0, 10) + "..." : rawName;

                        leaderboardString.append(i).append(". ").append(displayName)
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
        if(scroll != null) scroll.dispose();
        background.dispose();
        stage.dispose();
        skin.dispose();
        clickSound.dispose();
        hoverSound.dispose();
        bgMusic.stop();
        bgMusic.dispose();
    }
}
