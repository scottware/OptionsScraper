package com.scott.app.OptionsScraper;

public class OptionsScraper {
	final boolean DEBUG = false;

	public static void main(String[] args) {
		OptionsScraper OS = new OptionsScraper();
		OS.go();

	}

	public void go() {
		Stock AAPL = new Stock("AAPL", DEBUG);
		//AAPL.fetchData();
		StockOptionPrinter sop = new StockOptionPrinter(AAPL);
		sop.printPuts();
	}

}
