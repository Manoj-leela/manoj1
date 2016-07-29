package infrastracture;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URL;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Random;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

import net.spy.memcached.internal.OperationFuture;

import org.codehaus.jettison.json.JSONArray;
import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;

import play.Logger;

import com.couchbase.client.CouchbaseClient;
import com.couchbase.client.protocol.views.DesignDocument;
import com.couchbase.client.protocol.views.Query;
import com.couchbase.client.protocol.views.View;
import com.couchbase.client.protocol.views.ViewDesign;
import com.couchbase.client.protocol.views.ViewResponse;
import com.couchbase.client.protocol.views.ViewRow;
import com.fasterxml.jackson.databind.ObjectMapper;

import controllers.User;

public class CouchBaseOperation {
	
	private static org.slf4j.Logger logger = play.Logger.underlying();
	CouchbaseClient client1 = null;
	
	ObjectMapper object = new ObjectMapper();

	public CouchbaseClient getConnectionToCouchBaseForProduction() {
		String url = null;
		String bucket = null;
		String pass = null;
		int maximumRetry = 0;

		Properties prop = new Properties();
		ArrayList<URI> nodes = new ArrayList<URI>();
		try {

			// getting connection parameter
			prop.load(CouchBaseOperation.class.getClassLoader()
					.getResourceAsStream("config.properties"));

		} catch (Exception e) {
			logger.error("error in getting the property file" +e.getMessage());
		}

		try {
			logger.info("inside getConnectionToCouchBase method of CouchBaseOperation class");
			// url = prop.getProperty("couchBaseUrl");

			url = prop.getProperty("couchBaseUrl");
			bucket = prop.getProperty("couchBaseBucketName");
			pass = prop.getProperty("couchBaseBucketPassword");
			// maximumRetry = new Integer(prop.getProperty("maximumRetry"));

			// 1. Add one or more nodes of your cluster (exchange the IP with
			// yours)
			nodes.add(URI.create(url));
			logger.debug("connecting .....");

			// =======

			client1 = new CouchbaseClient(nodes, bucket, pass);
		} catch (IOException e) {
			// TODO Please confirm with Shan the config of Production Couchbase
			// instance ... Should there be a failover address in catch block
			// and error in a final block?
			logger.error("error while connecting to couchbase"+e.getMessage());

		}
		return client1;
	}

	public CouchbaseClient getConnectionToCouchBase() {
		String url = null;
		String bucket = null;
		String pass = null;
		int maximumRetry = 0;
		Properties prop = new Properties();
		ArrayList<URI> nodes = new ArrayList<URI>();
		try {

			// getting connection parameter
			prop.load(CouchBaseOperation.class.getClassLoader()
					.getResourceAsStream("config.properties"));

		} catch (Exception e) {
			logger.error("error in getting the property file" +e.getMessage());
		}

		try {

			logger.info("inside getConnectionToCouchBase method of CouchBaseOperation class");
			// url = prop.getProperty("couchBaseUrl");
			url = prop.getProperty("couchBaseUrl3");
			bucket = prop.getProperty("couchBaseBucketName");
			pass = prop.getProperty("couchBaseBucketPassword");
			maximumRetry = new Integer(prop.getProperty("maximumRetry"));

			// 1. Add one or more nodes of your cluster (exchange the IP with
			// yours)
			nodes.add(URI.create(url));
			logger.debug("connecting .....");

			client1 = getCouchbaseConnectionOne(nodes, bucket, pass,
					maximumRetry);
			if (client1 == null) {
				url = prop.getProperty("couchBaseUrl1");
				bucket = prop.getProperty("couchBaseBucketName2");
				pass = prop.getProperty("couchBaseBucketPassword2");
				ArrayList<URI> nodes1 = new ArrayList<URI>();
				nodes1.add(URI.create(url));
				logger.debug(url + " " + bucket + ""
						+ prop.getProperty("couchBaseBucketPassword2"));

				client1 = getCouchbaseConnectionTwo(nodes1, bucket, pass,
						maximumRetry);
				if (client1 == null) {

					
					url = prop.getProperty("couchBaseUrl2");
					bucket = prop.getProperty("couchBaseBucketName2");
					pass = prop.getProperty("couchBaseBucketPassword2");
				 nodes1 = new ArrayList<URI>();
					nodes1.add(URI.create(url));
					logger.debug(url + " " + bucket + ""
							+ prop.getProperty("couchBaseBucketPassword2"));

					client1 = getCouchbaseConnectionThree(nodes1, bucket, pass,
							maximumRetry);
					if(client1==null){
					// Send mail error in connecting couchbase
					logger.error("Error in Connecting Couhbase");
					}
				}
			}
		} catch (Exception e) {
			logger.error("error while connecting to couchbase "+e.getMessage());

		}

		return client1;
	}

