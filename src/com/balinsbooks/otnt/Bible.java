package com.balinsbooks.otnt;

import java.awt.Color;
import java.awt.Point;
import java.util.ArrayList;
import java.util.Iterator;

import com.balinsbooks.otnt.utils.FloatPoint;

import processing.core.PApplet;

public class Bible {
	ArrayList<Book> books;
	ArrayList<Link> links;

	int hoverBook = -1;
	boolean highlight = false;

	double finishAngleR, startAngleR, lastStartAngleR;
	static final int numBooks = 66;
	static final int totalChapterCount = 1189; // TODO: Calculate?
	static double bookArcWidth = 0;
	int chpCount = 0;

	static final double bookBufferD = .6;

	private static final int DEFAULT_STROKEWEIGHT = 1;
	private static final int HIGHLIGHT_STROKEWEIGHT = 3;
	private static final float QUOTATION_STROKEWEIGHT = 3;
	private static final float ALLUSION_STROKEWEIGHT = 2;
	private static final float POSSIBLEALLUSION_STROKEWEIGHT = 1;
	private static final float UNKNOWN_STROKEWEIGHT = 0;

	double bookBufferR = toRad(bookBufferD);

	double chpAngleD = (360 - (bookBufferD * numBooks)) / totalChapterCount;;
	double chpAngleR = toRad(chpAngleD);

	float rInner = 400;
	float rOuter = 420;
	float arcThickness = 90;

	int cx, cy;

	boolean debug = true;

	Book book;
	PApplet papplet;

	Point startPt, endPt;
	float startX, endX, startY, endY, startDeg, endDeg;

	Bible(PApplet papplet) {
		this.papplet = papplet;
		papplet.strokeWeight(.1f);
		cx = papplet.width / 2;
		cy = papplet.height / 2;

		rInner = Math.min(papplet.width, papplet.height) * 0.8f;
		rOuter = rInner + arcThickness;

		setupBookList();
		setupLinks();
	}

	Bible(PApplet papplet, boolean debugOn) {
		this.papplet = papplet;
		cx = papplet.width / 2;
		cy = papplet.height / 2;

		debug = debugOn;
		setupBookList();
		setupLinks();
	}

