package controllers;

import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;

import org.codehaus.jettison.json.JSONArray;
import org.codehaus.jettison.json.JSONObject;

import platform.Platform;

import com.bizsyml.infrastructure.CouchBaseOperation;
import com.couchbase.client.CouchbaseClient;
import com.jayway.jsonpath.JsonPath;
import com.squareup.okhttp.ResponseBody;

import http.APIResponse;

public class SendSMS {
	
	JSONObject jsonObject;

	public static void sendSms() throws Exception{
		System.out.println("********************Inside  Send  SMS*************");
		CouchBaseOperation couchbase = new CouchBaseOperation();
		CouchbaseClient client = null;
		JSONObject jsonObject;

		
		Platform p = new Platform("5C15396Df3e3d4897A2419e4343cf4363B3Febf780F90e01226d87A2419F4DD6", "2005E1EF9c8dBB7C7CE88526d9106A0391E4c0ff0099DA99C21Bd384cea64E5A", Platform.Server.PRODUCTION);

		p.authorize("18556992400", "102 ", "Syml@123");

		System.out.println("Access Token: "+p.getAccessToken());

		APIResponse obj = p.apiCall("GET", "/restapi/v1.0/account/~/extension/~/sms",null,null);

		ResponseBody str2 = obj.body();

		String json = str2.string();
		System.out.println("*****************Json String for  SMS  IS***************" + json);
		jsonObject=new JSONObject(json);
		DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");            //get current date time with Calendar()
		
		 Calendar cal = Calendar.getInstance();
		 
		 String currentDateTime=(dateFormat.format(cal.getTime())); 
		System.out.println("Json Data Is  for  Messages"+json);
		//save(str2.string());
		
		String fromPhoneNumber=JsonPath.read(json, "$.records[0].from.phoneNumber");
		String TOPhoneNumber=JsonPath.read(json, "$.records[0].to[0].phoneNumber");
		String  subject=JsonPath.read(json, "$.records[0].subject");
        System.out.println("******From******"+fromPhoneNumber);

         System.out.println("******TO******"+TOPhoneNumber);
         System.out.println("******subject******"+subject);
         
         
 JSONArray jr = new JSONArray();
         
         JSONObject jobj = new JSONObject();
         jobj.put("From", fromPhoneNumber);
         jobj.put("To", TOPhoneNumber);
         jobj.put("Message", subject);

		
	  	couchbase.storeDataInCouchBase("Sms_Log"+fromPhoneNumber+"_"+TOPhoneNumber, jobj);

		System.out.println("Data stored in couchBase");
		
		
		
		try{
			Thread.sleep(300000);
		}catch (Exception e) {
			e.printStackTrace();
		}
		

	
		System.out.println("done 2");

		System.out.println(obj.error());
	}
	public static void main(String[] args) throws Exception {
		sendSms();
	}

}
