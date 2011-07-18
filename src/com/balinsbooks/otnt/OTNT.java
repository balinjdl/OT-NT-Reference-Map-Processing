package com.balinsbooks.otnt;

import java.util.Iterator;

import processing.core.PApplet;
import processing.core.PFont;
import processing.pdf.*;

public class OTNT extends PApplet {
	private static final long serialVersionUID = 8005850003613011975L;

	public boolean debug = false;

	Bible books;
	Iterator<Book> iter;

	float scaler = 1;
	public int cx, cy;
	int bkId = -1;

	PFont font;

	boolean record = false;

	private boolean highlight = false;
	private boolean freeze = false;

	public void setup() {
		// font = createFont("Calibri", 24, true);
		// textFont(font,36);

		debug = true;
		size(1000, 1000);
		background(255);
		smooth();

		books = new Bible(this);
		// debugPrint("chpAngleR = " + chpAngleR + " (deg: " + chpAngleD +
		// "); bookBuffer = " + bookBufferR
		// + " (deg: " + bookBufferD + ")");

		books.draw();

		// String[] fontList = PFont.list();
		// println(fontList);

		debug = false;
	}

	public void draw() {
		if (record) {
			// Note that #### will be replaced with the frame number. Fancy!
			beginRecord(PDF, "frame-####.pdf");
		}

		background(255);

		translate(width / 2, height / 2);
		scale(scaler);
		translate(-width / 2, -height / 2);

		books.draw();

		if (record) {
			endRecord();
			record = false;
		}

		if (!freeze) {
			if (highlight && (bkId = books.inBook(mouseX, mouseY)) >= 0) {
				books.highlight(bkId);
			} else {
				books.unhighlight();
			}
		}

		fill(126);
		textAlign(LEFT);
//		text("mouse loc: " + mouseX + ", " + mouseY + "; inbook? " + books.getName(books.inBook(mouseX, mouseY)), 0, 30);
		int id = books.inBook(mouseX, mouseY);
		String name =books.getName(id); 
		if (name.length() > 0) {
			text(name, 10, 30);
			text("links in/out: " + String.valueOf(books.getLinkCount(id)), 10, 60);
		}		
	}

	public void keyPressed() {
		if (keyCode == PApplet.SHIFT) { // highlight on
			highlight = false;
		}

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
		if (key == 'f') {
			record = true;
			freeze = !freeze;
		}
	}

	public void keyReleased() {
		if (keyCode == PApplet.SHIFT) {
			highlight = true;
		}
	}
}
