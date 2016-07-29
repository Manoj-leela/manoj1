package com.bizsyml.utill;

import java.util.HashMap;
import com.google.gson.Gson;
import com.google.gson.JsonObject;

/*
 *  This class holds the data that need to be passed as JSON object to RingOut API call
 */

public class RingDataObject {

	private HashMap<String, String> from = new HashMap<String, String>();
	private HashMap<String, String> to = new HashMap<String, String>();
	
	RingDataObject(String Caller, String Callee){
		from.put("phoneNumber", Caller);
		to.put("phoneNumber", Callee);
	}
	
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		
		RingDataObject obj = new RingDataObject("3109885496","3102515847");
		Gson gson = new Gson();
		String json = gson.toJson(obj);
		System.out.println(json);
	 
	}

}
