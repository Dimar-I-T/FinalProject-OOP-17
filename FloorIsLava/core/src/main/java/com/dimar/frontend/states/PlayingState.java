package com.dimar.frontend.states;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g3d.particles.renderers.PointSpriteRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.dimar.frontend.*;
import com.dimar.frontend.commands.*;
import com.dimar.frontend.factories.CoinFactory;
import com.dimar.frontend.factories.GroundsFactory;
import com.dimar.frontend.observers.ScoreUIObserver;
import com.dimar.frontend.strategies.CoinPattern;
import com.dimar.frontend.strategies.LinePattern;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Random;

public class PlayingState implements GameState {
    private final GameStateManager gsm;
    private ShapeRenderer shapeRenderer;
    private GameManager gameManager;
    private Player player;
    private Ground ground;
    private Lava lava;
    private OrthographicCamera camera;
    private ScoreUIObserver scoreUIObserver;
    private Random random = new Random();
    private GroundsFactory groundsFactory;
    private float maxWidth, maxHeight;
    private float widthAwal, heightAwal;
    private List<Command> playerCommand;
    private int lastLoggedScore = -1;
    float currentScore;
    private float GAP = 200f;
    private float HEIGHT_PLATFORM = 20f;
    private float MIN_WIDTH = 150f;
    private float MAX_WIDTH = 200f;
    private float POSISI_Y_AWAL = -1500f;
    private CoinFactory coinFactory;
    private List<CoinPattern> coinPatterns;
    List<Grounds> toRelease;
    List<Coin> coinsToRelease;