	public CouchbaseClient getCouchbaseConnectionOne(ArrayList<URI> nodes,
			String bucketName, String password, int maximumRetry) {

		logger.debug("inside GetcoonectionOne  method of couchbase");
		int retry = 1;
		CouchbaseClient client = null;
		while (retry <= maximumRetry && client == null) {
			try {
				client = new CouchbaseClient(nodes, bucketName, password);
			} catch (Exception e) {
				client = null;
				logger.error(" getCouchbaseConnectionOne  Retry..    " + retry);
				retry += 1;
				logger.error("error in connecting Couchbase one"+e.getMessage());
			}

		}
		return client;
	}

	public CouchbaseClient getCouchbaseConnectionTwo(ArrayList<URI> nodes,
			String bucketName, String password, int maximumRetry) {
		logger.debug("inside GetCouchbaseConectionTwo  method of couchbase");
		int retry = 1;
		CouchbaseClient client = null;
		while (retry <= maximumRetry && client == null) {
			try {
				logger.debug(nodes + "" + bucketName + "" + password);
				client = new CouchbaseClient(nodes, bucketName, password);
			} catch (Exception e) {
				client = null;
				logger.error(" getCouchbaseConnectionTwo Retry..   " + retry);
				retry += 1;
				logger.error("error in connecting Couchbase two"+e.getMessage());
			}

		}
		return client;
	}
	public CouchbaseClient getCouchbaseConnectionThree(ArrayList<URI> nodes,String bucketName,String password,int maximumRetry){
		logger.debug("inside GetCouchbaseConectionThree  method of couchbase");
			int retry=1;
			CouchbaseClient client=null;
			while(retry<=maximumRetry && client==null){
			try{
				logger.debug(nodes +""+ bucketName +""+password );
				client=new CouchbaseClient(nodes, bucketName, password);
				
			}catch(Exception e){
				client=null;
				logger.error(" getCouchbaseConnectionThree Retry..   "+retry);
				retry+=1;
				logger.error("error in connecting Couchbase three"+e.getMessage());
			}
			
			}
			return client;
		}

	public void storeDataInCouchBase(String key, JSONObject data) {
		logger.info("inside storeDataInCouchBase method of CouchBaseOperation class");

		DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss"); // get
																				// current
																				// date
																				// time
																				// with
																				// Calendar()
		Calendar cal = Calendar.getInstance();
		String currentDateTime = (dateFormat.format(cal.getTime()));
		try {
			data.put("Submission_Date_Time1b", currentDateTime);

			client1 = getConnectionToCouchBase();
			client1.set(key, data.toString());
			closeCouchBaseConnection();
		} catch (Exception e) {
			logger.error("error while storing data into couchbase : " +e.getMessage());
		}

	}

	public void updatedDataInCouchBase(String key, JSONObject editData) {
		try {
			logger.info("inside editDataInCouchBase method of CouchBaseOperation class");

			client1 = getConnectionToCouchBase();

			DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss"); // get
																					// current
																					// date
																					// time
																					// with
																					// Calendar()
			Calendar cal = Calendar.getInstance();
			String currentDateTime = (dateFormat.format(cal.getTime()));
			editData.put("Submission_Date_Time1b", currentDateTime);

			logger.debug("editing data...");
			client1.replace(key, editData.toString());

			closeCouchBaseConnection();

		} catch (Exception e) {
			logger.error("error while editing data into couchbase : "+e.getMessage());
		}
	}

	public void updateTaskInCouchBase(String key, JSONObject editData) {

		try {
			logger.info("inside editDataInCouchBase method of CouchBaseOperation class");

			client1 = getConnectionToCouchBase();
			JSONObject json = null;

			try {
				json = new JSONObject(client1.get(key).toString());
				Iterator<String> keysItr = editData.keys();

				while (keysItr.hasNext()) {
					String keyString = keysItr.next();
					Object value = editData.get(keyString);
					json.put(keyString, value);
				}
			} catch (Exception e) {
				logger.error("error while editing data into couchbase : "+e.getMessage());
			}
			logger.debug("editing data...");
			if (json != null) {
				DateFormat dateFormat = new SimpleDateFormat(
						"yyyy/MM/dd HH:mm:ss"); // get current date time with
												// Calendar()
				Calendar cal = Calendar.getInstance();
				String currentDateTime = (dateFormat.format(cal.getTime()));
				json.put("Submission_Date_Time1b", currentDateTime);

				client1.replace(key, json.toString());

			} else {
				DateFormat dateFormat = new SimpleDateFormat(
						"yyyy/MM/dd HH:mm:ss"); // get current date time with
												// Calendar()
				Calendar cal = Calendar.getInstance();
				String currentDateTime = (dateFormat.format(cal.getTime()));
				editData.put("Submission_Date_Time1b", currentDateTime);

				client1.set(key, editData.toString());

			}

			closeCouchBaseConnection();

		} catch (Exception e) {
			logger.error("error while editing data into couchbase : "+e.getMessage());
		}
	}

