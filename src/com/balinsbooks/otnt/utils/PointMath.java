package com.balinsbooks.otnt.utils;

public final class PointMath {
	/**
	 * @param x
	 * @param y
	 * @param sngCenterX center coordinate X value
	 * @param sngCenterY center coordinate Y value
	 * @return angle (in degrees)
	 */
	public static final double calcAngle(float x, float y, float sngCenterX, float sngCenterY) {
		double sngAngle = Math.atan((x - sngCenterX) / (sngCenterY - y)) * (180 / Math.PI);
		if ((x > sngCenterX) && (y > sngCenterY)) { // 0-90
			sngAngle = sngAngle + 90;
		} else if ((x < sngCenterX) && (y > sngCenterY)) { // 90-180
			sngAngle = sngAngle + 90;
		} else if ((x < sngCenterX) && (y < sngCenterY)) { // 180-270
			sngAngle = sngAngle + 270;
		} else if ((x > sngCenterX) && (y < sngCenterY)) { // 270-360
			sngAngle = sngAngle + 270;
		}
		return(sngAngle);
	}
}
