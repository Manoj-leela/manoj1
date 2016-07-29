package infrastracture.odoo;

import infrastracture.couchbase.CouchBaseOperation;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Properties;

import org.apache.xmlrpc.XmlRpcException;

import com.debortoliwines.openerp.api.FilterCollection;
import com.debortoliwines.openerp.api.ObjectAdapter;
import com.debortoliwines.openerp.api.OpeneERPApiException;
import com.debortoliwines.openerp.api.Row;
import com.debortoliwines.openerp.api.RowCollection;
import com.debortoliwines.openerp.api.Session;
import com.syml.Constants;

import play.Logger;



public class OdooHelpler {

	private static org.slf4j.Logger logger = play.Logger.underlying();
	Session openERPSession = null;
	public Session getOdooConnection() throws OdooException {
		logger.debug("(.) inside getOdooConnection method of OdooHelpler   class");
		int port = 0;
		String host = null;
		String db = null;
		String user = null;
		String pass = null;
		int maximumRetry = 0;
		try {
			Properties prop = readConfigfile();
			host=prop.getProperty("odooHost1");
			port= new Integer(prop.getProperty("odooPort"));
			db=prop.getProperty("odooDB");
			user = prop.getProperty("odooUsername1");
			pass = prop.getProperty("odoopassword1");
			maximumRetry = 3;
			openERPSession = getCRMConnectionOne(host, port, db, user, pass,
					maximumRetry);
			if (openERPSession == null) {
				logger.warn("error in getCRMConnectionOne");
				host =  prop.getProperty("odooHost2");
				openERPSession = getCRMConnectionTwo(host, port, db, user,
						pass, maximumRetry);
			}

		} catch (Exception e) {
			throw new OdooException("error in connectiong with odoo ", e);
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
	/**
	 * to get applicant Details By given Lead ID
	 * @param opprunityID
	 * @return
	 * @throws OdooException 
	 * @throws OpeneERPApiException
	 * @throws XmlRpcException
	 */
	public String getApplicantnamesByleadId(String opprunityID) throws OdooException{
		String applicantName = null;

		String contactId = null;
		if(opprunityID==null)
			opprunityID="0";
		if(opprunityID!=null&&opprunityID.equalsIgnoreCase(""))
			opprunityID="0";
		
		try {
			Session openERPSession =getOdooConnection();
			logger.debug("Seesion is" + openERPSession);
			ObjectAdapter opprtunity = openERPSession
					.getObjectAdapter("crm.lead");

			FilterCollection filter = new FilterCollection();
			filter.add("id", "=", opprunityID);
			RowCollection row = opprtunity.searchAndReadObject(filter,
					new String[] { "id", "app_rec_ids" });
			
			for (Iterator iterator = row.iterator(); iterator.hasNext();) {
				Row row2 = (Row) iterator.next();
				Object[] object = (Object[]) row2.get("app_rec_ids");
				contactId = object[0].toString();
			}
	
		} catch (OpeneERPApiException | XmlRpcException e) {
			 throw new OdooException("Error occured in when getting Applicant details for given Lead  id="+opprunityID,e);
		}

		try {
			Session openERPSession =getOdooConnection();
			logger.debug("Seesion is" + openERPSession);
			ObjectAdapter opprtunity = openERPSession
					.getObjectAdapter("applicant.record");

			FilterCollection filter = new FilterCollection();
			if (contactId != null) {
				filter.add("id", "=", contactId);
			}
			RowCollection row = opprtunity.searchAndReadObject(filter,
					new String[] { "id", "applicant_name",
							"applicant_last_name", "emial_personal" });
			for (Iterator<Row> iterator = row.iterator(); iterator.hasNext();) {
				Row row2 = (Row) iterator.next();
				applicantName = row2.get("applicant_name").toString() + "_"
						+ row2.get("applicant_last_name").toString();

			}

		} catch (OpeneERPApiException | XmlRpcException  e) {
			 throw new OdooException("Error occured in when getting Applicant details for given Lead  id="+opprunityID,e);
		}

		return applicantName;
	}
	
	/**
	 * get Referral Details By given lead ID
	 * @param leadId
	 * @return
	 * @throws OpeneERPApiException
	 * @throws XmlRpcException
	 * @throws OdooException 
	 */
	public List<String> getReferralnameByLeadID(String leadId) throws  OdooException{
		
		String referralID = null;
		ArrayList<String> list = new ArrayList<>();

		RowCollection leadList = null;
		try {

			if(leadId==null)
				leadId="0";
			if(leadId!=null&&leadId.equalsIgnoreCase(""))
				leadId="0";
			
			
			Session openERPSession =getOdooConnection();
			ObjectAdapter lead = openERPSession.getObjectAdapter("crm.lead");

			FilterCollection filters1 = new FilterCollection();
			filters1.add("id", "=", leadId);

			leadList = lead.searchAndReadObject(filters1, new String[] { "id",
					"referred_source" });
			logger.debug(leadList.size() + "id");

			for (Row row : leadList) {
				if (row.get("referred_source") != null) {
					Object[] object = (Object[]) row.get("referred_source");
					referralID = object[0].toString();
				}
			}
		} catch (OpeneERPApiException | XmlRpcException e) {
			 throw new OdooException("Error occured in when getting referral details for given lead  id="+leadId,e);
		}

		RowCollection referralList = null;
		try {

			Session openERPSession = getOdooConnection();
			ObjectAdapter referral = openERPSession
					.getObjectAdapter("hr.applicant");

			if(referralID!=null&&referralID.equalsIgnoreCase(""))
				referralID="0";
			
			if(referralID==null)
				referralID="0";
			FilterCollection filters1 = new FilterCollection();
			filters1.add("id", "=", referralID);

			referralList = referral.searchAndReadObject(filters1, new String[] {
					"id", "name", "partner_id", "email_from" });

			for (Row row : referralList) {
				
				list.add(row.get("email_from").toString());
				list.add(row.get("name").toString());
			}
		} catch (OpeneERPApiException | XmlRpcException e) {
			 throw new OdooException("Error occured in when getting referral details for given lead  id="+leadId,e);
		}

		

		return list;
	}
	
	
	
	/**
	 * to get applicants name for given applicant ID
	 * @param contactId
	 * @return
	 */
	public List<String> getApplicantnames(String applicantId) throws OdooException {
		String applicantName = null;
		ArrayList<String> list = new ArrayList<>();
		String applicantEmail=null;
		try {
			Session openERPSession =getOdooConnection();
			logger.debug("applicant id is" + applicantId);
			ObjectAdapter opprtunity;
			
				opprtunity = openERPSession
						.getObjectAdapter("applicant.record");
			
				if(applicantId!=null&&applicantId.equalsIgnoreCase(""))
					applicantId="0";

			FilterCollection filter = new FilterCollection();
			if (applicantId != null) {
				filter.add("id", "=", applicantId);
			}
			RowCollection row = opprtunity.searchAndReadObject(filter,
					new String[] { "id", "applicant_name",
							"applicant_last_name", "email_personal" });
			for (Iterator<Row> iterator = row.iterator(); iterator.hasNext();) {
				Row row2 = (Row) iterator.next();
				logger.debug("applicant size"+row2.get("applicant_name") +" "+row2.get("applicant_last_name").toString()+" "+row2.get("email_personal"));
				applicantName = row2.get("applicant_name") + "_"
						+ row2.get("applicant_last_name");
				applicantEmail=row2.get("email_personal").toString() ;
				list.add(applicantName);
				list.add(applicantEmail);
			}
		} catch (XmlRpcException | OpeneERPApiException e) {
			 throw new OdooException("Error occured in when getting applicant details for given applicant  id="+applicantId,e);
		}
		

		return list;
	}
	public Properties readConfigfile() throws IOException {
		Properties prop = new Properties();
		prop.load(CouchBaseOperation.class.getClassLoader().getResourceAsStream(Constants.CONFIG_FILE));
		return prop;
	}
}
