package controllers;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.HashMap;

import org.json.JSONObject;

import com.couchbase.client.java.document.json.JsonObject;
import com.sendwithus.SendWithUsExample;
import com.syml.couchbase.dao.CouchbaseDaoServiceException;
import com.syml.couchbase.dao.service.CouchBaseService;

import play.Logger;
import couchbase.CouchBaseOperation;
import helper.GenericHelperClass;
import openerp.CheckReferalResource;
import openerp.LeadTaskCreationRestcall;
import openerp.ReferalCreateLead;
import play.*;
import static play.data.Form.form;
import play.data.DynamicForm;
import play.mvc.*;
import views.html.*;

public class Application extends Controller {

	private static org.slf4j.Logger logger = play.Logger.underlying();
	
	public static Result index() {

		// String referralId=request().getQueryString("referralId");
	logger.debug("device type "+request().getHeader("User-Agent"));	
	
	
	String  browserDetails  =   request().getHeader("User-Agent");
    String  userAgent       =   browserDetails;
    String  user            =   userAgent.toLowerCase();

    String os = "";
    String browser = "";

    logger.info("User Agent for the request is===>"+(browserDetails.contains("mobile") || browserDetails.contains("Mobile")));
    //=================OS=======================
     if (userAgent.toLowerCase().indexOf("windows") >= 0 )
     {
         os = "Windows";
     } else if(userAgent.toLowerCase().indexOf("mac") >= 0)
     {
         os = "Mac";
     } else if(userAgent.toLowerCase().indexOf("x11") >= 0)
     {
         os = "Unix";
     } else if(userAgent.toLowerCase().indexOf("android") >= 0)
     {
         os = "Android";
     } else if(userAgent.toLowerCase().indexOf("iphone") >= 0)
     {
         os = "IPhone";
     }else{
         os = "UnKnown, More-Info: "+userAgent;
     }
     return ok("Wel Come To Mortagage Form ");
     //return ok(wfg2.render("Realtor",231,1));
		//return ok(visdomreferral2.render("Realtor",231,1));
	}

	public static Result indexNotVedio() throws UnknownHostException {

		String referralId = request().getQueryString("referralId");
		String firstName="";
		String lastName="";
		String email="";
		try{
			ArrayList<String> list=new CouchBaseOperation().getReferralData(referralId);
			if(list.size()!=0){
				firstName=list.get(2);
				lastName=list.get(3);
				email=list.get(1);
			}
			
		}catch(Exception e){
			logger.error("error while processing ReferralData in couchbase  "+e.getMessage());	
		}
		logger.info("user address "+InetAddress.getLocalHost());
		return ok(submitreferral.render(referralId, "", "", "", "", "", "", "",firstName,lastName,email));
	}

