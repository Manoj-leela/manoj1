package controllers;

import java.util.Arrays;
import java.util.Map;
import java.util.Set;

import play.*;
import play.mvc.*;

import play.Logger;
import views.html.*;

public class Application extends Controller {

	private static org.slf4j.Logger logger = play.Logger.underlying();
	
    public static Result index() {
    	
    	
    	  final Set<Map.Entry<String,String[]>> entries = request().queryString().entrySet();
          for (Map.Entry<String,String[]> entry : entries) {
              final String key = entry.getKey();
              final String value = Arrays.toString(entry.getValue());
              logger.debug(key + " " + value);
          }
          logger.debug(request().getQueryString("a"));
          logger.debug(request().getQueryString("b"));
          logger.debug(request().getQueryString("c"));
      
    	
        return ok(realtor.render("Your new application is ready."));
    }
    
    
    public static Result realtor() {

  	  final Set<Map.Entry<String,String[]>> entries = request().queryString().entrySet();
        for (Map.Entry<String,String[]> entry : entries) {
            final String key = entry.getKey();
            final String value = Arrays.toString(entry.getValue());
            logger.debug(key + " " + value);
        }
        String referralid=request().getQueryString("referralid");
    	String emailid=request().getQueryString("email");
    	String applicantId=request().getQueryString("ContactID");
    	String opprtunityId=request().getQueryString("LeadID");
    	request().getHeader("X-FORWARDED-FOR");
    	logger.debug(request().remoteAddress());
		new StoredRealtorDataTocouchabse(applicantId,emailid,request().remoteAddress(),opprtunityId,referralid).start();
        return ok(realtor.render("Your new application is ready."));
    }
    
    public static Result planner() {
    	
  	  final Set<Map.Entry<String,String[]>> entries = request().queryString().entrySet();
  	String referralid=request().getQueryString("referralid");
  	String emailid=request().getQueryString("email");
	String applicantId=request().getQueryString("ContactID");
	String opprtunityId=request().getQueryString("LeadID");
	request().getHeader("X-FORWARDED-FOR");
	logger.debug(request().remoteAddress());
	new StorePlanerDataToCouhbase(applicantId,emailid,request().remoteAddress(),opprtunityId,referralid).start();

        return ok(planner.render("Your new application is ready."));
    }
    
    public static Result plannerC() {
    	
    	
    final Set<Map.Entry<String,String[]>> entries = request().queryString().entrySet();
    String referralid=request().getQueryString("referralid");
    String emailid=request().getQueryString("email");
  	String applicantId=request().getQueryString("ContactID");
  	String opprtunityId=request().getQueryString("LeadID");
  	request().getHeader("X-FORWARDED-FOR");
  	logger.debug(request().remoteAddress());
  	new StorePlanerCDataToCouhbase(applicantId,emailid,request().remoteAddress(),opprtunityId,referralid).start();


          return ok(plannerC.render("Your new application is ready."));
      }
    
    
    public static Result realtorC() {
    	
    	 final Set<Map.Entry<String,String[]>> entries = request().queryString().entrySet();
         for (Map.Entry<String,String[]> entry : entries) {
             final String key = entry.getKey();
             final String value = Arrays.toString(entry.getValue());
             logger.debug(key + " " + value);
         }
        String referralid=request().getQueryString("referralid");
     	String emailid=request().getQueryString("email");
     	String applicantId=request().getQueryString("ContactID");
     	String opprtunityId=request().getQueryString("LeadID");
     	request().getHeader("X-FORWARDED-FOR");
     	logger.debug(request().remoteAddress());
 		new StoredRealtorCDataTocouchabse(applicantId,emailid,request().remoteAddress(),opprtunityId,referralid).start();


        return ok(realtorC.render("Your new application is ready."));
    }
  
    
    public static Result clientABC() {
    	 final Set<Map.Entry<String,String[]>> entries = request().queryString().entrySet();
    	String referralid=request().getQueryString("referralid"); 
 	  	String emailid=request().getQueryString("email");
 		String applicantId=request().getQueryString("ContactID");
 		String opprtunityId=request().getQueryString("LeadID");
 		request().getHeader("X-FORWARDED-FOR");
 		logger.debug(request().remoteAddress());
 		new StoreClientABCDataToCouchbase(applicantId,emailid,request().remoteAddress(),opprtunityId,referralid).start();

          return ok(clientABC.render("Your new application is ready."));
      }
      
    
    public static Result clientA() {
  	  final Set<Map.Entry<String,String[]>> entries = request().queryString().entrySet();
  	String referralid=request().getQueryString("referralid");  
  	String emailid=request().getQueryString("email");
	String applicantId=request().getQueryString("ContactID");
	String opprtunityId=request().getQueryString("LeadID");
	request().getHeader("X-FORWARDED-FOR");
	logger.debug(request().remoteAddress());
	new StoreClientADataToCouhcbase(applicantId,emailid,request().remoteAddress(),opprtunityId,referralid).start();

        return ok(clientA.render("Your new application is ready."));
    }
    
    public static Result clientB() {
    	 final Set<Map.Entry<String,String[]>> entries = request().queryString().entrySet();
    	 String referralid=request().getQueryString("referralid");
    	  	String emailid=request().getQueryString("email");
    		String applicantId=request().getQueryString("ContactID");
    		String opprtunityId=request().getQueryString("LeadID");
    		request().getHeader("X-FORWARDED-FOR");
    		logger.debug(request().remoteAddress());
    		new StoreClintBDataToCouhbase(applicantId,emailid,request().remoteAddress(),opprtunityId,referralid).start();

        return ok(clientB.render("Your new application is ready."));
    }
    public static Result clientAB() {
    	final Set<Map.Entry<String,String[]>> entries = request().queryString().entrySet();
    	String referralid=request().getQueryString("referralid");
 	  	String emailid=request().getQueryString("email");
 		String applicantId=request().getQueryString("ContactID");
 		String opprtunityId=request().getQueryString("LeadID");
 		request().getHeader("X-FORWARDED-FOR");
 		logger.debug(request().remoteAddress());
 		new StoreClientABDataToCouchbase(applicantId,emailid,request().remoteAddress(),opprtunityId).start();

        return ok(clientAB.render("Your new application is ready."));
    }
}
