package com.bob;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;

public class GameScreen implements Screen, InputProcessor {
    World world;
    WorldRenderer renderer;
    BobController controller;

    private int width, height;

    @Override
    public void show() {
        Gdx.input.setInputProcessor(this);
        world = new World();
        controller = new BobController(world);
        renderer = new WorldRenderer(world, false);
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0.1f, 0.1f, 0.1f, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        //controller.checkCollisionWithBlocks(delta);
        controller.update(delta);
        renderer.render();
    }

    @Override
    public void resize(int width, int height) {
        renderer.setSize(width, height);
        this.width = width;
        this.height = height;
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

    @Override
    public boolean keyDown(int keycode) {
        if (keycode == Input.Keys.LEFT) {
            controller.leftPressed();
        }
        if (keycode == Input.Keys.RIGHT) {
            controller.rightPressed();
        }
        if (keycode == Input.Keys.UP) {
            controller.jumpPressed();
        }
        if (keycode == Input.Keys.SPACE) {
            controller.firePressed();
        }
        if (keycode == Input.Keys.D) {
            renderer.setDebug();
        }
        return true;
    }

    @Override
    public boolean keyUp(int keycode) {
        if (keycode == Input.Keys.LEFT) {
            controller.leftReleased();
        }
        if (keycode == Input.Keys.RIGHT) {
            controller.rightReleased();
        }
        if (keycode == Input.Keys.UP) {
            controller.jumpReleased();
        }
        if (keycode == Input.Keys.SPACE) {
            controller.fireReleased();
        }
        return true;
    }

    @Override
    public boolean keyTyped(char character) {
        return false;
    }

    @Override
    public boolean touchDown(int screenX, int screenY, int pointer, int button) {
        if (screenX < width / 2 && screenY < height / 2) {
            controller.firePressed();
        }
        if (screenX > 0 && screenX < 300 && screenY > height - 170 && screenY < height) {
            controller.leftPressed();
        }
        if (screenX > 400 && screenX < 700 && screenY > height - 170 && screenY < height) {
            controller.rightPressed();
        }
        /*if (screenX > width / 2 && screenY > height / 2) {
            controller.rightPressed();
        }*/
        return true;
    }

    @Override
    public boolean touchUp(int screenX, int screenY, int pointer, int button) {
        if (screenX > 0 && screenX < 300 && screenY > height - 170 && screenY < height) {
            controller.leftReleased();
        }
        if (screenX > 400 && screenX < 700 && screenY > height - 170 && screenY < height) {
            controller.rightReleased();
        }
        if (screenX < width / 2 && screenY < height / 2) {
            controller.fireReleased();
        }
        /*if (screenX < width / 2 && screenY > height / 2) {
            controller.leftReleased();
        }
        if (screenX > width / 2 && screenY > height / 2) {
            controller.rightReleased();
        }*/
        return true;
    }

    @Override
    public boolean touchDragged(int screenX, int screenY, int pointer) {
        if (screenX > 800 && screenY < height / 2 && !controller.jumpingPressed) {
            controller.jumpPressed();
            controller.jumpingPressed = true;
        } else {
            controller.jumpReleased();
            controller.jumpingPressed = false;
        }
        return false;
    }

    @Override
    public boolean mouseMoved(int screenX, int screenY) {
        return false;
    }

    @Override
    public boolean scrolled(float amountX, float amountY) {
        return false;
    }
}
