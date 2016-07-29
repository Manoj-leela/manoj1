package war.couchbase;

import java.io.IOException;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Set;

import org.codehaus.jettison.json.JSONArray;
import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;
import org.slf4j.LoggerFactory;

import com.couchbase.client.CouchbaseClient;
import com.couchbase.client.java.document.json.JsonObject;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import war.couchbase.dao.CouchbaseDaoServiceException;
import war.couchbase.dao.service.CouchBaseService;

public class CouchbaseData {

	private static org.slf4j.Logger logger = LoggerFactory.getLogger(CouchbaseData.class);

	CouchbaseClient client1 = null;
	CouchBaseOperation couchbas = null;

	CouchBaseService service = null;

	public JsonObject getDataCouchbase(String opp) throws CouchbaseDaoServiceException {

		service = new CouchBaseService();

		JsonObject expiryDate = null;

		try {
			expiryDate = service.getCouhbaseDataByKey("ExpiryDateTime_" + opp);
			logger.debug("expiryData " + expiryDate);

		} catch (NullPointerException e) {
			logger.error("bcoz expiry date is -->" + e.getMessage());

		}

		return expiryDate;

	}

	public void UpdateDataCouchbase(String opp, String date) {
		service = new CouchBaseService();
		

			try {
				service.storeDataToCouchbase("ExpiryDateTime_", date);
			} catch (CouchbaseDaoServiceException e) {
			logger.error("error while storing date : "+e.getMessage());
				
			}
			logger.debug("update expirydatetime" + "ExpiryDateTime_"+date);


	}

	public UwAppAllAlgo getDataFromCouchbase(String opp) {
		
		UwAppAllAlgo algo = null;

		 service = new CouchBaseService();

		ObjectMapper mapper = new ObjectMapper();

		String cbData;
		try {
			cbData = service.getCouhbaseDataByKey("uwapps2couchbase_allProductAlgoJSON_"+opp).toString();
			if (cbData != null) {
				System.out.println("inside conditon {} ");

				try {
					
					
					algo = mapper.readValue(cbData, UwAppAllAlgo.class);
				} catch (JsonParseException e) {

					logger.error("error while parsing UwAppAllAlgo"+e.getMessage());
				} catch (JsonMappingException e) {
					logger.error("error while mapping UwAppAllAlgo"+e.getMessage());
					
				} catch (IOException e) {

					logger.error("error while IO  UwAppAllAlgo"+e.getMessage());
				}

			}
		} catch (CouchbaseDaoServiceException e) {
			logger.error("CouchbaseDaoServiceException "+e.getMessage());
		}
		

		return algo;

	}

