package com.gilded.thegame;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

public class Entity extends Sprite {
	/** What level am I in? */
	protected Level currentLevel;
	
	/** Am I on the ground? */
	protected boolean onGround = false;
	
	/** Current Location (top left) */
	private float x, y;
	/** Current direction */
	protected float dx, dy;
	
	/** "Bounciness" - taken from Metagun, have yet to test whether it helps with not being in blocks */
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
	 * Update me!
	 * This function is where we will have all computations such as AI, wanking off, and the like.
	 */
	public void tick() {
		if(currentLevel == null) // Do nothing if we're not in a world
			return;
		
		if(!onGround) {
			if(dy > -0.1f) dy -= 0.1f;
			tryMove(dx,dy);
		}
	}
	
	/**
	 * Try to move specified distance.
	 * 
	 * @param dx
	 * @param dy
	 */
	public void tryMove(float dx, float dy) {
		float w = getWidth();
		float h = getHeight();
		onGround = false;
		// First, try to move horizontally
		if(currentLevel.canMove(this, x + dx, y, w, h, dx, 0)) {
			x += dx;
		}
		else {
			// Slope?
			if(currentLevel.canMove(this, x + dx, y + dx + 1, w, h , dx, 0)) {
				x += dx;
				y += Math.abs(dx);
			}
			// Nope. Definitely a wall
			else {
				// Hit a wall
				hitWall(dx, 0);
			}
		}
		
		// Next, move vertically
		if(currentLevel.canMove(this, x, y + dy, w, h, 0f, dy)) {
			y += dy;
		}
		else {
			if(dy > 0) onGround = true;
			// Hit the wall
			hitWall(0, dy);
		}
		setPosition(x, y);
	}
	
	/**
	 * Called when you run into a wall
	 * s
	 * @param dx
	 * @param dy
	 */
	public void hitWall(float dx, float dy) {
		if(dx != 0) this.dx = 0;
		if(dy != 0) this.dy = 0;
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
}
