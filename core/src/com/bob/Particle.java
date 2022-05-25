package com.bob;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.MathUtils;

import org.lwjgl.Sys;

public class Particle {
    private float posX;
    private float posY;
    private float width, height;
    private float radians;
    private float speed;
    private float dx;
    private float dy;
    public float time;

    public Particle(float posX, float posY) {
        this.posX = posX;
        this.posY = posY;
        this.time = 0;
        width = height = 0.1f;
        speed = 10;
        radians = MathUtils.random(-5 * 3.141f);
        dx = MathUtils.cos(radians) * speed;
        dy = MathUtils.sin(radians) * speed;
    }

    public void update() {
        this.posX += dx * Gdx.graphics.getDeltaTime();
        this.posY += dy * Gdx.graphics.getDeltaTime();
        this.time++;
    }

    public void draw(ShapeRenderer sr) {
        //sr.setProjectionMatrix(WorldRenderer.cam.projection);
        //System.out.println(this.posX);
        //System.out.println(bobBody.getPosition().y);
        //sr.setColor(Color.BLACK);
        sr.begin(ShapeRenderer.ShapeType.Filled);
        sr.circle(this.posX , this.posY - 7f, .5f);
        sr.end();
    }
}
