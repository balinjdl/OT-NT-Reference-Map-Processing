package com.balinsbooks.otnt.utils;

public class FloatPoint {
	float x, y;

	public FloatPoint() {
	}

	public FloatPoint(float newX, float newY) {
		x = newX;
		y = newY;
	}

	public float getX() {
		return x;
	}

	public void setX(float x) {
		if (this.x != x) {
//			System.err.println("new x for FloatPoint: " + x);
			this.x = x;
		}
	}

	public float getY() {
		return y;
	}

	public void setY(float y) {
		if (this.y != y) {
//			System.err.println("new y for FloatPoint: " + y);
			this.y = y;
		}
	}
}
