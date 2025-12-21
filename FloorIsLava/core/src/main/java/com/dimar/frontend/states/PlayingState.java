package com.dimar.frontend.states;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.dimar.frontend.*;
import com.dimar.frontend.backgrounds.Background;
import com.dimar.frontend.commands.*;
import com.dimar.frontend.factories.CoinFactory;
import com.dimar.frontend.factories.GroundsFactory;
import com.dimar.frontend.observers.ScoreUIObserver;
import com.dimar.frontend.strategies.*;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Random;

public class PlayingState implements GameState {
    private final GameStateManager gsm;
    private final ShapeRenderer shapeRenderer;
    private final GameManager gameManager;
    private final Player player;
    private final Ground ground;
    private final Lava lava;
    private final OrthographicCamera camera;
    private final ScoreUIObserver scoreUIObserver;
    private final Random random = new Random();
    private final GroundsFactory groundsFactory;
    private float maxWidth, maxHeight;
    private final float widthAwal;
    private final List<Command> playerCommand;
    private final List<Command> dashCommand;
    private int lastLoggedScore = -1;
    float currentScore;
    private final float GAP = 200f;
    private final float MIN_WIDTH = 160f;
    private final float MAX_WIDTH = 200f;
    private final float POSISI_Y_AWAL = -1300f;
    private float jarakPlayerLava = 0f;
    private float batasNaikDifficulty = 0f;
    private boolean menungguUpdateBatas = false;
    private float timerBatas = 0f;
    private int level = 0;
    Grounds groundDiAtasPlayer;
    private final CoinFactory coinFactory;
    private final List<CoinPattern> coinPatterns;
    List<Grounds> toRelease;
    List<Coin> coinsToRelease;
    private boolean bisaDash = false;
    private DifficultyStrategy difficultyStrategy;

    private Background background;

    private boolean isPaused = false;

    private Sound coinSound;
    private Music bgMusic;
    private String currentMusicFile = ""; // Melacak file musik aktif

    private Texture dashUnavailable;
    private Texture dashAvailable;

