package com.syml.submitreferral.impl;

import java.util.ArrayList;
import java.util.List;

import org.junit.Assert;
import org.junit.Test;

import play.Logger;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.syml.hibernate.dao.PostGressDaoServiceException;
import com.syml.hibernate.dao.impl.PostgresDAO;

import controllers.Contact;
import controllers.Lead;
import controllers.Referral_Source;

public class SubmitReferralFormPageTest {

	PostgresDAO psDao = new PostgresDAO();

	public void testToReferralFind() {

	}

	
	/**
	 * to Test negative case get Contact method 
	 * @throws PostGressDaoServiceException
	 */
	@Test
	public void toTestGetContactForNegativeCase() throws PostGressDaoServiceException {
		Logger.info("inside toTestGetContactForNegativeCase");

		Contact contact = new Contact();
		contact.setEmail("venkatesh.m@bizruntime.com");
		contact.setName("venkatesh");
		contact.setLast_name("m1");
		List<Contact> listOfContacts = psDao.getContact(contact);
		Assert.assertTrue("List of contacts should  be empty ",
				listOfContacts.isEmpty());

	}

	/**
	 * to test get contact method 
	 * @throws PostGressDaoServiceException
	 */
	@Test
	public void toTestGetContactForPositiveCase() throws PostGressDaoServiceException {
		Logger.info("inside toTestGetContactForPositiveCase");

		Contact contact = new Contact();
		contact.setEmail("darryl@visdom.ca");
		contact.setName("Aaron");
		contact.setLast_name("Rodgers");
		List<Contact> listOfContacts = psDao.getContact(contact);
		Assert.assertTrue("List of contacts size should be greater than zero ",
				listOfContacts.size() > 0);

	}

	/**
	 * To test Createconatct method 
	 * @throws PostGressDaoServiceException
	 */
	 @Test
	public void toTestCreatContact() throws PostGressDaoServiceException {

		Contact contact = new Contact();
		contact.setEmail("venkatesh.m@bizruntime.com");
		contact.setName("venkatesh");
		contact.setLast_name("m");
		contact = psDao.insertContact(contact);
		Assert.assertTrue("id should not be zero ", contact.getId() > 0);

		List<Contact> listOfContacts = psDao.getContact(contact);
		Assert.assertTrue("List of contacts size should be greater than zero ",
				listOfContacts.size() > 0);

	}

	 /**
	  * To test get Lead method
	  * @throws PostGressDaoServiceException
	  */
	@Test
	public void toTestGetLeadPostiveTesCase() throws PostGressDaoServiceException {
		Lead lead = new Lead();
		lead.setName("Test222334343443_tes");
		lead.setEmail_from("venkatesh.m@bizruntime.com");
		List<Lead> listOfLeads = psDao.getLead(lead);
		Assert.assertNotNull("lead object data should notbe null ", listOfLeads);

		Assert.assertTrue("list size greater than zero ",
				listOfLeads.size() > 0);

	}

