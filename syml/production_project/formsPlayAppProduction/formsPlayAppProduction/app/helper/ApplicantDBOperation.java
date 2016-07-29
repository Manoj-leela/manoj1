package helper;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;

import openerp.CreateApplicant;

import org.json.JSONException;
import org.json.JSONObject;

import play.Logger;
import controllers.ApplicantBasicDetails;
import couchbase.CouchBaseOperation;

public class ApplicantDBOperation {
	CouchBaseOperation  couchbaseObject=null;
	String applicantID ="";
	String applicantIDCB="Applicant_"+applicantID;
	JSONObject jsonObject=null;
	HashMap<String, String> dataStoreValue = null;
	CreateApplicant applicantCreate = null;
	
	public void createApplicant(ApplicantBasicDetails appBasicDetails,String ip){
		
		Logger.info("Inside createApplicant method ");
		dataStoreValue = new HashMap<String, String>();
		try{
		Logger.debug("Additional Applicant"+appBasicDetails.getAdditionalApplicant());
		// Let's make the formType Mortgage Application which allows us to
		//"Group" the applicaiton in Couchbase
		String formType = "Mortgage Application"; 
		// Let's create a subform field which allows to identify this 
		// particular "subform" submission.
		String subForm = "Mortgage Application 1a";
					
		DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
		// get current date time with Calendar()
		Calendar cal = Calendar.getInstance();
		String currentDateTime = (dateFormat.format(cal.getTime()));
		// get ip of latest form sumitted
		
	
			applicantCreate = new CreateApplicant();
		applicantCreate.createApplicant(appBasicDetails.getFirstName(),	appBasicDetails.getLastName(), appBasicDetails.getEmail());
		int appId = applicantCreate.getId();
		System.out.println("");
		Logger.debug("Applicant Created in openErp with id "+appId);
		applicantID = Integer.toString(appId);
		appBasicDetails.setApplicantId(applicantID);
		dataStoreValue.put("FirstName_of_applicant", appBasicDetails.getFirstName());
		dataStoreValue.put("LastName_of_applicant", appBasicDetails.getLastName());
		dataStoreValue.put("Email_of_applicant", appBasicDetails.getEmail());
		dataStoreValue.put("FormType", formType);
		dataStoreValue.put("Applicant-SubForm-1a", subForm);
		//Changed name of field in DB to allow for a different
		//submission time for each subform in the Record.
		dataStoreValue.put("Submission_Date_Time1a", currentDateTime); 
		dataStoreValue.put("Applicant-additionalApplicant", appBasicDetails.getAdditionalApplicant());
		dataStoreValue.put("ip", ip);
		//uncomment this line
		dataStoreValue.put("ApplicantID1", applicantID);
//		dataStoreValue.put("ApplicantID1", "2753");
		dataStoreValue.put("Applicant-leadingGoal", appBasicDetails.getCurrentLendingGoal());
		
		couchbaseObject = new CouchBaseOperation();
		couchbaseObject.storeDataInCouchBase("Applicant_" + applicantID,formType, dataStoreValue);
		Logger.debug("Data is stored in couchbase with id  Applicant_"+applicantID);
		Logger.info("input from form2 referal resource : firstname "
				+ appBasicDetails.getFirstName() + "\t lastname : " + appBasicDetails.getLastName()
				+ "\t email : " + appBasicDetails.getEmail() + "\t current time : "
				+ currentDateTime + "\t ip : " + ip);
		}catch(JSONException | NullPointerException excp){
			Logger.debug("Exception when reading record from couchbase.",excp);
		}
		
	}
	public void createCoApplicant(ApplicantBasicDetails appBasicDetails,String ip){
		Logger.info("Inside createCoApplicant method of PorpertiesDBOperation");
		dataStoreValue = new HashMap<String, String>();
		try{
		//jsonObject = couchbaseObject.getCouchBaseData(applicantIDCB);
		String formType = "Mortgage Application"; 
		String subForm = "Mortgage Co-Application 1b";
		
		DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
		// get current date time with Calendar()
		Calendar cal = Calendar.getInstance();
		String currentDateTime = (dateFormat.format(cal.getTime()));
		
		Logger.info("input from form1 mortgage application  : firstname "
				+ appBasicDetails.getCoAppFirstName() + "\t lastname : "
				+ appBasicDetails.getCoAppLastName() + "\t email : " + appBasicDetails.getCoAppEmail()
				+ "\t current time : " + currentDateTime + "\t ip : " + ip);
		
		applicantCreate = new CreateApplicant();
		applicantCreate.createApplicant(appBasicDetails.getCoAppFirstName(),appBasicDetails.getCoAppLastName(), appBasicDetails.getCoAppEmail());
		int coAppId = applicantCreate.getId();
		String coApplicantId = Integer.toString(coAppId);
		Logger.debug("CoApplicant id is: "+coApplicantId);
		
		dataStoreValue.put("FirstName_of_co_applicant", appBasicDetails.getCoAppFirstName());
		dataStoreValue.put("LastName_of_co_applicant", appBasicDetails.getCoAppLastName());
		dataStoreValue.put("Email_of_co_applicant",appBasicDetails.getCoAppEmail());
		dataStoreValue.put("FormType", formType);
		dataStoreValue.put("CoApplicant_SubForm1b", subForm);
		//Changed name of field in DB to allow for a different
		//submission time for each subform in the Record.
		dataStoreValue.put("Submission_Date_Time1b", currentDateTime); 
		dataStoreValue.put("ip_of_co_applicant", ip);
		dataStoreValue.put("ApplicantID_2", coApplicantId);
		
		couchbaseObject = new CouchBaseOperation();
		couchbaseObject.appendDataInCouchBase("Applicant_"+appBasicDetails.getApplicantId(), dataStoreValue);
		Logger.debug("Data is stored in couchbase with id  Applicant_"+appBasicDetails.getApplicantId());
		
		}catch(JSONException | NullPointerException excp){
			Logger.debug("Exception when reading record from couchbase.",excp);
		}
	}
	
