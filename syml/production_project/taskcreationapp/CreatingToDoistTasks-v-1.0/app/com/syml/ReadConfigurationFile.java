package com.syml;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.Properties;

import play.Logger;

import com.couchbase.client.deps.com.fasterxml.jackson.core.JsonParseException;
import com.couchbase.client.deps.com.fasterxml.jackson.databind.JsonMappingException;
import com.couchbase.client.java.Bucket;
import com.couchbase.client.java.bucket.BucketManager;
import com.couchbase.client.java.document.json.JsonObject;
import com.couchbase.client.java.view.DefaultView;
import com.couchbase.client.java.view.Stale;
import com.couchbase.client.java.view.ViewQuery;
import com.couchbase.client.java.view.ViewRow;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.syml.couchbase.dao.CouchbaseDaoServiceException;
import com.syml.couchbase.dao.CouchbaseUtil;
import com.syml.couchbase.dao.service.CouchBaseService;
import com.syml.dto.ProjectDetails;
import com.syml.dto.User;
import com.syml.infrastracture.CouchBaseOperation;
import com.syml.todoist.TaskCreationException;

public class ReadConfigurationFile {

	private static org.slf4j.Logger log = play.Logger.underlying();

	public static Properties readConfigProperties() throws IOException {
		log.debug("(.) inside  readConfigProperties  method of ReadConfigurationFile");
		Properties prop = new Properties();

		prop.load(CouchBaseOperation.class.getClassLoader()
				.getResourceAsStream(TodoistConstants.CONFIGURATION_FILE));
		return prop;
	}

	public static String getUserToken() throws IOException,
			TaskCreationException {
		String token = "";
		Properties properties = readConfigProperties();
		if (properties == null)
			throw new TaskCreationException(
					"Error in reading configuration file ");
		token = properties
				.getProperty(TodoistConstants.TODOIST_APPLICATION_TOKEN);

		return token;
	}

	public static List<User> getTodoistUserDetails()
			throws CouchbaseDaoServiceException, JsonParseException,
			JsonMappingException, IOException {
		ArrayList<User> listOfTodostUser = new ArrayList<User>();
		CouchBaseService couchBaseService = new CouchBaseService();
		Bucket bucket = new CouchbaseUtil().getCouchbaseClusterConnection();
		ObjectMapper objectMapper = new ObjectMapper();

		// Get bucket manager
		BucketManager bucketManager = bucket.bucketManager();

		com.couchbase.client.java.view.DesignDocument designDoc = bucketManager
				.getDesignDocument("todoistUsers");
		if (designDoc == null) {
			// Initialize design document
			designDoc = com.couchbase.client.java.view.DesignDocument
					.create("todoistUsers",
							Arrays.asList(DefaultView
									.create("todoistUsers",
											"function (doc, meta) { if (doc.userId && doc.userName && doc.token ) { emit(meta.id, null); } }")));
			// Insert design document into the bucket
			bucketManager.insertDesignDocument(designDoc);

		}

		List<ViewRow> list = bucket.query(
				ViewQuery.from("todoistUsers", "todoistUsers").stale(
						Stale.FALSE)).allRows();

		User user = null;
		for (Iterator iterator = list.iterator(); iterator.hasNext();) {
			ViewRow viewRow = (ViewRow) iterator.next();
			log.debug("referral Document " + viewRow.id());
			try {

				String userData = couchBaseService.getCouhbaseDataByKey(
						viewRow.id()).toString();
				user = objectMapper.readValue(userData, User.class);
			} catch (Exception e) {
				Logger.error("error in getting couchbase data ", e);
			}
			if (user != null) {
				listOfTodostUser.add(user);
			}

		}

		return listOfTodostUser;

	}

