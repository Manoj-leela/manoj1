package infrastracture;

import java.io.IOException;
import java.net.URI;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Iterator;
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

import play.Logger;

import com.couchbase.client.CouchbaseClient;
import com.couchbase.client.protocol.views.DesignDocument;
import com.couchbase.client.protocol.views.ViewDesign;
import com.debortoliwines.openerp.api.Session;
import com.fasterxml.jackson.databind.ObjectMapper;

import controllers.Client;

public class CouchBaseOperation {
	
	private static org.slf4j.Logger logger = play.Logger.underlying();
	CouchbaseClient client1 = null;
	
	ObjectMapper object = new ObjectMapper();

	// --------------------------------couchbase
	// connection----------------------------------

	public CouchbaseClient getConnectionToCouchBaseProduc() {
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
			logger.error("error occured in couchbase connection "+e.getMessage());
		}
		logger.debug("cooooonection");

		try {
			logger.info("inside getConnectionToCouchBase method of CouchBaseOperation class");
			// url = prop.getProperty("couchBaseUrl");

			url = "http://107.23.89.76:8091/pools";
			// prop.getProperty("couchBaseUrl");
			bucket = "syml";
			// prop.getProperty("couchBaseBucketName");
			pass = "symL@0115";
			// prop.getProperty("couchBaseBucketPassword");
			// maximumRetry = new Integer(prop.getProperty("maximumRetry"));

			// 1. Add one or more nodes of your cluster (exchange the IP with
			// yours)
			nodes.add(URI.create(url));
			logger.debug("connecting .....");

			// =======

			client1 = new CouchbaseClient(nodes, bucket, pass);
		} catch (IOException e) {
			// TODO Please confirm with Shan the config of Production Couchbase
			// instance ... Should there be a failover address in catch block
			// and error in a final block?
			logger.error("error while connecting to couchbase" + e.getMessage());

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
			logger.error("error in getting the property file" + e.getMessage());
		}

		try {

			logger.info("inside getConnectionToCouchBase method of CouchBaseOperation class");
			// url = prop.getProperty("couchBaseUrl");
			url = "http://52.25.159.93:8091/pools";
			// prop.getProperty("couchBaseUrl1");
			bucket = "syml";
			// prop.getProperty("couchBaseBucketName");
			pass = "symL@0115";
			// prop.getProperty("couchBaseBucketPassword");
			try {
				maximumRetry = 3;
				// new Integer(prop.getProperty("maximumRetry"));
			} catch (Exception e) {
				logger.error("error in getting the property file" + e.getMessage());
			}
			// 1. Add one or more nodes of your cluster (exchange the IP with
			// yours)
			nodes.add(URI.create(url));
			logger.debug("connecting .....");

			client1 = getCouchbaseConnectionOne(nodes, bucket, pass,
					maximumRetry);
			if (client1 == null) {
				url = "http://198.72.106.5:8091/pools";
				// prop.getProperty("couchBaseUrl2");
				// bucket = prop.getProperty("couchBaseBucketName2");
				// pass =prop.getProperty("couchBaseBucketPassword2");
				ArrayList<URI> nodes1 = new ArrayList<URI>();
				nodes1.add(URI.create(url));
				
				// +" "+bucket+""+prop.getProperty("couchBaseBucketPassword2"));

				client1 = getCouchbaseConnectionTwo(nodes1, bucket, pass,
						maximumRetry);
				if (client1 == null) {
					url = "http://198.72.106.10:8091/pools";
					// prop.getProperty("couchBaseUrl2");
					// bucket = prop.getProperty("couchBaseBucketName2");
					// pass =prop.getProperty("couchBaseBucketPassword2");
					nodes1 = new ArrayList<URI>();
					nodes1.add(URI.create(url));
					client1 = getCouchbaseConnectionThree(nodes1, bucket, pass,
							maximumRetry);

					if (client1 == null) {
						// Send mail error in connecting couchbase
						logger.error("Error in Connecting Couhbase");
					}
				}
			}
		} catch (Exception e) {
			logger.error("error while connecting to couchbase "+e.getMessage());

		}

