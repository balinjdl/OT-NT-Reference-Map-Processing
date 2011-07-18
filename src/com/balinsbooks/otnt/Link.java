package com.balinsbooks.otnt;

public class Link {
	String id;
	String sourceBook;
	int sourceChp;
	String targetBook;
	int targetChp;
	String type;

	public Link() {
	}

	public Link(String newId, String newSourceBook, int newSourceChp, String newTargetBook, int newTargetChp,
			String newType) {
		id = newId;
		sourceBook = newSourceBook;
		sourceChp = newSourceChp;
		targetBook = newTargetBook;
		targetChp = newTargetChp;
		type = newType;
	}
}
