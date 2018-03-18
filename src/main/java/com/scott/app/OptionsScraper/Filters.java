package com.scott.app.OptionsScraper;

public interface Filters<T> {
	public boolean meetsRequirement(T element);
}