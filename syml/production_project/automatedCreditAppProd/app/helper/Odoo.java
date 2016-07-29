package helper;

import java.util.Iterator;
import java.util.Properties;

import org.codehaus.jettison.json.JSONObject;

import play.Logger;

import com.debortoliwines.openerp.api.FilterCollection;
import com.debortoliwines.openerp.api.ObjectAdapter;
import com.debortoliwines.openerp.api.Row;
import com.debortoliwines.openerp.api.RowCollection;
import com.debortoliwines.openerp.api.Session;
import com.sendwithus.SendWithUsExample;

//import controllers.OpportunityList;

public class Odoo extends Thread {
	
	private static org.slf4j.Logger logger = play.Logger.underlying();
	
	static Session openERPSession = null;
  public static final String senderEmail="support@visdom.ca";

	public Session getOdooConnectionForProduction() {
		String ip = null;
		int port = 0;
		String host = null;
		String db = null;
		String user = null;
		String pass = null;

		try {

			Properties prop = readConfigfile();

			ip = prop.getProperty("odooIP");
			try {
				port = new Integer(prop.getProperty("odooPort"));
			} catch (Exception e) {
				logger.error("error occures while connecting with odoo"+e.getMessage());
			}
			db = prop.getProperty("odooDB");
			user = prop.getProperty("odooUsername");
			pass = prop.getProperty("odoopassword");

			openERPSession = new Session(ip, port, db, user, pass);
			openERPSession.startSession();

		} catch (Exception e) {
			logger.error("error occures while connecting with odoo"+e.getMessage());
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

			logger.debug("inside getOdooConnection method of Generic Helper  class");
			Properties prop = readConfigfile();
			logger.info("stg---"
					+ prop.getProperty("submitReferralstageId") + "---"
					+ prop.getProperty("visdomreferralStageId") + "----"
					+ prop.getProperty("opprtunitySatgeid"));
			// ip=prop.getProperty("odooIP");
			host = prop.getProperty("odooHost1");
			try {
				port = new Integer(prop.getProperty("odooPort"));
			} catch (Exception e) {
				logger.error("error occures while connecting with odoo"+e.getMessage());
			}
			db = prop.getProperty("odooDB");
			user = prop.getProperty("odooUsername1");
			pass = prop.getProperty("odoopassword1");
			maximumRetry = new Integer(prop.getProperty("maximumRetry"));
			logger.info("");

			openERPSession = getCRMConnectionOne(host, port, db, user, pass,
					maximumRetry);
			if (openERPSession == null) {
				logger.info("error in getCRMConnectionOne");
				host = prop.getProperty("odooHost2");
				port = new Integer(prop.getProperty("odooPort2"));
				db = prop.getProperty("odooDB2");
				user = prop.getProperty("odooUsername2");
				pass = prop.getProperty("odoopassword2");

				openERPSession = getCRMConnectionTwo(host, port, db, user,
						pass, maximumRetry);

				if (openERPSession == null) {
					logger.info("error in getCRMConnectionTwo");
				} else {
					logger.info("connection successful");
				}

			}

		} catch (Exception e) {

			logger.error("error occures while connecting with odoo"+e.getMessage());
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
				logger.info(" GetCRMConectionOne  Retry..    " + retry);
				retry += 1;
				logger.error("error in connecting GetCRMConectionOne "
						+e.getMessage());
			}
		}
		return openERPSession;
	}

	public Session getCRMConnectionTwo(String host, int port, String db,
			String user, String pass, int maximumRetry) {

     logger.info("inside GetCRMConectionTwo method of GenericHelperClass");
		int retry = 1;
		Session openERPSession = null;
		while (retry <= maximumRetry && openERPSession == null) {
			try {
				openERPSession = new Session(host, port, db, user, pass);
				openERPSession.startSession();
			} catch (Exception e) {
				openERPSession = null;
				logger.info(" GetCRMConectionTwo  Retry..    " + retry);
				retry += 1;
				logger.error("error in connecting GetCRMConectionTwo "
						+e.getMessage());
			}
		}
		return openERPSession;
	}



	public Properties readConfigfile() {

		Properties prop = new Properties();
		try {
			prop.load(Odoo.class.getClassLoader().getResourceAsStream(
					"config.properties"));
		} catch (Exception e) {
			logger.error("error while processing readConfigfile "
					+e.getMessage());
		}
		return prop;
	}


	public String gettAPplicantName(String opportunityId) {

		String applicantName="";
	
		try {

			openERPSession = getOdooConnection();

			ObjectAdapter opprtunity = openERPSession
					.getObjectAdapter("crm.lead");

			FilterCollection filter = new FilterCollection();
			filter.add("id", "=", opportunityId);
			RowCollection row = opprtunity.searchAndReadObject(filter,
					new String[] { "id", "name", "email_from", "app_rec_ids" });
			Row rowData = row.get(0);
			Object[] object = (Object[]) rowData.get("app_rec_ids");
			ObjectAdapter applicantobject = openERPSession
					.getObjectAdapter("crm.lead");
			for (int i = 0; i < object.length; i++) {

				FilterCollection applicantFilter = new FilterCollection();
				applicantFilter.add("id", "=", object[i]);
				RowCollection applicantRow = applicantobject
						.searchAndReadObject(filter, new String[] { "id","name"
								 });
				int count =0;
				for (Iterator iterator = applicantRow.iterator(); iterator
						.hasNext();) {
					Row row2 = (Row) iterator.next();
					if(count==0){
					applicantName=	row2.get("name").toString();
					}else if(count==1){
						applicantName=applicantName+" and "+row2.get("name").toString();
					}
						count++;
					}
					
				
			
				
				
			}
		} catch (Exception e) {
			logger.error("error while processing gettAPplicantName "+e.getMessage());
		}

		return applicantName;
	}
	
	
	public String  chnageToCreditStage(String  oppId) {
		String opporinityName="";
		
		logger.info
		("inside method credit stage change");
		try{
		openERPSession = getOdooConnection();

		ObjectAdapter opprtunity = openERPSession
				.getObjectAdapter("crm.lead");

		FilterCollection filter = new FilterCollection();
		filter.add("id", "=", oppId);
		RowCollection row = opprtunity.searchAndReadObject(filter,
				new String[] {"stage_id","id","name" });
		Row rowData = row.get(0);
		opporinityName=rowData.get("name").toString();
		rowData.put("stage_id",16);
		
		opprtunity.writeObject(rowData, true);
		logger.info("Stage  chnages  To  Credit");
		
		
		}catch(Exception e1){
			try {
			String applicantNAme=	gettAPplicantName(oppId);
				JSONObject jsonSucessdata=new JSONObject();
				jsonSucessdata.put("id",oppId);
				jsonSucessdata.put("applicantName", applicantNAme);
				new RestCallClass(jsonSucessdata.toString()).start();
				new SendWithUsExample().errorInRetrievingCreditMail(applicantNAme, senderEmail, opporinityName);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				logger.error("error occures while getting applicatnt name "+e.getMessage());
			}
			logger.error("error while processing chnageToCreditStage "+e1.getMessage());			
		}
		
		return oppId;	
	}

}
