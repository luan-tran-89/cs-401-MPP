package com.example.theaterapplab.model;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.IntStream;

public class Theater2 {
	private String name;
	private int columns;
	private int rows;
	private Map<Integer, List<Seat>> seats;

	public Theater2(String name, int columns, int rows) {
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

	private Map<Integer, List<Seat>> generateEmptySeats() {
		Map<Integer, List<Seat>> seats = new HashMap<>();
		IntStream.range(0, this.getRows()).forEach(row -> {
			seats.put(row, IntStream.range(0, this.getColumns())
					.boxed()
					.map(column -> new Seat(column, row))
					.toList());
			
		});
		return seats;
	}
	
	private Seat getSeat(int column, int row) {
		return this.seats.get(row - 1).stream()
				.filter(s -> s.getColumn() == column - 1)
				.findFirst()
				.orElse(null);
	}
	
	/**
	 * a. Show ALL empty seats.
	 */
	public void showAllEmptySeats() {
		System.out.printf("%-8s  |  %-8s  |  %-8s%n", "Column", "Row", "Reserved");
		System.out.println("----------------------------------");
		seats.entrySet().stream().forEach(s -> {
			s.getValue().stream()
				.filter(Seat::isEmpty)
				.forEach(System.out::println);
		});
	}
	
	/**
	 * b. Show All seats that are already reserved.
	 */
	public void showAllReservedSeats() {
		System.out.printf("%-8s  |  %-8s  |  %-8s%n", "Column", "Row", "Reserved");
		System.out.println("----------------------------------");
		seats.entrySet().stream().forEach(s -> {
			s.getValue().stream()
				.filter(Seat::isReserved)
				.forEach(System.out::println);
		});
	}
	
	/**
	 * c. Reserve a seat for a person.
	 */
	public boolean reserveSeat(int column, int row, Person person) {
		Seat seat = this.getSeat(column, row);
		boolean success = false;
		
		if (seat == null) {
			System.out.printf("The seat %s-%s does not exist!!!", column, row);
		} else if (seat.isReserved()) {
			System.out.printf("The seat %s-%s has been reserved!!!", column, row);
		} else {
			seat.setReserved(true);
			success = true;
			System.out.printf("The seat %s-%s is reserved successfully!!!", column, row);
		}
		
		return success;
	}
	
	/**
	 * d. Unreserve a seat (the seat becomes free).
	 */
	public boolean unreserveSeat(int column, int row) {
		Seat seat = this.getSeat(column, row);
		boolean success = false;
		
		if (seat == null) {
			System.out.printf("The seat %s-%s does not exist!!!", column, row);
		} else if (!seat.isReserved()) {
			System.out.printf("The seat %s-%s has not been reserved, so it can't be unreserved!!!", column, row);
		} else {
			seat.setReserved(false);
			success = true;
			System.out.printf("The seat %s-%s is unreserved successfully!!!", column, row);
		}
		
		return success;
	}
	
	/**
	 * e. Empty all seats in the theater.
	 */
	public void emptyAllSeats() {
		this.setSeats(this.generateEmptySeats());
	}
	
	/**
	 * f. Reserve all seats in the theater.
	 */
	public void reserveAllSeats() {
		seats.entrySet().stream().forEach(map -> {
			map.getValue().stream()
				.forEach(s -> s.setReserved(true));
		});
	}
}
