package com.syml.helper;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.SecureRandom;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import java.util.Set;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.KeyManager;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;







import org.apache.ws.commons.util.Base64;
import org.apache.xmlrpc.XmlRpcException;
import org.json.JSONException;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;



import com.couchbase.client.CouchbaseClient;
import com.debortoliwines.openerp.api.FilterCollection;
import com.debortoliwines.openerp.api.ObjectAdapter;
import com.debortoliwines.openerp.api.OpeneERPApiException;
import com.debortoliwines.openerp.api.Row;
import com.debortoliwines.openerp.api.RowCollection;
import com.debortoliwines.openerp.api.Session;
import com.debortoliwines.openerp.api.OpenERPXmlRpcProxy.RPCProtocol;

import com.syml.address.splitAddress.Address;
import com.syml.couchbase.CouchBaseOperation;
import com.syml.mail.SendEmail;
import com.syml.openerp.CreateApplicant;

public class GenericHelperClass extends Thread {

	
	
	public GenericHelperClass(){
		
	}
	int id=0;
	JSONObject jsonObject=null;
public GenericHelperClass(int id, JSONObject jsonObject){
		this.id=id;
		this.jsonObject=jsonObject;
	}



@Override
public void run() {
	// TODO Auto-generated method stub
	try{
	createNote2(id, jsonObject.toString(), "Lead Json Data", "crm.lead");
	}catch(Exception e){
		
	}
}

	static Session openERPSession = null;
	SendEmail email = new SendEmail();
	static Logger log = LoggerFactory.getLogger(GenericHelperClass.class);
	public static int propertyCounter=0; 
	
	public static void main(String[] args) throws Exception {
		System.out.println("Connection  "+new GenericHelperClass().getOdooConnection());
		System.out.println(new GenericHelperClass().getReferralnameByLeadID(400+""));
		 RowCollection referralList=null;
			try{
				
			Session	openERPSession=new GenericHelperClass().getOdooConnection();
			ObjectAdapter referral=openERPSession.getObjectAdapter("applicant.record");

			
		
		    		
		    			FilterCollection filters1=new FilterCollection();
						filters1.add("id","=",549);
					    
					   referralList= referral.searchAndReadObject(filters1, new String[]{"id","referred_source"});
					   	System.out.println(referralList.size()+"id");
		    	
		    
		    
		    
		    for (Row row : referralList){
				if(row
						.get("referred_source")!=null){
					Object [] obje=(Object[]) row.get("referred_source");
		   System.out.println(obje[0]);
				}
			}
			}catch(Exception e){
				e.printStackTrace();
			}
		    
		
	}
	  
	
	/*
	 * get the openErp Connection
	 */
	/*odooHost1=crm1.visdom.ca
			odooHost2=crm2.visdom.ca
			odooPort=8069
			odooDB=symlsys
			odooUsername=guy@visdom.ca
			 odoopassword=VisdomTesting
			odooUsername1=admin
			odoopassword1=BusinessPlatform@Visdom1*/
	
	
	//not used in prodction
	public Session getOdooConnectionForProduction() {
		String ip = null;
		int port = 0;
		String host = null;
		String db = null;
		String user = null;
		String pass = null;

		try {

			log.info("inside getOdooConnection method of Generic Helper  class");
			Properties prop = readConfigfile();

			ip =prop.getProperty("odooIP");
			try{
			port =new Integer(prop.getProperty("odooPort"));
			}catch(Exception e){
				
			}
			db = prop.getProperty("odooDB");
			user = prop.getProperty("odooUsername");
			pass =prop.getProperty("odoopassword");

			openERPSession = new Session(ip, port, db, user, pass);
			openERPSession.startSession();
			log.info("connection successful");

		} catch (Exception e) {
			log.error("error in connectiong with odoo : " + e);
		}

		return openERPSession;
	}
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

			
			log.info("inside getOdooConnection method of Generic Helper  class");
			Properties prop = readConfigfile();
System.out.println("stg---"+prop.getProperty("submitReferralstageId")+"---"+prop.getProperty("visdomreferralStageId")+"----"+prop.getProperty("opprtunitySatgeid"));
			// ip=prop.getProperty("odooIP");
			host = prop.getProperty("odooHost1");
			try{
			port = new Integer(prop.getProperty("odooPort"));
			}catch(Exception e){
				
			}
			db = prop.getProperty("odooDB");
			user = prop.getProperty("odooUsername1");
			pass = prop.getProperty("odoopassword1");
			maximumRetry = new Integer(prop.getProperty("maximumRetry"));
			System.out.println("");

