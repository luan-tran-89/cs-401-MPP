package com.example.theaterapplab;

import javafx.fxml.FXML;
import javafx.scene.control.Label;

public class TheaterController {
    @FXML
    private Label welcomeText;

    @FXML
    protected void onHelloButtonClick() {
        welcomeText.setText("Welcome to JavaFX Application!");
    }
}