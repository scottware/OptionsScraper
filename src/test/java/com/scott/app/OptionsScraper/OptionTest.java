package com.scott.app.OptionsScraper;

import org.junit.Before;
import org.junit.Test;

import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

import static org.junit.Assert.*;

public class OptionTest {
	Option put, call;

	@Before
	public void setUp() {
		Date today = new Date(1521997200l * 1000l); // Sun Mar 25 2017 10:00 PST
		Stock amzn = new Stock("AMZN");
		amzn.setUnderlyingPrice("1495.0");

		put = new Option(amzn, Option.PUT, 1475.0f, 82.50f, 83.10f, 81.40f, today);
		put.setExpirationDate(1526601600l); // May 18 2017 00:00 GMT
		put.setAPR();

		call = new Option(amzn, Option.CALL, 1475.0f, 109.25f, 109.95f, 107.95f, today);
		call.setExpirationDate(1526601600l);
		call.setAPR();


	}

	@Test
	public void getExpirationDate() {
	}

	@Test
	public void setExpirationDate() {
		Option testOption = new Option(new Stock("amzn"), Option.PUT, 0.0f, 0.0f, 0.0f, 0.0f, new Date(152199720000l));

		assertTrue("setExpectation",testOption.setExpirationDate(1526601600l));
		assertEquals("getExpectation", "5/18/2018", testOption.getExpirationDate());

		assertTrue("setExpectation",testOption.setExpirationDate(1521936000l)); // 3/25/18 00:00 GMT, 3/24/18 17:00 PDT
		assertEquals("getExpectation","3/25/2018", testOption.getExpirationDate());

		assertFalse("setExpectation",testOption.setExpirationDate(1521936001l)); // 3/25/18 00:00:01 GMT, 3/24/18 17:00:01 PDT
		assertFalse("setExpectation",testOption.setExpirationDate(1521935999l)); // 3/24/18 23:49:59 GMT, 3/24/18 16:59:59 PDT

	}

	@Test
	public void getTypeAsString() {
		assertEquals("CALL", call.getTypeAsString());
		assertEquals("PUT", put.getTypeAsString());
	}

	@Test
	public void isType() {
		assertTrue(put.isType(Option.PUT));
		assertTrue(put.isType(Option.ANY));
		assertFalse(put.isType(Option.CALL));

		assertTrue(call.isType(Option.CALL));
		assertTrue(call.isType(Option.ANY));
		assertFalse(call.isType(Option.PUT));
	}

	@Test
	public void setRatio() {
	}

	@Test
	public void setAPR() {
		put.setAPR();

		assertEquals(38.282, put.getAPR(), .01);
	}

	@Test
	public void toDataRow() {
	}
}