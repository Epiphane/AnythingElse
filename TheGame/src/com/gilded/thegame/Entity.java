package com.gilded.thegame;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.MapProperties;

public class Entity extends Sprite {
	public static final float MAX_FALL_SPEED = -0.5f;
	public static final float GRAVITY = -0.015f;

	/** What level am I in? */
	protected Level currentLevel;

	/** Am I on the ground? */
	protected boolean onGround = false, againstLWall = false, againstRWall = false;

	/** Current Location (top left) */
	protected float x;
	protected float y;
	/** Current direction */
	protected float dx, dy;

	/**
	 * "Bounciness" - taken from Metagun, have yet to test whether it helps with
	 * not being in blocks
	 */
	protected double bounce = 0.05;

	/**
	 * Initializes the entity to a specific location.
	 */
	public Entity(int x, int y, TextureRegion texture) {
		super(texture);
		setSize(getWidth() / TheGame.TILE_SIZE, getHeight() / TheGame.TILE_SIZE);
		this.x = x;
		this.y = y;
	}

	/**
	 * Initializes the entity to a specific location.
	 */
	public Entity(int x, int y) {
		this.x = x;
		this.y = y;
	}

	/**
	 * Update me! This function is where we will have all computations such as
	 * AI, walking off, and the like.
	 */
	public void tick() {
		onGround = checkDirection(Input.DOWN);
		againstRWall = checkDirection(Input.RIGHT);
		againstLWall = checkDirection(Input.LEFT);
		
		if (currentLevel == null) // Do nothing if we're not in a world
			return;
	}

	/**
	 * Try to move specified distance.
	 * 
	 * @param dx
	 * @param dy
	 */
	public void tryMove(float dx, float dy) {
		if(Math.abs(dx) < 0.01) dx = 0;

		float w = getWidth();
		float h = getHeight();

		// First, try to move horizontally
		if (currentLevel.canMove(x + dx, y, w, h)) {
			x += dx;
		} else {
			// Slope?
			/*if (currentLevel.canMove(x + dx, y + dy + 0.5f, w, h)) {
				x += dx;
				y += Math.abs(dx);
			}
			// Nope. Definitely a wall
			else {*/ //TODO: remove
				// Hit a wall
				hitWall(dx, dy);
				if(dx != 0 && dy < 0)
					this.dy = 0;
			//}
		}

		// Next, move vertically
		if (currentLevel.canMove(x, y + dy, w, h)) {
			y += dy;
		} else {
			// Hit the wall
			hitWall(dx, dy);
		}
		setPosition(x, y);
	}

	/**
	 * Called when you run into a wall. Basically just "backs you up" until
	 * you're not colliding with the wall anymore.
	 * 
	 * @param dx
	 * @param dy
	 */
	public void hitWall(float dx, float dy) {
		x += dx;
		y += dy;

		while(!currentLevel.canMove(x, y, getWidth(), getHeight())) {
			x -= dx * 0.01;
			y -= dy * 0.01;
		}

		// Now we figure out which part of you is hitting a surface
		//onGround = checkFoot()
	}

	public Level getCurrentLevel() {
		return currentLevel;
	}

	public void setCurrentLevel(Level currentLevel) {
		this.currentLevel = currentLevel;
	}

	public void setPosition(float x, float y) {
		super.setPosition(x, y);
		this.x = x;
		this.y = y;
	}

	/**
	 * Looks in the specified direction for a wall.  If there is one, returns true. Otherwise
	 * returns false.
	 * @param direction Which direction to look for a wall
	 */
	public boolean checkDirection(int direction) {
		MapObject collider = currentLevel.checkFoot(this, direction);
		if(collider != null) {
			
			MapProperties props = collider.getProperties();
			if(props.containsKey("Slope")) {
				int slope = Integer.parseInt((String) props.get("Slope"));
				System.out.println(slope);
				adjustForSlope(slope);
			}
			return true;
		} else {
			return false;
		}
	}
	
	/**
	 * Given a slope, converts this entity's dx and dy so that they appropriately move on the slope.
	 * @param slope The slope value of the slope we're on, in degrees
	 */
	public void adjustForSlope(int slope) {
		int entityAngle = (int) (Math.atan(dy / dx) * 180 / Math.PI);
		entityAngle = (entityAngle + 360) % 360;
		
		// If the player isn't moving towards the slope, we can ignore it safely.
		if(entityAngle > slope && entityAngle < slope + 180) 
			return;
		
		double newSpeed = Math.sin(slope + entityAngle) * (dx + dy);
		newSpeed *= 8;
		dy = (float) (Math.sin(slope) * newSpeed);
		dx = (float) (Math.sin(slope) * newSpeed);
	}
}
