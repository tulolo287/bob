package com.bob;

import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Pool;

import java.util.Map;

import java.util.HashMap;

public class BobController {
    enum Keys {
        LEFT, RIGHT, JUMP, FIRE
    }

    private static final long LONG_JUMP_PRESS = 150L;
    private static final float GRAVITY = -150f;
    private static final float ACCELERATION = 100f;
    private static final float MAX_JUMP_SPEED = 10f;
    private static final float DUMP = 10.90f;
    private static final float MAX_VELOCITY = 1118f;
    private static final float JUMP_FORCE = 5f;

    public boolean grounded = false;

    private static final float WIDTH = 20f;

    private WorldRenderer renderer;
    private Body bobBody;
    private Bob bob;
    private long jumpPressedTime;
    public boolean jumpingPressed;
    private WorldContactListener worldContactListener;

    private Array<Block> collidable = new Array<Block>();

    private Pool<Rectangle> rectPool = new Pool<Rectangle>() {
        @Override
        protected Rectangle newObject() {
            return new Rectangle();
        }
    };

    static Map<Keys, Boolean> keys = new HashMap<BobController.Keys, Boolean>();
    static {
        keys.put(Keys.LEFT, false);
        keys.put(Keys.RIGHT, false);
        keys.put(Keys.JUMP, false);
        keys.put(Keys.FIRE, false);
    }

    public BobController(WorldRenderer renderer) {
        this.renderer = renderer;
        this.bob = renderer.getBob();
        this.bobBody = renderer.getBobBody();
    }

    public void leftPressed() {
        keys.get(keys.put(Keys.LEFT, true));
    }

    public void rightPressed() {
        keys.get(keys.put(Keys.RIGHT, true));
    }

    public void jumpPressed() {
        keys.get(keys.put(Keys.JUMP, true));
    }

    public void firePressed() {
        keys.get(keys.put(Keys.FIRE, true));
    }

    public void leftReleased() {
        keys.get(keys.put(Keys.LEFT, false));
    }

    public void rightReleased() {
        keys.get(keys.put(Keys.RIGHT, false));
    }

    public void jumpReleased() {
        keys.get(keys.put(Keys.JUMP, false));
        jumpingPressed = false;
    }

    public void fireReleased() {
        keys.get(keys.put(Keys.FIRE, false));
    }

    public void update(float dt) {
        processInput();


        /*if (WorldContactListener.grounded && bob.getState().equals(Bob.State.JUMP)) {
            bob.setState(Bob.State.IDLE);
        }*/
        bob.update(dt);

       /* bob.getAcceleration().y = GRAVITY;
        bob.getAcceleration().scl(dt);
        bob.getVelocity().add(bob.getAcceleration().x, bob.getAcceleration().y);
        if (bob.getAcceleration().x == 0) bob.getVelocity().x *= DUMP;
        //checkCollisionWithBlocks(dt);
        if (bob.getAcceleration().x > MAX_VELOCITY) {
            bob.getAcceleration().x = MAX_VELOCITY;
        }
        if (bob.getAcceleration().x < -MAX_VELOCITY) {
            bob.getAcceleration().x = -MAX_VELOCITY;
        }

        bob.update(dt);

        if (bob.getPosition().y < 1f) {
            bob.getPosition().y = 1f;
            bob.setPosition(bob.getPosition());
            if (bob.getState().equals(Bob.State.JUMP)) {
                bob.setState(Bob.State.IDLE);
            }
        }

        if (bob.getPosition().x < 0) {
            bob.getPosition().x = 0f;
            bob.setPosition(bob.getPosition());
            if (!bob.getState().equals(Bob.State.JUMP)) {
                bob.setState(Bob.State.IDLE);
            }
        }

        if (bob.getPosition().x > WIDTH - bob.getBounds().width) {
            bob.getPosition().x = WIDTH - bob.getBounds().width;
            bob.setPosition(bob.getPosition());
            if (!bob.getState().equals(Bob.State.JUMP)) {
                bob.setState(Bob.State.IDLE);
            }
        }*/
    }

/*
    public void checkCollisionWithBlocks(float dt) {
        bob.getVelocity().scl(dt);

        Rectangle bobRect = rectPool.obtain();
        bobRect.set(bob.getBounds().x, bob.getBounds().y, bob.getBounds().width, bob.getBounds().height);

        int startX, endX;
        int startY = (int) bob.getBounds().y;
        int endY = (int) (bob.getBounds().y + bob.getBounds().height);

        if (bob.getVelocity().x < 0) {
            startX = endX = (int) Math.floor(bob.getBounds().x + bob.getVelocity().x);
        } else {
            startX = endX = (int) Math.floor(bob.getBounds().x + bob.getBounds().width + bob.getVelocity().x);
        }

        populateCollidableBlocks(startX, startY, endX, endY);

        bobRect.x += bob.getVelocity().x;

        for (Block block : collidable) {
            if (block == null) continue;
            if (bobRect.overlaps(block.getBounds())) {
                bob.getVelocity().x = 0;
                world.getCollisionRects().add(block.getBounds());
                break;
            }
        }

        world.getCollisionRects().clear();

        startX = (int) bob.getBounds().x;
        endX = (int) (bob.getBounds().x + bob.getBounds().width);

        if (bob.getVelocity().y < 0) {
            startY = endY = (int) Math.floor(bob.getBounds().y + bob.getVelocity().y);
        } else {
            startY = endY = (int) Math.floor(bob.getBounds().y + bob.getBounds().height + bob.getVelocity().y);
        }

        populateCollidableBlocks(startX, startY, endX, endY);

        bobRect.y += bob.getVelocity().y;

        for (Block block : collidable) {
            if (block == null) continue;
            if (bobRect.overlaps(block.getBounds())) {
                if (bob.getVelocity().y < 0) {
                    grounded = true;
                }
                bob.getVelocity().y = 0;
                world.getCollisionRects().add(block.getBounds());
                break;
            }
        }

        bobRect.y = bob.getPosition().y;

        // update Bob's position
        bob.getPosition().add(bob.getVelocity());
        bob.getBounds().x = bob.getPosition().x;
        bob.getBounds().y = bob.getPosition().y;

        // un-scale velocity (not in frame time)
        //bob.getVelocity().scl(1 / dt);
    }

    private void populateCollidableBlocks(int startX, int startY, int endX, int endY) {
        collidable.clear();
        for (int x = startX; x <= endX; x++) {
            for (int y = startY; y <= endY; y++) {
                if (x >= 0 && x < 40 && y >= 0 && y < 7) {
                    collidable.add(world.getBlock(x, y));
                }
            }
        }
    }
*/

