package com.balinsbooks.otnt;

import java.awt.Color;

import processing.core.PApplet;

import com.balinsbooks.otnt.utils.FloatPoint;

public class Link {
	String id;
	private String sourceBook;
	int sourceChp;
	private String targetBook;
	int targetChp;
	String type;
	PApplet papplet;

	private static final int DEFAULT_STROKEWEIGHT = 1;
	private static final float QUOTATION_STROKEWEIGHT = 2;
	private static final float ALLUSION_STROKEWEIGHT = 1.5f;
	private static final float POSSIBLEALLUSION_STROKEWEIGHT = 1;
	private static final float UNKNOWN_STROKEWEIGHT = .5f;

	private static final int PROXIMTY_THRESHOLD = 2;

	private FloatPoint startPoint = null;
	private FloatPoint endPoint = null;
	private FloatPoint midPoint1 = null;
	private FloatPoint midPoint2 = null;
	private int color;
	private float weight = DEFAULT_STROKEWEIGHT;
	
	private boolean visible = true;

	boolean pointsSet = false;
	private double bezDist = Double.MAX_VALUE;

	public Link() {
	}

	public Link(String newId, String newSourceBook, int newSourceChp, String newTargetBook, int newTargetChp,
			String newType, PApplet newPapplet) {
		id = newId;
		setSourceBook(newSourceBook);
		sourceChp = newSourceChp;
		setTargetBook(newTargetBook);
		targetChp = newTargetChp;
		type = newType;
		papplet = newPapplet;

		if (type == "q") { // quotation
			this.setColor(Color.RED.getRGB());
			this.setWeight(QUOTATION_STROKEWEIGHT);
		} else if (type == "a") { // allusion
			this.setColor(Color.BLUE.getRGB());
			this.setWeight(ALLUSION_STROKEWEIGHT);
		} else if (type == "p") { // possible allusion
			this.setColor(Color.ORANGE.getRGB());
			this.setWeight(POSSIBLEALLUSION_STROKEWEIGHT);
		} else { // unrecognized type
			this.setColor(Color.LIGHT_GRAY.getRGB());
			this.setWeight(UNKNOWN_STROKEWEIGHT);
		}
	}

	public boolean onLink(PApplet papplet, int x, int y) {
		// Based on http://www.openprocessing.org/visuals/?visualID=12768

		int ndivs = 100;
		float bestDistanceSquared = 0;
//		float bestT = 0;
		for (int i = 0; i <= ndivs; i++) {
			float t = (float) (i) / (float) (ndivs);
			float bx = papplet.bezierPoint(startPoint.getX(), midPoint1.getX(), midPoint2.getX(), endPoint.getX(), t);
			float by = papplet.bezierPoint(startPoint.getY(), midPoint1.getY(), midPoint2.getY(), endPoint.getY(), t);
			float dx = bx - x;
			float dy = by - y;
			float dissq = dx * dx + dy * dy;
			if (i == 0 || dissq < bestDistanceSquared) {
				bestDistanceSquared = dissq;
				bezDist = Math.sqrt(dissq);
//				bestT = t;
			}
		}
		return (Math.round(bezDist) <= PROXIMTY_THRESHOLD);
	}

	public void setSourceBook(String sourceBook) {
		this.sourceBook = sourceBook;
	}

	public String getSourceBook() {
		return sourceBook;
	}

	public void setTargetBook(String targetBook) {
		this.targetBook = targetBook;
	}

	public String getTargetBook() {
		return targetBook;
	}

	void setStartPoint(FloatPoint startPoint) {
		this.startPoint = startPoint;
	}

	FloatPoint getStartPoint() {
		return startPoint;
	}

	void setMidPoint1(FloatPoint midPoint1) {
		this.midPoint1 = midPoint1;
	}

	FloatPoint getMidPoint1() {
		return midPoint1;
	}

	void setMidPoint2(FloatPoint midPoint2) {
		this.midPoint2 = midPoint2;
	}

	FloatPoint getMidPoint2() {
		return midPoint2;
	}

	void setEndPoint(FloatPoint endPoint) {
		this.endPoint = endPoint;
	}

	FloatPoint getEndPoint() {
		return endPoint;
	}

	public void setColor(int color) {
		this.color = color;
	}

	public int getColor() {
		return color;
	}

	public void setWeight(float weight) {
		this.weight = weight;
	}

	public float getWeight() {
		return weight;
	}

	public void setVisible(boolean visible) {
		this.visible = visible;
	}

	public boolean isVisible() {
		return visible;
	}
}