	public void closeCouchBaseConnection() {
		logger.debug("closing connection");
		client1.shutdown(9000l, TimeUnit.MILLISECONDS);

	}

	// --------------------------------------Todoist
	// adding,create,update,delete---> project,task,notes ,lable methods
	// ----------------------------------------------
	public void splitEvents(String eventName, String id, JSONObject eventData,
			String userId) throws JSONException {

		switch (eventName) {
		case "project:added":
			addProjectName(eventData);
			eventData = getModefiedUserData(eventData, userId);
			eventData.put("Type", "ProjectToDo");

			storeDataInCouchBase("tasksproject_" + id, eventData);

			break;

		case "project:updated":

			addProjectName(eventData);

			eventData = getModefiedUserData(eventData, userId);
			eventData.put("Type", "ProjectToDo");

			updatedDataInCouchBase("tasksproject_" + id, eventData);
			break;

		case "project:deleted":
			addProjectName(eventData);

			eventData = getModefiedUserData(eventData, userId);
			eventData.put("Type", "ProjectToDo");

			updatedDataInCouchBase("tasksproject_" + id, eventData);

			break;

		case "project:archived":
			addProjectName(eventData);

			eventData = getModefiedUserData(eventData, userId);
			eventData.put("Type", "ProjectToDo");

			updatedDataInCouchBase("tasksproject_" + id, eventData);
			break;

		case "item:added":
			
			eventData.put("Type", "TaskToDo");
			eventData.put("Type_TaskUncompleted", "Type_TaskUncompleted");
			eventData.put("note1", "");
			

			try {

				JSONObject eventJsondata = eventData
						.getJSONObject("event_data");
				int taskId = eventJsondata.getInt("id");

				eventData.put("Type_TaskUncompleted1", taskId);
				eventJsondata.put("responsible_userName", "");
				JSONArray jsonArray=new JSONArray();
				eventData.put("NotesContent",jsonArray);
				eventData.put("event_data",eventJsondata);
			} catch (Exception e) {
				// TODO: handle exception
				logger.error("error while performing event on daocument in couchbase : "+e.getMessage());
			}
			
			
			eventData = getModefiedUserData(eventData, userId);
			getLabels(eventData);
			

			storeDataInCouchBase("tasksitem_" + id, eventData);

			break;

		case "item:updated":
			
			
			
			eventData.put("Type", "TaskToDo");
			eventData.put("Type_TaskUncompleted", "Type_TaskUncompleted");
			eventData.put("note1", "");
			
		
			try {

				JSONObject eventJsondata = eventData
						.getJSONObject("event_data");
				int taskId = eventJsondata.getInt("id");

				eventData.put("Type_TaskUncompleted1", taskId);
				eventJsondata.put("responsible_userName", "");
				JSONArray jsonArray=new JSONArray();
				eventData.put("NotesContent",jsonArray);
				eventData.put("event_data",eventJsondata);
			} catch (Exception e) {
				// TODO: handle exception
				logger.error("error while adding content of json"+e.getMessage());
			}
			eventData = getModefiedUserData(eventData, userId);
			getLabels(eventData);
		
			updateTaskInCouchBase("tasksitem_" + id, eventData);

			break;

		case "item:completed":
			eventData.put("Type_TaskCompleted", "Type_TaskCompleted");

			eventData.put("Type", "TaskToDo");
			eventData.put("note1", "");
		
			try {

				JSONObject eventJsondata = eventData
						.getJSONObject("event_data");
				int taskId = eventJsondata.getInt("id");

				eventData.put("Type_TaskCompleted1", taskId);
				eventJsondata.put("responsible_userName", "");
				JSONArray jsonArray=new JSONArray();
				eventData.put("NotesContent",jsonArray);
				eventData.put("event_data",eventJsondata);
			} catch (Exception e) {
				// TODO: handle exception
				logger.error("error while adding content of json"+e.getMessage());
			}
		
			eventData = getModefiedUserData(eventData, userId);
			getLabels(eventData);
			updateTaskInCouchBase("tasksitem_" + id, eventData);

			break;

		case "item:uncompleted":
			
			
			eventData.put("Type", "TaskToDo");
			eventData.put("Type_TaskUncompleted", "Type_TaskUncompleted");
			eventData.put("note1", "");
			

			try {

				JSONObject eventJsondata = eventData
						.getJSONObject("event_data");
				int taskId = eventJsondata.getInt("id");

				eventData.put("Type_TaskUncompleted1", taskId);
				eventJsondata.put("responsible_userName", "");
				JSONArray jsonArray=new JSONArray();
				eventData.put("NotesContent",jsonArray);
				eventData.put("event_data",eventJsondata);
			} catch (Exception e) {
				// TODO: handle exception
				logger.error("error while adding content of json"+e.getMessage());
			}
			eventData = getModefiedUserData(eventData, userId);
			

			updateTaskInCouchBase("tasksitem_" + id, eventData);

			break;

		case "item:deleted":
			eventData = getModefiedUserData(eventData, userId);

			eventData.put("Type", "TaskToDo");

			updateTaskInCouchBase("tasksitem_" + id, eventData);

			break;

		case "note:added":
			eventData = getModefiedUserData(eventData, userId);
			try {

				JSONObject obj = (JSONObject) eventData.get("event_data");

				getNoteToTasks(obj.get("item_id").toString().trim(),
						obj.get("id").toString().trim(), obj.get("content")
								.toString().trim());
			} catch (Exception e) {
				logger.error("Error is getting while Modifying UserData"+e.getMessage());
			}
			eventData.put("Type", "NoteToDo");

			storeDataInCouchBase("tasksnote_" + id, eventData);

			break;

		case "note:updated":
			eventData = getModefiedUserData(eventData, userId);

			try {

				JSONObject obj = (JSONObject) eventData.get("event_data");

				getNoteToTasks(obj.get("item_id").toString().trim(),
						obj.get("id").toString().trim(), obj.get("content")
								.toString().trim());
			} catch (Exception e) {
				logger.error("error while performing Coucgbaseoperation "+e.getMessage());
			}
			eventData.put("Type", "NoteToDo");

			updatedDataInCouchBase("tasksnote_" + id, eventData);

			break;

		case "note:deleted":
			eventData = getModefiedUserData(eventData, userId);
			try {

				JSONObject obj = (JSONObject) eventData.get("event_data");

				getNoteToTasks(obj.get("item_id").toString().trim(),
						obj.get("id").toString().trim(), obj.get("content")
								.toString().trim());
			} catch (Exception e) {
				logger.error("error while performing deleting item of Coucgbaseoperation "+e.getMessage());
			}
			eventData.put("Type", "NoteToDo");

			updatedDataInCouchBase("tasksnote_" + id, eventData);

			break;

		case "label:added":
			eventData = getModefiedUserData(eventData, userId);
			eventData.put("Type", "LabelToDo");

			storeDataInCouchBase("taskslabel_" + id, eventData);

			break;

		case "label:updated":
			eventData = getModefiedUserData(eventData, userId);
			eventData.put("Type", "LabelToDo");

			updatedDataInCouchBase("taskslabel_" + id, eventData);

			break;

		case "label:deleted":
			eventData = getModefiedUserData(eventData, userId);
			eventData.put("Type", "LabelToDo");

			updatedDataInCouchBase("taskslabel_" + id, eventData);

			break;

		default:
			break;
		}

	}

