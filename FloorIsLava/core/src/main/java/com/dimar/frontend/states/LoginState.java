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
import com.dimar.frontend.backgrounds.LoginBackground;

public class LoginState implements GameState {

    private LoginBackground background;
    private final GameStateManager gsm;
    private final Stage stage;
    private Skin skin;
    private TextField nameField, passwordField;
    private Label errorLabel;

    // Sound Effects
    private Sound clickSound;
    private Sound hoverSound;
    private Music bgMusic; // Musik Background

    public LoginState(GameStateManager gsm) {
        background = new LoginBackground();
        this.gsm = gsm;
        stage = new Stage(new ScreenViewport());
        Gdx.input.setInputProcessor(stage);

        // Load Audio
        clickSound = Gdx.audio.newSound(Gdx.files.internal("audio/sound/click.wav"));
        hoverSound = Gdx.audio.newSound(Gdx.files.internal("audio/sound/hover.wav"));

        // Load & Play Music
        bgMusic = Gdx.audio.newMusic(Gdx.files.internal("audio/music/title-screen.wav"));
        bgMusic.setLooping(true);
        bgMusic.setVolume(0.5f);
        bgMusic.play();

        Gdx.input.setCursorCatched(false);
        createBasicSkin();
        buildUI();
    }

    // bikin background rounded putih
    private TextureRegionDrawable createRoundedRect(int width, int height, int radius, Color color) {
        Pixmap pixmap = new Pixmap(width, height, Pixmap.Format.RGBA8888);
        pixmap.setColor(color);
        pixmap.fillCircle(radius, radius, radius);
        pixmap.fillCircle(width - radius, radius, radius);
        pixmap.fillCircle(width - radius, height - radius, radius);
        pixmap.fillCircle(radius, height - radius, radius);
        pixmap.fillRectangle(radius, 0, width - 2 * radius, height);
        pixmap.fillRectangle(0, radius, width, height - 2 * radius);
        Texture texture = new Texture(pixmap);
        pixmap.dispose();
        return new TextureRegionDrawable(new TextureRegion(texture));
    }

