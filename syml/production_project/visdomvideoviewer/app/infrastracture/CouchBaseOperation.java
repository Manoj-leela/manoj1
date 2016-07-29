package infrastracture;

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

import org.codehaus.jettison.json.JSONObject;
import play.Logger;
import com.couchbase.client.CouchbaseClient;
import com.debortoliwines.openerp.api.FilterCollection;
import com.debortoliwines.openerp.api.ObjectAdapter;
import com.debortoliwines.openerp.api.Row;
import com.debortoliwines.openerp.api.RowCollection;
import com.debortoliwines.openerp.api.Session;
import com.fasterxml.jackson.databind.ObjectMapper;


public class CouchBaseOperation {
	private static org.slf4j.Logger logger = play.Logger.underlying();
	CouchbaseClient client1 = null;
	
	ObjectMapper object = new ObjectMapper();

	public CouchbaseClient getConnectionToCouchBaseDev() {
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

			url =prop.getProperty("couchBaseUrl");
			bucket =prop.getProperty("couchBaseBucketName");
			pass =prop.getProperty("couchBaseBucketPassword");
			//maximumRetry = new Integer(prop.getProperty("maximumRetry"));

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
			logger.debug("prop.getProperty"
					+ prop.getProperty("couchBaseBucketName"));
		} catch (Exception e) {
			logger.error("error in getting the property file" + e.getMessage());
		}
		try {

			logger.info("inside getConnectionToCouchBase method of CouchBaseOperation class");
			// url = prop.getProperty("couchBaseUrl");
			url = prop.getProperty("couchBaseUrl3");
			bucket = prop.getProperty("couchBaseBucketName");
			pass = prop.getProperty("couchBaseBucketPassword");
			maximumRetry = new Integer(prop.getProperty("maximumRetry"));

			// 1. Add one or more nodes of your cluster (exchange the IP with
			// yours)
			nodes.add(URI.create(url));
			logger.debug("connecting .....");

			client1 = getCouchbaseConnectionOne(nodes, bucket, pass,
					maximumRetry);
			if (client1 == null) {
				url = prop.getProperty("couchBaseUrl1");
				nodes.add(URI.create(url));
				client1 = getCouchbaseConnectionTwo(nodes, bucket, pass,
						maximumRetry);
				if (client1 == null) {

					
					url = prop.getProperty("couchBaseUrl2");
					nodes.add(URI.create(url));
					client1 = getCouchbaseConnectionThree(nodes, bucket, pass,
							maximumRetry);
					if(client1==null){
					// Send mail error in connecting couchbase
					logger.error("Error in Connecting Couhbase");
					}
				}
			}
		} catch (Exception e) {
			logger.error("error while connecting to couchbase " + e.getMessage());

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
				logger.error("error in connecting Couchbase one" + e.getMessage());
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
				client = new CouchbaseClient(nodes, bucketName, password);
			} catch (Exception e) {
				client = null;
				logger.error(" getCouchbaseConnectionTwo Retry..   " + retry);
				retry += 1;
				logger.error("error in connecting Couchbase two" + e.getMessage());
			}

		}
		return client;
	}
	
	public CouchbaseClient getCouchbaseConnectionThree(ArrayList<URI> nodes,String bucketName,String password,int maximumRetry){
		logger.debug("inside GetCouchbaseConectionThree method of couchbase");
			int retry=1;
			CouchbaseClient client=null;
			while(retry<=maximumRetry && client==null){
			try{
				logger.debug(nodes +""+ bucketName +""+password );
				client=new CouchbaseClient(nodes, bucketName, password);
			}catch(Exception e){
				client=null;
				logger.error(" getCouchbaseConnectionthree Retry..   "+retry);
				retry+=1;
				logger.error("error in connecting Couchbase three"+e.getMessage());
			}
			
			}
			return client;
		}
		

	public void storeDataInCouchBase(String key, JSONObject data) {
		logger.info("inside storeDataInCouchBase method of CouchBaseOperation class");
logger.debug("key "+key);
DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");            //get current date time with Calendar()
Calendar cal = Calendar.getInstance();
String currentDateTime=(dateFormat.format(cal.getTime())); 
		try {
			data.put("Submission_Date_Time1b",currentDateTime);
			
			client1 = getConnectionToCouchBase();
			client1.set(key, data.toString());
			closeCouchBaseConnection();
		} catch (Exception e) {
			logger.error("error while storing data into couchbase : " + e.getMessage());
		}

	}

	public void updatedDataInCouchBase(String key, JSONObject editData) {
		try {
			logger.info("inside editDataInCouchBase method of CouchBaseOperation class");

			client1 = getConnectionToCouchBase();

			logger.debug("editing data...");
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
	
	Session openERPSession=null;
	public Session getOdooConnection() {
		logger.debug("Inside GetOddoConnection");
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

			// ip=prop.getProperty("odooIP");
			host = prop.getProperty("odooHost1");
			logger.debug("host "+host);
			try{
			port = new Integer(prop.getProperty("odooPort"));
			logger.debug("port "+port);
			}catch(Exception e){
				logger.error("Error when reading from properties file "+e.getMessage());
			}
			db = prop.getProperty("odooDB");
			user = prop.getProperty("odooUsername1");
			pass = prop.getProperty("odoopassword1");
			maximumRetry = new Integer(prop.getProperty("maximumRetry"));
			logger.debug("");

			openERPSession = getCRMConnectionOne(host, port, db, user, pass,maximumRetry);
			
			logger.debug("openERPSession "+openERPSession);
			if (openERPSession == null) {
				logger.warn("error in getCRMConnectionOne");
				host = prop.getProperty("odooHost2");
				port = new Integer(prop.getProperty("odooPort2"));
				db = prop.getProperty("odooDB2");
				user = prop.getProperty("odooUsername2");
				pass = prop.getProperty("odoopassword2");
				

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


	public String getReferralname(String contactId,String email){
		String referralName=null;
		
		try{
			Session openERPSession=	getOdooConnection();
			logger.debug("Seesion is"+openERPSession);
		ObjectAdapter opprtunity=openERPSession.getObjectAdapter("applicant.record");
		
		FilterCollection filter=new FilterCollection();
		if(contactId!=null){
		filter.add("id", "=", contactId);}else if(email!=null){
			filter.add("email_personal", "=", email);
		}
		RowCollection row=opprtunity.searchAndReadObject(filter,new String[]{"id","applicant_name","applicant_last_name","emial_personal"});
		for (Iterator iterator = row.iterator(); iterator.hasNext();) {
			Row row2 = (Row) iterator.next();
			referralName=row2.get("applicant_name").toString()+" "+row2.get("applicant_last_name").toString();

		}

		}catch(Exception e){
			logger.error("Error in connecting openerp "+e.getMessage());
		}
		
		return referralName;
	}
	
	public Properties readConfigfile() {

		Properties prop = new Properties();
		try {
			prop.load(CouchBaseOperation.class.getClassLoader().getResourceAsStream(
					"config.properties"));
		} catch (Exception e) {
			logger.error("error in Reading config.properties file" + e.getMessage());
		}
		return prop;
	}
	
	public  List<String> getHumanResourceData(String referalId) {
		List<String> nameEmailList = new ArrayList<String>();
		try{
			Session openERPSession=	getOdooConnection();
			logger.debug("Seesion is "+openERPSession);
			logger.debug("inside getHumanResourceData");
			ObjectAdapter hrObject=openERPSession.getObjectAdapter("hr.applicant");
			
			FilterCollection filter=new FilterCollection();
			
			logger.debug("upto referal id null check");
			
			if(referalId!=null){
			filter.add("id", "=", referalId);
			}
			
			RowCollection row=hrObject.searchAndReadObject(filter,new String[]{"id","name","email_from"});
			logger.debug("beforee  for loop");
			for (Iterator iterator = row.iterator(); iterator.hasNext();) {
				
				Row row2 = (Row) iterator.next();
				nameEmailList.add(row2.get("name").toString());
				nameEmailList.add(row2.get("email_from").toString());
				logger.debug(row2.get("name").toString());
				logger.debug(row2.get("email_from").toString());
				
			}
			
			
		}catch(Exception e){
			logger.error("Unable to connect openERP "+e.getMessage());
		}
		return nameEmailList;
	}	
	public static void main(String[] args) {
	//	System.out.println(new CouchBaseOperation().getHumanResourceData("592"));
	}
	

}
