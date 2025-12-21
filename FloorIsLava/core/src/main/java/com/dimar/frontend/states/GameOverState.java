package com.dimar.frontend.states;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.dimar.frontend.GameManager;

public class GameOverState implements GameState {
    private final GameStateManager gsm;
    private final BitmapFont font, font1;
    private final OrthographicCamera camera;
    private final Texture deathImage;
    private float width;
    private final float widthAwal;
    private float height;
    private final float heightAwal;
    private final GameManager gameManager;
    private int finalScore;
    private int finalCoins;

    public GameOverState(GameStateManager gsm) {
        gameManager = GameManager.getInstance();
        System.out.println("You are dead.");
        camera = new OrthographicCamera();
        camera.setToOrtho(false, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        width = Gdx.graphics.getWidth();
        height = Gdx.graphics.getHeight();
        widthAwal = Gdx.graphics.getWidth();
        heightAwal = Gdx.graphics.getHeight();
        this.gsm = gsm;

        this.finalScore = com.dimar.frontend.GameManager.getInstance().getScore();
        this.finalCoins = com.dimar.frontend.GameManager.getInstance().getCoinsCollected();

        deathImage = new Texture(Gdx.files.internal("death.png"));
        font = new BitmapFont(Gdx.files.internal("04b30.fnt"));
        font.setColor(Color.RED);
        font.getData().setScale(1.8f);
        font1 = new BitmapFont(Gdx.files.internal("04b30.fnt"));
        font1.setColor(Color.WHITE);
        font1.getData().setScale(0.8f);
    }

    @Override
    public void update(float delta) {
        //System.out.println("You are dead.");
        if (Gdx.input.isKeyJustPressed((Input.Keys.SPACE))) {
            gsm.set(new PlayingState(gsm));
        }

        if (Gdx.input.isKeyJustPressed(Input.Keys.ESCAPE)) {
            GameManager.getInstance().endGame();
            gsm.set(new MenuState(gsm));
        }
    }

    @Override
    public void render(ShapeRenderer shapeRenderer, SpriteBatch batch) {
        batch.setProjectionMatrix(camera.combined);
        batch.begin();
        batch.draw(deathImage, 0, 0, width, height);
        GlyphLayout layout = new GlyphLayout(font, "GAME OVER");
        GlyphLayout scoreLayout = new GlyphLayout(font1, "Your Score: " + finalScore);
        GlyphLayout coinLayout = new GlyphLayout(font1, "Coins Collected: " + finalCoins);
        GlyphLayout layout1 = new GlyphLayout(font1, "SPC/ESC to Restart/Quit");

        float corner = 40f;
        float x = corner;
        float y = height - corner;
        float yStats = (height + layout.height - (height - heightAwal)) / 2f;;
        float yCoin = (height + layout1.height - (height - heightAwal)) / 2f - layout.height - 20;
        float x1 = corner;
        float y1 = corner + layout1.height;
        font.draw(batch, layout, x, y);
        font1.draw(batch, layout1, x1, y1);
        font1.draw(batch, scoreLayout, x, yStats);
        font1.draw(batch, coinLayout, x, yCoin);
        batch.end();
    }

    @Override
    public void resize(int width, int height) {
        this.width = width;
        this.height = height;
        camera.setToOrtho(false, width, height);
    }

    @Override
    public void dispose() {
        font.dispose();
        font1.dispose();
        deathImage.dispose();
    }
}
