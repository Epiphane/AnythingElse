package com.gilded.thegame;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;

public class Level {
	private OrthographicCamera camera;
	private TiledMap map;
	private OrthogonalTiledMapRenderer renderer;
	
	private SpriteBatch batch;
	
	private Player mainCharacter;
	
	/**
	 * Initialize the map.
	 * 
	 * @param mapName - Name of the map file (e.g. exampleMap.tmx)
	 * @param mainCharacter
	 */
	public Level(String mapName, Player mainCharacter) {
		camera = new OrthographicCamera();
		camera.setToOrtho(false, 30, 20);
		camera.update();
		
		this.mainCharacter = mainCharacter;
		mainCharacter.setCurrentLevel(this);
		
		map = new TmxMapLoader().load("maps/"+mapName);
		renderer = new OrthogonalTiledMapRenderer(map, 1 / 16f);
		renderer.setView(camera);
		
		batch = renderer.getSpriteBatch();
	}
	
	/**
	 * Update the level
	 */
	public void tick(Input input) {
		mainCharacter.tick(input);
	}
	
	/**
	 * Draw the level!
	 */
	public void render() {
		renderer.render();
		batch.begin();
		mainCharacter.draw(batch);
		batch.end();
	}

	public void dispose() {
		map.dispose();
	}

	public boolean canMove(Entity entity, double xc, double yc, int w, int h,
			double dx, double dy) {
		// TODO Auto-generated method stub
		return false;
	}
}
