package com.scott.app.OptionsScraper;

import java.util.ArrayList;
import java.util.List;

public class Option {
	public static final int PUT = 1;
	public static final int CALL = 0;

	private String date, strike, price, ask, bid;
	private int type;
	private float ratio;

	public Option(int type, String strike, String price, String ask, String bid) {
		this.setType(type);
		this.setStrike(strike);
		this.setPrice(price);
		this.setAsk(ask);
		this.setBid(bid);
		this.setRatio();
	}

	public String getDate() {
		return date;
	}

	public void setDate(String year, String month, String day) {
		this.date = month + "/" + day + "/" + year;
	}

	public String getStrike() {
		return strike;
	}

	public void setStrike(String strike) {
		this.strike = strike;
	}

	public String getPrice() {
		return price;
	}

	public void setPrice(String price) {
		this.price = price;
	}

	public String getType() {
		if (type == CALL)
			return "CALL";
		else if (type == PUT)
			return "PUT";
		else
			return "unknown";
	}

	public void setRatio() {
		try {
			Float price = Float.parseFloat(this.getPrice());
			Float strike = Float.parseFloat(this.getStrike());
			this.ratio = price / strike;
		} catch (NumberFormatException e) {
			this.ratio = 0.0f;
		}
	}

	public float getRatio() {
		return this.ratio;
	}

	public void setType(int type) {
		this.type = type;
	}

	public String toString() {
		String output = this.getType() + " " + this.getDate() + " $" + this.getStrike() + " $" + this.getPrice() + " "
				+ this.getAsk() + " " + this.getBid() + " " + Float.toString(this.getRatio());
		return output;
	}

	public List<Object> toDataRow() {
		List<Object> dataRow = new ArrayList<>();
		dataRow.add(this.getType());
		dataRow.add(this.getDate());
		dataRow.add("$" + this.getStrike());
		dataRow.add("$" + this.getPrice());
		dataRow.add("$" + this.getAsk());
		dataRow.add("$" + this.getBid());
		dataRow.add(String.format("%1$.2f",100*this.getRatio())+"%");
		return dataRow;
	}

	public String getAsk() {
		return ask;
	}

	public void setAsk(String ask) {
		this.ask = ask;
	}

	public String getBid() {
		return bid;
	}

	public void setBid(String bid) {
		this.bid = bid;
	}
}
