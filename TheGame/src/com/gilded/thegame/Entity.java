package com.gilded.thegame;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.MapProperties;

public class Entity extends Sprite {
	public static final float MAX_FALL_SPEED = -0.5f;
	public static final float GRAVITY = -0.020f;

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
		if (currentLevel == null) // Do nothing if we're not in a world
			return;
		
		onGround = checkDirection(Input.DOWN);
		againstRWall = checkDirection(Input.RIGHT);
		againstLWall = checkDirection(Input.LEFT);
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
			if (currentLevel.canMove(x + dx, y + Math.abs(dx), w, h)) {
				x += dx;
				y += Math.abs(dx);
			}
			// Nope. Definitely a wall
			else { //TODO: remove
				// Hit a wall
				hitWall(dx, dy);
				if(dx != 0 && dy < 0) {
					this.dy = 0;
					//onGround = false;
				}
			}
		}

		// Next, move vertically
		if (currentLevel.canMove(x, y + dy, w, h)) {
			y += dy;
			
			// What if we're above something really close? "Step" down
			// Any slope that's less than a 45 degree drop
			if(dy < 0 && Math.abs(dy) < Math.abs(dx)) {
				if(!currentLevel.canMove(x-dx, y - Math.abs(dx), w, h)) {
					hitWall(0, -Math.abs(dx));
				}
			}
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
		
		// Check to see if we hit something above us
		if(this.dy > 0 && !currentLevel.canMove(x, y + 0.5f, getWidth(), getHeight())) {
			this.dy = 0;
		}
		
		if(dy < 0) {
			this.dy = 0;
			//onGround = true;
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
	
	public Point getCoords() {
		return new Point((int) x, (int) y);
 	}

	/**
	 * Looks in the specified direction for a wall.  If there is one, returns true. Otherwise
	 * returns false.
	 * @param direction Which direction to look for a wall
	 */
	public boolean checkDirection(int direction) {
		System.out.println("Checking " + direction);
		MapObject collider = currentLevel.checkFoot(this, direction);
		if(collider != null) {
			MapProperties props = collider.getProperties();
			if(props.containsKey("Slope")) {
//				int slope = Integer.parseInt((String) props.get("Slope"));
//				adjustForSlope(slope);
				return false;
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
		
		System.out.println("Entity: " + entityAngle);
		
		// If the player isn't moving towards the slope, we can ignore it safely.
		if(entityAngle > slope && entityAngle < slope + 180) 
			return;
		
//		double newSpeed = Math.sin(slope + entityAngle) * (dx + dy);
//		newSpeed *= 8;

		System.out.print("Old: ["+dx+","+dy+"]");
		double newSpeed = Math.sqrt(dx * dx + dy * dy);
		newSpeed *= 5;
		dy = (float) (Math.sin(slope) * newSpeed);
		dx = (float) (Math.cos(slope) * newSpeed);
		
		System.out.println("New: ["+dx+","+dy+"]");
	}
	
	public void changeColor(int color) {
		System.out.println("I changed colors to "+color+"! Who knows what I'm supposed to do!");
	}
}
