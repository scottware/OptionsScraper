package com.scott.app.OptionsScraper;

import java.io.IOException;
import java.util.List;

import com.google.api.client.http.HttpRequest;
import com.google.api.client.http.json.JsonHttpContent;
import com.google.api.services.sheets.v4.Sheets;
import com.google.api.services.sheets.v4.model.ValueRange;

public class OptionsScraper {
	final boolean DEBUG = false;

	public static void main(String[] args) {
		OptionsScraper OS = new OptionsScraper();
		OS.go();

	}

	public void go() {
		Stock stock = new Stock("FB", DEBUG);
		stock.loadData();
		// stock.print();

		String spreadsheetId = "1ELoKfVKW-3UKMe5qXlx2uojuUAfJt-mVoT67pDGhlxw";
		String writeRange = "FB!A2:G";

		List<List<Object>> writeData = stock.toDataRange();
		ValueRange valueRange = new ValueRange().setValues(writeData).setMajorDimension("ROWS");
		// System.out.println(valueRange.toString());

		try {
			Sheets service = GoogAuth.getSheetsService();
			 service.spreadsheets().values().update(spreadsheetId, writeRange,
			 valueRange).setValueInputOption("RAW")
			.execute();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

}
