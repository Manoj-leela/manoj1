package couchbase;

import helper.GenericHelperClass;

import java.io.IOException;
import java.net.URI;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Set;
import java.util.concurrent.TimeUnit;

import org.json.JSONException;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.couchbase.client.CouchbaseClient;
import com.couchbase.client.java.Bucket;
import com.couchbase.client.java.Cluster;
import com.couchbase.client.java.CouchbaseCluster;
import com.couchbase.client.java.document.JsonDocument;
import com.couchbase.client.java.document.json.JsonObject;
import com.couchbase.client.java.env.CouchbaseEnvironment;
import com.couchbase.client.java.env.DefaultCouchbaseEnvironment;

//import com.syml.helper.GenericHelperClass;

public class CouchBaseOperation {
	CouchbaseClient client1 = null;
	static Logger log = LoggerFactory.getLogger(CouchBaseOperation.class);

	
	public static void main(String[] args) {
		new CouchBaseOperation().getCouchbaseClusterConnection();
	}
	// dev connection-----------------------

	public CouchbaseClient getConnectionToCouchBaseDev() {
		log.info("Comming to getConnectionToCouchBaseDev*****************************");
		String url = null;
		String bucket = null;
		String pass = null;
		// int maximumRetry=0;

		Properties prop = new Properties();
		ArrayList<URI> nodes = new ArrayList<URI>();
		try {

			// getting connection parameter
			prop.load(CouchBaseOperation.class.getClassLoader()
					.getResourceAsStream("config.properties"));

		} catch (Exception e) {
			log.error("error in getting the property file" + e);
		}

		try {
			log.info("inside getConnectionToCouchBase method of CouchBaseOperation class");
			url = prop.getProperty("couchBaseUrl");

			bucket = prop.getProperty("couchBaseBucketName");
			pass = prop.getProperty("couchBaseBucketPassword");
			// maximumRetry=new Integer(prop.getProperty("maximumRetry"));

			nodes.add(URI.create(url));
			log.debug("connecting .....");

			client1 = new CouchbaseClient(nodes, bucket, pass);
		} catch (IOException e) {
			log.error("error while connecting to couchbase" + e);

		}
		return client1;
	}

	public void getCouchbaseClusterConnection() {
		final List<String> SEED_IPS = Arrays.asList("52.25.159.93",
				"198.72.106.5", "198.72.106.10");

		CouchbaseEnvironment ENV = DefaultCouchbaseEnvironment.builder()
				.connectTimeout(8 * 1000) // 8 Seconds in milliseconds
				.keepAliveInterval(3600 * 1000) // 3600 Seconds in milliseconds
				.build();

		final Cluster CLUSTER = CouchbaseCluster.create(ENV, SEED_IPS);

		final Bucket EXAMPLEBUCKET = CLUSTER.openBucket("syml", "symL@0115");
		
		System.out.println("bucket data "+EXAMPLEBUCKET);
		String tesyValue = "{\"test\":\"test\"}";
		JsonObject jsonObject = JsonObject.fromJson(tesyValue);
		JsonDocument jsonDocument = JsonDocument.create("Test", jsonObject);
		EXAMPLEBUCKET.upsert(jsonDocument);
		System.out.println("data from couchbase "+		EXAMPLEBUCKET.get("Test"));
	}

	// couchbase production connection ---------------------------------->
	public CouchbaseClient getConnectionToCouchBase() {
		String url = null;
		String bucket = null;
		String pass = null;
		int maximumRetry = 0;

		Properties prop = new GenericHelperClass().readConfigfile();
		ArrayList<URI> nodes = new ArrayList<URI>();

		try {

			log.info("inside getConnectionToCouchBase method of CouchBaseOperation class");

			url = prop.getProperty("couchBaseUrl3");
			bucket = prop.getProperty("couchBaseBucketName");
			pass = prop.getProperty("couchBaseBucketPassword");
			try {
				maximumRetry = new Integer(prop.getProperty("maximumRetry"));
				System.out.println(url + " " + bucket + "" + pass + ""
						+ prop.getProperty("couchBaseBucketPassword2"));

			} catch (Exception e) {
				e.printStackTrace();
			}
			// 1. Add one or more nodes of your cluster (exchange the IP with
			// yours)
			nodes.add(URI.create(url));
			log.debug("connecting .....");

			client1 = getCouchbaseConnectionOne(nodes, bucket, pass,
					maximumRetry);
			if (client1 == null) {
				url = prop.getProperty("couchBaseUrl1");
				bucket = prop.getProperty("couchBaseBucketName2");
				pass = prop.getProperty("couchBaseBucketPassword2");
				ArrayList<URI> nodes1 = new ArrayList<URI>();
				nodes1.add(URI.create(url));
				System.out.println(url + " " + bucket + ""
						+ prop.getProperty("couchBaseBucketPassword2"));

				client1 = getCouchbaseConnectionTwo(nodes1, bucket, pass,
						maximumRetry);
				if (client1 == null) {

					url = prop.getProperty("couchBaseUrl2");
					bucket = prop.getProperty("couchBaseBucketName2");
					pass = prop.getProperty("couchBaseBucketPassword2");
					nodes1 = new ArrayList<URI>();
					nodes1.add(URI.create(url));
					client1 = getCouchbaseConnectionTwo(nodes1, bucket, pass,
							maximumRetry);

					if (client1 == null) {
						// Send mail error in connecting couchbase
						log.error("Error in Connecting Couhbase");
					}
				}
			}
		} catch (Exception e) {
			log.error("error while connecting to couchbase " + e.getMessage());

		}

		return client1;
	}

