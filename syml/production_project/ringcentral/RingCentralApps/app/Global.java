
import controllers.RingCentralListener;
import play.Application;
import play.GlobalSettings;


public class Global extends GlobalSettings {
	
	@Override
	public void onStart(Application app) {
		
		// TODO Auto-generated method stub
	new RingCentralListener().start();
		System.out.println("appp started----------------");
		
	}
	
	

}
