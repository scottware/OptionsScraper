package com.scott.app.OptionsScraper;

import java.util.Iterator;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

public class StockOptionPrinter {

	Stock stock;

	public StockOptionPrinter(Stock stock) {
		this.stock = stock;
	}

	public void printPuts() {
		JSONObject jsonObject = this.stock.getJsonObject();

		Object oPuts = (Object) jsonObject.get("puts");
		JSONArray puts = (JSONArray) oPuts;
		Iterator<String> iterator = puts.iterator();
		while (iterator.hasNext()) {
			Object o = iterator.next();
			JSONObject jo = (JSONObject) o;
			System.out.println(jo.get("strike"));
		}

	}
}
