/****
 * For whatever reason, the HTML5 compiler can't handle the default java.awt.Point.  
 * So I made my own pretty little Point class.
 */

package com.gilded.thegame;

public class PointD {
	public double x, y;
	
	public PointD(double x, double y) {
		this.x = x;
		this.y = y;
	}
	
	public PointD() {
		this(0, 0);
	}
	
	/** Translates this Point BY the coordinates of the given Point. */
	public void addPoint(PointD adder) {
		x += adder.x;
		y += adder.y;
	}
	
	public void addPoint(double x, double y) {
		this.x += x;
		this.y += y;
	}
	
	/** Multiply both x and y by Z */
	public void mult(double z) {
		x *= z;
		y *= z;
	}
	
	/** This fixes a nasty bug. */
	public boolean equals(Object other) {
		// != if null or not a point
		if(other == null) return false;
		if(!(other instanceof PointD)) return false;
		
		// Compare their x and y values!
		PointD otherp = (PointD) other;
		return otherp.x == x && otherp.y == y;
	}
}

