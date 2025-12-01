package com.dimar.frontend;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.dimar.frontend.commands.Command;
import com.dimar.frontend.commands.LompatCommand;
import com.dimar.frontend.commands.KananCommand;
import com.dimar.frontend.commands.KiriCommand;
import com.dimar.frontend.factories.GroundsFactory;
import com.dimar.frontend.observers.ScoreUIObserver;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * {@link com.badlogic.gdx.ApplicationListener} implementation shared by all platforms.
 */
public class Main extends ApplicationAdapter {
    private ShapeRenderer shapeRenderer;
    private GameManager gameManager;
    private Player player;
    private Ground ground;
    private OrthographicCamera camera;
    private ScoreUIObserver scoreUIObserver;
    private Random random = new Random();
    private GroundsFactory groundsFactory;
    private float maxWidth, maxHeight;
    private float widthAwal;
    private List<Command> playerCommand;
    private int lastLoggedScore = -1;
    float currentScore;
    private float GAP = 200f;
    private float HEIGHT_PLATFORM = 20f;
    private float MIN_WIDTH = 200f;
    private float MAX_WIDTH = 250f;

    @Override
    public void create() {
        widthAwal = Gdx.graphics.getWidth();
        maxWidth = Gdx.graphics.getWidth();
        maxHeight = Gdx.graphics.getHeight();
        shapeRenderer = new ShapeRenderer();
        groundsFactory = new GroundsFactory();
        player = new Player(new Vector2(Gdx.graphics.getWidth() / 2f, 50f));
        playerCommand = new ArrayList<>();
        playerCommand.add(new LompatCommand(player));
        playerCommand.add(new KiriCommand(player));
        playerCommand.add(new KananCommand(player));
        ground = new Ground(new Vector2(-Gdx.graphics.getWidth() / 2f, -450), 2 * Gdx.graphics.getWidth(), 500f, false);
        camera = new OrthographicCamera(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        camera.setToOrtho(false);
        float y = GAP;
        float widthAcuan = MIN_WIDTH + random.nextFloat() * (MAX_WIDTH - MIN_WIDTH);
        float titikAcuan = Gdx.graphics.getWidth() / 2f;
        groundsFactory.groundsPool.obtain(titikAcuan, y, widthAcuan, 1);
        createGrounds();
        scoreUIObserver = new ScoreUIObserver();
        gameManager = GameManager.getInstance();
        gameManager.addObserver(scoreUIObserver);
        gameManager.startGame();
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
        scoreUIObserver.render(scoreUIObserver.getScore(), 0);
        shapeRenderer.end();
    }

    public void update(float delta) {
        for (Command command : playerCommand) {
            command.execute();
        }

        camera.position.set(camera.position.x, player.getPosition().y + maxWidth * 0.05f, 0);
        camera.update();
        player.update(delta);
        player.setGrounded(false);
        player.handleGroundCollision(ground);

        float bottomScreen = camera.position.y - maxHeight / 2f;

        List<Grounds> toRelease = new ArrayList<>();

        for (Grounds grounds : groundsFactory.getInUse()) {
            if (grounds.posisiY < bottomScreen - GAP) {
                toRelease.add(grounds);
            }

            for (Ground ground : grounds.grounds) {
                player.handleGroundCollision(ground);
            }
        }

        for (Grounds g : toRelease) {
            groundsFactory.release(g);
        }

        if (player.getPosition().y >= groundsFactory.getInUse().get(groundsFactory.getInUse().size() - 1).posisiY - 2*GAP) {
            createGrounds();
        }

        int currentScoreMeters = (int)player.getVerticalDistanceTravelled();
        int previousScoreMeters = gameManager.getScore();

        if (currentScoreMeters > previousScoreMeters) {
            if (currentScoreMeters != lastLoggedScore) {
                System.out.println("Score: " + currentScoreMeters);
                lastLoggedScore = currentScoreMeters;
                currentScore = currentScoreMeters;
            }

            gameManager.setScore(currentScoreMeters);
        }

        player.checkBoundaries(maxWidth);
    }

    @Override
    public void resize(int width, int height) {
        camera.viewportHeight = height;
        camera.viewportWidth = width;
        maxWidth = width;
        maxHeight = height;
    }

    public void createGrounds() {
        float y = GAP;
        float maksGap = maxGap(Player.speed, Player.lompatan, Player.gravity, y, HEIGHT_PLATFORM);
        for (int x = 1; x <= 2; x++) {
            int kiriKanan = random.nextInt(2);
            float widthAcuan = MIN_WIDTH + random.nextFloat() * (MAX_WIDTH - MIN_WIDTH);
            float titikAcuan;
            Grounds groundTerakhir = groundsFactory.getInUse().get(groundsFactory.getInUse().size() - 1);
            float minX = (widthAwal - maxWidth) / 2f + GAP;
            float maxX = (widthAwal + maxWidth) / 2f - GAP;
            float posisiKanan = groundTerakhir.getPosisiAcuan() + groundTerakhir.widthAcuan + maksGap;
            float posisiKiri = groundTerakhir.getPosisiAcuan() - (widthAcuan + maksGap);

            if (kiriKanan == 1) {
                titikAcuan = posisiKanan;
            } else {
                titikAcuan = posisiKiri;
            }

            if (titikAcuan < minX) {
                kiriKanan = 1;
                titikAcuan = posisiKanan;
            }

            if (titikAcuan > maxX) {
                kiriKanan = 0;
                titikAcuan = posisiKiri;
            }

            titikAcuan = MathUtils.clamp(titikAcuan, minX, maxX);
            //System.out.println("maks= " + maxX + " x = " + titikAcuan + " y = " + groundTerakhir.posisiY + y);
            groundsFactory.groundsPool.obtain(titikAcuan, groundTerakhir.posisiY + y, widthAcuan, kiriKanan);
        }
    }

    float maxGap(float vx, float vy, float g, float gap, float hPlatform) {
        float lp = vy * vy / (2 * g);
        return (float) (vx * ((vy + Math.sqrt(vy * vy - 2 * g * lp)) / g + Math.sqrt((2 * (lp - (gap + hPlatform)) / g))));
    }

    @Override
    public void dispose() {
        scoreUIObserver.dispose();
        shapeRenderer.dispose();
        groundsFactory.releaseAll();
    }
}
