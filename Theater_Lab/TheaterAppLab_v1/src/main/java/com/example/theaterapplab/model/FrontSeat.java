package com.example.theaterapplab.model;

public class FrontSeat  extends Seat {

    @Override
    public double getPrice() {
        return this.getSection().getPrice();
    }
}