    private void processInput() {

        if (keys.get(Keys.JUMP) && WorldContactListener.grounded) {
            bobBody.applyLinearImpulse(new Vector2(0f, JUMP_FORCE), bobBody.getWorldCenter(), true);
            bob.setState(Bob.State.JUMP);
            //bob.getVelocity().y = MAX_JUMP_SPEED;
          /*  if (!bob.getState().equals(Bob.State.JUMP)) {
                jumpingPressed = true;
                jumpPressedTime = System.currentTimeMillis();
                bob.setState(Bob.State.JUMP);
                bobBody.applyForceToCenter(new Vector2(0f, -JUMP_FORCE), true);
                //bob.getVelocity().y = MAX_JUMP_SPEED;
                grounded = false;
            } else {
                if (jumpingPressed && ((System.currentTimeMillis() - jumpPressedTime) >= LONG_JUMP_PRESS)) {
                    jumpingPressed = false;
                    //grounded = true;
                   // bob.setState(Bob.State.IDLE);
                } else {
                    if (jumpingPressed) {
                        bobBody.applyForceToCenter(new Vector2(0f, 10f), true);
                        //bobBody.getVelocity().y = MAX_JUMP_SPEED;
                    }
                }
            }*/
        }
        if (keys.get(Keys.LEFT)) {
            bob.setFacingLeft(true);
            if (WorldContactListener.grounded) {
                bob.setState(Bob.State.WALK);
                //bob.getVelocity().x = -Bob.SPEED;
            }
            //bob.getAcceleration().x = -ACCELERATION;
            bobBody.applyLinearImpulse(new Vector2(-ACCELERATION, 0), bobBody.getWorldCenter(), true);
        } else if (keys.get(Keys.RIGHT)) {
            bob.setFacingLeft(false);
            if (WorldContactListener.grounded) {
                bob.setState(Bob.State.WALK);
            }
            //bob.getAcceleration().x = ACCELERATION;
            bobBody.applyLinearImpulse(new Vector2(ACCELERATION, 0), bobBody.getWorldCenter(), true);
        } else {
            bobBody.setLinearVelocity(0, bobBody.getLinearVelocity().y);
        }
        if (bobBody.getLinearVelocity().x == 0) {
            bob.setState(Bob.State.IDLE);
        }
            //bob.getAcceleration().x = 0;
            ///bobBody.setV

        if (keys.get(Keys.FIRE)) {
            bob.setState(Bob.State.FIRE);
            bob.fired = true;
            //bob.getVelocity().y = Bob.JUMP_VELOCITY;
        }
        /*if (keys.get(Keys.LEFT) && keys.get(Keys.RIGHT) || !keys.get(Keys.LEFT) && !keys.get(Keys.RIGHT) && !bob.fired) {
            bob.setState(Bob.State.IDLE);
            bob.getAcceleration().x = 0;
            bob.getVelocity().x = 0;
        }*/
    }
}
