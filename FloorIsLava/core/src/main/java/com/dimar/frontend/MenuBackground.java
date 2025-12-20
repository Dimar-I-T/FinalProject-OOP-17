package com.dimar.frontend;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;
import com.dimar.frontend.states.GameState;

import java.util.ArrayList;
import java.util.List;

public class MenuBackground {
    private Texture backgroundTexture;
    private TextureRegion backgroundRegion;
    private List<Clouds> clouds;
    private float width;
    private float height;
    private Player player;
    private Ground ground;
    private OrthographicCamera camera;

    public MenuBackground() {
        backgroundTexture = new Texture(Gdx.files.internal("menu/SKY/1.png"));
        backgroundRegion = new TextureRegion(backgroundTexture);
        clouds = new ArrayList<>();

        this.width = Gdx.graphics.getWidth();
        this.height = Gdx.graphics.getHeight();
        camera = new OrthographicCamera();
        camera.setToOrtho(false, width, height);
        camera.update();

        ground = new Ground(new Vector2(0f,-5f), 2 * Gdx.graphics.getWidth(), 150f, false);
        ground.setTexture(new Texture("menu/GROUND/dirt.jpg"));
        player = new Player(new Vector2(100f, 50f));
        player.setSize(150f);

        clouds.add(0, new Clouds(0, ground.getHeight(),20f, 0f));
        clouds.get(0).setTexture(new Texture("menu/SKY/2.png"));
        clouds.add(1, new Clouds(0,ground.getHeight(),10f, 0f));
        clouds.get(1).setTexture(new Texture("menu/SKY/3.png"));
        clouds.add(2, new Clouds(0, ground.getHeight(),5f, 0f));
        clouds.get(2).setTexture(new Texture("menu/SKY/4.png"));
    }

    public void update(float delta){
        player.update(delta);
        player.handleGroundCollision(ground);
        for (Clouds c: clouds){
            c.update(delta);
        }
    }

    public void render(SpriteBatch batch, GameState currentState){
        batch.begin();
        batch.draw(backgroundRegion, 0f, 0f, width, height);
        for (Clouds c: clouds){
            c.render(batch);
        }
//        batch.end();
        ground.renderTexture(batch, ground.getHeight() / 2f, true);
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
