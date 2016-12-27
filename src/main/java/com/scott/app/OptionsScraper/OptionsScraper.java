package com.scott.app.OptionsScraper;

import java.io.IOException;

public class OptionsScraper {
	final boolean DEBUG = false;

	public static void main(String[] args) {
		OptionsScraper OS = new OptionsScraper();
		OS.go();

	}

	public void go() {
//		Stock stock = new Stock("FB", DEBUG);
//		stock.loadData();
//		stock.print();
		
		GoogAuth GA = new GoogAuth();
		try {
			GA.foo("Options Worksheet");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
