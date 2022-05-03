package com.bob;

import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.ContactImpulse;
import com.badlogic.gdx.physics.box2d.ContactListener;
import com.badlogic.gdx.physics.box2d.Manifold;

public class WorldContactListener implements ContactListener {

    public WorldContactListener() {
        super();
    }

    public static boolean grounded;
    @Override
    public void beginContact(Contact contact) {
        if (contact.getFixtureA().getUserData() == null && contact.getFixtureB().getUserData() == null) return;
        if (contact.getFixtureA().getUserData() == "feet" || contact.getFixtureB().getUserData() == "feet") {
            grounded = true;
            System.out.println("Grounded");
        }
    }

    @Override
    public void endContact(Contact contact) {
        if (contact.getFixtureA().getUserData() == null && contact.getFixtureB().getUserData() == null) return;
        if (contact.getFixtureA().getUserData() == "feet" || contact.getFixtureB().getUserData() == "feet") {
            grounded = false;
            System.out.println("On air");
        }
    }

    @Override
    public void preSolve(Contact contact, Manifold oldManifold) {

    }

    @Override
    public void postSolve(Contact contact, ContactImpulse impulse) {

    }
}