	// add project name to before storing TodoitProject to couchbase
	public void addProjectName(JSONObject eventData) throws JSONException {

		JSONObject insideEventdata = (JSONObject) eventData.get("event_data");
		try {
			if (insideEventdata.getInt("id") != 0) {
				eventData.put("ProjectName_", insideEventdata.get("name")
						.toString());
			}
		} catch (Exception e) {
			logger.error("error while performing addProjectName operation "+e.getMessage());
		}
	}

	// adding username,assigned username, responsible username to
	// tasks,project,lebals,notes
	public JSONObject getModefiedUserData(JSONObject eventData, String userId)
			throws JSONException {

		String userName = getUserName(userId);
		if (userName != null) {

			JSONObject insideEventdata = (JSONObject) eventData
					.get("event_data");

			/*
			 * try { switch (eventData.get("event_name").toString()) { case
			 * "note:added": if (insideEventdata.getInt("id") != 0) {
			 * 
			 * HashSet set = new HashSet();
			 * set.add(getProjectName(insideEventdata.getInt("id") + ""));
			 * insideEventdata.put("Notes", set); insideEventdata.put( "Notes",
			 * getProjectName(insideEventdata .getInt("content") + ""));
			 * 
			 * } break;
			 * 
			 * case "note:updated":
			 * 
			 * if (insideEventdata.getInt("id") != 0) {
			 * 
			 * HashSet set = new HashSet();
			 * set.add(getProjectName(insideEventdata.getInt("id") + ""));
			 * insideEventdata.put("Notes", set); insideEventdata.put( "Notes",
			 * getProjectName(insideEventdata .getInt("content") + ""));
			 * 
			 * }
			 * 
			 * break; case "note:deleted":
			 * 
			 * if (insideEventdata.getInt("id") != 0) {
			 * 
			 * HashSet set = new HashSet();
			 * set.add(getProjectName(insideEventdata.getInt("id") + ""));
			 * insideEventdata.put("Notes", set); insideEventdata.put( "Notes",
			 * getProjectName(insideEventdata .getInt("content") + ""));
			 * 
			 * }
			 * 
			 * break; default: break; } } catch (Exception e) {
			 * e.printStackTrace(); }
			 */

			/*
			 * try { if (insideEventdata.getInt("project_id") != 0) {
			 * eventData.put("ProjectName",
			 * getProjectName(insideEventdata.getInt("project_id") + ""));
			 * 
			 * } } catch (Exception e) { e.printStackTrace(); }
			 */
			JSONObject jsonProjectdata = null;
			String projectId = "0";
			CouchbaseClient clint = null;
			try {
				if (insideEventdata.getInt("project_id") != 0) {
					eventData.put("ProjectName",
							getProjectName(insideEventdata.getInt("project_id")
									+ ""));

					try {
						projectId = insideEventdata.getInt("project_id") + "";
						clint = getConnectionToCouchBase();
						jsonProjectdata = new JSONObject(clint.get(
								"tasksproject_"
										+ insideEventdata.getInt("project_id"))
								.toString());

					} catch (Exception e) {
						logger.error("error while performing addProjectName operation "+e.getMessage());
					}
				}
			} catch (Exception e) {
				logger.error("error while performing addProjectName operation "+e.getMessage());
			}
			try {
				if (insideEventdata.getInt("responsible_uid") != 0) {
					
					insideEventdata.put(
							"responsible_userName",
							getUserName(insideEventdata
									.getInt("responsible_uid") + ""));

					jsonProjectdata.put(
							"assigned_userName",
							getUserName(insideEventdata
									.getInt("responsible_uid") + ""));
				}
			} catch (Exception e) {
				logger.error("error while performing addProjectName operation "+e.getMessage());
			}

			try {
				if (insideEventdata.getInt("assigned_by_uid") != 0) {
					eventData.put(
							"assigned_userName",
							getUserName(insideEventdata
									.getInt("assigned_by_uid") + ""));

				}

			} catch (Exception e) {
				logger.error("error while performing addProjectName operation "+e.getMessage());
			}
			try {
				updatedDataInCouchBase("tasksproject_" + projectId,
						jsonProjectdata);
			} catch (Exception e) {
				logger.error("error while performing addProjectName operation "+e.getMessage());
			}

			insideEventdata.put("username", userName);

			eventData.put("username", userName);
			eventData.put("event_data", insideEventdata);

		}

		logger.debug("-------------------------inside Getmodiefied user data-------------------------------"
						+ eventData.get("event_data"));
		return eventData;

	}