	public CouchbaseClient getCouchbaseConnectionOne(ArrayList<URI> nodes,
			String bucketName, String password, int maximumRetry) {

		log.debug("inside GetcoonectionOne  method of couchbase");
		int retry = 1;
		CouchbaseClient client = null;
		while (retry <= maximumRetry && client == null) {
			log.debug("inside whilw GetcoonectionOne  method of couchbase");

			try {
				client = new CouchbaseClient(nodes, bucketName, password);
			} catch (Exception e) {
				client = null;
				log.error(" getCouchbaseConnectionOne  Retry..    " + retry);
				retry += 1;
				log.error("error in connecting Couchbase one" + e);
			}

		}
		return client;
	}

	public CouchbaseClient getCouchbaseConnectionTwo(ArrayList<URI> nodes,
			String bucketName, String password, int maximumRetry) {
		log.debug("inside GetCouchbaseConectionTwo  method of couchbase");
		int retry = 1;
		CouchbaseClient client = null;
		while (retry <= maximumRetry && client == null) {
			try {
				System.out.println(nodes + "" + bucketName + "" + password);
				client = new CouchbaseClient(nodes, bucketName, password);

			} catch (Exception e) {
				client = null;
				log.error(" getCouchbaseConnectionTwo Retry..   " + retry);
				retry += 1;
				log.error("error in connecting Couchbase two" + e);
			}

		}
		return client;
	}

	public CouchbaseClient getCouchbaseConnectionThree(ArrayList<URI> nodes,
			String bucketName, String password, int maximumRetry) {
		log.debug("inside GetCouchbaseConectionThree  method of couchbase");
		int retry = 1;
		CouchbaseClient client = null;
		while (retry <= maximumRetry && client == null) {
			try {
				System.out.println(nodes + "" + bucketName + "" + password);
				client = new CouchbaseClient(nodes, bucketName, password);

			} catch (Exception e) {
				client = null;
				log.error(" getCouchbaseConnectionThree Retry..   " + retry);
				retry += 1;
				log.error("error in connecting Couchbase three" + e);
			}

		}
		return client;
	}

	@SuppressWarnings("unchecked")
	public void storeDataInCouchBase(String key, String formType,
			@SuppressWarnings("rawtypes") HashMap map) {
		try {
			log.info("inside storeDataInCouchBase method of CouchBaseOperation class");
			client1 = getConnectionToCouchBase();

			// convert data into json
			JSONObject jsonData = new JSONObject();
			jsonData.put("FormType", formType);
			Set<Map.Entry<String, String>> set = map.entrySet();
			for (Map.Entry<String, String> entry : set) {
				jsonData.put(entry.getKey(), entry.getValue());
			}
			DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss"); // get
																					// current
																					// date
																					// time
																					// with
																					// Calendar()
			Calendar cal = Calendar.getInstance();
			String currentDateTime = (dateFormat.format(cal.getTime()));
			jsonData.put("Submission_Date_Time1b", currentDateTime);

			log.debug("sending data...");

			client1.set(key, jsonData.toString());
			log.debug("sending data... done with id :" + key);
			closeCouchBaseConnection();

		} catch (Exception e) {
			log.error("error while storing data into couchbase : " + e);
		}

	}

