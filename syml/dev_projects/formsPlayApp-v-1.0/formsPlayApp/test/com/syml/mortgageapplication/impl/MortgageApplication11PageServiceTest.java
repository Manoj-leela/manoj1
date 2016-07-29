package com.syml.mortgageapplication.impl;

import java.util.Iterator;
import java.util.List;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import com.syml.couchbasehelper.CouchbaseServiceHelper;
import com.syml.hibernate.dao.PostGressDaoServiceException;

import controllers.Opportunity;
import controllers.Property;

public class MortgageApplication11PageServiceTest {

	
	CouchbaseServiceHelper couchbaseServiceHelper = new CouchbaseServiceHelper();
	Opportunity opportunity = null;
	MortgageApplicationPageElevenService mPageElevenService=new MortgageApplicationPageElevenService();

	@Before
	public void loadingOpportunityData()
			throws MortgageApplicationPageServiceException {
		if (opportunity == null) {
			opportunity = couchbaseServiceHelper.getCouhbaseDataByKey(4591);
		}

	}

	
	@Test
	public void toTestListOfProperties() throws MortgageApplicationPageServiceException, PostGressDaoServiceException{
		mPageElevenService.deleteProperty(opportunity);

		Assert.assertNotNull("Opporunity object should not be null ",opportunity);
		
		Property property=new Property();
		property.setApplicantId(3039);
		property.setMonthlyRent(5455);
		property.setMoCondoFees(3553);
		property.setPropertyId("1");
	
		property.setName("76 strathcona test sgdhgs sgjsgdh ");
		opportunity.getApplicants().get(0).getProperties().add(property);
		mPageElevenService.updateApplicantProperties(opportunity);
		
	 Opportunity opportunity=couchbaseServiceHelper.getCouhbaseDataByKey(4591);
	 
	 Assert.assertNotNull("Opportunity object should not be null ",opportunity);
	 List<Property> listOfPerporty=opportunity.getApplicants().get(0).getProperties();
	 
	 for (Iterator iterator = listOfPerporty.iterator(); iterator.hasNext();) {
		Property property2 = (Property) iterator.next();
		Assert.assertTrue("property ID should not be null ",property2.getId()!=null);
		Assert.assertEquals("propertyApplicantID should be 3039", property.getApplicantId(), property2.getApplicantId());
		Assert.assertEquals("Property condfee should be 3553 ",property.getMoCondoFees() ,property2.getMoCondoFees());
		Assert.assertEquals("property ID should be 1",property.getPropertyId(),property2.getPropertyId());
	 }
	
	}
}