	// adding lables to the Tasks
	public void getLabels(JSONObject eventData) {
		JSONObject jsn = null;
		org.codehaus.jettison.json.JSONArray arrayOflables = null;
		org.codehaus.jettison.json.JSONArray arrayOflables1 = new org.codehaus.jettison.json.JSONArray();
		try {

			client1 = getConnectionToCouchBase();
			jsn = (JSONObject) eventData.get("event_data");
			arrayOflables = (org.codehaus.jettison.json.JSONArray) jsn
					.get("labels");
			logger.debug("-------------inside  get lables-----------"
					+ arrayOflables);

			for (int i = 0; i < arrayOflables.length(); i++) {
				try {

					JSONObject jsn1 = new JSONObject(client1.get(
							"taskslabel_" + arrayOflables.get(i)).toString());
					logger.debug("------------- get lables-----------"
							+ client1.get("taskslabel_"
									+ arrayOflables.get(i).toString()));

					JSONObject json = (JSONObject) jsn1.get("event_data");
					arrayOflables1.put(json.get("name").toString());
				} catch (Exception e) {
					logger.error("error while performing  operation in getLabels"+e.getMessage());
				}
			}
			eventData.put("LabelsName", arrayOflables1);
			client1.shutdown();
		} catch (Exception e) {
			logger.error("error while performing  operation in getLabels"+e.getMessage());
		}
	}

