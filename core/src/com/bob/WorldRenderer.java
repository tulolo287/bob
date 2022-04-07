package com.bob;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.utils.viewport.FitViewport;

public class WorldRenderer {
    private World world;
    private OrthographicCamera cam;
    private FitViewport viewport;

    private static float CAMERA_WIDTH = 10f;
    private static float CAMERA_HEIGHT = 8f;

    ShapeRenderer debugRenderer = new ShapeRenderer();

    private Texture blockTexture;
    private Texture bobTexture;

    private SpriteBatch spriteBatch;
    private boolean debug = false;
    private int width;
    private int height;
    private float ppuX;
    private float ppuY;

    private static final float RUNNING_FRAME_DURATION = 0.2f;

    private TextureRegion bobIdle;
    private TextureRegion bobFrame;


    public Animation<TextureRegion> bobIdleRightAnimation;
    public Animation<TextureRegion> bobIdleLeftAnimation;
    public Animation<TextureRegion> bobWalkRightAnimation;
    public Animation<TextureRegion> bobWalkLeftAnimation;
    public Animation<TextureRegion> bobFireRightAnimation;
    public Animation<TextureRegion> bobFireLeftAnimation;
    public Animation<TextureRegion> bobJumpRightAnimation;
    public Animation<TextureRegion> bobJumpLeftAnimation;

    public void setDebug() {
        this.debug = !debug;
    }

    public void setSize(int w, int h) {
        this.width = w;
        this.height = h;
        ppuX = (float)width / CAMERA_WIDTH;
        ppuY = (float)height / CAMERA_HEIGHT;
    }

