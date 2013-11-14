package com.gilded.thegame;

import com.badlogic.gdx.graphics.g2d.Sprite;

public class Entity extends Sprite {
	private int x, y;
	private int dx, dy;
	
	/**
	 * Initializes the entity to a specific location.
	 */
	public Entity(int x, int y) {
		this.x = x;
		this.y = y;
	}
	
	public void render() {
		
	}
}
