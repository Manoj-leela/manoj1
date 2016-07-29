package controllers;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.bizsyml.infrastructure.CouchBaseOperation;
import com.bizsyml.utill.PhoneCall;
import com.bizsyml.utill.RingOut;
import com.bizsyml.utill.RingStatusObject;
import com.bizsyml.utill.Token;



import play.*;
import play.Logger.ALogger;
import play.mvc.*;

import views.html.*;

public class Application extends Controller {
	public static final long serialVersionUID = 1L;
	static ALogger log1 = play.Logger.of("application");

	public static List<String> errMsg  ;
    public static Result index() {
    	log1.info("vikash");
    	

        return ok(index.render("Your new application is ready."));
    }
    
    public static Result getCallLogs(){
    	log1.info("Rendering homepage.");
		// TODO Auto-generated method stub
		boolean isFormValid = true;
		if(isFormValid){
		errMsg = new ArrayList<String>();
			try {
				Double duration = PhoneCall.getDuration();
				
				
				
				
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				errMsg.add(e.getMessage());
			}
		}
		if (isFormValid) {
						try{
							
							// Generate Access Token
							String  accessToken= Token.getInstance().getAccess_token();
							log1.info("accessToken"+accessToken);
							
						
							//System.out.println(Caller);
							//System.out.println(Callee);
							RingStatusObject statusObject = RingOut.MakeCall(accessToken,"","" );
						
						}
						catch(Exception e){
							e.printStackTrace();
							errMsg.add(e.getMessage());
						}
					}
		         return ok(index.render("Your new application is ready."));

					
	}
}
