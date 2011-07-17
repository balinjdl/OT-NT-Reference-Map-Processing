package com.balinsbooks.otnt;

import java.awt.Point;
import java.util.ArrayList;

import processing.core.PApplet;

public class Bible {
	ArrayList<Book> books;

	double finishAngleR, startAngleR, lastStartAngleR;
	static final int numBooks = 66;
	static final int totalChapterCount = 1189; // TODO: Calculate?
	static double bookArcWidth = 0;
	int chpCount = 0;

	static final double bookBufferD = .6;
	double bookBufferR = toRad(bookBufferD);

	double chpAngleD = (360 - (bookBufferD * numBooks)) / totalChapterCount;;
	double chpAngleR = toRad(chpAngleD);

	float rInner = 400;
	float rOuter = 420;
	float arcThickness = 90;

	int cx, cy;

	boolean debug = true;

	PApplet papplet;

	Point startPt, endPt;
	float startX, endX, startY, endY;

	Bible(PApplet papplet) {
		this.papplet = papplet;
		papplet.strokeWeight(.1f);
		cx = papplet.width / 2;
		cy = papplet.height / 2;

		rInner = Math.min(papplet.width, papplet.height) * 0.8f;
		rOuter = rInner + arcThickness;
		
		setupBookList();
	}

	Bible(PApplet papplet, boolean debugOn) {
		this.papplet = papplet;
		cx = papplet.width / 2;
		cy = papplet.height / 2;

		debug = debugOn;
		setupBookList();
	}

	private void setupBookList() {
		books = new ArrayList<Book>();
		books.add(new Book("Gen", "OT", 50));
		books.add(new Book("Exo", "OT", 40));
		books.add(new Book("Lev", "OT", 27));
		books.add(new Book("Num", "OT", 36));
		books.add(new Book("Deu", "OT", 34));
		books.add(new Book("Jos", "OT", 24));
		books.add(new Book("Jdg", "OT", 21));
		books.add(new Book("Rut", "OT", 4));
		books.add(new Book("1Sa", "OT", 31));
		books.add(new Book("2Sa", "OT", 24));
		books.add(new Book("1Ki", "OT", 22));
		books.add(new Book("2Ki", "OT", 25));
		books.add(new Book("1Ch", "OT", 29));
		books.add(new Book("2Ch", "OT", 36));
		books.add(new Book("Ezr", "OT", 10));
		books.add(new Book("Neh", "OT", 13));
		books.add(new Book("Est", "OT", 10));
		books.add(new Book("Job", "OT", 42));
		books.add(new Book("Psa", "OT", 150));
		books.add(new Book("Pro", "OT", 31));
		books.add(new Book("Ecc", "OT", 12));
		books.add(new Book("Sol", "OT", 8));
		books.add(new Book("Isa", "OT", 66));
		books.add(new Book("Jer", "OT", 52));
		books.add(new Book("Lam", "OT", 5));
		books.add(new Book("Eze", "OT", 48));
		books.add(new Book("Dan", "OT", 12));
		books.add(new Book("Hos", "OT", 14));
		books.add(new Book("Joe", "OT", 3));
		books.add(new Book("Amo", "OT", 9));
		books.add(new Book("Oba", "OT", 1));
		books.add(new Book("Jon", "OT", 4));
		books.add(new Book("Mic", "OT", 7));
		books.add(new Book("Nah", "OT", 3));
		books.add(new Book("Hab", "OT", 3));
		books.add(new Book("Zep", "OT", 3));
		books.add(new Book("Hag", "OT", 2));
		books.add(new Book("Zec", "OT", 14));
		books.add(new Book("Mal", "OT", 4));
		books.add(new Book("Mt", "NT", 28));
		books.add(new Book("Mr", "NT", 16));
		books.add(new Book("Lu", "NT", 24));
		books.add(new Book("Joh", "NT", 21));
		books.add(new Book("Ac", "NT", 28));
		books.add(new Book("Ro", "NT", 16));
		books.add(new Book("1Co", "NT", 16));
		books.add(new Book("2Co", "NT", 13));
		books.add(new Book("Ga", "NT", 6));
		books.add(new Book("Eph", "NT", 6));
		books.add(new Book("Php", "NT", 4));
		books.add(new Book("Col", "NT", 4));
		books.add(new Book("1Th", "NT", 5));
		books.add(new Book("2Th", "NT", 3));
		books.add(new Book("1Ti", "NT", 6));
		books.add(new Book("2Ti", "NT", 4));
		books.add(new Book("Tit", "NT", 3));
		books.add(new Book("Phm", "NT", 1));
		books.add(new Book("Heb", "NT", 13));
		books.add(new Book("Jas", "NT", 5));
		books.add(new Book("1Pe", "NT", 5));
		books.add(new Book("2Pe", "NT", 3));
		books.add(new Book("1Jo", "NT", 5));
		books.add(new Book("2Jo", "NT", 1));
		books.add(new Book("3Jo", "NT", 1));
		books.add(new Book("Jude", "NT", 1));
		books.add(new Book("Re", "NT", 22));
	}

