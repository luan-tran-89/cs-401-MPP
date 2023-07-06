package com.example.theaterapplab;

import com.example.theaterapplab.model.*;
import com.example.theaterapplab.utils.Utils;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.IntStream;

public class TheaterManagement {
	private static int COLUMNS = 12;
	private static int ROWS = 15;

	public static void main(String[] args) throws IOException {
		TheaterManagement.lanchApp();

//		while (true) {
//			Utils.putText("Enter first letter of ");
//
//			Utils.putText("showAllEmptySeats (a), showAllReservedSeats (b), reserveSeat (c), "
//					+ "unreserveSeat (d), emptyAllSeats (e), reserveAllSeats (f) or quit (q): ");
//			int choice = Utils.getChar();
//			switch (choice) {
//				case 'a':
//					// a) Show ALL empty seats.
//					theater.showAllEmptySeats();
//					break;
//				case 'b':
//					// b) Show All seats that are already reserved.
//					theater.showAllReservedSeats();
//					break;
//				case 'c':
//					System.out.printf("Enter column from %s to %s: ", 1, column);
//					columnInput = Utils.getInt();
//					System.out.printf("Enter row  from %s to %s: ", 1, row);
//					rowInput = Utils.getInt();
//					Utils.putText("Enter person name: ");
//					String name = Utils.getString();
//					Utils.putText("Enter person address ");
//					String address = Utils.getString();
//					
//					// c) Reserve a seat for a person.
//					theater.reserveSeat(columnInput, rowInput, new Person(name, address));
//					break;
//				case 'd':
//					System.out.printf("Enter column from %s to %s: ", 1, column);
//					columnInput = Utils.getInt();
//					System.out.printf("Enter row  from %s to %s: ", 1, row);
//					rowInput = Utils.getInt();
//					
//					// d) Unreserve a seat (the seat becomes free).
//					theater.unreserveSeat(columnInput, rowInput);
//					break;
//				case 'e':
//					// e) Empty all seats in the theater.
//					theater.emptyAllSeats();
//					break;
//				case 'f':
//					// f) Reserve all seats in the theater.
//					theater.reserveAllSeats();
//					break;
//				case 'q':
//					return;
//				default:
//					Utils.putText("Invalid entry\n");
//			}
//		}
	}
	
	public static void lanchApp() throws IOException {
		int columnInput, rowInput;
		
		TheaterManagement management = new TheaterManagement();
		
		SeatingChart seatingChart = new SeatingChart("Main Floor", COLUMNS, ROWS);
		Theater theater = new Theater("My Theater", seatingChart);
		
		List<PerformanceTicket> performanceTickets = management.getPerformanceTickets();
		List<Performance> performances = performanceTickets.stream().map(PerformanceTicket::getPerformance).toList();
		
//		Map<Performance, PerformanceTicket> aaa = performanceTickets.stream()
//				.collect(Collectors.groupingBy(PerformanceTicket::getPerformance));
		
		Utils.putText("List of performances:");
		IntStream.range(0, performances.size()).forEach(index -> {
			System.out.printf("Select (%s) - %s%n", index, performances.get(index).toString());
		});
		
		Performance performance = management.choosePerformance(performances);
		if (performance == null) {
			return;
		}
		System.out.println(performance);
		
		PerformanceTicket performanceTicket = performanceTickets.stream().filter(s -> s.getPerformance().equals(performance)).findAny().get();
		
		
		while (true) {
			Utils.putText("Enter first letter of ");
	
			Utils.putText("showAllEmptySeats (a), showAllReservedSeats (b), reserveSeat (c), "
					+ "unreserveSeat (d), emptyAllSeats (e), reserveAllSeats (f) or quit (q): ");
			int choice = Utils.getChar();
			switch (choice) {
				case 'a':
					// a) Show ALL empty seats.
					performanceTicket.showAllEmptySeats();
					break;
				case 'b':
					// b) Show All seats that are already reserved.
					performanceTicket.showAllReservedSeats();
					break;
				case 'c':
					System.out.printf("Enter column from %s to %s: ", 1, COLUMNS);
					columnInput = Utils.getInt();
					System.out.printf("Enter row  from %s to %s: ", 1, ROWS);
					rowInput = Utils.getInt();
					Utils.putText("Enter person name: ");
					String name = Utils.getString();
					Utils.putText("Enter person address ");
					String address = Utils.getString();
					
					// c) Reserve a seat for a person.
					performanceTicket.reserveSeat(columnInput, rowInput);
					break;
				case 'd':
					System.out.printf("Enter column from %s to %s: ", 1, COLUMNS);
					columnInput = Utils.getInt();
					System.out.printf("Enter row  from %s to %s: ", 1, ROWS);
					rowInput = Utils.getInt();
					
					// d) Unreserve a seat (the seat becomes free).
					performanceTicket.unreserveSeat(columnInput, rowInput);
					break;
				case 'e':
					// e) Empty all seats in the theater.
					performanceTicket.emptyAllSeats();
					break;
				case 'f':
					// f) Reserve all seats in the theater.
					performanceTicket.reserveAllSeats();
					break;
				case 'q':
					return;
				default:
					Utils.putText("Invalid entry\n");
			}
		}
	}
	
	public static List<PerformanceTicket> getPerformanceTickets() {
		List<PerformanceTicket> performanceTickets = new ArrayList<>();
		TheaterManagement.getPerformances().forEach(p ->
			performanceTickets.add(new PerformanceTicket(p, new SeatingChart("Main Floor", COLUMNS, ROWS))));
		return performanceTickets;
	}
	
	public static List<Performance> getPerformances() {
		LocalDateTime date = LocalDateTime.of(2022, 10, 10, 14, 0);
		
		LocalDateTime date1 = LocalDateTime.of(2022, 10, 12, 14, 0);
		List<Performance> performances = new ArrayList<>() {
			{
				add(new Performance(1, "Romeo And Juliet", date, date.plusHours(3)));
				add(new Performance(2, "Only an Octave Apart", date, date.plusHours(3)));
				add(new Performance(3, "Blindness", date, date.plusHours(3)));
				add(new Performance(4, "Romeo And Juliet", date1, date1.plusHours(3)));
			}
		};
		
		return performances;
	}
	
	public Performance choosePerformance(List<Performance> performances) throws IOException {
		Utils.putText("Please choose a performance:");
		int choice = Utils.getInt();
		if (choice < 0 || choice > performances.size()) {
			Utils.putText("Invalid selection!!!");
			return null;
		}
		return performances.get(choice);
	}
	
}
