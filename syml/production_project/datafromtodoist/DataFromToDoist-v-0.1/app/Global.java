import play.Application;
import play.GlobalSettings;

import com.syml.infrastracture.CouchBaseOperation;



public class Global extends GlobalSettings {
	private static org.slf4j.Logger logger = play.Logger.underlying();
	
	@Override
	public void onStart(Application app) {
		CouchBaseOperation couhBaseOperation=new CouchBaseOperation();
		try {
			couhBaseOperation.storeAdminDetailsCouhbase("admin", "admin");
			
		} catch (Exception e) {
			logger.error("Error occured while error login  "+e.getMessage());
		}
		logger.debug("appp started----------------");

	}
	
	@Override
	public void onStop(Application app) {
		
		logger.debug("appp stopped----------------");	
	}
}
