package war.util;

import java.io.IOException;


import org.codehaus.jettison.json.JSONObject;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import war.couchbase.*;

public class JsonConvertion{
	private static org.slf4j.Logger logger = LoggerFactory.getLogger(CouchBaseOperation.class);
	
	public static  OrignalDetails fromJsonforOriginalDetails(String json) {
		OrignalDetails recdetails=null;
		try {
			recdetails = new ObjectMapper().readValue(json, OrignalDetails.class);
		} catch (JsonParseException e) {
			logger.info("Json Parse Excettion : "+e.getMessage());
		} catch (JsonMappingException e) {
			logger.info("Json Mapping  Excettion : "+e.getMessage());
		} catch (IOException e) {
			logger.info("IO  Excettion : "+e.getMessage());
		}
		

		return recdetails;
}
	public static  Comparison getComparison(String json) {
		Comparison compairson=null;
		try {
			compairson = new ObjectMapper().readValue(json, Comparison.class);
		} catch (JsonParseException e) {
			logger.error("Json Parse Excettion : "+e.getMessage());
		} catch (JsonMappingException e) {
			logger.error("Json Mapping  Excettion : "+e.getMessage());
		} catch (IOException e) {
			logger.error("IO  Excettion : "+e.getMessage());
		}catch(NullPointerException e){
			logger.error("null pointer exception",e.getMessage());
		}
		

		return compairson;
}
	
	public static   RecommendDetails fromJsonforRecommendDetails(String json) 
    {
		
		RecommendDetails recdetails = null;
		try {
			recdetails = new ObjectMapper().readValue(json, RecommendDetails.class);
		} catch (JsonParseException e) {
			
			logger.info("Json Parse Excettion : "+e.getMessage());
		} catch (JsonMappingException e) {
			logger.info("Json Parse Excettion : "+e.getMessage());
		} catch (IOException e) {
			logger.info("Json Parse Excettion : "+e.getMessage());
		}
		

		return recdetails;
}
	public static   MaximumPurchase fromJsontoMaximumPurchase(String json) 
    {
		
		MaximumPurchase maxpurchase = null;
		try {
			
			maxpurchase = new ObjectMapper().readValue(json, MaximumPurchase.class);
		} catch (JsonParseException e) {
			
			logger.info("Json Parse Excettion : "+e.getMessage());
		} catch (JsonMappingException e) {
			logger.info("Json Parse Excettion : "+e.getMessage());
		} catch (IOException e) {
			logger.info("Json Parse Excettion : "+e.getMessage());
		}
		

		return maxpurchase;
}
	public static   JSONObject getJsonObject(String json) 
    {
		JSONObject obj=null;
		try{
		obj= new JSONObject(json);
		
		}catch(Exception js){
			logger.error("JsonException occured while parsing from String :  "+js.getMessage());
		}
		return obj;
		
		
		
		

		
}



}
