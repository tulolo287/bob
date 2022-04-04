package com.bob;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;

public class World {
    Array<Block> blocks = new Array<Block>();

    Bob bob;

    public Array<Block> getBlocks() {
        return blocks;
    }

    public Bob getBob() {
        return bob;
    }

    public World() {
        createWorld();
    }

    private void createWorld() {
        bob = new Bob(new Vector2(2, 1));

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
