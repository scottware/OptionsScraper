package com.scott.app.OptionsScraper;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import com.google.api.services.sheets.v4.Sheets;
import com.google.api.services.sheets.v4.model.BatchUpdateSpreadsheetRequest;
import com.google.api.services.sheets.v4.model.BatchUpdateSpreadsheetResponse;
import com.google.api.services.sheets.v4.model.CellData;
import com.google.api.services.sheets.v4.model.GridRange;
import com.google.api.services.sheets.v4.model.Request;
import com.google.api.services.sheets.v4.model.RowData;
import com.google.api.services.sheets.v4.model.Sheet;
import com.google.api.services.sheets.v4.model.Spreadsheet;
import com.google.api.services.sheets.v4.model.UpdateCellsRequest;
import com.google.api.services.sheets.v4.model.ValueRange;
import com.scott.app.OptionsScraper.Google.GoogleStockOptionFetcher;
import com.scott.app.OptionsScraper.TDA.TDAStockOptionFetcher;
import com.scott.app.OptionsScraper.Yahoo.YahooStockOptionFetcher;

public class OptionsScraper {
	final boolean DEBUG = false;
	private final String spreadsheetId = "1ELoKfVKW-3UKMe5qXlx2uojuUAfJt-mVoT67pDGhlxw";
	public static Properties defaultProperties, googleDocProperties;
	static boolean writeToGoogleDocs = false;

	enum Source {
		TDA, GOOG, YHOO
	}

	public static void main(String[] args) {

		OptionsScraper OS = new OptionsScraper();
		try {
			defaultProperties = OS.loadProperties("defaultProperties");
		} catch (IOException e) {
			e.printStackTrace();
			return;
		}

		if (Boolean.parseBoolean(defaultProperties.getProperty("google_docs"))) {
			try {
				googleDocProperties = OS.loadProperties(defaultProperties.getProperty("google_docs_properties"));
				writeToGoogleDocs = true;
			} catch (IOException e) {
				e.printStackTrace();
				return;
			}
		}
//		String symbol = "^GSPC";

		// Set the symbol to research
		String symbol = defaultProperties.getProperty("symbol".toLowerCase()).toUpperCase();

		// Set the data source to scrape from
		Source dataSource = Source.YHOO;
		try {
			dataSource = Source.valueOf(defaultProperties.getProperty("source".toLowerCase(), "YHOO").toUpperCase());
		} catch (IllegalArgumentException e) {
			//e.printStackTrace();
		}
		System.out.println("Source: " + dataSource);


		Stock stock = OS.go(symbol, dataSource);
		stock.sortByAPR();
		stock.print();

		if (writeToGoogleDocs) {
			int sheetId = Integer.parseInt(googleDocProperties.getProperty("sheet_id"));
			OS.writeToGoogleSheets(stock, sheetId);
		}
	}

	public Properties loadProperties(String propertiesFile) throws FileNotFoundException, IOException {
		String property = System.getProperty("user.dir");
		System.out.println("PATH: " + property);
		// create and load default properties
		Properties defaultProps = new Properties();
		FileInputStream in = new FileInputStream(propertiesFile);
		defaultProps.load(in);
		in.close();

// create application properties with default
		Properties applicationProps = new Properties(defaultProps);
		return applicationProps;
	}


