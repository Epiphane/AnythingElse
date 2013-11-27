package com.gilded.thegame;

import java.awt.Point;

public class Utility {
	/**
	 * Converts from a point-offset to a direction integer
	 * @param offset The x and y of the direction
	 * @return The int value of the direction (0-7)
	 */
	public static int directionFromOffset(Point offset) {
		offset.x = sign(offset.x);
		offset.y = sign(offset.y);
		
		if(offset.equals(new Point(0, -1))) {
			return 0;
		} else if(offset.equals(new Point(1, -1))) {
			return 1;
		} else if(offset.equals(new Point(1, 0))) {
			return 2;
		} else if(offset.equals(new Point(1, 1))) {
			return 3;
		} else if(offset.equals(new Point(0, 1))) {
			return 4;
		} else if(offset.equals(new Point(-1, 1))) {
			return 5;
		} else if(offset.equals(new Point(-1, 0))) {
			return 6;
		} else if(offset.equals(new Point(-1 ,-1))) {
			return 7;
		} else {
			// PANIC EVERYTHING IS BROKEN
			System.out.println("Invalid point... somehow?? Values: " 
				+ offset.x + ", " + offset.y);
		}
		
		return -1;
	}
	
	/** Returns the point-offset of a given int-direction */
	public static Point offsetFromDirection(int direction) {
		switch(direction) {
		case 0:
			return new Point(0, -1);
		case 1:
			return new Point(1, -1);
		case 2:
			return new Point(1, 0);
		case 3:
			return new Point(1, 1);
		case 4:
			return new Point(0, 1);
		case 5:
			return new Point(-1, 1);
		case 6:
			return new Point(-1, 0);
		case 7:
			return new Point(-1, -1);
		default:
			System.out.println("What just happened??? Tried to get offset from direction: " + direction);
			break;
		}
		
		return null;
	}
	
	/** Returns -1, 0, or 1 based on the sign of x */
	public static int sign(double x) {
		if(x < 0) return -1;
		if(x > 0) return 1;
		return 0;
	}
	
	public static int sign(int x) {
		return sign(x);
	}
}
