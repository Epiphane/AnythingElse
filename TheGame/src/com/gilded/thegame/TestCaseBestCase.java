package com.gilded.thegame;

import com.badlogic.gdx.math.Rectangle;

public class TestCaseBestCase {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		testRectCorners();
	}
	
	public static void testRectCorners() {
		Rectangle rect = new Rectangle(0, 0, 5, 5);
		assert Utility.getCorner(rect, 0) == new PointD(-2.5, -2.5);
		assert Utility.getCorner(rect, 1) == new PointD(2.5, -2.5);
		assert Utility.getCorner(rect, 2) == new PointD(2.5, 2.5);
		assert Utility.getCorner(rect, 3) == new PointD(-2.5, 2.5);
		System.out.println("U DID IT YEY");
	}

}