	public static String getTodoistLeadProjectID(String opportunityID)
			throws CouchbaseDaoServiceException, JsonParseException,
			JsonMappingException, IOException {
		Bucket bucket = new CouchbaseUtil().getCouchbaseClusterConnection();
		String projectId = "";
		// Get bucket manager
		BucketManager bucketManager = bucket.bucketManager();

		com.couchbase.client.java.view.DesignDocument designDoc = bucketManager
				.getDesignDocument("projectid" + opportunityID);
		if (designDoc == null) {
			// Initialize design document
			designDoc = com.couchbase.client.java.view.DesignDocument.create(
					"projectid" + opportunityID, Arrays.asList(DefaultView
							.create("projectid" + opportunityID,
									"function (doc, meta) { if (doc.crmId == "
											+ opportunityID
											+ ") { emit(meta.id, null); } }")));
			// Insert design document into the bucket
			bucketManager.insertDesignDocument(designDoc);

		}

		List<ViewRow> list = bucket.query(
				ViewQuery.from("projectid" + opportunityID,
						"projectid" + opportunityID).stale(Stale.FALSE))
				.allRows();
		projectId = getProjectDetails(list);

		return projectId;

	}

	static String getProjectDetails(List<ViewRow> listview)
			throws CouchbaseDaoServiceException {
		String documentID = "";
		for (Iterator iterator = listview.iterator(); iterator.hasNext();) {
			ViewRow viewRow = (ViewRow) iterator.next();
			documentID = viewRow.id();
		}

		return documentID;
	}

public	static List<ProjectDetails> getProjectList(String documentID
			) {
Logger.debug("(.) inside getProjectList  method with document Id "
						+ documentID);
		JsonObject userData = null;
		;
		List<ProjectDetails> listProjects=new ArrayList<ProjectDetails>();

		try {
			Thread.sleep(6000);
			try {
				userData = new CouchBaseService()
						.getCouhbaseDataByKey(documentID);
			} catch (CouchbaseDaoServiceException | NullPointerException e1) {
				Logger.error(
						"Error in getting TodoitProject data from Couchbase ",
						e1);
			}

			if (userData == null) {
				Thread.sleep(6000);
				userData = new CouchBaseService()
						.getCouhbaseDataByKey(documentID);

			}
			String assginUser = "";
			String projectId = "";

			log.debug("task user details : " + userData);

			try {
				assginUser = userData.getString("assigned_userName");
			} catch (NullPointerException e) {
				log.error("error : assigned UserName details not exist todoist project details  "
						+ e.getMessage());
			}
			JsonObject josnInsidedata = (JsonObject) userData.get("event_data");
			log.info("datat " + josnInsidedata);
			projectId = josnInsidedata.get("id").toString();
			String leadNAme = "";
			try {
				String leadNAmeTosplit = josnInsidedata.getString("name");

				String[] leadNameArray = leadNAmeTosplit.split("_");
				leadNAme = leadNameArray[0];

			} catch (NullPointerException e) {
				log.error("error lead name not exist in Todoist Project Data "
						+ e.getMessage());
			}

			try {
				assginUser = josnInsidedata.get("responsible_uid").toString();
			} catch (NullPointerException e) {
				log.error("error responsible_uid not exist  in Todooist Project Data "
						+ e.getMessage());
			}

			String token = getToken(assginUser);
			ProjectDetails project = new ProjectDetails();
			project.setLeadName(leadNAme);
			project.setProjectId(projectId);
			project.setAssignedUser(assginUser);
			project.setToken(token);
			listProjects.add(project);
			log.info("datat " + projectId + " token " + token);
		} catch (Exception e) {
			Logger.error("error in getting couchbase data ", e);
		}
		return listProjects;
	}

	static String getToken(String responisbleuser) throws JsonParseException,
			JsonMappingException, CouchbaseDaoServiceException, IOException {
		String token = "";
		List<User> listOfUser = getTodoistUserDetails();
		for (Iterator iterator = listOfUser.iterator(); iterator.hasNext();) {
			User user = (User) iterator.next();
			if (user.getUserName().equalsIgnoreCase(responisbleuser)) {
				token = user.getToken();
				break;
			}

		}
		return token;
	}

	/**
	 * @param args
	 * @throws JsonParseException
	 * @throws JsonMappingException
	 * @throws CouchbaseDaoServiceException
	 * @throws IOException
	 */
	public static void main(String[] args) throws JsonParseException,
			JsonMappingException, CouchbaseDaoServiceException, IOException {
		 List<ProjectDetails> list=new ArrayList<ProjectDetails>();
 String documentID = getTodoistLeadProjectID("783");

		// getProjectList(documentID, list);
		System.out.println("Project details " + list);

	}
}
