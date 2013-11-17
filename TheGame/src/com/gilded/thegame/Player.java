package com.gilded.thegame;


public class Player extends Entity {
	public static final float WALKSPEED = TheGame.TILE_SCALE / 32f;
	
	public Player(int x, int y) {
		super(x, y, Art.mainCharacter[0][0]);
	}
	
	/**
	 * Update the player according to the input/level
	 * 
	 * @param input
	 */
	public void tick(Input input) {
		super.tick();
		
		// First, set direction we plan to move and do actions
		if(input.buttonStack.peek() == Input.LEFT) {
			dx = -WALKSPEED;
		}
		else if(input.buttonStack.peek() == Input.RIGHT) {
			dx = WALKSPEED;
		}
		else {
			dx = 0;
		}
	
		if(input.buttonStack.peek() == Input.UP) {
			dy = 0.5f;
		}

		tryMove(dx, dy);
	}
}