	public void insertDataInCouchbase(OrignalDetails orignalDet, String oppurtunityId, String ipaddress) {

		JSONArray jsonArray = new JSONArray();
		// JSONObject jsonObject = new JSONObject();

		// ObjectMapper mapper = null;

		UwAppAllAlgo algo = null;
		String productName = null;
		// Recommendation recommendation = new Recommendation();

		algo = getDataFromCouchbase(oppurtunityId);
		String opportunityName = algo.getOpportunityName();

		Set<Recommendation> recommendation = algo.getRecommendations();
		for (Recommendation rec : recommendation) {
			if (rec.getProductID() == orignalDet.getProductID()) {
				productName = rec.getProduct();
			}
		}

		jsonArray = new JSONArray();

		Set<ApplicantsNames> appilcantname = algo.getApplicantsNames();
		for (ApplicantsNames applicant : appilcantname) {
			JSONObject jsn = new JSONObject();
			try {
				jsn.put("firstname", applicant.getFirstName());
				jsn.put("lastname", applicant.getLastName());
				jsonArray.put(jsn);

			} catch (JSONException e) {

				logger.error("error while json getting "+e.getMessage());
			}

			// logger.debug("applicant name : "+applicant.getFirstName()+" last
			// : "+applicant.getLastName());
		}

		// algo.setApplicantsNames(appilcantname);

		service = new  CouchBaseService();

		JSONObject content = new JSONObject();

		Date today = Calendar.getInstance().getTime();
		DateFormat df = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss");
		String create_date = df.format(today);

		try {
			logger.debug("@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@ comming @@@@@@@@@@@@@@@@@@@@@@@@@@@@@@");
			// orignalDet.setId(UUID.randomUUID().toString());
			content.put("Type", "SelectedRec");
			content.put("opportunitID", oppurtunityId);
			content.put("OpportunityName", opportunityName);
			content.put("CreatedDateTime", create_date);
			content.put("ModifiedDateTime", create_date);
			content.put("ApplicantsNames", jsonArray.toString());

			content.put("propertyValue", orignalDet.getPropertyValue());

			content.put("Downpayment", orignalDet.getDownpaymentEquity());
			content.put("LendingGoal", "");
			content.put("mortgageAmount", orignalDet.getMortgageAmount());

			content.put("Recommendation", "");
			content.put("selectedProductID", orignalDet.getProductID());

			content.put("selectedProductName", productName);

			content.put("selectedProductLender", orignalDet.getLender());

			content.put("selectedProductRate", orignalDet.getInterestRate());
			content.put("selectedProductPayment", orignalDet.getPaymentAmount());
			content.put("selectedProductType", orignalDet.getMortgageType());
			content.put("selectedProductTerm", orignalDet.getMortgageTerm());
			content.put("SignatureIP", ipaddress);
			content.put("SignatureDateTime", "");
			content.put("Signature", "");

			logger.debug("Please Select this key for test : " + "SelectRec_"+oppurtunityId);
			try {
				service.storeDataToCouchbase("SelectRec_"+oppurtunityId, content);
			} catch (CouchbaseDaoServiceException e) {
				logger.error("error while stroing SelectRec_"+e.getMessage());
			}
			

		} catch (JSONException e) {
			logger.error("json exception ");
		}

	}

	public void insertDataInCouchbase(RecommendDetails recommendDet, String oppurtunityId, String ipaddress) {

		UwAppAllAlgo algo = null;
		service = new CouchBaseService();
		
		JSONArray jsonArray = new JSONArray();
		// JSONObject jsonObject = new JSONObject();

		// ObjectMapper mapper = null;

		Date today = Calendar.getInstance().getTime();
		DateFormat df = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss");
		String create_date = df.format(today);

		String productName = null;
		algo = getDataFromCouchbase(oppurtunityId);
		String opportunityName = algo.getOpportunityName();
		Set<Recommendation> recommendation = algo.getRecommendations();
		for (Recommendation rec : recommendation) {
			if (rec.getProductID() == recommendDet.getProductID()) {
				productName = rec.getProduct();
			}
		}

		Set<ApplicantsNames> appilcantname = algo.getApplicantsNames();
		for (ApplicantsNames applicant : appilcantname) {
			JSONObject jsn = new JSONObject();
			try {
				jsn.put("firstname", applicant.getFirstName());
				jsn.put("lastname", applicant.getLastName());
				jsonArray.put(jsn);

			} catch (JSONException e) {

				e.printStackTrace();
			}

			JSONObject content = new JSONObject();
			logger.debug("@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@ comming not coming @@@@@@@@@@@@@@@@@@@@@@@@@@@@@@");
			try {

				// recommendDet.setId(UUID.randomUUID().toString());
				content.put("Type", "SelectedRec");
				content.put("opportunitID", oppurtunityId);
				content.put("OpportunityName", opportunityName);
				content.put("CreatedDateTime", create_date);
				content.put("ModifiedDateTime", create_date);

				content.put("ApplicantsNames", jsonArray.toString());

				content.put("propertyValue", recommendDet.getPropertyValue());

				content.put("Downpayment", recommendDet.getDownpaymentEquity());
				content.put("LendingGoal", "");
				content.put("MortgageAmount", recommendDet.getMortgageAmount());
				content.put("Recommendation", "");
				content.put("selectedProductID", recommendDet.getProductID());
				content.put("selectedProductName", productName);

				content.put("selectedProductLender", recommendDet.getLender());

				content.put("selectedProductRate", recommendDet.getInterestRate());
				content.put("selectedProductPayment", recommendDet.getPaymentAmount());
				content.put("selectedProductType", recommendDet.getMortgageType());
				content.put("selectedProductTerm", recommendDet.getMortgageTerm());
				content.put("SignatureIP", ipaddress);
				content.put("SignatureDateTime", "");
				content.put("Signature", "");

				logger.debug("Please Select this key for test : " + "SelectRec_" + oppurtunityId);
				
					service.storeDataToCouchbase("SelectRec_" + oppurtunityId, content);
				} catch (CouchbaseDaoServiceException e) {
					logger.error("errr while in storing in couchbase");
				
				

			} catch (JSONException e) {
				logger.error("error has occured in json reading"+e.getMessage());
			}

		}
	}

