package com.syml.mortgageapplication.impl;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.syml.SplitAddress;
import com.syml.couchbase.dao.CouchbaseServiceException;
import com.syml.couchbase.dao.service.CouchBaseService;
import com.syml.couchbasehelper.CouchbaseServiceHelper;
import com.syml.hibernate.dao.PostGressDaoServiceException;
import com.syml.hibernate.service.PostGresDaoService;
import com.syml.mortgageapplication.MortgageApplicationConstants;

import controllers.Address;
import controllers.ApplicantAddressParameter7;
import controllers.CoApplicantAddressParameter7;
import controllers.Opportunity;
import play.Logger;
import play.data.DynamicForm;
import play.mvc.Http.Session;

public class MortgageApplicationPageSevenService {
	
	String subForm = "Mortgage Application 7";
	
	private MortgageApplicationPageService mortAppService = new MortgageApplicationPageService();
	private CouchBaseService couchBaseService= new CouchBaseService();
	private CouchbaseServiceHelper couchbaseServiceHelper = new CouchbaseServiceHelper();
	private PostGresDaoService postGresDaoService = new PostGresDaoService(); 
	private Opportunity opportunity = null;
	private Address address = null;
	private SplitAddress addressSplit = new SplitAddress();
	private Map<String,String> storeFormData = null;
	
	private String formInputAddress ="";
	private String formInputMovedInDate = "";
	private String formInputSumMonth = "";
	private String formInputTotalMonth = "";
	
	public Opportunity updateOpportunityAddresses(DynamicForm dynamicForm,Session session) throws MortgageApplicationPageServiceException{
		Logger.info("Inside (.) updateOpportunityAddresses of MortgageApplicationPageSevenService");
		Map<String,String> readFormData = null;
		List<Address> listOfAddress = new ArrayList<Address>();
		Integer totalNoOfMonth=null;
		try{
		int crm_LeadId = mortAppService.parseLeadId(session);
			
		opportunity = couchbaseServiceHelper.getCouhbaseDataByKey(crm_LeadId);
			
		readFormData = getFirstPriorAddressDataOfApplicant(dynamicForm);
		
		totalNoOfMonth = mortAppService.convertStringToInteger(readFormData.get("totalMonth"));
		
		if (readFormData.get("inputAddress") != null) {
			listOfAddress.add(getAddress(readFormData,opportunity.getApplicants().get(0).getId()));	
		}
		Logger.debug("totalNoOfMonth "+totalNoOfMonth);
		if (totalNoOfMonth != null && totalNoOfMonth < 36) {
			readFormData = getSecPriorAddressDataOfApplicant(dynamicForm);
			totalNoOfMonth = mortAppService.convertStringToInteger(readFormData.get("totalMonth"));
			if(readFormData.get("inputAddress") !=null){
				listOfAddress.add(getAddress(readFormData, opportunity.getApplicants().get(0).getId()));
			}
			Logger.debug("totalNoOfMonth "+totalNoOfMonth);
			if(totalNoOfMonth != null && totalNoOfMonth < 36){
				readFormData = getThirdPriorAddressDataOfApplicant(dynamicForm);
				if(readFormData.get("inputAddress") !=null){
					listOfAddress.add(getAddress(readFormData, opportunity.getApplicants().get(0).getId()));
				}
			}
		}
		
	opportunity.getApplicants().get(0).setListOfAddress(listOfAddress);
	
		listOfAddress = new ArrayList<Address>();
		
		if (opportunity.getIsAdditionalApplicantExist() != null && opportunity.getIsAdditionalApplicantExist().equalsIgnoreCase("yes")) {
			readFormData = getFirstPriorAddressDataOfCoApplicant(dynamicForm);
			totalNoOfMonth = mortAppService.convertStringToInteger(readFormData.get("totalMonth"));
			
			if (readFormData.get("inputAddress") != null) {
				listOfAddress.add(getAddress(readFormData, opportunity.getApplicants().get(1).getId()));	
			}
			Logger.debug("totalNoOfMonth "+totalNoOfMonth);
			if(totalNoOfMonth != null && totalNoOfMonth < 36){
				readFormData = getSecPriorAddressDataOfCoApplicant(dynamicForm);
				totalNoOfMonth = mortAppService.convertStringToInteger(readFormData.get("totalMonth"));
				Logger.debug("totalNoOfMonth "+totalNoOfMonth);
				if(readFormData.get("inputAddress") !=null){
					listOfAddress.add(getAddress(readFormData, opportunity.getApplicants().get(1).getId()));
				}
				Logger.debug("totalNoOfMonth "+totalNoOfMonth);
				if(totalNoOfMonth != null && totalNoOfMonth < 36){
					readFormData = getThirdPriorAddressDataOfCoApplicant(dynamicForm);
					
					if(readFormData.get("inputAddress") !=null){
						listOfAddress.add(getAddress(readFormData, opportunity.getApplicants().get(1).getId()));
					}
				}
			}
			opportunity.getApplicants().get(1).setListOfAddress(listOfAddress);
		}
		opportunity.setPogressStatus(65);
		postGresDaoService.updateApplicantPage7(opportunity);
		couchBaseService.storeDataToCouchbase(MortgageApplicationConstants.MORTGAGE_APPLICATION_COUCHBASE_KEY+crm_LeadId, opportunity);
		Logger.debug("Comming here");
		}catch(MortgageApplicationPageServiceException mortAppException){
			throw new MortgageApplicationPageServiceException("Error when reading crm_LeadId Data from Couchbase / Invalid crm_LeadId ",mortAppException);
		}catch(PostGressDaoServiceException pgException){
			throw new MortgageApplicationPageServiceException(" Error when Updating Applicant Addresses into the PostgressDB ", pgException);
		}catch(CouchbaseServiceException cbException){
			throw new MortgageApplicationPageServiceException("Error when Updating Applicant Addresses into the CouchbaseDB  ",cbException);
		}
		return opportunity;
	}
	
