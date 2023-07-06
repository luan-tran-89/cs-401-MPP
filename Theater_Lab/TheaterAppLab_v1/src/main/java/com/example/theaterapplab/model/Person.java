package com.example.theaterapplab.model;

import java.util.ArrayList;
import java.util.List;

public class Person {
	private String name;
	private String address;
	private String phone;
	private List<Ticket> tickets;

	public Person(String name, String address, String phone) {
		super();
		this.name = name;
		this.address = address;
		this.phone = phone;
		this.tickets = new ArrayList<>();
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public List<Ticket> getTickets() {
		return tickets;
	}

	public void setTickets(List<Ticket> tickets) {
		this.tickets = tickets;
	}

	public void addAllTickets(List<Ticket> tickets) {
		tickets.forEach(t -> t.setPerson(this));
		this.tickets.addAll(tickets);
	}
}