	public void storeJsonDataInCouchBase(String key, JSONObject jsonData) {
		try {
			log.info("inside storeDataInCouchBase method of CouchBaseOperation class");
			client1 = getConnectionToCouchBase();

			DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss"); // get
																					// current
																					// date
																					// time
																					// with
																					// Calendar()
			Calendar cal = Calendar.getInstance();
			String currentDateTime = (dateFormat.format(cal.getTime()));
			jsonData.put("Submission_Date_Time1b", currentDateTime);

			log.debug("sending data...");

			client1.set(key, jsonData.toString());
			log.debug("sending data... done with id :" + key);
			closeCouchBaseConnection();

		} catch (Exception e) {
			log.error("error while storing data into couchbase : " + e);
		}

	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	public void appendDataInCouchBase(String key, HashMap appendData) {
		try {
			log.info("inside appendDataInCouchBase method of CouchBaseOperation class");
			client1 = getConnectionToCouchBase();

			String object = (String) client1.get(key);
			// convert data into json
			log.debug("old json  data...  " + object);
			JSONObject jsonData = new JSONObject(object);
			Set<Map.Entry<String, String>> set = appendData.entrySet();
			for (Map.Entry<String, String> entry : set) {
				jsonData.put(entry.getKey(), entry.getValue());
			}

			log.debug("new json  data...  " + jsonData.toString());
			client1.replace(key, jsonData.toString());
			// client1.replace(key, o1.toString());
			log.debug("replacing data... done");

			closeCouchBaseConnection();

		} catch (Exception e) {
			log.error("error while apeending data into couchbase : " + e);
		}

	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	public void editDataInCouchBase(String key, String formType,
			HashMap editData) {
		try {
			log.info("inside editDataInCouchBase method of CouchBaseOperation class");

			client1 = getConnectionToCouchBase();
			DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss"); // get
																					// current
																					// date
																					// time
																					// with
																					// Calendar()
			Calendar cal = Calendar.getInstance();
			String currentDateTime = (dateFormat.format(cal.getTime()));

			// convert data into json
			JSONObject jsonData = new JSONObject();
			jsonData.put("FormType", formType);
			Set<Map.Entry<String, String>> set = editData.entrySet();
			for (Map.Entry<String, String> entry : set) {
				jsonData.put(entry.getKey(), entry.getValue());
			}

			jsonData.put("Submission_Date_Time1b", currentDateTime);

			log.debug("editing data...");
			client1.replace(key, jsonData.toString());

			closeCouchBaseConnection();

		} catch (Exception e) {
			log.error("error while editing data into couchbase : " + e);
		}
	}


	public JSONObject getData(String key) {
		String object = "";
		JSONObject jsonData = null;
		try {
			client1 = getConnectionToCouchBase();

			object = (String) client1.get(key);
			jsonData = new JSONObject(object);
			closeCouchBaseConnection();

		} catch (Exception e) {

		}
		return jsonData;
	}

	public void closeCouchBaseConnection() {
		log.debug("closing connection");
		client1.shutdown(900l, TimeUnit.MILLISECONDS);

	}

	public void deleteCouchBaseData(String key) {
		log.debug("Deleting couchbase data based on key");
		client1 = getConnectionToCouchBase();
		client1.delete(key);
		closeCouchBaseConnection();
	}

	public JSONObject getCouchBaseData(String key) {

		play.Logger.debug("Inside Get couchbase data based on key");
		client1 = getConnectionToCouchBase();

		String object = (String) client1.get(key);

		JSONObject jsonObject = null;
		try {
			jsonObject = new JSONObject(object);
			// System.out.println("jsonOjbectdddd "+jsonObject);
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			play.Logger.error("error in getting data from couchbase " + e);
		}
		closeCouchBaseConnection();
		return jsonObject;
	}

	public ArrayList<String> getReferralData(String referralID) {
		ArrayList<String> list = new ArrayList<String>();
		log.info("inside findReferralSourceCode method of CheckReferalSource class");
		String name = "";
		String email = "";
		String firstName = "";
		String LastName = "";
		String phoneNumber = "";
		try {

			JSONObject json = getCouchBaseData("ReferralTrigerData_"
					+ referralID);
			play.Logger.info("referal ID >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>"
					+ "ReferralTrigerData_" + referralID);

			try {
				phoneNumber = (String) json.get("partner_mobile");

			} catch (Exception e) {

			}
			try {
				email = (String) json.get("email_from");
			} catch (Exception e) {

			}
			try {
				name = (String) json.get("name");
			} catch (Exception e) {

			}
			try {
				String arString[] = name.split("_");
				firstName = arString[0];
				LastName = arString[1];
			} catch (Exception e) {

			}
			list.add(phoneNumber);
			list.add(email);
			list.add(firstName);
			list.add(LastName);
		} catch (Exception e) {
			play.Logger.error("Error while reading data from server:\n\n"
					+ e.getMessage());
		}

		return list;
	}

}
