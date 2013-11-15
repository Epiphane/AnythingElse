package com.gilded.thegame;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.Sprite;

public class InGameState extends GameState {
	private Player mainCharacter;
	private Level currentLevel;
	
	public InGameState() {
		float w = Gdx.graphics.getWidth();
		float h = Gdx.graphics.getHeight();
		
		camera = new OrthographicCamera(1, h/w);
		
		mainCharacter = new Player(0,0);
		currentLevel = new Level("testMap.tmx", mainCharacter);
	}
	
	@Override
	public void render() {
		currentLevel.render();
	}

	@Override
	public void tick(Input input) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void dispose() {
		currentLevel.dispose();
	}

}
