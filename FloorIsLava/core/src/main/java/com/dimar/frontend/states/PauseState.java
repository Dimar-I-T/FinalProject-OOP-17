package com.dimar.frontend.states;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
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
import com.dimar.frontend.commands.Command;
import com.dimar.frontend.commands.ResumeCommand;

import java.util.ArrayList;
import java.util.List;

public class PauseState implements GameState {
    private GameStateManager gsm;
    private Stage stage;
    private Skin skin;
    private TextField nameField;
    private TextButton startButton;
    private PlayingState playingState;
    private List<Command> commands;

    public PauseState(GameStateManager gsm, PlayingState playingState) {
        this.playingState = playingState;
        this.gsm = gsm;
        commands = new ArrayList<>();
        commands.add(new ResumeCommand(gsm));
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
    }

    private void buildUI() {
        Table table = new Table();
        table.setFillParent(true);
        stage.addActor(table);

        Label judul = new Label("Game is paused", skin, "labelStyle");
        judul.setFontScale(2f);

        TextButton textButton = new TextButton("RESUME", skin, "textButtonStyle");
        textButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                gsm.pop();
            }
        });

        table.add(judul).padBottom(20f);
        table.row();

        table.add(textButton).width(200f).height(50f);
    }

    @Override
    public void resize(int width, int height) {
        stage.getViewport().update(width, height, true);
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
        playingState.render(shapeRenderer, new SpriteBatch());
        stage.draw();
    }

    @Override
    public void dispose() {
        stage.dispose();
        skin.dispose();
    }
}
