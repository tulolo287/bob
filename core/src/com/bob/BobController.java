package com.bob;

import java.util.Map;

import java.util.HashMap;

public class BobController {
    enum Keys {
        LEFT, RIGHT, JUMP, FIRE
    }

    private static final long LONG_JUMP_PRESS = 150L;
    private static final float GRAVITY = -20f;
    private static final float ACCELERATION = 250f;
    private static final float MAX_JUMP_SPEED = 7f;
    private static final float DUMP = 10.90f;
    private static final float MAX_VELOCITY = 1118f;

    private static final float WIDTH = 10f;

    private World world;
    private Bob bob;
    private long jumpPressedTime;
    public boolean jumpingPressed;

    static Map<Keys, Boolean> keys = new HashMap<BobController.Keys, Boolean>();
    static {
        keys.put(Keys.LEFT, false);
        keys.put(Keys.RIGHT, false);
        keys.put(Keys.JUMP, false);
        keys.put(Keys.FIRE, false);
    }

    public BobController(World world) {
        this.world = world;
        this.bob = world.getBob();
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

        bob.getAcceleration().y = GRAVITY;
        bob.getAcceleration().scl(dt);
        bob.getVelocity().add(bob.getAcceleration().x, bob.getAcceleration().y);
        if (bob.getAcceleration().x == 0) bob.getVelocity().x *= DUMP;
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
        }
    }

    private void processInput() {

        if (keys.get(Keys.JUMP)) {
            if (!bob.getState().equals(Bob.State.JUMP)) {
                jumpingPressed = true;
                jumpPressedTime = System.currentTimeMillis();
                bob.setState(Bob.State.JUMP);
                bob.getVelocity().y = MAX_JUMP_SPEED;
            } else {
                if (jumpingPressed && ((System.currentTimeMillis() - jumpPressedTime) >= LONG_JUMP_PRESS)) {
                    jumpingPressed = false;
                    //bob.setState(Bob.State.IDLE);
                } else {
                    if (jumpingPressed) {
                        bob.getVelocity().y = MAX_JUMP_SPEED;
                    }
                }
            }
        }
        if (keys.get(Keys.LEFT)) {
            bob.setFacingLeft(true);
            if (!bob.getState().equals(Bob.State.JUMP)) {
                bob.setState(Bob.State.WALK);
                //bob.getVelocity().x = -Bob.SPEED;
            }
            bob.getAcceleration().x = -ACCELERATION;
        } else if (keys.get(Keys.RIGHT)) {
            bob.setFacingLeft(false);
            if (!bob.getState().equals(Bob.State.JUMP)) {
                bob.setState(Bob.State.WALK);
            }
            bob.getAcceleration().x = ACCELERATION;
        } else {
            if (!bob.getState().equals(Bob.State.JUMP)) {
                bob.setState(Bob.State.IDLE);
            }
                bob.getAcceleration().x = 0;
        }
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
