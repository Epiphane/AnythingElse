package com.gilded.thegame;

public class Player extends Entity {
	public static final int BASIC = 0;
	
	public static final float JUMP_DY = 0.3f;
	public static final float WALKSPEED = TheGame.TILE_SCALE / 24f;
	
	private int frame;
	private boolean walking;
	private boolean facingRight;
	
	private int state = BASIC;
	
	/* Dashing through the snow... */
	private boolean dashing = false;
	public static final float DASHSPEED = 0.5f;
	public static final int DASH_TICKS = 10;
	public static final float DASH_FRICTION = 0.9f;
	public int dashTicksRemaining;
	
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
		if(input.buttonStack.shouldJump() && onGround) {
			dy = JUMP_DY;
		}
		
		// Daaash!
		//TODO: Change to a switch/case based on what form the player is in
		if(input.buttonStack.peek() == Input.DASH && !dashing) {
			// Initiate dash based on what directions the player is holding
			Point dir = input.buttonStack.airDirection();

			dashing = true;
			dashTicksRemaining = DASH_TICKS;
			dx = DASHSPEED * dir.x;
			dy = DASHSPEED * dir.y;
		}
		
		// Handle gravity
		if (!onGround && !dashing) {
			if (dy > MAX_FALL_SPEED)
				dy += GRAVITY;
		}
		
		// First, set direction we plan to move and do actions
		if(!dashing) {
			if(input.buttonStack.walkDirection() == -1 && dx > -WALKSPEED) {
				dx -= WALKSPEED / 10f;
				walking = true;
				if(dx < 0)
					facingRight = false;
			}
			else if(input.buttonStack.walkDirection() == 1 && dx < WALKSPEED) {
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
		}

		tryMove(dx, dy);
		
		// Iterate dashingness
		if(dashing) {
			dashTicksRemaining--;
			if(dashTicksRemaining == 0) {
				dashing = false;
				// Slow doooown
				dy *= DASH_FRICTION;
				dx *= DASH_FRICTION;
			}
		}
		
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