	// adding number of notes and notescontent to Tasks
	public void getNoteToTasks(String taskId, String id, String content) {

		JSONObject jsn = null;
		try {

			logger.debug("tasksitem_" + taskId.trim());

			client1 = getConnectionToCouchBase();
			jsn = new JSONObject(client1.get("tasksitem_" + taskId.trim())
					.toString());
			org.codehaus.jettison.json.JSONArray arrayOfNotes = null;
			org.codehaus.jettison.json.JSONArray arrayOfContents = null;
			try {
				arrayOfNotes = (org.codehaus.jettison.json.JSONArray) jsn
						.get("Notes");
				arrayOfContents = (org.codehaus.jettison.json.JSONArray) jsn
						.get("NotesContent");

			} catch (Exception e) {

				arrayOfNotes = null;
				arrayOfContents = null;
				logger.error("error while performing  operation in getNoteToTasks"+e.getMessage());
			}
			logger.debug("notes-------------- " + arrayOfNotes + " arrayOfNotes "
					+ arrayOfContents + "----------json " + jsn);

			if (arrayOfNotes != null && arrayOfContents != null) {
				arrayOfContents.put(content);
				arrayOfNotes.put(id);

				logger.debug("after adding notesnotes-------------- "
						+ arrayOfNotes + " arrayOfNotes " + arrayOfContents
						+ "----------json " + jsn);

				jsn.put("Notes", arrayOfNotes);
				jsn.put("NotesContent", arrayOfContents);
				String notesdata="";
				try{
				for(int i=0;i<arrayOfContents.length();i++){
					
					notesdata=notesdata+arrayOfContents.get(i).toString()+"\n\n";
				}
				jsn.put("note1", notesdata);

				}catch(Exception e){
		logger.error("error while performing  operation in getNoteToTasks"+e.getMessage());		
				}
			} else {
				arrayOfContents = new JSONArray();
				arrayOfNotes = new JSONArray();
				arrayOfContents.put(content);
				arrayOfNotes.put(id);
				jsn.put("Notes", arrayOfNotes);
				jsn.put("NotesContent", arrayOfContents);
				jsn.put("note1",content );
			}

			closeCouchBaseConnection();
			updatedDataInCouchBase("tasksitem_" + taskId.trim(), jsn);
			// name = (String) jsonEventdata.get("name");
		} catch (Exception e) {
			logger.error("error while performing  operation in getNoteToTasks"+e.getMessage());
		}

	}

	// Get project name using Proejct id from couchbase to Add projectName in
	// Tasks
	public String getProjectName(String projectId) {
		String name = "";

		try {

			logger.debug("tasksproject_" + projectId.trim());

			client1 = getConnectionToCouchBase();
			JSONObject jsn = new JSONObject(client1.get(
					"tasksproject_" + projectId.trim()).toString());

			JSONObject jsonEventdata = (JSONObject) jsn.get("event_data");
			name = (String) jsonEventdata.get("name");
			closeCouchBaseConnection();
		} catch (Exception e) {
			logger.error("error while performing  operation in getProjectName"+e.getMessage());
		}
		return name;

	}

	// get UserName of Todoist From Coucbase to add in project, tasks,items,
	// labels and notes
	public String getUserName(String userid) {

		String name = null;
		try {

			logger.debug("Taskuser_" + userid.trim());

			client1 = getConnectionToCouchBase();
			JSONObject jsn = new JSONObject(client1.get(
					"TaskUser_" + userid.trim()).toString());
			name = (String) jsn.get("userName");
			closeCouchBaseConnection();
		} catch (Exception e) {
			logger.error("error while performing  operation in getUserName"+e.getMessage());
		}

		logger.debug("-------------------------inside getusername-------------------------------"
						+ name);
		return name;
	}

	// Fetching the perticuler TaskUser Name based UserId from Couchbase, To add
	// userName to Tasks,project,notes
	public User getPerticulerUser(String userid) {

		User user = null;
		try {

			logger.debug("Taskuser_" + userid.trim());

			client1 = getConnectionToCouchBase();
			ObjectMapper object = new ObjectMapper();
			user = object.readValue(
					(client1.get("TaskUser_" + userid.trim()).toString()),
					User.class);
			closeCouchBaseConnection();
		} catch (Exception e) {
			logger.error("error while performing  operation in getPerticulerUser"+e.getMessage());
		}

		return user;
	}

	// Fetching UserId of TodOist user Using Token and Storing TaskUser to
	// couchbase
	public boolean addingUser(String token) {
		boolean sucess = false;

		JSONObject jsonObject = null;
		String userId = null;
		String email = null;
		String userName = null;
		try {

			// URL url = new
			// URL("https://todoist.com/API/v6/login?email=assistant@visdom.ca&password=Visdom1234");
			URL url = new URL("https://todoist.com/API/v6/sync?token="
					+ token.trim()
					+ "&seq_no=0&seq_no_global=0&resource_types=[\"all\"]");

			HttpURLConnection conn = (HttpURLConnection) url.openConnection();
			conn.setDoOutput(true);
			conn.setRequestMethod("POST");

			if (conn.getResponseCode() != 200) {
				logger.debug("Failed : HTTP error code : "
						+ conn.getResponseCode());
				sucess = false;
			} else {

				BufferedReader br = new BufferedReader(new InputStreamReader(
						(conn.getInputStream())));

				logger.debug("Output from Server .... \n");
				String output;
				while ((output = br.readLine()) != null) {
					// logger.debug(output);
					jsonObject = new JSONObject(output);
					// token=jsonObject.getString("api_token");
					JSONObject userJson = new JSONObject(jsonObject.get("User")
							.toString());
					userId = userJson.get("id").toString();
					email = userJson.get("email").toString();
					userName = userJson.get("full_name").toString();
					sucess = true;

				}

			}

			if (userId != null) {

				JSONObject json1 = new JSONObject();
				json1.put("userId", userId);
				json1.put("userName", userName);
				json1.put("token", token);
				json1.put("email", email);
				storeDataInCouchBase("TaskUser_" + userId, json1);

			}
			conn.disconnect();

		} catch (Exception e) {

			logger.error("error while performing  operation in addingUser"+e.getMessage());

		}
		return sucess;
	}

