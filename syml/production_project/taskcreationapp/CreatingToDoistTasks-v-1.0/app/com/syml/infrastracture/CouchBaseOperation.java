package com.syml.infrastracture;

import java.io.IOException;
import java.net.URI;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Iterator;
import java.util.List;
import java.util.Properties;
import java.util.concurrent.TimeUnit;

import org.apache.http.auth.AuthScope;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.CredentialsProvider;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.BasicCredentialsProvider;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;

import com.couchbase.client.CouchbaseClient;
import com.couchbase.client.protocol.views.DesignDocument;
import com.couchbase.client.protocol.views.ViewDesign;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.syml.dto.User;

public class CouchBaseOperation {
	CouchbaseClient client1 = null;
	// static Logger log = LoggerFactory.getLogger(CouchBaseOperation.class);
	private static org.slf4j.Logger log = play.Logger.underlying();

	ObjectMapper object = new ObjectMapper();

	public CouchbaseClient getConnectionToCouchBaseDev() {
		String url = null;
		String bucket = null;
		String pass = null;
		@SuppressWarnings("unused")
		int maximumRetry = 0;

		Properties prop = new Properties();
		ArrayList<URI> nodes = new ArrayList<URI>();
		try {

			// getting connection parameter
			prop.load(CouchBaseOperation.class.getClassLoader()
					.getResourceAsStream("config.properties"));

		} catch (Exception e) {
			log.error("error in getting the property file" + e.getMessage());
		}

		try {
			log.info("inside getConnectionToCouchBase method of CouchBaseOperation class");
			// url = prop.getProperty("couchBaseUrl");

			url = prop.getProperty("couchBaseUrl");
			bucket = prop.getProperty("couchBaseBucketName");
			pass = prop.getProperty("couchBaseBucketPassword");
			maximumRetry = new Integer(prop.getProperty("maximumRetry"));

			// 1. Add one or more nodes of your cluster (exchange the IP with
			// yours)
			nodes.add(URI.create(url));
			log.debug("connecting .....");

			// =======

			client1 = new CouchbaseClient(nodes, bucket, pass);
		} catch (IOException e) {
			// TODO Please confirm with Shan the config of Production Couchbase
			// instance ... Should there be a failover address in catch block
			// and error in a final block?
			log.error("error while connecting to couchbase" + e.getMessage());

		}
		return client1;
	}

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
			log.error("error in getting the property file" + e.getMessage());
		}

		try {

			log.info("inside getConnectionToCouchBase method of CouchBaseOperation class");
			// url = prop.getProperty("couchBaseUrl");
			url = prop.getProperty("couchBaseUrl3");
			bucket = prop.getProperty("couchBaseBucketName");
			pass = prop.getProperty("couchBaseBucketPassword");
			try {
				maximumRetry = new Integer(prop.getProperty("maximumRetry"));
			} catch (Exception e) {
				log.error("error while getting couchbaseconnection" + e.getMessage());
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

				client1 = getCouchbaseConnectionTwo(nodes1, bucket, pass,
						maximumRetry);
				if (client1 == null) {
					url = prop.getProperty("couchBaseUrl2");
					bucket = prop.getProperty("couchBaseBucketName2");
					pass = prop.getProperty("couchBaseBucketPassword2");
					nodes1 = new ArrayList<URI>();
					nodes1.add(URI.create(url));

					client1 = getCouchbaseConnectionThree(nodes1, bucket, pass,
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
			try {
				client = new CouchbaseClient(nodes, bucketName, password);
			} catch (Exception e) {
				client = null;
				log.error(" getCouchbaseConnectionOne  Retry..    " + retry);
				retry += 1;
				log.error("error in connecting Couchbase one" + e.getMessage());
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
				log.debug(nodes + "" + bucketName + "" + password);
				client = new CouchbaseClient(nodes, bucketName, password);
			} catch (Exception e) {
				client = null;
				log.error(" getCouchbaseConnectionTwo Retry..   " + retry);
				retry += 1;
				log.error("error in connecting Couchbase two" + e.getMessage());
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
				log.info(nodes + "" + bucketName + "" + password);
				client = new CouchbaseClient(nodes, bucketName, password);

			} catch (Exception e) {
				client = null;
				log.error(" getCouchbaseConnectionThree Retry..   " + retry);
				retry += 1;
				log.error("error in connecting Couchbase three" + e.getMessage());
			}

		}
		return client;
	}

	public void storeDataInCouchBase(String key, JSONObject data) {
		log.info("inside storeDataInCouchBase method of CouchBaseOperation class");
		DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss"); // get
																				// current
																				// date
																				// time
																				// with
																				// Calendar()
		Calendar cal = Calendar.getInstance();
		String currentDateTime = (dateFormat.format(cal.getTime()));
		try {
			data.put("Submission_Date_Time1b", currentDateTime);
			client1 = getConnectionToCouchBase();
			client1.set(key, data.toString());
		} catch (Exception e) {
			log.error("error while storing data into couchbase : " + e.getMessage());
		}

	}

	public void updatedDataInCouchBase(String key, JSONObject editData) {
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
			try {
				editData.put("Submission_Date_Time1b", currentDateTime);
			} catch (Exception e) {

			}
			log.debug("editing data...");
			client1.replace(key, editData.toString());

			closeCouchBaseConnection();

		} catch (Exception e) {
			log.error("error while editing data into couchbase : "+e.getMessage());
		}
	}

	public void closeCouchBaseConnection() {
		log.debug("closing connection");
		client1.shutdown(9000l, TimeUnit.MILLISECONDS);

	}

	static int dataExsist = 0;

	public User getTodoistUserEmail(String username) {
		ArrayList<User> listOfTodoistUser=(ArrayList<User>) getUsers();
		User user=null;
		for (@SuppressWarnings("rawtypes")
		Iterator iterator = listOfTodoistUser.iterator(); iterator
				.hasNext();) {
			 user = (User) iterator.next();
			
			if(user.getUserName().equalsIgnoreCase(username.trim()))
				break;
			
		}
		
		return user;

	}

	private DesignDocument getDesignDocument(String name) {
		try {
			log.debug("Design document with " + name + " exist ? "
					+ client1.getDesignDoc(name));
			return client1.getDesignDoc(name);
		} catch (com.couchbase.client.protocol.views.InvalidViewException e) {
			return new DesignDocument(name);
		}
	}

	

	

	public JSONObject getCouchBaseData(String key) {
		log.debug("Inside Get couchbase data based on key");
		client1 = getConnectionToCouchBase();
		String object = (String) client1.get(key);

		JSONObject jsonObject = null;
		try {
			jsonObject = new JSONObject(object);
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			log.error("error in getting data from couchbase "+e.getMessage());
		}
		closeCouchBaseConnection();
		return jsonObject;
	}

	public List<User> getUsers()  {
		ArrayList<User> list = new ArrayList<User>();

		try {
			client1 = getConnectionToCouchBase();

			DesignDocument designdoc = getDesignDocument("dev_TodoistUserGetView");
			boolean found = false;

			// 5. get the views and check the specified in code for existence
			for (ViewDesign view : designdoc.getViews()) {
				if (view.getMap() == "user") {
					found = true;
					break;
				}
			}

			// 6. If not found then create view inside document
			if (!found) {

				ViewDesign view = new ViewDesign("user",
						"function (doc, meta) {\n" + "if(doc.token)\n"
								+ "{emit(meta.id,null)}\n" +

								"}");
				designdoc.getViews().add(view);
				client1.createDesignDoc(designdoc);
			}

			// 7. close the connection with couchbase

		} catch (Exception e) {
			// TODO Auto-generated catch block
			log.error("error while getting couchbase connection "+e.getMessage());
		}

		try{
		list=	getCocuhbViewData("TodoistUserGetView", "user");
			
		}catch(Exception e){
			log.error("error in getting the couchbase view dtata for Todoist User"+e.getMessage());
		}
			client1.shutdown();

		return list;
	}

	

	public  ArrayList<User> getCocuhbViewData(String designDooc, String viewName)
				throws JSONException {
		ArrayList<User> list = new ArrayList<User>();

		Properties properties=getCouchbasePropertiesFile();
 	   ObjectMapper object = new ObjectMapper();
 	  client1=getConnectionToCouchBase();
				String couchbaseHost="";
				int couchbasePort=0;
				String couchbaseUser="";
				String couchbasePassword="";
				JSONObject jsonObject=null;
		        org.codehaus.jettison.json.JSONArray jsonArray=null;
		        JSONObject getProjectId=null;
				if(properties!=null){
					couchbaseHost=properties.getProperty("couchbaseHost");
					couchbasePort=Integer.parseInt( properties.getProperty("couchbaseHostPort"));
					couchbaseUser=properties.getProperty("couchbaseHostUser");
					couchbasePassword=properties.getProperty("couchbaseHostPassword");
				}
			  CredentialsProvider credsProvider = new BasicCredentialsProvider();
		        credsProvider.setCredentials(
		                new AuthScope(couchbaseHost, couchbasePort),
		                new UsernamePasswordCredentials(couchbaseUser, couchbasePassword));
		        CloseableHttpClient httpclient = HttpClients.custom()
		                .setDefaultCredentialsProvider(credsProvider)
		                .build();
		        
		        try {
		            HttpGet httpget = new HttpGet("http://"+couchbaseHost+":"+couchbasePort+"/syml/_design/dev_"+designDooc+"/_view/"+viewName+"?full_set=true&inclusive_end=true&stale=false&connection_timeout=60000&limit=10&skip=0");

		          log.info("Executing request " + httpget.getRequestLine());
		            CloseableHttpResponse response = httpclient.execute(httpget);
		            try {
		              log.info("statu of getting desisndoc "+response.getStatusLine());
		              log.info(" ResponseData of DesignDoc "+response.getStatusLine());
		               jsonObject=new JSONObject(EntityUtils.toString(response.getEntity()));
		               jsonArray=jsonObject.getJSONArray("rows");
		               for (int i = 0; i < jsonArray.length(); i++) {
		            	   getProjectId=jsonArray.getJSONObject(i);
		   				User user = object.readValue(client1.get(getProjectId.getString("id").toString()).toString()   ,
		   						User.class);
		   				list.add(user);
					}
		   			return list;

		            } finally {
		            	try{
		                response.close();
		            	}catch(Exception e){
		            		log.error("error while performing operation on getCocuhbViewData "+e.getMessage());		
		            	}
		            }
		        } catch (ClientProtocolException e) {
					// TODO Auto-generated catch block
					log.error("error in gettin the view query Data "+e.getMessage());
				} catch (IOException e) {
					// TODO Auto-generated catch block
					log.error("error in gettin the view query Data "+e.getMessage());
				} finally {
		            try {
						httpclient.close();
					} catch (IOException e) {
						// TODO Auto-generated catch block
						log.error("failed to close http client S "+e.getMessage());
					}
		        }
		
			return list;
		}

	public static Properties getCouchbasePropertiesFile() {
		Properties prop = new Properties();
		try {

			// getting connection parameter
			prop.load(CouchBaseOperation.class.getClassLoader()
					.getResourceAsStream("config.properties"));

		} catch (Exception e) {
			log.error("error in getting the property file" + e.getMessage());
		}
		return prop;
	}
	
	public static void main(String[] args) throws JSONException {new CouchBaseOperation().getCocuhbViewData("TodoistUserGetView", "user");
 log.debug("",new CouchBaseOperation().getCouchBaseData("Applicant_773"));
	}
}
