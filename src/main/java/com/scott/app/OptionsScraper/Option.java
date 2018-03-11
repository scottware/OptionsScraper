package com.scott.app.OptionsScraper;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class Option {
	public static final int PUT = 1;
	public static final int CALL = 0;

	private float strike, price, ask, bid;
	private int type;
	private float ratio;
	private Date date;
	private double apr;

	public Option(int type, float strike, float price, float ask, float bid) {
		this.setType(type);
		this.setStrike(strike);
		this.setPrice(price);
		this.setAsk(ask);
		this.setBid(bid);
		this.setRatio();
	}

	public String getDate() {
		SimpleDateFormat dt1 = new SimpleDateFormat("M/dd/yyyy");
		return dt1.format(this.date);
	}

	public void setDate(Long epoch) {
		this.date = new Date(epoch);
	}

	public void setDate(String year, String month, String day) {
		Calendar cal = Calendar.getInstance();
		cal.set(Integer.parseInt(year), Integer.parseInt(month)-1, Integer.parseInt(day));
		this.date = cal.getTime();
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
		Date today = Calendar.getInstance().getTime();
		long days = (this.date.getTime() - today.getTime()) / 86400000;
		double years = (double)days/365;
		this.apr = Math.log( (this.getStrike() + this.getPrice()) / this.getStrike()) / years;
		
		
		double a = ( this.getStrike() + this.getPrice() ) /this.getStrike();
		double log = Math.log(a);

		System.out.println(this.getStrike() + "*" + this.getPrice() + "*" + a+"*"+ log + "*"+years+"*"+this.date.toString()+"*"+days+"*"+this.apr);
		// =ln(strike+price/strike)/(days/365)
	}

	public float getAPR() {
		this.setAPR();
		return (float) this.apr;
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
		dataRow.add(this.getTypeAsString());
		dataRow.add(this.getDate());
		dataRow.add("$" + this.getStrike());
		dataRow.add("$" + this.getPrice());
		dataRow.add("$" + this.getAsk());
		dataRow.add("$" + this.getBid());
		dataRow.add(String.format("%1$.2f", 100 * this.getRatio()) + "%");
		dataRow.add(String.format("%1$.2f", 100 * this.getAPR()) + "%");
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
}
