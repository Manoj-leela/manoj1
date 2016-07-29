package com.bizsyml.infrastructure;

import java.io.IOException;
import java.net.URI;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Set;
import java.util.concurrent.TimeUnit;


import org.codehaus.jettison.json.JSONObject;

import play.Logger.ALogger;
import play.api.Logger;
import org.slf4j.LoggerFactory;



import com.couchbase.client.CouchbaseClient;
import com.couchbase.client.protocol.views.DesignDocument;
import com.couchbase.client.protocol.views.Query;
import com.couchbase.client.protocol.views.View;
import com.couchbase.client.protocol.views.ViewDesign;
import com.couchbase.client.protocol.views.ViewResponse;
import com.couchbase.client.protocol.views.ViewRow;
import com.fasterxml.jackson.databind.ObjectMapper;

import controllers.Application;






public class CouchBaseOperation {
	CouchbaseClient client1 = null;
	static ALogger log1 = play.Logger.of(CouchBaseOperation.class);

	
	static org.slf4j.Logger log = LoggerFactory.getLogger(CouchBaseOperation.class);
		ObjectMapper object = new ObjectMapper();

	public CouchbaseClient getConnectionToCouchBase() {
		String url = null;
		String bucket = null;
		String pass = null;
		int maximumRetry = 0;

		Properties prop = new Properties();
		ArrayList<URI> nodes = new ArrayList<URI>();
		try {

			// getting connection parameter
			prop.load(CouchBaseOperation.class.getClassLoader()
					.getResourceAsStream("config.properties"));

		} catch (Exception e) {
			log1.error("error in getting the property file" + e);
		}

		try {
			log1.info("inside getConnectionToCouchBase method of CouchBaseOperation class");
			// url = prop.getProperty("couchBaseUrl");

			url = prop.getProperty("couchBaseUrl");
			bucket = prop.getProperty("couchBaseBucketName");
			pass = prop.getProperty("couchBaseBucketPassword");
			//maximumRetry = new Integer(prop.getProperty("maximumRetry"));

			// 1. Add one or more nodes of your cluster (exchange the IP with
			// yours)
			nodes.add(URI.create(url));
			log1.debug("connecting .....");

			// =======

			client1 = new CouchbaseClient(nodes, bucket, pass);
		} catch (IOException e) {
			// TODO Please confirm with Shan the config of Production Couchbase
			// instance ... Should there be a failover address in catch block
			// and error in a final block?
			log1.error("error while connecting to couchbase" + e);

		}
		return client1;
	}

	public CouchbaseClient getConnectionToCouchBaseForProduction(){
		String url = null;
		String bucket = null;
		String pass = null;
		int maximumRetry = 0;

		Properties prop = new Properties();
		ArrayList<URI> nodes = new ArrayList<URI>();

		try {

			// getting connection parameter
			prop.load(CouchBaseOperation.class.getClassLoader()
					.getResourceAsStream("config.properties"));
			log1.info("prop.getProperty"
					+ prop.getProperty("couchBaseBucketName"));
		} catch (Exception e) {
			log1.error("error in getting the property file" + e);
		}
		try {

			log1.info("inside getConnectionToCouchBase method of CouchBaseOperation class");
			// url = prop.getProperty("couchBaseUrl");
			url = prop.getProperty("couchBaseUrl1");
			bucket = prop.getProperty("couchBaseBucketName");
			pass = prop.getProperty("couchBaseBucketPassword");
			maximumRetry = new Integer(prop.getProperty("maximumRetry"));

			// 1. Add one or more nodes of your cluster (exchange the IP with
			// yours)
			nodes.add(URI.create(url));
			log1.debug("connecting .....");

			client1 = getCouchbaseConnectionOne(nodes, bucket, pass,
					maximumRetry);
			if (client1 == null) {
				url = prop.getProperty("couchBaseUrl2");
				nodes.add(URI.create(url));
				client1 = getCouchbaseConnectionTwo(nodes, bucket, pass,
						maximumRetry);
				if (client1 == null) {

					// Send mail error in connecting couchbase
					log1.error("Error in Connecting Couhbase");
				}
			}
		} catch (Exception e) {
			log1.error("error while connecting to couchbase " + e);

		}

		return client1;
	}

	public CouchbaseClient getCouchbaseConnectionOne(ArrayList<URI> nodes,
			String bucketName, String password, int maximumRetry) {

		log.debug("inside GetcoonectionOne  method of couchbase");
		int retry = 1;
		CouchbaseClient client = null;
		while (retry <= maximumRetry && client == null) {
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

	public void storeDataInCouchBase(String key, JSONObject string) {
		log1.info("inside storeDataInCouchBase method of CouchBaseOperation class");

		try {
			
			DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");            //get current date time with Calendar()
	        Calendar cal = Calendar.getInstance();
	        String currentDateTime=(dateFormat.format(cal.getTime())); 
	        System.out.println("Current Time"+currentDateTime);
			
			string.put("Submission_Date_Time1b", currentDateTime);
			client1 = getConnectionToCouchBase();
			client1.set(key, string.toString());
		} catch (Exception e) {
			log1.error("error while storing data into couchbase : " + e);
		}

	}

public void  updateDataInCouchBase(String key, JSONObject editData){
		
		
		try {
			log1.info("inside editDataInCouchBase method of CouchBaseOperation class");

			DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");            //get current date time with Calendar()
	        Calendar cal = Calendar.getInstance();
	        String currentDateTime=(dateFormat.format(cal.getTime())); 
	        System.out.println("Current Time"+currentDateTime);
			
	        editData.put("Submission_Date_Time1b", currentDateTime);
			
			client1 = getConnectionToCouchBase();
			
			client1.replace(key, editData);

			closeCouchBaseConnection();

		} catch (Exception e) {
			e.printStackTrace();
			log1.error("error while editing data into couchbase : " + e);
		}
	}

	public void closeCouchBaseConnection() {
		log.debug("closing connection");
		client1.shutdown(9000l, TimeUnit.MILLISECONDS);

	}

	
	
	
		
	
		
		

}