    private void createBasicSkin() {
        skin = new Skin();

        // Font Pixel
        BitmapFont pixelFont = new BitmapFont(Gdx.files.internal("04b30.fnt"));
        pixelFont.getRegion().getTexture().setFilter(Texture.TextureFilter.Nearest, Texture.TextureFilter.Nearest);
        skin.add("pixelFont", pixelFont);

        // Texture Warna Dasar
        Pixmap pixmapBlack = new Pixmap(1, 1, Pixmap.Format.RGBA8888);
        pixmapBlack.setColor(Color.BLACK); pixmapBlack.fill(); skin.add("black", new Texture(pixmapBlack));

        Pixmap pixmapGray = new Pixmap(1, 1, Pixmap.Format.RGBA8888);
        pixmapGray.setColor(Color.LIGHT_GRAY); pixmapGray.fill(); skin.add("light_gray", new Texture(pixmapGray));

        pixmapBlack.dispose(); pixmapGray.dispose();

        // LOAD GAMBAR TOMBOL
        Texture loginUp = new Texture("login/login-button.png");
        loginUp.setFilter(Texture.TextureFilter.Nearest, Texture.TextureFilter.Nearest);
        skin.add("login_up", loginUp);

        Texture loginOver = new Texture("login/login-button-hover.png");
        loginOver.setFilter(Texture.TextureFilter.Nearest, Texture.TextureFilter.Nearest);
        skin.add("login_over", loginOver);

        Texture menuUp = new Texture("login/menu-button.png");
        menuUp.setFilter(Texture.TextureFilter.Nearest, Texture.TextureFilter.Nearest);
        skin.add("menu_up", menuUp);

        Texture menuOver = new Texture("login/menu-button-hover.png");
        menuOver.setFilter(Texture.TextureFilter.Nearest, Texture.TextureFilter.Nearest);
        skin.add("menu_over", menuOver);

        Texture registerUp = new Texture("login/create-account-button.png");
        registerUp.setFilter(Texture.TextureFilter.Nearest, Texture.TextureFilter.Nearest);
        skin.add("register_up", registerUp);

        Texture registerOver = new Texture("login/create-account-button-hover.png");
        registerOver.setFilter(Texture.TextureFilter.Nearest, Texture.TextureFilter.Nearest);
        skin.add("register_over", registerOver);


        // --- STYLE ---
        Label.LabelStyle labelStyle = new Label.LabelStyle();
        labelStyle.font = pixelFont; labelStyle.fontColor = Color.ORANGE; skin.add("labelStyle", labelStyle);

        Label.LabelStyle navyLabelStyle = new Label.LabelStyle();
        navyLabelStyle.font = pixelFont; navyLabelStyle.fontColor = new Color(0.05f, 0.05f, 0.45f, 1f);
        skin.add("navyLabelStyle", navyLabelStyle);

        Label.LabelStyle errorStyle = new Label.LabelStyle();
        errorStyle.font = pixelFont; errorStyle.fontColor = Color.RED; skin.add("error", errorStyle);

        Label.LabelStyle strikingStyle = new Label.LabelStyle();
        strikingStyle.font = pixelFont;
        strikingStyle.fontColor = Color.SCARLET;
        skin.add("strikingStyle", strikingStyle);

        // --- TextField Style ---
        TextField.TextFieldStyle textFieldStyle = new TextField.TextFieldStyle();
        textFieldStyle.font = pixelFont;
        textFieldStyle.fontColor = Color.BLACK;

        // bikin background
        TextureRegionDrawable fieldBg = createRoundedRect(700, 80, 20, Color.WHITE);
        // SET PADDING
        fieldBg.setPadding(15f, 15f, 10f, 10f);
        textFieldStyle.background = fieldBg;

        textFieldStyle.cursor = skin.newDrawable("black");
        textFieldStyle.selection = skin.newDrawable("light_gray");
        skin.add("textFieldStyle", textFieldStyle);

        // --- IMAGE BUTTON STYLES ---
        ImageButton.ImageButtonStyle loginImgStyle = new ImageButton.ImageButtonStyle();
        loginImgStyle.imageUp = new TextureRegionDrawable(skin.getRegion("login_up"));
        loginImgStyle.imageOver = new TextureRegionDrawable(skin.getRegion("login_over"));
        skin.add("loginImgStyle", loginImgStyle);

        ImageButton.ImageButtonStyle menuImgStyle = new ImageButton.ImageButtonStyle();
        menuImgStyle.imageUp = new TextureRegionDrawable(skin.getRegion("menu_up"));
        menuImgStyle.imageOver = new TextureRegionDrawable(skin.getRegion("menu_over"));
        skin.add("menuImgStyle", menuImgStyle);

        ImageButton.ImageButtonStyle registerImgStyle = new ImageButton.ImageButtonStyle();
        registerImgStyle.imageUp = new TextureRegionDrawable(skin.getRegion("register_up"));
        registerImgStyle.imageOver = new TextureRegionDrawable(skin.getRegion("register_over"));
        skin.add("registerImgStyle", registerImgStyle);
    }