	private Address getAddress(Map<String,String> readFormData,int applicantId){
		Map<String,String> currentAddressSplit = addressSplit.getProperAddress(readFormData.get("inputAddress"));
		
		if(currentAddressSplit != null){
			address = new Address();
			address.setName(currentAddressSplit.get("address1"));
			address.setCity(currentAddressSplit.get("city"));
			address.setProvience(currentAddressSplit.get("Province"));
			address.setPostalCode(currentAddressSplit.get("postalcode"));
			address.setMovedIn(convertString2Date(readFormData.get("movedInDate")));
			address.setApplicant_id(applicantId);
			address.setFullAddress(readFormData.get("inputAddress"));
			Logger.debug("readFormData =============================================================================== "+readFormData);
			address.setMonths(mortAppService.convertStringToInteger(readFormData.get("sumMonth")));
			address.setTotalMonths(mortAppService.convertStringToInteger(readFormData.get("totalMonth")));
		}
		return address;
	}
	
	public Date convertString2Date(String inputDateString){
		DateFormat df2 = new SimpleDateFormat("MM/dd/yyyy");
		Date movedIn1 = null;
		try{
			movedIn1 = df2.parse(inputDateString);
		}catch (ParseException e) {
			Logger.error("Error in parsing string to date");
		}
		return movedIn1;
	}
	
	
	
	/**
	 * Reading Applicant's First Prior Address Details
	 * @param dynamicForm
	 * @return
	 */
	private Map<String,String> getFirstPriorAddressDataOfApplicant(DynamicForm dynamicForm){
		Logger.info("inside (.) getFirstPriorAddressDataOfApplicant of MortgageApplicationPageSevenService ");
		storeFormData = new HashMap<String, String>();
		formInputAddress = dynamicForm.get("currentAddress1");
		formInputMovedInDate = dynamicForm.get("movedIn1");
		formInputSumMonth = dynamicForm.get("currentaddressmonthsum");
		formInputTotalMonth = dynamicForm.get("totalcurrentmonths");
		Logger.debug("================================================================================");
		Logger.debug("currentAddress" + formInputAddress+ "\n input MovedIn1 " + formInputMovedInDate
				 + "\n currentSumMonth" + formInputSumMonth
				+ "\n totalcurrentMonths" + formInputTotalMonth);
		Logger.debug("currentSumMonth>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>"+formInputSumMonth+"<<<<<<<<<<<<<");
		Logger.debug("totalcurrentMonths>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>"+formInputTotalMonth+"<<<<<<<<<<<<<");
		Logger.debug("================================================================================");
		storeFormData.put("inputAddress", formInputAddress);
		storeFormData.put("movedInDate", formInputMovedInDate);
		storeFormData.put("sumMonth", formInputSumMonth);
		storeFormData.put("totalMonth", formInputTotalMonth);
		return storeFormData;
	}
	
