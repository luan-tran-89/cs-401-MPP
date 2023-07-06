package com.example.theaterapplab.model;

import com.example.theaterapplab.enums.Section;

import java.util.Objects;

public class Seat {
	private int column;
	private int row;
	private boolean isReserved;

	public Seat() {
		super();
		isReserved = false;
	}
	
	public Seat(int column, int row) {
		super();
		this.column = column;
		this.row = row;
	}
	
	public int getColumn() {
		return column;
	}

	public void setColumn(int column) {
		this.column = column;
	}

	public int getRow() {
		return row;
	}

	public void setRow(int row) {
		this.row = row;
	}

	public boolean isReserved() {
		return isReserved;
	}

	public void setReserved(boolean isReserved) {
		this.isReserved = isReserved;
	}

	public boolean isEmpty() {
		return !isReserved;
	}
	
	public Section getSection() {
		return Section.getSection(row);
	}

	public double getPrice() {
		return this.getSection().getPrice();
	};

	@Override
	public String toString() {
		return String.format("%-8s  |  %-8s  |  %-8s", column + 1, row + 1, isReserved);
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;
		Seat seat = (Seat) o;
		return column == seat.column && row == seat.row && isReserved == seat.isReserved;
	}

	@Override
	public int hashCode() {
		return Objects.hash(column, row, isReserved);
	}
}
