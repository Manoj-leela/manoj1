package controllers;



import helper.Odoo;

import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;
import play.Logger;

import com.fasterxml.jackson.databind.JsonNode;

import play.*;
import play.mvc.*;
import views.html.*;

public class Application extends Controller {
	private static org.slf4j.Logger logger = play.Logger.underlying();
 static	JSONObject jsonData=null;

	public static Result index() {
		return ok(index.render("Your new application is ready."));
	}

	public static Result getCreditServerData() {
		JsonNode json = request().body().asJson();
		logger.debug("jsondata =------------------------------------"
				+ json);

		
		try {
			 jsonData = new JSONObject(json.toString());

			String  oppId=jsonData.get("id").toString();
		logger.info("Opp_id  is"+oppId);
			logger.info("calling credit  stage  Methods");

			new Odoo().chnageToCreditStage(oppId);
			logger.info("stage  Chnages  To  Credit");
			
			Thread.sleep(60000);
		} catch (Exception e1) {
			// TODO Auto-generated catch block
			logger.error("Data  Id  not  Found"+e1.getMessage());
		}

		return ok("");
	}
}
