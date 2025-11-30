package com.dimar.frontend;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.dimar.frontend.factories.GroundsFactory;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Random;

/**
 * {@link com.badlogic.gdx.ApplicationListener} implementation shared by all platforms.
 */
public class Main extends ApplicationAdapter {
    private ShapeRenderer shapeRenderer;
    private Player player;
    private Ground ground;
    private OrthographicCamera camera;
    private Random random = new Random();
    private GroundsFactory groundsFactory;

    @Override
    public void create() {
        shapeRenderer = new ShapeRenderer();
        groundsFactory = new GroundsFactory();
        player = new Player(new Vector2(Gdx.graphics.getWidth() / 2f, 50f));
        ground = new Ground(new Vector2(0, -450), 2 * Gdx.graphics.getWidth(), 500f, false);
        camera = new OrthographicCamera(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        camera.setToOrtho(false);
        int y = 200;
        float maksGap = maxGap(player.getSpeed(), player.getLompatan(), player.getGravity(), y, 20f);
        float widthAcuan = 100f + random.nextFloat() * 100f;
        float titikAcuan = Gdx.graphics.getWidth() / 2f;
        groundsFactory.groundsPool.obtain(titikAcuan, y, widthAcuan, 1);
        for (int x = 1; x <= 4; x++) {
            int kiriKanan = random.nextInt(2);
            widthAcuan = 100f + random.nextFloat() * 100f;
            Grounds groundTerakhir = groundsFactory.getInUse().get(groundsFactory.getInUse().size() - 1);
            float minX = 200f;
            float maxX = Gdx.graphics.getWidth() - 200f;

            if (kiriKanan == 1) {
                titikAcuan = groundTerakhir.getPosisiAcuanSelanjutnya()
                    + groundTerakhir.widthAcuan + maksGap;
            } else {
                titikAcuan = groundTerakhir.getPosisiAcuanSelanjutnya()
                    - (groundTerakhir.widthAcuan + maksGap);
            }

            if (titikAcuan < minX) {
                kiriKanan = 1;
                titikAcuan = groundTerakhir.getPosisiAcuanSelanjutnya()
                    + groundTerakhir.widthAcuan + maksGap;
            }

            if (titikAcuan > maxX) {
                kiriKanan = 0;
                titikAcuan = groundTerakhir.getPosisiAcuanSelanjutnya()
                    - (groundTerakhir.widthAcuan + maksGap);
            }

            titikAcuan = MathUtils.clamp(titikAcuan, minX, maxX);

            groundsFactory.groundsPool.obtain(titikAcuan, groundTerakhir.posisiY + y, widthAcuan, kiriKanan);
        }
    }

    @Override
    public void render() {
        float delta = Gdx.graphics.getDeltaTime();
        update(delta);
        Gdx.gl.glClearColor(0, 0, 0, 0);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        shapeRenderer.setProjectionMatrix(camera.combined);
        shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
        for (Grounds grounds1 : groundsFactory.getInUse()) {
            grounds1.render(shapeRenderer);
        }

        player.render(shapeRenderer);
        ground.render(shapeRenderer);
        shapeRenderer.end();
    }

    public void update(float delta) {
        if (Gdx.input.isKeyPressed(Input.Keys.A)) {
            player.setKiri(true);
        } else {
            player.setKiri(false);
        }

        if (Gdx.input.isKeyPressed(Input.Keys.D)) {
            player.setKanan(true);
        } else {
            player.setKanan(false);
        }

        if (Gdx.input.isKeyPressed(Input.Keys.SPACE)) {
            player.Lompat();
        }

        camera.position.set(camera.position.x, player.getPosition().y + Gdx.graphics.getWidth() * 0.05f, 0);
        camera.update();
        player.update(delta);
        player.setGrounded(false);
        player.handleGroundCollision(ground);

        float bottomScreen = camera.position.y - Gdx.graphics.getHeight() / 2f;

        List<Grounds> toRelease = new ArrayList<>();

        for (Grounds grounds : groundsFactory.getInUse()) {
            if (grounds.posisiY < bottomScreen) {
                toRelease.add(grounds);
            }

            for (Ground ground : grounds.grounds) {
                player.handleGroundCollision(ground);
            }
        }

        for (Grounds g : toRelease) {
            groundsFactory.release(g);
        }

        if (player.getPosition().y >= groundsFactory.getInUse().get(groundsFactory.getInUse().size() - 1).posisiY - 200) {
            createGrounds();
        }

        player.checkBoundaries();
    }

    public void createGrounds() {
        float y = 200f;
        float maksGap = maxGap(player.getSpeed(), player.getLompatan(), player.getGravity(), y, 20f);
        for (int x = 1; x <= 2; x++) {
            int kiriKanan = random.nextInt(2);
            float widthAcuan = 100f + random.nextFloat() * 100f;
            float titikAcuan;
            Grounds groundTerakhir = groundsFactory.getInUse().get(groundsFactory.getInUse().size() - 1);
            float minX = 200f;
            float maxX = Gdx.graphics.getWidth() - 200f;
            if (kiriKanan == 1) {
                titikAcuan = groundTerakhir.getPosisiAcuanSelanjutnya()
                    + groundTerakhir.widthAcuan + maksGap;
            } else {
                titikAcuan = groundTerakhir.getPosisiAcuanSelanjutnya()
                    - (groundTerakhir.widthAcuan + maksGap);
            }

            if (titikAcuan < minX) {
                kiriKanan = 1;
                titikAcuan = groundTerakhir.getPosisiAcuanSelanjutnya()
                    + groundTerakhir.widthAcuan + maksGap;
            }

            if (titikAcuan > maxX) {
                kiriKanan = 0;
                titikAcuan = groundTerakhir.getPosisiAcuanSelanjutnya()
                    - (groundTerakhir.widthAcuan + maksGap);
            }

            titikAcuan = MathUtils.clamp(titikAcuan, minX, maxX);

            groundsFactory.groundsPool.obtain(titikAcuan, groundTerakhir.posisiY + y, widthAcuan, kiriKanan);
        }
    }

    float maxGap(float vx, float vy, float g, float gap, float hPlatform) {
        float lp = vy * vy / (2 * g);
        return (float) (vx * ((vy + Math.sqrt(vy * vy - 2 * g * lp)) / g + Math.sqrt((2 * (lp - (gap + hPlatform)) / g)))) - 20f;
    }

    @Override
    public void dispose() {
        shapeRenderer.dispose();
    }
}