	public ApplicantBasicDetails readApplicantRecord(){
		
	Logger.info("Inside readApplicantRecord method of PorpertiesDBOperation");
	ApplicantBasicDetails appBasicDetail = new ApplicantBasicDetails();
	
	try{
		couchbaseObject =new CouchBaseOperation();
		jsonObject = couchbaseObject.getCouchBaseData(applicantIDCB);
		appBasicDetail.setFirstName(jsonObject.getString("FirstName_of_applicant"));
		appBasicDetail.setLastName(jsonObject.getString("LastName_of_applicant"));
		appBasicDetail.setEmail(jsonObject.getString("Email_of_applicant"));
		appBasicDetail.setAdditionalApplicant(jsonObject.getString("Applicant-additionalApplicant"));
		appBasicDetail.setCurrentLendingGoal(jsonObject.getString("Applicant-leadingGoal"));
	}catch(JSONException | NullPointerException excp){
		Logger.debug("Exception when reading record from couchbase.",excp);
	}
	return appBasicDetail;
}
	public ApplicantBasicDetails readCoApplicantRecord(){
		
		Logger.info("Inside readApplicantRecord method of PorpertiesDBOperation");
		ApplicantBasicDetails appBasicDetail = new ApplicantBasicDetails();
		try{
			couchbaseObject =new CouchBaseOperation();
			jsonObject = couchbaseObject.getCouchBaseData(applicantIDCB);
			appBasicDetail.setCoAppFirstName(jsonObject.getString("FirstName_of_co_applicant"));
			appBasicDetail.setCoAppLastName(jsonObject.getString("LastName_of_co_applicant"));
			appBasicDetail.setCoAppEmail(jsonObject.getString("Email_of_co_applicant"));
		}catch(JSONException | NullPointerException excp){
			Logger.debug("Exception when reading record from couchbase.",excp);
		}
		return appBasicDetail;
	}
	
	//Use this code for reading mortgagePage1 
	public ApplicantBasicDetails readApplicantRecords(){
		Logger.info("Inside readApplicantRecord method of PorpertiesDBOperation");
		ApplicantBasicDetails appBasicDetail = new ApplicantBasicDetails();
		String additionalApplicant="";
		try{
			couchbaseObject =new CouchBaseOperation();
			jsonObject = couchbaseObject.getCouchBaseData(applicantIDCB);
			additionalApplicant=jsonObject.getString("Applicant-additionalApplicant");
			if(additionalApplicant != "" && additionalApplicant.equalsIgnoreCase("yes")){
				appBasicDetail.setCoAppFirstName(jsonObject.getString("FirstName_of_co_applicant"));
				appBasicDetail.setCoAppLastName(jsonObject.getString("LastName_of_co_applicant"));
				appBasicDetail.setCoAppEmail(jsonObject.getString("Email_of_co_applicant"));
			}
			appBasicDetail.setFirstName(jsonObject.getString("FirstName_of_applicant"));
			appBasicDetail.setLastName(jsonObject.getString("LastName_of_applicant"));
			appBasicDetail.setEmail(jsonObject.getString("Email_of_applicant"));
			appBasicDetail.setAdditionalApplicant(jsonObject.getString("Applicant-additionalApplicant"));
			appBasicDetail.setCurrentLendingGoal(jsonObject.getString("Applicant-leadingGoal"));
		}catch(JSONException | NullPointerException excp){
			Logger.debug("Exception when reading record from couchbase.",excp);
		}
		return appBasicDetail;
	}
}
