package com.gilded.thegame;

import java.util.ArrayList;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.objects.PolygonMapObject;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapTile;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.maps.tiled.TiledMapTileSet;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.math.Polygon;

public class Level {
	public  static int numTilesInEachSet;
	
	private Camera camera;
	private TiledMap map;
	private ArrayList<MapObject> polygonCollisions;
	private TiledMapRenderer renderer;
	
	/** 
	 * Width and height of the level in tiles
	 */
	private int width, height;

	private SpriteBatch batch;
	
	private Player mainCharacter;
	
	private int changeColorRange;
	private int changeColor;
	private Point changeColorOrigin;
	
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
		changeColor = -1;
		
		numTilesInEachSet = (Integer) map.getTileSets().getTileSet(1).getProperties().get("firstgid") - 1;
		
		this.mainCharacter = mainCharacter;
		this.mainCharacter.setCurrentLevel(this);
		
		this.mainCharacter.setOrigin(mainCharacter.getWidth()/2, mainCharacter.getHeight()/2);

		placeCharacter();
		
		// Set size
		width = (Integer) map.getProperties().get("width");
		height = (Integer) map.getProperties().get("height");
		
		// Create renderer
		renderer = new TiledMapRenderer(map, 1 / TheGame.TILE_SIZE);
		renderer.setView(camera);
		
		batch = renderer.getSpriteBatch();
		
		camera.setFocus(this.mainCharacter, true);
		camera.update(width, height);
		
		polygonCollisions = new ArrayList<MapObject>();
		for(MapObject object : map.getLayers().get(2).getObjects()) {
			if(object instanceof PolygonMapObject) {
				polygonCollisions.add(object);
			}
		}
	}
	
	/**
	 * Update the level
	 */
	public void tick(Input input) {
		int newColor = input.getNewColor();
		if(newColor != 0) {
			changeColor(newColor);
			input.freeNewColor();
		}
		updateTiles();
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
		
		for(MapObject objectToCheck : polygonCollisions) {
			// Check if our 4 corners intersect with any platform polygons
			/*if(polyToCheck.contains(x0, y0)) {
				System.out.println("Intersected: " + x0 + ", " + y0);
			}
			if(polyToCheck.contains(x1, y0)) {
				System.out.println("Intersected: " + x1 + ", " + y0);
			}
			if(polyToCheck.contains(x0, y1)) {
				System.out.println("Intersected: " + x0 + ", " + y1);
			}
			if(polyToCheck.contains(x1, y1)) {
				System.out.println("Intersected: " + x1 + ", " + y1);
			}*/
			Polygon polyToCheck = ((PolygonMapObject) objectToCheck).getPolygon();
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
	 * @return The MapObject we're touching in that direction, or null if there is none.
	 */
	public MapObject checkFoot(Entity target, int direction) {
		Point offset = Utility.offsetFromDirection(direction);
		PointD offsetD = new PointD(offset.x, offset.y);
		offsetD.x *= 0.1;
		offsetD.y *= 0.1;
		
//		float centerX = (target.getX() + target.getWidth()/2) * TheGame.TILE_SIZE;
//		float centerY = (target.getY() + target.getHeight()/2) * TheGame.TILE_SIZE;

		// Grab the location of the corners we want to check
		int cornerCW = direction/2 + 1;
		int cornerCCW = direction/2;
		if(cornerCW == 4) cornerCW = 0;
		
		PointD pointCW = Utility.getCorner(target.getBoundingRectangle(), cornerCW);
		PointD pointCCW = Utility.getCorner(target.getBoundingRectangle(), cornerCCW);
		pointCW.mult(TheGame.TILE_SIZE);
		pointCCW.mult(TheGame.TILE_SIZE);
//		System.out.println("Center: " + centerX + ", " + centerY);
//		System.out.println("Clockwise pt: " + pointCW.x + ", " + pointCW.y);
//		System.out.println("Counterclockwise pt: " + pointCCW.x + ", " + pointCCW.y);
		
		pointCW.addPoint(offsetD);
		pointCCW.addPoint(offsetD);
		
		for(MapObject objectToCheck : polygonCollisions) {
			Polygon walls = ((PolygonMapObject) objectToCheck).getPolygon();
			if(walls.contains((float) pointCW.x, (float) pointCW.y) || walls.contains((float) pointCCW.x, (float) pointCCW.y)) {
				
//				MapProperties props = objectToCheck.getProperties();
//				if(props.containsKey("Slope")) {
//					String cool = (String) props.get("Slope");
//				}
				
				return objectToCheck;
			}
		}
		return null;
	}
	
	
	public void placeCharacter() {
		MapObject spawn = map.getLayers().get(3).getObjects().get("Spawn");
		this.mainCharacter.setPosition((Integer) spawn.getProperties().get("x") / TheGame.TILE_SIZE, 
				(Integer) spawn.getProperties().get("y") / TheGame.TILE_SIZE);
		
		camera.rush();
	}
	
	/**
	 * Changes the world to be a new color!
	 * 
	 * @param color
	 */
	public void changeColor(int color) {
		mainCharacter.changeColor(color);
		
		changeColor = color;
		changeColorRange = 0;
		changeColorOrigin = mainCharacter.getCoords();
		changeTiles(2);
	}

	public void updateTiles() {
		if(changeColor >= 0)
			changeTiles(changeColorRange++);
	}
	
	public void changeTiles(int range) {
		TiledMapTileLayer layer = (TiledMapTileLayer) map.getLayers().get(1);

		int layerWidth = layer.getWidth();
		int layerHeight = layer.getHeight();
		
		int rowStart = Math.max(0, changeColorOrigin.y - range);
		int colStart = Math.max(0, changeColorOrigin.x - range);
		int rowEnd = Math.min(layerHeight, changeColorOrigin.y + range);
		int colEnd = Math.min(layerWidth, changeColorOrigin.x + range);
		
		// If we've done the whole layer, stop computing
		if(rowStart == 0 && rowEnd == layerHeight && colStart == 0 && colEnd == layerWidth) {
			changeColor = -1;
			return;
		}
		
		// Check to see if we have a tileset for this color. if so, replace all tiles
		TiledMapTileSet newTileSet = map.getTileSets().getTileSet(Input.colors[changeColor]);
		if(newTileSet != null) {
			int offset = (Integer) newTileSet.getProperties().get("firstgid") - 1;
			
			TiledMapTile newTile;
			for (int row = rowStart; row < rowEnd; row++) {
				for (int col = colStart; col < colEnd; col++) {
					// Only do the square at range distance
					if(true || row == rowStart || row == rowEnd || col == colStart || col == colEnd) {
						final TiledMapTileLayer.Cell cell = layer.getCell(col, row);
						if(cell == null) {
							continue;
						}
						final TiledMapTile tile = cell.getTile();
		
						if (tile != null) {
							int nt = tile.getId() % numTilesInEachSet + offset;
							if(map.getTileSets().getTile(nt) != null) {
								newTile = map.getTileSets().getTile(nt);
								cell.setTile(newTile);
							}
						}
					}
				}
			}
		}
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