	/**
	 * to getLead for Negative case
	 * @throws PostGressDaoServiceException
	 */
	@Test
	public void toTestGetLeadNegativeTestCase() throws PostGressDaoServiceException {
		Lead lead = new Lead();
		lead.setName("Test222334343443_tes11");

		lead.setEmail_from("venkatesh.m@bizruntime.com");
		List<Lead> listOfLeads = psDao.getLead(lead);
		Assert.assertNotNull("lead object data should notbe null ", listOfLeads);

		Assert.assertFalse("list size be zero  ", listOfLeads.size() > 0);

	}

	
	/**
	 * to test create lead method 
	 * @throws PostGressDaoServiceException
	 */
	 @Test
	public void toTestCreateLead() throws PostGressDaoServiceException {
		Lead lead = new Lead();
		lead.setName("Test222334343443_tesValue");
		lead.setEmail_from("venkatesh.m@bizruntime.com");
		lead.setPartner_id(3946);

		lead.setCity("Banglaore ");
		lead.setActive(true);
		lead.setMobile("989-636-3545");
		lead.setStage_id(6);
		lead.setReferref_source(231);
		psDao.insertLead(lead);
		Assert.assertTrue("generated ID Greater than zero ", lead.getId() > 0);

		List<Lead> listOfLeads = psDao.getLead(lead);

		Assert.assertNotNull("lead object data should notbe null ", listOfLeads);

		Assert.assertTrue("list size greater than zero ",
				listOfLeads.size() > 0);

		Assert.assertEquals("The name should be equal ",
				"Test222334343443_tesValue", listOfLeads.get(0).getName());
		Assert.assertEquals("The email should be equal ",
				"venkatesh.m@bizruntime.com", listOfLeads.get(0)
						.getEmail_from());
		Assert.assertEquals("The Phone number should be equal ",
				"989-636-3545", listOfLeads.get(0).getMobile());

	}

	 
	 /**
	  * To Test findREferralSourceBy ID method
	  * @throws PostGressDaoServiceException
	  */
	@Test
	public void toTestFindReferralResorceByID() throws PostGressDaoServiceException {

		Referral_Source referral_Source = psDao.getReferralSourceById(231);

		Assert.assertNotNull("The Referral Sorce Object should not be null ",
				referral_Source);

	}

	/**
	 * To test FindReferralSourceByEmailID method
	 * @throws PostGressDaoServiceException
	 */
	@Test
	public void toTestFindeReferralSourceByEmailId() throws PostGressDaoServiceException {

		List<Referral_Source> referralList = psDao
				.getReferral_SourceByEmail("venkatesh.m@bizruntime.com");

		Assert.assertNotNull("The list of Referral should not be Null ",
				referralList);

		Assert.assertTrue(
				"the referral sourcelist size should be greater than zero ",
				referralList.size() > 0);

	}

	/**
	 * To test find referral sourceBy email and lastname
	 * @throws PostGressDaoServiceException
	 */
	@Test
	public void toTestFindReferralSorceByEmailidAndLastName() throws PostGressDaoServiceException {

		Referral_Source referral_Source = new Referral_Source();
		referral_Source.setName("venkates_test");
		referral_Source.setEmail_from("venkatesh.m@bizruntime.com");
		ArrayList<Referral_Source> list = psDao
				.getReferral_SourceByEmailAndName(
						referral_Source.getEmail_from(),
						referral_Source.getName());
		Assert.assertNotNull(
				"The list of referral Sources should not be Null ", list);

		Assert.assertTrue(
				"The list of referral Size should be greater than zero ",
				list.size() > 0);

	}

	@Test
	public void toTestFindReferralByIdorEmailAndSendMail()
			throws JsonProcessingException, PostGressDaoServiceException {

		Lead lead = new Lead();
		lead.setReferral_Source_First_Name("venkates");
		lead.setReferral_Source_Last_Name("test");
		lead.setReferral_Source_Email("venkatesh.m@bizruntime.com");
		lead.setReferref_source(850);

		new SubmitReferralPageService().toFindREferralSource(lead);

		Assert.assertNotNull("referral ID should not be null ",
				lead.getReferref_source());
	}

	@Test
	public void toTestFindReferralSourceIfreferralIDNotgiven()
			throws JsonProcessingException, PostGressDaoServiceException {
		Lead lead = new Lead();
		lead.setReferral_Source_First_Name("test");
		lead.setReferral_Source_Last_Name("test");
		lead.setReferral_Source_Email("venkatesh.m@bizruntime.com");

		new SubmitReferralPageService().toFindREferralSource(lead);

		Assert.assertNull("referral ID should not be null ",
				lead.getReferref_source());
	}
	
	@Test
	public void toTestGetLeadByContactId() throws PostGressDaoServiceException{
		Contact contact=new Contact();
		contact.setId(4298);
	Lead lead=	  psDao.getLeadByConatctId(contact);
	
	Logger.info("lead details fetched from DB using ContactID ",lead);
	Assert.assertNotNull("The lead object should not be null ",lead);
	}
	
	
 

}