	/**
	 * Reading Applicant's 2nd Prior Address Details
	 * @param dynamicForm
	 * @return
	 */
	private Map<String,String> getSecPriorAddressDataOfApplicant(DynamicForm dynamicForm){
		Logger.info("inside (.) getSecPriorAddressDataOfApplicant of MortgageApplicationPageSevenService ");
		storeFormData = new HashMap<String, String>();
		
		formInputAddress = dynamicForm.get("currentAddress2");
		formInputMovedInDate = dynamicForm.get("movedIn2");
		formInputSumMonth = dynamicForm.get("priormonthsum1");
		formInputTotalMonth = dynamicForm.get("totalpriormonths1");
		
		Logger.debug("================================================================================");
		Logger.debug("currentAddress" + formInputAddress+ "\n input MovedIn1 " + formInputMovedInDate
				+ "\n After string-to-date movedIn1 " + formInputSumMonth
				+ "\n currentSumMonth" + formInputTotalMonth);
		Logger.debug("priorSumMonth1>>>>>>>>>>>>>>>>>>>>"+formInputSumMonth+"<<<<<<<<<<<<<<<<<<<<<<<<<");
		Logger.debug("totalpriorcurrentmonths1>>>>>>>>>>>>>>>>>>>>"+formInputTotalMonth+"<<<<<<<<<<<<<<<<<<<<<<<<<");
		Logger.debug("================================================================================");
		storeFormData.put("inputAddress", formInputAddress);
		storeFormData.put("movedInDate", formInputMovedInDate);
		storeFormData.put("sumMonth", formInputSumMonth);
		storeFormData.put("totalMonth", formInputTotalMonth);
		
		return storeFormData;
	}
	
	/**
	 * Reading Applicant's 3rd Prior Address Details
	 * @param dynamicForm
	 * @return
	 */
	private Map<String,String> getThirdPriorAddressDataOfApplicant(DynamicForm dynamicForm){
		Logger.info("inside (.) getThirdPriorAddressDataOfApplicant of MortgageApplicationPageSevenService ");
		storeFormData = new HashMap<String, String>();
		
		formInputAddress = dynamicForm.get("currentAddress3");
		formInputMovedInDate = dynamicForm.get("movedIn3");
		formInputSumMonth = dynamicForm.get("priormonthsum2");
		formInputTotalMonth = dynamicForm.get("totalpriormonths2");
		Logger.debug("================================================================================");
		Logger.debug("priorAddress2" + formInputAddress+ "\n inputMovedIn3 " + formInputMovedInDate+ "\n priorSumMonth2" + formInputSumMonth+"\n formInputTotalMonth "+formInputTotalMonth);
		Logger.debug("priormonthsum2>>>>>>>>>>>>>>>>>>>>>>>>>>>>>"+formInputSumMonth+"<<<<<<<<<<<<<<<<<<<<<<<<<<<");
		Logger.debug("totalpriormonths2>>>>>>>>>>>>>>>>>>>>>>>>>>>>>"+formInputTotalMonth+"<<<<<<<<<<<<<<<<<<<<<<<<<<<");
		Logger.debug("================================================================================");
		
		storeFormData.put("inputAddress", formInputAddress);
		storeFormData.put("movedInDate", formInputMovedInDate);
		storeFormData.put("sumMonth", formInputSumMonth);
		storeFormData.put("totalMonth", formInputTotalMonth);
		return storeFormData;
	}
	
	/**
	 * Reading CoCoApplicant's 1st Prior Address Details
	 * @param dynamicForm
	 * @return
	 */
	private Map<String,String> getFirstPriorAddressDataOfCoApplicant(DynamicForm dynamicForm){
		Logger.info("inside (.) getFirstPriorAddressDataOfCoApplicant of MortgageApplicationPageSevenService ");
		storeFormData = new HashMap<String, String>();
		
		formInputAddress = dynamicForm.get("CoCurrentAddress1");
		formInputMovedInDate = dynamicForm.get("CoMovedIn1");
		formInputSumMonth = dynamicForm.get("coAppcurrentaddressmonthsum");
		formInputTotalMonth = dynamicForm.get("coAppTotalcurrentMonths");
		Logger.debug("================================================================================");
		Logger.debug("coAppcurrentAddress" + formInputAddress+ "\n coAppInputMovedIn1 " + 
				formInputMovedInDate+ "\n coAppCurrentSumMonth" + formInputSumMonth+"\n coAppTotalcurrentMonths "+formInputTotalMonth);
		Logger.debug("co first summonth>>>>>>>>>>>>>>>>>>>>>>>>>>>>>"+formInputSumMonth+"<<<<<<<<<<<<<<<<<<<<<<<<<<<");
		Logger.debug("co first totalmonth>>>>>>>>>>>>>>>>>>>>>>>>>>>>>"+formInputSumMonth+"<<<<<<<<<<<<<<<<<<<<<<<<<<<");
		Logger.debug("================================================================================");
		storeFormData.put("inputAddress", formInputAddress);
		storeFormData.put("movedInDate", formInputMovedInDate);
		storeFormData.put("sumMonth", formInputSumMonth);
		storeFormData.put("totalMonth", formInputTotalMonth);
		return storeFormData;
	}
	
