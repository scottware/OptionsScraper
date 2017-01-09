package com.scott.app.OptionsScraper;

import com.google.api.services.sheets.v4.model.CellData;
import com.google.api.services.sheets.v4.model.CellFormat;
import com.google.api.services.sheets.v4.model.ExtendedValue;
import com.google.api.services.sheets.v4.model.NumberFormat;

public class SheetsHelper {

	public static void setCellDataFormatPercent(CellData cd) {
		cd.setUserEnteredFormat(
				new CellFormat().setNumberFormat(new NumberFormat().setPattern("#.#%").setType("PERCENT")));
	}

	public static CellData setCellDataFormatCurrency(CellData cd) {
		cd.setUserEnteredFormat(
				new CellFormat().setNumberFormat(new NumberFormat().setPattern("$#.00").setType("CURRENCY")));
		return cd;
	}

	public static CellData setNumberValue(CellData cd, double num) {
		ExtendedValue ev = new ExtendedValue().setNumberValue(num);
		cd.setUserEnteredValue(ev);
		return cd;
	}

	public static CellData setStringValue(CellData cd, String value) {
		ExtendedValue ev = new ExtendedValue().setStringValue(value);
		cd.setUserEnteredValue(ev);
		return cd;
	}
}
