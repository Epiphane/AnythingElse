package com.gilded.thegame;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;

public class Entity extends Sprite {
	/** What level am I in? */
	protected Level currentLevel;
	
	/** Am I on the ground? */
	protected boolean onGround = false;
	
	/** Current Location (top left) */
	private double x, y;
	/** Current direction */
	protected double dx, dy;
	/** Width and height of object */
	private int w, h;
	
	/** "Bounciness" - taken from Metagun, have yet to test whether it helps with not being in blocks */
	protected double bounce = 0.05;
	
	/**
	 * Initializes the entity to a specific location.
	 */
	public Entity(int x, int y, Texture texture) {
		super(texture);
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
	}
	
	/**
	 * Try to move specified distance.
	 * 
	 * @param dx
	 * @param dy
	 */
	public void tryMove(double dx, double dy) {
		onGround = false;
		// First, try to move horizontally
		if(currentLevel.canMove(this, x + dx, y, w, h, dx, 0)) {
			x += dx;
		}
		else {
			// Hit a wall
			hitWall(dx, 0);
			if(dx < 0) {
				double xx = x / TheGame.TILESIZE;
				dx = -(xx - (int) xx) * TheGame.TILESIZE;
			}
			else {
				double xx = (x + w) / TheGame.TILESIZE;
				dx = TheGame.TILESIZE - (xx - (int) xx) / TheGame.TILESIZE;
			}
			dx *= -bounce;
		}
		
		// Next, move vertically
		if(currentLevel.canMove(this, x, y + dy, w, h, 0, dy)) {
			y += dy;
		}
		else {
			// Hit the wall
			hitWall(0, dy);
			if(dy < 0) {
				double yy = y / TheGame.TILESIZE;
				dy = -(yy - (int) yy) * TheGame.TILESIZE;
			}
			else {
				double yy = (y + h) / TheGame.TILESIZE;
				dy = TheGame.TILESIZE - (yy - (int) yy) / TheGame.TILESIZE;
			}
			dy *= -bounce;
		}
	}
	
	/**
	 * Called when you run into a wall
	 * s
	 * @param dx
	 * @param dy
	 */
	public void hitWall(double dx, double dy) {
		if(dx != 0) this.dx = 0;
		if(dy != 0) this.dy = 0;
	}

	public Level getCurrentLevel() {
		return currentLevel;
	}
	public void setCurrentLevel(Level currentLevel) {
		this.currentLevel = currentLevel;
	}
}