	/**
	 * Reading CoCoApplicant's 2nd Prior Address Details
	 * @param dynamicForm
	 * @return
	 */
	private Map<String,String> getSecPriorAddressDataOfCoApplicant(DynamicForm dynamicForm){
		Logger.info("inside (.) getSecPriorAddressDataOfCoApplicant of MortgageApplicationPageSevenService ");
		storeFormData = new HashMap<String, String>();
		
		formInputAddress = dynamicForm.get("CoCurrentAddress2");
		formInputMovedInDate = dynamicForm.get("CoMovedIn2");
		formInputSumMonth = dynamicForm.get("coAppPriorSumMonth1");
		formInputTotalMonth = dynamicForm.get("coAppTotalpriorcurrentmonths1");
		
		Logger.debug("================================================================================");
		Logger.debug("coAppPriorAddress1" + formInputAddress+ "\n coAppInputMovedIn2 "  
				+formInputMovedInDate+ "\n coAppPriorSumMonth1" + formInputSumMonth
				+"\n coAppTotalpriorcurrentmonths1 "+formInputTotalMonth);
		Logger.debug("co 2nd summonth>>>>>>>>>>>>>>>>>>>>>>>>>>>>>"+formInputSumMonth+"<<<<<<<<<<<<<<<<<<<<<<<<<<<");
		Logger.debug("co 2nd totalmonth>>>>>>>>>>>>>>>>>>>>>>>>>>>>>"+formInputSumMonth+"<<<<<<<<<<<<<<<<<<<<<<<<<<<");
		Logger.debug("================================================================================");
		storeFormData.put("inputAddress", formInputAddress);
		storeFormData.put("movedInDate", formInputMovedInDate);
		storeFormData.put("sumMonth", formInputSumMonth);
		storeFormData.put("totalMonth", formInputTotalMonth);
		return storeFormData;
	}
	
	/**
	 * Reading CoCoApplicant's 3rd Prior Address Details
	 * @param dynamicForm
	 * @return
	 */
	private Map<String,String> getThirdPriorAddressDataOfCoApplicant(DynamicForm dynamicForm){
		Logger.info("inside (.) getThirdPriorAddressDataOfCoApplicant of MortgageApplicationPageSevenService ");
		storeFormData = new HashMap<String, String>();
		
		formInputAddress = dynamicForm.get("CoCurrentAddress4");
		formInputMovedInDate = dynamicForm.get("CoMovedIn3");
		formInputSumMonth = dynamicForm.get("coApppriorSumMonth2");
		formInputTotalMonth = dynamicForm.get("coApptotalpriorcurrentmonths2");
		
		Logger.debug("================================================================================");
		Logger.debug("coApppriorAddress2" + formInputAddress+ "\n coAppInputMovedIn3 "  
				+formInputMovedInDate+ "\n coApppriorSumMonth2" + formInputSumMonth+
				"\n formInputTotalMonth "+formInputTotalMonth);
		Logger.debug("co 3rd summonth>>>>>>>>>>>>>>>>>>>>>>>>>>>>>"+formInputSumMonth+"<<<<<<<<<<<<<<<<<<<<<<<<<<<");
		Logger.debug("co 3rd totalmonth>>>>>>>>>>>>>>>>>>>>>>>>>>>>>"+formInputTotalMonth+"<<<<<<<<<<<<<<<<<<<<<<<<<<<");
		Logger.debug("================================================================================");
		storeFormData.put("inputAddress", formInputAddress);
		storeFormData.put("movedInDate", formInputMovedInDate);
		storeFormData.put("sumMonth", formInputSumMonth);
		storeFormData.put("totalMonth", formInputTotalMonth);
		return storeFormData;
	}
	
