package controllers;

import static play.data.Form.form;
import infrastracture.ClientSurvey;
import infrastracture.CouchBaseOperation;
import infrastracture.ReferralSurvey;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.GregorianCalendar;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import org.codehaus.jettison.json.JSONObject;
import play.Logger;

import play.data.DynamicForm;
import play.mvc.Controller;
import play.mvc.Result;
import views.html.client;
import views.html.index;
import views.html.referral;

public class Application extends Controller {
	
	private static org.slf4j.Logger logger = play.Logger.underlying();

    public static Result index() throws FileNotFoundException {
        return ok(index.render("") ); }
    
    static CouchBaseOperation couchbase=new CouchBaseOperation();
    

    public static Result getClientDetails(){
    	
    	
    	 final Set<Map.Entry<String,String[]>> entries = request().queryString().entrySet();
    		String applicantId=request().getQueryString("app");
    		String opprtunityId=request().getQueryString("op");
    		request().getHeader("X-FORWARDED-FOR");
    		logger.debug(request().remoteAddress());
    		  ArrayList<Client> 		list=null;
    		  
    	    	int exist=0;

    		try{
      	list = couchbase.getClientSurveyFromCouchbase(opprtunityId);
          logger.debug("list---------"+list.size());
    		 
    		}catch(Exception e){
    			logger.error("Error occured in getClientDetailes  "+e.getMessage());		
    		}
    		try{
    			if(list!=null){
    	        	if(list.size()!=0){
    	        		exist=1;
    	        	}
    	        	}
    	    	
    		}catch(Exception e){
    			logger.error("Error occured in getClientDetailes "+e.getMessage());		
    		}
    	
    		 logger.debug("list---------"+list.size());

    	if(exist==0){
        return ok(client.render(opprtunityId,applicantId,exist));
    	}else{
    		int dataExist=0;
    		try{
    		for (Iterator iterator = list.iterator(); iterator.hasNext();) {
				Client client = (Client) iterator.next();
				
				if (client.getApplicantId().equalsIgnoreCase(applicantId)) {
					dataExist=1;
				}
    		}}catch(Exception e){
    			logger.error("Error is getting applicantid not get  "+e.getMessage());	
    		}
    		
    		 logger.debug("list---------"+list.size());

    		if(dataExist==0){
    	        return ok(client.render(opprtunityId,applicantId,exist));

    		}else{
	        return ok(index.render("You have already completed a survey for this mortgage with Visdom.  Thank you for your feedback."));
    		}
    	}
    }
    
