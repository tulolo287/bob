package com.bob;


import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.ParticleEffect;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.FrameBuffer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.objects.RectangleMapObject;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapRenderer;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthoCachedTiledMapRenderer;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.Box2D;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.viewport.FitViewport;

import java.util.ArrayList;

import aurelienribon.tweenengine.BaseTween;
import aurelienribon.tweenengine.Tween;
import aurelienribon.tweenengine.TweenCallback;
import aurelienribon.tweenengine.TweenManager;
import aurelienribon.tweenengine.equations.Sine;
import com.badlogic.gdx.physics.box2d.World;

import org.lwjgl.Sys;

public class WorldRenderer {
    private World world;
    static OrthographicCamera cam;
    static OrthographicCamera hudCam;
    static FitViewport viewport;

    private static float CAMERA_WIDTH = 10f;
    private static float CAMERA_HEIGHT = 7f;

    static final float STEP_TIME = 1 / 60f;
    static final int VELOCITY_ITERATIONS = 6;
    static final int POSITION_ITERATIONS = 2;
    static final float SCALE = 0.05f;
    static final int COUNT = 10;

    ShapeRenderer debugRenderer = new ShapeRenderer();

    private Texture blockTexture;
    private Texture bobTexture;
    private Texture grassTexture;
    private Texture playerTexture;

    private Sprite grassSprite;
    private Sprite player;

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
    private float vX3 = 0;
    private float vX2 = 0;
    private float wind;

    private float scX = 1f;
    private float scY = 1f;

    private Bob bob;
    private Body bobBody;
    private Body collisionBody;

    private Box2DDebugRenderer box2DDebugRenderer;

    public ShapeRenderer shapeRenderer;

    private ArrayList<Sprite> grasses;

    private float accumulator;

    private TiledMap map;
    private OrthoCachedTiledMapRenderer tiledMapRenderer;

    //private FitViewport viewport;

    private BodyDef bodyDef;
    private float aspectRatio;
    private int worldWidth;

    public Vector2 padPos;
    public float padSize;

    private Array<Particle> particles;

    private ParticleEffect effect;

    private ShapeRenderer particleShapeRenderer;

    private int particleCount = 0;

    public float touchCircleHeight;

    public void setDebug() {
        this.debug = !debug;
    }

    public void setSize(int w, int h) {
        this.width = w;
        this.height = h;
        ppuX = (float)width / CAMERA_WIDTH;
        ppuY = (float)height / CAMERA_HEIGHT;

        viewport.update(w, h);
        //cam.position.set(cam.viewportWidth / 2, cam.viewportHeight / 2, 0);

    }

    public WorldRenderer(Boolean debug) {


        particleShapeRenderer = new ShapeRenderer();
        TextureAtlas particleAtlas = new TextureAtlas();
        particleAtlas.getTextures();
        //effect = new ParticleEffect();
        //effect.load(Gdx.files.internal("dust.p"), Gdx.files.internal("./"));
        //effect.setEmittersCleanUpBlendFunction(false);





        particles = new Array<>();
        Box2D.init();
        accumulator = 0;
        //this.world = world;
        //bob = world.getBob();
        bob = new Bob();
        world = new World(new Vector2(0f, -10f), true);
        world.setContactListener(new WorldContactListener());
        box2DDebugRenderer = new Box2DDebugRenderer();

        aspectRatio = (float) Gdx.graphics.getWidth() / Gdx.graphics.getHeight();
        worldWidth = 300;

        cam = new OrthographicCamera();
        //cam.setToOrtho();
        cam.position.set(10,  100, 0);
        cam.update();

        hudCam = new OrthographicCamera(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());

        viewport = new FitViewport(worldWidth, worldWidth / aspectRatio, cam);
        viewport.apply();


        tweenManager = new TweenManager();
        Tween.registerAccessor(Sprite.class, new SpriteAccessor());
        Tween.call(windCallback).start(tweenManager);

        //this.cam.setToOrtho(false, 20, 170);
        //this.viewport = new FitViewport(1280, 720);
        //cam.position.set(CAMERA_WIDTH / 2f, CAMERA_HEIGHT / 2f, 0);
        //cam.position.set(world.getBob().getPosition().x, world.getBob().getPosition().y, 0);
        //cam.update();
        this.debug = debug;

        spriteBatch = new SpriteBatch();

        spriteBatch.enableBlending();
        Gdx.gl.glBlendFuncSeparate(GL20.GL_BLEND_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA, GL20.GL_ONE, GL20.GL_ONE_MINUS_SRC_ALPHA);
        test = new Sprite();

        shapeRenderer = new ShapeRenderer();
        touchCircleHeight = GameScreen.HEIGHT / 5;
        padSize = touchCircleHeight / 3;
        padPos = new Vector2(touchCircleHeight, touchCircleHeight);

        loadTextures();
        createWorld();
        createBody();
        //createParticles();


    }