		return client1;
	}

	public CouchbaseClient getCouchbaseConnectionOne(ArrayList<URI> nodes,
			String bucketName, String password, int maximumRetry) {

		logger.debug("inside GetcoonectionOne  method of couchbase");
		int retry = 1;
		CouchbaseClient client = null;
		while (retry <= maximumRetry && client == null) {
			try {
				client = new CouchbaseClient(nodes, bucketName, password);
			} catch (Exception e) {
				client = null;
				logger.error(" getCouchbaseConnectionOne  Retry..    " + retry);
				retry += 1;
				logger.error("error in connecting Couchbase one" +e.getMessage());
			}

		}
		return client;
	}

	public CouchbaseClient getCouchbaseConnectionTwo(ArrayList<URI> nodes,
			String bucketName, String password, int maximumRetry) {
		logger.debug("inside GetCouchbaseConectionTwo  method of couchbase");
		int retry = 1;
		CouchbaseClient client = null;
		while (retry <= maximumRetry && client == null) {
			try {
				logger.debug(nodes + "" + bucketName + "" + password);
				client = new CouchbaseClient(nodes, bucketName, password);
			} catch (Exception e) {
				client = null;
				logger.error(" getCouchbaseConnectionTwo Retry..   " + retry);
				retry += 1;
				logger.error("error in connecting Couchbase two"+e.getMessage());
			}

		}
		return client;
	}

	public void storeDataInCouchBase(String key, JSONObject data) {
		logger.info("inside storeDataInCouchBase method of CouchBaseOperation class");
		logger.debug("key " + key);
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
			closeCouchBaseConnection();
		} catch (Exception e) {
			logger.error("error while storing data into couchbase : " + e.getMessage());
		}

	}

	public CouchbaseClient getCouchbaseConnectionThree(ArrayList<URI> nodes,
			String bucketName, String password, int maximumRetry) {
		logger.debug("inside GetCouchbaseConectionThree  method of couchbase");
		int retry = 1;
		CouchbaseClient client = null;
		while (retry <= maximumRetry && client == null) {
			try {
				logger.debug(nodes + "" + bucketName + "" + password);
				client = new CouchbaseClient(nodes, bucketName, password);

			} catch (Exception e) {
				client = null;
				logger.error(" getCouchbaseConnectionThree Retry..   " + retry);
				retry += 1;
				logger.error("error in connecting Couchbase three" + e.getMessage());
			}

		}
		return client;
	}

	public void updatedDataInCouchBase(String key, JSONObject editData) {
		try {
			logger.info("inside editDataInCouchBase method of CouchBaseOperation class");

			client1 = getConnectionToCouchBase();

			logger.debug("editing data...");

			DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss"); // get
																					// current
																					// date
																					// time
																					// with
																					// Calendar()
			Calendar cal = Calendar.getInstance();
			String currentDateTime = (dateFormat.format(cal.getTime()));
			editData.put("Submission_Date_Time1b", currentDateTime);
			client1.replace(key, editData.toString());

			closeCouchBaseConnection();

		} catch (Exception e) {
			logger.error("error while editing data into couchbase : " + e.getMessage());
		}
	}

	public void updateTaskInCouchBase(String key, JSONObject editData) {

		try {
			logger.info("inside editDataInCouchBase method of CouchBaseOperation class");

			client1 = getConnectionToCouchBase();
			JSONObject json = null;

			try {
				json = new JSONObject(client1.get(key).toString());
				Iterator<String> keysItr = editData.keys();

				while (keysItr.hasNext()) {
					String keyString = keysItr.next();
					Object value = editData.get(keyString);
					json.put(keyString, value);
				}
			} catch (Exception e) {
				 logger.error("Error is getting"+e.getMessage());
			}
			logger.debug("editing data...");
			if (json != null) {
				client1.replace(key, json.toString());

			} else {
				client1.set(key, editData.toString());

			}

			closeCouchBaseConnection();

		} catch (Exception e) {
			logger.error("error while editing data into couchbase : " + e.getMessage());
		}
	}

	public void closeCouchBaseConnection() {
		logger.debug("closing connection");
		client1.shutdown(9000l, TimeUnit.MILLISECONDS);

	}

	// -----------------------------------------openERp connection
	// -------------------------

	Session openERPSession = null;

	// connection useed for production -----------------
	public Session getOdooConnection() {
		String ip = null;
		int port = 0;
		String host = null;
		String db = null;
		String user = null;
		String pass = null;
		int maximumRetry = 0;
		try {

			logger.info("inside getOdooConnection method of Generic Helper  class");
			Properties prop = readConfigfile();
			logger.debug("stg---"
					+ prop.getProperty("submitReferralstageId") + "---"
					+ prop.getProperty("visdomreferralStageId") + "----"
					+ prop.getProperty("opprtunitySatgeid"));
			// ip=prop.getProperty("odooIP");
			host = "crm1.visdom.ca";
			// prop.getProperty("odooHost1");
			try {
				port = 8069;
				// new Integer(prop.getProperty("odooPort"));
			} catch (Exception e) {
				 logger.error("Error is getting"+e.getMessage());
			}
			db = "symlsys";
			// prop.getProperty("odooDB");
			user = "admin";
			// prop.getProperty("odooUsername1");
			pass = "BusinessPlatform@Visdom1";
			// prop.getProperty("odoopassword1");
			maximumRetry = 3;
			// new Integer(prop.getProperty("maximumRetry"));
			logger.debug("");

			openERPSession = getCRMConnectionOne(host, port, db, user, pass,
					maximumRetry);
			if (openERPSession == null) {
				logger.warn("error in getCRMConnectionOne");
				host = "crm2.visdom.ca";
				// prop.getProperty("odooHost2");
				// port = new Integer(prop.getProperty("odooPort2"));
				// db = prop.getProperty("odooDB2");
				// user = prop.getProperty("odooUsername2");
				// pass = prop.getProperty("odoopassword2");

				openERPSession = getCRMConnectionTwo(host, port, db, user,
						pass, maximumRetry);

				if (openERPSession == null) {
					logger.warn("error in getCRMConnectionTwo");
				} else {
					logger.debug("connection successful");
				}

			}

		} catch (Exception e) {

			logger.error("error in connectiong with odoo : " + e.getMessage());
		}

		return openERPSession;
	}

	public Session getCRMConnectionOne(String host, int port, String db,
			String user, String pass, int maximumRetry) {

		logger.debug("inside GetCRMConectionOne  method of GenericHelperClass");
		int retry = 1;
		Session openERPSession = null;
		while (retry <= maximumRetry && openERPSession == null) {
			try {
				openERPSession = new Session(host, port, db, user, pass);
				openERPSession.startSession();
			} catch (Exception e) {
				openERPSession = null;
				logger.error(" GetCRMConectionOne  Retry..    " + retry);
				retry += 1;
				logger.error("error in connecting GetCRMConectionOne " + e.getMessage());
			}
		}
		return openERPSession;
	}

	public Session getCRMConnectionTwo(String host, int port, String db,
			String user, String pass, int maximumRetry) {

		logger.debug("inside GetCRMConectionTwo method of GenericHelperClass");
		int retry = 1;
		Session openERPSession = null;
		while (retry <= maximumRetry && openERPSession == null) {
			try {
				openERPSession = new Session(host, port, db, user, pass);
				openERPSession.startSession();
			} catch (Exception e) {
				openERPSession = null;
				logger.error(" GetCRMConectionTwo  Retry..    " + retry);
				retry += 1;
				logger.error("error in connecting GetCRMConectionTwo " + e.getMessage());
			}

		}
		return openERPSession;
	}

	// ---------------------------------------------referral
	// survey-----------------------------------------

	public Properties readConfigfile() {

		Properties prop = new Properties();
		try {
			prop.load(CouchBaseOperation.class.getClassLoader()
					.getResourceAsStream("config.properties"));
		} catch (Exception e) {
			logger.error("error in Reading config.properties file" + e.getMessage());
		}
		return prop;
	}

	// -----------------query to get Already paied applicants------------

	public ArrayList<Client> getClientSurveyFromCouchbase(String opprtunityId) {
		boolean exsist = false;
		ArrayList<Client> list = new ArrayList<Client>();
		try {
			client1 = getConnectionToCouchBase();
			DesignDocument designdoc = getDesignDocument("dev_client_"
					+ opprtunityId);
			boolean found = false;

			// 5. get the views and check the specified in code for existence
			for (ViewDesign view : designdoc.getViews()) {
				if (view.getMap() == "client") {
					found = true;
					break;
				}
			}

			// 6. If not found then create view inside document
			if (!found) {
				ViewDesign view = new ViewDesign("client",
						"function (doc, meta) {\n" + "if(doc.opprtunityId==\""
								+ opprtunityId + "\"&& doc.Type==\"Client\")\n"
								+ "{emit(meta.id,null)}\n" +

								"}");
				designdoc.getViews().add(view);
				client1.createDesignDoc(designdoc);
			}

			
		list=	getCocuhbViewDataForClinet("client_" + opprtunityId, "client");
			// 7. close the connection with couchbase

		} catch (Exception e) {
			// TODO Auto-generated catch block
			 logger.error("Error is getting"+e.getMessage());
		}
		
		return list;
	}

	public ArrayList<Client> getReferralSurveyFromCouchbase(String opprtunityId) {
		boolean exsist = false;
		ArrayList<Client> list = new ArrayList<Client>();
		try {
			client1 = getConnectionToCouchBase();
			DesignDocument designdoc = getDesignDocument("dev_referral_"
					+ opprtunityId);
			boolean found = false;

			// 5. get the views and check the specified in code for existence
			for (ViewDesign view : designdoc.getViews()) {
				if (view.getMap() == "referral") {
					found = true;
					break;
				}
			}

			// 6. If not found then create view inside document
			if (!found) {
				ViewDesign view = new ViewDesign("referral",
						"function (doc, meta) {\n" + "if(doc.opprtunityId==\""
								+ opprtunityId
								+ "\"&& doc.Type==\"Referral\")\n"
								+ "{emit(meta.id,null)}\n" +

								"}");
				designdoc.getViews().add(view);
				client1.createDesignDoc(designdoc);
			}

			list=	getCocuhbViewDataForREferral("referral_"
					+ opprtunityId, "referral");
			// 7. close the connection with couchbase

		} catch (Exception e) {
			// TODO Auto-generated catch block
			 logger.error("Error occured in arraylist in couchbase"+e.getMessage());
		}

		
		return list;
	}

	private DesignDocument getDesignDocument(String name) {
		try {
		logger.debug("Design document with " + name + " exist ? "
					+ client1.getDesignDoc(name));
			return client1.getDesignDoc(name);
		} catch (com.couchbase.client.protocol.views.InvalidViewException e) {
			return new DesignDocument(name);
		}
	}

	public ArrayList<Client> getCocuhbViewDataForClinet(String designDooc,
			String viewName) throws JSONException, ClientProtocolException, IOException {
		ArrayList<Client> list1 = new ArrayList<Client>();
		client1 = getConnectionToCouchBase();

		Properties properties = CouchBaseOperation.getCouchbasePropertiesFile();
		String couchbaseHost = "";
		int couchbasePort = 0;
		String couchbaseUser = "";
		String couchbasePassword = "";
		JSONObject jsonObject = null;
		org.codehaus.jettison.json.JSONArray jsonArray = null;
		if (properties != null) {
			couchbaseHost = properties.getProperty("couchbaseHost");
			couchbasePort = Integer.parseInt(properties
					.getProperty("couchbaseHostPort"));
			couchbaseUser = properties.getProperty("couchbaseHostUser");
			couchbasePassword = properties.getProperty("couchbaseHostPassword");
		}
		CredentialsProvider credsProvider = new BasicCredentialsProvider();
		credsProvider.setCredentials(
				new AuthScope(couchbaseHost, couchbasePort),
				new UsernamePasswordCredentials(couchbaseUser,
						couchbasePassword));
		CloseableHttpClient httpclient = HttpClients.custom()
				.setDefaultCredentialsProvider(credsProvider).build();

		HttpGet httpget = new HttpGet(
				"http://"
						+ couchbaseHost
						+ ":"
						+ couchbasePort
						+ "/syml/_design/dev_"
						+ designDooc
						+ "/_view/"
						+ viewName
						+ "?full_set=true&inclusive_end=true&stale=false&connection_timeout=60000&limit=10&skip=0");

		logger.info("Executing request " + httpget.getRequestLine());
		CloseableHttpResponse response = httpclient.execute(httpget);
		try {
			jsonObject = new JSONObject(EntityUtils.toString(response
					.getEntity()));
			jsonArray = jsonObject.getJSONArray("rows");

			for (int i = 0; i < jsonArray.length(); i++) {

				JSONObject jsonData = new JSONObject(client1.get(
						jsonArray.getJSONObject(i).getString("id").toString())
						.toString());

				try {
					Client client = new Client();
					client.setApplicantId(jsonData.get("applicantId").toString());
					client.setOpprtunityId(jsonData.get("opprtunityId")
							.toString());
					try {
						client.setApplicantname(jsonData.get("ClientName")
								.toString());
					} catch (Exception e) {
						logger.error("Error occured while getting applicantname "+e.getMessage());
					}
					list1.add(client);
				} catch (Exception e) {
					logger.error("Error occured while getting client"+e.getMessage());
				}
			}

		} catch (ClientProtocolException e) {
			// TODO Auto-generated catch block
			logger.error("error in gettin the view query Data " + e.getMessage());
		} catch (IOException e) {
			// TODO Auto-generated catch block
			logger.error("error in gettin the view query Data " + e.getMessage());
		} finally {
			try {
				httpclient.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				logger.error("failed to close http client S " + e.getMessage());
			}
		}
		client1.shutdown();
		return list1;

	}
	

	public ArrayList<Client> getCocuhbViewDataForREferral(String designDooc,
		String viewName) throws JSONException, ClientProtocolException, IOException {
		ArrayList<Client> list1 = new ArrayList<Client>();
		client1 = getConnectionToCouchBase();

		String projectId = "";
		Properties properties = CouchBaseOperation.getCouchbasePropertiesFile();
		String assginUser = "";
		String couchbaseHost = "";
		int couchbasePort = 0;
		String couchbaseUser = "";
		String couchbasePassword = "";
		JSONObject jsonObject = null;
		org.codehaus.jettison.json.JSONArray jsonArray = null;
		JSONObject getProjectId = null;
		if (properties != null) {
			couchbaseHost = properties.getProperty("couchbaseHost");
			couchbasePort = Integer.parseInt(properties
					.getProperty("couchbaseHostPort"));
			couchbaseUser = properties.getProperty("couchbaseHostUser");
			couchbasePassword = properties.getProperty("couchbaseHostPassword");
		}
		CredentialsProvider credsProvider = new BasicCredentialsProvider();
		credsProvider.setCredentials(
				new AuthScope(couchbaseHost, couchbasePort),
				new UsernamePasswordCredentials(couchbaseUser,
						couchbasePassword));
		CloseableHttpClient httpclient = HttpClients.custom()
				.setDefaultCredentialsProvider(credsProvider).build();

		HttpGet httpget = new HttpGet(
				"http://"
						+ couchbaseHost
						+ ":"
						+ couchbasePort
						+ "/syml/_design/dev_"
						+ designDooc
						+ "/_view/"
						+ viewName
						+ "?full_set=true&inclusive_end=true&stale=false&connection_timeout=60000&limit=10&skip=0");

		logger.info("Executing request " + httpget.getRequestLine());
		CloseableHttpResponse response = httpclient.execute(httpget);
		try {
			jsonObject = new JSONObject(EntityUtils.toString(response
					.getEntity()));
			jsonArray = jsonObject.getJSONArray("rows");

			for (int i = 0; i < jsonArray.length(); i++) {

				JSONObject jsonData = new JSONObject(client1.get(
						jsonArray.getJSONObject(i).getString("id").toString())
						.toString());

				try {
					Client client = new Client();
					client.setOpprtunityId(jsonData.get("opprtunityId")
							.toString());
					client.setApplicantname(jsonData.get("ClientName").toString());
					list1.add(client);
				} catch (Exception e) {
					 logger.error("Error occured while couchbase datareferal"+e.getMessage());
				}
			}

		} catch (ClientProtocolException e) {
			// TODO Auto-generated catch block
			logger.error("error in gettin the view query Data " + e.getMessage());
		} catch (IOException e) {
			// TODO Auto-generated catch block
			logger.error("error in gettin the view query Data " + e.getMessage());
		} finally {
			try {
				httpclient.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				logger.error("failed to close http client S " + e.getMessage());
			}
		}
		client1.shutdown();
		return list1;

	}


	public static Properties getCouchbasePropertiesFile() {
		Properties prop = new Properties();
		ArrayList<URI> nodes = new ArrayList<URI>();
		try {

			// getting connection parameter
			prop.load(CouchBaseOperation.class.getClassLoader()
					.getResourceAsStream("config.properties"));

		} catch (Exception e) {
			logger.error("error in getting the property file" + e.getMessage());
		}
		return prop;
	}

}
