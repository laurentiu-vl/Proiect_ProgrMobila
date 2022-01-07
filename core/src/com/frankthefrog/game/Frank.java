package com.frankthefrog.game;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.frankthefrog.game.Screens.PlayScreen;

public class Frank extends Game {
	public SpriteBatch batch;
	public static final int V_WIDTH = 1920;
	public static final int V_HEIGHT = 1080;
	public static final float PPM = 100;
	public static final short NOTHING_BIT = 0;
	public static final short GROUND_BIT = 1 ;
	public static final short PLAYER_BIT = 2;
	public static final short STAR_BIT = 4;
	public static final short HEART_BIT = 8;
	public static final short LIGHTNING_BIT = 16;
	public static final short SPIKE_BIT = 32;
	public static final short COIN_BIT = 64;
	public static final short TRAMPOLINE_BIT = 128;
	public static final short WALL_BIT = 256;
	public static final short KEY_BIT = 512;
	public static final short DOOR_BIT = 1024;
	public static final short POWER_UP_BIT = 2048;
	public static final short BOTTOM_BIT = 4096;

	public static AssetManager manager;

	@Override
	public void create () {
		batch = new SpriteBatch();
		manager = new AssetManager();
		manager.load("Sounds/background.mp3", Music.class);
		manager.load("Sounds/coins.wav", Sound.class);
		manager.load("Sounds/dead.mp3", Sound.class);
		manager.load("Sounds/final.mp3", Music.class);
		manager.load("Sounds/final-level.mp3", Music.class);
		manager.load("Sounds/jump.wav", Sound.class);
		manager.load("Sounds/power-up.wav", Sound.class);
		manager.load("Sounds/press-button.wav", Sound.class);
		manager.load("Sounds/spikes.mp3", Sound.class);
		manager.load("Sounds/star.wav", Sound.class);
		manager.load("Sounds/intro.mp3", Music.class);
		manager.finishLoading();
		setScreen(new PlayScreen(this));
	}

	public void reset() {
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
