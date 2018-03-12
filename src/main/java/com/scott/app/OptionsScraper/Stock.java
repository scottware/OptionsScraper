package com.scott.app.OptionsScraper;

import java.util.*;

import org.json.simple.JSONObject;

public class Stock {
	private JSONObject jsonObject = null;
	private String symbol;
	private Float underlying_price;
	private boolean DEBUG;
	final boolean LEAP = true;
	private ArrayList<Option> options = null;

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

	public void print(int type) {
		System.out.println(this.getSymbol() + "  " + this.getUnderlyingPrice().toString());
		System.out.println("Type Expiration Strike Price Ask Bid");
		if (this.options != null) {
			Iterator<Option> iterator = this.options.iterator();
			while (iterator.hasNext()) {
				Option option = iterator.next();
				if (type == Option.ANY || option.getType() == type)
					System.out.println(option.toString());
			}
		}
	}

}


class SortByAPR implements Comparator<Option> {
	public int compare(Option a, Option b) {
		if (a.getAPR() > b.getAPR()) return -1;
		else if (a.getAPR() < b.getAPR()) return 1;
		else return 0;
	}
}