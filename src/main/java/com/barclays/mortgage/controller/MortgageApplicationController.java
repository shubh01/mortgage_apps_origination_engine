package com.barclays.mortgage.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.barclays.mortgage.exception.MortgageApplicationException;
import com.barclays.mortgage.model.MortgageApplication;
import com.barclays.mortgage.service.MortgageApplicationService;
import com.barclays.mortgage.util.ErrorResponse;

@RestController
public class MortgageApplicationController {

	@Autowired
	private MortgageApplicationService mortgageApplicationService;

	@RequestMapping(value ="/create", method = RequestMethod.POST)
	public ResponseEntity createMortgageApplication(@RequestBody MortgageApplication mortgageApplication) {
		try {
			 mortgageApplicationService.createMortgageApplication(mortgageApplication);
			return new ResponseEntity(mortgageApplication,HttpStatus.OK);
		}catch (MortgageApplicationException e) {
			ErrorResponse errorResponse = new ErrorResponse(e.getMessage());
			return new ResponseEntity(errorResponse, HttpStatus.BAD_REQUEST);
		}
	}
	
	@RequestMapping(value="/reterive/{criteria}", method = RequestMethod.GET)
	public List<MortgageApplication> reteriveMortgageApplications(@PathVariable("criteria") String criteria){
		return mortgageApplicationService.retrieveMortgageApplication(criteria);
	}
	
}
