package com.gilded.thegame;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.Sprite;

/**
 * Extends the OrthographicCamera class and adds the ability to scroll when the player moves far enough across the screen
 * 
 * @author Thomas
 *
 */
public class Camera extends OrthographicCamera {
	public static final float LEEWAY_X = 60;
	public static final float MAX_SCROLL_SPEED_X = Player.WALK_SPEED * TheGame.TILE_SIZE * TheGame.TILE_SCALE * 10;
	public static final float MAX_SCROLL_SPEED_Y = -Entity.MAX_FALL_SPEED * TheGame.TILE_SIZE * TheGame.TILE_SCALE * 10;
	
	private boolean rushing = false;
	
	private Sprite focus;
	private float dx, dy;
	private int x, y;
	
	/**
	 * Initializes the camera like a default OrthographicCamera
	 * 
	 * @param gameWidth
	 * @param gameHeight
	 */
	public Camera(float gameWidth, float gameHeight) {
		super(gameWidth, gameHeight);
		
		x = (int) position.x;
		y = (int) position.y;
	}
	
	public void update(int levelWidth, int levelHeight) {
		if(focus != null) {
			dx = focus.getX() * TheGame.TILE_SIZE * TheGame.TILE_SCALE - x;
			
			// Account for leeway. Essentially, don't move if the player is just in
			// the middle area-ish
			if(dx > 0) {
				dx -= LEEWAY_X;
				if(dx < 0) dx = 0;
			}
			else {
				dx += LEEWAY_X;
				if(dx > 0) dx = 0;
			}
			dy = focus.getY() * TheGame.TILE_SIZE * TheGame.TILE_SCALE - y;
			float scalefx = 0.050f;
			float scalefy = 0.125f;

			if(dx < 1 && dx > -1)
				scalefx = 0.025f;
			
			if(dy < 1 && dx > -1)
				scalefy = 0.075f;
						
			dx *= scalefx;
			dy *= scalefy;

			if(!rushing) {
				if(Math.abs(dx) > MAX_SCROLL_SPEED_X) dx *= MAX_SCROLL_SPEED_X / Math.abs(dx);
				if(Math.abs(dy) > MAX_SCROLL_SPEED_Y) dy *= MAX_SCROLL_SPEED_Y / Math.abs(dy);
			}
				
			x += dx;
			y += dy;
			
			if(dx <= MAX_SCROLL_SPEED_X || dy <= MAX_SCROLL_SPEED_Y) rushing = false;
		}
		
		// Check for out of bounds
		if(x - TheGame.GAME_WIDTH / 2 < 0) x = TheGame.GAME_WIDTH / 2;
		if((x + TheGame.GAME_WIDTH / 2) / (TheGame.TILE_SIZE * TheGame.TILE_SCALE) > levelWidth) x = (int) (levelWidth * TheGame.TILE_SCALE * TheGame.TILE_SIZE) - TheGame.GAME_WIDTH / 2;
		if(y - TheGame.GAME_HEIGHT / 2 < 0) y = TheGame.GAME_HEIGHT / 2;
		if((y + TheGame.GAME_HEIGHT / 2) / (TheGame.TILE_SIZE * TheGame.TILE_SCALE) > levelHeight) y = (int) (levelHeight * TheGame.TILE_SCALE * TheGame.TILE_SIZE) - TheGame.GAME_HEIGHT / 2;
		
		position.x = x / (TheGame.TILE_SIZE * TheGame.TILE_SCALE);
		position.y = y / (TheGame.TILE_SIZE * TheGame.TILE_SCALE);

		super.update();
	}

	public Sprite getFocus() {
		return focus;
	}
	
	public void rush() {
		rushing = true;
	}

	public void setFocus(Sprite focus, boolean cut) {
		this.focus = focus;
		if(cut) {
			this.x = (int) (focus.getX() * TheGame.TILE_SIZE * TheGame.TILE_SCALE);
			this.y = (int) (focus.getY() * TheGame.TILE_SIZE * TheGame.TILE_SCALE);
			position.x = x / (TheGame.TILE_SIZE * TheGame.TILE_SCALE);
			position.y = y / (TheGame.TILE_SIZE * TheGame.TILE_SCALE);

			rushing = false;
		}
	}
}