	public void exampleOfReadingFromGoogleSheets() {

		// Prints the names and majors of students in a sample spreadsheet:
		// https://docs.google.com/spreadsheets/d/1BxiMVs0XRA5nFMdKvBdBZjgmUUqptlbs74OgvE2upms/edit
		String range = "Test!A1:E";
		ValueRange response;
		try {
			Sheets service = GoogAuth.getSheetsService(googleDocProperties.getProperty("client_secret_path"));
			response = service.spreadsheets().values().get(this.spreadsheetId, range).execute();
			System.out.println(response.toPrettyString());
			List<List<Object>> values = response.getValues();
			if (values == null || values.size() == 0) {
				System.out.println("No data found.");
			} else {
				System.out.println("Name, Major");
				for (List row : values) {
					// Print columns A and E, which correspond to indices 0 and
					// 4.
					System.out.printf("%s, %s, %s, %s, %s\n", row.get(0), row.get(1), row.get(2), row.get(3),
							row.get(4));
				}
			}

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void testStuff(int sheetId, String symbol) {

		Sheets service;

		try {
			service = GoogAuth.getSheetsService(defaultProperties.getProperty("client_secret_path"));
			Spreadsheet response1;
			response1 = service.spreadsheets().get(this.spreadsheetId).setIncludeGridData(false).execute();
			List<Sheet> workSheetList = response1.getSheets();
			for (Sheet sheet : workSheetList) {
				System.out.println(sheet.getProperties().getSheetId() + ": " + sheet.getProperties().getTitle());
			}
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		this.setHeaders(sheetId);
		this.setCurrentPrice(sheetId, 777.77f);
	}

	public void setHeaders(int sheetId) {
		List<CellData> cellData = new ArrayList<CellData>();
		cellData.add(SheetsHelper.setStringValue(new CellData(), "TYPE"));
		cellData.add(SheetsHelper.setStringValue(new CellData(), "EXPIRATION"));
		cellData.add(SheetsHelper.setStringValue(new CellData(), "STRIKE"));
		cellData.add(SheetsHelper.setStringValue(new CellData(), "PRICE"));
		cellData.add(SheetsHelper.setStringValue(new CellData(), "ASK"));
		cellData.add(SheetsHelper.setStringValue(new CellData(), "BID"));
		cellData.add(SheetsHelper.setStringValue(new CellData(), "RATIO"));
		cellData.add(SheetsHelper.setStringValue(new CellData(), "APR"));
		this.updateRows(sheetId, 0, 0, cellData);
	}

	public void updateRows(int sheetId, int row, int column, List<CellData> cellData) {
		List<RowData> rowData = new ArrayList<RowData>();
		rowData.add(new RowData().setValues(cellData));

		BatchUpdateSpreadsheetRequest batchRequests = new BatchUpdateSpreadsheetRequest();
		BatchUpdateSpreadsheetResponse response;
		List<Request> requests = new ArrayList<Request>();

		UpdateCellsRequest updateCellReq = new UpdateCellsRequest();
		updateCellReq.setRange(new GridRange().setSheetId(sheetId).setStartRowIndex(row).setStartColumnIndex(column));
		updateCellReq.setRows(rowData);
		updateCellReq.setFields("userEnteredValue,userEnteredFormat.numberFormat");
		requests = new ArrayList<Request>();
		requests.add(new Request().setUpdateCells(updateCellReq));
		batchRequests = new BatchUpdateSpreadsheetRequest();
		batchRequests.setRequests(requests);

		try {
			System.out.println(updateCellReq.toPrettyString());
			Sheets service = GoogAuth.getSheetsService(googleDocProperties.getProperty("client_secret_path"));
			response = service.spreadsheets().batchUpdate(this.spreadsheetId, batchRequests).execute();
			System.out.println(response.toPrettyString());
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	public void setCurrentPrice(int sheetId, float currentPrice) {
		CellData cd = new CellData();

		SheetsHelper.setCellDataFormatCurrency(cd);
		SheetsHelper.setNumberValue(cd, currentPrice);

		List<CellData> cellData = new ArrayList<CellData>();
		cellData.add(cd);
		this.updateRows(sheetId, 0, 8, cellData);
	}

	public Stock go(String symbol, Source optionServiceSource) {
		Stock stock = new Stock(symbol, DEBUG);

		switch (optionServiceSource) {
			case TDA:
				stock.loadData(new TDAStockOptionFetcher().setStock(stock));
				break;
			case GOOG:
				stock.loadData(new GoogleStockOptionFetcher().setStock(stock));
				break;
			case YHOO:
				stock.loadData(new YahooStockOptionFetcher().setStock(stock));
				break;
		}

		return stock;

	}

	public void writeToGoogleSheets(Stock stock, int sheetId) {
		this.setHeaders(sheetId);
		String writeRange = stock.getSymbol() + "!A2:H";
		this.setCurrentPrice(sheetId, stock.getUnderlyingPrice());

		List<List<Object>> writeData = stock.optionChainToDataRange(Option.PUT);

		ValueRange valueRange = new ValueRange().setValues(writeData).setMajorDimension("ROWS");
		System.out.println(valueRange.toString());
		try {
			Sheets service = GoogAuth.getSheetsService(googleDocProperties.getProperty("client_secret_path"));
			service.spreadsheets().values().update(this.spreadsheetId, writeRange, valueRange)
					.setValueInputOption("USER_ENTERED").execute();

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}
}