    public OrthographicCamera getCam() {
        return cam;
    }

    private void createParticles() {
      /*  effect.setPosition(bobBody.getPosition().x * App.PPM, bobBody.getPosition().y * App.PPM);
        //effect.scaleEffect(0.8f);
        effect.start();*/




        for (int i = 0; i < 5; i++) {
            particles.add(new Particle(bobBody.getPosition().x * App.PPM, bobBody.getPosition().y * App.PPM));
        }

    }

    private Body createBody() {
        bodyDef = new BodyDef();
        bodyDef.type = BodyDef.BodyType.DynamicBody;
        bodyDef.position.set(.2f, 2f);
        bobBody = world.createBody(bodyDef);

        PolygonShape shape = new PolygonShape();
        shape.setAsBox(.04f, .07f);

        FixtureDef fixtureDef = new FixtureDef();
        fixtureDef.shape = shape;
        Fixture fixture = bobBody.createFixture(fixtureDef);

        shape = new PolygonShape();
        shape.setAsBox(.03f, .01f, new Vector2(0, -.07f), 0);


        fixtureDef = new FixtureDef();
        fixtureDef.shape = shape;

        fixtureDef.filter.categoryBits = App.PLAYER;
        fixtureDef.filter.maskBits = App.GROUND;
        fixtureDef.isSensor = true;
        fixture = bobBody.createFixture(fixtureDef);
        fixture.setUserData("feet");


        shape.dispose();
        return bobBody;
    }

    private void createWorld() {
        //Sprite grass = (Sprite) map.getLayers().get("grass");
        for (MapObject object : map.getLayers().get("collision").getObjects().getByType(RectangleMapObject.class)) {
            Rectangle rectangle = ((RectangleMapObject) object).getRectangle();
            bodyDef = new BodyDef();
            bodyDef.type = BodyDef.BodyType.StaticBody;
            bodyDef.position.set(rectangle.x / App.PPM + rectangle.getWidth() / 2 / App.PPM, rectangle.y / App.PPM + rectangle.getHeight() / 2 / App.PPM);
            collisionBody = world.createBody(bodyDef);

            PolygonShape shape = new PolygonShape();
            shape.setAsBox(rectangle.getWidth() / 2 / App.PPM, rectangle.getHeight() / 2 / App.PPM);

            FixtureDef fixtureDef = new FixtureDef();
            fixtureDef.shape = shape;
            fixtureDef.filter.categoryBits = App.GROUND;
            fixtureDef.filter.maskBits = App.PLAYER;


            Fixture fixture = collisionBody.createFixture(fixtureDef);

            collisionBody.setUserData("ground");

            shape.dispose();
        }

        for (MapObject object : map.getLayers().get("grassObj").getObjects().getByType(RectangleMapObject.class)) {
            Rectangle rectangle = ((RectangleMapObject) object).getRectangle();
            bodyDef = new BodyDef();
            bodyDef.type = BodyDef.BodyType.StaticBody;
            bodyDef.position.set(rectangle.x / App.PPM + rectangle.getWidth() / 2 / App.PPM, rectangle.y / App.PPM + rectangle.getHeight() / 2 / App.PPM);
            collisionBody = world.createBody(bodyDef);

            PolygonShape shape = new PolygonShape();
            shape.setAsBox(rectangle.getWidth() / 2 / App.PPM, rectangle.getHeight() / 2 / App.PPM);

            FixtureDef fixtureDef = new FixtureDef();
            fixtureDef.shape = shape;


            fixtureDef.isSensor = true;
            Fixture fixture = collisionBody.createFixture(fixtureDef);
            collisionBody.setUserData("grass");

            shape.dispose();
        }

        //return bobBody;
    }

