package com.dimar.frontend;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Grounds {
    public List<Ground> grounds = new ArrayList<>();
    public float posisiAcuan;
    private float gapMin = 250f;
    private float widthMin = 80f;
    private float screenWidth = Gdx.graphics.getWidth();
    private float height = 20f;
    private Random random = new Random();
    public float widthAcuan;
    public float posisiY;
    private boolean active;

    public Grounds(float posisiAcuan, float posisiY, float widthAcuan, int kiriKanan) {
        this.posisiY = posisiY;
        this.posisiAcuan = posisiAcuan;
        this.widthAcuan = widthAcuan;
        Ground groundAcuan = new Ground(new Vector2(posisiAcuan, posisiY), widthAcuan, height, true);
        grounds.add(groundAcuan);
        float tambahKiri = 0;
        float tambahKanan = 0;

        // kanan
        float gap = tambahKanan + gapMin + random.nextFloat() * 120f;
        float x = groundAcuan.getPosition().x + widthAcuan + gap;
        if (kiriKanan == 1) {
            while (x < screenWidth) {
                float width = widthMin + random.nextFloat() * 50f;
                Ground ground = new Ground(new Vector2(x, posisiY), width, height, false);
                grounds.add(ground);
                gap = gapMin + random.nextFloat() * 120f;
                x = ground.getPosition().x + width + gap;
            }
        }

        // kiri
        if (kiriKanan == 0) {
            gap = tambahKiri + gapMin + random.nextFloat() * 120f;
            x = groundAcuan.getPosition().x - gap;
            while (x > 0) {
                float width = widthMin + random.nextFloat() * 50f;
                Ground ground = new Ground(new Vector2(x - width, posisiY), width, height, false);
                grounds.add(ground);
                gap = gapMin + random.nextFloat() * 120f;
                x = ground.getPosition().x - gap;
            }
        }
    }

    public void initialize(float posisiAcuan, float posisiY, float widthAcuan, int kiriKanan) {
        grounds.clear();
        this.posisiY = posisiY;
        this.posisiAcuan = posisiAcuan;
        this.widthAcuan = widthAcuan;
        Ground groundAcuan = new Ground(new Vector2(posisiAcuan, posisiY), widthAcuan, height, true);
        grounds.add(groundAcuan);
        float tambahKiri = 0;
        float tambahKanan = 0;

        // kanan
        float gap = tambahKanan + gapMin + random.nextFloat() * 120f;
        float x = groundAcuan.getPosition().x + widthAcuan + gap;
        if (kiriKanan == 1) {
            while (x < screenWidth) {
                float width = widthMin + random.nextFloat() * 50f;
                Ground ground = new Ground(new Vector2(x, posisiY), width, height, false);
                grounds.add(ground);
                gap = gapMin + random.nextFloat() * 120f;
                x = ground.getPosition().x + width + gap;
            }
        }

        // kiri
        if (kiriKanan == 0) {
            gap = tambahKiri + gapMin + random.nextFloat() * 120f;
            x = groundAcuan.getPosition().x - gap;
            while (x > 0) {
                float width = widthMin + random.nextFloat() * 50f;
                Ground ground = new Ground(new Vector2(x - width, posisiY), width, height, false);
                grounds.add(ground);
                gap = gapMin + random.nextFloat() * 120f;
                x = ground.getPosition().x - gap;
            }
        }
    }

    public void render(ShapeRenderer shapeRenderer) {
        for (Ground g : grounds) {
            g.render(shapeRenderer);
        }
    }

    public float getPosisiAcuanSelanjutnya() {
        int selanjutnya = random.nextInt(grounds.size());
        return grounds.get(selanjutnya).getPosition().x;
    }

    public void setActive(boolean active) {
        this.active = active;
    }
}
