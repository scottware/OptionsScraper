package com.scott.app.OptionsScraper;

import java.util.Map;
import org.json.simple.parser.ParseException;

public class OptionsScraper {
	public static void main(String[] args) {
		System.out.println("test");
		OptionsScraper OS = new OptionsScraper();
		OS.go();

	}

	public void go() {

		String scrapedURL;
		if (false)
			scrapedURL = "{expiry:{y:2014,m:4,d:4},expirations:[{y:2017,m:1,d:20},{y:2017,m:3,d:17},{y:2017,m:4,d:21},{y:2017,m:6,d:16},{y:2017,m:11,d:17},{y:2018,m:1,d:19}],underlying_;id:\"22144\",underlying_price:111.79}";
		else {
			String dateurl = "http://www.google.com/finance/option_chain?q=AAPL&expd=4&expm=4&expy=2014&output=json";
			String leapurl = "http://www.google.com/finance/option_chain?q=AAPL&output=json";
			Scraper scraper = new Scraper();
			scrapedURL = Scraper.getURL(dateurl);
		}

		scrapedURL = Scraper.cleanJSON(scrapedURL);
		System.out.println(scrapedURL);

		try {
			Map<?, ?> map = JSONHelper.parseText(scrapedURL);
			JSONHelper.printJson(map);
		} catch (ParseException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
	}

}
