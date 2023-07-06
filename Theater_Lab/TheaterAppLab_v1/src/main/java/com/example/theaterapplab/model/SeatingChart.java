package com.example.theaterapplab.model;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.IntStream;

public class SeatingChart {
	private String name;
	private int columns;
	private int rows;
	private Map<Integer, List<Seat>> seats;

	public SeatingChart(String name, int columns, int rows) {
		this.name = name;
		this.columns = columns;
		this.rows = rows;
		this.seats = this.generateEmptySeats();
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getRows() {
		return rows;
	}

	public void setRows(int rows) {
		this.rows = rows;
	}

	public int getColumns() {
		return columns;
	}

	public void setColumns(int columns) {
		this.columns = columns;
	}

	public Map<Integer, List<Seat>> getSeats() {
		return seats;
	}

	public void setSeats(Map<Integer, List<Seat>> seats) {
		this.seats = seats;
	}

	public Map<Integer, List<Seat>> generateEmptySeats() {
		Map<Integer, List<Seat>> seats = new HashMap<>();
		IntStream.range(0, this.getRows()).forEach(row -> {
			seats.put(row, 
					IntStream.range(0, this.getColumns())
					.boxed()
					.map(column -> new Seat(column, row))
					.toList());
			
		});
		return seats;
	}
	
	public Seat getSeat(int column, int row) {
		return this.getSeats().get(row).stream()
				.filter(s -> s.getColumn() == column)
				.findFirst()
				.orElse(null);
	}
}
