package controllers;

import infrastracture.CouchBaseOperation;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.URL;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.Enumeration;
import java.util.GregorianCalendar;
import java.util.List;

import org.codehaus.jettison.json.JSONArray;
import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;

import play.Logger;

public class StoredRealtorDataTocouchabse extends Thread {
	private static org.slf4j.Logger logger = play.Logger.underlying();
	public static void main(String[] args) {
		// new StoredWistiaDataTocouchabse().getPublicIpAddress();
new StoredRealtorDataTocouchabse("", "", "", "").start();
	}

	String contactId;
	String email;
	String ip;
	String leadId;
	String referralid;
	public StoredRealtorDataTocouchabse(String contactId, String email, String ip,String leadId) {
		
		this.contactId = contactId;
		this.email = email;
		this.ip = ip;
		this.leadId = leadId;


	}
	public StoredRealtorDataTocouchabse(String contactId, String email, String ip,String leadId,String referralid) {
		
		this.contactId = contactId;
		this.email = email;
		this.ip = ip;
		this.leadId = leadId;
		this.referralid = referralid;

	}

	public void run() {

		try {
			Thread.sleep(240000);
		} catch (Exception e) {
			 logger.error("Error appeared while connecting"+e.getMessage());
		}

		JSONArray jsonArray = storeRealtorData();
		String applicantName = couchBaseOperation.getReferralname(contactId,
				email);
		String name="";
		String email_from="";
		
		try{
		List<String> nameEmailList = couchBaseOperation.getHumanResourceData(referralid);
		if(nameEmailList!=null && nameEmailList.size() !=0)
		name=nameEmailList.get(0);
		email_from=nameEmailList.get(1);
		}catch(Exception e){
			 logger.error("Error appeared while connecting to couchbase"+e.getMessage());
		}
		
		if (jsonArray != null) {
			
			boolean test = true;
			try {
				while (test) {
					for (int i = 0; i < jsonArray.length(); i++) {
						
						JSONObject jsonObject=jsonArray.getJSONObject(i);
					
					try {
						jsonObject.put("ApplicantName", applicantName);
						jsonObject.put("applicantId", contactId);
						jsonObject.put("DateTime", cal.getTime());
						jsonObject.put("leadId",leadId);
						jsonObject.put("referralid", referralid);
						jsonObject.put("referal_name",name);
						jsonObject.put("referal_email",email_from);
						
					} catch (Exception e) {

					}
					if (ip.equalsIgnoreCase(jsonObject.get("ip").toString())) {
						JSONArray json = getVisitorCount();
						String key = "0";
						if (json != null) {
							for (int j = 0; j < json.length(); j++) {
								JSONObject vistorData=json.getJSONObject(j);
								logger.debug("json------"+jsonObject.get("visitor_key").toString()+"  ==   "+vistorData.get("visitor_key").toString());
								if(jsonObject.get("visitor_key").toString().equalsIgnoreCase(vistorData.get("visitor_key").toString())){
									key = vistorData.get("play_count").toString();
	
									break;
								}
								
							}
						}
						couchBaseOperation.storeDataInCouchBase(
								"Vedio_realtorpage_" + key, jsonObject);
						test = false;
					} else {

						try {
							Thread.sleep(200000);
						} catch (Exception e) {
							 logger.error("Error occured while datastored in couchbase"+e.getMessage());
						}
					}
					}
				}
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				 logger.error("Error occured while JSONException"+e.getMessage());
			}

		}
	}

	GregorianCalendar cal = new GregorianCalendar();

	CouchBaseOperation couchBaseOperation = new CouchBaseOperation();
	SimpleDateFormat date = new SimpleDateFormat("yyyy-MM-dd");

	public  JSONArray storeRealtorData() {

		JSONArray json = null;

		try {

			logger.debug(date.format(cal.getTime()));
			// String
			// ur="https://api.wistia.com/v1/stats/events.json?api_password=6bca8b64faaf09189e8266ed51b726292fd133063ffc1ca0d9fab9fc3661f3ab";
			String ur = "https://api.wistia.com/v1/stats/events.json?api_password=6bca8b64faaf09189e8266ed51b726292fd133063ffc1ca0d9fab9fc3661f3ab&start_date="
					+ date.format(cal.getTime())
					+ "&end_date="
					+ date.format(cal.getTime()) + "media_id=wub3jjetak";

			URLEncoder.encode(ur, "UTF-8");
			URL url = new URL(ur);
			HttpURLConnection conn = (HttpURLConnection) url.openConnection();

			conn.setDoOutput(true);
			conn.setRequestMethod("GET");
			conn.setRequestProperty("Content-Type", "application/json");

			if (conn.getResponseCode() != 200) {
				throw new RuntimeException("Failed : HTTP error code : "
						+ conn.getResponseCode());
			} else {

				BufferedReader br = new BufferedReader(new InputStreamReader(
						(conn.getInputStream())));

				String output;
				while ((output = br.readLine()) != null) {
					logger.debug("Output from Server .... \n"+output);
					json = new JSONArray(output);

				}

			}

			conn.disconnect();

		} catch (Exception e) {

			 logger.error("Error occured while connection"+e.getMessage());

		}
		return json;

	}

	public JSONArray getVisitorCount() {

		// String applicantName=couchBaseOperation.getReferralname(contactId,
		// email);
		JSONArray json = null;

		try {

			logger.debug(date.format(cal.getTime()));
			// String
			// ur="https://api.wistia.com/v1/stats/events.json?api_password=6bca8b64faaf09189e8266ed51b726292fd133063ffc1ca0d9fab9fc3661f3ab";
			String ur = "https://api.wistia.com/v1/stats/visitors.json?api_password=6bca8b64faaf09189e8266ed51b726292fd133063ffc1ca0d9fab9fc3661f3ab&media_id=wub3jjetak";

			URLEncoder.encode(ur, "UTF-8");
			URL url = new URL(ur);
			HttpURLConnection conn = (HttpURLConnection) url.openConnection();

			conn.setDoOutput(true);
			conn.setRequestMethod("GET");
			conn.setRequestProperty("Content-Type", "application/json");

			if (conn.getResponseCode() != 200) {
				throw new RuntimeException("Failed : HTTP error code : "
						+ conn.getResponseCode());
			} else {

				BufferedReader br = new BufferedReader(new InputStreamReader(
						(conn.getInputStream())));

				logger.debug("Output from Server .... \n");
				String output;
				while ((output = br.readLine()) != null) {

					json = new JSONArray(output);

				}

			}

			conn.disconnect();

		} catch (Exception e) {

			 logger.error("Error occured in getVisitorCount"+e.getMessage());

		}
		return json;

	}
}
