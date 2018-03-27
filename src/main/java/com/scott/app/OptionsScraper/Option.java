package com.scott.app.OptionsScraper;

import java.text.SimpleDateFormat;
import java.util.*;

public class Option {
	public static final int PUT = 1;
	public static final int CALL = 0;
	public static final int ANY = -1;

	private float strike, price, ask, bid;
	private int type;
	private float ratio;
	private Date expirationDate;
	private double apr;
	private Stock stock;
	private final Date calendarInstanceToday;


	public Option(Stock stock, int type, float strike, float price, float ask, float bid, Date calendarInstance) {
		this.calendarInstanceToday = calendarInstance;
		this.setType(type);
		this.setStrike(strike);
		this.setPrice(price);
		this.setAsk(ask);
		this.setBid((bid != 0.0f) ? bid : price);
		this.setRatio();
		this.setStock(stock);
	}

	public String getExpirationDate() {
		SimpleDateFormat dt1 = new SimpleDateFormat("M/dd/yyyy");
		dt1.setTimeZone(TimeZone.getTimeZone("GMT"));

		return dt1.format(this.expirationDate);
	}

	public boolean setExpirationDate(Long epoch) {
		// assumption is epoch time passed in is 12:00AM GMT on expirationDate of expiration.
		// we need to convert that to 4:00 PM EST. To do that we need the offset difference
		// plus 16 hours (12:00AM + 16:00 = 4:00PM)
		if (epoch % 86400 != 0)
			return false;

		//
		// This is all custom logic for the Yahoo scraper.
		// if others behave differently, this should be pushed down to the Yahoo logic layer.
		long epochMillis = epoch * 1000L;
		long gmtOffset = TimeZone.getTimeZone("GMT").getOffset(epochMillis);
		long estOffset = TimeZone.getTimeZone("America/New_York").getOffset(epochMillis);
		long totalOffset = gmtOffset - estOffset + (16 * 60 * 60 * 1000L);
		this.expirationDate = new Date(totalOffset + epochMillis);
		return true;
	}

	public void setDate(String year, String month, String day) {
		Calendar cal = Calendar.getInstance();
		cal.set(Integer.parseInt(year), Integer.parseInt(month) - 1, Integer.parseInt(day));
		this.expirationDate = cal.getTime();
	}

	public Float getStrike() {
		return strike;
	}

	public void setStrike(float strike) {
		this.strike = strike;
	}

	public float getPrice() {
		return price;
	}

	public void setPrice(Float price) {
		this.price = price;
	}

	public String getTypeAsString() {
		if (type == CALL)
			return "CALL";
		else if (type == PUT)
			return "PUT";
		else
			return "unknown";
	}

	public int getType() {
		return this.type;
	}

	public boolean isType(int optionType) {
		if (getType() == optionType || optionType == Option.ANY)
			return true;
		else
			return false;
	}

	public void setRatio() {
		try {
			this.getPrice();
			this.getStrike();
			this.ratio = price / strike;
		} catch (NumberFormatException e) {
			this.ratio = 0.0f;
		}
	}

	public void setAPR() {
		//Date today = Calendar.getInstance(TimeZone.getTimeZone("GMT")).getTime();

		double days = (this.expirationDate.getTime() - calendarInstanceToday.getTime()) / 86400000d;
		double years = ((double) days) / 365d;
//		this.apr = 100 * Math.log((this.getStock().getUnderlyingPrice() + this.getBid()) / this.getStock().getUnderlyingPrice()) / years;

//		this.apr = 100 * Math.log((this.getStrike() + this.getBid()) / this.getStrike()) / years;
		if (this.getBid() > this.getStrike()) {
		this.apr = 0;
		}
		else
			this.apr = 100 * Math.log((this.getStrike()) / (this.getStrike() - this.getBid())) / years;
	}

	public float getAPR() {
		//this.setAPR();
		return (float) this.apr;
	}

	public float getRatio() {
		return this.ratio;
	}

	public void setType(int type) {
		this.type = type;
	}

	public String toString() {
		String output = this.getTypeAsString() + " " + this.getExpirationDate() + " $" + this.getStrike() + " $" + this.getPrice() + " "
				+ this.getAsk() + " " + this.getBid() + " " + Float.toString(this.getRatio()) + " " + String.format("%.2f", this.getAPR()) + "%";
		return output;
	}

	public List<Object> toDataRow() {
		List<Object> dataRow = new ArrayList<>();
		dataRow.add(this.getTypeAsString());
		dataRow.add(this.getExpirationDate());
		dataRow.add("$" + this.getStrike());
		dataRow.add("$" + this.getPrice());
		dataRow.add("$" + this.getAsk());
		dataRow.add("$" + this.getBid());
		dataRow.add(String.format("%1$.2f", 100 * this.getRatio()) + "%");
		dataRow.add(String.format("%1$.2f", 1 * this.getAPR()) + "%");
		return dataRow;
	}

	public float getAsk() {
		return ask;
	}

	public void setAsk(float ask) {
		this.ask = ask;
	}

	public float getBid() {
		return bid;
	}

	public void setBid(float bid) {
		this.bid = bid;
	}

	public void setStock(Stock stock) {
		this.stock = stock;
	}

	public Stock getStock() {
		return stock;
	}
}
