import infrastracture.CouchBaseOperation;

import org.codehaus.jettison.json.JSONException;


import play.Application;
import play.GlobalSettings;



public class Global extends GlobalSettings {
	private static org.slf4j.Logger logger = play.Logger.underlying();
	
	@Override
	public void onStart(Application app) {
		// TODO Auto-generated method stub
		CouchBaseOperation couhBaseOperation=new CouchBaseOperation();
		try {
			couhBaseOperation.storeAdminDetailsCouhbase("admin", "admin");
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			logger.error("Error occured while error login  "+e.getMessage());
		}
		logger.debug("appp started----------------");

	}
	
	@Override
	public void onStop(Application app) {
		// TODO Auto-generated method stub
		
		logger.debug("appp stopped----------------");	
	}
}
