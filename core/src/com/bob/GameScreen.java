package com.bob;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;

public class GameScreen implements Screen {
    World world;
    WorldRenderer renderer;
    @Override
    public void show() {
        world = new World();
<<<<<<< HEAD
        renderer = new WorldRenderer(world, false);
=======
        renderer = new WorldRenderer(world);
>>>>>>> 518773d (init bob)
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0.1f, 0.1f, 0.1f, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        renderer.render();
    }

    @Override
    public void resize(int width, int height) {
<<<<<<< HEAD
        renderer.setSize(width, height);
=======

>>>>>>> 518773d (init bob)
    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void hide() {

    }

    @Override
    public void dispose() {

    }
}
