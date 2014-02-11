package com.gilded.thegame;

import com.badlogic.gdx.graphics.g2d.TextureRegion;


public class Player extends Entity {
	public static final int BASIC = 0;
	public static final int DASHER = 1;
	public static final int PRANCER = 2;
	public static final int FLACCID = 3;
	
	public static int ANIMATION_TIMEPER = 8;
	
	public static final float JUMP_DY = 0.4f;
	public static final float JUMP_DX_OFF_WALL = 0.25f;
	public static final float WALK_SPEED = TheGame.MULTIPLIER_FOR_GOOD_CALCULATIONS / 16f;
	public static final float WALK_ACCELERATION = WALK_SPEED / 16f;
	public static final float FALL_DX_ACCELERATION = WALK_ACCELERATION;
	public static final float WALK_FRICTION = 0.33f;

	private int frame;
	private boolean walking;
	private boolean facingRight;
	
	private int state = BASIC;
	
	/* For allllll the animations */
	public int ticksRemaining;
	
	/* During dashes or stone mode we want to ignore input but still keep it in the stack. */
	public boolean ignoreInput = false;
	
	/* Double jump */
	private boolean doubleJumpReady = true;
	public static final boolean CAN_DOUBLE_JUMP = true;
	
	/* Dashing through the snow... */
	private boolean dashing = false;
	public static final float DASHSPEED = 0.6f;
	public static final int DASH_TICKS = 10;
	public static final float DASH_FRICTION = 0.8f;
	public int dashDirection;
	
	/* Stomp it, stomp it good */
	private boolean stomping = false;
	private boolean preparingStomp = false;
	public static final float STOMPSPEED = 0.8f;
	public static final int STOMP_DELAY = 15;
	
	/* For the glorious wall cling and jump */
	public static final int CLING_TO_WALL_TICKS = 7;
	
	/* Glide stuffz */
	private boolean isGliding = false;
	public static final float glideFallSlowFactor = 0.5f;
	
	public TextureRegion[][] currentSpriteSheet;
	
	public Player(int x, int y) {
		super(x, y, Art.mainCharacter[0][0][0]);
		
		currentSpriteSheet = Art.mainCharacter[0];
	}
	
	/**
	 * Update the player according to the input/level
	 * 
	 * @param input
	 */
	public void tick(Input input)
	{
		super.tick();
		
		// Reset double jump when on ground
		System.out.println("On ground? " + onGround);
		if (onGround)
			doubleJumpReady = true;
	
		// Jump
		if(input.buttonStack.shouldJump()) {
			if(onGround) {
				dy = JUMP_DY;
			} else { 
				if(againstRWall) { // Just hangin' on..
					dy = JUMP_DY;
					dx = -JUMP_DX_OFF_WALL; 
					doubleJumpReady = true;
				} else if(againstLWall) {
					dy = JUMP_DY;
					dx = JUMP_DX_OFF_WALL;
					doubleJumpReady = true;
				} else if(doubleJumpReady && CAN_DOUBLE_JUMP) {
					dy = JUMP_DY;
					doubleJumpReady = false;
				}
			}
		}
		
		// Daaash!
		//TODO: Change to a switch/case based on what form the player is in
		if(input.buttonStack.shouldDash() && !ignoreInput) {
			// Initiate dash based on what directions the player is holding
			Point dir = input.buttonStack.airDirection();
			dashDirection = Utility.directionFromOffset(dir);
			
			dashing = true;
			ignoreInput = true;
			ticksRemaining = DASH_TICKS;
			dx = DASHSPEED * dir.x;
			dy = DASHSPEED * dir.y;

			// Account for our buddy Pythagoras
			if(dx != 0 && dy != 0) {
				dx *= 0.8;
				dy *= 0.8;
			}
		}
		
		// Stommmp!
		//TODO: Implement switch/case based on what form the character is in
		if(input.buttonStack.isStomping()) {
			// Not stomping -> stomping
			if(!stomping && !ignoreInput) {
				stomping = true;
				ignoreInput = true;
			
				dashing = false;
				
				ticksRemaining = STOMP_DELAY;
				preparingStomp = true;
				dx = 0;
				dy = 0;
			} else if(ticksRemaining == 0) {
				preparingStomp = false;
				stomping = true;
				dx = 0;
				dy = -STOMPSPEED;
			} else if(preparingStomp) {
				ticksRemaining--;
				dy = 0;
				dx = 0;
			}
				
		} else {
			// Stomping -> not stomping
			if(stomping) {
				stomping = false;
				ignoreInput = false;
			}
		}
		
		// Handle gravity
		if (!onGround && !dashing) {
			if (dy > MAX_FALL_SPEED)
				dy += GRAVITY;
		}
		// First, set direction we plan to move and do actions
		if(!ignoreInput) {
			if(input.buttonStack.walkDirection() == -1 && dx >= -WALK_SPEED) {
				dx -= onGround ? WALK_ACCELERATION : FALL_DX_ACCELERATION;
				walking = true;
				if(dx < 0)
					facingRight = false;
			}
			else if(input.buttonStack.walkDirection() == 1 && dx <= WALK_SPEED) {
				dx += onGround ? WALK_ACCELERATION : FALL_DX_ACCELERATION;
				walking = true;
				if(dx > 0)
					facingRight = true;
			}
			else if(input.buttonStack.walkDirection() == 0) {
				dx *= WALK_FRICTION;
				walking = false;
			}
		}

//		System.out.print("dx,dy: "+dx+", "+dy+"    ");
		tryMove(dx, dy);
//		System.out.print(againstLWall + " - ");
//		System.out.println(onGround + " when " + dy + " is dy");
		
		// Iterate dashingness
		if(dashing) {
			ticksRemaining--;
			if(ticksRemaining == 0) {
				dashing = false;
				// Slow doooown
				dy *= DASH_FRICTION;
				dx *= DASH_FRICTION;
				
				ignoreInput = false;
			}
		}
		
		if(input.buttonStack.peek() == Input.GLIDE && !onGround) {
		    glide();
		}
		
		// Run the animations
		if(true | walking) {
			if(++frame >= ANIMATION_TIMEPER * 3)
				frame = 0;
		}
		
		// Reset rotation
		this.setRotation(0);
		
		if(dashing) {
			// Draw dashing character
			this.setRegion(currentSpriteSheet[DASH_TICKS - ticksRemaining][2]);
			this.setRotation(Utility.dirToDegree(dashDirection));
		}
		else if(!onGround && againstLWall && dx != 0) {
			// Draw character against wall
			this.setRegion(currentSpriteSheet[CLING_TO_WALL_TICKS - ticksRemaining][1]);
		} else if(!onGround && againstRWall && dx != 0) {
			this.setRegion(currentSpriteSheet[CLING_TO_WALL_TICKS - ticksRemaining][1]);
			this.flip(true, false);
		} else {
			// Draw walking character
			this.setRegion(currentSpriteSheet[frame/ANIMATION_TIMEPER][0]);
			if(!facingRight)
				this.flip(true, false);
			
			if(y < 0) {
				currentLevel.placeCharacter();
			}
		}
	}
	
	private void startAnimation(int animation) {
		
	}
	
	private void glide()
	{
	    isGliding = true;
	    
	    dy *= 0.5f ;
	}

	public void changeColor(int color) {
		if(Art.mainCharacter[color] != null) {
			currentSpriteSheet = Art.mainCharacter[color];
		}
	}
}
