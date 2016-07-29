package helper;

import java.util.HashMap;

import openerp.CreateApplicant;

import org.json.JSONException;
import org.json.JSONObject;

import play.Logger;
import controllers.ApplicantAddressParameter7;
import controllers.CoApplicantAddressParameter7;
import couchbase.CouchBaseOperation;

public class AddressDBOperation {
	String applicantID ;

	public AddressDBOperation(String applicantID) {
		// TODO Auto-generated constructor stub
		this.applicantID=applicantID;
	}
	
	CouchBaseOperation  couchbaseObject=null;
	String applicantIDCB="Applicant_"+applicantID;
	JSONObject jsonObject=null;
	HashMap dataStoreValue = null;
	CreateApplicant applicantCreate = null;
	CreateApplicant updateApplicant=null;
	ApplicantAddressParameter7 appAddressDet = null;
	FormatDateString formatedDateString=null;
	CoApplicantAddressParameter7 coAppAddressDetails=null;
	public ApplicantAddressParameter7 getApplAddressDetails(){
		Logger.info("Inside getApplAddressDetails method of PersonalInfoDBOperation ");
		try{
			formatedDateString = new FormatDateString();
			appAddressDet = new ApplicantAddressParameter7();
			couchbaseObject =new CouchBaseOperation();
			jsonObject = couchbaseObject.getCouchBaseData(applicantIDCB);
			appAddressDet.setApplicantCurrentAddress(jsonObject.getString("Applicant-CurrentAddress"));
			String returnedDate=jsonObject.getString("Applicant-movedIn1");
			appAddressDet.setApplicantMovedIn1(formatedDateString.getFormattedDateString(returnedDate));
			String totalMonth1=jsonObject.getString("Applicant-totalcurrentMonths");
			appAddressDet.setApplicantTotalcurrentMonths(totalMonth1);
			int totalMonthInt=0;
			if(totalMonth1 != "" && totalMonth1 != null)
				totalMonthInt=Integer.parseInt(totalMonth1);
			
			if(totalMonthInt<36){
				appAddressDet.setApplicantPriorAddress1(jsonObject.getString("Applicant-priorAddress1"));
				String returnedDate2 = jsonObject.getString("Applicant-movedIn2");
				appAddressDet.setApplicantMovedIn2(formatedDateString.getFormattedDateString(returnedDate2));
				String totalMonth2=jsonObject.getString("Applicant-totalpriorcurrentmonths1");
				appAddressDet.setApplicantTotalpriorcurrentmonths1(totalMonth2);
				
				int totalMonthInt2=0;
				if(totalMonth2 !="" && totalMonth2 != null)
					totalMonthInt2=Integer.parseInt(totalMonth2);
				if(totalMonthInt2<36){
					appAddressDet.setApplicantPriorAddress2(jsonObject.getString("Applicant-priorAddress2"));
					String returnedDate3=jsonObject.getString("Applicant-movedIn3");
					appAddressDet.setApplicantMovedIn3(formatedDateString.getFormattedDateString(returnedDate3));
					String totalMonth3=jsonObject.getString("Applicant-totalpriorcurrentmonths2");
					appAddressDet.setApplicantTotalpriorcurrentmonths2(totalMonth3);
				}
			}
			
		}catch(JSONException | NullPointerException excp){
			Logger.debug("Exception when reading record from couchbase.",excp);
		}
		
		return appAddressDet;
	}
	public CoApplicantAddressParameter7 getCoApplAddressDetails(){

		Logger.info("Inside getCoApplAddressDetails method of PersonalInfoDBOperation ");
		try{
			coAppAddressDetails=new CoApplicantAddressParameter7(); 
			formatedDateString = new FormatDateString();
			couchbaseObject =new CouchBaseOperation();
			jsonObject = couchbaseObject.getCouchBaseData(applicantIDCB);
			
			coAppAddressDetails.setCoAppcurrentAddress(jsonObject.getString("CoApplicant-currentAddress"));
			String returnedDate=jsonObject.getString("CoApplicant-AppmovedIn1");
			coAppAddressDetails.setCoAppmovedIn1(formatedDateString.getFormattedDateString(returnedDate));
			String totalMonth11=jsonObject.getString("CoApplicant-totalcurrentMonths");
			coAppAddressDetails.setCoAppTotalcurrentMonths(totalMonth11);
			int totalMonthInt=0;
			if(totalMonth11 != "" && totalMonth11 != null)
				totalMonthInt=Integer.parseInt(totalMonth11);
			
			if(totalMonthInt<36){
				coAppAddressDetails.setCoAppPriorAddress1(jsonObject.getString("CoApplicant-priorAddress1"));
				String returnedDate2 = jsonObject.getString("CoApplicant-movedIn1");
				coAppAddressDetails.setCoMovedIn2(formatedDateString.getFormattedDateString(returnedDate2));
				String totalMonth2=jsonObject.getString("CoApplicant-totalpriorcurrentmonths1");
				coAppAddressDetails.setCoTotalpriorcurrentmonths1(totalMonth2);
				
				int totalMonthInt2=0;
				if(totalMonth2 !="" && totalMonth2 != null)
					totalMonthInt2=Integer.parseInt(totalMonth2);
				if(totalMonthInt2<36){
					coAppAddressDetails.setCoApppriorAddress2(jsonObject.getString("CoApplicant-priorAddress2"));
					String returnedDate3=jsonObject.getString("CoApplicant-movedIn3");
					coAppAddressDetails.setCoMovedIn3(formatedDateString.getFormattedDateString(returnedDate3));
					String totalMonth3=jsonObject.getString("CoApplicant-totalpriorcurrentmonths2");
					coAppAddressDetails.setCoApptotalpriorcurrentmonths2(totalMonth3);
				}
			}
			
		}catch(JSONException | NullPointerException excp){
			Logger.debug("Exception when reading record from couchbase.",excp);
		}
		
		return coAppAddressDetails;
	}
}
