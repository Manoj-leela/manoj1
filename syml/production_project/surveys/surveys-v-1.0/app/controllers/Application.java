package controllers;

import static play.data.Form.form;
import infrastracture.couchbase.CouchBaseOperation;

import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;

import javax.naming.ReferralException;

import play.data.DynamicForm;
import play.mvc.Controller;
import play.mvc.Result;
import views.html.client;
import views.html.index;
import views.html.referral;

import com.couchbase.client.java.document.json.JsonObject;
import com.syml.Constants;
import com.syml.client.ClientSurvey;
import com.syml.client.ClientSurveyException;
import com.syml.referral.ReferralSurvey;
import com.syml.referral.RefferalSurveyException;

public class Application extends Controller {

	private static org.slf4j.Logger logger = play.Logger.underlying();

	public static Result index() throws FileNotFoundException {
		return ok(index.render(""));
	}

	static CouchBaseOperation couchbase = new CouchBaseOperation();
	static ClientSurvey clientSurvey = new ClientSurvey();
	static ReferralSurvey referralSurvey = new ReferralSurvey();
	
	/**
	 * To Redirect Client survey page First check user is already survey form filled or not
	 * if filled we not allow to fill form once again else allowed fill form once again
	 * @return
	 */
	public static Result getClientDetails()  {

		logger.debug("(.) inside getClientDetails method of Controler");
		String applicantId = request().getQueryString("app");
		String opprtunityId = request().getQueryString("op");
		request().getHeader("X-FORWARDED-FOR");
		logger.debug(request().remoteAddress());
		ArrayList<Client> list = null;
		int exist = 0;
		int dataExist = 0;
		try {
			try{
			if(opprtunityId!=null)
			list = clientSurvey.getListOfClientSurvey(opprtunityId);
			
			}catch(ClientSurveyException e){
				logger.debug("Error in getting Client survey details from couchbase query ", e);
			}catch(Exception e){
				logger.debug("Error in getting Client survey details from couchbase query ", e);
			}
			if (list != null) {
				if (list.size() != 0) {
					exist = 1;
				}
			}
			if (exist == 0) {
				return ok(client.render(opprtunityId, applicantId, exist));
			} else {
				for (Iterator<Client> iterator = list.iterator(); iterator.hasNext();) {
					Client client = (Client) iterator.next();

					if (client.getApplicantId().equalsIgnoreCase(applicantId)) {
						dataExist = 1;
					}
				}
				if (dataExist == 0) {
					return ok(client.render(opprtunityId, applicantId, exist));

				} else {
					return ok(index.render(
							"You have already completed a survey for this mortgage with Visdom.  Thank you for your feedback."));
				}
			}
		} catch ( Exception cb) {
			logger.error("The client survey list has not found "+cb.getMessage());
			return ok(index.render("The Cleint survey list has not found" + cb));
		}

		

	}

	/**
	 * To Redirect referral survey page First check user is already survey form filled or not
	 * if filled we not allow to fill form once again else allowed fill form once again
	 * @return
	 */
	public static Result getReferralDetails() {
		logger.debug("(.) inside getReferralDetails method of Controler");

		String referralid = request().getQueryString("app");
		String opprtunityId = request().getQueryString("op");
		request().getHeader("X-FORWARDED-FOR");
		logger.debug(request().remoteAddress());
		int exist = 0;

		ArrayList<Client> list = null;
		try {
			try{
			if(opprtunityId!=null)
			list = referralSurvey.getReferralSurveyFromCouchbase(opprtunityId);
			}catch(RefferalSurveyException e){
				logger.error("Error in getting Referral Survey details from couchbase ",e);
			}catch(Exception e){
				logger.debug("Error in getting Client survey details from couchbase query ", e);
			}
		
		if (list != null) {
			if (list.size() != 0) {
				exist = 1;
			}
		}
		if (exist == 0) {
			return ok(referral.render(opprtunityId, referralid, exist));
		} else {
			return ok(index.render(
					"You have already completed a survey for this mortgage with Visdom.  Thank you for your feedback."));

		}} catch (Exception  cbe) {
			logger.error("The Refferall survey list has not found" + cbe.getMessage());
			return ok(index.render("The client survey list has not found" + cbe)); 
		}

	}

	/**
	 * To store  client feedback details into Couchbase 
	 * * @return
	 */
	public static Result postClientSurveydetails() {
		logger.debug("(.) inside postClientSurveydetails method of Controler");

		DynamicForm dynamicForm = form().bindFromRequest();
			logger.debug(dynamicForm.get("charity"));
		String opprtunityId = dynamicForm.get("opprtunityId");

		if (dynamicForm.get("options") != null && dynamicForm.get("option") != null) {
			request().getHeader("X-FORWARDED-FOR");

			JsonObject jsonObject = getClientSurveyJsonFromDynamicForm(dynamicForm);
			try {
				new ClientSurvey().storeClientSurvey(jsonObject);
			} catch (ClientSurveyException e) {
				logger.error("The Cleint data has not stored " + e.getMessage());
				return ok(index.render("The Cleint data has not stored"));
			}
			if (!dynamicForm.get("option").equalsIgnoreCase("yack")
					|| !dynamicForm.get("option").equalsIgnoreCase("not so good")) {
				return redirect(Constants.REFERRAL_SOURCE_URL);
			} else {
				return ok(index.render("Thank you for your feedback ....."));

			}
		} else {
			ArrayList<Client> list = null;
			try {
				if(opprtunityId!=null)
					list = clientSurvey.getListOfClientSurvey(opprtunityId);
			
			} catch (ClientSurveyException cbe) {
				logger.error("The Cleint survey list has not found" + cbe.getMessage());
				return ok(index.render("The client survey list has not found" + cbe)); 
			}
		

			int exist = 0;
			if (list!=null&&list.size() != 0) {
				exist = 1;
			}

			return ok(client.render(dynamicForm.get("opprtunityId"), dynamicForm.get("applicantId"), exist));
		}

	}