	// updating Task Users----------------------------
	public boolean updateUser(String token) {
		boolean sucess = false;

		JSONObject jsonObject = null;
		String userId = null;
		String email = null;
		String userName = null;
		try {

			// URL url = new
			// URL("https://todoist.com/API/v6/login?email=assistant@visdom.ca&password=Visdom1234");
			URL url = new URL("https://todoist.com/API/v6/sync?token="
					+ token.trim()
					+ "&seq_no=0&seq_no_global=0&resource_types=[\"all\"]");

			HttpURLConnection conn = (HttpURLConnection) url.openConnection();
			conn.setDoOutput(true);
			conn.setRequestMethod("POST");

			if (conn.getResponseCode() != 200) {
				logger.debug("Failed : HTTP error code : "
						+ conn.getResponseCode());
				sucess = false;
			} else {

				BufferedReader br = new BufferedReader(new InputStreamReader(
						(conn.getInputStream())));

				logger.debug("Output from Server .... \n");
				String output;
				while ((output = br.readLine()) != null) {
					// log.debug(output);
					jsonObject = new JSONObject(output);
					// token=jsonObject.getString("api_token");
					JSONObject userJson = new JSONObject(jsonObject.get("User")
							.toString());
					userId = userJson.get("id").toString();
					email = userJson.get("email").toString();
					userName = userJson.get("full_name").toString();
					sucess = true;

				}

			}

			if (userId != null) {

				JSONObject json1 = new JSONObject();
				json1.put("userId", userId);
				json1.put("userName", userName);
				json1.put("token", token);
				json1.put("email", email);
				storeDataInCouchBase("TaskUser_" + userId, json1);

			}
			conn.disconnect();

		} catch (Exception e) {

			logger.error("error while performing  operation in updateUser"+e.getMessage());
		}
		return sucess;

	}

	// get list of Taskusers for couchbase using view and query to display in
	// webpage
	static int dataExsist = 0;

	public List<User> getUsers() {
		ArrayList<User> list = new ArrayList<User>();

		try {
			client1 = getConnectionToCouchBase();
			DesignDocument designdoc = getDesignDocument("dev_TodoistUserGetView");
			boolean found = false;

			// 5. get the views and check the specified in code for existence
			for (ViewDesign view : designdoc.getViews()) {
				if (view.getMap() == "user") {
					found = true;
					break;
				}
			}

			// 6. If not found then create view inside document
			if (!found) {
				ViewDesign view = new ViewDesign("user",
						"function (doc, meta) {\n"
								+ "if(doc.token&&doc.userId)\n"
								+ "{emit(meta.id,null)}\n" +

								"}");
				designdoc.getViews().add(view);
				client1.createDesignDoc(designdoc);
			}

			// 7. close the connection with couchbase

		} catch (Exception e) {
			// TODO Auto-generated catch block
			logger.error("error while performing  operation in getUsers"+e.getMessage());
		}

		try {

			// get the view
			View view = client1.getView("dev_TodoistUserGetView", "user");

			// create Query.
			Query query = new Query();
			query.setIncludeDocs(true);

			// get ViewResponse
			ViewResponse viewRes = client1.query(view, query);

			// Iterate over the ViewResponse
			for (ViewRow row : viewRes) {
				++dataExsist;
				// Parse the Document to a bean User class
				ObjectMapper object = new ObjectMapper();
				User user = object.readValue(row.getDocument().toString(),
						User.class);
				list.add(user);

			}
			client1.shutdown();
			if (list.isEmpty()) {
				if (dataExsist < 3) {
					++dataExsist;

					getUsers();

				}
			}

		} catch (Exception e) {
			logger.debug("error is : "+e.getMessage());
		}
		return list;
	}

	private DesignDocument getDesignDocument(String name) {
		try {
			logger.debug("Design document with " + name + " exist ? "
					+ client1.getDesignDoc(name));
			return client1.getDesignDoc(name);
		} catch (com.couchbase.client.protocol.views.InvalidViewException e) {
			return new DesignDocument(name);
		}
	}

	// Deleting Task user From couchbase
	public boolean deleteUser(String userid) {

		boolean sucess = false;
		try {

			client1 = getConnectionToCouchBase();

			OperationFuture test = client1.delete(("TaskUser_" + userid.trim())
					.toString());
			logger.debug("TaskUser_" + userid.trim().toString() + "            "
					+ test);
			sucess = true;
			closeCouchBaseConnection();
		} catch (Exception e) {
			logger.error("error while deleting user"+e.getMessage());
		}

		return sucess;
	}

