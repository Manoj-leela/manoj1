package controllers;


import infrastracture.CouchBaseOperation;

import org.codehaus.jettison.json.JSONObject;

import play.mvc.Controller;
import play.mvc.Result;
import toDoist.CreditInformationTasks;
import toDoist.DocumentAnalyzerTasks;
import toDoist.LeadTasks;
import toDoist.ReferralTasks;
import toDoist.StageMailTasksCreation;
import views.html.index;

import play.Logger;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

public class Application extends Controller {
 	
	private static org.slf4j.Logger log = play.Logger.underlying();

    
    public static Result index() {
        return ok(index.render("Hi test")
        );
    }
    
    
    public static Result creatingTasks() {
    	
    	JsonNode json = request().body().asJson();
		if (json == null) {
			log.debug("Expecting Json data");
			return badRequest("Expecting Json data");
		} else {

			log.debug("properjson data sent");

			ObjectMapper mapper=new  ObjectMapper();
			try {
					JSONObject jsonOfDocumentlist=	new CouchBaseOperation().getCouchBaseData("DocumentListOfApplicationNo_"+json.get("id").textValue());
				dto.ApplicantDocument docList=mapper.readValue(jsonOfDocumentlist.toString(),dto.ApplicantDocument.class);
				new DocumentAnalyzerTasks(docList).start();
			} catch (Exception e) {
				// TODO Auto-generated catch block
				log.error("error in creatingtask s method "+e.getMessage());
			} 
			return ok(index.render("sucess")
			        );
		}
    	
        
    }
    
    
    public static Result createTasksForCreaditInfromation(){
    	JsonNode json = request().body().asJson();

    	try{
    		JSONObject jsonData=new JSONObject(json.toString());
		 CreditInformationTasks.createTasksCredit(jsonData);

    	}catch(Exception e){
    		log.error("error occur while processing createTasksForCreaditInfromation"+e.getMessage());
    	}
    	return ok("sucesss");
    }
    
    
public static Result creatingStageMailTasks() {
    	
    	JsonNode jsona = request().body().asJson();
		if (jsona == null) {
			return badRequest("Expecting Json data");
		} else {
			log.info("json " +jsona);
			try {
				JSONObject json = new JSONObject(jsona.toString());
				String opprunityId=json.get("id").toString();
				int stage=new Integer(json.get("stage_Id").toString());
				String lenderName=null;
				
				try{
					lenderName=json.getString("lender_name");
				}catch(Exception e){
					log.error("error occur while processing creatingStageMailTasks"+e.getMessage());				
				}
				new StageMailTasksCreation(opprunityId, stage,lenderName).start();
			} catch (Exception e) {
				// TODO Auto-generated catch block
				log.error("error in stagemail s method "+e.getMessage());
			} 
			return ok(index.render("sucess")
			        );
		}
    	
        
    }

    
public static Result creatingReferralTasks() {

	JsonNode jsona = request().body().asJson();
	if (jsona == null) {
		return badRequest("Expecting Json data");
	} else {
		log.info("json " +jsona);
		try {
			JSONObject json = new JSONObject(jsona.toString());
			String referralName=json.get("name").toString();
			String phoneNumber=json.get("phonenumber").toString();
			
			String email="";
			String referrdBy="";
			
			try{
				email=json.get("email").toString();
				referrdBy=json.get("referrdBy").toString();
			}catch(Exception e){
				log.error("erroroccur while processing creatingReferralTasks "+e.getMessage());			
			}
		new 	ReferralTasks(referralName, phoneNumber,email,referrdBy).start();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			log.error("error in referral tasks s method "+e.getMessage());
		} 
		return ok(index.render("sucess")
		        );
	}
	
    
} 

public static Result creatingLeadTasks() {
	
	JsonNode jsona = request().body().asJson();
	if (jsona == null) {
		return badRequest("Expecting Json data");
	} else {
		log.info("json " +jsona);
		try {
			JSONObject json = new JSONObject(jsona.toString());
			String leadid=json.get("leadid").toString();
			String leadName=json.get("leadName").toString();
			String leadPhone=json.get("leadPhone").toString();
			String emailid=json.get("leadEmail").toString();
			String leadReferralName=json.get("leadReferralName").toString();
			String leadReferralPhone=json.get("leadReferralPhone").toString();
			String leadReferralEmail=json.get("leadReferralEmail").toString();

			LeadTasks.createLeadtasks(leadid, leadName, leadPhone,emailid ,leadReferralName, leadReferralPhone,leadReferralEmail);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			log.error("error in Creaditasts method "+e.getMessage());
		} 
		return ok(index.render("sucess")
		        );
	}   
} 
  
}