			openERPSession = getCRMConnectionOne(host, port, db, user, pass,maximumRetry);
			if (openERPSession == null) {
				log.warn("error in getCRMConnectionOne");
				host = prop.getProperty("odooHost2");
				port = new Integer(prop.getProperty("odooPort2"));
				db = prop.getProperty("odooDB2");
				user = prop.getProperty("odooUsername2");
				pass = prop.getProperty("odoopassword2");
				

				openERPSession = getCRMConnectionTwo(host, port, db, user,
						pass, maximumRetry);

				if (openERPSession == null) {
					log.warn("error in getCRMConnectionTwo");
				} else {
					log.debug("connection successful");
				}

			}

		} catch (Exception e) {

			log.error("error in connectiong with odoo : " + e);
		}

		return openERPSession;
	}

	public Session getCRMConnectionOne(String host, int port, String db,
			String user, String pass, int maximumRetry) {

		log.debug("inside GetCRMConectionOne  method of GenericHelperClass");
		int retry = 1;
		Session openERPSession = null;
		while (retry <= maximumRetry && openERPSession == null) {
			try {
				openERPSession = new Session(host, port, db, user, pass);
				openERPSession.startSession();
			} catch (Exception e) {
				openERPSession = null;
				log.error(" GetCRMConectionOne  Retry..    " + retry);
				retry += 1;
				log.error("error in connecting GetCRMConectionOne " + e);
			}
		}
		return openERPSession;
	}

	public Session getCRMConnectionTwo(String host, int port, String db,
			String user, String pass, int maximumRetry) {

		log.debug("inside GetCRMConectionTwo method of GenericHelperClass");
		int retry = 1;
		Session openERPSession = null;
		while (retry <= maximumRetry && openERPSession == null) {
			try {
				openERPSession = new Session(host, port, db, user, pass);
				openERPSession.startSession();
			} catch (Exception e) {
				openERPSession = null;
				log.error(" GetCRMConectionTwo  Retry..    " + retry);
				retry += 1;
				log.error("error in connecting GetCRMConectionTwo " + e);
			}

		}
		return openERPSession;
	}

	public ObjectAdapter getOpenErpModule(String module) {

		ObjectAdapter objectAdapter = null;
		try {
			openERPSession = getOdooConnection();
			objectAdapter = openERPSession.getObjectAdapter(module);
		} catch (Exception e) {
			// TODO: handle exception
			log.error("error in connectiong with module : " + e);

		}
		return objectAdapter;
	}

	/**
	 * This method is to check for contact by email in contact module of odoo
	 * 
	 * @param :
	 * @param :
	 * @return : RowCollection
	 */
	public RowCollection lookupContactByEmail(String email, String module)
			throws Exception {

		log.info("inside lookupContactByEmailmethod of GenericHelperClass class");

		openERPSession = getOdooConnection();

		ObjectAdapter partnerAd = openERPSession.getObjectAdapter(module);

		// add filter
		FilterCollection filters = new FilterCollection();
		
		filters.add("email", "=", email.toLowerCase());
		RowCollection partners = partnerAd.searchAndReadObject(filters,
				new String[] { "email", "name", "last_name" });
		log.debug("no. of contact with email  : " + partners.size());

		return partners;

	}

	/**
	 * This method is to check for referal-source in human-department module of
	 * odoo
	 * 
	 * @param :
	 * @param :
	 * @return : referal source id
	 */
	public int lookupReferalResource(int contactId, String module) {

		log.info("inside lookupReferalResource method of GenericHelperClass class");

		int referalSourceId = 0;
		log.debug("contact id is : " + contactId);
		openERPSession = getOdooConnection();

		try {

			ObjectAdapter hradapter = openERPSession.getObjectAdapter(module);

			FilterCollection referalFileter = new FilterCollection();
			referalFileter.add("partner_id", "=", contactId);
			RowCollection hrReferral = hradapter.searchAndReadObject(
					referalFileter, new String[] { "name", "email_from",
							"partner_id" });

			log.debug("no of record in referal resource : " + hrReferral.size());

			if (hrReferral.size() == 0) {
				log.debug("no record exist with contact id");
				return referalSourceId;

			} else {

				for (Row row : hrReferral) {

					log.debug("row id of referal source : " + row.getID());
					referalSourceId = row.getID();
					// TODloglogic to get referal source id
					log.info("maching id found in referal resource with specified contact id");

					return referalSourceId;
				}

			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return referalSourceId;

	}

	/**
	 * This method is to check for contact using data in contact module of odoo
	 * 
	 * @param :
	 * @param :
	 * @return : contact source id
	 */
	public int lookupContactID(String email, String firstName, String lastName,
			String module) {

		log.info("inside lookupContactID method of GenericHelperClass class");

		int code = 0;

		RowCollection partners;
		try {
			partners = lookupContactByEmail(email, module);

			if (partners.size() == 0) {
				log.info("no person exist with this email id");
				return code;

			} else {
				boolean referralSourceFound = false;
				for (Row row : partners) {
					if (firstName == null || firstName.equals("")
							|| firstName.isEmpty() || firstName.length() == 0) {
						log.info("first name doesnt exist");
						if (row.get("last_name").toString().equalsIgnoreCase(lastName)) {
							log.info("searching based on lastname, beacuse firstname is not specified");
							int contactId = row.getID();
							return contactId;
						}

					} else if (lastName == null || lastName.equals("")
							|| lastName.isEmpty() || lastName.length() == 0) {

						log.info("last_name doesnt exist");
						if (row.get("name").toString().equalsIgnoreCase(firstName)) {
							log.info("searching based of firstname, because lastname doesnot exist");
							int contactId = row.getID();
							log.debug("contact id in contact : " + contactId);
							return contactId;
						}
					} else {
						log.debug("both first name and last name exist");
						System.out.println("name "+row.get("name")  +" last name  "+row.get("last_name"));
						if (row.get("name").toString().trim().equalsIgnoreCase(firstName)
								&& row.get("last_name").toString().trim().equalsIgnoreCase(lastName)) {
							log.info("searching based on both firstname and lastname");
							int contactId = row.getID();
							return contactId;

						}
					}
				}
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return code;

	}

	/**
	 * This method will search conatact in contact module take its id search for
	 * referal source and take its id
	 * 
	 * @param searchParam
	 * @param module
	 * @return : referal-source id
	 * @throws Exception
	 */
	
	
	

	public int searchContactAndGetReferalID(String email, String fname,
			String lname, String module) throws Exception {
		int code = 0;
		log.info("inside searchContactAndGetReferalID method of GenericHelperClass class");

		RowCollection partners = lookupContactByEmail(email, module);
		log.debug("no. of contacts : " + partners.size());

		if (partners.size() == 0) {
			log.info("no person exist with this email id");
			return code;

		} else {
			boolean referralSourceFound = false;
			for (Row row : partners) {
				if (fname == null || fname.equals("") || fname.isEmpty()
						|| fname.length() == 0) {
					log.info("first name doesnt exist");
					if (row.get("last_name").toString().equalsIgnoreCase(lname)) {
						log.info("searching with lastname since firstname doesnot exist");
						int contactId = row.getID();
						log.debug("contact id in contact : " + contactId);
						// TODO logic to get referal source id
						int checkReferalResource = lookupReferalResource(
								contactId, "hr.applicant");

						log.debug("check referal resource value : "
								+ checkReferalResource);
						if (checkReferalResource == 0) {
							return code;
						} else {
							return checkReferalResource;
						}

					}
				} else if (lname == null || lname.equals("") || lname.isEmpty()
						|| lname.length() == 0) {
					log.info("last_name doesnt exist");
					if (row.get("name").toString().equalsIgnoreCase(fname)) {
						log.info("searching based on firstname since lastname doesnot exist");
						int contactId = row.getID();
						log.debug("contact id in contact : " + contactId);
						// TODO logic to get referal source id
						int checkReferalResource = lookupReferalResource(
								contactId, "hr.applicant");
						log.debug("check referal resource value : "
								+ checkReferalResource);
						if (checkReferalResource == 0) {
							return code;
						} else {
							return checkReferalResource;
						}

					}
				} else {
					log.info("both first name and last name exist");
					if (row.get("name").toString().equalsIgnoreCase(fname)
							&& row.get("last_name").toString().equalsIgnoreCase(lname)) {
						int contactId = row.getID();
						log.debug("contact id in contact : " + contactId);
						// TODO logic to get referal source id
						int checkReferalResource = lookupReferalResource(
								contactId, "hr.applicant");
						log.debug("check referal resource value : "
								+ checkReferalResource);
						if (checkReferalResource == 0) {
							return code;
						} else {
							return checkReferalResource;
						}

					}
				}
			}
		}

		return code;

	}

	/**
	 * This method is used to get state code id
	 * 
	 * @param propAdd
	 * @param module
	 * @return : state code id
	 */

	public int getStateCode(HashMap propAdd, String module) {

		log.info("inside getStateCode method of GenericHelperClass class");

		int stateid = 0;
		ObjectAdapter partnerAd;
		openERPSession = getOdooConnection();
		try {

			String code = (String) propAdd.get("Province");
			log.debug("state code : " + code);
			partnerAd = openERPSession.getObjectAdapter("res.country.state");

			// add filter
			FilterCollection filters = new FilterCollection();

			filters.add("code", "=", code);

			RowCollection partners = partnerAd.searchAndReadObject(filters,
					new String[] { "name", "code", "country_id" });

			log.debug("state exist with specified code : " + partners.size());

			for (Row row : partners) {

				log.debug("state id : " + row.getID());
				stateid = row.getID();
			}
			return stateid;
		} catch (Exception e) {
			log.error("error in spliting address "+e);
		}

		return stateid;
	}

	/**
	 * This method is used to look for lead based on email exist or not
	 * 
	 * @return size : no. of record found
	 */
	public int lookupLeadByEmail(String email) {

		log.info("inside lookupLeadByEmail method of GenericHelperClass class");
		int size = 0;
		openERPSession = getOdooConnection();
		try {
			ObjectAdapter Leadpartner = openERPSession
					.getObjectAdapter("crm.lead");

			// search lead first by email
			FilterCollection filter = new FilterCollection();
			filter.add("email_from", "=", email);

			RowCollection lead = Leadpartner.searchAndReadObject(filter,
					new String[] { "name", "code", "country_id" });

			log.debug("lead exist with email : " + lead.size());

			size = lead.size();

			return size;

		} catch (Exception e) {
			// TODO Auto-generated catch block
			log.error("error in lookupLeadby email "+e);
		}
		return size;

	}

	public int lookupLeadAddress(HashMap propAdd) {
		log.info("inside lookupLeadAddress method of GenericHelperClass class");
		int size = 0;
		openERPSession = getOdooConnection();
		try {
			ObjectAdapter Leadpartner = openERPSession
					.getObjectAdapter("crm.lead");

			// search lead first by email
			FilterCollection filter = new FilterCollection();
			filter.add("street", "=", (String) propAdd.get("address1"));
			filter.add("city", "=", (String) propAdd.get("city"));
			
			RowCollection lead = Leadpartner.searchAndReadObject(filter,
					new String[] { "street", "city" });

			log.debug("lead exist with address : " + lead.size());

			size = lead.size();

			return size;

		} catch (Exception e) {
			// TODO Auto-generated catch block
			log.error("error in lookupLeadby address "+e);
		}
		return size;

	}

	public int createRefereal(String name, String lastName, String email,
			int partner_id, String phoneNumber, String referrer, String role,
			String comanyName, String brokerNumber, int referrerToVisdomHisId,
			HashMap address, String module) {
		int rferealid = 0;

		ObjectAdapter hrApplicant;
		try {
			openERPSession = getOdooConnection();
			// openERPSession.startSession();
			hrApplicant = openERPSession.getObjectAdapter(module);

			log.info("Res.partner id : " + partner_id);
			Row newHrPartner = null;
			if (referrerToVisdomHisId == 0) {
				if (address.isEmpty()) {
					newHrPartner = hrApplicant.getNewRow(new String[] { "name",
							"email_from", "partner_id", "partner_phone",
							"partner_mobile", "company" });
				} else {
					newHrPartner = hrApplicant.getNewRow(new String[] { "name",
							"email_from", "partner_id", "partner_phone",
							"partner_mobile", "candidate_street",
							"candidate_city", "candidate_state_id",
							"candidate_zip", "company" });
				}
				newHrPartner.put("name", name+"_"+lastName);
				newHrPartner.put("email_from", email);
				newHrPartner.put("partner_id", partner_id);
				newHrPartner.put("partner_phone",brokerNumber );
				newHrPartner.put("partner_mobile", phoneNumber);
				if (!address.isEmpty()) {
					newHrPartner.put("candidate_street",
							address.get("address1"));
					newHrPartner.put("candidate_city", address.get("city"));
					newHrPartner.put("candidate_state_id",
							getStateCode(address, ""));
					newHrPartner
							.put("candidate_zip", address.get("postalcode"));

				}
				// newHrPartner.put("referred_by", referrerToVisdomHisId);
				// newHrPartner.put("role", role);
				newHrPartner.put("company", comanyName);
			} else {
				if (address.isEmpty()) {
					newHrPartner = hrApplicant.getNewRow(new String[] { "name",
							"email_from", "partner_id", "partner_phone",
							"partner_mobile", "referred_by", "company" });
				} else {
					newHrPartner = hrApplicant.getNewRow(new String[] { "name",
							"email_from", "partner_id", "partner_phone",
							"partner_mobile", "referred_by",
							"candidate_street", "candidate_city",
							"candidate_state_id", "candidate_zip", "company" });
				}
				newHrPartner.put("name", name+"_"+lastName);
				newHrPartner.put("email_from", email);
				newHrPartner.put("partner_id", partner_id);
				newHrPartner.put("partner_phone", brokerNumber);
				newHrPartner.put("partner_mobile", phoneNumber);
				newHrPartner.put("referred_by", referrerToVisdomHisId);
				if (!address.isEmpty()) {
					newHrPartner.put("candidate_street",
							address.get("address1"));
					newHrPartner.put("candidate_city", address.get("city"));
					newHrPartner.put("candidate_state_id",
							getStateCode(address, ""));
					newHrPartner
							.put("candidate_zip", address.get("postalcode"));

				}
				// newHrPartner.put("referred_by", referrerToVisdomHisId);
				// newHrPartner.put("role", role);
				newHrPartner.put("company", comanyName);
			}

			hrApplicant.createObject(newHrPartner);
			rferealid = newHrPartner.getID();
			log.info("referal id : " + rferealid);

		} catch (Exception e) {
			// TODO Auto-generated catch block
			log.error("error in creatingreferral "+e);
		}

		return rferealid;
	}

	public int createContact(String name, String lastname, String email,

	String module) {

		int resId = 0;
		ObjectAdapter hrApplicant;
		try {
			openERPSession = getOdooConnection();
			// openERPSession.startSession();
			hrApplicant = openERPSession.getObjectAdapter(module);

			Row newResPartner = hrApplicant.getNewRow(new String[] { "name",
					"last_name", "email" });
			newResPartner.put("name", name);
			newResPartner.put("last_name", lastname);
			newResPartner.put("email", email);
			hrApplicant.createObject(newResPartner);
			resId = newResPartner.getID();
			log.info("inside createcontactMethod and created contact with Partner id is "
					+ resId);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			log.error("error in creating contact" + e);
		}
		return resId;

	}

	public Properties readConfigfile() {

		Properties prop = new Properties();
		try {
			prop.load(GenericHelperClass.class.getClassLoader().getResourceAsStream(
					"config.properties"));
		} catch (Exception e) {
			log.error("error in Reading config.properties file" + e);
		}
		return prop;
	}

	public Properties readEmailConfigfile() {

		Properties prop = new Properties();
		try {
			prop.load(GenericHelperClass.class.getClassLoader().getResourceAsStream(
					"Emailconfig.properties"));
		} catch (Exception e) {
			log.error("error in Reading config.properties file" + e);
		}
		return prop;
	}

	
	public static void createNote2(int crmLeadId,String message,String subject,String module){
try{
		
		GenericHelperClass ghelper=	new GenericHelperClass();

		Properties prop=new Properties();
		try{
			prop=ghelper.readConfigfile();
		}catch(Exception e){
			
		}
		// get current date time with Calendar()
		Calendar cal = Calendar.getInstance();

		Session openErpSession = ghelper.getOdooConnection();

		try {

			ObjectAdapter objectAdapter1 = openErpSession
					.getObjectAdapter("mail.message");
		if(crmLeadId==0){
			Row row = objectAdapter1.getNewRow(new String[] { "subject","author_id",
			 "body", "date"});
			row.put("subject", subject);
			//row.put("email_from", fromEmail);
			row.put("author_id",  prop.getProperty("autherId"));
			row.put("body", message);
			row.put("date", cal.getTime());
			//row.put("model", module);
			objectAdapter1.createObject(row);
		}else{
			
			Row row = objectAdapter1.getNewRow(new String[] { "subject","author_id",
					"res_id", "model", "body", "date",});
			row.put("subject", subject);
			//row.put("email_from", fromEmail);
			row.put("author_id",  prop.getProperty("autherId"));
			row.put("res_id", crmLeadId);
			row.put("model", module);
			row.put("body", message);
			row.put("date", cal.getTime());
			//row.put("opportunity_id", crmLeadId);

			objectAdapter1.createObject(row);
			
			
		}
		
		}catch(Exception e){
			
		}
}catch(Exception e){
	
}
		
	}
	
public static void createNote(int crmLeadId, String message, String module,String subject,String fromEmail) {

	GenericHelperClass ghelper=	new GenericHelperClass();
		
	try{
		// get current date time with Calendar()
		Calendar cal = Calendar.getInstance();

		Session openErpSession = ghelper.getOdooConnection();
Properties prop=new Properties();
		try{
			prop=ghelper.readConfigfile();
		}catch(Exception e){
			
		}
		
		try {

			ObjectAdapter objectAdapter1 = openErpSession
					.getObjectAdapter("mail.message");

log.debug(" inside createNote method   res id  "+crmLeadId);
log.debug(" inside createNote method   module  "+module);
log.debug(" inside createNote method   email  "+fromEmail);
log.debug(" inside createNote method   message  "+message);
log.debug(" inside createNote method   subject  "+subject);


	if(crmLeadId==0){
		Row row = objectAdapter1.getNewRow(new String[] { "subject","author_id",
		 "body", "date"});
		row.put("subject", subject);
		//row.put("email_from", fromEmail);
		row.put("author_id", prop.getProperty("autherId"));
		row.put("body", message);
		row.put("date", cal.getTime());
		//row.put("model", module);
		objectAdapter1.createObject(row);
	}else{
		System.out.println("else");
		Row row = objectAdapter1.getNewRow(new String[] { "subject","author_id",
				"res_id", "model", "body", "date",});
		row.put("subject", subject);
		//row.put("email_from", fromEmail);
		row.put("author_id",  prop.getProperty("autherId"));
		row.put("res_id", crmLeadId);
		row.put("model", module);
		row.put("body", message);
		row.put("date", cal.getTime());
		//row.put("opportunity_id", crmLeadId);

		objectAdapter1.createObject(row);
		
	}

		} catch (Exception e) {
			// TODO Auto-generated catch block
			log.error("error in create note list" + e);
		} 
	}catch(Exception e){
		e.printStackTrace();
	}
	}
	
/*public static void main(String[] args) {
	createNote1(321,  "testing","hr.applicant", "Testing", "s@gmail.com","C:/Users/VenkyM/Desktop/test.pdf","Test12");
}*/

	public static void createNote1(int crmLeadId, String message, String module,String subject,String fromEmail,String filePath,String fileName) {
try{
		GenericHelperClass ghelper=	new GenericHelperClass();

		
		
		// get current date time with Calendar()
		Calendar cal = Calendar.getInstance();
		Properties prop=new Properties();
		try{
			prop=ghelper.readConfigfile();
		}catch(Exception e){
			
		}
		Session openErpSession = ghelper.getOdooConnection();

		try {

			ObjectAdapter objectAdapter1 = openErpSession
					.getObjectAdapter("mail.message");
			

			ObjectAdapter objectAdapter2 = openErpSession
					.getObjectAdapter("ir.attachment");

log.debug(" inside createNote method   res id  "+crmLeadId);
log.debug(" inside createNote method   module  "+module);
log.debug(" inside createNote method   email  "+fromEmail);
log.debug(" inside createNote method   message  "+message);
log.debug(" inside createNote method   subject  "+subject);


	if(crmLeadId==0){
		Row row = objectAdapter1.getNewRow(new String[] { "subject","author_id",
		 "body", "date"});
		row.put("subject", subject);
		//row.put("email_from", fromEmail);
		row.put("author_id",  prop.getProperty("autherId"));
		row.put("body", message);
		row.put("date", cal.getTime());
		//row.put("model", module);
		objectAdapter1.createObject(row);
	}else{
		//System.out.println("else");
	
		
		File file = new File(filePath);
		byte[] fileData = new byte[(int) file.length()];
	
		
	      Path path = Paths.get(filePath);
	      byte[] data = Files.readAllBytes(path);
	     
	      

		  
		double kilobytes = (file.length() / 1024);

		  //BASE64Encoder decoder = new BASE64Encoder();
		 String decodedBytes = Base64.encode(data);
	
	
	String arrString[]=filePath.split("/");
		System.out.println("file name"+arrString +" last string "+arrString[arrString.length-1]);
		Row row1=objectAdapter2.getNewRow(new String[] {"document_link","res_id","name","store_fname","datas","db_datas","attach","file_type","file_size","index_content","datas_fname","type"});
	
		row1.put("res_id", crmLeadId);
		row1.put("name", fileName);
	row1.put("datas",decodedBytes);
	row1.put("datas_fname", arrString[arrString.length-1]);
	row1.put("file_size",kilobytes);
	row1.put("type", "binary");
	row1.put("attach",true);
	

		objectAdapter2.createObject(row1);
		int attachementId=row1.getID();
		
		
		Row row = objectAdapter1.getNewRow(new String[] { "subject","author_id",
				"res_id", "model", "body", "date","attachment_ids"});
		row.put("subject", subject);
		//row.put("email_from", fromEmail);
		row.put("author_id",  prop.getProperty("autherId"));
		row.put("res_id", crmLeadId);
		row.put("model", module);
		row.put("body", message);
		row.put("date", cal.getTime());
		
		
		System.out.println("path "+filePath);
		row.putMany2ManyValue("attachment_ids",  new Object[]{attachementId},false);
		//row.put("opportunity_id", crmLeadId);

		objectAdapter1.createObject(row);
		
	
		
	}

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			log.error("error in create note list" + e);
		} 
		
}catch(Exception e){
	e.printStackTrace();
}
	}
	
	
	public int getLeadId(String firstname,String lastName,String email,HashMap address){
		


			log.info("inside getLeadId method of GenericHelperClass class");
			int id = 0;
			openERPSession = getOdooConnection();
			try {
				ObjectAdapter Leadpartner = openERPSession
						.getObjectAdapter("crm.lead");
				String name=firstname.trim()+"_ "+lastName.trim();
				// search lead first by email
				FilterCollection filter = new FilterCollection();
				
				filter.add("email_from", "=", email.trim());
				
				filter.add("name", "=",firstname+"_ "+lastName);
				RowCollection lead = Leadpartner.searchAndReadObject(filter,
						new String[] { "name", "email_from" });
				
					
					
					try{
						Row row=lead.get(0);
					
						if(lead.size()>=1){
							int addres=lookupLeadAddress(address);
							if(addres!=0){
								id=row.getID();
							}else{
								id=row.getID();
							}
						}
					}catch(Exception e){
						log.error("error in getleadid "+e);
					}
					
				//Row row=lead.get(0);
				//id = row.getID();

				log.debug("lead exist with email : " + id);
				
				return id;

			} catch (Exception e) {
				// TODO Auto-generated catch block
				log.error("error in GetLeadId  "+e);
			}
			return id;

	
	}



/** This method is used to get PropertyCounter value
 * 
 * @return : int values, property counter
 */
public static int getPropertyCounter(){
	log.debug("inside getPropertyCounter method");
	propertyCounter=propertyCounter+1;
	
	log.debug("propertycounter value : "+propertyCounter);
	
	return propertyCounter;
}

public void createMortgageAndApplicentRecord(String mortgageYesNo,String sellingYesNo,String applicantID,String condoYesNo,String address,String condofee){
	
	
	
	CreateApplicant createLead = new CreateApplicant( );
	if (mortgageYesNo != null
			&& mortgageYesNo
					.equalsIgnoreCase("yes")
			&& sellingYesNo != null
			&& sellingYesNo
					.equalsIgnoreCase("yes")) {

		log.debug("both mortgagae and selling set to yes");

		int propertyconter = GenericHelperClass
				.getPropertyCounter();
		createLead.createApplicantMortgage(
				applicantID, address,
				propertyconter,
				sellingYesNo);
		if (condoYesNo != null
				&& condoYesNo
						.equalsIgnoreCase("yes")) {
			log.debug("condo values set to yes");
			createLead
					.createApplicantProperties(
							applicantID,
							address,
							condofee,
							propertyconter,
							sellingYesNo);
		} else {
			log.debug("condo values set to no");
			createLead
					.createApplicantProperties(
							applicantID,
							address, "-1",
							propertyconter,
							sellingYesNo);

		}

	} else if (mortgageYesNo != null
			&& mortgageYesNo
					.equalsIgnoreCase("yes")
			&& sellingYesNo != null
			&& sellingYesNo
					.equalsIgnoreCase("no")) {
		log.debug("when mortgage is true and selling is false");
		createLead.createApplicantMortgage(
				applicantID, address, 0,
				sellingYesNo);
		if (condoYesNo != null
				&& condoYesNo
						.equalsIgnoreCase("yes")) {
			log.debug("condo values set to yes");
			createLead
					.createApplicantProperties(
							applicantID,
							address,
							condofee, 0,
							sellingYesNo);
		} else {
			log.debug("condo values set to no");
			createLead
					.createApplicantProperties(
							applicantID,
							address, "-1",
							0, sellingYesNo);

		}
	} else if (mortgageYesNo != null
			&& mortgageYesNo
					.equalsIgnoreCase("no")
			&& sellingYesNo != null
			&& sellingYesNo
					.equalsIgnoreCase("yes")) {
		log.debug("when mortgage is no and selling is true");
		createLead.createApplicantMortgage(
				applicantID, address, 0,
				sellingYesNo);
		if (condoYesNo != null
				&& condoYesNo
						.equalsIgnoreCase("yes")) {
			log.debug("condo values set to yes");
			createLead
					.createApplicantProperties(
							applicantID,
							address,
							condofee, 0,
							sellingYesNo);
		} else {
			log.debug("condo values set to no");
			createLead
					.createApplicantProperties(
							applicantID,
							address, "-1",
							0, sellingYesNo);

		}
	} else {
		log.debug("when mortgage is no and selling is false");
		createLead.createApplicantMortgage(
				applicantID, address, 0,
				sellingYesNo);
		if (condoYesNo != null
				&& condoYesNo
						.equalsIgnoreCase("yes")) {
			log.debug("condo values set to yes");
			createLead
					.createApplicantProperties(
							applicantID,
							address,
							condofee, 0,
							sellingYesNo);
		} else {
			log.debug("condo values set to no");
			createLead
					.createApplicantProperties(
							applicantID,
							address, "-1",
							0, sellingYesNo);

		}
	}// end of else (mortgage and selling
}

public int searchForLead(String email, String fname,
		String lname, String module) throws Exception {
	int code = 0;
	log.info("inside searchForLead method of GenericHelperClass class");

	openERPSession=getOdooConnection();
	ObjectAdapter Leadpartner = openERPSession
			.getObjectAdapter("crm.lead");

	// search lead first by email
	FilterCollection filter = new FilterCollection();
	filter.add("email_from", "=", email);


	RowCollection partners = Leadpartner.searchAndReadObject(filter,
			new String[] { "name", "code", "country_id" });

	log.debug("no. of CRM leads : " + partners.size());

	if (partners.size() == 0) {
		log.info("no person exist with this email id");
		return code;

	} else {
		boolean referralSourceFound = false;
		for (Row row : partners) {
			
				log.info("both first name and last name exist");
				if (row.get("name").toString().equalsIgnoreCase(fname+"_ "+lname)) {
					int contactId = row.getID();
					log.debug("CRMLEAD id in crmlead : " + contactId);
					// TODO logic to get referal source id
					
				
					if (contactId == 0) {
						return code;
					} else {
						return contactId;
					}

				}
			}
		}
	

	return code;

}


public void updateLeadAddress(String email, String fname,
		String lname, HashMap propAdd) {
	log.info("inside updateLeadAddress method of GenericHelperClass class");
	int size = 0;
	openERPSession = getOdooConnection();
	try {
		
		ObjectAdapter Leadpartner = openERPSession
				.getObjectAdapter("crm.lead");

		
		
		int stateid = getStateCode(propAdd, "res.country.state");
		// search lead first by email
		FilterCollection filter = new FilterCollection();
		filter.add("email_from", "=", email);
		filter.add("name", "=", fname+"_ "+lname);
	

		RowCollection lead = Leadpartner.searchAndReadObject(filter,
				new String[] { "email_from","name","street", "street2", "city",
				"state_id" });

		Row row=lead.get(0);
		
		row.put("street", (String) propAdd.get("address1"));
		row.put("street2", (String) propAdd.get("street"));
		row.put("city", (String) propAdd.get("city"));
		row.put("state_id", stateid);
		
		Leadpartner.writeObject(row,true);
		
		log.debug("Lead address updted sucessfully ");

		

	} catch (Exception e) {
		// TODO Auto-generated catch block
		log.error("error in updatte lead address "+e);
	}


}

public int getDateDifference(int crmLeadId){
	
	int days =0;
	try{
		
		log.debug("inside date difference method, the leadId is "+crmLeadId);
	  Session opSession=getOdooConnection();
	    ObjectAdapter crmLead=opSession.getObjectAdapter("crm.lead");
	    
	    FilterCollection filterCollection=new FilterCollection();
	    filterCollection.add("id","=",crmLeadId);
	    
	    RowCollection rowCollection=crmLead.searchAndReadObject(filterCollection, new String[]{"date_action","name","create_date","opportunity_id"});
	   
	    Row row=rowCollection.get(0);
	    
	    Calendar calendar1=Calendar.getInstance();
	    Calendar calendar2=Calendar.getInstance();
	    Date dateString=(Date) row.get("create_date");
	    calendar1.setTime(dateString);
	        SimpleDateFormat formatter = new SimpleDateFormat("yy/MM/dd HH:mm:ss");
	      String date2=formatter.format(calendar2.getTime());
	      String date1=formatter.format(calendar1.getTime());
	     // System.out.println("date 1"+ date2  +"dat3 "+ date1);
	        days=(int)( (formatter.parse(date2).getTime() - formatter.parse(date1).getTime())/ (1000 * 60 * 60 * 24) );
	        log.debug("difference in date------> "+ days);
	return days;
	}catch(Exception e){
		
	}
	
	return days;
	
}
public static void mai(String[] args) throws XmlRpcException, OpeneERPApiException {
	openERPSession=new GenericHelperClass().getOdooConnection();
	System.out.println(openERPSession);
	  ObjectAdapter crmLead=openERPSession.getObjectAdapter("crm.lead");
	  
		com.debortoliwines.openerp.api.FilterCollection filters11 = new com.debortoliwines.openerp.api.FilterCollection();
		filters11.add("id","=",236);
		com.debortoliwines.openerp.api.RowCollection partners11 = crmLead.searchAndReadObject(filters11, new String[]{"job_5_years","income_decreased_worried","future_family","buy_new_vehicle","lifestyle_change","financial_risk_taker","property_less_then_5_years","id","stage_id","type"});
		for (Row r:partners11){
	  r.put("stage_id", 15);
		r.put("type","opportunity");
		//r.put("id", leadId);

		crmLead.writeObject(r, true);
		}
	System.out.println();
HashMap hash=	new Address().getProperAddress("1341 Windstone Road Southwest, Rocky View County, AB T4B, Canada");
	System.out.println("hash"+hash.toString());
}

public static void main() throws JSONException, IOException {
	CouchBaseOperation cbo=new CouchBaseOperation();
CouchbaseClient	client1=cbo.getConnectionToCouchBase();






  Path path = Paths.get("C:\\Users\\VenkyM\\Desktop\\Sample12.pdf");
  byte[] data = Files.readAllBytes(path);
	HashMap hm=new HashMap();
	hm.put("birthady","12/11/2015");
	hm.put("dob","11/11/2123");
	/*hm.put("age","123");
	hm.put("phone","1234567");
	hm.put("gender","male");
*/	//hm.put("file",data);
	

	String object=(String)client1.get("Applicant_274");
	System.out.println("data "+ object);
	JSONObject jsonData = new JSONObject(object);
	//jsonData.put("FormType","mortgage");
	Set<Map.Entry<String, String>> set = hm.entrySet();
	for (Map.Entry<String, String> entry : set) {
		//jsonData.put(entry.getKey(), entry.getValue());
	}

	
	//cbo.appendDataInCouchBase("123", hm);

	//client1.set("123", jsonData.toString());

	log.debug("sending data...");
	//client1.add("123", jsonData.toString());
	//client1.set("124", jsonData.toString());


	//cbo.storeDataInCouchBase("biz1","motgageform1",hm);
	//cbo.appendDataInCouchBase("biz1", hm);
	//String object=(String)client1.get("4be153e4-e908-44e7-8f78-6c82c5c2cd8d");
	
	
System.out.println();
	System.out.println("objct is : "+object);
	JSONObject jsonObject=null;
	try {
		//jsonObject = new JSONObject(object);
	
			
		
	/*	JSONParser p = new JSONParser();
	    net.minidev.json.JSONObject o1;
		try {
			o1 = (net.minidev.json.JSONObject) p
			                    .parse(jsonObject.toString());
		
	    net.minidev.json.JSONObject o2 = (net.minidev.json.JSONObject) p
	                        .parse(jsonData.toString());
System.out.println("hello");
	    o1.merge(o2);
	    
	    
	

	
		
		System.out.println(o1.toString());
		//client1.replace("123", o1.toString());

		System.out.println(o1.toString());
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}*/
	} catch (Exception e) {
		// TODO Auto-generated catch block
		log.error("error in getting data from couchbase "+e);
	}
	client1.shutdown();
}


public String getReferralname(String referralEmail){
	String referralName=null;
	String contactId=null;
	
	 RowCollection referralList=null;
		try{
			
		Session	openERPSession=new GenericHelperClass().getOdooConnection();
		ObjectAdapter referral=openERPSession.getObjectAdapter("hr.applicant");

		
	
	    		
	    			FilterCollection filters1=new FilterCollection();
					filters1.add("email_from","=",referralEmail.toLowerCase().trim());
				    
				   referralList= referral.searchAndReadObject(filters1, new String[]{"email_from","name","partner_id"});
				   	System.out.println(referralList.size()+"id");
	    	
	    
	    
	    
	    for (Row row : referralList){
			if(row
					.get("partner_id")!=null){
				Object [] object=(Object[])row.get("partner_id");
				contactId= object[0].toString();
			}
		}
		}catch(Exception e){
			e.printStackTrace();
		}
	
	
	try{
		Session openERPSession=new GenericHelperClass().getOdooConnection();
		System.out.println("Seesion is"+openERPSession);
	ObjectAdapter opprtunity=openERPSession.getObjectAdapter("res.partner");
	
	FilterCollection filter=new FilterCollection();
	filter.add("id", "=", contactId);
	RowCollection row=opprtunity.searchAndReadObject(filter,new String[]{"id","name"});
	Row row1=row.get(0);
	referralName=row1.get("name").toString();
System.out.println("-----------------referral soursce Name--------------------- "+referralName);
	}catch(Exception e){
		
	}
	
	return referralName;
}

public String getReferralnameByLeadID(String leadId){
	String referralName=null;
	String contactId=null;
	
	String referralID=null;
	
	
	
	 RowCollection leadList=null;
		try{
			
		Session	openERPSession=new GenericHelperClass().getOdooConnection();
		ObjectAdapter lead=openERPSession.getObjectAdapter("crm.lead");

		
	
	    		
	    			FilterCollection filters1=new FilterCollection();
					filters1.add("id","=",leadId);
				    
				   leadList= lead.searchAndReadObject(filters1, new String[]{"id","referred_source"});
				   	System.out.println(leadList.size()+"id");
	    	
	    
	    
	    
	    for (Row row : leadList){
			if(row
					.get("referred_source")!=null){
				Object [] object=(Object[])row.get("referred_source");
				referralID= object[0].toString();
			}
		}
		}catch(Exception e){
			e.printStackTrace();
		}
	
	
	
	
	
	
	
	 RowCollection referralList=null;
		try{
			
		Session	openERPSession=new GenericHelperClass().getOdooConnection();
		ObjectAdapter referral=openERPSession.getObjectAdapter("hr.applicant");

		
	
	    		
	    			FilterCollection filters1=new FilterCollection();
					filters1.add("id","=",referralID);
				    
				   referralList= referral.searchAndReadObject(filters1, new String[]{"id","name","partner_id"});
				   	System.out.println(referralList.size()+"id");
	    	
	    
	    
	    
	    for (Row row : referralList){
			if(row
					.get("partner_id")!=null){
				Object [] object=(Object[])row.get("partner_id");
				contactId= object[0].toString();
			}
		}
		}catch(Exception e){
			e.printStackTrace();
		}
	
	
	try{
		Session openERPSession=new GenericHelperClass().getOdooConnection();
		System.out.println("Seesion is"+openERPSession);
	ObjectAdapter opprtunity=openERPSession.getObjectAdapter("res.partner");
	
	FilterCollection filter=new FilterCollection();
	filter.add("id", "=", contactId);
	RowCollection row=opprtunity.searchAndReadObject(filter,new String[]{"id","name"});
	Row row1=row.get(0);
	referralName=row1.get("name").toString();
System.out.println("-----------------referral soursce Name--------------------- "+referralName);
	}catch(Exception e){
		
	}
	
	return referralName;
}
}