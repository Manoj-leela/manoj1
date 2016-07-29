package com.syml.purchaseProposal.util;

import java.io.IOException;

import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.syml.purchaseProposal.couchbase.Comparison;
import com.syml.purchaseProposal.couchbase.CouchBaseOperation;
import com.syml.purchaseProposal.couchbase.MaximumPurchase;
import com.syml.purchaseProposal.couchbase.OrignalDetails;
import com.syml.purchaseProposal.couchbase.RecommendDetails;
import com.syml.purchaseProposal.couchbase.dao.ProposalException;


public class JsonConvertion {
	private static org.slf4j.Logger logger = LoggerFactory.getLogger(CouchBaseOperation.class);

	public static OrignalDetails fromJsonforOriginalDetails(String json) throws ProposalException {
		OrignalDetails recdetails = null;
		try {
			recdetails = new ObjectMapper().readValue(json, OrignalDetails.class);
		} catch (JsonParseException|JsonMappingException  e) {
			
			logger.error("Json Parse Excettion : " + e.getMessage());
			throw new ProposalException("The OriginalDetail mapping and Parsing ",e);
		} catch (IOException e) {
			logger.error("IO  Excettion : " + e.getMessage());
			throw new ProposalException("The OriginalDetail IO Exception Occured ",e);
		}

		return recdetails;
	}

	/*public static Comparison getComparison(String json) throws ProposalException {
		Comparison compairson = null;
		try {
			compairson = new ObjectMapper().readValue(json, Comparison.class);
		} catch (JsonParseException|JsonMappingException e) {
			logger.error("Json Parse Excettion : " + e.getMessage());
			throw new ProposalException("The Comparison mapping and Parsing ",e);
		} catch (IOException|NullPointerException e) {
			logger.error("IO  Excettion : " + e.getMessage());
			throw new ProposalException("The Comparison getting null ",e);
		} 

		return compairson;
	}*/

	public static RecommendDetails fromJsonforRecommendDetails(String json) throws ProposalException {

		RecommendDetails recdetails = null;
		try {
			recdetails = new ObjectMapper().readValue(json, RecommendDetails.class);
		} catch (JsonParseException| JsonMappingException e) {

			logger.info("Json Parse Excettion : " + e.getMessage());
			throw new ProposalException("The RecommendDetails should be parsing properly  ",e);
		}  catch (IOException e) {
			logger.info("Io Exception in RecommmedationDetails  : " + e.getMessage());
			throw new ProposalException("The RecommendDetails  Io Exception occured ",e);
		}

		return recdetails;
	}

	public static MaximumPurchase fromJsontoMaximumPurchase(String json) throws ProposalException {

		MaximumPurchase maxpurchase = null;
		try {

			maxpurchase = new ObjectMapper().readValue(json, MaximumPurchase.class);
		} catch (JsonParseException e) {

			logger.info("Json Parse Excettion : " + e.getMessage());
			throw new ProposalException("Json Parsing in maximumPruchase",e);
		} catch (JsonMappingException e) {
			logger.info("Json Parse Excettion : " + e.getMessage());
			throw new ProposalException("mapping not done properly  in maximumPruchase",e);
		} catch (IOException e) {
			logger.info("Json Parse Excettion : " + e.getMessage());
			throw new ProposalException("IO Exception occured in maximum purchase ",e);
		}

		return maxpurchase;
	}

	public static JSONObject getJsonObject(String json) throws JSONException {
			JSONObject obj = null;
		
			obj = new JSONObject(json);
			logger.debug(">>>>>>>>><<<<<<<<<<<"+obj);
		
			return obj;

	}
	public static String getJsonObject(Object json) throws JSONException, ProposalException {
		String obj = null;
		
		try {
			obj = new ObjectMapper().writeValueAsString(json);
		} catch (JsonProcessingException e) {
			logger.error("error while mapping json"+e.getMessage());
			throw new ProposalException("The  Json Conversion not done properly  ",e);
		}
			
		logger.info("The String value from JsonObject"+obj);
			return obj;

	}
	public static JSONObject getJsonForRecommendDetailsSingle(RecommendDetails rec) throws JSONException {
	
		JSONObject obj = new JSONObject();
		
		obj.put("propertyValue",rec.getPropertyValue());
		obj.put("downpaymentEquity",rec.getDownpaymentEquity());
		obj.put("mortgageAmount",rec.getMortgageAmount());
		obj.put("amortization",rec.getAmortization());
		obj.put("mortgageType",rec.getMortgageType());
		obj.put("mortgageTerm",rec.getMortgageTerm());
		obj.put("paymentAmount",rec.getPaymentAmount());
		
		obj.put("interestRate",rec.getInterestRate());
		obj.put("lender",rec.getLender());
		obj.put("totalMortgage",rec.getTotalMortgage());
		obj.put("productName",rec.getProductName());
		obj.put("insuranceAmount",rec.getInsuranceAmount());
		obj.put("productID",rec.getProductID());
		
		return obj;
		
		
			

	}
}
