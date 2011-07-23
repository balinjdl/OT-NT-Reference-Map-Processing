package com.balinsbooks.otnt;

import java.awt.Color;

//import processing.core.PApplet;

import com.balinsbooks.otnt.utils.FloatPoint;
import com.balinsbooks.otnt.utils.PointMath;

public class Book {
	String bookName;
	String bookTestament;
	int numChapters;
	public int R;
	public int G;
	public int B;

	private int strokeWeight = 2;
	private int stroke;

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

		if (bookTestament == "OT") {
			setStroke(Color.BLUE.getRGB());
		} else {
			setStroke(Color.GREEN.getRGB());
		}
	}

	public boolean containsPoint(float x, float y, float sngCenterX, float sngCenterY) {
		// sngAngle = in degrees
		double sngAngle = PointMath.calcAngle(x, y, sngCenterX, sngCenterY);

		double distanceX = Math.abs(x - sngCenterX);
		double distanceY = Math.abs(y - sngCenterY);
		double distanceFromCenter = Math.sqrt((distanceX * distanceX) + (distanceY * distanceY));

		// System.err.println(bookName + "; sngAngle: " + sngAngle +
		// "; startRdeg: " + Math.toDegrees(startR) + "; endRdeg: " +
		// Math.toDegrees(endR));

		// && (sngAngle >= Math.toDegrees(startR) && sngAngle <=
		// Math.toDegrees(endR))
		if ((distanceFromCenter >= (radiusInner / 2) && distanceFromCenter <= ((radiusInner / 2) + (arcThickness / 2)) && (sngAngle >= Math
				.toDegrees(startR) && sngAngle <= Math.toDegrees(endR)))) {
			// System.err.println("containsPoint: yes! " + bookName);
			return (true);
		} else {
			return (false);
		}
	}

	public void setStrokeWeight(int strokeWeight) {
		this.strokeWeight = strokeWeight;
	}

	public int getStrokeWeight() {
		return strokeWeight;
	}

	public void setStroke(int stroke) {
		this.stroke = stroke;
	}

	public int getStroke() {
		return stroke;
	}
}
