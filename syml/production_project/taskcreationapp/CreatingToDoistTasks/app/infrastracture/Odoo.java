package infrastracture;

import java.util.Properties;

import org.apache.xmlrpc.XmlRpcException;

import com.debortoliwines.openerp.api.FilterCollection;
import com.debortoliwines.openerp.api.ObjectAdapter;
import com.debortoliwines.openerp.api.OpeneERPApiException;
import com.debortoliwines.openerp.api.Row;
import com.debortoliwines.openerp.api.RowCollection;
import com.debortoliwines.openerp.api.Session;

public class Odoo {
	//static Logger log = LoggerFactory.getLogger(Odoo.class);
	private static org.slf4j.Logger log = play.Logger.underlying();


static Session openERPSession = null;
	
	@SuppressWarnings("unused")
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

			ip = prop.getProperty("odooIP");

			port = new Integer(prop.getProperty("odooPort"));
			db = prop.getProperty("odooDB");
			user = prop.getProperty("odooUsername");
			pass = prop.getProperty("odoopassword");

			openERPSession = new Session(ip, port, db, user, pass);
			openERPSession.startSession();
			log.info("connection successful");

		} catch (Exception e) {
			log.error("error in connectiong with odoo : " + e.getMessage());
		}

		return openERPSession;
	}

	@SuppressWarnings("unused")
	public Session getOdooConnection() {
		log.debug("Inside GetOddoConnection");
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

			// ip=prop.getProperty("odooIP");
			host = prop.getProperty("odooHost1");
			try{
			port = new Integer(prop.getProperty("odooPort"));
			}catch(Exception e){
				log.error("error while connectiong with odoo : " + e.getMessage());	
			}
			db = prop.getProperty("odooDB");
			user = prop.getProperty("odooUsername1");
			pass = prop.getProperty("odoopassword1");
			maximumRetry = new Integer(prop.getProperty("maximumRetry"));
			log.debug("");

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

			log.error("error in connectiong with odoo : " + e.getMessage());
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
				log.error("error in connecting GetCRMConectionOne " + e.getMessage());
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
				log.error("error in connecting GetCRMConectionTwo " + e.getMessage());
			}

		}
		return openERPSession;
	}
		
	@SuppressWarnings("unused")
	public static String getReferralname(String opprtunityId){

	RowCollection referralList=null;
    String name=null;
	try{
		
	Session	openERPSession=new Odoo().getOdooConnection();
	log.debug("connection "+openERPSession);
	ObjectAdapter lead=openERPSession.getObjectAdapter("crm.lead");
	ObjectAdapter conatact=openERPSession.getObjectAdapter("res.partner");
	FilterCollection filters2=new FilterCollection();
	filters2.add("id","=",opprtunityId.trim());
    
  RowCollection   leadRow= lead.searchAndReadObject(filters2, new String[]{"id","partner_id"});
  String id=null;
	log.debug("lead size"+leadRow.size());
	Row row=leadRow.get(0);
	Object [] object=(Object[])row.get("partner_id");
	id=object[0].toString();
	
	FilterCollection filters3=new FilterCollection();
	filters3.add("id","=",id);
    
RowCollection   contactRow= conatact.searchAndReadObject(filters3, new String[]{"id","name","last_name"});
Row row2=contactRow.get(0);
name=row2.get("name").toString()+"_"+row2.get("last_name").toString();
	log.debug("applicantid "+id);
	
	
	}catch(Exception e){
		log.error("error while processing in getReferralname " + e.getMessage());
	}
	
	return name;
	}
	public Properties readConfigfile() {

		Properties prop = new Properties();
		try {
			prop.load(Odoo.class.getClassLoader().getResourceAsStream(
					"config.properties"));
		} catch (Exception e) {
		log.error("error while processing in readConfigfile " + e.getMessage());
		}
		return prop;
	}

	
	public static void main(String[] args) throws XmlRpcException, OpeneERPApiException {
		//new CreateApplicant().assignOppertunity(340, 1, 1, 1, 1, 1, 1, 1, 268);
	//new CreateApplicant().assignOppertunityPreApproval(340, 268, "1",200000+"", 2+"", 1+"", 0+"", 2+"", 7+"", 0+"",5+"", 800000+"");
		//new CreateApplicant().updateApplicantIfNotCanad("599", "566657676","Married", new Date(), true);

		Session	openERPSession = new Odoo().getOdooConnection();
       ObjectAdapter lead = openERPSession.getObjectAdapter("crm.lead");
		
		
		FilterCollection filters11 = new FilterCollection();
		 // filters11.add("customer","=",true);
		
		  filters11.add("id","=",606);

       Row ad = lead.getNewRow(new String[]{"id","stage_id"});
			ad.put("stage_id",14);
			lead.writeObject(ad,true);
	}
}