	public static Result indexV() {
		String referralId = request().getQueryString("referralId");
		logger.info("referel id" + referralId);
		String firstName="";
		String lastName="";
		String email="";
		int couchbaseConnectionSucess=0;
		
			ArrayList<String> list;
			try {
				list = (ArrayList<String>) new CouchBaseService().getReferralTriggerDataFromCouhbase("ReferralTrigerData_"+referralId);
		
			if(list.size()!=0){
				firstName=list.get(2);
				lastName=list.get(3);
				email=list.get(1);
			}
			
			} catch (CouchbaseDaoServiceException e) {
				couchbaseConnectionSucess=1;
				logger.error("error in getting referral data from couchbase  "+e.getMessage());
			}catch(Exception e){
				logger.error("error in getting referral data from couchbase  "+e.getMessage());

			}
			if(couchbaseConnectionSucess==1){
				String errorMessage="Due to heavy network traffic, we were unable to access your information in a timely manner. Please try again later.";

				return ok(leadsucess.render(errorMessage));

			}else{
				return ok(submitreferralV
						.render(referralId, "", "", "", "", "", "", "",firstName,lastName,email));
			}
	
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	public static Result submitReferral() {
		SendWithUsExample sendWithus = new SendWithUsExample();
		String formType = "Submit_Referral";
		logger.info("inside   submitReferralwithout vedio   controller method ----- ");
		int couchbaseConnectionSucess=0;
		DynamicForm dynamicForm = form().bindFromRequest();
		String referralid = dynamicForm.get("referralId");
		logger.info("referel id===========" + referralid);
		String ip = dynamicForm.get("ip");

		logger.info("ip----------------"+ip);
		String leadFirstName = dynamicForm.get("fname");
		String leadLastName = dynamicForm.get("lname");
		String leadPhoneNumber = dynamicForm.get("phone");

		String leadEmail = dynamicForm.get("email");
		String leadAddress = dynamicForm.get("formatted_address");

		String referralFirstName = dynamicForm.get("reffirstname");
		String referralLastName = dynamicForm.get("reflastname");
		String referralEmail = dynamicForm.get("refemail");
		String leadFNameRequired = "";
		String leadLNameRequired = "";
		String leadPhoneNumberRequired = "";
		String leadEmailRequired = "";
		String refFnameRequired = "";

		String refLnameRequired = "";

		String refEmailRequired = "";

		boolean sucess = true;

		if (leadLastName != null && leadLastName.isEmpty()) {
			leadFNameRequired = "FirstName Required";
			sucess = false;
		}

		if (leadLastName != null && leadLastName.isEmpty()) {
			leadLNameRequired = "LastName Required";
			sucess = false;

		}

		if (leadPhoneNumber != null && leadPhoneNumber.isEmpty()) {
			leadPhoneNumberRequired = "PhoneNumber Required";
			sucess = false;

		}
		if (leadEmail != null && leadEmail.isEmpty()) {
			leadEmailRequired = "Email Required";
			sucess = false;

		}

		if (referralFirstName != null && referralFirstName.isEmpty()) {
			refFnameRequired = "Your FirstName Required";
			sucess = false;

		}

		if (referralLastName != null && referralLastName.isEmpty()) {
			refEmailRequired = "Your LastName Required";
			sucess = false;

		}

		if (referralEmail != null && referralEmail.isEmpty()) {
			refEmailRequired = "Your Email ID Required";
			sucess = false;

		}

		logger.info("inside Submitreferraform Controller ");

		logger.info("leadFirstName  : " + leadFirstName
				+ "  leadLastName : " + leadLastName + "  leadPhoneNumber : "
				+ leadPhoneNumber + " leadEmail : " + leadEmail
				+ " leadAddress : " + leadAddress + "  referralFirstName : "
				+ referralFirstName + " referralEmail : " + referralEmail);
		String message = "";
		String errorMessage="Due to heavy network traffic, we were unable to access your information in a timely manner. Please try again later.";

		if (sucess) {
			HashMap dataStoreValue = new HashMap();
			ArrayList<String> sucessList = null;
			int sucessMessage = 0;
			CheckReferalResource referalResource = new CheckReferalResource();

			String phoneNumber = "";
			if (referralid != null && !referralid.isEmpty()) {
				dataStoreValue.put("referralIdFound", referralid);
				phoneNumber = referalResource
						.getReferralPhoneNumber(referralid);
			} else {
				sucessList = referalResource
						.findReferralSourceCode(referralEmail);
				try {

					try {
						sucessMessage = Integer.parseInt(sucessList.get(1));
					} catch (Exception e) {
						logger.error("error in getting while parsing succeededList1 "+e.getMessage());
					}
					try {
						referralid = sucessList.get(2);
					} catch (Exception e) {
						logger.error("error in getting while parsing refferalid "+e.getMessage());
					}
					try {
						phoneNumber = sucessList.get(0);
					} catch (Exception e) {
						logger.error("error in getting  while parsing phonenumber  "+e.getMessage());
					}
				} catch (Exception e) {
					logger.error("error in getting  while processing refferal data  "+e.getMessage());
				}
			}

			// display sucess message
			if (sucessMessage == 0) {
				message = "Thank you for verifying your involvement in Visdom's Referral Program. We have sent an email to you confirming the beginning of the mortgage application process for your referral.  In the event you did not receive it, please check the spam folder and mark all emails from Visdom as Not Spam.  Thank You.";

			} else if (sucessMessage == 1 || sucessMessage == 2) {
				message = "Thank you for verifying your involvement in Visdom's Referral Program. We have sent an email to you confirming the beginning of the mortgage application process for your referral.  In the event you did not receive it, please check the spam folder and mark all emails from Visdom as Not Spam.  Thank You.";

			} else {
				message = "Your email address does not match our records.  Please confirm email address provided when you became a involved in Visdoms Referral Program.  If your email address has changed, please send an email to referrals@visdom.ca with all your correct contact information so we can adjust our records accordingly and pay appropriate referral fees.";

			}
			CouchBaseService storeData = new CouchBaseService();

			dataStoreValue.put("Referal_Source_FirstName", referralFirstName);
			dataStoreValue.put("Referal_Source_LastName", referralLastName);
			dataStoreValue.put("Referal_Source_Email",
					referralEmail.toLowerCase());

			dataStoreValue.put("FirstName_of_referal", leadFirstName);
			dataStoreValue.put("LastName_of_referal", leadLastName);
			dataStoreValue.put("phoneNumber_of_referal", leadPhoneNumber);
			dataStoreValue.put("Email_of_referal", leadEmail);
			dataStoreValue.put("Submission_Date_Time", "");
			dataStoreValue.put("address_of_referal", leadAddress);
			dataStoreValue.put("ip", request().remoteAddress());
			dataStoreValue.put("Type", "Lead");

			ReferalCreateLead referalCreateLead = new ReferalCreateLead();

			int code = 0;
			int id = 0;
			try {

				ArrayList list = referalCreateLead
						.checkAndCreateLead(dataStoreValue);
				code = (Integer) list.get(0);

				id = (Integer) list.get(1);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				code = 3;
				logger.error("error in getting  while processing arrayList  "+e.getMessage());
			}
			try{
			if(phoneNumber==null){
				phoneNumber="";
			}
			}catch(Exception e){
				phoneNumber="";
				logger.error("error in getting  while processing phoneNumber  "+e.getMessage());
			}
			if(leadPhoneNumber==null){
				leadPhoneNumber="";
			}
			String key = formType + "_" + id;
			org.codehaus.jettison.json.JSONObject jsonTableData = new org.codehaus.jettison.json.JSONObject();
			try {
				jsonTableData.put("leadid", id);
				jsonTableData.put("leadName", leadFirstName + "_"
						+ leadLastName);
				jsonTableData.put("leadPhone", leadPhoneNumber);
				jsonTableData.put("leadEmail", leadEmail);

			
				jsonTableData.put("leadReferralName", referralFirstName
						+ " " + referralLastName);
				jsonTableData.put("leadReferralPhone", phoneNumber);
				jsonTableData.put("leadReferralEmail", referralEmail);

			} catch (org.codehaus.jettison.json.JSONException e1) {
				// TODO Auto-generated catch block
				logger.error("error in getting  while processing lead Referral Data  "+e1.getMessage());
			}
			if (code == 1) {

			

				// log.debug("calling taskcretion appppppppppp------------->");
				// new
				new LeadTaskCreationRestcall(jsonTableData.toString()).start();
				logger.info("Lead taskcreation done in leadreferral Method ");
				try {
					sendWithus.sendTOreferalSubmittedReferral(leadFirstName,
							referralFirstName, referralEmail);
				} catch (Exception e) {
					logger.error("error in sending mail " + e.getMessage());
				}

				// ConfirmEmailOfReferringLeadMessageTemplate.ConfirmEmailOfReferringLeadMessageTemplateMethod(form1Fname,form1Email,
				// refFirstName, refLastName, refEmail,message,id,"crm.lead");

				JsonObject jsonObject=JsonObject.from(dataStoreValue);
				jsonObject.put("FormType", formType);
				try {
					storeData.storeDataToCouchbase(key, jsonObject);
				} catch (CouchbaseDaoServiceException e1) {
					couchbaseConnectionSucess=1;
					logger.error("error in storing lead data to couchbase "+e1.getMessage());
				}
				try {
					JsonObject jsonObject1 = storeData.getCouhbaseDataByKey(key);
					if (jsonObject != null) {
						new GenericHelperClass(id, jsonObject1).start();
					}
				} catch (Exception e) {
					logger.error("error in adding couchbas data to notes"+e.getMessage());
				}
			} else if (code == 2) {

				message = "The Lead already exsist with your enterd details";
				JsonObject jsonObject=JsonObject.from(dataStoreValue);
				jsonObject.put("FormType", formType);
			
				try {
					storeData.storeDataToCouchbase(key, jsonObject);
				} catch (CouchbaseDaoServiceException e) {
					couchbaseConnectionSucess=1;
					logger.error("error in storing lead data to couchbase "+e.getMessage());

				}
				//new LeadTaskCreationRestcall(jsonTableData.toString()).start();

			} else if (code == 3) {
				message = "Error in creating Lead please fill the form once again";
			}

			if(couchbaseConnectionSucess==1){
				return ok(leadsucess.render(errorMessage));

			}else{
			return ok(leadsucess.render(message));
			}
		} else {
			return ok(submitreferral.render(referralid, leadFNameRequired,
					leadLNameRequired, leadPhoneNumberRequired,
					leadEmailRequired, refFnameRequired, refLnameRequired,
					refEmailRequired,"","",""));

		}
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	public static Result submitReferralV() {
		SendWithUsExample sendWithus = new SendWithUsExample();
		String formType = "Submit_Referral";
		logger.info("inside   submitReferralV  controller method ----- ");
		DynamicForm dynamicForm = form().bindFromRequest();
		String referralid = dynamicForm.get("referralId");
		logger.info("referel id===========" + referralid);
		String errorMessage="Due to heavy network traffic, we were unable to access your information in a timely manner. Please try again later.";

		int couhbaseSucess=0;
		String leadFirstName = dynamicForm.get("fname");
		String leadLastName = dynamicForm.get("lname");
		String leadPhoneNumber = dynamicForm.get("phone");

		String leadEmail = dynamicForm.get("email");
		String leadAddress = dynamicForm.get("formatted_address");

		String referralFirstName = dynamicForm.get("reffirstname");
		String referralLastName = dynamicForm.get("reflastname");
		String referralEmail = dynamicForm.get("refemail");
		String leadFNameRequired = "";
		String leadLNameRequired = "";
		String leadPhoneNumberRequired = "";
		String leadEmailRequired = "";
		String refFnameRequired = "";

		String refLnameRequired = "";

		String refEmailRequired = "";

		boolean sucess = true;

		if (leadLastName != null && leadLastName.isEmpty()) {
			leadFNameRequired = "FirstName Required";
			sucess = false;
		}

		if (leadLastName != null && leadLastName.isEmpty()) {
			leadLNameRequired = "LastName Required";
			sucess = false;

		}

		if (leadPhoneNumber != null && leadPhoneNumber.isEmpty()) {
			leadPhoneNumberRequired = "PhoneNumber Required";
			sucess = false;

		}
		if (leadEmail != null && leadEmail.isEmpty()) {
			leadEmailRequired = "Email Required";
			sucess = false;

		}

		if (referralFirstName != null && referralFirstName.isEmpty()) {
			refFnameRequired = "Your FirstName Required";
			sucess = false;

		}

		if (referralLastName != null && referralLastName.isEmpty()) {
			refEmailRequired = "Your LastName Required";
			sucess = false;

		}

		if (referralEmail != null && referralEmail.isEmpty()) {
			refEmailRequired = "Your Email ID Required";
			sucess = false;

		}

		logger.info("inside Submitreferraform Controller ");

		logger.info("leadFirstName  : " + leadFirstName
				+ "  leadLastName : " + leadLastName + "  leadPhoneNumber : "
				+ leadPhoneNumber + " leadEmail : " + leadEmail
				+ " leadAddress : " + leadAddress + "  referralFirstName : "
				+ referralFirstName + " referralEmail : " + referralEmail);
		String message = "";

		if (sucess) {
			HashMap dataStoreValue = new HashMap();
			ArrayList<String> sucessList = null;
			int sucessMessage = 0;
			CheckReferalResource referalResource = new CheckReferalResource();

			String phoneNumber = "";
			if (referralid != null && !referralid.isEmpty()) {
				dataStoreValue.put("referralIdFound", referralid);
				phoneNumber = referalResource
						.getReferralPhoneNumber(referralid);
				if (phoneNumber != null && phoneNumber.isEmpty()) {
					sucessList = referalResource
							.findReferralSourceCode(referralEmail);

				} else if (phoneNumber == null) {
					sucessList = referalResource
							.findReferralSourceCode(referralEmail);

				}
			} else {
				sucessList = referalResource
						.findReferralSourceCode(referralEmail);

			}

			try {

				try {
					sucessMessage = Integer.parseInt(sucessList.get(1));
				} catch (Exception e) {
					logger.error("error in checking lead data present in  couchbase or not "+e.getMessage());
				}
				try {
					referralid = sucessList.get(2);
				} catch (Exception e) {
					logger.error("error in checking referralid id present or not "+e.getMessage());
				}
				try {
					phoneNumber = sucessList.get(0);
				} catch (Exception e) {
					logger.error("error in checking phoneNumber present or not "+e.getMessage());
				}
			} catch (Exception e) {
				logger.error("error in checking leadData present or not "+e.getMessage());
			}

			// display sucess message
			if (sucessMessage == 0) {
				message = "Thank you for verifying your involvement in Visdom's Referral Program. We have sent an email to you confirming the beginning of the mortgage application process for your referral.  In the event you did not receive it, please check the spam folder and mark all emails from Visdom as Not Spam.  Thank You.";

			} else if (sucessMessage == 1 || sucessMessage == 2) {
				message = "Thank you for verifying your involvement in Visdom's Referral Program. We have sent an email to you confirming the beginning of the mortgage application process for your referral.  In the event you did not receive it, please check the spam folder and mark all emails from Visdom as Not Spam.  Thank You.";

			} else {
				message = "Your email address does not match our records.  Please confirm email address provided when you became a involved in Visdoms Referral Program.  If your email address has changed, please send an email to referrals@visdom.ca with all your correct contact information so we can adjust our records accordingly and pay appropriate referral fees.";

			}
			CouchBaseService storeData = new CouchBaseService();

			dataStoreValue.put("Referal_Source_FirstName", referralFirstName);
			dataStoreValue.put("Referal_Source_LastName", referralLastName);
			dataStoreValue.put("Referal_Source_Email",
					referralEmail.toLowerCase());

			dataStoreValue.put("FirstName_of_referal", leadFirstName);
			dataStoreValue.put("LastName_of_referal", leadLastName);
			dataStoreValue.put("phoneNumber_of_referal", leadPhoneNumber);
			dataStoreValue.put("Email_of_referal", leadEmail);
			dataStoreValue.put("Submission_Date_Time", "");
			dataStoreValue.put("address_of_referal", leadAddress);
			dataStoreValue.put("ip", request().remoteAddress());
			dataStoreValue.put("Type", "Lead");

			ReferalCreateLead referalCreateLead = new ReferalCreateLead();

			int code = 0;
			int id = 0;
			try {

				ArrayList list = referalCreateLead
						.checkAndCreateLead(dataStoreValue);
				code = (Integer) list.get(0);

				id = (Integer) list.get(1);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				code = 3;
				logger.error("error in checking leadData present or not in couchbase"+e.getMessage());
			}

			
			try{
				if(phoneNumber==null){
					phoneNumber="";
				}
				}catch(Exception e){
					phoneNumber="";
					logger.error("error while  processing phoneNumber present or not"+e.getMessage());
				}
			if(leadPhoneNumber==null){
				leadPhoneNumber="";
			}
			String key = formType + "_" + id;
			org.codehaus.jettison.json.JSONObject jsonTableData = new org.codehaus.jettison.json.JSONObject();
			try {
				jsonTableData.put("leadid", id);
				jsonTableData.put("leadName", leadFirstName + "_"
						+ leadLastName);
				jsonTableData.put("leadPhone", leadPhoneNumber);
				jsonTableData.put("leadEmail", leadEmail);

			
				jsonTableData.put("leadReferralName", referralFirstName
						+ " " + referralLastName);
				jsonTableData.put("leadReferralPhone", phoneNumber);
				jsonTableData.put("leadReferralEmail", referralEmail);


			} catch (org.codehaus.jettison.json.JSONException e1) {
				// TODO Auto-generated catch block
				logger.error("error while upadting LeadData in couchbase"+e1.getMessage());
			}

			if (code == 1) {

		
				new LeadTaskCreationRestcall(jsonTableData.toString()).start();
				logger.info("Lead taskcreation done in leadreferralVedio Method ");

				try {
					sendWithus.sendTOreferalSubmittedReferral(leadFirstName,
							referralFirstName, referralEmail);
				} catch (Exception e) {
					logger.error("error in sending mail " +e.getMessage());
				}

				// ConfirmEmailOfReferringLeadMessageTemplate.ConfirmEmailOfReferringLeadMessageTemplateMethod(form1Fname,form1Email,
				// refFirstName, refLastName, refEmail,message,id,"crm.lead");

				JsonObject jsonObject=JsonObject.from(dataStoreValue);
				jsonObject.put("FormType", formType);

				try {
					storeData.storeDataToCouchbase(key, jsonObject);
				} catch (CouchbaseDaoServiceException e1) {
					couhbaseSucess=1;
					logger.error("error in storing data to couchbase "+e1.getMessage());

				}
				try {
					JsonObject jsonObject2 = storeData.getCouhbaseDataByKey(key);
					if (jsonObject != null) {
						new GenericHelperClass(id, jsonObject2).start();
					}
				} catch (Exception e) {
					logger.error("error in adding couchbas data to notes"+e.getMessage());
				}
			} else if (code == 2) {
				//new LeadTaskCreationRestcall(jsonTableData.toString()).start();
				logger.info("Lead taskcreation done in leadreferralVedio Method ");
				message = "The Lead already exsist with your enterd details";
				JsonObject jsonObject=JsonObject.from(dataStoreValue);
				jsonObject.put("FormType", formType);
				try {
					storeData.storeDataToCouchbase(key, jsonObject);
				} catch (CouchbaseDaoServiceException e) {
					couhbaseSucess=1;
					logger.error("error in storing data to couchbase "+e.getMessage());
				}

			} else if (code == 3) {
				message = "Error in creating Lead please fill the form once again";
			}

			
			
			if(couhbaseSucess==1){
				return ok(leadsucess.render(errorMessage));

			}else{
				return ok(leadsucess.render(message));

			}

		} else {
			return ok(submitreferralV.render(referralid, leadFNameRequired,
					leadLNameRequired, leadPhoneNumberRequired,
					leadEmailRequired, refFnameRequired, refLnameRequired,
					refEmailRequired,"","",""));
		}
	}

	public static Result visdomReferral() {
		logger.debug("testtststs");
		return ok(visdomferral.render("sucess"));
	}

}