    public PlayingState(GameStateManager gsm) {
        this.gsm = gsm;
        BitmapFont fontDash = new BitmapFont(Gdx.files.internal("arial.fnt"));
        fontDash.setColor(Color.WHITE);
        toRelease = new ArrayList<>();
        coinsToRelease = new ArrayList<>();
        coinFactory = new CoinFactory();
        coinPatterns = new ArrayList<>();
        coinPatterns.add(new LinePattern());
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
        playerCommand.add(new PauseCommand(gsm, this));
        dashCommand = new ArrayList<>();
        dashCommand.add(new DashKiriCommand(player));
        dashCommand.add(new DashKananCommand(player));
        lava = new Lava(new Vector2(-Gdx.graphics.getWidth() / 2f, POSISI_Y_AWAL), 2 * Gdx.graphics.getWidth(), 1000f);

        // Load Asset Suara
        coinSound = Gdx.audio.newSound(Gdx.files.internal("audio/sound/coin-collected.wav"));

        // Init Difficulty (Ini akan memicu updateMusic otomatis ke easy.wav)
        setDifficulty(new VeryEasyDifficulty());

        batasNaikDifficulty = difficultyStrategy.getBatasNaikDifficulty();
        ground = new Ground(new Vector2(-Gdx.graphics.getWidth() / 2f, -450), 2 * Gdx.graphics.getWidth(), 500f, false);
        ground.setTexture(new Texture("Cobblestone.jpg"));
        player.setGroundSekarang(ground);
        camera = new OrthographicCamera(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        camera.setToOrtho(false);
        float HEIGHT_PLATFORM = 20f;
        float widthAcuan = MIN_WIDTH + random.nextInt((int)((MAX_WIDTH - MIN_WIDTH) / HEIGHT_PLATFORM) + 1) * HEIGHT_PLATFORM;
        float titikAcuan = random.nextFloat() * (Gdx.graphics.getWidth() - 300f);
        groundDiAtasPlayer = groundsFactory.groundsPool.obtain(titikAcuan, GAP, widthAcuan, 1);
        createGrounds(groundsFactory.getInUse());
        scoreUIObserver = new ScoreUIObserver();
        gameManager = GameManager.getInstance();
        gameManager.addObserver(scoreUIObserver);
        gameManager.startGame();

        background = new Background();

        dashUnavailable = new Texture("dash/DASH.png");
        dashAvailable = new Texture("dash/DASH_ACTIVE.png");
        Gdx.input.setCursorCatched(true);
    }

    public void setPaused(boolean paused) {
        this.isPaused = paused;
        // Pause/Resume musik sesuai state
        if (bgMusic != null) {
            if (paused) bgMusic.pause();
            else bgMusic.play();
        }
    }

    public boolean isPaused() {
        return isPaused;
    }

    @Override
    public void render(ShapeRenderer shapeRenderer, SpriteBatch spriteBatch) {
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        spriteBatch.setProjectionMatrix(camera.combined);
        spriteBatch.begin();
        spriteBatch.disableBlending();
        background.render(spriteBatch, camera);
        ground.renderTexture(spriteBatch, 300f, false, null);
        lava.renderTexture(spriteBatch);
        spriteBatch.enableBlending();
        player.renderTexture(spriteBatch);
        for (Grounds grounds1 : groundsFactory.getInUse()) {
            grounds1.render(spriteBatch);
        }

        float iconSize = 120f;
        float padding = 30f;
        float x = camera.position.x - (maxWidth / 2f) + padding;
        float y = camera.position.y + (maxHeight / 2f) - iconSize - padding - 10f;

        spriteBatch.draw(bisaDash ? dashAvailable : dashUnavailable, x, y, iconSize, iconSize);
        for (Coin coin : coinFactory.getInUse()) {
            coin.render(spriteBatch);
        }

        spriteBatch.end();
        shapeRenderer.setProjectionMatrix(camera.combined);
        scoreUIObserver.render(scoreUIObserver.getScore(), gameManager.getCoinsCollected(), difficultyStrategy.getMode());
    }

    public void update(float delta) {
        // Fix delta spike saat loading
        if (delta > 0.1f) {
            delta = 0.016f;
        }

        if (player.getIsDead()) {
            gameManager.endGame();
            if (bgMusic != null) {
                bgMusic.stop();
            }
            gsm.set(new GameOverState(gsm));
            return;
        }

        for (Command command : playerCommand) {
            if (isPaused && !(command instanceof PauseCommand)) {
                continue;
            }
            command.execute();
        }

        if (isPaused) return;

        camera.position.set(camera.position.x, player.getPosition().y + maxHeight * 0.05f, 0);
        camera.update();

        background.update(camera.position.y);
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
        hitungLevel();
        bisaDash = hitungWaktu();
        if (bisaDash) {
            for (Command command : dashCommand) {
                command.execute();
            }
        }

        cariGroundsSelanjutnya(groundsInUse, (int) (level + GAP));

        for (Coin coin : coinsInUse) {
            coin.update(delta);
        }

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

        int currentScoreMeters = (int) (level / GAP);
        int previousScoreMeters = gameManager.getScore();

        if (currentScoreMeters > previousScoreMeters) {
            if (currentScoreMeters != lastLoggedScore) {
                lastLoggedScore = currentScoreMeters;
                currentScore = currentScoreMeters;
            }

            gameManager.setScore(currentScoreMeters);
        }

        player.checkBoundaries(maxWidth);
        setJarak(delta);
    }

    public void setJarak(float delta) {
        if (jarakPlayerLava > batasNaikDifficulty && !menungguUpdateBatas) {
            setDifficulty(difficultyStrategy.getNextDifficulty());
            menungguUpdateBatas = true;
            timerBatas = 0f;
        }

        if (menungguUpdateBatas) {
            timerBatas += delta;
            if (timerBatas >= difficultyStrategy.getTimerToNext()) {
                batasNaikDifficulty = difficultyStrategy.getBatasNaikDifficulty();
                menungguUpdateBatas = false;
            }
        }
    }

    public boolean hitungWaktu() {
        float jarakXPlayerKeAcuan = Math.abs(groundDiAtasPlayer.posisiAcuan - player.getPosition().x);
        float kecepatanPlayer = Player.speed;
        float waktuKeAcuan = jarakXPlayerKeAcuan / kecepatanPlayer;
        float jarakYLavaKePlayer = player.getPosition().y - lava.getPosition().y - lava.getHeight();
        jarakPlayerLava = jarakYLavaKePlayer;
        float epsilon = 1e-3f;
        float kecepatanLava = Math.max(lava.getKecepatan(), epsilon);
        float waktuLavaKePlayer = jarakYLavaKePlayer / kecepatanLava;
        if (waktuKeAcuan > waktuLavaKePlayer) {
            player.setJarakDash(0.8f * jarakXPlayerKeAcuan);
        }

        return waktuKeAcuan > waktuLavaKePlayer;
    }

    public void setDifficulty(DifficultyStrategy difficultyStrategy) {
        this.difficultyStrategy = difficultyStrategy;
        lava.setKecepatan(difficultyStrategy.getKecepatanLava());

        // Update musik setiap ganti difficulty
        updateMusic(difficultyStrategy);
    }

    private void updateMusic(DifficultyStrategy strategy) {
        if (player != null && player.getIsDead()) return;
        String newMusicFile = "audio/music/easy.wav"; // Default untuk VeryEasy & Easy

        if (strategy instanceof MediumDifficulty) {
            newMusicFile = "audio/music/medium.wav";
        } else if (strategy instanceof HardDifficulty || strategy instanceof VeryHardDifficulty || strategy instanceof ExtremeDifficulty) {
            newMusicFile = "audio/music/hard.wav";
        }

        // Hanya ganti jika file beda
        if (!newMusicFile.equals(currentMusicFile)) {
            if (bgMusic != null) {
                bgMusic.stop();
                bgMusic.dispose();
                bgMusic = null;
            }
            try {
                bgMusic = Gdx.audio.newMusic(Gdx.files.internal(newMusicFile));
                bgMusic.setLooping(true);
                // VOLUME BACKGROUND
                bgMusic.setVolume(0.2f);
                if (!isPaused) bgMusic.play();
                currentMusicFile = newMusicFile;
            } catch (Exception e) {
                System.out.println("Gagal load musik: " + newMusicFile);
            }
        }
    }

    public void hitungLevel() {
        level = (int) player.getGroundSekarang().getPosition().y;
    }

    public void checkCoinsCollision(List<Coin> coinsInUse) {
        Rectangle colliderPlayer = player.getCollider();
        Iterator<Coin> iterator = coinsInUse.iterator();
        while (iterator.hasNext()) {
            Coin coin = iterator.next();
            if (coin.isColliding(colliderPlayer)) {
                coinSound.play(1.0f);

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
        float HEIGHT_PLATFORM = 20f;
        float maksGap = maxGap(Player.speed, Player.lompatan, Player.gravity, y, HEIGHT_PLATFORM);
        for (int x = 1; x == 1; x++) {
            int kiriKanan = random.nextInt(2);
            float widthAcuan = MIN_WIDTH + random.nextInt((int)((MAX_WIDTH - MIN_WIDTH) / HEIGHT_PLATFORM) + 1) * HEIGHT_PLATFORM;
            float titikAcuan;
            Grounds groundTerakhir = groundsInUse.get(groundsInUse.size() - 1);
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
            Grounds grounds = groundsFactory.groundsPool.obtain(titikAcuan, groundTerakhir.posisiY + y, widthAcuan, kiriKanan);
            spawnLineCoin(grounds.grounds);
        }
    }

    public void cariGroundsSelanjutnya(List<Grounds> groundsInUse, int target) {
        if (groundsInUse.isEmpty()) {
            return;
        }

        int indeksKiri = 0, indeksKanan = groundsInUse.size() - 1;
        int indeksTengah;
        while (indeksKiri <= indeksKanan) {
            indeksTengah = (indeksKiri + indeksKanan) / 2;
            Grounds grounds = groundsInUse.get(indeksTengah);
            if (target == grounds.posisiY) {
                groundDiAtasPlayer = grounds;
                return;
            } else {
                if (target < grounds.posisiY) {
                    indeksKanan = indeksTengah - 1;
                } else {
                    indeksKiri = indeksTengah + 1;
                }
            }
        }
    }

    public void spawnLineCoin(List<Ground> grounds) {
        for (Ground ground : grounds) {
            float width = ground.getWidth();
            float x = ground.getPosition().x;
            float y = ground.getPosition().y;
            float height = ground.getHeight();
            float randX = 0.5f * random.nextFloat() * width;
            float radius = 3.5f * 10f;
            float spacing = 30f;
            int n = (int) ((width - randX + spacing - 2*radius) / spacing);
            int r = random.nextInt(10);
            if (r < 2) {
                spawnCoins(radius + x + randX, y + height + 15f, n);
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
        lava.reset(new Vector2(-Gdx.graphics.getWidth() / 2f, POSISI_Y_AWAL));
        toRelease.clear();
        coinsToRelease.clear();
        player.reset();
        lastLoggedScore = -1;
        currentScore = 0f;
        gameManager.setScore(0);
        gameManager.setCoinsCollected(0);

        // Reset music via setDifficulty (Kembali ke VeryEasy -> easy.wav)
        currentMusicFile = "";
        setDifficulty(new VeryEasyDifficulty());

        groundsFactory.releaseAll();
        coinFactory.releaseAll();

        camera.position.set(camera.position.x, player.getPosition().y + maxHeight * 0.05f, 0);
        camera.update();

        float widthAcuan = MIN_WIDTH + random.nextFloat() * (MAX_WIDTH - MIN_WIDTH);
        float titikAcuan = random.nextFloat() * (Gdx.graphics.getWidth() - 300f);
        groundDiAtasPlayer = groundsFactory.groundsPool.obtain(titikAcuan, GAP, widthAcuan, 1);
        createGrounds(groundsFactory.getInUse());
    }

    @Override
    public void dispose() {
        toRelease.clear();
        coinsToRelease.clear();
        playerCommand.clear();
        gameManager.removeObserver(scoreUIObserver);
        scoreUIObserver.dispose();
        shapeRenderer.dispose();
        groundsFactory.releaseAll();
        coinFactory.releaseAll();
        player.dispose();
        coinSound.dispose();
        if (bgMusic != null) {
            bgMusic.stop();
            bgMusic.dispose();
        }
    }
}
