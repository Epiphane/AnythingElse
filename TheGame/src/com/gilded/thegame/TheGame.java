package com.gilded.thegame;

import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL10;

public class TheGame implements ApplicationListener {
	public static final int GAME_WIDTH = 160;
	public static final int GAME_HEIGHT = 144;
	public final static int TILESIZE = 16;
	
	private GameState gameState;

	/**
	 * Keeps track of delay in order to run updates possibly multiple times
	 */
	private float accumulatedTime = 0;
	
	/** Is the program running? (or paused?) */
	private boolean running = false;
	
	/**
	 * Keeps track of all inputs
	 */
	private final Input input = new Input();
	
	@Override
	public void create() {
		Art.load();
		gameState = new InGameState();
		gameState.init(this);
		Gdx.input.setInputProcessor(input);
		
		running = true;
	}

	@Override
	public void dispose() {
		gameState.dispose();
	}

	@Override
	public void render() {		
		Gdx.gl.glClearColor(1, 1, 1, 1);
		Gdx.gl.glClear(GL10.GL_COLOR_BUFFER_BIT);
		accumulatedTime += Gdx.graphics.getDeltaTime();
		while(accumulatedTime > 1.0f / 60.0f && running) {
			gameState.tick(input);
			input.tick();
			accumulatedTime -= 1.0f / 60.0f;
			
		}
		gameState.render();
	}
	
	public void setScreen(GameState newState) {
		if(gameState != null) gameState.removed();
		gameState = newState;
		if(gameState != null) gameState.init(this);
	}

	@Override
	public void resize(int width, int height) {
	}

	@Override
	public void pause() {
	}

	@Override
	public void resume() {
	}
}
