package com.frankthefrog.game;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.ScreenUtils;
import com.frankthefrog.game.Screens.PlayScreen;

public class Frank extends Game {
	public SpriteBatch batch;
	public static final int V_WIDTH = 800;
	public static final int V_HEIGHT = 400;
	public static final float PPM = 100;
	public static final short NOTHING_BIT = 0;
	public static final short GROUND_BIT = 1 ;
	public static final short PLAYER_BIT = 2;
	public static final short STAR_BIT = 4;
	public static final short HEART_BIT = 8;
	public static final short LIGHTNING_BIT = 16;
	public static final short SPIKE_BIT = 32;
	public static final short COIN_BIT = 64;
	public static AssetManager manager;

	@Override
	public void create () {
		batch = new SpriteBatch();
		manager = new AssetManager();
		// Load music and sounds

		setScreen(new PlayScreen(this));
	}

	@Override
	public void render () {
		super.render();
	}
	
	@Override
	public void dispose () {
		super.dispose();
		batch.dispose();
		manager.dispose();
	}
}
