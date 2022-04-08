package com.bob;

import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.ui.List;
import com.badlogic.gdx.utils.Array;

import java.util.ArrayList;
import java.util.logging.Level;

public class World {
    Array<Block> blocks = new Array<Block>();
    Block[][] block;
    Array<Rectangle> collisionRects = new Array<Rectangle>();

    Bob bob;

    Level level;

    public Array<Rectangle> getCollisionRects() {
        return collisionRects;
    }



    public ArrayList<Block> getDrawableBlocks(int width, int height) {
        int x = (int) bob.getPosition().x - width;
        int y = (int) bob.getPosition().y - height;
        if (x < 0) {
            x = 0;
        }
        if (y < 0) {
            y = 0;
        }
        int x2 = x + 2 * width;
        int y2 = y + 2 * height;
        //.if (x2 > )
        ArrayList<Block> blocks = new ArrayList<Block>();
        Block block;
        for (int col = x; col <= x2; col++) {
            for (int row = y; row <= y2; row++) {
                block = getBlock()[col][row];
                if (block != null) {
                    blocks.add(block);
                }
            }
        }
        return blocks;
    }

    public Array<Block> getBlocks() {
        return blocks;
    }

    public Block[][] getBlock() {
        return block;
    }
    public Bob getBob() {
        return bob;
    }

    public World() {
        createWorld();
    }

    private void createWorld() {
        bob = new Bob(new Vector2(1, 1));

        for (int i = 0; i < 20; i++) {
            blocks.add(new Block(new Vector2(i, 0)));
            blocks.add(new Block(new Vector2(i, 7)));
            //if (i > 2) blocks.add(new Block(new Vector2(i, 1)));
        }
        blocks.add(new Block(new Vector2(9, 1)));
        blocks.add(new Block(new Vector2(11, 3)));
        blocks.add(new Block(new Vector2(14, 4)));
        blocks.add(new Block(new Vector2(9, 5)));
        blocks.add(new Block(new Vector2(5, 3)));
        blocks.add(new Block(new Vector2(6, 3)));
        blocks.add(new Block(new Vector2(7, 3)));
    }
}
