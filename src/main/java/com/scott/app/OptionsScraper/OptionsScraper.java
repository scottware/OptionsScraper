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
//		OS.read();

	}

	public void read() {

		// Prints the names and majors of students in a sample spreadsheet:
		// https://docs.google.com/spreadsheets/d/1BxiMVs0XRA5nFMdKvBdBZjgmUUqptlbs74OgvE2upms/edit
		String spreadsheetId = "1ELoKfVKW-3UKMe5qXlx2uojuUAfJt-mVoT67pDGhlxw";
		String range = "Test!A1:E";
		ValueRange response;
		try {
			Sheets service = GoogAuth.getSheetsService();
			response = service.spreadsheets().values()
				.get(spreadsheetId, range).execute();
			System.out.println(response.toPrettyString());
			List<List<Object>> values = response.getValues();
			if (values == null || values.size() == 0) {
				System.out.println("No data found.");
			} else {
				System.out.println("Name, Major");
				for (List row : values) {
					// Print columns A and E, which correspond to indices 0 and
					// 4.
					System.out.printf("%s, %s, %s, %s, %s\n", 
							row.get(0), 
							row.get(1), 
							row.get(2), 
							row.get(3), 
							row.get(4));
				}
			}

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void go() {
		Stock stock = new Stock("FB", DEBUG);
		stock.loadData();
		// stock.print();

		String spreadsheetId = "1ELoKfVKW-3UKMe5qXlx2uojuUAfJt-mVoT67pDGhlxw";
		String writeRange = "FB!A2:G";

		List<List<Object>> writeData = stock.toDataRange();
		//ValueRange valueRange = new ValueRange().setValues(writeData).set("NumberFormatType", "PERCENT").setMajorDimension("ROWS");
		ValueRange valueRange = new ValueRange().setValues(writeData).setMajorDimension("ROWS");
		 System.out.println(valueRange.toString());

		try {
			Sheets service = GoogAuth.getSheetsService();
			service.spreadsheets().values().update(spreadsheetId, writeRange, valueRange).setValueInputOption("USER_ENTERED")
					.execute();
			
			} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

}
