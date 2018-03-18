package com.scott.app.OptionsScraper;

import java.util.ArrayList;

public class ArrayListFilterable<T> extends ArrayList {
	public static <T> ArrayListFilterable filter(ArrayList<T> list, Filters<? super T> filter) {
		ArrayListFilterable result = new ArrayListFilterable();
		//Iterator i = list.iterator();
		for (T element : list) {
			if (filter.meetsRequirement(element)) {
				result.add(element);
			}

		}
		return result;
	}
}