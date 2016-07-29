package com.sendwithus;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.Map;
import java.util.TimeZone;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.sendwithus.exception.SendWithUsException;
import com.sendwithus.model.SendReceipt;



public class SendWithUsExample {

	private static org.slf4j.Logger logger = play.Logger.underlying();
	 
    public static final String SENDWITHUS_API_KEY = "live_a7c95c3b0fb3acb519463955b1a2be67b2299734";

	 public static final String SENDWITH_US_CREDITMAIL_TEMPLATE_KEY = "tem_gKLfppVMBWcxnSRofCsYum";
	   
	    
	    
	    public static void main(String[] args) throws IOException {
		new SendWithUsExample().errorInRetrievingCreditMail("test", "venkatesh.m@bizruntime.com", "tests");
	    }
	    String senderEmail="support@visdom.ca";

	    
				public void errorInRetrievingCreditMail(String applicantFirstName,
					String EmailId,
					String opporunityName) throws JsonProcessingException {
		
				Map<String, Object>[] ccMap = null;
				SendWithUs sendwithusAPI = new SendWithUs(SENDWITHUS_API_KEY);
		
				// Print list of available emails
		
				logger.debug("First steps Email Ok");
				// Send Welcome Email
				Map<String, Object> recipientMap = new HashMap<String, Object>();
				recipientMap.put("name", "Error Retrieving Credit"); // optional
				recipientMap.put("address", EmailId);
				

			
					ccMap = (Map<String, Object>[]) new Map[1];

					
					Map<String, Object> ccMap1 = new HashMap<String, Object>();
					
					ccMap1.put("address", "assistant@visdom.ca");
			
					ccMap[0] = ccMap1;
			
					// sender is optional
					Map<String, Object> senderMap = new HashMap<String, Object>();
					senderMap.put("name", "Error in Retrieving Credit "); // optional
					senderMap.put("address", senderEmail);
					senderMap.put("reply_to", senderEmail); // optional
			
					// email data in to inject in the email template
					Map<String, Object> emailDataMap = new HashMap<String, Object>();
					SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
					SendWithUsExample sendus = new SendWithUsExample();
					Calendar cal = Calendar.getInstance();
					cal.add(Calendar.DATE, 3);
					String currentDateTime = (dateFormat.format(cal.getTime()));
					String mesage = sendus.getGreeting();
					emailDataMap.put("Greeting", mesage);
					
						emailDataMap.put("ApplicantName", applicantFirstName);
						emailDataMap.put("OpportunityName", opporunityName);

					

	        // Example sending a simple email
	        logger.debug("Before Try");
	        try {
	        
	           SendReceipt sendReceipt = sendwithusAPI.send(SENDWITH_US_CREDITMAIL_TEMPLATE_KEY,
	                recipientMap,
	                senderMap,
	                emailDataMap,
	                ccMap
	            );
	            logger.debug("",sendReceipt);
	        	} catch (SendWithUsException e) {
	            logger.error("Error while retrieving credit mail"+e.getMessage());
	        }
	     
	    }
	    				
	    public String getGreeting(){
	    	String greeting="";
	    	Calendar time = new GregorianCalendar();  

	    					time.setTimeZone(TimeZone.getTimeZone("Canada/Mountain"));
	    
	    	int hour = time.get(Calendar.HOUR_OF_DAY);  
	    	int min = time.get(Calendar.MINUTE);  
	    	int day = time.get(Calendar.DAY_OF_MONTH);  
	    	int month = time.get(Calendar.MONTH) + 1;  
	    	int year = time.get(Calendar.YEAR);
	    	logger.debug("date Time "+time.getTime());

	    	logger.debug("The current time is \t" + hour + ":" + min);  
	    	logger.debug("Today's date is \t" + month + "/" + day + "/"  
	    	  + year);  
	

	    	if (hour < 12){
	    		greeting="Good Morning";
	    	 logger.debug("Good Morning");  
	    	}else if (hour >12 && hour < 16 ){  
	    		greeting="Good Afternoon";
	    	 logger.debug("Good Afternoon");  
	    	}else if (hour == 12)  {
	    		greeting="Good Afternoon";

	    	 logger.debug("Good Afternoon");  
	    	 
	    }else if (hour == 16)  {
			greeting="Good Evening";

		 logger.debug("Good Evening");  
		 
	}
	    	
	    	
	    	else if (hour > 16 && hour<24)  {
				greeting="Good Evening";


	    	}else  
	    	 logger.debug("Good Evening"); 
			return greeting;
	    	
	    	
	    	
	    }	   

}