    private void buildUI() {
        Table table = new Table();
        table.setFillParent(true);
        stage.addActor(table);

        // Judul & Prompt
        Label judul = new Label("LOGIN", skin, "labelStyle"); judul.setFontScale(1f);
        Label promptUsername = new Label("Enter Your Username:", skin, "navyLabelStyle");
        nameField = new TextField("", skin, "textFieldStyle");
        promptUsername.setFontScale(0.7f);
        addInputHoverEffect(nameField);

        Label promptPassword = new Label("Enter Your Password:", skin, "navyLabelStyle");
        passwordField = new TextField("", skin, "textFieldStyle");
        passwordField.setPasswordMode(true); passwordField.setPasswordCharacter('*');
        promptPassword.setFontScale(0.7f);
        addInputHoverEffect(passwordField);

        errorLabel = new Label("", skin, "error");
        errorLabel.setFontScale(0.6f);
        errorLabel.setVisible(false);

        // --- BUTTONS ---
        ImageButton loginButton = new ImageButton(skin, "loginImgStyle");
        loginButton.setTransform(true);
        loginButton.setOrigin(Align.center);
        loginButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                clickSound.play();
                Gdx.graphics.setSystemCursor(Cursor.SystemCursor.Arrow);
                handleLogin();
            }
        });
        addButtonHoverEffect(loginButton);

        ImageButton menuButton = new ImageButton(skin, "menuImgStyle");
        menuButton.setTransform(true);
        menuButton.setOrigin(Align.center);
        menuButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                clickSound.play();
                Gdx.graphics.setSystemCursor(Cursor.SystemCursor.Arrow);
                gsm.set(new MenuState(gsm));
            }
        });
        addButtonHoverEffect(menuButton);

        ImageButton registerButton = new ImageButton(skin, "registerImgStyle");
        registerButton.setTransform(true);
        registerButton.setOrigin(Align.center);
        registerButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                clickSound.play();
                Gdx.graphics.setSystemCursor(Cursor.SystemCursor.Arrow);
                gsm.set(new RegisterState(gsm));
            }
        });
        addButtonHoverEffect(registerButton);

        Label haveNoAcc = new Label("Don't have an account?", skin, "strikingStyle");
        haveNoAcc.setFontScale(0.6f);

        // --- LAYOUT ---

        // Judul
        table.add(judul).padBottom(20f).padTop(90f);
        table.row();

        // Form Username
        table.add(promptUsername).padBottom(20f); table.row();
        table.add(nameField).width(700f).height(80f).padBottom(10f); table.row();

        // Form Password
        table.add(promptPassword).padBottom(20f); table.row();
        table.add(passwordField).width(700f).height(80f).padBottom(10f); table.row();

        // Error Label
        table.add(errorLabel).padBottom(10f); table.row();

        // --- BUTTONS ---

        // LOGIN
        table.add(loginButton).width(420f).height(90f).padBottom(20f);
        table.row();

        // MENU
        table.add(menuButton).width(420f).height(90f).padBottom(5f);
        table.row();

        // Jarak Pemisah ke Bagian Register
        table.add().height(5f);
        table.row();

        // Label Don't have account
        table.add(haveNoAcc).padBottom(15f); table.row();

        // REGISTER
        table.add(registerButton).width(420f).height(90f).padBottom(50f);
    }

    private void handleLogin() {
        String username = nameField.getText();
        String password = passwordField.getText();
        if (username.trim().isEmpty() || password.trim().isEmpty()) {
            errorLabel.setText("Username or password can't be empty");
            errorLabel.setVisible(true);
            return;
        }
        GameManager.getInstance().loginPlayer(username, password, new GameManager.LoginCallback() {
            @Override
            public void onSuccess(String token) {
                Gdx.app.postRunnable(() -> gsm.set(new MenuState(gsm)));
                errorLabel.setVisible(false);
            }
            @Override
            public void onError(String error) {
                Gdx.app.postRunnable(() -> {
                    if (error.toLowerCase().contains("timeout")) errorLabel.setText("Server timeout.");
                    else errorLabel.setText("Invalid Username/Password");
                    errorLabel.setVisible(true);
                });
            }
        });
    }

    // --- HOVER BUTTON ---
    private void addButtonHoverEffect(Actor actor) {
        actor.addListener(new ClickListener() {
            @Override
            public void enter(InputEvent event, float x, float y, int pointer, Actor fromActor) {
                if (pointer == -1) {
                    hoverSound.play();
                    actor.clearActions();
                    actor.setOrigin(Align.center);
                    actor.addAction(Actions.scaleTo(1.1f, 1.1f, 0.1f));
                    Gdx.graphics.setSystemCursor(Cursor.SystemCursor.Hand);
                }
            }
            @Override
            public void exit(InputEvent event, float x, float y, int pointer, Actor toActor) {
                if (pointer == -1) {
                    actor.clearActions();
                    actor.setOrigin(Align.center);
                    actor.addAction(Actions.scaleTo(1f, 1f, 0.1f));
                    Gdx.graphics.setSystemCursor(Cursor.SystemCursor.Arrow);
                }
            }
        });
    }

    // --- HOVER INPUT ---
    private void addInputHoverEffect(TextField textField) {
        textField.addListener(new ClickListener() {
            @Override
            public void enter(InputEvent event, float x, float y, int pointer, Actor fromActor) {
                if (pointer == -1) {
                    Gdx.graphics.setSystemCursor(Cursor.SystemCursor.Ibeam);
                }
            }
            @Override
            public void exit(InputEvent event, float x, float y, int pointer, Actor toActor) {
                if (pointer == -1) {
                    Gdx.graphics.setSystemCursor(Cursor.SystemCursor.Arrow);
                }
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
        stage.dispose();
        skin.dispose();
        background.dispose();
        clickSound.dispose();
        hoverSound.dispose();
        // Stop & Dispose Music
        bgMusic.stop();
        bgMusic.dispose();
    }
}
