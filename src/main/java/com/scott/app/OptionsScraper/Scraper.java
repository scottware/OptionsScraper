package com.scott.app.OptionsScraper;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.io.InputStream;
import java.net.URL;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class Scraper {

	static protected String getURL(String u) {
		URL url;
		InputStream is;
		InputStreamReader isr;
		BufferedReader r;
		String str;
		StringBuilder response = new StringBuilder();

		try {
			System.out.println("Reading URL: " + u);
			url = new URL(u);
			is = url.openStream();
			isr = new InputStreamReader(is);
			r = new BufferedReader(isr);
			while ((str = r.readLine()) != null)
				response.append(str);
			// do {
			// str = r.readLine();
			// if (str != null)
			// System.out.println(str);
			// } while (str != null);
		} catch (MalformedURLException e) {
			System.out.println("Must enter a valid URL");
		} catch (IOException e) {
			System.out.println("Can not connect");
		}

		return response.toString();
	}

	public static String cleanJSON(String input) {
		String patternString1 = "underlying_;id";
		String patternString2 = "(\\w+:)(\\d+\\.?\\d*)";
		String patternString3 = "(\\w+):";

		Pattern pattern1 = Pattern.compile(patternString1);
		Pattern pattern2 = Pattern.compile(patternString2);
		Pattern pattern3 = Pattern.compile(patternString3);

		String replaceAll;
		Matcher matcher1 = pattern1.matcher(input);
		replaceAll = matcher1.replaceAll("underlying_id");
		Matcher matcher2 = pattern2.matcher(replaceAll);
		replaceAll = matcher2.replaceAll("$1\"$2\"");
		Matcher matcher3 = pattern3.matcher(replaceAll);
		replaceAll = matcher3.replaceAll("\"$1\":");
		
		return replaceAll;
	}

}
