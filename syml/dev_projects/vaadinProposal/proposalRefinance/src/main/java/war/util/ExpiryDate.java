package war.util;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.couchbase.client.java.document.json.JsonObject;

import war.couchbase.*;
import war.couchbase.dao.CouchbaseDaoServiceException;
import war.couchbase.dao.service.CouchBaseService;

public class ExpiryDate {
	
	static	Logger logger=LoggerFactory.getLogger(ExpiryDate.class);
	
	public Date getExpiryDate(String exp){
	System.out.println("------------------------------------------"+exp);
	CouchbaseData couchdata = new CouchbaseData();
	JsonObject expiry=null;
	try {
		expiry = couchdata.getDataCouchbase(exp);
	} catch (CouchbaseDaoServiceException e1) {
		logger.error("expiry date :"+e1.getMessage());
	}
	Date       expirayTime=null;
	String expirayTime1="";
	DateFormat df = new SimpleDateFormat("MM/dd/yyyy");
	
     try {
    	
		expirayTime1=expiry.get("ExpiryDateTime").toString();
	
		
     }catch(NullPointerException e){
    	 logger.error("NUll pointer:"+e.getMessage());
	}
     try{
    	 expirayTime=	df.parse(expirayTime1);
    	
     }catch(ParseException e){
    	 logger.error("Parse Exception: "+e.getMessage());
     }
    
	
	
	return expirayTime;
	
	
	
}
	
	
	public String getStringExpiryDate(String opp){

		CouchbaseData couchdata = new CouchbaseData();
		JsonObject expiry = null;
		try {
			expiry = couchdata.getDataCouchbase(opp);
		} catch (CouchbaseDaoServiceException e1) {
			logger.error("CouchbaseDaoServiceException:"+e1.getMessage());
		}
		
		String expirayTime="";
		
		
	     try {
	    	
			expirayTime=expiry.get("ExpiryDateTime").toString();
			
		
		}catch(NullPointerException e){
			logger.error("NUll pointer:"+e.getMessage());
		}
	    
	    
		
		
		return expirayTime;
		
		
	}
	
	
	public Date getCurrentDate(){
	
		 Date today = Calendar.getInstance().getTime();
			DateFormat df = new SimpleDateFormat("MM/dd/yyyy");
			String current_date = df.format(today);
			Date date=null;
		 try {
			 date=	df.parse(current_date);
		} catch (ParseException e) {
			
			logger.error("ParseException:"+e.getMessage());
		}
			/*Date date_current_date=null;
			Date  date_expiryDate=null;
			*/
			return date;
	}
	
	public static void main(String... A){
		ExpiryDate EX = new ExpiryDate();
		Date dddd= EX.getExpiryDate("3584");
		System.out.println("sdsd"+dddd);
		//System.out.println(EX.getCurrentDate());
	}
	
	public static void setExpiryDate(Date date,String oppertunityId ){
		System.out.println("insie thee ex");
		DateFormat df = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss");
		String reportDate = df.format(date);
		CouchbaseData cbdata = new CouchbaseData();
		
		JsonObject expiryDate = null;
		try {
			expiryDate = cbdata.getDataCouchbase(oppertunityId);
		} catch (CouchbaseDaoServiceException e1) {
			
			logger.error("CouchbaseDaoServiceException"+e1.getMessage());
		}
		
		
		
		

		
		
				expiryDate.put("ExpiryDateTime",reportDate);
				   
				   cbdata.UpdateDataCouchbase(oppertunityId,expiryDate.toString());
	
		
		
		
	}
}