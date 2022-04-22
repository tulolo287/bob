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
    private static float CAMERA_HEIGHT = 7f;

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
    public Animation<TextureRegion> bobFallRightAnimation;
    public Animation<TextureRegion> bobFallLeftAnimation;

    private float fallAnimationTime = 0;
    private float jumpAnimationTime = 0;
    private float fireAnimationTime = 0;

    private boolean fallAnimationEnd = false;
    private boolean fireAnimationEnd = false;

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

        //this.cam.setToOrtho(false, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        //this.viewport = new FitViewport(1280, 720);
        this.cam.position.set(CAMERA_WIDTH / 2f, CAMERA_HEIGHT / 2f, 0);
        //this.cam.position.set(world.getBob().getPosition().x, world.getBob().getPosition().y, 0);
        this.cam.update();
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

        TextureRegion[] bobJumpRight = new TextureRegion[3];
        for (int i = 0; i < 3; i++) {
            bobJumpRight[i] = atlas.findRegion("Hobbit - jumpt" + (i + 1));
        }
        bobJumpRightAnimation = new Animation<>(0.17f, bobJumpRight);

        TextureRegion[] bobJumpLeft = new TextureRegion[3];
        for (int i = 0; i < 3; i++) {
            bobJumpLeft[i] = new TextureRegion(bobJumpRight[i]);
            bobJumpLeft[i].flip(true, false);
        }
        bobJumpLeftAnimation = new Animation<>(0.17f, bobJumpLeft);


        TextureRegion[] bobFallRight = new TextureRegion[7];
        for (int i = 0; i < 7; i++) {
            bobFallRight[i] = atlas.findRegion("Hobbit - jumpt" + (i + 4));
        }
        bobFallRightAnimation = new Animation<>(0.27f, bobFallRight);

        TextureRegion[] bobFallLeft = new TextureRegion[7];
        for (int i = 0; i < 7; i++) {
            bobFallLeft[i] = new TextureRegion(bobFallRight[i]);
            bobFallLeft[i].flip(true, false);
        }
        bobFallLeftAnimation = new Animation<>(0.27f, bobFallLeft);


    }

    public void render() {

        spriteBatch.setProjectionMatrix(cam.combined);

        spriteBatch.begin();

        drawBlocks();
        drawBob();
        spriteBatch.end();

        drawCollisionBlocks();

        if (debug) {
            drawDebug();
        }

    }

    private void drawCollisionBlocks() {
        debugRenderer.setProjectionMatrix(cam.combined);
        debugRenderer.begin(ShapeRenderer.ShapeType.Line);
        debugRenderer.setColor(Color.CORAL);
        //debugRenderer.circle(5, 1, 23);
        //debugRenderer.rect(1, 1, 10, 10);
        //System.out.println(world.getCollisionRects());
        for (Rectangle rect : world.getCollisionRects()) {
            //debugRenderer.setColor(Color.CORAL);
           // debugRenderer.circle(14, 14, 223, 3);
            debugRenderer.rect(rect.x, rect.y, rect.width, rect.height);

        }
        debugRenderer.end();
    }

    private void drawBob() {

        Bob bob = world.getBob();

        cam.position.x = bob.position.x;
        //cam.position.y = bob.position.y;
        cam.update();

        //System.out.println(bob.getState());


        if (!(bob.getVelocity().y < 0)) {
            switch (bob.getState()) {
                case IDLE:

                    bobFrame = bob.isFacingLeft() ? bobIdleLeftAnimation.getKeyFrame(bob.getStateTime(), true) : bobIdleRightAnimation.getKeyFrame(bob.getStateTime(), true);
                    fallAnimationEnd = false;
                    //jumpAnimationTime += Gdx.graphics.getDeltaTime();
                    //System.out.println(bobIdleLeftAnimation.isAnimationFinished(bob.getStateTime()));
                    break;
                case WALK:
                    fallAnimationEnd = false;
                    bobFrame = bob.isFacingLeft() ? bobWalkLeftAnimation.getKeyFrame(bob.getStateTime(), true) : bobWalkRightAnimation.getKeyFrame(bob.getStateTime(), true);
                    break;
                case JUMP:
                    //if (!(bob.getVelocity().y < 0)) {
                        //jumpAnimationTime += Gdx.graphics.getDeltaTime();
                        //if (!bobJumpRightAnimation.isAnimationFinished(jumpAnimationTime) || bobJumpRightAnimation.isAnimationFinished(jumpAnimationTime)) {
                        bobFrame = bob.isFacingLeft() ? bobJumpLeftAnimation.getKeyFrame(bob.getStateTime(), false) : bobJumpRightAnimation.getKeyFrame(bob.getStateTime(), false);
                        //jumpAnimationTime += Gdx.graphics.getDeltaTime();
                        //}
                   // }
                    break;
                case FIRE:
                    if (!fallAnimationEnd) {
                        bobFrame = bob.isFacingLeft() ? bobFireLeftAnimation.getKeyFrame(fireAnimationTime, false) : bobFireRightAnimation.getKeyFrame(fireAnimationTime, false);
                        fireAnimationTime += Gdx.graphics.getDeltaTime();
                        if (bobFireRightAnimation.isAnimationFinished(fireAnimationTime) || bobFallLeftAnimation.isAnimationFinished(fireAnimationTime)) {
                            fireAnimationEnd = true;
                            fireAnimationTime = 0;
                        }
                    }
                    break;
            }
        } if (bob.getVelocity().y < 0 && fallAnimationEnd == false) {

            bobFrame = bob.isFacingLeft() ? bobFallLeftAnimation.getKeyFrame(fallAnimationTime, false) : bobFallRightAnimation.getKeyFrame(fallAnimationTime, false);
            fallAnimationTime += Gdx.graphics.getDeltaTime();
            //fallAnimationEnd = true;
            if (bobFallLeftAnimation.isAnimationFinished(fallAnimationTime)) {
                fallAnimationEnd = true;
                fallAnimationTime = 0;
            }

        } /*else if (bob.getVelocity().y >= 0){
            bobFrame = bob.isFacingLeft() ? bobIdleLeftAnimation.getKeyFrame(bob.getStateTime(), true) : bobIdleRightAnimation.getKeyFrame(bob.getStateTime(), true);
            fallAnimationEnd = false;
        }*/
        System.out.println(1 / Gdx.graphics.getDeltaTime());



        spriteBatch.draw(bobFrame, bob.getPosition().x, bob.getPosition().y, bob.bounds.getWidth() / 2, bob.bounds.getHeight() / 2, Bob.SIZE, Bob.SIZE, 5f,5f, 0);
        //spriteBatch.draw(bobTexture, bob.getPosition().x, bob.getPosition().y, Bob.SIZE, Bob.SIZE);

         //spriteBatch.draw(bobTexture, bob.getPosition().x * ppuX, bob.getPosition().y * ppuY, Bob.SIZE * ppuX, Bob.SIZE * ppuY);
    }

    private void drawBlocks() {
        for (Block block : world.getDrawableBlocks((int) CAMERA_WIDTH, (int) CAMERA_HEIGHT)) {
            spriteBatch.draw(blockTexture, block.getPosition().x, block.getPosition().y, Block.SIZE, Block.SIZE);
        }
       /* for (Block block : world.getBlocks()) {
            spriteBatch.draw(blockTexture, block.getPosition().x * ppuX, block.getPosition().y * ppuY, Block.SIZE * ppuX, Block.SIZE * ppuY);
        }*/
    }

    private void drawDebug() {
        debugRenderer.setProjectionMatrix(cam.combined);
        debugRenderer.begin(ShapeRenderer.ShapeType.Line);
        for (int w = 0; w < 10; w++) {
            for (int h = 0; h < 7; h++) {
                Block[][] block = world.getBlocks();
                Rectangle rect = block[w][h].getBounds();
                float x1 = block[w][h].getPosition().x;
                float y1 = block[w][h].getPosition().y;
                debugRenderer.setColor(1, 0, 0, 1);
                debugRenderer.rect(x1, y1, rect.width, rect.height);
            }
        }

        Bob bob = world.getBob();
        Rectangle rect = bob.getBounds();
        float x1 = bob.getPosition().x;
        float y1 = bob.getPosition().y;
        debugRenderer.setColor(0, 0, 1, 1);
        debugRenderer.rect(x1, y1, rect.width, rect.height);

        debugRenderer.end();
    }


}
