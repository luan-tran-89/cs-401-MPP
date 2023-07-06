package com.example.theaterapplab.model;

import com.example.theaterapplab.enums.Section;

import java.util.Objects;

public class Ticket {
	private final double defaultTax = 5;

	private double tax = defaultTax;
	private Person person;
	private Seat seat;
	private Performance performance;

	public Ticket(Performance performance, Seat seat) {
		super();
		this.performance = performance;
		this.seat = seat;
	}

	public Ticket(Person person, Seat seat) {
		super();
		this.person = person;
		this.seat = seat;
	}
	
	public Ticket(Person person, Seat seat, double tax) {
		this(person, seat);
		this.tax = tax;
	}

	public double getTax() {
		return tax;
	}

	public void setTax(double tax) {
		this.tax = tax;
	}

	public Person getPerson() {
		return person;
	}

	public void setPerson(Person person) {
		this.person = person;
	}

	public Seat getSeat() {
		return seat;
	}

	public void setSeat(Seat seat) {
		this.seat = seat;
	}
	
	public Performance getPerformance() {
		return performance;
	}

	public void setPerformance(Performance performance) {
		this.performance = performance;
	}

	private double getSeatPrice() {
		return Section.getSeatPrice(seat.getRow());
	}

	public double getTotalPrice() {
		double seatPrice = this.getSeatPrice();
		return seatPrice + ((seatPrice * tax)/100);
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;
		Ticket ticket = (Ticket) o;
		return Objects.equals(person, ticket.person) && Objects.equals(seat, ticket.seat) && Objects.equals(performance, ticket.performance);
	}

	@Override
	public int hashCode() {
		return Objects.hash(person, seat, performance);
	}
}