    public PlayingState(GameStateManager gsm) {
        this.gsm = gsm;
        toRelease = new ArrayList<>();
        coinsToRelease = new ArrayList<>();
        coinFactory = new CoinFactory();
        coinPatterns = new ArrayList<>();
        coinPatterns.add(new LinePattern());
        widthAwal = Gdx.graphics.getWidth();
        heightAwal = Gdx.graphics.getHeight();
        maxWidth = Gdx.graphics.getWidth();
        maxHeight = Gdx.graphics.getHeight();
        shapeRenderer = new ShapeRenderer();
        groundsFactory = new GroundsFactory();
        player = new Player(new Vector2(Gdx.graphics.getWidth() / 2f, 50f));
        playerCommand = new ArrayList<>();
        playerCommand.add(new LompatCommand(player));
        playerCommand.add(new KiriCommand(player));
        playerCommand.add(new KananCommand(player));
        playerCommand.add(new PauseCommand(gsm, this));
        ground = new Ground(new Vector2(-Gdx.graphics.getWidth() / 2f, -450), 2 * Gdx.graphics.getWidth(), 500f, false);
        lava = new Lava(new Vector2(-Gdx.graphics.getWidth() / 2f, POSISI_Y_AWAL), 3 * Gdx.graphics.getWidth(), 1000f);
        camera = new OrthographicCamera(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        camera.setToOrtho(false);
        float y = GAP;
        float widthAcuan = MIN_WIDTH + random.nextFloat() * (MAX_WIDTH - MIN_WIDTH);
        float titikAcuan = Gdx.graphics.getWidth() / 2f;
        groundsFactory.groundsPool.obtain(titikAcuan, y, widthAcuan, 1);
        createGrounds(groundsFactory.getInUse());
        scoreUIObserver = new ScoreUIObserver();
        gameManager = GameManager.getInstance();
        gameManager.addObserver(scoreUIObserver);
        gameManager.startGame();
    }

    @Override
    public void render(ShapeRenderer shapeRenderer, SpriteBatch spriteBatch) {
        Gdx.gl.glClearColor(0, 0, 0, 0);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        shapeRenderer.setProjectionMatrix(camera.combined);
        shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
        for (Grounds grounds1 : groundsFactory.getInUse()) {
            grounds1.render(shapeRenderer);
        }

        for (Coin coin : coinFactory.getInUse()) {
            coin.renderShape(shapeRenderer);
        }

        player.render(shapeRenderer);
        ground.render(shapeRenderer);
        lava.render(shapeRenderer);
        shapeRenderer.end();
        scoreUIObserver.render(scoreUIObserver.getScore(), gameManager.getCoinsCollected());
    }

    public void update(float delta) {
        if (player.getIsDead()) {
            reset();
            gsm.set(new GameOverState(gsm));
            return;
        }

        for (Command command : playerCommand) {
            command.execute();
        }

        camera.position.set(camera.position.x, player.getPosition().y + maxHeight * 0.05f, 0);
        camera.update();
        player.update(delta);
        toRelease.clear();
        coinsToRelease.clear();
        List<Grounds> groundsInUse = groundsFactory.getInUse();
        List<Coin> coinsInUse = coinFactory.getInUse();
        checkLavaCollision();
        checkCoinsCollision(coinsInUse);
        if (player.getIsDead()) {
            return;
        }

        lava.update(delta);
        player.setGrounded(false);
        player.handleGroundCollision(ground);

        for (Grounds grounds : groundsInUse) {
            if (lava.isCollidingWithGround(grounds.collider)) {
                toRelease.add(grounds);
            }

            for (Ground ground : grounds.grounds) {
                player.handleGroundCollision(ground);
            }
        }

        for (Coin coin : coinsInUse) {
            if (lava.isColliding(coin.getCollider())) {
                coinsToRelease.add(coin);
            }
        }

        for (Coin coin : coinsToRelease) {
            coinFactory.release(coin);
        }

        for (Grounds g : toRelease) {
            groundsFactory.release(g);
        }

        if (player.getPosition().y >= groundsInUse.get(groundsInUse.size() - 1).posisiY - 2 * GAP) {
            createGrounds(groundsInUse);
        }

        int currentScoreMeters = (int) player.getVerticalDistanceTravelled();
        int previousScoreMeters = gameManager.getScore();

        if (currentScoreMeters > previousScoreMeters) {
            if (currentScoreMeters != lastLoggedScore) {
                lastLoggedScore = currentScoreMeters;
                currentScore = currentScoreMeters;
            }

            gameManager.setScore(currentScoreMeters);
        }

        player.checkBoundaries(maxWidth);
    }

    public void checkCoinsCollision(List<Coin> coinsInUse) {
        Rectangle colliderPlayer = player.getCollider();

        Iterator<Coin> iterator = coinsInUse.iterator();
        while (iterator.hasNext()) {
            Coin coin = iterator.next();
            if (coin.isColliding(colliderPlayer)) {
                gameManager.addCoin();
                coin.setActive(false);
                iterator.remove();
                coinFactory.release(coin);
            }
        }
    }

    public void checkLavaCollision() {
        if (lava.isCollidingWithPlayer(player.getCollider())) {
            player.die();
        }
    }

    @Override
    public void resize(int width, int height) {
        camera.viewportHeight = height;
        camera.viewportWidth = width;
        maxWidth = width;
        maxHeight = height;
        scoreUIObserver.updateWH(width, height);
    }

    public void createGrounds(List<Grounds> groundsInUse) {
        float y = GAP;
        float maksGap = maxGap(Player.speed, Player.lompatan, Player.gravity, y, HEIGHT_PLATFORM);
        for (int x = 1; x <= 2; x++) {
            int kiriKanan = random.nextInt(2);
            float widthAcuan = MIN_WIDTH + random.nextFloat() * (MAX_WIDTH - MIN_WIDTH);
            float titikAcuan;
            Grounds groundTerakhir = groundsInUse.get(groundsInUse.size() - 1);
            //System.out.println(groundTerakhir.grounds.size());
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
            Grounds grounds = groundsFactory.groundsPool.obtain(titikAcuan, groundTerakhir.posisiY + y, widthAcuan, kiriKanan);
            spawnLineCoin(grounds.grounds);
        }
    }

    public void spawnLineCoin(List<Ground> grounds) {
        for (Ground ground : grounds) {
            float width = ground.getWidth();
            float x = ground.getPosition().x;
            float y = ground.getPosition().y;
            float height = ground.getHeight();
            float randX = 0.5f * random.nextFloat() * width;
            float radius = 10f;
            float spacing = 30f;
            int n = (int) ((width - randX + spacing) / (2 * radius + spacing));
            //System.out.println("width = " + width + " x = " + x + " randX = " + randX + " n = " + n);
            int r = random.nextInt(10);
            if (r < 2) {
                spawnCoins(radius + x + randX, y + height + radius + 5f, n);
            }
        }
    }

    public void spawnCoins(float spawnX, float spawnY, int banyakKoin) {
        if (!coinPatterns.isEmpty()) {
            CoinPattern pattern = coinPatterns.get(random.nextInt(coinPatterns.size()));
            pattern.spawn(coinFactory, spawnX, spawnY, banyakKoin);
        }
    }

    float maxGap(float vx, float vy, float g, float gap, float hPlatform) {
        float lp = vy * vy / (2 * g);
        return (float) (vx * ((vy + Math.sqrt(vy * vy - 2 * g * lp)) / g + Math.sqrt((2 * (lp - (gap + hPlatform)) / g))));
    }

    public void reset() {
        //System.out.println("terpanggil");
        lava.reset(new Vector2(-Gdx.graphics.getWidth() / 2f, POSISI_Y_AWAL));
        player.reset();
        lastLoggedScore = -1;
        currentScore = 0f;
        gameManager.setScore(0);
        gameManager.setCoinsCollected(0);

        groundsFactory.releaseAll();
        coinFactory.releaseAll();

        camera.position.set(camera.position.x, player.getPosition().y + maxHeight * 0.05f, 0);
        camera.update();

        float y = GAP;
        float widthAcuan = MIN_WIDTH + random.nextFloat() * (MAX_WIDTH - MIN_WIDTH);
        float titikAcuan = Gdx.graphics.getWidth() / 2f;
        groundsFactory.groundsPool.obtain(titikAcuan, y, widthAcuan, 1);
        createGrounds(groundsFactory.getInUse());
    }

    @Override
    public void dispose() {
        scoreUIObserver.dispose();
        shapeRenderer.dispose();
        groundsFactory.releaseAll();
        coinFactory.releaseAll();
    }
}
