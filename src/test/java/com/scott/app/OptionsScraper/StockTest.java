package com.scott.app.OptionsScraper;

import static org.junit.Assert.*;

import org.junit.Test;

public class StockTest {

	@Test
	public void testGetSymbol() {
		Stock stock = new Stock("AAPL");
		stock.setSymbol("FB");
		String symbol = stock.getSymbol();
		assertEquals(symbol, "FB");
	}

}
