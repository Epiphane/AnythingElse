package com.gilded.thegame;

import java.util.ArrayList;
import java.util.Iterator;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.MapObjects;
import com.badlogic.gdx.maps.MapProperties;
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
		camera.setToOrtho(false, TheGame.GAME_WIDTH / (TheGame.TILE_SIZE * TheGame.DISPLAY_TILE_SCALE), TheGame.GAME_HEIGHT / (TheGame.TILE_SIZE * TheGame.DISPLAY_TILE_SCALE));
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
		
		MapProperties mp = map.getProperties();
		Iterator<String> stringit = mp.getKeys();
		
		MapObjects mo = map.getLayers().get(2).getObjects();
		
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

	public boolean canMove(float xc, float yc, float w, float h) {
		
		float x0 = (xc) * TheGame.TILE_SIZE;
		float y0 = (yc) * TheGame.TILE_SIZE;
		float x1 = (xc + w) * TheGame.TILE_SIZE;
		float y1 = (yc + h) * TheGame.TILE_SIZE;
		
		for(Polygon polyToCheck : polygonCollisions) {
			// Check if our 4 corners intersect with any platform polygons
			if(polyToCheck.contains(x0, y0) || polyToCheck.contains(x0, y1) || polyToCheck.contains(x1, y0) || polyToCheck.contains(x1, y1)) {
				return false;
			}
		}
		
		return true;
	}
	
	/**
	 * See if there is a wall touching the target Entity in the specified direction.
	 * @param target The Entity we need to see
	 * @param direction The direction (Up is 0, clockwise ascends) that we want to check
	 * @return Whether or not there is a wall at the specified location.
	 */
	public boolean checkFoot(Entity target, int direction) {
		Point offset = Utility.offsetFromDirection(direction);
		PointD offsetD = new PointD(offset.x, offset.y);
		offsetD.x *= 0.01;
		offsetD.y *= 0.01;
		
		float centerX = target.getX() + target.getWidth()/2;
		float centerY = target.getY() + target.getHeight()/2;

		// Grab the location of the corners we want to check
		int cornerCW = direction/2;
		int cornerCCW = direction/2 - 1;
		if(cornerCCW == -1) cornerCCW = 3;
		
		PointD pointCW = Utility.getCorner(target.getBoundingRectangle(), cornerCW);
		PointD pointCCW = Utility.getCorner(target.getBoundingRectangle(), cornerCCW);
		
		pointCW.addPoint(new PointD(centerX, centerY));
		pointCCW.addPoint(new PointD(centerX, centerY));
		
		pointCW.addPoint(offsetD);
		pointCCW.addPoint(offsetD);
		
		for(Polygon walls : polygonCollisions) {
			if(walls.contains((float) pointCW.x, (float) pointCW.y) || walls.contains((float) pointCCW.x, (float) pointCCW.y)) {
				return true;
			}
		}
		return false;
	}
	
	
	public void placeCharacter() {
		MapObject spawn = map.getLayers().get(3).getObjects().get("Spawn");
		this.mainCharacter.setPosition((Integer) spawn.getProperties().get("x") / TheGame.TILE_SIZE, 
				(Integer) spawn.getProperties().get("y") / TheGame.TILE_SIZE);
		
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
