package com.barclays.mortgage.service;

import java.util.List;

import com.barclays.mortgage.model.MortgageApplication;

/**
 * @author Shubhashish Tiwari
 *
 *Interface to for mortgage applications service
 *
 */
public interface MortgageApplicationService {

	public String createMortgageApplication(MortgageApplication mortgageApplication);	
	public List<MortgageApplication> retrieveMortgageApplication(String criteria);
		
}