    public Bob getBob() {
        return bob;
    }
    public Body getBobBody() {
        return bobBody;
    }

    private void loadTextures() {
        bobTexture = new Texture(Gdx.files.internal("images/hobbit/Hobbit - Idle1.png"));
        playerTexture = new Texture(Gdx.files.internal("images/hobbit/player.png"));
        blockTexture = new Texture(Gdx.files.internal("images/hobbit/ground2.png"));
        grassTexture = new Texture(Gdx.files.internal("images/hobbit/grass.png"));
        map = new TmxMapLoader().load("maps/level1.tmx");
        tiledMapRenderer = new OrthoCachedTiledMapRenderer(map);
        tiledMapRenderer.setBlending(true);

        grassSprite = new Sprite(grassTexture);
        player = new Sprite(playerTexture);

        grasses = new ArrayList<Sprite>();

        treesBg = new Texture(Gdx.files.internal("images/bg1.png"));
        cloudsBg = new Texture(Gdx.files.internal("images/bg2.png"));

        //grassSprite = new Sprite(grassTexture);




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
        //particleShapeRenderer.setProjectionMatrix(cam.combined.cpy().scl(App.PPM));
        particleShapeRenderer.setProjectionMatrix(cam.combined);

        windBlowing();
        if (Gdx.input.isTouched() && BobController.padTouched) {
            Vector2 touchPos = new Vector2(Gdx.input.getX(), Gdx.input.getY());
            //System.out.println(touchPos);
           // System.out.println(viewport.unproject(touchPos));

           // if (touchPos.x + 100 < 600 && touchPos.x - 100 > 0 && Gdx.graphics.getHeight() - touchPos.y + 100 < 600 && Gdx.graphics.getHeight() - touchPos.y - 100 > 0) {
                padPos.y = Gdx.graphics.getHeight() - touchPos.y > 500 ? 500 : Math.max(Gdx.graphics.getHeight() - touchPos.y, 100);
                padPos.x = touchPos.x > 500 ? 500 : Math.max(touchPos.x, 100);
          //  }

            //padPos.y = touchPos.y;
            //shapeRenderer.translate(touchPos.x, touchPos.y, 0);
            //createParticles();
            //drawParticles();


        }
        if (bobBody.getLinearVelocity().y < 0 && padPos.y > 450) {
            BobController.jumpingPressed = true;
        } else if (padPos.y < 370) {
            BobController.jumpingPressed = false;
        }


        tiledMapRenderer.setView(cam);


        cam.update();
        stepWorld();




        spriteBatch.begin();
        //effect.draw(spriteBatch, Gdx.graphics.getDeltaTime());

        drawParallaxBg();
        spriteBatch.end();

        tiledMapRenderer.render();

        spriteBatch.begin();


        //drawBlocks();
        //drawGrass();

        drawGamePlay();
        drawBob();
        drawParticles();


        //drawUI();

//effect.draw(spriteBatch, Gdx.graphics.getDeltaTime());
        spriteBatch.end();

       /* if (effect.isComplete()) {
           // effect.reset();
        }*/
        //box2DDebugRenderer.render(world, cam.combined.cpy().scl(App.PPM));

        //tweenManager.update(Gdx.graphics.getDeltaTime());
       // drawCollisionBlocks();

        if (debug) {
            //drawDebug();
        }

    }

    private void drawParticles() {
        spriteBatch.end();
        for (Particle particle : particles) {
            particle.update();
            if (particle.time >= 10) {
                particles.pop();
            }
            particle.draw(particleShapeRenderer);

        }
        spriteBatch.begin();
    }

