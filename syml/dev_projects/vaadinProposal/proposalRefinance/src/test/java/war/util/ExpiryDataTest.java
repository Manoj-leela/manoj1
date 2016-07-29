package war.util;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.couchbase.client.java.document.json.JsonObject;


import org.junit.Assert;
import org.junit.Ignore;

import war.couchbase.CouchbaseData;
import war.couchbase.dao.CouchbaseDaoServiceException;
import war.couchbase.dao.service.CouchBaseService;
import war.util.ExpiryDate;

public class ExpiryDataTest {
	static	Logger logger=LoggerFactory.getLogger(CouchBaseService.class);
	
	ExpiryDate  expirydate = new ExpiryDate();
	
	
	String oppid ="4406";
	
	
	@Test
	public void testgetExpiryDate() throws CouchbaseDaoServiceException{
		
		Date date=	expirydate.getExpiryDate(oppid);
		CouchbaseData couchdata = new CouchbaseData();
		Assert.assertNotNull("date should not be null",date);
			JsonObject json = couchdata.getDataCouchbase(oppid);
			
			Assert.assertNotNull("expirydata not null ",json.get("ExpiryDateTime"));
			
	}
	
	@Test
	public void testcurrentdate() throws CouchbaseDaoServiceException, ParseException{
		Date today = Calendar.getInstance().getTime();
		DateFormat df = new SimpleDateFormat("MM/dd/yyyy");
		Date date=null;
		String current_date = df.format(today);
		date =df.parse(current_date);
		
		Assert.assertEquals(current_date, df.format(today));
	}
	@Ignore
	@Test
	public void testDateDifferenceLessThanExpiryDate(){
		ExpiryDate date = new ExpiryDate();
		Date currentdate =date.getCurrentDate();
		Date expirydate =date.getExpiryDate("4406");
		logger.debug(currentdate+"   "+expirydate);
		Assert.assertNotNull(currentdate);
		Assert.assertNotNull(expirydate);
		
		/**
		 * The expiry date on condition writine current data is greater than it will goes to expriy page
		 * if Expriy data i greater than it will goes to recommended page
		 * 
		 */
		Assert.assertTrue(currentdate.compareTo(expirydate)<0);
		
		
	}
	
	@Test
	public void testDateDifferenceGreaterThanExpiryDate(){
		ExpiryDate date = new ExpiryDate();
		Date currentdate =date.getCurrentDate();
		Date expirydate =date.getExpiryDate("4406");
		currentdate.setMonth(expirydate.getMonth()+5);
		logger.debug(currentdate+"   "+expirydate);
		Assert.assertNotNull(currentdate);
		Assert.assertNotNull(expirydate);
		
		/**
		 * The expiry date on condition writine current data is greater than it will goes to expriy page
		 * if Expriy data i greater than it will goes to recommended page
		 * 
		 */
		Assert.assertTrue(currentdate.compareTo(expirydate)>0);
		
		
	}
}
