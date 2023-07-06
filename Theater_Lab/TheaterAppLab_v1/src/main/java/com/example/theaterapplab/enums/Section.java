package com.example.theaterapplab.enums;

import java.util.function.IntPredicate;
import java.util.stream.IntStream;

public enum Section {
	FRONT(18), MIDDLE(15), BACK(12);
	
	private final double price;
	
	Section(double i) {
		this.price = i;
	}

	public double getPrice() {
		return this.price;
	}
	
	public static Section getSection(int index) {
		IntPredicate condition = n -> n == index;
		if (IntStream.range(0, 5).anyMatch(condition)) {
			return FRONT;
		} else if (IntStream.range(5, 12).anyMatch(condition)) {
			return MIDDLE;
		}
		return BACK;
	}
	
	public static double getSeatPrice(int index) {
		return Section.getSection(index).getPrice();
	}
}
