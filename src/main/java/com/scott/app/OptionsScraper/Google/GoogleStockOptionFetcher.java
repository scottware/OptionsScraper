package com.scott.app.OptionsScraper.Google;

import com.scott.app.OptionsScraper.ArrayListFilterable;

import java.util.Calendar;
import java.util.Date;
import java.util.Iterator;
import java.util.TimeZone;

import com.scott.app.OptionsScraper.*;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

// https://query1.finance.yahoo.com/v7/finance/options/AMZN
// https://query1.finance.yahoo.com/v7/finance/options/AMZN180302C01497500

public class GoogleStockOptionFetcher implements IStockOptionFetcher {
	private Stock stock;

	public GoogleStockOptionFetcher setStock(Stock stock) {
		this.stock = stock;
		return this;
	}

	public String getLeapURL() {
		return "http://www.google.com/finance/option_chain?q=" + this.stock.getSymbol() + "&output=json";
	}

	public String getDateURL(String year, String month, String day) {
		return "http://www.google.com/finance/option_chain?q=" + this.stock.getSymbol() + "&expd=" + day + "&expm="
				+ month + "&expy=" + year + "&output=json";
	}

	public ArrayListFilterable<Option> fetchData() {
		JSONObject leapObject = fetchLeapData();
		// System.out.println(leapObject.toString());
		ArrayListFilterable<Option> options = new ArrayListFilterable<>();

		Object oExpirations = (Object) leapObject.get("expirations");
		JSONArray expirations = (JSONArray) oExpirations;
		Iterator<String> iterator = expirations.iterator();
		while (iterator.hasNext()) {
			Object o = iterator.next();
			JSONObject jo = (JSONObject) o;
			String day = jo.get("d").toString();
			String month = jo.get("m").toString();
			String year = jo.get("y").toString();
			JSONObject d = this.fetchDateData(year, month, day);
			System.out.println(year + "/" + month + "/" + day);

			// PUTS
			Object oPuts = (Object) d.get("puts");
			JSONArray puts = (JSONArray) oPuts;
			this.processOption(puts, options, Option.PUT, year, month, day);
			// CALLS
			Object oCalls = (Object) d.get("calls");
			JSONArray calls = (JSONArray) oCalls;
			this.processOption(calls, options, Option.CALL, year, month, day);
		}
		return options;

	}

	private void processOption(JSONArray oOptions, ArrayListFilterable<Option> optionSet, int optionType, String year, String month,
			String day) {
		Date today = Calendar.getInstance(TimeZone.getTimeZone("GMT")).getTime();
		JSONArray options = (JSONArray) oOptions;
		Iterator<String> optionsIterator = options.iterator();
		while (optionsIterator.hasNext()) {
			Object oO = optionsIterator.next();
			JSONObject oOption = (JSONObject) oO;
			String sStrike = oOption.get("strike").toString();
			String sPrice = oOption.get("p").toString();
			String sAsk = oOption.get("a").toString();
			String sBid = oOption.get("b").toString();
			try {
				float strike = Float.parseFloat(sStrike);
				float price = Float.parseFloat(sPrice);
				float ask = Float.parseFloat(sAsk);
				float bid = Float.parseFloat(sBid);
				Option option = new Option(stock, optionType, strike, price, ask, bid, today);
				option.setDate(year, month, day);
				optionSet.add(option);
			} catch (NumberFormatException e) {
				//e.printStackTrace();
				//TODO not handling where there are commas: 1,050.00
			}

			// System.out.println(" " + oPut.get("strike").toString() + " "
			// + oPut.get("p").toString());
		}
	}

	public JSONObject fetchLeapData() {
		String scrapedURL;
		String leapurl = this.getLeapURL();

		scrapedURL = Scraper.getURL(leapurl);
		scrapedURL = Scraper.cleanJSON(scrapedURL);
		JSONObject jsonObject = JSONHelper.parseData(scrapedURL);
		this.stock.setUnderlyingPrice(jsonObject.get("underlying_price").toString());
		return jsonObject;
	}

	public JSONObject fetchDateData(String year, String month, String day) {
		String scrapedURL;
		String dateUrl = this.getDateURL(year, month, day);

		scrapedURL = Scraper.getURL(dateUrl);

		scrapedURL = Scraper.cleanJSON(scrapedURL);
		// StockOptionPrinter.printJSON(scrapedURL);
		return JSONHelper.parseData(scrapedURL);
	}


}
