package com.balinsbooks.otnt;

import java.util.Iterator;

import processing.core.PApplet;
import processing.core.PFont;

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

	public void setup() {
		// font = createFont("Calibri", 24, true);
		// textFont(font,36);

		debug = false;
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

		if (!isFrozen()) {

			if (books.highlight && (bkId = books.inBook()) >= 0) {
				books.highlight(bkId);
				// } else {
				// if (!books.inArc()) {
				// books.unhighlight();
				// }
			}

			if (books.withinArc()) {
				if (books.highlight) {
					books.highlightLinks();
				}
			}
		}

		if (record) {
			endRecord();
			record = false;
		}

		fill(100);
		textAlign(LEFT);
		// text("mouse loc: " + mouseX + ", " + mouseY + "; inbook? " +
		// books.getName(books.inBook(mouseX, mouseY)), 0, 30);
		int id = books.inBook();
		String name = books.getName(id);
		if (books.frozen) {
			text("Frozen", 10, 40);
		}
		if (books.highlight) {
			text("Highlight mode on", 10, 60);
		}
		if (name.length() > 0) {
			text("In book: " + name, 10, 20);
			text("# Links in/out: " + String.valueOf(books.getLinkCount(id)), 10, 80);
		}
		if (books.showQuotationLinks) {
			text("Showing quotations", 10,100);
		} else {
			text("NOT showing quotations", 10,100);
		}
		if (books.showAllusionLinks) {
			text("Showing allusions", 10,120);
		} else {
			text("NOT showing allusions", 10,120);
		}
		if (books.showPossibleAllusionLinks) {
			text("Showing possible allusions", 10,140);
		} else {
			text("NOT showing possible allusions", 10,140);
		}
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
		if (key == 'f') {
			// record = true;
			freeze(!isFrozen());
		}
		if (key == 'h') {
			books.highlight = !books.highlight;
		}
		
		if (key == 'p') { 
			books.showPossibleAllusionLinks = !books.showPossibleAllusionLinks;
		}
		
		if (key == 'a') { 
			books.showAllusionLinks = !books.showAllusionLinks;
		}
		
		if (key == 'q') { 
			books.showQuotationLinks = !books.showQuotationLinks;
		}
	}

	public void keyReleased() {
	}

	protected void freeze(boolean freeze) {
		books.frozen = freeze;
	}

	public boolean isFrozen() {
		return books.frozen;
	}

}
