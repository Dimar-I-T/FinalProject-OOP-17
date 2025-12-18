package com.dimar.frontend;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Grounds {
    public List<Ground> grounds = new ArrayList<>();
    public Rectangle collider;
    public float posisiAcuan;
    private float screenWidth = Gdx.graphics.getWidth();
    private final Random random = new Random();
    public float widthAcuan;
    public float posisiY;
    private final float maxGap = 2 * Player.speed * Player.lompatan / Player.gravity;
    private boolean active;
    private Texture edgeKanan;
    private Texture middle;
    private Texture edgeKiri;

    public Grounds(float posisiAcuan, float posisiY, float widthAcuan, int kiriKanan) {
        edgeKanan = new Texture("wood_edge2.png");
        middle = new Texture("wood_middle.png");
        edgeKiri = new Texture("wood_edge1.png");

        initialize(posisiAcuan, posisiY, widthAcuan, kiriKanan);
    }

    public void initialize(float posisiAcuan, float posisiY, float widthAcuan, int kiriKanan) {
        float height = 20f;
        collider = new Rectangle(posisiAcuan, posisiY, screenWidth, height);
        grounds.clear();
        this.posisiY = posisiY;
        this.posisiAcuan = posisiAcuan;
        this.widthAcuan = widthAcuan;
        Ground groundAcuan = new Ground(new Vector2(posisiAcuan, posisiY), widthAcuan, height, true);
        grounds.add(groundAcuan);
        float tambahKiri = 0;
        float tambahKanan = 0;

        // kanan
        float gapMin = 120f;
        float gap = tambahKanan + gapMin + random.nextFloat() * (maxGap - gapMin);
        float x = groundAcuan.getPosition().x + widthAcuan + gap;
        float widthMin = 160f;
        float maxWidth = 200f;
        if (kiriKanan == 1) {
            while (x < screenWidth) {
                float width = widthMin + random.nextInt((int)((maxWidth - widthMin) / height) + 1) * height;
                Ground ground = new Ground(new Vector2(x, posisiY), width, height, false);
                grounds.add(ground);
                gap = gapMin + random.nextFloat() * (maxGap - gapMin);
                x = ground.getPosition().x + width + gap;
            }
        }

        // kiri
        if (kiriKanan == 0) {
            gap = tambahKiri + gapMin + random.nextFloat() * (maxGap - gapMin);
            x = groundAcuan.getPosition().x - gap;
            while (x > 0) {
                float width = widthMin + random.nextInt((int)((maxWidth - widthMin) / height) + 1) * height;
                Ground ground = new Ground(new Vector2(x - width, posisiY), width, height, false);
                grounds.add(ground);
                gap = gapMin + random.nextFloat() * (maxGap - gapMin);
                x = ground.getPosition().x - gap;
            }
        }
    }

    public void renderShape(ShapeRenderer shapeRenderer) {
        for (Ground g : grounds) {
            g.render(shapeRenderer);
        }
    }

    public void render(SpriteBatch batch){
        // MASIH BELOM BENER
        for (Ground g: grounds){
            float scaledWidth = g.getHeight();

            float startX = g.getPosition().x;
            float boundX = startX + g.getWidth();

            batch.draw(edgeKiri, startX, g.getPosition().y, scaledWidth, g.getHeight());
            for (float x = startX + scaledWidth; x < boundX - scaledWidth; x += scaledWidth){
                batch.draw(middle, x, g.getPosition().y, scaledWidth, g.getHeight());
            }

            batch.draw(edgeKanan, boundX - scaledWidth, g.getPosition().y, scaledWidth, g.getHeight());
        }
    }

    public float getPosisiAcuan() {
        int selanjutnya = random.nextInt(grounds.size());
        return grounds.get(selanjutnya).getPosition().x;
    }

    public void setActive(boolean active) {
        this.active = active;
    }
}
