package submitreferral;

import static play.data.Form.form;
import play.data.DynamicForm;
import play.mvc.Controller;
import play.mvc.Result;

public class SubmitReferralController extends Controller {

	
	public static Result submitreferral() {

		String referralid = request().getQueryString("referralId");

		DynamicForm dynamicForm = form().bindFromRequest();

		String leadFirstName = dynamicForm.get("fname");
		String leadLastName = dynamicForm.get("lname");
		String leadPhoneNumber = dynamicForm.get("phone");

		String leadEmail = dynamicForm.get("email");
		String leadAddress = dynamicForm.get("formatted_address");

		String referralFirstName = dynamicForm.get("reffirstname");
		String referralLastName = dynamicForm.get("reflastname");
		String referralEmail = dynamicForm.get("refemail");
		
		
		System.out.println("inside Submitreferraform Controller ");

		System.out.println("leadFirstName  : "+leadFirstName  +"  leadLastName : "+leadLastName+"  leadPhoneNumber : "+leadPhoneNumber +
				" leadEmail : "+leadEmail +" leadAddress : "+ leadAddress  +"  referralFirstName : "+ referralFirstName  +" referralEmail : "+referralEmail);

	
		
		return ok("test");
	}
	
}
