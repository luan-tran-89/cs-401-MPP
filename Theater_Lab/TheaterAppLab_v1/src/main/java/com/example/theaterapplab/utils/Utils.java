package com.example.theaterapplab.utils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.text.NumberFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

public class Utils {
	private static final NumberFormat numberFormat = NumberFormat.getCurrencyInstance(Locale.US);
	private static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MM/dd/yy hh:mm");

	public static String getPrice(double price) {
		return numberFormat.format(price);
	}

	public static String fomatDateTime(LocalDateTime dateTime) {
		return dateTime.format(formatter);
	}

	public static void putText(String s) {
		System.out.println(s);
	}

	public static String getString() throws IOException {
		InputStreamReader isr = new InputStreamReader(System.in);
		BufferedReader br = new BufferedReader(isr);
		String s = br.readLine();
		return s;
	}

	public static char getChar() throws IOException {
		String s = getString();
		return s.charAt(0);
	}

	public static int getInt() throws IOException {
		String s = getString();
		return Integer.parseInt(s);
	}
}
