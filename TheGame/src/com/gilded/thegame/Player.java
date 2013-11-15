package com.gilded.thegame;

public class Player extends Entity {
	public Player(int x, int y) {
		super(x, y);
		
		this.setTexture(Art.mainCharacter[0][0]);
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
			dx = 1;
		}
		else if(input.buttonStack.peek() == Input.RIGHT) {
			dx = -1;
		}
		else {
			dx = 0;
		}
		
		tryMove(dx, dy);
	}
}
