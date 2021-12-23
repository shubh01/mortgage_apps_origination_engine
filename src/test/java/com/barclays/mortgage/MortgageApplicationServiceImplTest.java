package com.barclays.mortgage;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.lang.reflect.Field;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import com.barclays.mortgage.exception.MortgageApplicationException;
import com.barclays.mortgage.model.MortgageApplication;
import com.barclays.mortgage.service.MortgageApplicationServiceImpl;

/**
 * @author Shubhashish Tiwari
 *
 *test class for MortgageApplicationServiceImpl
 */
public class MortgageApplicationServiceImplTest {

	
	private static MortgageApplicationServiceImpl mortgageApplicationServiceImpl;
	private static final String SUCCESS = "Success";
	private static final String OFFER_DATE_CRITERIA="offerdate";
	
	@BeforeAll
	public static void setUp() {
		mortgageApplicationServiceImpl = new MortgageApplicationServiceImpl();
	}

	@AfterEach
	public void cleanUp() {
		try {
			Field field = MortgageApplicationServiceImpl.class.getDeclaredField("MORTGAGE_APPLICATIONS");
			field.setAccessible(true);
			field.set(mortgageApplicationServiceImpl, null);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * test method for createMortgageApplication in MortgageApplicationServiceImpl
	 */
	@Test
	public void createMortgageApplicationTest() {			
		MortgageApplication mortgageApplication = getMortgageApplication(new GregorianCalendar(2024, Calendar.JUNE, 20).getTime(), 1, "M5", "N");
		String result = mortgageApplicationServiceImpl.createMortgageApplication(mortgageApplication);
		assertEquals(SUCCESS, result);
	}

	/**
	 * test method for checkLowerVersion in MortgageApplicationServiceImpl
	 */
	@Test
	public void checkLowerVersionTest() {
		MortgageApplication mortgageApplication1 = getMortgageApplication(new GregorianCalendar(2024, Calendar.JUNE, 20).getTime(), 3, "M6", "N");
		mortgageApplicationServiceImpl.createMortgageApplication(mortgageApplication1);
		MortgageApplication mortgageApplication2 = getMortgageApplication(new GregorianCalendar(2024, Calendar.JUNE, 24).getTime(), 2, "M6", "N");
		MortgageApplicationException exception = Assertions.assertThrows(MortgageApplicationException.class, () -> {			
			mortgageApplicationServiceImpl.createMortgageApplication(mortgageApplication2);			
		});
		assertEquals("Mortgage rejected because of lower version", exception.getMessage());
	}
	

	/**
	 * test method for checkOfferDate in MortgageApplicationServiceImpl
	 */
	@Test
	public void checkOfferDateTest() {
		MortgageApplication mortgageApplication1 = getMortgageApplication(new GregorianCalendar(2021, Calendar.JUNE, 20).getTime(), 3, "M7", "N");
		MortgageApplicationException exception = Assertions.assertThrows(MortgageApplicationException.class, () -> {			
			mortgageApplicationServiceImpl.createMortgageApplication(mortgageApplication1);			
		});
		assertEquals("Mortgage has invaid offer date", exception.getMessage());
	}

	/**
	 * test method for checkAndRemoveExistingRecord in MortgageApplicationServiceImpl
	 */
	@Test
	public void checkAndRemoveExistingRecordTest() {
		MortgageApplication mortgageApplication1 = getMortgageApplication(new GregorianCalendar(2022, Calendar.SEPTEMBER, 20).getTime(), 3, "M8", "N");
		mortgageApplicationServiceImpl.createMortgageApplication(mortgageApplication1);
		MortgageApplication mortgageApplication2 = getMortgageApplication(new GregorianCalendar(2022, Calendar.SEPTEMBER, 24).getTime(), 3, "M8", "N");
		mortgageApplicationServiceImpl.createMortgageApplication(mortgageApplication2);
		assertEquals(new GregorianCalendar(2022, Calendar.SEPTEMBER, 24).getTime(), mortgageApplicationServiceImpl.retrieveMortgageApplication("offerDate").get(0).getOfferDate());
	}
	
	/**
	 * test method for retrieveMortgageApplication in MortgageApplicationServiceImpl
	 */
	@Test
	public void retrieveMortgageApplicationTest() {
		MortgageApplication mortgageApplication1 = getMortgageApplication(new GregorianCalendar(2023, Calendar.JUNE, 20).getTime(), 3, "M9", "N");
		mortgageApplicationServiceImpl.createMortgageApplication(mortgageApplication1);
		MortgageApplication mortgageApplication2 = getMortgageApplication(new GregorianCalendar(2023, Calendar.JUNE, 19).getTime(), 4, "M9", "N");
		mortgageApplicationServiceImpl.createMortgageApplication(mortgageApplication2);
		assertEquals(new GregorianCalendar(2023, Calendar.JUNE, 19).getTime(), mortgageApplicationServiceImpl.retrieveMortgageApplication(OFFER_DATE_CRITERIA).get(0).getOfferDate());
	}
	
	/**
	 * @param offerDate
	 * @param version
	 * @param id
	 * @param expired
	 * @return
	 * 
	 * method create returns dummy mortgage application object
	 * 
	 */
	private MortgageApplication getMortgageApplication(Date offerDate, Integer version, String id, String expired) {
		MortgageApplication mortgageApplication = new MortgageApplication();
		mortgageApplication.setId(id);
		mortgageApplication.setCreatedDate(new Date());
		mortgageApplication.setIsExpired(expired);
		mortgageApplication.setOfferDate(offerDate);
		mortgageApplication.setOfferId("OF-1");
		mortgageApplication.setVersion(version);
		mortgageApplication.setProductId("P1");
		return mortgageApplication;
	}
	
	
}
