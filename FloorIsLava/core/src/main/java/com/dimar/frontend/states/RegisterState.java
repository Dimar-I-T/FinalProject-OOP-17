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

public class RegisterState implements GameState {
    private Background background;
    private final GameStateManager gsm;
    private final Stage stage;
    private Skin skin;
    private TextField nameField, passwordField;
    private Label errorLabel;

    public RegisterState(GameStateManager gsm) {
        background = new Background();
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

        Label.LabelStyle errorStyle = new Label.LabelStyle();
        errorStyle.font = bitmapFont;
        errorStyle.fontColor = Color.RED;
        skin.add("error", errorStyle);
    }

    private void buildUI() {
        Table table = new Table();
        table.setFillParent(true);
        stage.addActor(table);

        Label judul = new Label("Register", skin, "labelStyle");
        judul.setFontScale(2f);

        Label promptUsername = new Label("Enter Your Username:", skin, "labelStyle");
        nameField = new TextField("", skin, "textFieldStyle");

        Label promptPassword = new Label("Enter Your Password:", skin, "labelStyle");
        passwordField = new TextField("", skin, "textFieldStyle");

        errorLabel = new Label("", skin, "error");
        errorLabel.setVisible(false);

        TextButton textButton = new TextButton("REGISTER", skin, "textButtonStyle");
        textButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                String username = nameField.getText();
                String password = passwordField.getText();

                if (username.trim().isEmpty() || password.trim().isEmpty()) {
                    errorLabel.setText("Username or password can't be empty");
                    errorLabel.setVisible(true);
                    return;
                }

                GameManager.getInstance().registerPlayer(username, password, new GameManager.LoginCallback() {
                    @Override
                    public void onSuccess(String token) {
                        Gdx.app.postRunnable(() -> gsm.set(new MenuState(gsm)));
                        errorLabel.setVisible(false);
                    }

                    @Override
                    public void onError(String error) {
                        Gdx.app.postRunnable(() -> {
                            System.out.println("Register gagal: " + error);
                            if (error.toLowerCase().contains("timeout") || error.toLowerCase().contains("timed out")) {
                                errorLabel.setText("Server is waking up, please try again.");
                                errorLabel.setVisible(true);
                            }else{
                                errorLabel.setText("Username already exists");
                                errorLabel.setVisible(true);
                            }
                        });
                    }
                });
            }
        });

        TextButton textButtonBack = new TextButton("BACK TO MAIN MENU", skin, "textButtonStyle");
        textButtonBack.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                gsm.set(new MenuState(gsm));
            }
        });

        TextButton textButtonRegister = new TextButton("CREATE ACCOUNT", skin, "textButtonStyle");
        textButtonRegister.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                gsm.set(new RegisterState(gsm));
            }
        });

        table.add(judul).padBottom(20f);
        table.row();

        table.add(promptUsername).padBottom(10f);
        table.row();

        table.add(nameField).width(250f).height(40f).padBottom(20f);
        table.row();

        table.add(promptPassword).padBottom(10f);
        table.row();

        table.add(passwordField).width(250f).height(40f).padBottom(20f);
        table.row();

        table.add(errorLabel).padBottom(20f);
        table.row();

        table.add(textButton).padBottom(30f).width(200f).height(50f);
        table.row();
        table.add(textButtonBack).padBottom(30f).width(200f).height(50f);
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
        batch.begin();
        background.render(batch, new OrthographicCamera());
        batch.end();
        stage.draw();
    }

    @Override
    public void dispose() {
        stage.dispose();
        skin.dispose();
    }
}
