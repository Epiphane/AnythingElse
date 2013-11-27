package com.gilded.thegame;

import com.badlogic.gdx.graphics.OrthographicCamera;

public class InGameState extends GameState {
	private Player mainCharacter;
	private Level currentLevel;
	
	public InGameState() {		
		camera = new OrthographicCamera(TheGame.GAME_WIDTH, TheGame.GAME_HEIGHT);
		
		mainCharacter = new Player(0, 0);
		currentLevel = new Level("testMap.tmx", mainCharacter);
	}
	
	@Override
	public void render() {
		currentLevel.render();
	}

	@Override
	public void tick(Input input) {
		currentLevel.tick(input);
	}

	@Override
	public void dispose() {
		currentLevel.dispose();
	}

}
