package com.bob;

import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;

public class Bob {
    public static enum State {
        IDLE, JUMP, WALK, DYE
    }
    static final float SPEED = 4f;
    static final float JUMP_VELOCITY = 1f;
    static final float SIZE = 0.5f;

    public float stateTime = 0;

    Vector2 position = new Vector2();
    Vector2 acceleration = new Vector2();
    Vector2 velocity = new Vector2();
    Rectangle bounds = new Rectangle();
    State state = State.IDLE;
    boolean facingLeft = true;

    public Bob(Vector2 position) {
        this.position = position;
        this.bounds.height = SIZE;
        this.bounds.width = SIZE;
    }

    public void setState(State state) {
        this.state = state;
    }
    public void update(float dt) {
        stateTime += dt;
        position.add(velocity.scl(dt));
    }

    public static float getSPEED() {
        return SPEED;
    }

    public static float getJumpVelocity() {
        return JUMP_VELOCITY;
    }

    public static float getSIZE() {
        return SIZE;
    }

    public Vector2 getPosition() {
        return position;
    }

    public Vector2 getAcceleration() {
        return acceleration;
    }

    public Vector2 getVelocity() {
        return velocity;
    }

    public Rectangle getBounds() {
        return bounds;
    }

    public State getState() {
        return state;
    }

    public boolean isFacingLeft() {
        return facingLeft;
    }
}
