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
	public static final float MAX_SCROLL_SPEED = Player.WALKSPEED * TheGame.TILE_SIZE * TheGame.TILE_SCALE;
	
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
	
	public void update() {
		if(focus != null) {
			dx = focus.getX() * TheGame.TILE_SIZE * TheGame.TILE_SCALE - x;
			dy = focus.getY() * TheGame.TILE_SIZE * TheGame.TILE_SCALE - y;
			float scalefx = 0.10f;
			float scalefy = 0.25f;

			if(dx < 3 && dx > -3)
				scalefx = 0.05f;
			
			if(dy < 3 && dx > -3)
				scalefy = 0.15f;
						
			dx *= scalefx;
			dy *= scalefy;

			if(Math.abs(dx) > MAX_SCROLL_SPEED) dx *= MAX_SCROLL_SPEED / Math.abs(dx);
			if(Math.abs(dy) > MAX_SCROLL_SPEED) dy *= MAX_SCROLL_SPEED / Math.abs(dy);

			x += dx;
			y += dy;
		}
		
		position.x = x / (TheGame.TILE_SIZE * TheGame.TILE_SCALE);
		position.y = y / (TheGame.TILE_SIZE * TheGame.TILE_SCALE);
		
		super.update();
	}

	public Sprite getFocus() {
		return focus;
	}

	public void setFocus(Sprite focus, boolean cut) {
		this.focus = focus;
		if(cut) {
			this.x = (int) (focus.getX() * TheGame.TILE_SIZE * TheGame.TILE_SCALE);
			this.y = (int) (focus.getY() * TheGame.TILE_SIZE * TheGame.TILE_SCALE);
		}
	}
}
