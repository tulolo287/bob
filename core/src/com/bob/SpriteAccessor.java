package com.bob;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import aurelienribon.tweenengine.TweenAccessor;

public class SpriteAccessor implements TweenAccessor<Sprite> {
    public static final int SKEW_X2X3 = 1;
    @Override
    public int getValues(Sprite sprite, int i, float[] floats) {
        switch (i) {
            case SKEW_X2X3:
                float[] vs = sprite.getVertices();
                //floats[0] = sprite.getX();
                //floats[1] = sprite.getY();
                floats[0] = vs[SpriteBatch.X2] - sprite.getX();
                floats[1] = vs[SpriteBatch.X3] - sprite.getX() - sprite.getWidth();
                //floats[0] = sprite.getRotation();
                return 2;
            //default: assert false; return -1;
        }
        assert false;
        return -1;
    }

    @Override
    public void setValues(Sprite sprite, int i, float[] floats) {
        switch (i) {
            case SKEW_X2X3:
                //sprite.setRotation(floats[0]);
                float x2 = sprite.getX();
                float x3 = x2 + sprite.getWidth();
                float[] vs = sprite.getVertices();
                vs[SpriteBatch.X2] = x2 + floats[0];
                vs[SpriteBatch.X3] = x3 + floats[1];
                //sprite.setX(floats[0]);
                //sprite.setY(floats[1]);
                //sprite.setScale(floats[0]);

                break;
            //default: assert false; break;
        }
    }
}
