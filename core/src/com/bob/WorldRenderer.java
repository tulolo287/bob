package com.bob;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.viewport.FitViewport;

import aurelienribon.tweenengine.BaseTween;
import aurelienribon.tweenengine.Tween;
import aurelienribon.tweenengine.TweenCallback;
import aurelienribon.tweenengine.TweenManager;
import aurelienribon.tweenengine.equations.Sine;

public class WorldRenderer {
    private World world;
    private OrthographicCamera cam;
    private FitViewport viewport;

    private static float CAMERA_WIDTH = 10f;
    private static float CAMERA_HEIGHT = 7f;

    ShapeRenderer debugRenderer = new ShapeRenderer();

    private Texture blockTexture;
    private Texture bobTexture;
    private Texture grassTexture;

    private Sprite grassSprite;

    private Sprite test;

    private float xFactor;
    private float yFactor;
    private float xSpeed = 0;

    private Texture treesBg;
    private Texture cloudsBg;

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

    private TweenManager tweenManager;

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
        test = new Sprite();
        tweenManager = new TweenManager();
        loadTextures();
    }

    private void loadTextures() {
        bobTexture = new Texture(Gdx.files.internal("images/hobbit/Hobbit - Idle1.png"));
        blockTexture = new Texture(Gdx.files.internal("images/hobbit/ground2.png"));
        grassTexture = new Texture(Gdx.files.internal("images/hobbit/grass.png"));


        treesBg = new Texture(Gdx.files.internal("images/bg1.png"));
        cloudsBg = new Texture(Gdx.files.internal("images/bg2.png"));

        grassSprite = new Sprite(grassTexture);

        Tween.registerAccessor(Sprite.class, new SpriteAccessor());
        Tween.call(windCallback).start(tweenManager);


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

    private final TweenCallback windCallback = new TweenCallback() {
        @Override
        public void onEvent(int i, BaseTween<?> baseTween) {
            float d = MathUtils.random() * 0.5f + 0.5f;
            float t = -0.5f * grassSprite.getHeight();

            Tween.to(grassSprite, SpriteAccessor.SKEW_X2X3, d)
                    .target(t, t)
                    .ease(Sine.INOUT)
                    .repeatYoyo(1, 0)
                    .setCallback(windCallback)
                    .start(tweenManager);
        }
    };

    public void render() {

        spriteBatch.setProjectionMatrix(cam.combined);
        tweenManager.update(Gdx.graphics.getDeltaTime());

        spriteBatch.begin();
        drawParallaxBg();
        drawBob();
        drawBlocks();


        drawUI();

        spriteBatch.end();

       // drawCollisionBlocks();

        if (debug) {
            drawDebug();
        }

    }

    private void drawParallaxBg() {

        Array<Texture> textures = new Array<>();
        textures.add(cloudsBg);
        textures.add(treesBg);

        int xOffset = 0;
        xSpeed += 0.5f;
        for (Texture texture : textures) {
            if (texture == treesBg) {
                xFactor = 50f;
                yFactor = -10f;
                //xSpeed = 0;
                xOffset = (int) (cam.position.x * xFactor);
            } else if (texture == cloudsBg){
                //xSpeed = 1;

                xFactor = 5f;
                yFactor = -2f;
                xOffset = (int) (cam.position.x * xFactor + xSpeed);
            }

            //int xOffset = (int) (cam.position.x * xFactor + xSpeed);
            int yOffset = (int) (cam.position.y * yFactor);

            texture.setWrap(Texture.TextureWrap.Repeat, Texture.TextureWrap.ClampToEdge);

            TextureRegion region = new TextureRegion(texture);

            region.setRegionX(xOffset % texture.getWidth());
            region.setRegionY(yOffset % texture.getHeight());

            region.setRegionWidth((int) (texture.getWidth()));
            region.setRegionHeight((int) (texture.getHeight()));

            spriteBatch.setProjectionMatrix(cam.combined);
            //spriteBatch.draw(region, cam.position.x - cam.viewportWidth/2, cam.position.y - cam.viewportHeight/2);
            spriteBatch.draw(region, cam.position.x - cam.viewportWidth/2, cam.position.y - cam.viewportHeight/2 , cam.viewportWidth, cam.viewportHeight);
           // spriteBatch.draw(cloudsBg, 0, 0 , cam.viewportWidth, cam.viewportHeight);

        }



    }

    private void drawUI() {
        spriteBatch.end();

        Rectangle buttonLeft = new Rectangle(0, 0, 100, 50);
        ShapeRenderer shapeRenderer = new ShapeRenderer();
        //shapeRenderer.setProjectionMatrix(spriteBatch.getProjectionMatrix());
        shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
        shapeRenderer.setColor(new Color(0, 0, 1, 1.5f));
        //shapeRenderer.rect(buttonLeft);
        shapeRenderer.rect(0, 0, 300, 170);
        shapeRenderer.rect(400, 0, 300, 170);
        shapeRenderer.end();
        spriteBatch.begin();
       /* if (Gdx.input.isTouched()) {
            Rectangle touch = new Rectangle()
        }*/
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
        cam.position.y = bob.position.y + 2.5f;
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
            //spriteBatch.draw(grassSprite, block.getPosition().x, block.getPosition().y + Block.SIZE * 0.9f, Block.SIZE, Block.SIZE * 0.3f);
            grassSprite.setPosition(block.getPosition().x, block.getPosition().y + Block.SIZE * 0.9f);
            grassSprite.setSize(Block.SIZE, Block.SIZE * 0.3f);
            grassSprite.draw(spriteBatch);

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
