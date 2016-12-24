package com.scott.app.OptionsScraper;

public class OptionsScraper {
	final boolean DEBUG = false;

	public static void main(String[] args) {
		OptionsScraper OS = new OptionsScraper();
		OS.go();

	}

	public void go() {
		Stock stock = new Stock("FB", DEBUG);
		stock.loadData();
		stock.print();
	}

}
