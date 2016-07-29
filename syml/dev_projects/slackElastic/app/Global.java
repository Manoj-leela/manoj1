

import controllers.Application;
import controllers.ProposalListener;
import controllers.CrmSyncListener;
import controllers.SymlListener;
import controllers.TaskCreationListener;
import play.GlobalSettings;

public class Global extends GlobalSettings {
	@Override
	public void onStart(play.Application app) {
		// TODO Auto-generated method stub
		new SymlListener().start();
		new TaskCreationListener().start();
		new ProposalListener().start();
		new CrmSyncListener().start();
	}
	
}
