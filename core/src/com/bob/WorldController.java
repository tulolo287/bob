package com.bob;

import java.util.Map;

import java.util.HashMap;

public class WorldController {
    enum Keys {
        LEFT, RIGHT, JUMP, FIRE
    }

    private World world;
    private Bob bob;

    static Map<Keys, Boolean> keys = new HashMap<WorldController.Keys, Boolean>();
    static {
        keys.put(Keys.LEFT, false);
        keys.put(Keys.RIGHT, false);
        keys.put(Keys.JUMP, false);
        keys.put(Keys.FIRE, false);
    }

    public WorldController(World world) {
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
    }

    public void fireReleased() {
        keys.get(keys.put(Keys.FIRE, false));
    }

    public void update(float dt) {
        processInput();
        bob.update(dt);
    }

    private void processInput() {
        if (keys.get(Keys.LEFT)) {
           // bob.setFacingLeft(true);
            bob.setState(Bob.State.WALK);
            bob.getVelocity().x = -Bob.SPEED;
        }
        if (keys.get(Keys.RIGHT)) {
           // bob.setFacingLeft(false);
            bob.setState(Bob.State.WALK);
            bob.getVelocity().x = Bob.SPEED;
        }
        if (keys.get(Keys.LEFT) && keys.get(Keys.RIGHT) || !keys.get(Keys.LEFT) && !keys.get(Keys.RIGHT)) {
            bob.setState(Bob.State.IDLE);
            bob.getAcceleration().x = 0;
            bob.getVelocity().x = 0;
        }
    }
}
