package com.balinsbooks.otnt;

import java.awt.Point;

import processing.core.PApplet;

public class Book {
	String bookName;
	String bookTestament;
	int numChapters;
	public int R;
	public int G;
	public int B;

	Book(String name, String testament, int chpCount) {
		bookName = name;
		bookTestament = testament;
		numChapters = chpCount;
	}
}
