package com.dimar.frontend.backgrounds;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.dimar.frontend.Clouds;
import com.dimar.frontend.Ground;
import com.dimar.frontend.Player;
import com.dimar.frontend.states.GameState;

import java.util.ArrayList;
import java.util.List;

public class ControlBackground {
    private Texture backgroundTexture;
    private TextureRegion backgroundRegion;
    private List<Clouds> clouds;
    private float width;
    private float height;
    private Player player;
    private Ground ground;
    private OrthographicCamera camera;

    public ControlBackground() {
        backgroundTexture = new Texture(Gdx.files.internal("control/SKY/1.png"));
        backgroundRegion = new TextureRegion(backgroundTexture);
        clouds = new ArrayList<>();

        this.width = Gdx.graphics.getWidth();
        this.height = Gdx.graphics.getHeight();
        camera = new OrthographicCamera();
        camera.setToOrtho(false, width, height);
        camera.update();

        ground = new Ground(new Vector2(0f,-5f), 2 * Gdx.graphics.getWidth(), 150f, false);
        ground.setTexture(new Texture("control/GROUND/dirt.jpg"));
        player = new Player(new Vector2(100f, 50f));
        player.setSize(150f);

        clouds.add(0, new Clouds(0, ground.getHeight(),20f, 0f));
        clouds.get(0).setTexture(new Texture("control/SKY/2.png"));
        clouds.add(1, new Clouds(0,ground.getHeight(),10f, 0f));
        clouds.get(1).setTexture(new Texture("control/SKY/3.png"));
    }

    public void update(float delta){
        player.update(delta);
        player.handleGroundCollision(ground);
        for (Clouds c: clouds){
            c.update(delta);
        }
    }

    public void render(SpriteBatch batch, GameState currentState){
        batch.setProjectionMatrix(camera.combined);
        batch.begin();
        batch.draw(backgroundRegion, 0f, 0f, width, height);
        for (Clouds c: clouds){
            c.render(batch);
        }
//        batch.end();
        ground.renderTexture(batch, ground.getHeight() / 2f, true, new Texture("control/GROUND/Grass.png"));
//        batch.begin();
        player.renderTexture(batch);
        batch.end();
    }

    public void dispose(){
        for (Clouds c: clouds){
            c.dispose();
        }
        this.player = null;
        clouds.clear();
        ground.dispose();
        backgroundTexture.dispose();
    }
}
