package controllers;

import play.Logger;
import play.mvc.Controller;
import controllers.SymlListener;
import play.mvc.Result;
import views.html.*;
public class Application extends Controller {
	private static org.slf4j.Logger logger = Logger.underlying();
    public Result index() {
    	
    	logger.info("started from here ");
    
  //  SymlListener tr = new SymlListener();
  //  tr.start();
    	
        return ok(index.render("Your new application is ready."));
    }

 
}