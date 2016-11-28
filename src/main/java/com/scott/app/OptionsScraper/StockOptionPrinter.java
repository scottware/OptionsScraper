package com.scott.app.OptionsScraper;

import java.util.Iterator;
import java.util.Map;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.ParseException;

public class StockOptionPrinter {

	Stock stock;

	public StockOptionPrinter(Stock stock) {
		this.stock = stock;
	}

	public static void printJSON(String json) {
		Map<?, ?> map = null;
		try {
			map = JSONHelper.parseText(json);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		JSONHelper.printJson(map);
	}
	
	public void printPuts() {
		JSONObject jsonObject = this.stock.getJsonObject();

//		Object oPuts = (Object) jsonObject.get("puts");
//		JSONArray puts = (JSONArray) oPuts;
//		Iterator<String> iterator = puts.iterator();
//		while (iterator.hasNext()) {
//			Object o = iterator.next();
//			JSONObject jo = (JSONObject) o;
//			System.out.println(jo);
//			System.out.println(jo.get("strike"));
//		}
/*
		   "a": ask price
		   "b": bid price
		   "c": change value
		   "cid": some identity code.  I don't know
		   "cp": I don't know
		   "cs": change direction.  "chg" = up, "cur" = down
		   "e":  I think this tells us something about what country where the stock is traded.  
		         "OPRA" means USA.
		   "expiry": expiration date for this option
		   "name": I don't know.  I have never seen a value for this
		   "oi": open interest. How many of these are currently being held by others. 
		         See, http://www.investopedia.com/terms/o/openinterest.asp
		   "p": price, last
		   "s": option code. 
		        Basically, Stock Symbol + 7 if mini option + date + "C" or "P" + price
		   "strike": strike price for this option
		   "vol": the volume of options traded. 
*/
		   
	}
}
