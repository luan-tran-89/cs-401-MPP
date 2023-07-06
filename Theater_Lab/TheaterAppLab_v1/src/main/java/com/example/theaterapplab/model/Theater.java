package com.example.theaterapplab.model;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.IntStream;

public class Theater {
	private String name;
	private List<Performance> performances;
	private SeatingChart seatingChart;

	public Theater(String name, SeatingChart seatingChart) {
		this.name = name;
		this.seatingChart = seatingChart;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public SeatingChart getSeatingChart() {
		return seatingChart;
	}

	public void setSeatingChart(SeatingChart seatingChart) {
		this.seatingChart = seatingChart;
	}
	
	private Map<Integer, List<Seat>> getSeats() {
		return seatingChart.getSeats();
	}
	
	private Seat getSeat(int column, int row) {
		return seatingChart.getSeats().get(row - 1).stream()
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
		this.getSeats().entrySet().stream().forEach(s -> {
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
		this.getSeats().entrySet().stream().forEach(s -> {
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
		seatingChart.setSeats(seatingChart.generateEmptySeats());
	}
	
	/**
	 * f. Reserve all seats in the theater.
	 */
	public void reserveAllSeats() {
		this.getSeats().entrySet().stream().forEach(map -> {
			map.getValue().stream()
				.forEach(s -> s.setReserved(true));
		});
	}
}
