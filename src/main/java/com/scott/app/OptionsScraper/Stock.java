package com.scott.app.OptionsScraper;

public class Stock {
	String symbol;

	public Stock(String symbol) {
		this.symbol = symbol;
	}

	public void setSymbol(String symbol) {
		this.symbol = symbol;
	}

	public String getSymbol() {
		return this.symbol;
	}
}