	public void insertDataInCouchbase(Recommendation recommendation, UwAppAllAlgo algo, String ipaddress,
			String propertyvalue, String downpayment) {

		logger.debug("iside insertDataCoucbabse" + recommendation.toString());

		
		service = new CouchBaseService();
		JSONObject content = new JSONObject();
		JSONArray jsonArray = new JSONArray();

		Date today = Calendar.getInstance().getTime();
		DateFormat df = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss");
		String create_date = df.format(today);
		
		String opportunityName = algo.getOpportunityName();

		JSONArray jsarray = new JSONArray();
		JSONObject jsob = new JSONObject();

		String typeOfMorgage = "";
		// Recommendation rec = null;
		int type = 0;

		try {
			type = Integer.parseInt(recommendation.getMortgageType());
		} catch (NumberFormatException e) {
			logger.error("number format exception:"+e.getMessage());
		}
		switch (type) {
		case 1:
			typeOfMorgage = "LOC";
			break;
		case 2:
			typeOfMorgage = "Variable";
			break;
		case 3:
			typeOfMorgage = "Fixed";
			break;
		case 4:
			typeOfMorgage = "Cashback";
			break;
		case 5:
			typeOfMorgage = "Combined";
			break;
		case 6:
			typeOfMorgage = "Split";
			break;

		default:
			break;
		}

		Set<ApplicantsNames> appilcantname = algo.getApplicantsNames();
		for (ApplicantsNames applicant : appilcantname) {
			JSONObject jsn = new JSONObject();
			try {
				jsn.put("firstname", applicant.getFirstName());
				jsn.put("lastname", applicant.getLastName());
				jsonArray.put(jsn);

			} catch (JSONException e) {
				logger.error("json exception:"+e.getMessage());
				
			}

			try {

				content.put("Type", "SelectedRec");
				content.put("opportunitID", algo.getOpportunityID());
				content.put("OpportunityName", opportunityName);
				content.put("CreatedDateTime", create_date);
				content.put("ModifiedDateTime", create_date);

				content.put("ApplicantsNames", jsonArray);

				content.put("propertyValue", propertyvalue);

				content.put("Downpayment", downpayment);
				content.put("LendingGoal", "");
				content.put("MortgageAmount", recommendation.getMortgageAmount());

				jsob.put("productType", "Single");
				jsob.put("selectedProductID", recommendation.getProductID());
				jsob.put("selectedProductName", recommendation.getProduct());

				jsob.put("maximumAmortization", recommendation.getMaximumAmortization());
				jsob.put("position", recommendation.getPosition());
				jsob.put("cashbackPercent", recommendation.getCashbackPercent());

				jsob.put("selectedProductLender", recommendation.getLender());

				jsob.put("selectedProductRate", recommendation.getInterestRate());
				jsob.put("selectedProductPayment", recommendation.getPayment());

				jsob.put("selectedProductType", typeOfMorgage);
				jsob.put("selectedProductTerm", recommendation.getTerm());

				jsarray.put(jsob);
				content.put("Recommendations", jsarray);
				// content.put("SignatureIP",ipaddress);
				content.put("SignatureDateTime", "");
				content.put("Signature", "");

				JsonObject jsonObject=JsonObject.fromJson(content.toString());
				logger.debug("Please Select this key for test : " + "SelectRec_"+algo.getOpportunityID());
				service.storeDataToCouchbase("SelectRec_"+algo.getOpportunityID(),jsonObject);
				

			} catch (JSONException e) {
				logger.error("Json Exeption "+e.getMessage());
			}catch (CouchbaseDaoServiceException e) {
				logger.error("error while storing  in couchbase :"+e.getMessage());
			}

		}
	}

