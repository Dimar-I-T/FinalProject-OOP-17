package com.dimar.frontend.states;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.dimar.frontend.Background;
import com.dimar.frontend.GameManager;
import com.dimar.frontend.Leaderboard;
import com.dimar.frontend.MenuBackground;

import java.util.List;

public class MenuState implements GameState {
    private MenuBackground background;
    private final GameStateManager gsm;
    private final Stage stage;
    private Skin skin;

    public MenuState(GameStateManager gsm) {
        background = new MenuBackground();
        this.gsm = gsm;
        stage = new Stage(new ScreenViewport());
        Gdx.input.setInputProcessor(stage);
        createBasicSkin();
        buildUI();
    }

    private void createBasicSkin() {
        skin = new Skin();
        BitmapFont bitmapFont = new BitmapFont();
        skin.add("default", bitmapFont);
        Pixmap pixmapW = new Pixmap(1,1, Pixmap.Format.RGBA8888);
        Pixmap pixmapDG = new Pixmap(1,1, Pixmap.Format.RGBA8888);
        Pixmap pixmapG = new Pixmap(1,1, Pixmap.Format.RGBA8888);

        pixmapW.setColor(Color.WHITE);
        pixmapW.fill();
        skin.add("white", new Texture(pixmapW));

        pixmapG.setColor(Color.GRAY);
        pixmapG.fill();
        skin.add("gray", new Texture(pixmapG));

        pixmapDG.setColor(Color.DARK_GRAY);
        pixmapDG.fill();
        skin.add("dark_gray", new Texture(pixmapDG));

        pixmapDG.dispose();
        pixmapG.dispose();
        pixmapW.dispose();

        Label.LabelStyle labelStyle = new Label.LabelStyle();
        labelStyle.font = bitmapFont;
        labelStyle.fontColor = Color.ORANGE;
        skin.add("labelStyle", labelStyle);

        TextField.TextFieldStyle textFieldStyle = new TextField.TextFieldStyle();
        textFieldStyle.font = bitmapFont;
        textFieldStyle.fontColor = Color.WHITE;
        textFieldStyle.background = skin.newDrawable("dark_gray");
        textFieldStyle.cursor = skin.newDrawable("white");
        textFieldStyle.selection = skin.newDrawable("gray");
        skin.add("textFieldStyle", textFieldStyle);

        TextButton.TextButtonStyle textButtonStyle = new TextButton.TextButtonStyle();
        textButtonStyle.font = bitmapFont;
        textButtonStyle.fontColor = Color.WHITE;
        textButtonStyle.up = skin.newDrawable("gray");
        textButtonStyle.down = skin.newDrawable("white");
        textButtonStyle.over = skin.newDrawable("dark_gray");
        skin.add("textButtonStyle", textButtonStyle);

        Label.LabelStyle defaultStyle = new Label.LabelStyle();
        defaultStyle.font = bitmapFont;
        defaultStyle.fontColor = Color.WHITE;
        skin.add("default", defaultStyle);

        Label.LabelStyle leaderboardStyle = new Label.LabelStyle();
        leaderboardStyle.font = bitmapFont;
        leaderboardStyle.fontColor = new Color(0.894f, 0.816f, 0.039f, 1f);
        skin.add("leaderboard", leaderboardStyle);
    }

    private void buildUI() {
        Table table = new Table();
        table.setFillParent(true);
        stage.addActor(table);

        Label judul = new Label("FLOOR IS LAVA", skin, "labelStyle");
        judul.setFontScale(2f);
        table.add(judul).padBottom(20f);
        table.row();

        Label playerLabel = new Label("Hello, guest", skin, "default");
        table.add(playerLabel).padBottom(20f);
        table.row();

        Label skorLabel = new Label("High Score: ", skin, "default");
        table.add(skorLabel).padBottom(20f);
        table.row();

        Label coinsCollectedLabel = new Label("Coins Collected: ", skin, "default");
        table.add(coinsCollectedLabel).padBottom(40f);
        table.row();

        Label leaderboardLabel = new Label("", skin, "leaderboard");

        GameManager.getInstance().fetchUsername(new GameManager.UsernameCallback() {
            @Override
            public void onFetched(String fetchedUsername, int skor, int coinsCollected, List<Leaderboard> leaderboardList) {
                Gdx.app.postRunnable(() -> {
                    playerLabel.setText("Hello, " + fetchedUsername);
                    skorLabel.setText("High Score: " + skor);
                    coinsCollectedLabel.setText("Coins Collected: " + coinsCollected);

                    int i = 1;
                    String leaderboardString = "";
                    for (Leaderboard leaderboard : leaderboardList) {
                        if (i == 1) {
                            leaderboardString += "Leaderboard (Top 5 by High Score):\n";
                        }

                        leaderboardString += i + ". " + leaderboard.getUsername() + " (High Score: " + leaderboard.getScore() + ", Total Coins: " + leaderboard.getCoinsCollected() + ")\n";
                        i++;
                    }

                    leaderboardLabel.setText(leaderboardString);
                });
            }
        });

        TextButton textButton = new TextButton("START GAME", skin, "textButtonStyle");
        textButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                gsm.set(new PlayingState(gsm));
            }
        });

        TextButton textButtonLogin = new TextButton("Login", skin, "textButtonStyle");
        textButtonLogin.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                gsm.set(new LoginState(gsm));
            }
        });

        table.add(textButton).padBottom(30f).width(200f).height(50f);
        table.row();
        table.add(textButtonLogin).padBottom(40f).width(200f).height(50f);
        table.row();
        table.add(leaderboardLabel);
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
        background.render(batch, shapeRenderer);
        stage.draw();
    }

    @Override
    public void dispose() {
        background.dispose();
        stage.dispose();
        skin.dispose();
    }
}
