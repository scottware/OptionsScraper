package com.scott.app.OptionsScraper;

import org.junit.Test;

import static org.junit.Assert.*;

public class StockTest {

    @Test
    public void testGetSymbol() {
        Stock stock = new Stock("AAPL");
        stock.setSymbol("FB");
        String symbol = stock.getSymbol();
        assertEquals(symbol, "FB");
    }

}
