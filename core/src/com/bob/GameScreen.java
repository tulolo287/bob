package com.bob;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.GL30;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.glutils.FrameBuffer;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapRenderer;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.utils.viewport.FitViewport;

import jdk.internal.vm.compiler.word.WordFactory;

public class GameScreen implements Screen, InputProcessor {
    // world;
    WorldRenderer renderer;
    BobController controller;

    private int width, height;
    public final static int WIDTH = Gdx.graphics.getWidth();
    public final static int HEIGHT = Gdx.graphics.getHeight();
    public final static float SCALE = 0.3f;





    @Override
    public void show() {
        //frameBuffer = new FrameBuffer(Pixmap.Format.RGBA8888, Gdx.graphics.getWidth(), Gdx.graphics.getHeight(), false);

        //viewport = new FitViewport(width, height);
        Gdx.input.setInputProcessor(this);

        //world = new World();
        renderer = new WorldRenderer(false);
        controller = new BobController(renderer);

    }

    @Override
    public void render(float delta) {

        Gdx.gl.glClearColor(0, 0, 0.3f, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);



        //controller.checkCollisionWithBlocks(delta);

        controller.update(delta);
        renderer.render();
        //frameBuffer.end();
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
        renderer.dispose();
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
        //System.out.println(screenX);
        //System.out.println(screenY);
        //renderer.shapeRenderer.translate(screenX, screenY, 0f);
        if (screenX < width / 2 && screenY < height / 2) {
            controller.firePressed();
        }



        /*if (screenX < width / 2 && screenY < height / 2) {
            controller.firePressed();
        }
        if (screenX > 0 && screenX < 300 && screenY > height - 170 && screenY < height) {
            controller.leftPressed();
        }
        if (screenX > 400 && screenX < 700 && screenY > height - 170 && screenY < height) {
            controller.rightPressed();
        }*/

        /*if (screenX > width / 2 && screenY > height / 2) {
            controller.rightPressed();
        }*/
        return false;
    }

    @Override
    public boolean touchUp(int screenX, int screenY, int pointer, int button) {
        /*if (renderer.padPos.x > 280 && renderer.padPos.x < 320) {
            controller.leftReleased();
            controller.rightReleased();
        }*/
        controller.leftReleased();
        controller.rightReleased();
        controller.jumpReleased();
        renderer.padPos.x = 300;
        renderer.padPos.y = 300;
        /*if (renderer.padPos.x > 300) {
            controller.rightReleased();
        }*/

       /* if (screenX > 0 && screenX < 300 && screenY > height - 170 && screenY < height) {
            controller.leftReleased();
        }
        if (screenX > 400 && screenX < 700 && screenY > height - 170 && screenY < height) {
            controller.rightReleased();
        }*/
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
        System.out.println(renderer.padPos.y);
        if (renderer.padPos.x < 250) {
            controller.leftPressed();
        } else if (renderer.padPos.x > 350) {
            controller.rightPressed();
        } else if (renderer.padPos.x > 250 && renderer.padPos.x < 350) {
            controller.leftReleased();
            controller.rightReleased();
            renderer.padPos.x = 300;
            //renderer.padPos.y = 300;
        }
        //System.out.println(renderer.padPos.y);
        if (renderer.padPos.y > 350 && renderer.padPos.y < 500) {
            controller.jumpPressed();
        } else if (renderer.padPos.y > 0 && renderer.padPos.y < 350) {
            controller.jumpReleased();
            //renderer.padPos.x = 300;
            renderer.padPos.y = 300;
        }

       /* if (screenX > 800 && screenY < height / 2 && !controller.jumpingPressed) {
            controller.jumpPressed();
            controller.jumpingPressed = true;
        } else {
            controller.jumpReleased();
            controller.jumpingPressed = false;
        }*/
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
