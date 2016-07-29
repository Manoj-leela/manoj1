package helper;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;

import openerp.CreateApplicant;

import org.json.JSONException;
import org.json.JSONObject;

import play.Logger;
import controllers.PersonalInfo;
import couchbase.CouchBaseOperation;

public class PersonalInfoDBOperation {

	CouchBaseOperation  couchbaseObject=null;
	
	String applicantID;
	
	public PersonalInfoDBOperation(String applicantID) {
		
		this.applicantID=applicantID;
		// TODO Auto-generated constructor stub
	}
	
	String applicantIDCB="Applicant_"+applicantID;
	JSONObject jsonObject=null;
	HashMap dataStoreValue = null;
	CreateApplicant applicantCreate = null;
	PersonalInfo personalInfo = null;
	CreateApplicant updateApplicant=null;
	
	public void updatePersonalInfo(PersonalInfo personalInfo,String ip){
		Logger.info("Inside getPersonalInfo method of PersonalInfoDBOperation ");
		Logger.debug("mobilePhone "+personalInfo.getMobilePhone()+"\n workPhone "+personalInfo.getWorkPhone()+"\n homePhone "+personalInfo.getHomePhone()
				+"\n birthday "+personalInfo.getBirthDay()+"\n insurance "+personalInfo.getSocialInsurance()+"\nrelationshipStatus "+personalInfo.getRelationStatus()
				+"\n dependant "+personalInfo.getDependents()+"\n refareYouCanada " +personalInfo.getAreUCanadianRes()+"\n movedCanadas "+personalInfo.getMovedCanada());
		
		try{
			dataStoreValue=new HashMap();
			updateApplicant=new CreateApplicant();
			String formType ="Mortgage Application";
			String subForm ="Mortgage Application 6a";
			String inputBirthDay=personalInfo.getBirthDay();
			DateFormat df = new SimpleDateFormat("MM/dd/yyyy");
			Date birthday=null;
			 try {
				 birthday = df.parse(inputBirthDay);
			 } catch (ParseException e) {
				Logger.error("Error in parsing string to date");
			 }
			 
			dataStoreValue.put("Applicant-mobile", personalInfo.getMobilePhone());
			dataStoreValue.put("Applicant-workPhone", personalInfo.getWorkPhone());
			dataStoreValue.put("Applicant-homePhone", personalInfo.getHomePhone());
			dataStoreValue.put("Applicant-birthday", personalInfo.getBirthDay());
			dataStoreValue.put("Applicant-insurance", personalInfo.getSocialInsurance());
			dataStoreValue.put("Applicant-relationshipStatus", personalInfo.getRelationStatus());
			dataStoreValue.put("Applicant-dependant", personalInfo.getDependents());
			dataStoreValue.put("Applicant-non_Resident", personalInfo.getAreUCanadianRes());
			dataStoreValue.put("Applicant-moved_canada", personalInfo.getMovedCanada());
			dataStoreValue.put("Applicant-SubForm-6",subForm);
			boolean non_ResidentBool;
			if (personalInfo.getAreUCanadianRes() != null
					&& personalInfo.getAreUCanadianRes().equalsIgnoreCase("Yes")) {
				non_ResidentBool = true;
			} else {
				non_ResidentBool = false;
			}
			String relationshipStatus="";
			if (personalInfo.getRelationStatus() != null
					&& personalInfo.getRelationStatus().equalsIgnoreCase("Common-Law")) {
				relationshipStatus = "Common_Law";
			}
			updateApplicant.updateApplicant(applicantID, personalInfo.getMobilePhone(), personalInfo.getWorkPhone(), personalInfo.getHomePhone(), personalInfo.getSocialInsurance(), relationshipStatus,non_ResidentBool);
			Logger.debug("Record is updated in openERP Applicant module.");
			
			couchbaseObject=new CouchBaseOperation();
			couchbaseObject.appendDataInCouchBase("Applicant_"+applicantID, dataStoreValue);
			Logger.debug("Record is updated in couchbase ");
			
		}catch(JSONException | NullPointerException excp){
			Logger.error("Error  when inserting personal infor of mortgagePage6a record from couchbase.",excp);
		}
		
	}
	
