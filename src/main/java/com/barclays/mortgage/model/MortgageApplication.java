package com.barclays.mortgage.model;

import java.util.Date;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * @author Shubhashish Tiwari
 *
 *model class to hold mortgage application data
 */

@Getter @Setter @NoArgsConstructor
public class MortgageApplication{

	private String id;
	private Integer version;
	private String offerId;
	private String productId;
	private Date offerDate;
	private Date createdDate;
	private String isExpired;

	@Override
	public String toString() {
		return "MortgageApplication [id=" + id + ", version=" + version + ", offerId=" + offerId + ", productId="
				+ productId + ", offerDate=" + offerDate + ", createdDate=" + createdDate + ", isExpired=" + isExpired
				+ "]";
	}

}
