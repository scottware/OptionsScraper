package com.scott.app.OptionsScraper;

public class Option {
	public static final int PUT = 1;
	public static final int CALL = 0;

	private String date, strike, price, ask, bid;
	private int type;

	public Option(int type, String strike, String price, String ask, String bid) {
		this.setType(type);
		this.setStrike(strike);
		this.setPrice(price);
		this.setAsk(ask);
		this.setBid(bid);
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

	public void setType(int type) {
		this.type = type;
	}

	public String toString() {
		String output = this.getType() + " " + this.getDate() + " $" + this.getStrike() + " $" + this.getPrice() + " "+this.getAsk() + " " + this.getBid();
		return output;
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
