package com.barclays.mortgage.model;

import java.util.Comparator;

/**
 * @author Shubhashish Tiwari
 *
 */
public class SortByCreatedDate implements Comparator<MortgageApplication>{

	@Override
	public int compare(MortgageApplication o1, MortgageApplication o2) {
		return o1.getCreatedDate().compareTo(o2.getCreatedDate());
	}

}