    public WorldRenderer(World world, Boolean debug) {
        this.world = world;

        this.cam = new OrthographicCamera(CAMERA_WIDTH, CAMERA_HEIGHT);

        this.cam.setToOrtho(false, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        this.viewport = new FitViewport(1280, 720);
        //this.cam.position.set(CAMERA_WIDTH / 2f, CAMERA_HEIGHT / 2f, 0);
        //this.cam.update();
        this.debug = debug;
        spriteBatch = new SpriteBatch();
        loadTextures();
    }

    private void loadTextures() {
        bobTexture = new Texture(Gdx.files.internal("images/hobbit/Hobbit - Idle1.png"));
        blockTexture = new Texture(Gdx.files.internal("images/hobbit/ground2.png"));

        TextureAtlas atlas = new TextureAtlas(Gdx.files.internal("images/textures2/textures.atlas"));

       // b///obIdleRight = atlas.findRegion("Hobbit - Idle1");

        TextureRegion[] bobIdleRight = new TextureRegion[3];
        for (int i = 0; i < 3; i++) {
            bobIdleRight[i] = atlas.findRegion("Hobbit - Idle" + (i + 1));
        }
        bobIdleRightAnimation = new Animation<TextureRegion>(0.27f, bobIdleRight);


        TextureRegion[] bobIdleLeft = new TextureRegion[3];
        for (int i = 0; i < 3; i++) {
            bobIdleLeft[i] = new TextureRegion(bobIdleRight[i]);
            bobIdleLeft[i].flip(true, false);
        }
        bobIdleLeftAnimation = new Animation<>(0.27f, bobIdleLeft);

        TextureRegion[] bobWalkRight = new TextureRegion[8];
        for (int i = 0; i < 8; i++) {
            bobWalkRight[i] = atlas.findRegion("Hobbit - run" + (i + 1));
        }
        bobWalkRightAnimation = new Animation<>(0.07f, bobWalkRight);

        TextureRegion[] bobWalkLeft = new TextureRegion[8];
        for (int i = 0; i < 8; i++) {
            bobWalkLeft[i] = new TextureRegion(bobWalkRight[i]);
            bobWalkLeft[i].flip(true, false);
        }
        bobWalkLeftAnimation = new Animation<>(0.07f, bobWalkLeft);

        TextureRegion[] bobFireRight = new TextureRegion[17];
        for (int i = 0; i < 17; i++) {
            bobFireRight[i] = atlas.findRegion("Hobbit - attack" + (i + 1));
        }
        bobFireRightAnimation = new Animation<>(0.07f, bobFireRight);

        TextureRegion[] bobFireLeft = new TextureRegion[17];
        for (int i = 0; i < 17; i++) {
            bobFireLeft[i] = new TextureRegion(bobFireRight[i]);
            bobFireLeft[i].flip(true, false);
        }
        bobFireLeftAnimation = new Animation<>(0.07f, bobFireLeft);

        TextureRegion[] bobJumpRight = new TextureRegion[10];
        for (int i = 0; i < 10; i++) {
            bobJumpRight[i] = atlas.findRegion("Hobbit - jumpt" + (i + 1));
        }
        bobJumpRightAnimation = new Animation<>(0.07f, bobJumpRight);

        TextureRegion[] bobJumpLeft = new TextureRegion[10];
        for (int i = 0; i < 10; i++) {
            bobJumpLeft[i] = new TextureRegion(bobJumpRight[i]);
            bobJumpLeft[i].flip(true, false);
        }
        bobJumpLeftAnimation = new Animation<>(0.07f, bobJumpLeft);


    }

    public void render() {

        spriteBatch.setProjectionMatrix(cam.combined);
        spriteBatch.begin();
        drawBlocks();
        drawBob();
        drawCollisionBlocks();
        spriteBatch.end();
        if (debug) {
            drawDebug();
        }
    }

    private void drawCollisionBlocks() {
        debugRenderer.setProjectionMatrix(cam.combined);
        debugRenderer.begin(ShapeRenderer.ShapeType.Filled);
        debugRenderer.setColor(Color.CORAL);
        debugRenderer.circle(5, 1, 23);
        for (Rectangle rect : world.getCollisionRects()) {
            debugRenderer.setColor(Color.CORAL);
           // debugRenderer.circle(rect.x, rect.y, 23, 23);
            //debugRenderer.rect(rect.x, rect.y, rect.width, rect.height);
        }
        debugRenderer.end();
    }

    private void drawBob() {

        Bob bob = world.getBob();
        BobController bobController = new BobController(world);
        cam.position.x = bob.position.x * ppuX;
        cam.update();

        System.out.println(bob.getState());

        switch (bob.getState()) {
            case IDLE:
                if (!bob.fired) {
                    bobFrame = bob.isFacingLeft() ? bobIdleLeftAnimation.getKeyFrame(bob.getStateTime(), true) : bobIdleRightAnimation.getKeyFrame(bob.getStateTime(), true);
                }
                break;
            case WALK:
                bobFrame = bob.isFacingLeft() ? bobWalkLeftAnimation.getKeyFrame(bob.getStateTime(), true) : bobWalkRightAnimation.getKeyFrame(bob.getStateTime(), true);
                break;
            case JUMP:
                if (bob.getVelocity().y == 0) {
                    bobFrame = bob.isFacingLeft() ? bobJumpLeftAnimation.getKeyFrame(bob.getStateTime(), true) : bobJumpRightAnimation.getKeyFrame(bob.getStateTime(), true);
                }
                break;
            case FIRE:
                bobFrame = bob.isFacingLeft() ? bobFireLeftAnimation.getKeyFrame(bob.getStateTime(), false) : bobFireRightAnimation.getKeyFrame(bob.getStateTime(), false);
                if (bobFireRightAnimation.isAnimationFinished(1f) || bobFireLeftAnimation.isAnimationFinished(1f)) {
                    bob.fired = false;
                }
                break;
        }

        spriteBatch.draw(bobFrame, bob.getPosition().x * ppuX, bob.getPosition().y * ppuY, bob.bounds.getWidth() / 2 * ppuX, bob.bounds.getHeight() / 2 * ppuY, bob.bounds.width * ppuX, bob.bounds.height * ppuY, 5f,5f, 0);

       // spriteBatch.draw(bobTexture, bob.getPosition().x * ppuX, bob.getPosition().y * ppuY, Bob.SIZE * ppuX, Bob.SIZE * ppuY);
    }

    private void drawBlocks() {
        for (Block block : world.getBlocks()) {
            spriteBatch.draw(blockTexture, block.getPosition().x * ppuX, block.getPosition().y * ppuY, Block.SIZE * ppuX, Block.SIZE * ppuY);
        }
    }

    private void drawDebug() {
        debugRenderer.setProjectionMatrix(cam.combined);
        debugRenderer.begin(ShapeRenderer.ShapeType.Line);
        for (Block block : world.getBlocks()) {
            Rectangle rect = block.getBounds();
            float x1 = block.getPosition().x * ppuX;
            float y1 = block.getPosition().y * ppuY;
            debugRenderer.setColor(1, 0, 0, 1);
            debugRenderer.rect(x1, y1, rect.width * ppuX, rect.height * ppuY);
        }

        Bob bob = world.getBob();
        Rectangle rect = bob.getBounds();
        float x1 = bob.getPosition().x * ppuX;
        float y1 = bob.getPosition().y * ppuY;
        debugRenderer.setColor(0, 1, 0, 1);
        debugRenderer.rect(x1, y1, rect.width * ppuX, rect.height * ppuY);

        debugRenderer.end();
    }


}
