package com.scott.app.OptionsScraper.Yahoo;

import com.scott.app.OptionsScraper.*;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import java.lang.reflect.Array;
import com.scott.app.OptionsScraper.ArrayListFilterable;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

public class YahooStockOptionFetcher implements IStockOptionFetcher {


	String optionUrl = "https://query1.finance.yahoo.com/v7/finance/options/";

	// https://query1.finance.yahoo.com/v7/finance/options/AMZN180302C01497500
	String optionDateBaseUrl = "https://query1.finance.yahoo.com/v7/finance/options/";
	String equityUrl = "https://query1.finance.yahoo.com/v7/finance/quote?symbols=";

	private Stock stock;

	public YahooStockOptionFetcher setStock(Stock stock) {
		this.stock = stock;
		equityUrl += stock.getSymbol();
		optionUrl += stock.getSymbol();
		optionDateBaseUrl += stock.getSymbol() + "?date=";
		return this;
	}

	@Override
	public ArrayListFilterable<Option> fetchData() {

		ArrayListFilterable<Option> options = new ArrayListFilterable<Option>();
		stock.setUnderlyingPrice(fetchSharePrice());

		Iterator expirationDatesIterator = fetchOptionDates();

		while (expirationDatesIterator.hasNext()) {
			String expirationDate = expirationDatesIterator.next().toString();
			String OptionDateUrl = optionDateBaseUrl + expirationDate;

			ArrayListFilterable<Option> tempSet = fetchExpriationDate(OptionDateUrl);
			options.addAll(tempSet);
		}

		return options;
	}

	ArrayListFilterable<Option> fetchExpriationDate(String optionDateUrl) {
		// Given a URL for an expiration date, fetch the content and return a Set of all options for that expiration

		ArrayListFilterable<Option> optionSet = new ArrayListFilterable<>();

		String scrapedURL = Scraper.getURL(optionDateUrl);
		JSONObject data = JSONHelper.parseData(scrapedURL);
		JSONObject optionChain = (JSONObject) data.get("optionChain");
		JSONObject result = (JSONObject) ((JSONArray) optionChain.get("result")).get(0);

		JSONObject optionList = (JSONObject) ((JSONArray) result.get("options")).get(0);


		JSONArray puts = (JSONArray) optionList.get("puts");
		Iterator putIterator = puts.iterator();
		while (putIterator.hasNext()) {
			JSONObject putJSONObject = (JSONObject) putIterator.next();
			Float strike = Float.parseFloat(putJSONObject.get("strike").toString());
			Float bid = Float.parseFloat(putJSONObject.get("bid").toString());
			Float ask = Float.parseFloat(putJSONObject.get("ask").toString());
			Float lastPrice = Float.parseFloat(putJSONObject.get("lastPrice").toString());
			Long expiration = Long.parseLong(putJSONObject.get("expiration").toString());
			Option option = new Option(stock, Option.PUT, strike, lastPrice, ask, bid);
			option.setDate(expiration);
			option.setAPR();
			optionSet.add(option);
		}
		JSONArray calls = (JSONArray) optionList.get("calls");
		Iterator callIterator = calls.iterator();
		while (callIterator.hasNext()) {
			JSONObject callJSONObject = (JSONObject) callIterator.next();
			Float strike = Float.parseFloat(callJSONObject.get("strike").toString());
			Float bid = Float.parseFloat(callJSONObject.get("bid").toString());
			Float ask = Float.parseFloat(callJSONObject.get("ask").toString());
			Float lastPrice = Float.parseFloat(callJSONObject.get("lastPrice").toString());
			Long expiration = Long.parseLong(callJSONObject.get("expiration").toString());
			Option option = new Option(stock, Option.CALL, strike, lastPrice, ask, bid);
			option.setDate(expiration);
			option.setAPR();
			optionSet.add(option);
		}
		return optionSet;
	}

	Iterator fetchOptionDates() {
		// Given a URL for an option chain, fetch and parse. Locate the dates (in unixtime format)
		// return as an iterator to a list of dates

		String scrapedURL = Scraper.getURL(optionUrl);
		JSONObject data = JSONHelper.parseData(scrapedURL);
		JSONObject optionChain = (JSONObject) data.get("optionChain");
		JSONObject result = (JSONObject) ((JSONArray) optionChain.get("result")).get(0);
		JSONArray expirationDates = (JSONArray) result.get("expirationDates");
		return expirationDates.iterator();
	}

	String fetchSharePrice() {
		// Fetch the top level equity data. Parse out and return the current price.

		String scrapedEquity = Scraper.getURL(equityUrl);
		JSONObject data = JSONHelper.parseData(scrapedEquity);
		System.out.println(data);


		JSONObject price = (JSONObject) data.get("quoteResponse");
		JSONArray result = (JSONArray) price.get("result");
		JSONObject item = (JSONObject) result.get(0);
		return item.get("regularMarketPrice").toString();
	}
}