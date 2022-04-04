package com.bob;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Rectangle;

public class WorldRenderer {
    private World world;
    private OrthographicCamera cam;

    private static float CAMERA_WIDTH = 8f;
    private static float CAMERA_HEIGHT = 6f;

    ShapeRenderer debugRenderer = new ShapeRenderer();

    private Texture blockTexture;
    private Texture bobTexture;

    private SpriteBatch spriteBatch;
    private boolean debug = false;
    private int width;
    private int height;
    private float ppuX;
    private float ppuY;

    private static final float RUNNING_FRAME_DURATION = 0.06f;

    private TextureRegion bobIdle;
    //private TextureRegion blockTexture;


    private Animation bobIdleAnimation;


    public void setSize(int w, int h) {
        this.width = w;
        this.height = h;
        ppuX = (float)width / CAMERA_WIDTH;
        ppuY = (float)height / CAMERA_HEIGHT;
    }

    public WorldRenderer(World world, Boolean debug) {
        this.world = world;
        this.cam = new OrthographicCamera(CAMERA_WIDTH, CAMERA_HEIGHT);
        this.cam.position.set(CAMERA_WIDTH / 2f, CAMERA_HEIGHT / 2f, 0);
        this.cam.update();
        this.debug = debug;
        spriteBatch = new SpriteBatch();
        loadTextures();
    }

    private void loadTextures() {
        //bobTexture = new Texture(Gdx.files.internal("images/hobbit.png"));
        //blockTexture = new Texture(Gdx.files.internal("images/ground2.png"));

        TextureAtlas atlas = new TextureAtlas(Gdx.files.internal("images/textures3/textures.atlas"));
        bobIdle = atlas.findRegion("Hobbit - Idle1");
        System.out.println(bobIdle);
        TextureRegion[] bobIdleFrames = new TextureRegion[4];
        for (int i = 1; i < 4; i++) {
            bobIdleFrames[i] = atlas.findRegion("Hobbit - Idle" + (i + 2));
        }
        bobIdleAnimation = new Animation(RUNNING_FRAME_DURATION, bobIdleFrames);

    }

    public void render() {
        spriteBatch.begin();
        drawBlocks();
        drawBob();
        spriteBatch.end();
        if (debug) {
            drawDebug();
        }
    }

    private void drawBob() {
        Bob bob = world.getBob();

        spriteBatch.draw(bobIdle, bob.getPosition().x * ppuX, bob.getPosition().y * ppuY, Bob.SIZE * ppuX, Bob.SIZE * ppuY);

        //spriteBatch.draw(bobTexture, bob.getPosition().x * ppuX, bob.getPosition().y * ppuY, Bob.SIZE * ppuX, Bob.SIZE * ppuY);
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
            float x1 = block.getPosition().x;
            float y1 = block.getPosition().y;
            debugRenderer.setColor(1, 0, 0, 1);
            debugRenderer.rect(x1, y1, rect.width, rect.height);
        }

        Bob bob = world.getBob();
        Rectangle rect = bob.getBounds();
        float x1 = bob.getPosition().x + rect.x;
        float y1 = bob.getPosition().y + rect.y;
        debugRenderer.setColor(0, 1, 0, 1);
        debugRenderer.rect(x1, y1, rect.width, rect.height);

        debugRenderer.end();
    }


}
