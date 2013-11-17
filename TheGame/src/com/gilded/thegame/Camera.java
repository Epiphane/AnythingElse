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
	public static final float MAX_SCROLL_SPEED = Player.WALKSPEED;
	
	private Sprite focus;
	private float dx, dy;
	
	/**
	 * Initializes the camera like a default OrthographicCamera
	 * 
	 * @param gameWidth
	 * @param gameHeight
	 */
	public Camera(float gameWidth, float gameHeight) {
		super(gameWidth, gameHeight);
	}
	
	public void update() {
		if(focus != null) {
			dx = (focus.getX() - position.x) * TheGame.TILE_SIZE;
			dy = (focus.getY() - position.y);
			float scalefx = 0.10f;
			float scalefy = 0.25f;

			if(dx < 8 && dx > -8)
				scalefx = 0.05f;
			
			if(dy < 6 && dx > -6)
				scalefy = 0.15f;
			
			dx *= scalefx;
			dy *= scalefy;
			
			if(Math.abs(dx) > MAX_SCROLL_SPEED) dx *= MAX_SCROLL_SPEED / Math.abs(dx);
			if(Math.abs(dy) > MAX_SCROLL_SPEED) dy *= MAX_SCROLL_SPEED / Math.abs(dy);

			position.x += dx;
			position.y += dy;
		}
		
		super.update();
	}

	public Sprite getFocus() {
		return focus;
	}

	public void setFocus(Sprite focus, boolean cut) {
		this.focus = focus;
		if(cut) {
			this.position.x = focus.getX();
			this.position.y = focus.getY();
		}
	}
}