	private void setupBookList() {
		books = new ArrayList<Book>();
		books.add(new Book("Gen", "OT", 50));
		books.add(new Book("Exo", "OT", 40));
		books.add(new Book("Lev", "OT", 27));
		books.add(new Book("Num", "OT", 36));
		books.add(new Book("Deu", "OT", 34));
		books.add(new Book("Jos", "OT", 24));
		books.add(new Book("Jud", "OT", 21));
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

	private void drawBooks() {
		for (int i = 0; i < books.size(); i++) { // books.size()
//			debugPrintln("i: " + i);

			book = books.get(i);

			papplet.strokeWeight(book.strokeWeight);
			if (book.bookTestament == "OT") {
				papplet.stroke(Color.BLUE.getRGB());
			} else {
				papplet.stroke(Color.GREEN.getRGB());
			}

			finishAngleR = lastStartAngleR + toRad(chpAngleD * books.get(i).numChapters);
//			debugPrint("finishAngleR set to " + toDeg(finishAngleR));
			book.startR = lastStartAngleR;
			book.endR = finishAngleR;

			books.get(i).radiusInner = rInner;
			books.get(i).arcThickness = arcThickness;

			startX = getXf(rInner, lastStartAngleR);
			startY = getYf(rInner, lastStartAngleR);
			endX = getXf(rOuter, lastStartAngleR);
			endY = getYf(rOuter, lastStartAngleR);

			books.get(i).startInner.setX(startX);
			books.get(i).startInner.setY(startY);
			books.get(i).startOuter.setX(endX);
			books.get(i).startOuter.setY(endY);

			papplet.line(startX, startY, endX, endY);

			startX = getXf(rInner, finishAngleR);
			startY = getYf(rInner, finishAngleR);
			endX = getXf(rOuter, finishAngleR);
			endY = getYf(rOuter, finishAngleR);

			books.get(i).endInner.setX(startX);
			books.get(i).endInner.setY(startY);
			books.get(i).endOuter.setX(endX);
			books.get(i).endOuter.setY(endY);

			papplet.line(startX, startY, endX, endY);

			papplet.arc(cx, cy, rInner, rInner, (float) (lastStartAngleR), (float) (finishAngleR));
			papplet.arc(cx, cy, rOuter, rOuter, (float) (lastStartAngleR), (float) (finishAngleR));

//			debugPrint("; chpCount: " + books.get(i).numChapters + "; old lastStartAngle = " + toDeg(lastStartAngleR));
			lastStartAngleR = finishAngleR + bookBufferR;
//			debugPrintln("; new lastStartAngleD = " + toDeg(lastStartAngleR));
		}
		lastStartAngleR = 0;
		papplet.stroke(Color.GRAY.getRGB());
	}

	private void setupLinks() {
		links = new ArrayList<Link>();
		// Possible
		links.add(new Link("OTNT519", "2Th", 2, "Isa", 11, "p"));
		links.add(new Link("OTNT520", "1Ti", 2, "Gen", 1, "p"));
		links.add(new Link("OTNT527", "1Ti", 6, "Psa", 49, "p"));
		links.add(new Link("OTNT545", "Heb", 3, "Num", 14, "p"));
		links.add(new Link("OTNT549", "Heb", 5, "1Ch", 23, "p"));
		links.add(new Link("OTNT559", "Heb", 9, "Exo", 40, "p"));
		links.add(new Link("OTNT563", "Heb", 9, "Num", 14, "p"));
		links.add(new Link("OTNT569", "Heb", 10, "Isa", 64, "p"));
		links.add(new Link("OTNT582", "Heb", 11, "Gen", 47, "p"));
		links.add(new Link("OTNT583", "Heb", 11, "Psa", 39, "p"));
		links.add(new Link("OTNT585", "Heb", 11, "Hos", 14, "p"));
		links.add(new Link("OTNT601", "Heb", 11, "1Sa", 7, "p"));
		links.add(new Link("OTNT602", "Heb", 11, "2Sa", 2, "p"));
		links.add(new Link("OTNT603", "Heb", 11, "2Sa", 8, "p"));
		links.add(new Link("OTNT604", "Heb", 11, "Jud", 14, "p"));
		links.add(new Link("OTNT605", "Heb", 11, "Dan", 6, "p"));
		links.add(new Link("OTNT606", "Heb", 11, "Dan", 3, "p"));
		links.add(new Link("OTNT611", "Heb", 12, "Num", 27, "p"));
		links.add(new Link("OTNT631", "Jas", 1, "Pro", 17, "p"));
		links.add(new Link("OTNT641", "Jas", 5, "Pro", 16, "p"));
		links.add(new Link("OTNT648", "1Pe", 2, "Psa", 34, "p"));
		links.add(new Link("OTNT660", "1Pe", 3, "Pro", 17, "p"));
		links.add(new Link("OTNT662", "1Pe", 3, "Isa", 8, "p"));
		links.add(new Link("OTNT673", "2Pe", 3, "Eze", 12, "p"));
		links.add(new Link("OTNT677", "2Pe", 3, "Psa", 102, "p"));
		links.add(new Link("OTNT681", "1Jo", 3, "Isa", 53, "p"));
		links.add(new Link("OTNT683", "1Jo", 3, "Num", 22, "p"));
		links.add(new Link("OTNT693", "Re", 1, "Exo", 19, "p"));
		links.add(new Link("OTNT694", "Re", 1, "Dan", 7, "p"));
		links.add(new Link("OTNT695", "Re", 1, "Isa", 40, "p"));
		links.add(new Link("OTNT696", "Re", 1, "Zec", 12, "p"));
		links.add(new Link("OTNT697", "Re", 1, "Isa", 41, "p"));
		links.add(new Link("OTNT698", "Re", 1, "Isa", 44, "p"));
		links.add(new Link("OTNT699", "Re", 1, "Zec", 4, "p"));
		links.add(new Link("OTNT700", "Re", 1, "Dan", 7, "p"));
		links.add(new Link("OTNT701", "Re", 1, "Dan", 10, "p"));
		links.add(new Link("OTNT702", "Re", 1, "Eze", 1, "p"));
		links.add(new Link("OTNT703", "Re", 1, "Eze", 8, "p"));
		links.add(new Link("OTNT704", "Re", 1, "Eze", 43, "p"));
		links.add(new Link("OTNT705", "Re", 1, "Isa", 49, "p"));
		links.add(new Link("OTNT706", "Re", 1, "Dan", 8, "p"));
		links.add(new Link("OTNT707", "Re", 1, "Dan", 10, "p"));
		links.add(new Link("OTNT708", "Re", 1, "Isa", 44, "p"));
		links.add(new Link("OTNT709", "Re", 2, "Deu", 23, "p"));
		links.add(new Link("OTNT710", "Re", 2, "Gen", 2, "p"));
		links.add(new Link("OTNT711", "Re", 2, "Num", 25, "p"));
		links.add(new Link("OTNT712", "Re", 2, "Num", 31, "p"));
		links.add(new Link("OTNT713", "Re", 2, "1Ki", 16, "p"));
		links.add(new Link("OTNT714", "Re", 2, "1Ki", 21, "p"));
		links.add(new Link("OTNT715", "Re", 2, "2Ki", 9, "p"));
		links.add(new Link("OTNT716", "Re", 2, "Jer", 17, "p"));
		links.add(new Link("OTNT717", "Re", 2, "Psa", 2, "p"));
		links.add(new Link("OTNT718", "Re", 3, "Isa", 22, "p"));
		links.add(new Link("OTNT719", "Re", 3, "Job", 12, "p"));
		links.add(new Link("OTNT720", "Re", 3, "Isa", 60, "p"));
		links.add(new Link("OTNT721", "Re", 3, "Hos", 12, "p"));
		links.add(new Link("OTNT722", "Re", 3, "Pro", 3, "p"));
		links.add(new Link("OTNT723", "Re", 3, "Psa", 110, "p"));
		links.add(new Link("OTNT724", "Re", 4, "Eze", 1, "p"));
		links.add(new Link("OTNT725", "Re", 4, "Exo", 19, "p"));
		links.add(new Link("OTNT726", "Re", 4, "Eze", 1, "p"));
		links.add(new Link("OTNT727", "Re", 4, "Eze", 11, "p"));
		links.add(new Link("OTNT728", "Re", 4, "Isa", 6, "p"));
		links.add(new Link("OTNT729", "Re", 4, "Eze", 1, "p"));
		links.add(new Link("OTNT730", "Re", 4, "Exo", 24, "p"));
		links.add(new Link("OTNT731", "Re", 4, "Eze", 1, "p"));
		links.add(new Link("OTNT732", "Re", 4, "Eze", 10, "p"));
		links.add(new Link("OTNT733", "Re", 4, "Eze", 1, "p"));
		links.add(new Link("OTNT734", "Re", 4, "Isa", 6, "p"));
		links.add(new Link("OTNT735", "Re", 5, "Eze", 2, "p"));
		links.add(new Link("OTNT736", "Re", 5, "Isa", 53, "p"));
		links.add(new Link("OTNT737", "Re", 5, "Zec", 4, "p"));
		links.add(new Link("OTNT738", "Re", 5, "2Ch", 16, "p"));
		links.add(new Link("OTNT739", "Re", 5, "Psa", 141, "p"));
		links.add(new Link("OTNT740", "Re", 5, "Exo", 19, "p"));
		links.add(new Link("OTNT741", "Re", 5, "Dan", 7, "p"));
		links.add(new Link("OTNT742", "Re", 6, "Eze", 14, "p"));
		links.add(new Link("OTNT743", "Re", 6, "Isa", 24, "p"));
		links.add(new Link("OTNT744", "Re", 6, "Isa", 13, "p"));
		links.add(new Link("OTNT745", "Re", 6, "Hag", 2, "p"));
		links.add(new Link("OTNT746", "Re", 6, "Joe", 2, "p"));
		links.add(new Link("OTNT750", "Re", 6, "Isa", 2, "p"));
		links.add(new Link("OTNT751", "Re", 6, "Isa", 2, "p"));
		links.add(new Link("OTNT756", "Re", 7, "Eze", 9, "p"));
		links.add(new Link("OTNT760", "Re", 8, "Exo", 30, "p"));
		links.add(new Link("OTNT762", "Re", 8, "Eze", 10, "p"));
		links.add(new Link("OTNT765", "Re", 8, "Exo", 7, "p"));
		links.add(new Link("OTNT780", "Re", 10, "Jer", 1, "p"));
		links.add(new Link("OTNT781", "Re", 11, "Eze", 40, "p"));
		links.add(new Link("OTNT782", "Re", 11, "Eze", 41, "p"));
		links.add(new Link("OTNT783", "Re", 11, "Eze", 40, "p"));
		links.add(new Link("OTNT784", "Re", 11, "Dan", 7, "p"));
		links.add(new Link("OTNT786", "Re", 11, "2Ki", 1, "p"));
		links.add(new Link("OTNT791", "Re", 11, "Dan", 2, "p"));
		links.add(new Link("OTNT795", "Re", 11, "Dan", 7, "p"));
		links.add(new Link("OTNT796", "Re", 11, "Psa", 115, "p"));
		links.add(new Link("OTNT797", "Re", 11, "Dan", 11, "p"));
		links.add(new Link("OTNT798", "Re", 12, "Mic", 4, "p"));
		links.add(new Link("OTNT799", "Re", 12, "Isa", 66, "p"));
		links.add(new Link("OTNT801", "Re", 12, "Dan", 8, "p"));
		links.add(new Link("OTNT803", "Re", 12, "Psa", 2, "p"));
		links.add(new Link("OTNT804", "Re", 12, "Dan", 7, "p"));
		links.add(new Link("OTNT805", "Re", 12, "Dan", 10, "p"));
		links.add(new Link("OTNT806", "Re", 12, "Dan", 12, "p"));
		links.add(new Link("OTNT807", "Re", 12, "Dan", 7, "p"));
		links.add(new Link("OTNT808", "Re", 12, "Dan", 12, "p"));
		links.add(new Link("OTNT809", "Re", 13, "Dan", 7, "p"));
		links.add(new Link("OTNT813", "Re", 13, "Dan", 8, "p"));
		links.add(new Link("OTNT818", "Re", 13, "Dan", 3, "p"));
		links.add(new Link("OTNT824", "Re", 14, "Dan", 4, "p"));
		links.add(new Link("OTNT825", "Re", 14, "Psa", 75, "p"));
		links.add(new Link("OTNT830", "Re", 14, "Isa", 19, "p"));
		links.add(new Link("OTNT843", "Re", 16, "Eze", 10, "p"));
		links.add(new Link("OTNT844", "Re", 16, "Exo", 9, "p"));
		links.add(new Link("OTNT845", "Re", 16, "Exo", 7, "p"));
		links.add(new Link("OTNT846", "Re", 16, "Exo", 7, "p"));
		links.add(new Link("OTNT847", "Re", 16, "Eze", 16, "p"));
		links.add(new Link("OTNT848", "Re", 16, "Exo", 10, "p"));
		links.add(new Link("OTNT849", "Re", 16, "Isa", 11, "p"));
		links.add(new Link("OTNT850", "Re", 16, "Jer", 50, "p"));
		links.add(new Link("OTNT851", "Re", 16, "Zep", 3, "p"));
		links.add(new Link("OTNT852", "Re", 16, "Joe", 3, "p"));
		links.add(new Link("OTNT853", "Re", 16, "Zec", 14, "p"));
		links.add(new Link("OTNT854", "Re", 16, "Exo", 9, "p"));
		links.add(new Link("OTNT857", "Re", 17, "Dan", 7, "p"));
		links.add(new Link("OTNT858", "Re", 17, "Jer", 51, "p"));
		links.add(new Link("OTNT859", "Re", 17, "Dan", 7, "p"));
		links.add(new Link("OTNT893", "Re", 19, "Psa", 72, "p"));
		links.add(new Link("OTNT899", "Re", 19, "Isa", 34, "p"));
		links.add(new Link("OTNT902", "Re", 19, "Isa", 30, "p"));
		links.add(new Link("OTNT903", "Re", 19, "Dan", 7, "p"));
		links.add(new Link("OTNT904", "Re", 20, "Dan", 9, "p"));
		links.add(new Link("OTNT906", "Re", 21, "Eze", 40, "p"));
		links.add(new Link("OTNT918", "Re", 21, "Eze", 48, "p"));
		links.add(new Link("OTNT924", "Re", 22, "Zec", 14, "p"));
		links.add(new Link("OTNT928", "Re", 22, "Dan", 8, "p"));
		links.add(new Link("OTNT929", "Re", 22, "Dan", 12, "p"));
		links.add(new Link("OTNT937", "Re", 22, "Hab", 2, "p"));

		// Allusions
		links.add(new Link("OTNT13", "Mt", 5, "Psa", 37, "a"));
		links.add(new Link("OTNT14", "Mt", 5, "Exo", 20, "a"));
		links.add(new Link("OTNT16", "Mt", 5, "Exo", 20, "a"));
		links.add(new Link("OTNT18", "Mt", 5, "Deu", 24, "a"));
		links.add(new Link("OTNT19", "Mt", 5, "Exo", 20, "a"));
		links.add(new Link("OTNT20", "Mt", 5, "Lev", 19, "a"));
		links.add(new Link("OTNT21", "Mt", 5, "Exo", 21, "a"));
		links.add(new Link("OTNT22", "Mt", 5, "Lev", 24, "a"));
		links.add(new Link("OTNT24", "Mt", 5, "Lev", 19, "a"));
		links.add(new Link("OTNT27", "Mt", 8, "Lev", 14, "a"));
		links.add(new Link("OTNT30", "Mt", 10, "Mic", 7, "a"));
		links.add(new Link("OTNT31", "Mt", 11, "Isa", 35, "a"));
		links.add(new Link("OTNT32", "Mt", 11, "Isa", 29, "a"));
		links.add(new Link("OTNT34", "Mt", 11, "Mal", 4, "a"));
		links.add(new Link("OTNT35", "Mt", 12, "1Sa", 21, "a"));
		links.add(new Link("OTNT36", "Mt", 12, "Num", 28, "a"));
		links.add(new Link("OTNT40", "Mt", 12, "Jon", 1, "a"));
		links.add(new Link("OTNT41", "Mt", 12, "1Ki", 10, "a"));
		links.add(new Link("OTNT47", "Mt", 15, "Lev", 20, "a"));
		links.add(new Link("OTNT48", "Mt", 15, "Pro", 20, "a"));
		links.add(new Link("OTNT50", "Mt", 16, "Jon", 1, "a"));
		links.add(new Link("OTNT51", "Mt", 17, "Mal", 4, "a"));
		links.add(new Link("OTNT52", "Mt", 18, "Lev", 19, "a"));
		links.add(new Link("OTNT53", "Mt", 18, "Lev", 19, "a"));
		links.add(new Link("OTNT54", "Mt", 18, "Deu", 19, "a"));
		links.add(new Link("OTNT55", "Mt", 19, "Gen", 1, "a"));
		links.add(new Link("OTNT57", "Mt", 19, "Deu", 24, "a"));
		links.add(new Link("OTNT60", "Mt", 19, "Jer", 32, "a"));
		links.add(new Link("OTNT66", "Mt", 21, "Isa", 5, "a"));
		links.add(new Link("OTNT68", "Mt", 21, "Isa", 8, "a"));
		links.add(new Link("OTNT69", "Mt", 21, "Zec", 12, "a"));
		links.add(new Link("OTNT70", "Mt", 21, "Dan", 2, "a"));
		links.add(new Link("OTNT76", "Mt", 23, "Gen", 4, "a"));
		links.add(new Link("OTNT77", "Mt", 23, "2Ch", 24, "a"));
		links.add(new Link("OTNT78", "Mt", 23, "Psa", 69, "a"));
		links.add(new Link("OTNT79", "Mt", 23, "Jer", 12, "a"));
		links.add(new Link("OTNT80", "Mt", 23, "Jer", 22, "a"));
		links.add(new Link("OTNT81", "Mt", 23, "Psa", 118, "a"));
		links.add(new Link("OTNT82", "Mt", 24, "Dan", 9, "a"));
		links.add(new Link("OTNT83", "Mt", 24, "Dan", 8, "a"));
		links.add(new Link("OTNT84", "Mt", 24, "Dan", 11, "a"));
		links.add(new Link("OTNT85", "Mt", 24, "Dan", 12, "a"));
		links.add(new Link("OTNT87", "Mt", 24, "Isa", 13, "a"));
		links.add(new Link("OTNT88", "Mt", 24, "Joe", 2, "a"));
		links.add(new Link("OTNT89", "Mt", 24, "Joe", 3, "a"));
		links.add(new Link("OTNT90", "Mt", 24, "Eze", 32, "a"));
		links.add(new Link("OTNT91", "Mt", 24, "Isa", 51, "a"));
		links.add(new Link("OTNT92", "Mt", 24, "Gen", 7, "a"));
		links.add(new Link("OTNT94", "Mt", 26, "Psa", 22, "a"));
		links.add(new Link("OTNT96", "Mt", 26, "Psa", 35, "a"));
		links.add(new Link("OTNT97", "Mt", 26, "Isa", 50, "a"));
		links.add(new Link("OTNT100", "Mt", 27, "Psa", 22, "a"));
		links.add(new Link("OTNT102", "Mt", 28, "Dan", 7, "a"));
		links.add(new Link("OTNT105", "Mr", 1, "Lev", 14, "a"));
		links.add(new Link("OTNT106", "Mr", 2, "1Sa", 21, "a"));
		links.add(new Link("OTNT113", "Mr", 9, "Mal", 4, "a"));
		links.add(new Link("OTNT114", "Mr", 9, "Isa", 66, "a"));
		links.add(new Link("OTNT115", "Mr", 10, "Deu", 24, "a"));
		links.add(new Link("OTNT122", "Mr", 12, "Isa", 5, "a"));
		links.add(new Link("OTNT128", "Mr", 12, "1Sa", 15, "a"));
		links.add(new Link("OTNT130", "Mr", 13, "Jer", 29, "a"));
		links.add(new Link("OTNT131", "Mr", 13, "Mic", 7, "a"));
		links.add(new Link("OTNT132", "Mr", 13, "Dan", 9, "a"));
		links.add(new Link("OTNT133", "Mr", 13, "Dan", 8, "a"));
		links.add(new Link("OTNT134", "Mr", 13, "Dan", 11, "a"));
		links.add(new Link("OTNT135", "Mr", 13, "Dan", 12, "a"));
		links.add(new Link("OTNT136", "Mr", 13, "Isa", 13, "a"));
		links.add(new Link("OTNT137", "Mr", 13, "Joe", 3, "a"));
		links.add(new Link("OTNT138", "Mr", 13, "Isa", 40, "a"));
		links.add(new Link("OTNT142", "Lu", 1, "Lev", 16, "a"));
		links.add(new Link("OTNT144", "Lu", 1, "Psa", 132, "a"));
		links.add(new Link("OTNT145", "Lu", 1, "Mic", 4, "a"));
		links.add(new Link("OTNT146", "Lu", 1, "Dan", 4, "a"));
		links.add(new Link("OTNT147", "Lu", 1, "Gen", 22, "a"));
		links.add(new Link("OTNT148", "Lu", 1, "Gen", 17, "a"));
		links.add(new Link("OTNT149", "Lu", 1, "Gen", 22, "a"));
		links.add(new Link("OTNT150", "Lu", 1, "Gen", 12, "a"));
		links.add(new Link("OTNT151", "Lu", 1, "Num", 24, "a"));
		links.add(new Link("OTNT152", "Lu", 1, "Mal", 4, "a"));
		links.add(new Link("OTNT153", "Lu", 1, "Isa", 9, "a"));
		links.add(new Link("OTNT154", "Lu", 2, "Lev", 12, "a"));
		links.add(new Link("OTNT157", "Lu", 2, "Isa", 8, "a"));
		links.add(new Link("OTNT165", "Lu", 4, "1Ki", 17, "a"));
		links.add(new Link("OTNT166", "Lu", 4, "1Ki", 18, "a"));
		links.add(new Link("OTNT167", "Lu", 4, "2Ki", 5, "a"));
		links.add(new Link("OTNT168", "Lu", 5, "Lev", 14, "a"));
		links.add(new Link("OTNT169", "Lu", 6, "1Sa", 21, "a"));
		links.add(new Link("OTNT170", "Lu", 6, "Amo", 6, "a"));
		links.add(new Link("OTNT173", "Lu", 10, "2Ki", 4, "a"));
		links.add(new Link("OTNT176", "Lu", 10, "Lev", 18, "a"));
		links.add(new Link("OTNT177", "Lu", 11, "Jon", 1, "a"));
		links.add(new Link("OTNT178", "Lu", 11, "Jon", 3, "a"));
		links.add(new Link("OTNT179", "Lu", 11, "Jon", 4, "a"));
		links.add(new Link("OTNT180", "Lu", 11, "2Ki", 10, "a"));
		links.add(new Link("OTNT181", "Lu", 11, "Gen", 4, "a"));
		links.add(new Link("OTNT182", "Lu", 11, "2Ch", 24, "a"));
		links.add(new Link("OTNT184", "Lu", 13, "Psa", 118, "a"));
		links.add(new Link("OTNT185", "Lu", 13, "Jer", 12, "a"));
		links.add(new Link("OTNT186", "Lu", 13, "Jer", 22, "a"));
		links.add(new Link("OTNT187", "Lu", 14, "Pro", 25, "a"));
		links.add(new Link("OTNT188", "Lu", 14, "Mic", 7, "a"));
		links.add(new Link("OTNT189", "Lu", 17, "Lev", 19, "a"));
		links.add(new Link("OTNT190", "Lu", 17, "Gen", 7, "a"));
		links.add(new Link("OTNT191", "Lu", 17, "Gen", 19, "a"));
		links.add(new Link("OTNT192", "Lu", 17, "Gen", 19, "a"));
		links.add(new Link("OTNT197", "Lu", 20, "Isa", 5, "a"));
		links.add(new Link("OTNT199", "Lu", 20, "Isa", 8, "a"));
		links.add(new Link("OTNT200", "Lu", 20, "Zec", 12, "a"));
		links.add(new Link("OTNT201", "Lu", 20, "Dan", 2, "a"));
		links.add(new Link("OTNT203", "Lu", 20, "Exo", 3, "a"));
		links.add(new Link("OTNT206", "Lu", 23, "Isa", 54, "a"));
		links.add(new Link("OTNT207", "Lu", 23, "Hos", 10, "a"));
		links.add(new Link("OTNT209", "Lu", 24, "Isa", 53, "a"));
		links.add(new Link("OTNT211", "Joh", 1, "Gen", 28, "a"));
		links.add(new Link("OTNT213", "Joh", 3, "Num", 21, "a"));
		links.add(new Link("OTNT214", "Joh", 4, "Mic", 6, "a"));
		links.add(new Link("OTNT216", "Joh", 6, "Exo", 16, "a"));
		links.add(new Link("OTNT218", "Joh", 6, "Exo", 16, "a"));
		links.add(new Link("OTNT219", "Joh", 7, "Lev", 12, "a"));
		links.add(new Link("OTNT220", "Joh", 7, "Isa", 55, "a"));
		links.add(new Link("OTNT221", "Joh", 7, "Isa", 58, "a"));
		links.add(new Link("OTNT222", "Joh", 7, "Isa", 44, "a"));
		links.add(new Link("OTNT223", "Joh", 7, "Zec", 13, "a"));
		links.add(new Link("OTNT224", "Joh", 7, "Zec", 14, "a"));
		links.add(new Link("OTNT225", "Joh", 7, "Pro", 18, "a"));
		links.add(new Link("OTNT226", "Joh", 7, "Isa", 12, "a"));
		links.add(new Link("OTNT227", "Joh", 7, "Isa", 44, "a"));
		links.add(new Link("OTNT228", "Joh", 7, "Psa", 89, "a"));
		links.add(new Link("OTNT229", "Joh", 7, "Psa", 132, "a"));
		links.add(new Link("OTNT230", "Joh", 7, "Mic", 5, "a"));
		links.add(new Link("OTNT231", "Joh", 8, "Lev", 20, "a"));
		links.add(new Link("OTNT232", "Joh", 8, "Deu", 22, "a"));
		links.add(new Link("OTNT235", "Joh", 10, "Psa", 82, "a"));
		links.add(new Link("OTNT236", "Joh", 12, "Psa", 118, "a"));
		links.add(new Link("OTNT238", "Joh", 12, "2Sa", 7, "a"));
		links.add(new Link("OTNT239", "Joh", 12, "Psa", 89, "a"));
		links.add(new Link("OTNT240", "Joh", 12, "Psa", 110, "a"));
		links.add(new Link("OTNT241", "Joh", 12, "Isa", 9, "a"));
		links.add(new Link("OTNT244", "Joh", 12, "Deu", 18, "a"));
		links.add(new Link("OTNT249", "Joh", 17, "Psa", 41, "a"));
		links.add(new Link("OTNT250", "Joh", 17, "Psa", 109, "a"));
		links.add(new Link("OTNT252", "Joh", 19, "Psa", 69, "a"));
		links.add(new Link("OTNT255", "Joh", 19, "Num", 9, "a"));
		links.add(new Link("OTNT257", "Joh", 20, "Psa", 16, "a"));
		links.add(new Link("OTNT258", "Joh", 20, "Psa", 22, "a"));
		links.add(new Link("OTNT263", "Ac", 2, "2Sa", 7, "a"));
		links.add(new Link("OTNT264", "Ac", 2, "Psa", 89, "a"));
		links.add(new Link("OTNT269", "Ac", 3, "Gen", 12, "a"));
		links.add(new Link("OTNT271", "Ac", 4, "Isa", 28, "a"));
		links.add(new Link("OTNT273", "Ac", 7, "Gen", 15, "a"));
		links.add(new Link("OTNT274", "Ac", 7, "Neh", 9, "a"));
		links.add(new Link("OTNT276", "Ac", 7, "Gen", 11, "a"));
		links.add(new Link("OTNT277", "Ac", 7, "Gen", 12, "a"));
		links.add(new Link("OTNT278", "Ac", 7, "Gen", 12, "a"));
		links.add(new Link("OTNT279", "Ac", 7, "Gen", 13, "a"));
		links.add(new Link("OTNT281", "Ac", 7, "Gen", 17, "a"));
		links.add(new Link("OTNT282", "Ac", 7, "Gen", 21, "a"));
		links.add(new Link("OTNT283", "Ac", 7, "Gen", 25, "a"));
		links.add(new Link("OTNT284", "Ac", 7, "Gen", 42, "a"));
		links.add(new Link("OTNT285", "Ac", 7, "Gen", 37, "a"));
		links.add(new Link("OTNT286", "Ac", 7, "Gen", 39, "a"));
		links.add(new Link("OTNT287", "Ac", 7, "Gen", 41, "a"));
		links.add(new Link("OTNT288", "Ac", 7, "Gen", 41, "a"));
		links.add(new Link("OTNT289", "Ac", 7, "Gen", 42, "a"));
		links.add(new Link("OTNT290", "Ac", 7, "Gen", 45, "a"));
		links.add(new Link("OTNT291", "Ac", 7, "Gen", 45, "a"));
		links.add(new Link("OTNT292", "Ac", 7, "Jos", 24, "a"));
		links.add(new Link("OTNT293", "Ac", 7, "Exo", 1, "a"));
		links.add(new Link("OTNT294", "Ac", 7, "Exo", 1, "a"));
		links.add(new Link("OTNT295", "Ac", 7, "Exo", 1, "a"));
		links.add(new Link("OTNT296", "Ac", 7, "Exo", 2, "a"));
		links.add(new Link("OTNT297", "Ac", 7, "Exo", 2, "a"));
		links.add(new Link("OTNT298", "Ac", 7, "Exo", 2, "a"));
		links.add(new Link("OTNT300", "Ac", 7, "Exo", 18, "a"));
		links.add(new Link("OTNT301", "Ac", 7, "Exo", 3, "a"));
		links.add(new Link("OTNT305", "Ac", 7, "Exo", 3, "a"));
		links.add(new Link("OTNT306", "Ac", 7, "Exo", 7, "a"));
		links.add(new Link("OTNT307", "Ac", 7, "Exo", 14, "a"));
		links.add(new Link("OTNT308", "Ac", 7, "Exo", 12, "a"));
		links.add(new Link("OTNT309", "Ac", 7, "Exo", 15, "a"));
		links.add(new Link("OTNT310", "Ac", 7, "Exo", 16, "a"));
		links.add(new Link("OTNT312", "Ac", 7, "Exo", 19, "a"));
		links.add(new Link("OTNT313", "Ac", 7, "Exo", 20, "a"));
		links.add(new Link("OTNT315", "Ac", 7, "Exo", 32, "a"));
		links.add(new Link("OTNT316", "Ac", 7, "Amo", 5, "a"));
		links.add(new Link("OTNT317", "Ac", 7, "Exo", 25, "a"));
		links.add(new Link("OTNT318", "Ac", 7, "Exo", 26, "a"));
		links.add(new Link("OTNT319", "Ac", 7, "Jos", 3, "a"));
		links.add(new Link("OTNT320", "Ac", 7, "Jos", 18, "a"));
		links.add(new Link("OTNT321", "Ac", 7, "2Sa", 7, "a"));
		links.add(new Link("OTNT322", "Ac", 7, "Psa", 132, "a"));
		links.add(new Link("OTNT323", "Ac", 7, "1Ki", 8, "a"));
		links.add(new Link("OTNT326", "Ac", 10, "Deu", 10, "a"));
		links.add(new Link("OTNT327", "Ac", 10, "Job", 34, "a"));
		links.add(new Link("OTNT328", "Ac", 13, "Isa", 1, "a"));
		links.add(new Link("OTNT329", "Ac", 13, "Exo", 12, "a"));
		links.add(new Link("OTNT330", "Ac", 13, "Deu", 1, "a"));
		links.add(new Link("OTNT331", "Ac", 13, "Num", 14, "a"));
		links.add(new Link("OTNT332", "Ac", 13, "Psa", 95, "a"));
		links.add(new Link("OTNT333", "Ac", 13, "Deu", 7, "a"));
		links.add(new Link("OTNT334", "Ac", 13, "Jos", 14, "a"));
		links.add(new Link("OTNT335", "Ac", 13, "Jud", 2, "a"));
		links.add(new Link("OTNT336", "Ac", 13, "1Sa", 3, "a"));
		links.add(new Link("OTNT337", "Ac", 13, "1Sa", 8, "a"));
		links.add(new Link("OTNT338", "Ac", 13, "1Sa", 10, "a"));
		links.add(new Link("OTNT341", "Ac", 13, "1Ch", 10, "a"));
		links.add(new Link("OTNT345", "Ac", 13, "1Ki", 2, "a"));
		links.add(new Link("OTNT348", "Ac", 13, "Isa", 11, "a"));
		links.add(new Link("OTNT349", "Ac", 15, "Amo", 9, "a"));
		links.add(new Link("OTNT350", "Ac", 17, "Psa", 9, "a"));
		links.add(new Link("OTNT351", "Ac", 17, "Psa", 96, "a"));
		links.add(new Link("OTNT352", "Ac", 17, "Psa", 98, "a"));
		links.add(new Link("OTNT356", "Ro", 1, "Jer", 10, "a"));
		links.add(new Link("OTNT357", "Ro", 2, "Pro", 24, "a"));
		links.add(new Link("OTNT358", "Ro", 2, "Psa", 62, "a"));
		links.add(new Link("OTNT359", "Ro", 2, "Deu", 10, "a"));
		links.add(new Link("OTNT360", "Ro", 2, "Job", 34, "a"));
		links.add(new Link("OTNT365", "Ro", 3, "Jer", 17, "a"));
		links.add(new Link("OTNT386", "Ro", 9, "Isa", 45, "a"));
		links.add(new Link("OTNT387", "Ro", 9, "Jer", 18, "a"));
		links.add(new Link("OTNT396", "Ro", 10, "Deu", 30, "a"));
		links.add(new Link("OTNT406", "Ro", 11, "Psa", 94, "a"));
		links.add(new Link("OTNT410", "Ro", 11, "Isa", 6, "a"));
		links.add(new Link("OTNT414", "Ro", 11, "Job", 41, "a"));
		links.add(new Link("OTNT415", "Ro", 12, "Amo", 5, "a"));
		links.add(new Link("OTNT416", "Ro", 12, "Isa", 5, "a"));
		links.add(new Link("OTNT431", "1Co", 1, "Isa", 44, "a"));
		links.add(new Link("OTNT432", "1Co", 1, "Isa", 33, "a"));
		links.add(new Link("OTNT436", "1Co", 3, "Psa", 62, "a"));
		links.add(new Link("OTNT439", "1Co", 5, "Deu", 17, "a"));
		links.add(new Link("OTNT440", "1Co", 5, "Deu", 19, "a"));
		links.add(new Link("OTNT441", "1Co", 5, "Deu", 24, "a"));
		links.add(new Link("OTNT444", "1Co", 10, "Exo", 13, "a"));
		links.add(new Link("OTNT445", "1Co", 10, "Exo", 14, "a"));
		links.add(new Link("OTNT446", "1Co", 10, "Num", 9, "a"));
		links.add(new Link("OTNT447", "1Co", 10, "Exo", 16, "a"));
		links.add(new Link("OTNT448", "1Co", 10, "Exo", 17, "a"));
		links.add(new Link("OTNT449", "1Co", 10, "Num", 11, "a"));
		links.add(new Link("OTNT450", "1Co", 10, "Num", 20, "a"));
		links.add(new Link("OTNT451", "1Co", 10, "Num", 26, "a"));
		links.add(new Link("OTNT453", "1Co", 10, "Num", 25, "a"));
		links.add(new Link("OTNT454", "1Co", 10, "Num", 21, "a"));
		links.add(new Link("OTNT455", "1Co", 10, "Num", 14, "a"));
		links.add(new Link("OTNT456", "1Co", 10, "Psa", 106, "a"));
		links.add(new Link("OTNT460", "1Co", 14, "Gen", 3, "a"));
		links.add(new Link("OTNT461", "1Co", 15, "Isa", 53, "a"));
		links.add(new Link("OTNT462", "1Co", 15, "Psa", 22, "a"));
		links.add(new Link("OTNT463", "1Co", 15, "Psa", 40, "a"));
		links.add(new Link("OTNT464", "1Co", 15, "Psa", 16, "a"));
		links.add(new Link("OTNT471", "2Co", 3, "Exo", 34, "a"));
		links.add(new Link("OTNT480", "2Co", 9, "Pro", 22, "a"));
		links.add(new Link("OTNT485", "Ga", 2, "Psa", 143, "a"));
		links.add(new Link("OTNT486", "Ga", 3, "Gen", 15, "a"));
		links.add(new Link("OTNT488", "Ga", 3, "Gen", 22, "a"));
		links.add(new Link("OTNT494", "Ga", 3, "Exo", 12, "a"));
		links.add(new Link("OTNT495", "Ga", 4, "Gen", 21, "a"));
		links.add(new Link("OTNT496", "Ga", 4, "Gen", 16, "a"));
		links.add(new Link("OTNT500", "Eph", 2, "Isa", 57, "a"));
		links.add(new Link("OTNT507", "Eph", 6, "Deu", 10, "a"));
		links.add(new Link("OTNT508", "Eph", 6, "Job", 34, "a"));
		links.add(new Link("OTNT509", "Eph", 6, "Isa", 59, "a"));
		links.add(new Link("OTNT510", "Php", 2, "Isa", 45, "a"));
		links.add(new Link("OTNT511", "Php", 4, "Psa", 119, "a"));
		links.add(new Link("OTNT512", "Php", 4, "Psa", 145, "a"));
		links.add(new Link("OTNT513", "Col", 2, "Deu", 10, "a"));
		links.add(new Link("OTNT514", "Col", 3, "Deu", 10, "a"));
		links.add(new Link("OTNT515", "Col", 3, "Job", 34, "a"));
		links.add(new Link("OTNT516", "1Th", 5, "Isa", 59, "a"));
		links.add(new Link("OTNT517", "1Th", 5, "Pro", 17, "a"));
		links.add(new Link("OTNT518", "2Th", 2, "Dan", 11, "a"));
		links.add(new Link("OTNT521", "1Ti", 2, "Gen", 2, "a"));
		links.add(new Link("OTNT522", "1Ti", 2, "Gen", 3, "a"));
		links.add(new Link("OTNT523", "1Ti", 2, "Gen", 3, "a"));
		links.add(new Link("OTNT525", "1Ti", 6, "Job", 1, "a"));
		links.add(new Link("OTNT526", "1Ti", 6, "Ecc", 5, "a"));
		links.add(new Link("OTNT528", "2Ti", 2, "Num", 16, "a"));
		links.add(new Link("OTNT529", "2Ti", 3, "Exo", 7, "a"));
		links.add(new Link("OTNT542", "Heb", 3, "Num", 12, "a"));
		links.add(new Link("OTNT553", "Heb", 7, "Gen", 14, "a"));
		links.add(new Link("OTNT557", "Heb", 9, "Exo", 25, "a"));
		links.add(new Link("OTNT558", "Heb", 9, "Exo", 26, "a"));
		links.add(new Link("OTNT560", "Heb", 9, "Num", 17, "a"));
		links.add(new Link("OTNT561", "Heb", 9, "Exo", 30, "a"));
		links.add(new Link("OTNT562", "Heb", 9, "Lev", 16, "a"));
		links.add(new Link("OTNT566", "Heb", 10, "Exo", 29, "a"));
		links.add(new Link("OTNT567", "Heb", 10, "Psa", 110, "a"));
		links.add(new Link("OTNT570", "Heb", 10, "Deu", 17, "a"));
		links.add(new Link("OTNT573", "Heb", 11, "Gen", 1, "a"));
		links.add(new Link("OTNT574", "Heb", 11, "Gen", 4, "a"));
		links.add(new Link("OTNT575", "Heb", 11, "Gen", 5, "a"));
		links.add(new Link("OTNT576", "Heb", 11, "Gen", 6, "a"));
		links.add(new Link("OTNT577", "Heb", 11, "Gen", 12, "a"));
		links.add(new Link("OTNT578", "Heb", 11, "Gen", 12, "a"));
		links.add(new Link("OTNT579", "Heb", 11, "Gen", 27, "a"));
		links.add(new Link("OTNT580", "Heb", 11, "Gen", 18, "a"));
		links.add(new Link("OTNT581", "Heb", 11, "Gen", 22, "a"));
		links.add(new Link("OTNT584", "Heb", 11, "Gen", 23, "a"));
		links.add(new Link("OTNT586", "Heb", 11, "Gen", 22, "a"));
		links.add(new Link("OTNT587", "Heb", 11, "Gen", 22, "a"));
		links.add(new Link("OTNT588", "Heb", 11, "Gen", 27, "a"));
		links.add(new Link("OTNT590", "Heb", 11, "Gen", 48, "a"));
		links.add(new Link("OTNT591", "Heb", 11, "Gen", 50, "a"));
		links.add(new Link("OTNT592", "Heb", 11, "Exo", 2, "a"));
		links.add(new Link("OTNT593", "Heb", 11, "Exo", 2, "a"));
		links.add(new Link("OTNT594", "Heb", 11, "Exo", 2, "a"));
		links.add(new Link("OTNT595", "Heb", 11, "Exo", 12, "a"));
		links.add(new Link("OTNT596", "Heb", 11, "Exo", 14, "a"));
		links.add(new Link("OTNT597", "Heb", 11, "Jos", 6, "a"));
		links.add(new Link("OTNT598", "Heb", 11, "Jos", 2, "a"));
		links.add(new Link("OTNT599", "Heb", 11, "Jos", 6, "a"));
		links.add(new Link("OTNT600", "Heb", 11, "Jud", 6, "a"));
		links.add(new Link("OTNT607", "Heb", 11, "2Ki", 4, "a"));
		links.add(new Link("OTNT608", "Heb", 11, "1Ki", 17, "a"));
		links.add(new Link("OTNT609", "Heb", 11, "1Ki", 19, "a"));
		links.add(new Link("OTNT612", "Heb", 12, "Isa", 35, "a"));
		links.add(new Link("OTNT613", "Heb", 12, "Pro", 4, "a"));
		links.add(new Link("OTNT614", "Heb", 12, "Deu", 29, "a"));
		links.add(new Link("OTNT615", "Heb", 12, "Gen", 25, "a"));
		links.add(new Link("OTNT616", "Heb", 12, "Exo", 19, "a"));
		links.add(new Link("OTNT621", "Heb", 13, "Gen", 18, "a"));
		links.add(new Link("OTNT625", "Heb", 13, "Lev", 4, "a"));
		links.add(new Link("OTNT626", "Heb", 13, "Lev", 16, "a"));
		links.add(new Link("OTNT627", "Heb", 13, "Num", 19, "a"));
		links.add(new Link("OTNT628", "Heb", 13, "Mic", 2, "a"));
		links.add(new Link("OTNT629", "Jas", 1, "Isa", 40, "a"));
		links.add(new Link("OTNT630", "Jas", 1, "Job", 14, "a"));
		links.add(new Link("OTNT632", "Jas", 2, "Lev", 19, "a"));
		links.add(new Link("OTNT633", "Jas", 2, "Pro", 24, "a"));
		links.add(new Link("OTNT636", "Jas", 2, "Gen", 22, "a"));
		links.add(new Link("OTNT638", "Jas", 2, "Jos", 2, "a"));
		links.add(new Link("OTNT639", "Jas", 2, "Jos", 6, "a"));
		links.add(new Link("OTNT642", "Jas", 5, "Job", 1, "a"));
		links.add(new Link("OTNT643", "Jas", 5, "Job", 42, "a"));
		links.add(new Link("OTNT644", "Jas", 5, "1Ki", 17, "a"));
		links.add(new Link("OTNT645", "Jas", 5, "1Ki", 18, "a"));
		links.add(new Link("OTNT649", "1Pe", 2, "Psa", 118, "a"));
		links.add(new Link("OTNT653", "1Pe", 2, "Deu", 10, "a"));
		links.add(new Link("OTNT654", "1Pe", 2, "Hos", 1, "a"));
		links.add(new Link("OTNT655", "1Pe", 2, "Hos", 2, "a"));
		links.add(new Link("OTNT656", "1Pe", 2, "Pro", 24, "a"));
		links.add(new Link("OTNT659", "1Pe", 3, "Gen", 18, "a"));
		links.add(new Link("OTNT663", "1Pe", 3, "Gen", 6, "a"));
		links.add(new Link("OTNT665", "1Pe", 4, "Pro", 11, "a"));
		links.add(new Link("OTNT666", "1Pe", 5, "Pro", 3, "a"));
		links.add(new Link("OTNT667", "1Pe", 5, "Psa", 55, "a"));
		links.add(new Link("OTNT668", "2Pe", 2, "Gen", 7, "a"));
		links.add(new Link("OTNT669", "2Pe", 2, "Gen", 8, "a"));
		links.add(new Link("OTNT670", "2Pe", 2, "Gen", 19, "a"));
		links.add(new Link("OTNT671", "2Pe", 2, "Num", 22, "a"));
		links.add(new Link("OTNT674", "2Pe", 3, "Gen", 1, "a"));
		links.add(new Link("OTNT675", "2Pe", 3, "Gen", 7, "a"));
		links.add(new Link("OTNT676", "2Pe", 3, "Psa", 90, "a"));
		links.add(new Link("OTNT678", "2Pe", 3, "Isa", 65, "a"));
		links.add(new Link("OTNT679", "2Pe", 3, "Isa", 66, "a"));
		links.add(new Link("OTNT680", "1Jo", 1, "Pro", 20, "a"));
		links.add(new Link("OTNT682", "1Jo", 3, "Gen", 4, "a"));
		links.add(new Link("OTNT684", "1Jo", 3, "Num", 16, "a"));
		links.add(new Link("OTNT685", "Jude", 1, "Exo", 12, "a"));
		links.add(new Link("OTNT686", "Jude", 1, "Num", 14, "a"));
		links.add(new Link("OTNT687", "Jude", 1, "Gen", 19, "a"));
		links.add(new Link("OTNT688", "Jude", 1, "Deu", 34, "a"));
		links.add(new Link("OTNT689", "Jude", 1, "Gen", 4, "a"));
		links.add(new Link("OTNT690", "Jude", 1, "Num", 22, "a"));
		links.add(new Link("OTNT691", "Jude", 1, "Num", 16, "a"));
		links.add(new Link("OTNT692", "Jude", 1, "Gen", 5, "a"));
		links.add(new Link("OTNT747", "Re", 6, "Isa", 34, "a"));
		links.add(new Link("OTNT748", "Re", 6, "Psa", 102, "a"));
		links.add(new Link("OTNT749", "Re", 6, "Isa", 34, "a"));
		links.add(new Link("OTNT752", "Re", 6, "Hos", 10, "a"));
		links.add(new Link("OTNT753", "Re", 6, "Isa", 13, "a"));
		links.add(new Link("OTNT754", "Re", 6, "Psa", 110, "a"));
		links.add(new Link("OTNT755", "Re", 6, "Joe", 2, "a"));
		links.add(new Link("OTNT759", "Re", 8, "Lev", 16, "a"));
		links.add(new Link("OTNT761", "Re", 8, "Psa", 141, "a"));
		links.add(new Link("OTNT763", "Re", 8, "Joe", 2, "a"));
		links.add(new Link("OTNT764", "Re", 8, "Exo", 9, "a"));
		links.add(new Link("OTNT766", "Re", 8, "Jer", 9, "a"));
		links.add(new Link("OTNT767", "Re", 8, "Eze", 32, "a"));
		links.add(new Link("OTNT768", "Re", 9, "Eze", 9, "a"));
		links.add(new Link("OTNT769", "Re", 9, "Jer", 8, "a"));
		links.add(new Link("OTNT770", "Re", 9, "Joe", 2, "a"));
		links.add(new Link("OTNT771", "Re", 9, "Joe", 1, "a"));
		links.add(new Link("OTNT772", "Re", 9, "Joe", 2, "a"));
		links.add(new Link("OTNT773", "Re", 9, "Psa", 115, "a"));
		links.add(new Link("OTNT774", "Re", 9, "Psa", 135, "a"));
		links.add(new Link("OTNT775", "Re", 10, "Eze", 2, "a"));
		links.add(new Link("OTNT776", "Re", 10, "Jer", 25, "a"));
		links.add(new Link("OTNT777", "Re", 10, "Dan", 8, "a"));
		links.add(new Link("OTNT778", "Re", 10, "Dan", 12, "a"));
		links.add(new Link("OTNT779", "Re", 10, "Eze", 2, "a"));
		links.add(new Link("OTNT785", "Re", 11, "Zec", 4, "a"));
		links.add(new Link("OTNT787", "Re", 11, "1Ki", 17, "a"));
		links.add(new Link("OTNT788", "Re", 11, "Exo", 7, "a"));
		links.add(new Link("OTNT789", "Re", 11, "Dan", 7, "a"));
		links.add(new Link("OTNT790", "Re", 11, "Est", 9, "a"));
		links.add(new Link("OTNT792", "Re", 11, "Dan", 7, "a"));
		links.add(new Link("OTNT793", "Re", 11, "Psa", 2, "a"));
		links.add(new Link("OTNT794", "Re", 11, "Psa", 46, "a"));
		links.add(new Link("OTNT800", "Re", 12, "Dan", 7, "a"));
		links.add(new Link("OTNT802", "Re", 12, "Isa", 66, "a"));
		links.add(new Link("OTNT810", "Re", 13, "Dan", 7, "a"));
		links.add(new Link("OTNT811", "Re", 13, "Dan", 7, "a"));
		links.add(new Link("OTNT812", "Re", 13, "Dan", 7, "a"));
		links.add(new Link("OTNT814", "Re", 13, "Dan", 5, "a"));
		links.add(new Link("OTNT815", "Re", 13, "Dan", 2, "a"));
		links.add(new Link("OTNT816", "Re", 13, "Isa", 14, "a"));
		links.add(new Link("OTNT817", "Re", 13, "Gen", 9, "a"));
		links.add(new Link("OTNT819", "Re", 14, "Psa", 2, "a"));
		links.add(new Link("OTNT820", "Re", 14, "Isa", 59, "a"));
		links.add(new Link("OTNT821", "Re", 14, "Psa", 32, "a"));
		links.add(new Link("OTNT822", "Re", 14, "Isa", 21, "a"));
		links.add(new Link("OTNT823", "Re", 14, "Jer", 51, "a"));
		links.add(new Link("OTNT826", "Re", 14, "Isa", 51, "a"));
		links.add(new Link("OTNT827", "Re", 14, "Jer", 25, "a"));
		links.add(new Link("OTNT828", "Re", 14, "Isa", 34, "a"));
		links.add(new Link("OTNT829", "Re", 14, "Dan", 7, "a"));
		links.add(new Link("OTNT831", "Re", 14, "Joe", 3, "a"));
		links.add(new Link("OTNT832", "Re", 14, "Joe", 3, "a"));
		links.add(new Link("OTNT833", "Re", 14, "Isa", 63, "a"));
		links.add(new Link("OTNT834", "Re", 14, "Lam", 1, "a"));
		links.add(new Link("OTNT835", "Re", 15, "Eze", 1, "a"));
		links.add(new Link("OTNT836", "Re", 15, "Exo", 15, "a"));
		links.add(new Link("OTNT839", "Re", 15, "Eze", 10, "a"));
		links.add(new Link("OTNT840", "Re", 15, "Eze", 10, "a"));
		links.add(new Link("OTNT841", "Re", 15, "Isa", 6, "a"));
		links.add(new Link("OTNT842", "Re", 15, "1Ki", 8, "a"));
		links.add(new Link("OTNT855", "Re", 17, "Jer", 51, "a"));
		links.add(new Link("OTNT856", "Re", 17, "Jer", 51, "a"));
		links.add(new Link("OTNT860", "Re", 17, "Dan", 7, "a"));
		links.add(new Link("OTNT861", "Re", 17, "Dan", 8, "a"));
		links.add(new Link("OTNT862", "Re", 17, "Isa", 8, "a"));
		links.add(new Link("OTNT863", "Re", 17, "Jer", 51, "a"));
		links.add(new Link("OTNT865", "Re", 18, "Jer", 51, "a"));
		links.add(new Link("OTNT866", "Re", 18, "Isa", 13, "a"));
		links.add(new Link("OTNT868", "Re", 18, "Nah", 3, "a"));
		links.add(new Link("OTNT869", "Re", 18, "Isa", 52, "a"));
		links.add(new Link("OTNT870", "Re", 18, "Jer", 50, "a"));
		links.add(new Link("OTNT871", "Re", 18, "Jer", 51, "a"));
		links.add(new Link("OTNT872", "Re", 18, "Jer", 50, "a"));
		links.add(new Link("OTNT873", "Re", 18, "Psa", 137, "a"));
		links.add(new Link("OTNT874", "Re", 18, "Isa", 47, "a"));
		links.add(new Link("OTNT875", "Re", 18, "Jer", 50, "a"));
		links.add(new Link("OTNT876", "Re", 18, "Eze", 27, "a"));
		links.add(new Link("OTNT877", "Re", 18, "Isa", 23, "a"));
		links.add(new Link("OTNT878", "Re", 18, "Isa", 34, "a"));
		links.add(new Link("OTNT879", "Re", 18, "Isa", 44, "a"));
		links.add(new Link("OTNT880", "Re", 18, "Jer", 51, "a"));
		links.add(new Link("OTNT881", "Re", 18, "Jer", 51, "a"));
		links.add(new Link("OTNT882", "Re", 18, "Isa", 24, "a"));
		links.add(new Link("OTNT883", "Re", 18, "Jer", 7, "a"));
		links.add(new Link("OTNT884", "Re", 18, "Jer", 25, "a"));
		links.add(new Link("OTNT885", "Re", 18, "Isa", 23, "a"));
		links.add(new Link("OTNT886", "Re", 18, "Jer", 51, "a"));
		links.add(new Link("OTNT887", "Re", 19, "Deu", 32, "a"));
		links.add(new Link("OTNT888", "Re", 19, "Isa", 34, "a"));
		links.add(new Link("OTNT889", "Re", 19, "Psa", 135, "a"));
		links.add(new Link("OTNT890", "Re", 19, "Psa", 115, "a"));
		links.add(new Link("OTNT891", "Re", 19, "Psa", 45, "a"));
		links.add(new Link("OTNT892", "Re", 19, "Isa", 61, "a"));
		links.add(new Link("OTNT894", "Re", 19, "Dan", 10, "a"));
		links.add(new Link("OTNT895", "Re", 19, "Isa", 63, "a"));
		links.add(new Link("OTNT896", "Re", 19, "Psa", 2, "a"));
		links.add(new Link("OTNT897", "Re", 19, "Lam", 1, "a"));
		links.add(new Link("OTNT898", "Re", 19, "Isa", 63, "a"));
		links.add(new Link("OTNT900", "Re", 19, "Eze", 39, "a"));
		links.add(new Link("OTNT901", "Re", 19, "Psa", 2, "a"));
		links.add(new Link("OTNT905", "Re", 21, "Isa", 65, "a"));
		links.add(new Link("OTNT907", "Re", 21, "Eze", 37, "a"));
		links.add(new Link("OTNT908", "Re", 21, "Isa", 25, "a"));
		links.add(new Link("OTNT909", "Re", 21, "Isa", 65, "a"));
		links.add(new Link("OTNT910", "Re", 21, "Isa", 43, "a"));
		links.add(new Link("OTNT911", "Re", 21, "Isa", 55, "a"));
		links.add(new Link("OTNT912", "Re", 21, "Eze", 40, "a"));
		links.add(new Link("OTNT913", "Re", 21, "Eze", 48, "a"));
		links.add(new Link("OTNT914", "Re", 21, "Zec", 2, "a"));
		links.add(new Link("OTNT915", "Re", 21, "Eze", 40, "a"));
		links.add(new Link("OTNT916", "Re", 21, "Isa", 54, "a"));
		links.add(new Link("OTNT917", "Re", 21, "Isa", 60, "a"));
		links.add(new Link("OTNT919", "Re", 21, "Isa", 60, "a"));
		links.add(new Link("OTNT920", "Re", 21, "Isa", 52, "a"));
		links.add(new Link("OTNT921", "Re", 21, "Eze", 44, "a"));
		links.add(new Link("OTNT922", "Re", 22, "Zec", 14, "a"));
		links.add(new Link("OTNT923", "Re", 22, "Eze", 47, "a"));
		links.add(new Link("OTNT925", "Re", 22, "Isa", 24, "a"));
		links.add(new Link("OTNT926", "Re", 22, "Isa", 60, "a"));
		links.add(new Link("OTNT927", "Re", 22, "Eze", 48, "a"));
		links.add(new Link("OTNT930", "Re", 22, "Isa", 40, "a"));
		links.add(new Link("OTNT931", "Re", 22, "Isa", 41, "a"));
		links.add(new Link("OTNT932", "Re", 22, "Isa", 44, "a"));
		links.add(new Link("OTNT933", "Re", 22, "Isa", 11, "a"));
		links.add(new Link("OTNT934", "Re", 22, "Isa", 55, "a"));
		links.add(new Link("OTNT935", "Re", 22, "Deu", 4, "a"));
		links.add(new Link("OTNT936", "Re", 22, "Deu", 12, "a"));

		// Quotations
		links.add(new Link("OTNT1", "Mt", 1, "Isa", 7, "q"));
		links.add(new Link("OTNT2", "Mt", 2, "Mic", 5, "q"));
		links.add(new Link("OTNT3", "Mt", 2, "Hos", 11, "q"));
		links.add(new Link("OTNT4", "Mt", 2, "Jer", 31, "q"));
		links.add(new Link("OTNT5", "Mt", 3, "Isa", 40, "q"));
		links.add(new Link("OTNT6", "Mt", 4, "Deu", 8, "q"));
		links.add(new Link("OTNT7", "Mt", 4, "Psa", 91, "q"));
		links.add(new Link("OTNT8", "Mt", 4, "Deu", 6, "q"));
		links.add(new Link("OTNT9", "Mt", 4, "Deu", 6, "q"));
		links.add(new Link("OTNT10", "Mt", 4, "Deu", 10, "q"));
		links.add(new Link("OTNT11", "Mt", 4, "Isa", 9, "q"));
		links.add(new Link("OTNT12", "Mt", 4, "Isa", 42, "q"));
		links.add(new Link("OTNT15", "Mt", 5, "Deu", 5, "q"));
		links.add(new Link("OTNT17", "Mt", 5, "Deu", 5, "q"));
		links.add(new Link("OTNT23", "Mt", 5, "Deu", 19, "q"));
		links.add(new Link("OTNT25", "Mt", 5, "Gen", 17, "q"));
		links.add(new Link("OTNT26", "Mt", 7, "Psa", 6, "q"));
		links.add(new Link("OTNT28", "Mt", 8, "Isa", 53, "q"));
		links.add(new Link("OTNT29", "Mt", 9, "Hos", 6, "q"));
		links.add(new Link("OTNT33", "Mt", 11, "Mal", 3, "q"));
		links.add(new Link("OTNT37", "Mt", 12, "Hos", 6, "q"));
		links.add(new Link("OTNT38", "Mt", 12, "Isa", 42, "q"));
		links.add(new Link("OTNT39", "Mt", 12, "Isa", 42, "q"));
		links.add(new Link("OTNT42", "Mt", 13, "Isa", 6, "q"));
		links.add(new Link("OTNT43", "Mt", 13, "Psa", 78, "q"));
		links.add(new Link("OTNT44", "Mt", 15, "Exo", 20, "q"));
		links.add(new Link("OTNT45", "Mt", 15, "Deu", 5, "q"));
		links.add(new Link("OTNT46", "Mt", 15, "Exo", 21, "q"));
		links.add(new Link("OTNT49", "Mt", 15, "Isa", 29, "q"));
		links.add(new Link("OTNT56", "Mt", 19, "Gen", 2, "q"));
		links.add(new Link("OTNT58", "Mt", 19, "Exo", 20, "q"));
		links.add(new Link("OTNT59", "Mt", 19, "Lev", 19, "q"));
		links.add(new Link("OTNT61", "Mt", 21, "Zec", 9, "q"));
		links.add(new Link("OTNT62", "Mt", 21, "Psa", 118, "q"));
		links.add(new Link("OTNT63", "Mt", 21, "Isa", 56, "q"));
		links.add(new Link("OTNT64", "Mt", 21, "Jer", 7, "q"));
		links.add(new Link("OTNT65", "Mt", 21, "Psa", 8, "q"));
		links.add(new Link("OTNT67", "Mt", 21, "Psa", 118, "q"));
		links.add(new Link("OTNT71", "Mt", 22, "Deu", 25, "q"));
		links.add(new Link("OTNT72", "Mt", 22, "Exo", 3, "q"));
		links.add(new Link("OTNT73", "Mt", 22, "Deu", 6, "q"));
		links.add(new Link("OTNT74", "Mt", 22, "Lev", 19, "q"));
		links.add(new Link("OTNT75", "Mt", 22, "Psa", 110, "q"));
		links.add(new Link("OTNT86", "Mt", 24, "Jer", 30, "q"));
		links.add(new Link("OTNT93", "Mt", 25, "Psa", 6, "q"));
		links.add(new Link("OTNT95", "Mt", 26, "Zec", 13, "q"));
		links.add(new Link("OTNT98", "Mt", 27, "Zec", 11, "q"));
		links.add(new Link("OTNT99", "Mt", 27, "Psa", 22, "q"));
		links.add(new Link("OTNT101", "Mt", 27, "Psa", 22, "q"));
		links.add(new Link("OTNT103", "Mr", 1, "Mal", 3, "q"));
		links.add(new Link("OTNT104", "Mr", 1, "Isa", 40, "q"));
		links.add(new Link("OTNT107", "Mr", 4, "Isa", 6, "q"));
		links.add(new Link("OTNT108", "Mr", 7, "Isa", 29, "q"));
		links.add(new Link("OTNT109", "Mr", 7, "Exo", 20, "q"));
		links.add(new Link("OTNT110", "Mr", 7, "Deu", 5, "q"));
		links.add(new Link("OTNT111", "Mr", 7, "Exo", 21, "q"));
		links.add(new Link("OTNT112", "Mr", 7, "Pro", 20, "q"));
		links.add(new Link("OTNT116", "Mr", 10, "Gen", 1, "q"));
		links.add(new Link("OTNT117", "Mr", 10, "Gen", 2, "q"));
		links.add(new Link("OTNT118", "Mr", 10, "Exo", 20, "q"));
		links.add(new Link("OTNT119", "Mr", 11, "Psa", 118, "q"));
		links.add(new Link("OTNT120", "Mr", 11, "Isa", 56, "q"));
		links.add(new Link("OTNT121", "Mr", 11, "Jer", 7, "q"));
		links.add(new Link("OTNT123", "Mr", 12, "Psa", 118, "q"));
		links.add(new Link("OTNT124", "Mr", 12, "Deu", 25, "q"));
		links.add(new Link("OTNT125", "Mr", 12, "Exo", 3, "q"));
		links.add(new Link("OTNT126", "Mr", 12, "Deu", 6, "q"));
		links.add(new Link("OTNT127", "Mr", 12, "Lev", 19, "q"));
		links.add(new Link("OTNT129", "Mr", 12, "Psa", 110, "q"));
		links.add(new Link("OTNT139", "Mr", 14, "Zec", 13, "q"));
		links.add(new Link("OTNT140", "Mr", 15, "Isa", 53, "q"));
		links.add(new Link("OTNT141", "Mr", 15, "Psa", 22, "q"));
		links.add(new Link("OTNT143", "Lu", 1, "Mal", 4, "q"));
		links.add(new Link("OTNT155", "Lu", 2, "Exo", 13, "q"));
		links.add(new Link("OTNT156", "Lu", 2, "Lev", 12, "q"));
		links.add(new Link("OTNT158", "Lu", 3, "Isa", 40, "q"));
		links.add(new Link("OTNT159", "Lu", 4, "Deu", 8, "q"));
		links.add(new Link("OTNT160", "Lu", 4, "Deu", 6, "q"));
		links.add(new Link("OTNT161", "Lu", 4, "Deu", 10, "q"));
		links.add(new Link("OTNT162", "Lu", 4, "Psa", 91, "q"));
		links.add(new Link("OTNT163", "Lu", 4, "Deu", 6, "q"));
		links.add(new Link("OTNT164", "Lu", 4, "Isa", 61, "q"));
		links.add(new Link("OTNT171", "Lu", 7, "Mal", 3, "q"));
		links.add(new Link("OTNT172", "Lu", 8, "Isa", 6, "q"));
		links.add(new Link("OTNT174", "Lu", 10, "Deu", 6, "q"));
		links.add(new Link("OTNT175", "Lu", 10, "Lev", 19, "q"));
		links.add(new Link("OTNT183", "Lu", 13, "Psa", 6, "q"));
		links.add(new Link("OTNT193", "Lu", 18, "Exo", 20, "q"));
		links.add(new Link("OTNT194", "Lu", 18, "Deu", 5, "q"));
		links.add(new Link("OTNT195", "Lu", 19, "Isa", 56, "q"));
		links.add(new Link("OTNT196", "Lu", 19, "Jer", 7, "q"));
		links.add(new Link("OTNT198", "Lu", 20, "Psa", 118, "q"));
		links.add(new Link("OTNT202", "Lu", 20, "Deu", 25, "q"));
		links.add(new Link("OTNT204", "Lu", 20, "Psa", 110, "q"));
		links.add(new Link("OTNT205", "Lu", 22, "Isa", 53, "q"));
		links.add(new Link("OTNT208", "Lu", 23, "Psa", 31, "q"));
		links.add(new Link("OTNT210", "Joh", 1, "Isa", 40, "q"));
		links.add(new Link("OTNT212", "Joh", 2, "Psa", 69, "q"));
		links.add(new Link("OTNT215", "Joh", 6, "Psa", 78, "q"));
		links.add(new Link("OTNT217", "Joh", 6, "Isa", 54, "q"));
		links.add(new Link("OTNT233", "Joh", 8, "Deu", 19, "q"));
		links.add(new Link("OTNT234", "Joh", 9, "Psa", 82, "q"));
		links.add(new Link("OTNT237", "Joh", 12, "Zec", 9, "q"));
		links.add(new Link("OTNT242", "Joh", 12, "Isa", 53, "q"));
		links.add(new Link("OTNT243", "Joh", 12, "Isa", 6, "q"));
		links.add(new Link("OTNT245", "Joh", 13, "Psa", 41, "q"));
		links.add(new Link("OTNT246", "Joh", 15, "Psa", 69, "q"));
		links.add(new Link("OTNT247", "Joh", 15, "Psa", 109, "q"));
		links.add(new Link("OTNT248", "Joh", 15, "Psa", 35, "q"));
		links.add(new Link("OTNT251", "Joh", 19, "Psa", 22, "q"));
		links.add(new Link("OTNT253", "Joh", 19, "Exo", 12, "q"));
		links.add(new Link("OTNT254", "Joh", 19, "Psa", 34, "q"));
		links.add(new Link("OTNT256", "Joh", 19, "Zec", 12, "q"));
		links.add(new Link("OTNT259", "Ac", 1, "Psa", 69, "q"));
		links.add(new Link("OTNT260", "Ac", 1, "Psa", 109, "q"));
		links.add(new Link("OTNT261", "Ac", 2, "Joe", 2, "q"));
		links.add(new Link("OTNT262", "Ac", 2, "Psa", 16, "q"));
		links.add(new Link("OTNT265", "Ac", 2, "Psa", 16, "q"));
		links.add(new Link("OTNT266", "Ac", 2, "Psa", 110, "q"));
		links.add(new Link("OTNT267", "Ac", 3, "Deu", 18, "q"));
		links.add(new Link("OTNT268", "Ac", 3, "Gen", 22, "q"));
		links.add(new Link("OTNT270", "Ac", 4, "Psa", 118, "q"));
		links.add(new Link("OTNT272", "Ac", 4, "Psa", 2, "q"));
		links.add(new Link("OTNT275", "Ac", 7, "Gen", 12, "q"));
		links.add(new Link("OTNT280", "Ac", 7, "Gen", 15, "q"));
		links.add(new Link("OTNT299", "Ac", 7, "Exo", 2, "q"));
		links.add(new Link("OTNT302", "Ac", 7, "Exo", 3, "q"));
		links.add(new Link("OTNT303", "Ac", 7, "Exo", 3, "q"));
		links.add(new Link("OTNT304", "Ac", 7, "Exo", 2, "q"));
		links.add(new Link("OTNT311", "Ac", 7, "Deu", 18, "q"));
		links.add(new Link("OTNT314", "Ac", 7, "Exo", 32, "q"));
		links.add(new Link("OTNT324", "Ac", 7, "Isa", 66, "q"));
		links.add(new Link("OTNT325", "Ac", 8, "Isa", 53, "q"));
		links.add(new Link("OTNT339", "Ac", 13, "1Sa", 13, "q"));
		links.add(new Link("OTNT340", "Ac", 13, "Psa", 89, "q"));
		links.add(new Link("OTNT342", "Ac", 13, "Psa", 2, "q"));
		links.add(new Link("OTNT343", "Ac", 13, "Psa", 55, "q"));
		links.add(new Link("OTNT344", "Ac", 13, "Psa", 16, "q"));
		links.add(new Link("OTNT346", "Ac", 13, "Hab", 1, "q"));
		links.add(new Link("OTNT347", "Ac", 13, "Isa", 49, "q"));
		links.add(new Link("OTNT353", "Ac", 23, "Exo", 22, "q"));
		links.add(new Link("OTNT354", "Ac", 28, "Isa", 6, "q"));
		links.add(new Link("OTNT355", "Ro", 1, "Hab", 2, "q"));
		links.add(new Link("OTNT361", "Ro", 2, "Isa", 52, "q"));
		links.add(new Link("OTNT362", "Ro", 2, "Eze", 36, "q"));
		links.add(new Link("OTNT363", "Ro", 3, "Psa", 116, "q"));
		links.add(new Link("OTNT364", "Ro", 3, "Psa", 51, "q"));
		links.add(new Link("OTNT366", "Ro", 3, "Psa", 14, "q"));
		links.add(new Link("OTNT367", "Ro", 3, "Psa", 5, "q"));
		links.add(new Link("OTNT368", "Ro", 3, "Psa", 140, "q"));
		links.add(new Link("OTNT369", "Ro", 3, "Psa", 10, "q"));
		links.add(new Link("OTNT370", "Ro", 3, "Isa", 59, "q"));
		links.add(new Link("OTNT371", "Ro", 3, "Psa", 36, "q"));
		links.add(new Link("OTNT372", "Ro", 4, "Gen", 15, "q"));
		links.add(new Link("OTNT373", "Ro", 4, "Psa", 32, "q"));
		links.add(new Link("OTNT374", "Ro", 4, "Gen", 17, "q"));
		links.add(new Link("OTNT375", "Ro", 4, "Gen", 17, "q"));
		links.add(new Link("OTNT376", "Ro", 4, "Gen", 15, "q"));
		links.add(new Link("OTNT377", "Ro", 7, "Exo", 20, "q"));
		links.add(new Link("OTNT378", "Ro", 7, "Deu", 5, "q"));
		links.add(new Link("OTNT379", "Ro", 8, "Psa", 44, "q"));
		links.add(new Link("OTNT380", "Ro", 9, "Gen", 21, "q"));
		links.add(new Link("OTNT381", "Ro", 9, "Gen", 18, "q"));
		links.add(new Link("OTNT382", "Ro", 9, "Gen", 25, "q"));
		links.add(new Link("OTNT383", "Ro", 9, "Mal", 1, "q"));
		links.add(new Link("OTNT384", "Ro", 9, "Exo", 33, "q"));
		links.add(new Link("OTNT385", "Ro", 9, "Exo", 9, "q"));
		links.add(new Link("OTNT388", "Ro", 9, "Hos", 2, "q"));
		links.add(new Link("OTNT389", "Ro", 9, "Hos", 1, "q"));
		links.add(new Link("OTNT390", "Ro", 9, "Isa", 10, "q"));
		links.add(new Link("OTNT391", "Ro", 9, "Isa", 1, "q"));
		links.add(new Link("OTNT392", "Ro", 9, "Isa", 8, "q"));
		links.add(new Link("OTNT393", "Ro", 9, "Isa", 28, "q"));
		links.add(new Link("OTNT394", "Ro", 10, "Lev", 18, "q"));
		links.add(new Link("OTNT395", "Ro", 10, "Eze", 20, "q"));
		links.add(new Link("OTNT397", "Ro", 10, "Deu", 30, "q"));
		links.add(new Link("OTNT398", "Ro", 10, "Isa", 28, "q"));
		links.add(new Link("OTNT399", "Ro", 10, "Joe", 2, "q"));
		links.add(new Link("OTNT400", "Ro", 10, "Isa", 52, "q"));
		links.add(new Link("OTNT401", "Ro", 10, "Nah", 1, "q"));
		links.add(new Link("OTNT402", "Ro", 10, "Isa", 53, "q"));
		links.add(new Link("OTNT403", "Ro", 10, "Psa", 19, "q"));
		links.add(new Link("OTNT404", "Ro", 10, "Deu", 32, "q"));
		links.add(new Link("OTNT405", "Ro", 10, "Isa", 65, "q"));
		links.add(new Link("OTNT407", "Ro", 11, "1Ki", 19, "q"));
		links.add(new Link("OTNT408", "Ro", 11, "1Ki", 19, "q"));
		links.add(new Link("OTNT409", "Ro", 11, "Isa", 29, "q"));
		links.add(new Link("OTNT411", "Ro", 11, "Psa", 69, "q"));
		links.add(new Link("OTNT412", "Ro", 11, "Isa", 59, "q"));
		links.add(new Link("OTNT413", "Ro", 11, "Isa", 40, "q"));
		links.add(new Link("OTNT417", "Ro", 12, "Pro", 3, "q"));
		links.add(new Link("OTNT418", "Ro", 12, "Deu", 32, "q"));
		links.add(new Link("OTNT419", "Ro", 12, "Pro", 25, "q"));
		links.add(new Link("OTNT420", "Ro", 13, "Exo", 20, "q"));
		links.add(new Link("OTNT421", "Ro", 13, "Deu", 5, "q"));
		links.add(new Link("OTNT422", "Ro", 13, "Lev", 19, "q"));
		links.add(new Link("OTNT423", "Ro", 14, "Isa", 45, "q"));
		links.add(new Link("OTNT424", "Ro", 15, "Psa", 69, "q"));
		links.add(new Link("OTNT425", "Ro", 15, "Psa", 18, "q"));
		links.add(new Link("OTNT426", "Ro", 15, "Deu", 32, "q"));
		links.add(new Link("OTNT427", "Ro", 15, "Psa", 117, "q"));
		links.add(new Link("OTNT428", "Ro", 15, "Isa", 11, "q"));
		links.add(new Link("OTNT429", "Ro", 15, "Isa", 52, "q"));
		links.add(new Link("OTNT430", "1Co", 1, "Isa", 29, "q"));
		links.add(new Link("OTNT433", "1Co", 1, "Jer", 9, "q"));
		links.add(new Link("OTNT434", "1Co", 2, "Isa", 64, "q"));
		links.add(new Link("OTNT435", "1Co", 2, "Isa", 40, "q"));
		links.add(new Link("OTNT437", "1Co", 3, "Job", 5, "q"));
		links.add(new Link("OTNT438", "1Co", 3, "Psa", 94, "q"));
		links.add(new Link("OTNT442", "1Co", 6, "Gen", 2, "q"));
		links.add(new Link("OTNT443", "1Co", 9, "Deu", 25, "q"));
		links.add(new Link("OTNT452", "1Co", 10, "Exo", 32, "q"));
		links.add(new Link("OTNT457", "1Co", 10, "Deu", 32, "q"));
		links.add(new Link("OTNT458", "1Co", 10, "Psa", 24, "q"));
		links.add(new Link("OTNT459", "1Co", 14, "Isa", 28, "q"));
		links.add(new Link("OTNT465", "1Co", 15, "Psa", 110, "q"));
		links.add(new Link("OTNT466", "1Co", 15, "Psa", 8, "q"));
		links.add(new Link("OTNT467", "1Co", 15, "Isa", 22, "q"));
		links.add(new Link("OTNT468", "1Co", 15, "Gen", 2, "q"));
		links.add(new Link("OTNT469", "1Co", 15, "Isa", 25, "q"));
		links.add(new Link("OTNT470", "1Co", 15, "Hos", 13, "q"));
		links.add(new Link("OTNT472", "2Co", 4, "Psa", 116, "q"));
		links.add(new Link("OTNT473", "2Co", 5, "Isa", 43, "q"));
		links.add(new Link("OTNT474", "2Co", 6, "Isa", 49, "q"));
		links.add(new Link("OTNT475", "2Co", 6, "Lev", 26, "q"));
		links.add(new Link("OTNT476", "2Co", 6, "Isa", 52, "q"));
		links.add(new Link("OTNT477", "2Co", 6, "Jer", 31, "q"));
		links.add(new Link("OTNT478", "2Co", 6, "2Sa", 7, "q"));
		links.add(new Link("OTNT479", "2Co", 8, "Exo", 16, "q"));
		links.add(new Link("OTNT481", "2Co", 9, "Psa", 112, "q"));
		links.add(new Link("OTNT482", "2Co", 10, "Jer", 9, "q"));
		links.add(new Link("OTNT483", "2Co", 13, "Deu", 19, "q"));
		links.add(new Link("OTNT484", "Ga", 2, "Deu", 10, "q"));
		links.add(new Link("OTNT487", "Ga", 3, "Gen", 12, "q"));
		links.add(new Link("OTNT489", "Ga", 3, "Deu", 27, "q"));
		links.add(new Link("OTNT490", "Ga", 3, "Hab", 2, "q"));
		links.add(new Link("OTNT491", "Ga", 3, "Lev", 18, "q"));
		links.add(new Link("OTNT492", "Ga", 3, "Deu", 21, "q"));
		links.add(new Link("OTNT493", "Ga", 3, "Gen", 22, "q"));
		links.add(new Link("OTNT497", "Ga", 4, "Isa", 54, "q"));
		links.add(new Link("OTNT498", "Ga", 4, "Gen", 21, "q"));
		links.add(new Link("OTNT499", "Ga", 5, "Lev", 19, "q"));
		links.add(new Link("OTNT501", "Eph", 4, "Psa", 68, "q"));
		links.add(new Link("OTNT502", "Eph", 4, "Zec", 8, "q"));
		links.add(new Link("OTNT503", "Eph", 4, "Psa", 4, "q"));
		links.add(new Link("OTNT504", "Eph", 5, "Gen", 2, "q"));
		links.add(new Link("OTNT505", "Eph", 6, "Exo", 20, "q"));
		links.add(new Link("OTNT506", "Eph", 6, "Deu", 5, "q"));
		links.add(new Link("OTNT524", "1Ti", 5, "Deu", 25, "q"));
		links.add(new Link("OTNT530", "Heb", 1, "Psa", 2, "q"));
		links.add(new Link("OTNT531", "Heb", 1, "2Sa", 7, "q"));
		links.add(new Link("OTNT532", "Heb", 1, "Psa", 97, "q"));
		links.add(new Link("OTNT533", "Heb", 1, "Psa", 104, "q"));
		links.add(new Link("OTNT534", "Heb", 1, "Psa", 45, "q"));
		links.add(new Link("OTNT535", "Heb", 1, "Psa", 102, "q"));
		links.add(new Link("OTNT536", "Heb", 1, "Psa", 110, "q"));
		links.add(new Link("OTNT537", "Heb", 2, "Psa", 8, "q"));
		links.add(new Link("OTNT538", "Heb", 2, "Psa", 22, "q"));
		links.add(new Link("OTNT539", "Heb", 2, "Isa", 8, "q"));
		links.add(new Link("OTNT540", "Heb", 2, "Psa", 18, "q"));
		links.add(new Link("OTNT541", "Heb", 2, "2Sa", 22, "q"));
		links.add(new Link("OTNT543", "Heb", 3, "Psa", 95, "q"));
		links.add(new Link("OTNT544", "Heb", 3, "Psa", 95, "q"));
		links.add(new Link("OTNT546", "Heb", 4, "Psa", 95, "q"));
		links.add(new Link("OTNT547", "Heb", 4, "Gen", 2, "q"));
		links.add(new Link("OTNT548", "Heb", 4, "Psa", 95, "q"));
		links.add(new Link("OTNT550", "Heb", 5, "Psa", 2, "q"));
		links.add(new Link("OTNT551", "Heb", 5, "Psa", 110, "q"));
		links.add(new Link("OTNT552", "Heb", 6, "Gen", 22, "q"));
		links.add(new Link("OTNT554", "Heb", 7, "Psa", 110, "q"));
		links.add(new Link("OTNT555", "Heb", 8, "Exo", 25, "q"));
		links.add(new Link("OTNT556", "Heb", 8, "Jer", 31, "q"));
		links.add(new Link("OTNT564", "Heb", 9, "Exo", 24, "q"));
		links.add(new Link("OTNT565", "Heb", 10, "Psa", 40, "q"));
		links.add(new Link("OTNT568", "Heb", 10, "Jer", 31, "q"));
		links.add(new Link("OTNT571", "Heb", 10, "Deu", 32, "q"));
		links.add(new Link("OTNT572", "Heb", 10, "Hab", 2, "q"));
		links.add(new Link("OTNT589", "Heb", 11, "Gen", 47, "q"));
		links.add(new Link("OTNT610", "Heb", 12, "Pro", 3, "q"));
		links.add(new Link("OTNT617", "Heb", 12, "Exo", 19, "q"));
		links.add(new Link("OTNT618", "Heb", 12, "Deu", 9, "q"));
		links.add(new Link("OTNT619", "Heb", 12, "Hag", 2, "q"));
		links.add(new Link("OTNT620", "Heb", 12, "Deu", 4, "q"));
		links.add(new Link("OTNT622", "Heb", 13, "Deu", 31, "q"));
		links.add(new Link("OTNT623", "Heb", 13, "Jos", 1, "q"));
		links.add(new Link("OTNT624", "Heb", 13, "Psa", 118, "q"));
		links.add(new Link("OTNT634", "Jas", 2, "Lev", 19, "q"));
		links.add(new Link("OTNT635", "Jas", 2, "Exo", 20, "q"));
		links.add(new Link("OTNT637", "Jas", 2, "Gen", 15, "q"));
		links.add(new Link("OTNT640", "Jas", 4, "Pro", 3, "q"));
		links.add(new Link("OTNT646", "1Pe", 1, "Lev", 11, "q"));
		links.add(new Link("OTNT647", "1Pe", 1, "Isa", 40, "q"));
		links.add(new Link("OTNT650", "1Pe", 2, "Isa", 28, "q"));
		links.add(new Link("OTNT651", "1Pe", 2, "Psa", 118, "q"));
		links.add(new Link("OTNT652", "1Pe", 2, "Exo", 19, "q"));
		links.add(new Link("OTNT657", "1Pe", 2, "Isa", 53, "q"));
		links.add(new Link("OTNT658", "1Pe", 2, "Isa", 53, "q"));
		links.add(new Link("OTNT661", "1Pe", 3, "Psa", 34, "q"));
		links.add(new Link("OTNT664", "1Pe", 4, "Pro", 10, "q"));
		links.add(new Link("OTNT672", "2Pe", 2, "Pro", 26, "q"));
		links.add(new Link("OTNT757", "Re", 7, "Isa", 49, "q"));
		links.add(new Link("OTNT758", "Re", 7, "Isa", 25, "q"));
		links.add(new Link("OTNT837", "Re", 15, "Jer", 10, "q"));
		links.add(new Link("OTNT838", "Re", 15, "Psa", 86, "q"));
		links.add(new Link("OTNT864", "Re", 18, "Isa", 21, "q"));
		links.add(new Link("OTNT867", "Re", 18, "Jer", 51, "q"));
	}

	private void drawLinks() {
		Iterator<Link> iter = links.iterator();
		while (iter.hasNext()) {
			Link l = (Link) iter.next();
			int srcId = getBookId(l.sourceBook);
			int tarId = getBookId(l.targetBook);
			Book src = books.get(srcId);
			Book tar = books.get(tarId);
			if (highlight == false) {
				drawLinkLine(l);
			} else if ((hoverBook == -1 || (srcId == hoverBook || tarId == hoverBook))) {
				drawLinkLine(l);
			}
		}
	}

	private void drawLinkLine(Link l) {
		// System.err.println("drawLinkLine: l.id: " + l.id + "; " +
		// l.sourceBook + " to " + l.targetBook);

		int srcId = getBookId(l.sourceBook);
		int tarId = getBookId(l.targetBook);
		Book src = books.get(srcId);
		Book tar = books.get(tarId);

		double srcAngleR = src.startR + (chpAngleR * l.sourceChp);
		double tarAngleR = tar.startR + (chpAngleR * l.targetChp);

		FloatPoint startLinkSrc = new FloatPoint(getXf(rInner, srcAngleR), getYf(rInner, srcAngleR));
		FloatPoint startLinkTar = new FloatPoint(getXf(rInner, tarAngleR), getYf(rInner, tarAngleR));
		float startLinkSrcX = startLinkSrc.getX();
		float startLinkSrcY = startLinkSrc.getY();
		float startLinkTarX = startLinkTar.getX();
		float startLinkTarY = startLinkTar.getY();

		if (l.type == "q") { // quotation
			papplet.stroke(Color.RED.getRGB());
//			papplet.strokeWeight(QUOTATION_STROKEWEIGHT);
		} else if (l.type == "a") { // allusion
			papplet.stroke(Color.BLUE.getRGB());
//			papplet.strokeWeight(ALLUSION_STROKEWEIGHT);
		} else if (l.type == "p") { // possible allusion
			papplet.stroke(Color.ORANGE.getRGB());
//			papplet.strokeWeight(POSSIBLEALLUSION_STROKEWEIGHT);
		} else { // unrecognized type
			papplet.stroke(Color.LIGHT_GRAY.getRGB());
//			papplet.strokeWeight(UNKNOWN_STROKEWEIGHT);
		}
		papplet.line(startLinkSrcX, startLinkSrcY, startLinkTarX, startLinkTarY);
	}

	private int getBookId(String name) {
		int i = -1;
		Book b;
		// TODO optimize this code -- dedicated array for ID/Name lookup?
		// TODO verify that there aren't name collisions b/w OT and NT book names
		Iterator<Book> iter = books.iterator();
		while (iter.hasNext()) {
			i++;
			b = (Book) iter.next();
			if (b.bookName.equals(name)) {
				return (i);
			}
		}
		return (-1);
	}

	private FloatPoint getBookStartPoint(Book b) {
		return (b.startInner);
	}

	public void draw() {
		papplet.noFill();
		papplet.stroke(125);
		drawBooks();
		drawLinks();
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
//		debugPrint("getPoint(" + radius + ", " + rad + ") called...");
		x = getX(radius, rad);
		y = getY(radius, rad);
		pt.setLocation(x, y);
//		debugPrintln("...getPoint returning: " + pt.getX() + ", " + pt.getY());
		return (pt);
	}

	public float getXf(float radius, double rad) {
		float ax = (float) (cx + ((radius / 2) * (Math.cos((rad)))));
//		debugPrintln("  getX(" + (radius / 2) + ", " + rad + ") = " + ax);
		return ax;
	}

	public float getYf(float radius, double rad) {
		float ay = (float) (cy + ((radius / 2) * (Math.sin((rad)))));
//		debugPrintln("  getY(" + (radius / 2) + ", " + rad + ") = " + ay + "; ");
		return ay;
	}

	public double getX(float radius, double rad) {
		double ax = (cx + ((radius / 2) * (Math.cos((rad)))));
//		debugPrintln("  getX(" + (radius / 2) + ", " + rad + ") = " + ax);
		return ax;
	}

	public double getY(float radius, double rad) {
		double ay = (cy + ((radius / 2) * (Math.sin((rad)))));
//		debugPrintln("  getY(" + (radius / 2) + ", " + rad + ") = " + ay + "; ");
		return ay;
	}

	private void drawLine(Point pt1, Point pt2) {
//		debugPrintln("drawLine called; returning (float): " + (float) pt1.getX() + "," + (float) pt1.getY() + ","
//				+ (float) pt2.getX() + "," + (float) pt2.getY());
//		debugPrint("  drawLine called; returning: " + pt1.getX() + "," + pt1.getY() + "," + pt2.getX() + ","
//				+ pt2.getY());
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

	public int inBook(int mouseX, int mouseY) {
		Book b;
		Iterator<Book> iter = books.iterator();
		int i = 0;
		hoverBook = -1;

		while (iter.hasNext()) {
			b = (Book) iter.next();
			if (b.containsPoint(mouseX, mouseY, (papplet.width / 2), (papplet.height / 2))) {
				// System.err.println("in book (" + b.bookName + "; " + i +
				// ")! " + mouseX + ", " + mouseY);
				hoverBook = i;
				return (hoverBook);
			}
			i += 1;
		}
		hoverBook = -1;
		return (hoverBook);
	}

	public String getName(int index) {
		if (index == -1) {
			return ("");
		} else {
			return (books.get(index).bookName);
		}
	}

	public void unhighlight() {
		highlight = false;
		Book b;
		Iterator<Book> iter = books.iterator();
		int i = 0;
		while (iter.hasNext()) {
			b = (Book) iter.next();
			b.strokeWeight = DEFAULT_STROKEWEIGHT;
		}
	}

	public void highlight(int index) {
		highlight = true;
		Book b;
		Iterator<Book> iter = books.iterator();
		int i = 0;
		while (iter.hasNext()) {
			b = (Book) iter.next();
			b.strokeWeight = 0;
		}
		books.get(index).strokeWeight = HIGHLIGHT_STROKEWEIGHT;
	}


	public int getLinkCount(int id) {
		int linkCtr = 0;
		Iterator<Link> iter = links.iterator();
		while (iter.hasNext()) {
			Link l = (Link) iter.next();
			int srcId = getBookId(l.sourceBook);
			int tarId = getBookId(l.targetBook);
			if (srcId == id || tarId == id) {
				linkCtr++;
			}
		}
		return(linkCtr);
	}
}
