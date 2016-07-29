package com.syml.referralsource.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.junit.Assert;
import org.junit.Test;

import com.syml.SplitAddress;
import com.syml.hibernate.dao.PostGressDaoServiceException;
import com.syml.hibernate.dao.impl.PostgresDAO;

import controllers.Contact;
import controllers.Referral_Source;

public class ReferralSourcePageServiceTest {
	PostgresDAO psDao = new PostgresDAO();

	
	/**
	 * to create create Referral Source
	 * @throws PostGressDaoServiceException
	 */
		@Test
	public void toTestCreateReferralSoursce() throws PostGressDaoServiceException{

		Contact contact=new Contact();
		contact.setName("Test");
		contact.setName("Referral");
		contact.setEmail("venkatesh.m@bizruntime.com");
		psDao.insertContact(contact);

		Assert.assertNotNull("contact Id shouldnot be null ",contact.getId());

		Referral_Source referral_Source=new Referral_Source();
		referral_Source.setName("Test_Referral");
		referral_Source.setEmail_from("venkatesh.m@bizruntime.com");
		referral_Source.setPartner_id(contact.getId());
		
		psDao.insertReferral(referral_Source);
		
		Assert.assertNotNull("Referral  Id shouldnot be null ",referral_Source.getId());

		
		
	
		
	}
	
		/**
		 * To test Update Referral if exist else create method 
		 * @throws PostGressDaoServiceException
		 */
	@Test
	public void toTestUpdateReferralSourceIfExistElseCreate() throws PostGressDaoServiceException{

		Contact contact=new Contact();
		contact.setName("Test");
		contact.setLast_name("Referral2");
		contact.setEmail("venkatesh.m@bizruntime.com");
		contact.setId(4983);
	
		Referral_Source referral_Source=new Referral_Source();
		referral_Source.setName("Test_Referral2");
		referral_Source.setEmail_from("venkatesh.m@bizruntime.com");
		referral_Source.setPartner_mobile("989-345-3525");
		
		referral_Source=	new ReferralSourcePageService().createReferralSource(contact, referral_Source);
		
		Assert.assertNotNull("Referral Object should not be null ",referral_Source);
		
		Assert.assertTrue("The referral value should be Greater than Zero ",referral_Source.getId()>0);
		
		
	}
	
	/**
	 * To test getReferralSourceByPartnerID
	 * @throws PostGressDaoServiceException
	 */
	@Test
	public void toTestGetReferralSourceByPartnerID() throws PostGressDaoServiceException{
		
		
		
	ArrayList<Referral_Source> referral_SourceList=	psDao.getReferral_SourceByPartnerID(4983);
	Assert.assertNotNull("Referral Source list should not be null ",referral_SourceList);
	
	Assert.assertTrue("Referral Source lizt size should be one ",referral_SourceList.size()==1);
		
		
	}
	
	
	/**
	 * to test getstatID method
	 * @throws PostGressDaoServiceException
	 */
	@Test
	public void toTestGetStateId() throws PostGressDaoServiceException{
		int stateId=0;
		SplitAddress address=new SplitAddress();
		String addresGievn="54 Strathcona Ave, Ottawa, ON K1S 1X3, Canada";
		
		Map<String, String> addressMap=	address.getProperAddress(addresGievn);
		stateId=psDao.getStateID(addressMap.get("Province"));
		Assert.assertFalse("state Id should Greater than zero ", stateId>0);
	}
	
	/**
	 * To test getcontactByEMailAnd lastname Method 
	 * @throws PostGressDaoServiceException
	 */
	@Test
	public void toTestGetContactByEmailAndLastName() throws PostGressDaoServiceException{
		
		Contact contact=new Contact();
		contact.setLast_name("malik");
		contact.setEmail("venkatesh.m@bizruntime.com");
		
		List<Contact> list=	psDao.getContactByEmailAndLastName(contact);
			Assert.assertNotNull("The contact object should not be null ",contact);
			Assert.assertTrue("conatct list size should greater than o ",list.size()>0);
			
			
		
	}

	

	

}
