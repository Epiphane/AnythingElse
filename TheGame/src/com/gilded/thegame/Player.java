package com.gilded.thegame;


public class Player extends Entity {
	public static final float JUMP_DY = 0.6f;
	public static final float WALKSPEED = TheGame.TILE_SCALE / 16f;
	
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
	
		if(input.buttonStack.peek() == Input.UP) {
			dy = JUMP_DY;
			input.buttonStack.delete(Input.UP);
		}
		
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

		tryMove(dx, dy);
	}
}
