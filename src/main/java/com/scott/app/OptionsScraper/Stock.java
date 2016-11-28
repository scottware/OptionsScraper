package com.scott.app.OptionsScraper;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

public class Stock {
	private JSONObject jsonObject = null;
	private String symbol;
	private String underlying_price;
	private boolean DEBUG;
	final boolean LEAP = true;
	private Set<Option> options = null;

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
		this.underlying_price = price;
	}

	public String getUnderlyingPrice() {
		return this.underlying_price;
	}

	public void loadData() {
		if (options == null) {
			StockOptionFetcher sof = new StockOptionFetcher(this);
			this.options = sof.fetchData();
		}
	}

	public void print() {
		System.out.println(this.getSymbol() + "  " + this.getUnderlyingPrice());
		System.out.println("Type Expiration Strike Price Ask Bid");
		Iterator<Option> iterator = this.options.iterator();
		while (iterator.hasNext()) {
			Option option = iterator.next();
			System.out.println(option.toString());
		}

	}

}