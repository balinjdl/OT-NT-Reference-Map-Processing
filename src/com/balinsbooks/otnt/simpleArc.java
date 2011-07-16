package com.balinsbooks.otnt;
import processing.core.PApplet;

public class simpleArc extends PApplet {
    int strokeFat = 1;
    int numberOfCuts = 66;
    int numberOfArcs = 66;
    float cut = TWO_PI / (numberOfCuts - 1);
    float dis = 7;
    float scaler = 1;
    float[] rad = new float[numberOfCuts];
    int cx, cy;

    public void setup() {
	size(800, 800);
	stroke(0);
	strokeCap(SQUARE);
	smooth();
	cx = width / 2;
	cy = height / 2;
    }

    public void draw() {
	background(255);

	translate(width / 2, height / 2);
	scale(scaler);
	translate(-width / 2, -height / 2);

	for (int i = 0; i < numberOfCuts; i++) {
	    rad[i] = i * cut;
	    // println(rad[i]);
	}

	for (int i = 0; i < numberOfArcs; i++) {
	    arc(cx, cy, i * dis, i * dis, 0, rad[i]);
	    noFill();
	    strokeWeight(strokeFat);
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
	    cx = cx + 10;
	}
	if (key == '<') {
	    cx = cx - 10;
	}
	if (key == 'i') {
	    cy = cy - 10;
	}
	if (key == 'k') {
	    cy = cy + 10;
	}
	if (key == 'c') {
	    scaler = 1;
	    cx = width / 2;
	    cy = height / 2;
	}
    }
}
