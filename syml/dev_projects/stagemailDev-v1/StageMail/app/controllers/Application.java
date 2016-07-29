package controllers;

import com.couchbase.client.CouchbaseClient;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.sendwithus.SendWithUsExample;

import play.*;
import play.libs.Json;
import play.mvc.*;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.Properties;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import views.html.*;

public class Application extends Controller {
static	CouchbaseClient client=null;
    public static Result index() {
    	new TrigerListnerClass().callListner();
        return ok("Your new application is ready.");
    }
    public static Result getLeads() throws JSONException, IOException, org.codehaus.jettison.json.JSONException {
		JsonNode json = request().body().asJson();
		if (json == null) {
			return badRequest("Expecting Json data");
		} else {
			
			org.codehaus.jettison.json.JSONObject jsonObj=new org.codehaus.jettison.json.JSONObject(json.toString());
			
			new LeadStageListner(jsonObj.toString()).run();
			
			
				return ok("Leads Notified ");
			}
		
	}
    	
}
