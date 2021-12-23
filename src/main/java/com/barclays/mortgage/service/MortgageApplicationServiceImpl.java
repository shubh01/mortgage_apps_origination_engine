package com.barclays.mortgage.service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import com.barclays.mortgage.exception.MortgageApplicationException;
import com.barclays.mortgage.model.MortgageApplication;
import com.barclays.mortgage.model.SortByCreatedDate;
import com.barclays.mortgage.model.SortbyOfferDate;

/**
 * @author Shubhashish Tiwari
 *
 */
@Service
public class MortgageApplicationServiceImpl implements MortgageApplicationService{

	private static MortgageApplication[] MORTGAGE_APPLICATIONS = null;
	private static final String SUCCESS = "Success";
	private static final String OFFER_DATE_CRITERIA="offerdate";

	/**
	 * method to create mortgage application and save it in array
	 */
	@Override
	public String createMortgageApplication(MortgageApplication mortgageApplication) {
		List<MortgageApplication> dataList = new ArrayList<MortgageApplication>();		
		checkLowerVersion(mortgageApplication);
		checkOfferDate(mortgageApplication);
		if(MORTGAGE_APPLICATIONS != null) {
			dataList = checkAndRemoveExistingRecord(mortgageApplication);
		}
		dataList.add(mortgageApplication);
		MORTGAGE_APPLICATIONS = new MortgageApplication[dataList.size()];
		dataList.toArray(MORTGAGE_APPLICATIONS);
		return SUCCESS;
	}

	/**
	 *method sorts and return the mortgage data according to sort condition
	 */
	@Override
	public List<MortgageApplication> retrieveMortgageApplication(String criteria) {
		List<MortgageApplication> dataList = new ArrayList<MortgageApplication>();
		if(MORTGAGE_APPLICATIONS != null) {
			dataList = new ArrayList<MortgageApplication>(Arrays.asList(MORTGAGE_APPLICATIONS));
			if(criteria.equalsIgnoreCase(OFFER_DATE_CRITERIA) && MORTGAGE_APPLICATIONS != null && MORTGAGE_APPLICATIONS.length>0) {
				Collections.sort(dataList,new SortbyOfferDate());
			}else {
				Collections.sort(dataList,new SortByCreatedDate());
			}			
		}		
		return dataList;
	}	
	
	
	
	/**
	 * @param mortgageApplication
	 * 
	 * method performs lower version validation
	 */
	private void checkLowerVersion(MortgageApplication mortgageApplication) {				
		Optional<MortgageApplication> mortApp = null;	
		if(MORTGAGE_APPLICATIONS !=null ) {
			mortApp = Arrays.asList(MORTGAGE_APPLICATIONS).stream().filter(x -> (x.getId().equalsIgnoreCase(mortgageApplication.getId()) && x.getVersion()>mortgageApplication.getVersion())).findFirst();
			if(mortApp !=null && mortApp.isPresent()) {
				throw new MortgageApplicationException("Mortgage rejected because of lower version");
			}
		}
	}
	
	
	/**
	 * @param mortgageApplication
	 * 
	 * method performs offer date validation
	 */
	private void checkOfferDate(MortgageApplication mortgageApplication) {
		Calendar c = Calendar.getInstance();
		c.add(Calendar.MONTH, 6);
		if(!mortgageApplication.getOfferDate().after(c.getTime())) {
			throw new MortgageApplicationException("Mortgage has invaid offer date");
		}		
	}		
	
	/**
	 * Method checks and remove existing record from mortgage data array
	 * @param mortgageApplication
	 */
	private List<MortgageApplication> checkAndRemoveExistingRecord(MortgageApplication mortgageApplication) {
		ArrayList<MortgageApplication> dataList = new ArrayList<MortgageApplication>(Arrays.asList(MORTGAGE_APPLICATIONS));
		Optional<MortgageApplication> existingRecord = dataList.stream().filter(x -> x.getId().equalsIgnoreCase(mortgageApplication.getId()) && x.getVersion().equals(mortgageApplication.getVersion())).findFirst();		
		if(existingRecord.isPresent()) {
			dataList.remove(existingRecord.get());
			dataList.toArray(MORTGAGE_APPLICATIONS);
		}
		return dataList;
	}
	
	/**
	 * method which will run every midnight to set expiry status of mortgage application
	 */
	@Scheduled(cron="0 0 0 * * ?")
	public void run() {
		System.out.println("running cron method----------------------");
		if(MORTGAGE_APPLICATIONS != null) {
			List<MortgageApplication> mortgageApplications = new ArrayList<MortgageApplication>(Arrays.asList(MORTGAGE_APPLICATIONS));
			mortgageApplications.stream().forEach(x -> {
				if(x.getOfferDate().before(new Date())) {
					x.setIsExpired("Y");
				}
			});
		}
	}
}
