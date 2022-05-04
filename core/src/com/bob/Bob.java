package com.bob;

import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;

public class Bob {
    public static enum State {
        IDLE, JUMP, WALK, FIRE
    }
    static final float SPEED = 3f;
    static final float JUMP_VELOCITY = 1f;
    static final float SIZE = 32;

    public float stateTime = 0;

    private Box2DDebugRenderer box2DDebugRenderer;

    Vector2 position = new Vector2();
    Vector2 acceleration = new Vector2();
    Vector2 velocity = new Vector2();
    Rectangle bounds = new Rectangle();
    State state = State.IDLE;
    public boolean facingLeft = false;

    public boolean fired = false;

    public Bob() {
        this.position = position;
        this.bounds.x = position.x;
        this.bounds.y = position.y;
        this.bounds.height = SIZE;
        this.bounds.width = SIZE;


    }



    public void setState(State state) {
        this.state = state;
    }
    public void update(float dt) {
        stateTime += dt;
        //position.add(velocity.scl(dt));
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

    public void setPosition(Vector2 position) {
        this.position = position;
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

    public void setFacingLeft(Boolean b) {
        facingLeft = b;
    }

    public float getStateTime() {
        return stateTime;
    }
}
