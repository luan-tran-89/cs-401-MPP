package com.example.theaterapplab.model;

import com.example.theaterapplab.utils.Utils;

import java.time.LocalDateTime;
import java.util.Objects;

public class Performance {
	private int id;
	private String name;
	private LocalDateTime startTime;
	private LocalDateTime endTime;

	public Performance() {
		super();
	}

	public Performance(int id, String name, LocalDateTime startTime, LocalDateTime endTime) {
		super();
		this.id = id;
		this.name = name;
		this.startTime = startTime;
		this.endTime = endTime;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public LocalDateTime getStartTime() {
		return startTime;
	}

	public void setStartTime(LocalDateTime startTime) {
		this.startTime = startTime;
	}

	public LocalDateTime getEndTime() {
		return endTime;
	}

	public void setEndTime(LocalDateTime endTime) {
		this.endTime = endTime;
	}

	@Override
	public String toString() {
		return String.format("%s  |  %s  |  %-8s  |  %-8s",
				id,
				name, 
				Utils.fomatDateTime(startTime),
				Utils.fomatDateTime(endTime));
	}

	@Override
	public int hashCode() {
		return Objects.hash(id);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Performance other = (Performance) obj;
		return id == other.id;
	}
	
}