	/**
	 * To store  referral feedback details into Couchbase 
	 * * @return
	 */
	public static Result postReferralSurveydetails() {
		DynamicForm dynamicForm = form().bindFromRequest();
		logger.debug("(.) inside postReferralSurveydetails method of Controller ");

		request().getHeader("X-FORWARDED-FOR");
		logger.debug(request().remoteAddress());
		JsonObject jsonObject;
			jsonObject = getRefferalSurveyJsonFromDynamicForm(dynamicForm);
				try {
					new ReferralSurvey().storeReferralSurvey(jsonObject);
				} catch (RefferalSurveyException e) {
					logger.error("Refferal data has been not stored",e);
				}
		return redirect(Constants.REFERRAL_URL + dynamicForm.get("referralId"));

	}
	private static JsonObject getClientSurveyJsonFromDynamicForm(DynamicForm dynamicForm) {
		 logger.debug("(.) getClientJsonFromDynamicForm in Application");
		JsonObject jsonObject = JsonObject.create();

		jsonObject.put("Type", "Client");
		jsonObject.put("opprtunityId", dynamicForm.get("opprtunityId"));
		jsonObject.put("applicantId", dynamicForm.get("applicantId"));

		jsonObject.put("DateTime", new Date().toString());
		jsonObject.put("IPAddress", request().remoteAddress());
		jsonObject.put("Question1", dynamicForm.get("options"));

		jsonObject.put("Question2", dynamicForm.get("option"));

		jsonObject.put("Feedback", dynamicForm.get("text"));

		jsonObject.put("FeePaidTo", dynamicForm.get("sendMe"));
		if (dynamicForm.get("sendMe").equalsIgnoreCase("Charity")) {

			jsonObject.put("charityName", dynamicForm.get("charity"));
		}

		return jsonObject;
	}
	
	private static JsonObject getRefferalSurveyJsonFromDynamicForm(DynamicForm dynamicForm) {
		 logger.debug("(.) getRefferalSurveyJsonFromDynamicForm in Application");
		
		JsonObject jsonObject = JsonObject.create();
			jsonObject.put("Type", "Referral");
			jsonObject.put("opprtunityId", dynamicForm.get("opprtunityId"));
			jsonObject.put("referralId", dynamicForm.get("referralId"));

			jsonObject.put("ClientName", "null");
			jsonObject.put("ReferralEmail", "null");
			jsonObject.put("ReferralName", "null");

			jsonObject.put("DateTime",new Date().toString());
			jsonObject.put("IPAddress", request().remoteAddress());
			jsonObject.put("Question1", dynamicForm.get("options"));

			jsonObject.put("Question2", dynamicForm.get("option"));
			jsonObject.put("Question3", dynamicForm.get("recomend"));

			jsonObject.put("Feedback", dynamicForm.get("text"));

		
				jsonObject.put("FeePaidTo", dynamicForm.get("sendMe"));
				if (dynamicForm.get("sendMe").equalsIgnoreCase("Charity")) {

					jsonObject.put("charityName", dynamicForm.get("charity"));
				}
			

		return jsonObject;
	}


	
	/**
	 * duplicate getClientDetails1
	 * @return
	 */
	public Result getClientDetails1() {

		
		String applicantId = request().getQueryString("app");
		String opprtunityId = request().getQueryString("op");
		request().getHeader("X-FORWARDED-FOR");
		logger.debug(request().remoteAddress());
		ArrayList<Client> list = null;

		int exist = 0;
		try {
			if(opprtunityId!=null)
			list = clientSurvey.getListOfClientSurvey(opprtunityId);
			
			logger.debug("List of client from couchbase " + list.size());
			if (list != null) {
				if (list.size() != 0)
					exist = 1;
			}

			if (exist == 0)
				return ok(client.render(opprtunityId, applicantId, exist));
			else {
				int dataExist = 0;
				for (Iterator<Client> iterator = list.iterator(); iterator.hasNext();) {
					Client client = (Client) iterator.next();

					if (client.getApplicantId().equalsIgnoreCase(applicantId))
						dataExist = 1;
				}
				if (dataExist == 0) {
					return ok(client.render(opprtunityId, applicantId, exist));
				} else {
					return ok(index.render(
							"You have already completed a survey for this mortgage with Visdom.  Thank you for your feedback."));
				}
			}

		} catch (ClientSurveyException cbException) {
			logger.error("The client survey document list not found ", cbException);
			return ok(index.render("The client survey document list not found " + cbException));
		}

	}

}