    private void windBlowing() {
        wind = MathUtils.random(-0.005f, 0.005f);
        if (vX3 >= 0.5f || vX3 <= -0.5f) {
            wind = -wind;
            //vX3 = 0;
        }
        vX3 += wind;


    }

    private void stepWorld() {
        float dt = Gdx.graphics.getDeltaTime();
        accumulator += Math.min(dt, 0.25f);
        if (accumulator >= STEP_TIME) {
            accumulator -= STEP_TIME;
            world.step(STEP_TIME, VELOCITY_ITERATIONS, POSITION_ITERATIONS);
        }
    }

    private void drawGamePlay() {
        spriteBatch.end();
        //shapeRenderer.setProjectionMatrix(cam.projection);
        Gdx.gl.glEnable(GL20.GL_BLEND);
        Gdx.gl.glBlendFunc(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);
        shapeRenderer.begin(ShapeRenderer.ShapeType.Line);
        shapeRenderer.setColor(new Color(1f, 1f, 1f, 0.3f));
        shapeRenderer.circle(touchCircleHeight, touchCircleHeight, touchCircleHeight);
        shapeRenderer.end();
        shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
        shapeRenderer.circle(padPos.x, padPos.y, padSize);
        shapeRenderer.end();
        shapeRenderer.setColor(new Color(1f, 0, 0, 0.3f));
        shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
        shapeRenderer.circle(2100, 150, 100);
        shapeRenderer.end();
        shapeRenderer.setColor(new Color(0, 0, 1f, 0.3f));
        shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
        shapeRenderer.circle(2380, 350, 100);
        shapeRenderer.end();
        spriteBatch.begin();
    }

    private void drawGrass() {

        for (Sprite grass : grasses) {
            grass.draw(spriteBatch);
        }
    }

