package com.gilded.thegame;



public class Player extends Entity {
	public static final int BASIC = 0;
	
	public static final float JUMP_DY = 0.3f;
	public static final float WALKSPEED = TheGame.TILE_SCALE / 24f;
	
	private int frame;
	private boolean walking;
	private boolean facingRight;
	
	private int state = BASIC;
	
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
	
		// Jump
		if(input.buttonStack.peek() == Input.UP && onGround) {
			dy = JUMP_DY;
			input.buttonStack.delete(Input.UP);
		}
		
		// First, set direction we plan to move and do actions
		if(input.buttonStack.peek() == Input.LEFT && dx > -WALKSPEED) {
			dx -= WALKSPEED / 10f;
			walking = true;
			if(dx < 0)
				facingRight = false;
		}
		else if(input.buttonStack.peek() == Input.RIGHT && dx < WALKSPEED) {
			dx += WALKSPEED / 10f;
			walking = true;
			if(dx > 0)
				facingRight = true;
		}
		else {
			if(dx > WALKSPEED / 2) dx -= WALKSPEED / 2;
			else if(dx > -WALKSPEED / 4) dx = 0;
			else dx += WALKSPEED / 4;
			walking = false;
		}

		tryMove(dx, dy);
		
		// Run the animations
		if(walking) {
			if(++frame > 8)
				frame = 0;
			
		}
		this.setRegion(Art.mainCharacter[frame/3][0]);
		if(!facingRight)
			this.flip(true, false);
		
		if(y < 0) {
			currentLevel.placeCharacter();
		}
	}
}
