package com.balinsbooks.otnt;

import java.awt.Point;
import java.util.Iterator;

import processing.core.PApplet;

public class OTNT extends PApplet {
	private static final long serialVersionUID = 8005850003613011975L;

	public boolean debug = false;

	Bible books;
	Iterator<Book> iter;

	float scaler = 1;

	public int cx, cy;
	
	public void setup() {
		debug = true;
		size(1000, 1000);
		background(255);
		smooth();

		books = new Bible(this);
//		debugPrint("chpAngleR = " + chpAngleR + " (deg: " + chpAngleD + "); bookBuffer = " + bookBufferR
//				+ " (deg: " + bookBufferD + ")");

		books.draw();
		
		debug = false;
	}

	public void draw() {
		background(255);

		translate(width / 2, height / 2);
		scale(scaler);
		translate(-width / 2, -height / 2);

		books.draw();
		
	}

	public void keyPressed() {
		if (key == '-') {
			scaler -= 0.1;
		}
		if (key == '+') {
			scaler += 0.1;
		}
		if (key == '>') {
			books.cx = books.cx + 10;
		}
		if (key == '<') {
			books.cx = books.cx - 10;
		}
		if (key == 'i') {
			books.cy = books.cy - 10;
		}
		if (key == 'k') {
			books.cy = books.cy + 10;
		}
		if (key == 'c') {
			scaler = 1;
			books.cx = width / 2;
			books.cy = height / 2;
		}
	}
}