	public static void main(String[] args) throws org.json.JSONException {

		/*
		 * org.json.JSONObject jsn = new org.json.JSONObject();
		 * 
		 * jsn.put("va", 11); String[] str = new String[20]; str[0] = "123";
		 * str[1] = "2332"; jsn.put("val", str); logger.debug(jsn);
		 */
		new CouchBaseOperation().getConnectionToCouchBase();

	}

	public void updateDataInCouchBase(String key, JSONObject editData) {

		try {
			logger.info("inside editDataInCouchBase method of CouchBaseOperation class");

			client1 = getConnectionToCouchBase();

			client1.replace(key, editData.toString());

			closeCouchBaseConnection();

		} catch (Exception e) {
			logger.error("error while editing data into couchbase : " +e.getMessage());
		}
	}

	public String encryptPassword(String password) {
		StringBuffer secureString = new StringBuffer();
		try {
			password = "visdom" + password;
			char myKey = (char) 0x15; // binary 10101
			char[] chPass = password.toCharArray();
			for (int i = 0; i < chPass.length; i++) {
				chPass[i] = (char) (chPass[i] ^ myKey); // XOR
				secureString.append(chPass[i]);
			}
		} catch (Exception e) {
			logger.error("error while encryptPassword : " +e.getMessage());
		}

		return secureString.toString();
	}

	public String decryptPassword(String password) {
		StringBuffer secureString = new StringBuffer();
		try {

			char myKey = (char) 0x15; // binary 10101
			char[] chPass = password.toCharArray();
			for (int i = 0; i < chPass.length; i++) {
				chPass[i] = (char) (chPass[i] ^ myKey); // XOR
				secureString.append(chPass[i]);
			}
		} catch (Exception e) {
			logger.error("error while decrypt Password : " +e.getMessage());
		}

		return secureString.toString();
	}

	public JSONObject getDocument(String key) {
		JSONObject documentData = null;
		try {
			documentData = new JSONObject(new CouchBaseOperation()
					.getConnectionToCouchBase().get(key).toString());
		} catch (Exception e) {
			logger.error("error while getDocument from JSON " +e.getMessage());
		}

		return documentData;

	}

	public void storeAdminDetailsCouhbase(String userName, String password)
			throws JSONException {
		CouchbaseClient cleint = getConnectionToCouchBase();
		JSONObject json = null;

		try {
			json = new JSONObject(cleint.get("DataFromTodoist").toString());
			cleint.shutdown();

		} catch (Exception e) {
			logger.error("error while performing in storeAdminDetailsCouhbase" +e.getMessage());
		}
		if (json != null) {

		} else {
			json = new JSONObject();
			password = encryptPassword(password);
			json.put("username", userName);
			json.put("password", password);
			storeDataInCouchBase("DataFromTodoist", json);

		}
	}

	public void updateAdminDetailsCouhbase(String userName, String password) {
		CouchbaseClient cleint = getConnectionToCouchBase();
		JSONObject json = null;

		try {
			json = new JSONObject(cleint.get("DataFromTodoist").toString());
			cleint.shutdown();

		} catch (Exception e) {
			logger.error("error while performing in updateAdminDetailsCouhbase" +e.getMessage());
		}
		if (json != null) {
			try {

				password = encryptPassword(password);
				json.put("username", userName);
				json.put("password", password);
				updateDataInCouchBase("DataFromTodoist", json);
			} catch (Exception e) {
				logger.error("error while performing in updateAdminDetailsCouhbase like username & password" +e.getMessage());
			}
		} else {
			try {
				json = new JSONObject();
				password = encryptPassword(password);
				json.put("username", userName);
				json.put("password", password);
				storeDataInCouchBase("DataFromTodoist", json);
			} catch (Exception e) {
				logger.error("error while performing in updateAdminDetailsCouhbase like username & password" +e.getMessage());
			}
		}
	}

	public boolean getLogin(String userName, String password) {
		CouchbaseClient cleint = getConnectionToCouchBase();

		JSONObject json = null;
		boolean sucess = false;

		try {
			try {
				json = new JSONObject(cleint.get("DataFromTodoist").toString());
				client1.shutdown();
			} catch (Exception e) {
				logger.error("error while login" +e.getMessage());
			}
			if (json != null) {
				String pass = null;
				pass = decryptPassword(json.get("password").toString().trim());
				pass = pass.substring(6, pass.length());
				if (userName.trim().equalsIgnoreCase(
						json.get("username").toString().trim())
						&& password.trim().equalsIgnoreCase(pass)) {
					sucess = true;
				} else {
					sucess = false;
				}

			} else {
				sucess = false;
			}

		} catch (Exception e) {
			logger.error("error while login" +e.getMessage());
		}
		return sucess;
	}
}
