package com.bob;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.ScreenUtils;

public class App extends Game {
	SpriteBatch batch;
	static final float PPM = 100;
	static final byte PLAYER = 2;
	static final byte GROUND = 4;

	@Override
	public void create () {
		setScreen(new GameScreen());
	}


}