    public static Result getReferralDetails(){
    	
    	
   	 final Set<Map.Entry<String,String[]>> entries = request().queryString().entrySet();
   		String referralid=request().getQueryString("app");
   		String opprtunityId=request().getQueryString("op");
   		request().getHeader("X-FORWARDED-FOR");
   		logger.debug(request().remoteAddress());
   	  	int exist=0;

   	  ArrayList<Client>  list=null;
   	  try{
   	 list = couchbase.getReferralSurveyFromCouchbase(opprtunityId);
 
  	
   	  }catch(Exception e){
   		logger.error("Error occured in getReferraldetailes  "+e.getMessage());			  
   	  }
   	  
   	  try{
  	if(list!=null){
  	if(list.size()!=0){
  		exist=1;
  	}
  	}}catch(Exception e){
  		logger.error("Error occured in getReferraldetailes  "+e.getMessage());		
  	}
   	
  	if(exist==0){
       return ok(referral.render(opprtunityId,referralid,exist));
  	}else{
	        return ok(index.render("You have already completed a survey for this mortgage with Visdom.  Thank you for your feedback."));

  	}
   }

    
    public static Result getClientSurveydetails(){
        DynamicForm dynamicForm = form().bindFromRequest();
        logger.debug(dynamicForm.get("options"));
        logger.debug(dynamicForm.get("option"));
        logger.debug(dynamicForm.get("sendMe"));
        logger.debug(dynamicForm.get("text"));
        logger.debug(dynamicForm.get("charity"));
         String opprtunityId=dynamicForm.get("opprtunityId");
      
        logger.debug(dynamicForm.get("opprtunityId"));
        logger.debug(dynamicForm.get("applicantId"));
        if(dynamicForm.get("options")!=null&&dynamicForm.get("option")!=null){
        request().getHeader("X-FORWARDED-FOR");
   		logger.debug(request().remoteAddress());
   		GregorianCalendar cal =new GregorianCalendar();
   		
   		try{
        		JSONObject jsonObject=new JSONObject();
        		jsonObject.put("Type", "Client");
        		jsonObject.put("opprtunityId",dynamicForm.get("opprtunityId"));
        		jsonObject.put("applicantId",dynamicForm.get("applicantId"));

        		jsonObject.put("DateTime", cal.getTime());
        		jsonObject.put("IPAddress", request().remoteAddress());
        		jsonObject.put("Question1", dynamicForm.get("options"));

        		jsonObject.put("Question2", dynamicForm.get("option"));

        		jsonObject.put("Feedback", dynamicForm.get("text"));
        		try{
        		jsonObject.put("FeePaidTo", dynamicForm.get("sendMe"));
        		if(dynamicForm.get("sendMe").equalsIgnoreCase("Charity")){
        			
        		
        		jsonObject.put("charityName", dynamicForm.get("charity"));
        		}
        		}catch(Exception e){
        			logger.error("Error occured in getClientSurveydetails "+e.getMessage());		
        		}
        		new ClientSurvey().checkClientSurvey(jsonObject);
        		

        	}catch(Exception e){
        		logger.error("Error occured in ClientSurvey "+e.getMessage());		
        	}
   		if(!dynamicForm.get("option").equalsIgnoreCase("yack")||!dynamicForm.get("option").equalsIgnoreCase("not so good")){
   		 return redirect("https://forms.visdom.ca/referalV");
   		}else{
   	        return ok(index.render("Thank you for your feedback ....."));

   		}
        }else{
        	
        	  ArrayList list = couchbase.getClientSurveyFromCouchbase(opprtunityId);
          	if(list.size()==0){
          		try{
          			Thread.sleep(6000);
          		}catch(Exception e){
          			logger.error("Error occured in thread "+e.getMessage());			
          		}
          		list = couchbase.getClientSurveyFromCouchbase(opprtunityId);
          		if(list.size()==0){
          			try{
              			Thread.sleep(6000);
              		}catch(Exception e){
              			logger.error("Error occured in thread "+e.getMessage());		
              		}
              		list = couchbase.getClientSurveyFromCouchbase(opprtunityId);
              		if(list.size()==0){
              			try{
                  			Thread.sleep(6000);
                  		}catch(Exception e){
                  			logger.error("Error occured in thread "+e.getMessage());			
                  		}
                  		list = couchbase.getClientSurveyFromCouchbase(opprtunityId);

              		}
          		}
          	}
          	
          	
          	
            	int exist=0;
            	if(list.size()!=0){
            		exist=1;
            	}
        	
            return ok(client.render(dynamicForm.get("opprtunityId"),dynamicForm.get("applicantId"),exist));
        }


    }
    
    
    
    public static Result getReferralSurveydetails(){
        DynamicForm dynamicForm = form().bindFromRequest();
        logger.debug(dynamicForm.get("options"));
        logger.debug(dynamicForm.get("option"));
        logger.debug(dynamicForm.get("recomend"));
        logger.debug(dynamicForm.get("text"));
        logger.debug(dynamicForm.get("sendMe"));
        logger.debug(dynamicForm.get("charity"));

   
        logger.debug(dynamicForm.get("opprtunityId"));
        logger.debug(dynamicForm.get("referralId"));
        
    
        request().getHeader("X-FORWARDED-FOR");
   		logger.debug(request().remoteAddress());
   		GregorianCalendar cal =new GregorianCalendar();
	
   		try{
        		JSONObject jsonObject=new JSONObject();
        jsonObject.put("Type", "Referral");
		jsonObject.put("opprtunityId",dynamicForm.get("opprtunityId"));
		jsonObject.put("referralId",dynamicForm.get("referralId"));

		jsonObject.put("ClientName", "null");
		jsonObject.put("ReferralEmail", "null");
		jsonObject.put("ReferralName", "null");

		jsonObject.put("DateTime", cal.getTime());
		jsonObject.put("IPAddress", request().remoteAddress());
		jsonObject.put("Question1", dynamicForm.get("options"));

		jsonObject.put("Question2", dynamicForm.get("option"));
		jsonObject.put("Question3", dynamicForm.get("recomend"));

		jsonObject.put("Feedback", dynamicForm.get("text"));
		
		try{
    		jsonObject.put("FeePaidTo", dynamicForm.get("sendMe"));
    		if(dynamicForm.get("sendMe").equalsIgnoreCase("Charity")){
    			
    		
    		jsonObject.put("charityName", dynamicForm.get("charity"));
    		}
    		}catch(Exception e){
    			logger.error("Error occured in getReferralSurveydetails "+e.getMessage());			
    		}

	new ReferralSurvey().checkReferralSurvey(jsonObject);

   		}catch(Exception e){
   			logger.error("Error occured in getReferralSurveydetails  "+e.getMessage());	
   		}
   	 return redirect("https://forms.visdom.ca/clientrefV?referralId="+dynamicForm.get("referralId"));
  
    }    
}

 
