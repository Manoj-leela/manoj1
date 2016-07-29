package com.syml.proposalRefinance.util;

import java.io.IOException;

import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.syml.proposalRefinance.couchbase.Comparison;
import com.syml.proposalRefinance.couchbase.CouchBaseOperation;
import com.syml.proposalRefinance.couchbase.MaximumPurchase;
import com.syml.proposalRefinance.couchbase.OrignalDetails;
import com.syml.proposalRefinance.couchbase.RecommendDetails;
import com.syml.proposalRefinance.couchbase.dao.ProposalException;


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
		}

		return recdetails;
	}

	public static Comparison getComparison(String json) throws ProposalException {
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
	}

	public static RecommendDetails fromJsonforRecommendDetails(String json) throws ProposalException {

		RecommendDetails recdetails = null;
		try {
			recdetails = new ObjectMapper().readValue(json, RecommendDetails.class);
		} catch (JsonParseException| JsonMappingException e) {

			logger.info("Json Parse Excettion : " + e.getMessage());
			throw new ProposalException("The RecommendDetails getting parse Exception  ",e);
		}  catch (IOException e) {
			logger.info("Json Parse Excettion : " + e.getMessage());
		}

		return recdetails;
	}

	public static MaximumPurchase fromJsontoMaximumPurchase(String json) {

		MaximumPurchase maxpurchase = null;
		try {

			maxpurchase = new ObjectMapper().readValue(json, MaximumPurchase.class);
		} catch (JsonParseException e) {

			logger.info("Json Parse Excettion : " + e.getMessage());
		} catch (JsonMappingException e) {
			logger.info("Json Parse Excettion : " + e.getMessage());
		} catch (IOException e) {
			logger.info("Json Parse Excettion : " + e.getMessage());
		}

		return maxpurchase;
	}

	public static JSONObject getJsonObject(String json) throws JSONException {
		JSONObject obj = null;
		
			obj = new JSONObject(json);
			logger.debug(">>>>>>>>><<<<<<<<<<<"+obj);
		
			return obj;

	}
	public static String getJsonObject(Object json) throws JSONException {
		String obj = null;
		
		try {
			obj = new ObjectMapper().writeValueAsString(json);
		} catch (JsonProcessingException e) {
			logger.error("error while mapping json"+e.getMessage());
		}
			
		
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
