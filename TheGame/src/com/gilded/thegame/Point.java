/****
 * For whatever reason, the HTML5 compiler can't handle the default java.awt.Point.  
 * So I made my own pretty little Point class.
 */

package com.gilded.thegame;

public class Point {
	public int x, y;
	
	public Point(int x, int y) {
		this.x = x;
		this.y = y;
	}
	
	public Point() {
		this(0, 0);
	}
	
	/** Translates this Point BY the coordinates of the given Point. */
	public void addPoint(Point adder) {
		x += adder.x;
		y += adder.y;
	}
}

