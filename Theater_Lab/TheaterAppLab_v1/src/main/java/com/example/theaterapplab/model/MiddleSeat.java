package com.example.theaterapplab.model;

import com.example.theaterapplab.enums.Section;

public class MiddleSeat extends Seat {

    @Override
    public double getPrice() {
        return this.getSection().getPrice();
    }
}
