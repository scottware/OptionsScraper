package com.scott.app.OptionsScraper;

public class OptionsScraper {
	public static void main(String[] args) {
		OptionsScraper OS = new OptionsScraper();
		OS.go();

	}

	public void go() {
		Stock AAPL = new Stock("AAPL");
		AAPL.fetchData();
	}

}