    private void drawParallaxBg() {

        Array<Texture> textures = new Array<>();
        textures.add(cloudsBg);
        //textures.add(treesBg);

        int xOffset = 0;
        xSpeed += 0.2f;
        for (Texture texture : textures) {
            if (texture == treesBg) {
                xFactor = 2f;
                yFactor = -2f;
                //xSpeed = 0;
                xOffset = (int) (cam.position.x * xFactor);
            } else if (texture == cloudsBg){
                //xSpeed = 1;

                xFactor = 1f;
                yFactor = 0.1f;
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

            //region.setV2(0.4f);


           /* float[] vs = region.getVertices();
            vs[SpriteBatch.X3] += vX3;
            vs[SpriteBatch.X2] += vX3;*/

            //spriteBatch.setProjectionMatrix(cam.combined);
            //spriteBatch.draw(region, cam.position.x - cam.viewportWidth/2, cam.position.y - cam.viewportHeight/2);
            spriteBatch.draw(region, cam.position.x / App.PPM - cam.viewportWidth / 2 / App.PPM, cam.position.y / App.PPM - cam.viewportHeight / 2 / App.PPM, 0, 0, cam.viewportWidth / App.PPM, cam.viewportHeight / App.PPM, 1f, 1f, 0);
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

   /* private void drawCollisionBlocks() {
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
    }*/

    private void drawBob() {

        //shapeRenderer.setProjectionMatrix(cam.projection);
        cam.position.set(cam.viewportWidth / 2,  cam.viewportHeight / 2, 0);
     //if (bobBody.getPosition().x > cam.viewportWidth / 2){
        //cam.unproject(cam.position);
            cam.position.x = bobBody.getPosition().x * App.PPM;
       // }

        //cam.position.y = bob.position.y + 2.5f;
        //cam.position.y = bob.position.y;
        cam.update();


        //System.out.println(bob.getState());
        particleCount++;

        if (!(bobBody.getLinearVelocity().y < 0)) {
            switch (bob.getState()) {
                case IDLE:

                    bobFrame = bob.isFacingLeft() ? bobIdleLeftAnimation.getKeyFrame(bob.getStateTime(), true) : bobIdleRightAnimation.getKeyFrame(bob.getStateTime(), true);
                    fallAnimationEnd = false;
                    //jumpAnimationTime += Gdx.graphics.getDeltaTime();
                    //System.out.println(bobIdleLeftAnimation.isAnimationFinished(bob.getStateTime()));
                    break;
                case WALK:

                    if (bobBody.getLinearVelocity().x > .5f && WorldContactListener.grounded && particleCount >= 10 || bobBody.getLinearVelocity().x < -.5f && WorldContactListener.grounded && particleCount >= 20) {

                        createParticles();

                        particleCount = 0;
                    }

                    fallAnimationEnd = false;
                    bobFrame = bob.isFacingLeft() ? bobWalkLeftAnimation.getKeyFrame(bob.getStateTime(), true) : bobWalkRightAnimation.getKeyFrame(bob.getStateTime(), true);
                    //bobBody.applyLinearImpulse(new Vector2(1f, 0f),new Vector2(1f, 0f), true);
                    break;
                case JUMP:
                    //if (!(bob.getVelocity().y < 0)) {
                        //jumpAnimationTime += Gdx.graphics.getDeltaTime();
                        //if (!bobJumpRightAnimation.isAnimationFinished(jumpAnimationTime) || bobJumpRightAnimation.isAnimationFinished(jumpAnimationTime)) {
                        bobFrame = bob.isFacingLeft() ? bobJumpLeftAnimation.getKeyFrame(bob.getStateTime(), false) : bobJumpRightAnimation.getKeyFrame(bob.getStateTime(), false);

                    if (WorldContactListener.grounded && particleCount >= 20) {
                        createParticles();
                        particleCount = 0;
                    }

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
        } if (bobBody.getLinearVelocity().y < 0 && fallAnimationEnd == false) {

            bobFrame = bob.isFacingLeft() ? bobFallLeftAnimation.getKeyFrame(fallAnimationTime, false) : bobFallRightAnimation.getKeyFrame(fallAnimationTime, false);
            fallAnimationTime += Gdx.graphics.getDeltaTime();
            //fallAnimationEnd = true;
            if (bobFallLeftAnimation.isAnimationFinished(fallAnimationTime)) {
                fallAnimationEnd = true;
                fallAnimationTime = 0;
            }

        }



        /*else if (bob.getVelocity().y >= 0){
            bobFrame = bob.isFacingLeft() ? bobIdleLeftAnimation.getKeyFrame(bob.getStateTime(), true) : bobIdleRightAnimation.getKeyFrame(bob.getStateTime(), true);
            fallAnimationEnd = false;
        }*/
        //System.out.println(bob.getState());

        //spriteBatch.draw(player, bobBody.getPosition().x - player.getWidth() / 2, bobBody.getPosition().y - player.getHeight() / 2, 0, 0, player.getWidth(), player.getHeight(), 1f,1f, 0);

        //spriteBatch.draw(bobFrame, bobBody.getPosition().x - bobTexture.getWidth() / 2, bobBody.getPosition().y - bobTexture.getHeight() / 2, bobTexture.getWidth() / 2, bobTexture.getHeight() / 2, bobTexture.getWidth(), bobTexture.getHeight(), 1f,1f, 0);

        spriteBatch.draw(bobFrame, bobBody.getPosition().x * App.PPM - bobTexture.getWidth() / 2,  bobBody.getPosition().y * App.PPM - bobTexture.getHeight() / 2, 0, 0, bobTexture.getWidth(), bobTexture.getHeight(), 1f,1f, 0);

        //spriteBatch.draw(bobFrame, bob.getPosition().x, bob.getPosition().y, bob.bounds.getWidth() / 2, bob.bounds.getHeight() / 2, Bob.SIZE, Bob.SIZE, 1f,1f, 0);

        //spriteBatch.draw(bobTexture, bob.getPosition().x, bob.getPosition().y, Bob.SIZE, Bob.SIZE);

         //spriteBatch.draw(bobTexture, bob.getPosition().x * ppuX, bob.getPosition().y * ppuY, Bob.SIZE * ppuX, Bob.SIZE * ppuY);
    }

    /*private void drawBlocks() {
      *//*  wind = MathUtils.random(-0.005f, 0.005f);
        vX3 += wind;
        if (vX3 >= 0.2f || vX3 <= -0.2f) {
            wind = -wind;
        }*//*

        for (Block block : world.getDrawableBlocks((int) CAMERA_WIDTH, (int) CAMERA_HEIGHT)) {



            spriteBatch.draw(blockTexture, block.getPosition().x, block.getPosition().y, Block.SIZE, Block.SIZE);
            //spriteBatch.draw(grassSprite, block.getPosition().x, block.getPosition().y + Block.SIZE * 0.9f, Block.SIZE, Block.SIZE * 0.3f);

*//*grassSprite = new Sprite(grassTexture);
            grassSprite.setPosition(block.getPosition().x, block.getPosition().y + Block.SIZE * 0.9f);
            grassSprite.setSize(Block.SIZE, Block.SIZE * 0.3f);
            grassSprite.setOrigin(0, 0);
            grasses.add(grassSprite);*//*
if (bob.getPosition().x > 8) {
    //grassSprite.setScale(1f, 0.5f);
}


            //grassSprite.draw(spriteBatch);
            //spriteBatch.draw(grassSprite, block.getPosition().x, block.getPosition().y + Block.SIZE * 0.9f, 0, 0, Block.SIZE, Block.SIZE * 0.3f, scX, scY, 0f);
            //grassSprite.setV2(0.5f);





        }

        for (int i = 0; i < 18; i++) {
            grasses.add(new Sprite(grassTexture));
            if (bob.getPosition().x + bob.getBounds().width / 2 >= grasses.get(i).getX() && bob.getPosition().x + bob.getBounds().width / 2 <= grasses.get(i).getX() + grasses.get(i).getWidth() && bob.getVelocity().y == 0) {
                //grassSprite.setScale(1f, 0.4f);
                // for (int i = 0; i < grasses.size; i++) {
                //Sprite grass = grasses.get(7);
                //if (grass.getX() == 8) {
                grasses.get(i).setScale(1f, 0.2f);
                //grasses.get(i).setU(2f);
                *//*float[] vs = grasses.get(i).getVertices();
                vs[SpriteBatch.X3] = 1f;
                vs[SpriteBatch.X2] = 1f;*//*
                //vX3 += 0.5f;
                //spriteBatch.draw(grass, grass.getX(), grass.getY(), 0, 0, Block.SIZE, Block.SIZE * 0.3f, scX, scY, 0f);
                //spriteBatch.draw(grass, 5, 5);
                //}
                // }
                scX = 1f;
                scY = 0.7f;


            } else {
                grasses.get(i).setScale(1f, 1f);
                //scY = 1f;
                //grass.setScale(1f, 1f);
            }
            grasses.get(i).setPosition(i * 0.5f, 1f);
            grasses.get(i).setSize(Block.SIZE * 0.5f, Block.SIZE * 0.3f);
            grasses.get(i).setOrigin(0, 0);

            float[] vs = grasses.get(i).getVertices();
            vs[SpriteBatch.X3] += vX3;
            vs[SpriteBatch.X2] += vX3;
            grasses.get(i).draw(spriteBatch);

        }


       *//* for (Block block : world.getBlocks()) {
            spriteBatch.draw(blockTexture, block.getPosition().x * ppuX, block.getPosition().y * ppuY, Block.SIZE * ppuX, Block.SIZE * ppuY);
        }*//*
    }*/

    /*private void drawDebug() {
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


        Rectangle rect = bob.getBounds();
        float x1 = bob.getPosition().x;
        float y1 = bob.getPosition().y;
        debugRenderer.setColor(0, 0, 1, 1);
        debugRenderer.rect(x1, y1, rect.width, rect.height);

        debugRenderer.end();
    }*/

    public void dispose() {
        grassTexture.dispose();
        cloudsBg.dispose();
        treesBg.dispose();
        bobTexture.dispose();
        blockTexture.dispose();
    }
}