	public void insertDataInCouchbase(CombinedRecommendation recommendation, String oppurtunityId, String ipaddress) {
logger.debug("inside CombinedRecommendation");
		UwAppAllAlgo algo = null;
		service = new CouchBaseService();
		JSONObject content = new JSONObject();
		JSONArray jsonArray = new JSONArray();

		Date today = Calendar.getInstance().getTime();
		DateFormat df = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss");
		String create_date = df.format(today);
		algo = getDataFromCouchbase(oppurtunityId);
		String opportunityName = algo.getOpportunityName();

		JSONArray jsarray = new JSONArray();
		JSONObject jsob = new JSONObject();

		Set<ApplicantsNames> appilcantname = algo.getApplicantsNames();
		for (ApplicantsNames applicant : appilcantname) {
			JSONObject jsn = new JSONObject();
			try {
				jsn.put("firstname", applicant.getFirstName());
				jsn.put("lastname", applicant.getLastName());
				jsonArray.put(jsn);

			} catch (JSONException e) {

				logger.error("error while json storing "+e.getMessage());
			}

			try {

				content.put("Type", "SelectedRec");
				content.put("opportunitID", oppurtunityId);
				content.put("OpportunityName", opportunityName);
				content.put("CreatedDateTime", create_date);
				content.put("ModifiedDateTime", create_date);

				content.put("ApplicantsNames", jsonArray);

				content.put("LendingGoal", "");

				jsob.put("productType", "Combine");

				jsob.put("selectedProductID", recommendation.getBaseProductID());
				jsob.put("selectedAdditionalProductID", recommendation.getAdditionalProductID());
				jsob.put("selectedBaseProductName", recommendation.getBaseProduct());
				jsob.put("selectedAdditionalProductName", recommendation.getAdditionalProduct());

				jsob.put("selectedBaseMortgageType", recommendation.getBaseMortgageType());
				jsob.put("selectedAdditionalMortgageType", recommendation.getAdditionalMortgageType());

				jsob.put("selectedBaseProductTerm", recommendation.getBaseTerm());
				jsob.put("selectedAdditionalProductTerm", recommendation.getAdditionalTerm());

				jsob.put("selectedBaseInterestRate", recommendation.getBaseInterestRate());
				jsob.put("selectedAdditionalInterestRate", recommendation.getAdditionalInterestRate());

				jsob.put("selectedBaseMortageAmount", recommendation.getBaseMortgageAmount());
				jsob.put("selectedAdditionalMortageAmount", recommendation.getAdditionalMortgageAmount());

				jsob.put("totalMortgageAmount", recommendation.getTotalMortgageAmount());
				jsob.put("selectedProductLender", recommendation.getBaseLender());

				jsob.put("amortization", recommendation.getBaseAmortization());

				jsob.put("totalMonthlyPayment", recommendation.getTotalPayment());

				jsarray.put(jsob);
				content.put("Recommendations", jsarray);
				content.put("SignatureIP", ipaddress);
				content.put("SignatureDateTime", "");
				content.put("Signature", "");

				logger.info("Please Select this key for test : " + "SelectRec_" + oppurtunityId);
				service.storeDataToCouchbase("SelectRec_" + oppurtunityId, content);
				client1.set("SelectRec_" + oppurtunityId, content.toString());

			} catch (JSONException e) {
				logger.error("error while storing json "+e.getMessage());
			}catch(CouchbaseDaoServiceException e){
				logger.error("error while storing in couchbase"+e.getMessage());
			}

		}

	}

	public static void main(String[] args) {
		
		  /*CouchbaseData data = new CouchbaseData(); UwAppAllAlgo value =
		  data.getDataFromCouchbase("4406");
		  System.out.println(value.getAddress());
		 */

	}

}
