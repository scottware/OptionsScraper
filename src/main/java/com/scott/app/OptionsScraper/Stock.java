package com.scott.app.OptionsScraper;

import java.util.*;

import org.json.simple.JSONObject;

public class Stock {
	private JSONObject jsonObject = null;
	private String symbol;
	private Float underlying_price;
	private boolean DEBUG;
	final boolean LEAP = true;
	private ArrayListFilterable<Option> options = null;

	public Stock(String symbol) {
		this(symbol, false);
	}

	public Stock(String symbol, boolean debug) {
		this.DEBUG = debug;
		this.symbol = symbol;
	}

	public void setSymbol(String symbol) {
		this.symbol = symbol;
	}

	public String getSymbol() {
		return this.symbol;
	}

	public void setUnderlyingPrice(String price) {
		this.underlying_price = Float.parseFloat(price);
	}

	public Float getUnderlyingPrice() {
		return this.underlying_price;
	}

	public void loadData(IStockOptionFetcher stockOptionFetcher) {
		IStockOptionFetcher sof = stockOptionFetcher;
		if (options == null) {
			//StockOptionFetcher sof = new StockOptionFetcher(this);
			this.options = sof.fetchData();
		}
	}

	public List<List<Object>> optionChainToDataRange(int type) {
		List<List<Object>> writeData = new ArrayList<>();
		Iterator<Option> iterator = this.options.iterator();
		while (iterator.hasNext()) {
			Option option = iterator.next();
			if (option.getType() == type)
				writeData.add(option.toDataRow());
		}
		return writeData;

	}

	public void sortByAPR() {
		Collections.sort(this.options, new SortByAPR());
	}

	public void print() {
		System.out.println(this.getSymbol() + "  " + this.getUnderlyingPrice().toString());
		System.out.println("Type Expiration Strike Price Ask Bid");
		ArrayListFilterable<Option> q = ArrayListFilterable.filter(options, new Filter());
		if (this.options != null) {
			Iterator<Option> iterator = q.iterator();//this.options.iterator();
			while (iterator.hasNext()) {
				Option option = iterator.next();
				System.out.println(option.toString());
			}
		}
	}

}

class Filter implements Filters<Option> {

	public boolean meetsRequirement(Option element) {

		float apr_low = Float.parseFloat(OptionsScraper.properties.getProperty("filter_apr_low", "0"));
		float apr_high = Float.parseFloat(OptionsScraper.properties.getProperty("filter_apr_high", "9999999"));

		float strike_low = Float.parseFloat(OptionsScraper.properties.getProperty("filter_strike_low", "0"));
		float strike_high = Float.parseFloat(OptionsScraper.properties.getProperty("filter_strike_high", "9999999"));

		String optionTypeString = OptionsScraper.properties.getProperty("option_type", "ANY").toUpperCase();

		int optionType = Option.ANY;
		if (optionTypeString.equals("PUT")) {
			optionType = Option.PUT;
		} else if (optionTypeString.equals("CALL")) {
			optionType = Option.PUT;
		}

		if (element.getAPR() > apr_low && element.getAPR() < apr_high
				&& element.getStrike() > strike_low && element.getStrike() < strike_high
				&& element.isType(optionType))
			return true;
		else
			return false;
	}
}

class SortByAPR implements Comparator<Option> {
	public int compare(Option a, Option b) {
		if (a.getAPR() > b.getAPR()) return -1;
		else if (a.getAPR() < b.getAPR()) return 1;
		else return 0;
	}
}