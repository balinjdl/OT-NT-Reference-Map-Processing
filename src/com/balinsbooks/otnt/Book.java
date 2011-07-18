package com.balinsbooks.otnt;

import processing.core.PApplet;

import com.balinsbooks.otnt.utils.FloatPoint;
import com.balinsbooks.otnt.utils.PointMath;

public class Book {
	String bookName;
	String bookTestament;
	int numChapters;
	public int R;
	public int G;
	public int B;
	
	public int strokeWeight = 1;

	float radiusInner;
	FloatPoint startInner, startOuter, endInner, endOuter;
	double startR, endR, arcThickness;

	Book(String name, String testament, int chpCount) {
		bookName = name;
		bookTestament = testament;
		numChapters = chpCount;

		startInner = new FloatPoint();
		endInner = new FloatPoint();
		startOuter = new FloatPoint();
		endOuter = new FloatPoint();
	}

	public boolean containsPoint(float x, float y, float sngCenterX, float sngCenterY) {
		// sngAngle = in degrees
		double sngAngle = PointMath.calcAngle(x, y, sngCenterX, sngCenterY);

		double distanceX = Math.abs(x - sngCenterX);
		double distanceY = Math.abs(y - sngCenterY);
		double distanceFromCenter = Math.sqrt((distanceX * distanceX) + (distanceY * distanceY));

//		System.err.println(bookName + "; sngAngle: " + sngAngle + "; startRdeg: " + Math.toDegrees(startR) + "; endRdeg: " + Math.toDegrees(endR));
		
		//&& (sngAngle >= Math.toDegrees(startR) && sngAngle <= Math.toDegrees(endR))
		if ((distanceFromCenter >= (radiusInner / 2) && distanceFromCenter <= ((radiusInner / 2) + (arcThickness / 2)) 
				&& (sngAngle >= Math.toDegrees(startR) && sngAngle <= Math.toDegrees(endR)))) {
//			System.err.println("containsPoint: yes! " + bookName);
			return (true);
		} else {
			return (false);
		}
	}
}
