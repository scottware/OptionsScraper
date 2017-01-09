package com.scott.app.OptionsScraper;

import com.google.api.services.sheets.v4.Sheets;
import com.google.api.services.sheets.v4.Sheets.Spreadsheets.Values;
import com.google.api.services.sheets.v4.model.ValueRange;

public class UpdateTest extends com.google.api.services.sheets.v4.Sheets.Spreadsheets.Values.Update
 {

	protected UpdateTest(Values values, String spreadsheetId, String range, ValueRange content) {
		values.super(spreadsheetId, range, content);
		// TODO Auto-generated constructor stub
	}

}