	public void draw() {
		papplet.noFill();
		papplet.stroke(125);
		for (int i = 0; i < books.size(); i++) { // books.size()
			debugPrintln("i: " + i);
			finishAngleR = lastStartAngleR + toRad(chpAngleD * books.get(i).numChapters);
			// debugPrint("finishAngleR set to " + toDeg(finishAngleR));

			// Trying with individual X/Y
			startX = getXf(rInner, lastStartAngleR);
			startY = getYf(rInner, lastStartAngleR);
			endX = getXf(rOuter, lastStartAngleR);
			endY = getYf(rOuter, lastStartAngleR);
			papplet.line(startX, startY, endX, endY);

			startX = getXf(rInner, finishAngleR);
			startY = getYf(rInner, finishAngleR);
			endX = getXf(rOuter, finishAngleR);
			endY = getYf(rOuter, finishAngleR);
			papplet.line(startX, startY, endX, endY);

//			debugPrintln("  cx: " + cx + "; cy: " + cy + "; rInner: " +  rInner + "; lastStartAngleR: " + lastStartAngleR + " (float: " + (float) (lastStartAngleR) + "); finishAngleR: " + finishAngleR + " (float: " + (float) (finishAngleR) + ")");
			papplet.arc(cx, cy, rInner, rInner, (float) (lastStartAngleR), (float) (finishAngleR));
			papplet.arc(cx, cy, rOuter, rOuter, (float) (lastStartAngleR), (float) (finishAngleR));

//			debugPrintln("  startpt: " + startPt.x + "," + startPt.y + "; endPt: " + endPt.x + "," + endPt.y);
			debugPrint("; chpCount: " + books.get(i).numChapters + "; old lastStartAngle = " + toDeg(lastStartAngleR));
			lastStartAngleR = finishAngleR + bookBufferR;
			debugPrintln("; new lastStartAngleD = " + toDeg(lastStartAngleR));
		}
		lastStartAngleR = 0;

		debug = false; // only debug for one cycle
	}

	private double toRad(double deg) {
		return (deg * (2 * Math.PI / 360));
	}

	private double toDeg(double rad) {
		return (rad * (360 / (2 * Math.PI)));
	}

	public Point getPoint(float radius, double rad) {
		double x, y;
		Point pt = new Point();
		debugPrint("getPoint(" + radius + ", " + rad + ") called...");
		x = getX(radius, rad);
		y = getY(radius, rad);
		pt.setLocation(x, y);
		debugPrintln("...getPoint returning: " + pt.getX() + ", " + pt.getY());
		return (pt);
	}

	public float getXf(float radius, double rad) {
		float ax = (float) (cx + ((radius/2) * (Math.cos((rad)))));
		debugPrintln("  getX(" + (radius/2) + ", " + rad + ") = " + ax);
		return ax;
	}

	public float getYf(float radius, double rad) {
		float ay = (float) (cy + ((radius/2) * (Math.sin((rad)))));
		debugPrintln("  getY(" + (radius/2) + ", " + rad + ") = " + ay + "; ");
		return ay;
	}

	public double getX(float radius, double rad) {
		double ax = (cx + ((radius/2) * (Math.cos((rad)))));
		debugPrintln("  getX(" + (radius/2) + ", " + rad + ") = " + ax);
		return ax;
	}

	public double getY(float radius, double rad) {
		double ay = (cy + ((radius/2) * (Math.sin((rad)))));
		debugPrintln("  getY(" + (radius/2) + ", " + rad + ") = " + ay + "; ");
		return ay;
	}

	private void drawLine(Point pt1, Point pt2) {
		debugPrintln("drawLine called; returning (float): " + (float) pt1.getX() + "," + (float) pt1.getY() + ","
				+ (float) pt2.getX() + "," + (float) pt2.getY());
		debugPrint("  drawLine called; returning: " + pt1.getX() + "," + pt1.getY() + "," + pt2.getX() + "," + pt2.getY());
		papplet.line((float) pt1.getX(), (float) pt1.getY(), (float) pt2.getX(), (float) pt2.getY());
	}

	private void debugPrintln(String msg) {
		if (debug) {
			System.err.println(msg);
		}
	}

	private void debugPrint(String msg) {
		if (debug) {
			System.err.print(msg);
		}
	}

}
