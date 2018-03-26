package com.scott.app.OptionsScraper;

import java.util.Date;

public class TestOptionsFetcher implements IStockOptionFetcher {

	Stock stock;

	@Override
	public IStockOptionFetcher setStock(Stock stock) {
		this.stock = stock;
		return this;
	}

	@Override
	public ArrayListFilterable<Option> fetchData() {
		ArrayListFilterable<Option> options = new ArrayListFilterable<Option>();
		//may 18 1475 (as of 3/25/18 AMZN @1495.56)

		Date today = new Date(1521997200l * 1000l); // Sun Mar 25 2017 10:00 PST
		Option option = new Option(stock, Option.PUT, 1475.0f, 82.50f, 81.4f, 83.1f, today);
		option.setExpirationDate(1526601600l);
		option.setAPR();
		options.add(option);

		return options;
	}
}
