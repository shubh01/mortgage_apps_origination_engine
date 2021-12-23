package com.barclays.mortgage.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

/**
 * @author Shubhashish Tiwari
 *
 *Custom exception class for Mortgage Application
 */
@Getter @Setter @AllArgsConstructor
public class MortgageApplicationException extends RuntimeException{

	private String message;
	
}