	public void updateCoApplicantPersonalInfo(PersonalInfo personalInfo,String ip,String applicantID2){
		Logger.info("Inside updateCoApplicantPersonalInfo method of PersonalInfoDBOperation ");
		Logger.debug("mobilePhone "+personalInfo.getCoMobilePhone()+"\n workPhone "+personalInfo.getCoWorkPhone()+"\n homePhone "+personalInfo.getCoHomePhone()
				+"\n birthday "+personalInfo.getCoBirthDay()+"\n insurance "+personalInfo.getCoSocialInsurance()+"\nrelationshipStatus "+personalInfo.getCoRelationStatus()
				+"\n dependant "+personalInfo.getCoDependents()+"\n refareYouCanada " +personalInfo.getCoAreUCanadianRes()+"\n movedCanadas "+personalInfo.getCoMovedCanada());
		
		try{
			dataStoreValue=new HashMap();
			updateApplicant=new CreateApplicant();
			String formType ="Mortgage Application";
			String subForm ="Mortgage Application 6b";
			String inputBirthDay=personalInfo.getCoBirthDay();
			DateFormat df = new SimpleDateFormat("MM/dd/yyyy");
			Date birthday=null;
			 try {
				 birthday = df.parse(inputBirthDay);
			 } catch (ParseException e) {
				Logger.error("Error in parsing string to date");
			 }
				
			dataStoreValue.put("CoApplicant-mobile", personalInfo.getCoMobilePhone());
			dataStoreValue.put("CoApplicant-workPhone", personalInfo.getCoWorkPhone());
			dataStoreValue.put("CoApplicant-homePhone", personalInfo.getCoHomePhone());
			dataStoreValue.put("CoApplicant-birthday", personalInfo.getCoBirthDay());
			dataStoreValue.put("CoApplicant-insurance", personalInfo.getCoSocialInsurance());
			dataStoreValue.put("CoApplicant-relationshipStatus", personalInfo.getCoRelationStatus());
			dataStoreValue.put("CoApplicant-dependant", personalInfo.getCoDependents());
			dataStoreValue.put("CoApplicant-newtoCanada", personalInfo.getCoAreUCanadianRes());
			dataStoreValue.put("CoApplicant-whendidcametoCannada", personalInfo.getCoMovedCanada());
			dataStoreValue.put("CoApplicant-SubForm-6",subForm);
			boolean non_ResidentBool;
			if (personalInfo.getCoAreUCanadianRes() != null
					&& personalInfo.getCoAreUCanadianRes().equalsIgnoreCase("Yes")) {
				non_ResidentBool = true;
			} else {
				non_ResidentBool = false;
			}
			String relationshipStatus="";
			if (personalInfo.getCoRelationStatus() != null
					&& personalInfo.getCoRelationStatus().equalsIgnoreCase("Common-Law")) {
				relationshipStatus = "Common_Law";
			}
			
			updateApplicant.updateApplicant(applicantID2, personalInfo.getCoMobilePhone(), personalInfo.getCoWorkPhone(), personalInfo.getCoHomePhone(), personalInfo.getCoSocialInsurance(),relationshipStatus,non_ResidentBool,birthday);
			Logger.debug("Record is updated in openERP applicant module");
			
			couchbaseObject=new CouchBaseOperation();
			couchbaseObject.appendDataInCouchBase("Applicant_"+applicantID2, dataStoreValue);
			Logger.debug("Record is updated in couchbase.");
			
		}catch(JSONException | NullPointerException excp){
			Logger.error("Error  when inserting personal infor of mortgagePage6a record from couchbase.",excp);
		}
		
	}
	
	public PersonalInfo getPersonalInfo(){
	Logger.info("Inside getPersonalInfo method of PersonalInfoDBOperation");
	personalInfo = new PersonalInfo();
	try{
		couchbaseObject =new CouchBaseOperation();
		jsonObject = couchbaseObject.getCouchBaseData(applicantIDCB);
		personalInfo.setAdditionalApplicant(jsonObject.getString("Applicant-additionalApplicant"));
		personalInfo.setMobilePhone(jsonObject.getString("Applicant-mobile"));
		personalInfo.setWorkPhone(jsonObject.getString("Applicant-workPhone"));
		personalInfo.setHomePhone(jsonObject.getString("Applicant-homePhone"));
		personalInfo.setBirthDay(jsonObject.getString("Applicant-birthday"));
		personalInfo.setSocialInsurance(jsonObject.getString("Applicant-insurance"));
		personalInfo.setRelationStatus(jsonObject.getString("Applicant-relationshipStatus"));
		personalInfo.setDependents(jsonObject.getString("Applicant-dependant"));
		personalInfo.setAreUCanadianRes(jsonObject.getString("Applicant-non_Resident"));
		personalInfo.setMovedCanada(jsonObject.getString("Applicant-moved_canada"));
		
	}catch(JSONException | NullPointerException excp){
		Logger.debug("Exception when reading record from couchbase.",excp);
	}
	return personalInfo;
}
	public PersonalInfo getCoAppPersonalInfo(){
		Logger.info("Inside getPersonalInfo method of PersonalInfoDBOperation");
		personalInfo = new PersonalInfo();
		try{
			couchbaseObject =new CouchBaseOperation();
			jsonObject = couchbaseObject.getCouchBaseData(applicantIDCB);
			personalInfo.setAdditionalApplicant(jsonObject.getString("Applicant-additionalApplicant"));
			personalInfo.setCoMobilePhone(jsonObject.getString("CoApplicant-mobile"));
			personalInfo.setCoWorkPhone(jsonObject.getString("CoApplicant-workPhone"));
			personalInfo.setCoHomePhone(jsonObject.getString("CoApplicant-homePhone"));
			personalInfo.setCoBirthDay(jsonObject.getString("CoApplicant-birthday"));
			personalInfo.setCoSocialInsurance(jsonObject.getString("CoApplicant-insurance"));
			personalInfo.setCoRelationStatus(jsonObject.getString("CoApplicant-relationshipStatus"));
			personalInfo.setCoDependents(jsonObject.getString("CoApplicant-dependant"));
			personalInfo.setCoAreUCanadianRes(jsonObject.getString("CoApplicant-newtoCanada"));
			personalInfo.setCoMovedCanada(jsonObject.getString("CoApplicant-whendidcametoCannada"));
			
		}catch(JSONException | NullPointerException excp){
			Logger.debug("Exception when reading record from couchbase.",excp);
		}
		return personalInfo;
	}
}
