package com.gilded.thegame;

import java.util.ArrayList;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.objects.PolygonMapObject;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.math.Polygon;

public class Level {
	private Camera camera;
	private TiledMap map;
	private ArrayList<Polygon> polygonCollisions;
	private OrthogonalTiledMapRenderer renderer;
	
	/** 
	 * Width and height of the level in tiles
	 */
	private int width, height;

	private SpriteBatch batch;
	
	private Player mainCharacter;
	
	/**
	 * Initialize the map.
	 * 
	 * @param mapName - Name of the map file (e.g. exampleMap.tmx)
	 * @param mainCharacter
	 */
	public Level(String mapName, Player mainCharacter) {
		camera = new Camera(TheGame.GAME_WIDTH, TheGame.GAME_HEIGHT);
		camera.setToOrtho(false, TheGame.GAME_WIDTH / (TheGame.TILE_SIZE * TheGame.TILE_SCALE), TheGame.GAME_HEIGHT / (TheGame.TILE_SIZE * TheGame.TILE_SCALE));
		camera.update(width, height);
		
		map = new TmxMapLoader().load("maps/"+mapName);
		
		this.mainCharacter = mainCharacter;
		this.mainCharacter.setCurrentLevel(this);
		
		this.mainCharacter.setOrigin(mainCharacter.getWidth()/2, mainCharacter.getHeight()/2);

		placeCharacter();
		
		// Set size
		width = (Integer) map.getProperties().get("width");
		height = (Integer) map.getProperties().get("height");
		
		// Create renderer
		renderer = new OrthogonalTiledMapRenderer(map, 1 / TheGame.TILE_SIZE);
		renderer.setView(camera);
		
		batch = renderer.getSpriteBatch();
		
		camera.setFocus(this.mainCharacter, true);
		camera.update(width, height);
		
		polygonCollisions = new ArrayList<Polygon>();
		for(MapObject object : map.getLayers().get(2).getObjects()) {
			if(object instanceof PolygonMapObject) {
				polygonCollisions.add(((PolygonMapObject)object).getPolygon());
			}
		}
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
		camera.update(width, height);
		renderer.setView(camera);
		renderer.render();
		batch.begin();
		mainCharacter.draw(batch);
		batch.end();
	}

	public void dispose() {
		map.dispose();
	}

	public boolean canMove(Entity entity, float xc, float yc, float w, float h,
			float dx, float dy) {
		
		float x0 = (xc) * TheGame.TILE_SIZE;
		float y0 = (yc) * TheGame.TILE_SIZE;
		float x1 = (xc + w) * TheGame.TILE_SIZE;
		float y1 = (yc + h) * TheGame.TILE_SIZE;
		
		for(Polygon object : polygonCollisions) {
			if(object.contains(x0, y0) || object.contains(x0, y1) || object.contains(x1, y0) || object.contains(x1, y1)) {
				return false;
			}
		}
		
		return true;
	}
	
	public void placeCharacter() {
		MapObject spawn = map.getLayers().get(3).getObjects().get("Spawn");
		this.mainCharacter.setPosition((Integer) spawn.getProperties().get("x") / TheGame.TILE_SIZE, (Integer) spawn.getProperties().get("y") / TheGame.TILE_SIZE);
		
		camera.rush();
	}
	
	public Camera getCamera() {
		return camera;
	}

	public void setCamera(Camera camera) {
		this.camera = camera;
	}

	public int getWidth() {
		return width;
	}

	public void setWidth(int width) {
		this.width = width;
	}

	public int getHeight() {
		return height;
	}

	public void setHeight(int height) {
		this.height = height;
	}
}