	/* The Below written code for backbutton functionality */
	// This is not a proper or standard coding, need to devlope code, because of Deployement we ignore 
	public ApplicantAddressParameter7 getApplicantAddresses(Opportunity opportunity){
		List<Address> listOfAddressOfApplicant = opportunity.getApplicants().get(0).getListOfAddress();
		ApplicantAddressParameter7 appAddParam = new ApplicantAddressParameter7();
		appAddParam.setApplicantName(opportunity.getApplicants().get(0).getApplicant_name());
		appAddParam.setApplicantCurrentAddress(listOfAddressOfApplicant.get(0).getFullAddress());
		appAddParam.setApplicantMovedIn1(listOfAddressOfApplicant.get(0).getMovedIn()+"");
		appAddParam.setApplicantCurrentSumMonth(listOfAddressOfApplicant.get(0).getMonths()+"");
		appAddParam.setApplicantTotalcurrentMonths(listOfAddressOfApplicant.get(0).getTotalMonths()+"");
		
		if(listOfAddressOfApplicant.get(0).getTotalMonths() < 36){
		 
		appAddParam.setApplicantPriorAddress1(listOfAddressOfApplicant.get(1).getFullAddress());
		appAddParam.setApplicantMovedIn2(listOfAddressOfApplicant.get(1).getMovedIn()+"");
		appAddParam.setApplicantPriorSumMonth1(listOfAddressOfApplicant.get(1).getMonths()+"");
		appAddParam.setApplicantTotalpriorcurrentmonths1(listOfAddressOfApplicant.get(1).getTotalMonths()+"");
		if(listOfAddressOfApplicant.get(1).getTotalMonths() < 36){
			appAddParam.setApplicantPriorAddress2(listOfAddressOfApplicant.get(2).getFullAddress());
			appAddParam.setApplicantMovedIn3(listOfAddressOfApplicant.get(2).getMovedIn()+"");
			appAddParam.setApplicantPriorSumMonth2(listOfAddressOfApplicant.get(2).getMonths()+"");
			appAddParam.setApplicantTotalpriorcurrentmonths2(listOfAddressOfApplicant.get(2).getTotalMonths()+"");
		}
		}
		 Logger.debug("back page address -->"+appAddParam.getApplicantPriorAddress1());
		return appAddParam;
	}
	
	public CoApplicantAddressParameter7 getCoApplicantAddressParameter7(Opportunity opportunity){
		List<Address> listOfAddressOfApplicant = opportunity.getApplicants().get(1).getListOfAddress();
		
		CoApplicantAddressParameter7 coAppAddParam = new CoApplicantAddressParameter7();
		coAppAddParam.setCoApplicantName(opportunity.getApplicants().get(1).getApplicant_name());
		coAppAddParam.setCoAppcurrentAddress(listOfAddressOfApplicant.get(0).getFullAddress());
		coAppAddParam.setCoAppmovedIn1(listOfAddressOfApplicant.get(0).getMovedIn()+"");
		coAppAddParam.setCoAppCurrentSumMonth(listOfAddressOfApplicant.get(0).getMonths()+"");
		coAppAddParam.setCoAppTotalcurrentMonths(listOfAddressOfApplicant.get(0).getTotalMonths()+"");
		
		if(listOfAddressOfApplicant.get(0).getTotalMonths() < 36){
		 
			coAppAddParam.setCoAppPriorAddress1(listOfAddressOfApplicant.get(1).getFullAddress());
			coAppAddParam.setCoMovedIn2(listOfAddressOfApplicant.get(1).getMovedIn()+"");
			coAppAddParam.setCoPriorSumMonth1(listOfAddressOfApplicant.get(1).getMonths()+"");
			coAppAddParam.setCoTotalpriorcurrentmonths1(listOfAddressOfApplicant.get(1).getTotalMonths()+"");
			
		if(listOfAddressOfApplicant.get(1).getTotalMonths() < 36){
			coAppAddParam.setCoApppriorAddress2(listOfAddressOfApplicant.get(2).getFullAddress());
			coAppAddParam.setCoMovedIn3(listOfAddressOfApplicant.get(2).getMovedIn()+"");
			coAppAddParam.setCoApppriorSumMonth2(listOfAddressOfApplicant.get(2).getMonths()+"");
			coAppAddParam.setCoApptotalpriorcurrentmonths2(listOfAddressOfApplicant.get(2).getTotalMonths()+"");
		}
		}
		 
		return coAppAddParam;
	}
	
	
}