package com.example.theaterapplab.model;

public class BackSeat extends Seat {

    @Override
    public double getPrice() {
        return this.getSection().getPrice();
    }
}
