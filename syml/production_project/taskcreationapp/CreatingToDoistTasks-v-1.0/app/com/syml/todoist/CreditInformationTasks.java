package com.syml.todoist;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.Iterator;
import java.util.List;
import java.util.UUID;

import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;

import play.Logger;

import com.syml.ReadConfigurationFile;
import com.syml.TodoistConstants;
import com.syml.dto.ApplicantDocument;
import com.syml.dto.ProjectDetails;

public class CreditInformationTasks {

	ApplicantDocument doclist;
	private static org.slf4j.Logger log = play.Logger.underlying();

	static String token = "";
	static String todoistUser = "";
	static String assignUser = "";

	public static String createTasks(String taskName, String projectId,
			String token) throws JSONException {
		String taskId = "0";
		UUID uid = UUID.randomUUID();
		UUID uid1 = UUID.randomUUID();

		log.debug("inside taskss .... \n" + taskName);
		log.debug("inside projectId .... \n" + projectId);
		log.debug("inside token .... \n" + token);

		// log.info("uid " + uid);
		// log.info("uid 2" + uid1);
		String myvar = "[" + "  {" + "    \"type\": \"item_add\","
				+ "    \"temp_id\": \"" + uid + "\"," + "    \"uuid\": \""
				+ uid1 + "\"," + "    \"args\": {" + "      \"content\": \""
				+ taskName + "\"," + "      \"project_id\": " + projectId + ""
				+ "    }" + "  }" + "]";

		try {
			// log.info(URLEncoder.encode(myvar));

			URL url1 = new URL(TodoistConstants.TODOIST_APPLICATION_URL + token
					+ "&commands=" + URLEncoder.encode(myvar, "UTF-8"));
			HttpURLConnection conn1 = (HttpURLConnection) url1.openConnection();
			conn1.setDoOutput(true);
			conn1.setRequestMethod("POST");
			conn1.setRequestProperty("Content-Type",
					"application/x-www-form-urlencoded");

			if (conn1.getResponseCode() != 200) {
				log.error("Failed : HTTP error code : "
						+ conn1.getResponseCode());
			} else {

				BufferedReader br1 = new BufferedReader(new InputStreamReader(
						(conn1.getInputStream())));

				log.info("Output from Server .... \n");
				String output1;
				while ((output1 = br1.readLine()) != null) {
					// log.info(output1);
					log.debug("ouptut " + output1);
					JSONObject jsondata = new JSONObject(output1);
					JSONObject insideJsondata = (JSONObject) jsondata
							.get("TempIdMapping");
					taskId = insideJsondata.get(uid.toString()).toString();

				}

			}

			conn1.disconnect();

		} catch (MalformedURLException e) {

			log.error("error in credit task creation  " + e.getMessage());

		} catch (IOException e) {

			log.error("error in credit task creation  " + e.getMessage());

		}
		return taskId;
	}

	public static void taskUpdated(String taskId, String date, String assignId)
			throws JSONException {
		log.info("inside tasks updated .... \n");

		UUID uid = UUID.randomUUID();

		// Logger.debug("uid " + uid);
		// Logger.debug("uid 2" + uid1);

		String myvar = "[{\"type\": \"item_update\"," + " \"uuid\": \"" + uid
				+ "\", \"args\": {\"id\": " + taskId + ",\"responsible_uid\":"
				+ assignId + ", \"date_string\": \"" + date
				+ "\",\"priority\": 4}}]";

		try {
			log.info(date);

			URL url1 = new URL(TodoistConstants.TODOIST_APPLICATION_URL + token
					+ "&commands=" + URLEncoder.encode(myvar, "UTF-8"));
			// log.info(url1);
			HttpURLConnection conn1 = (HttpURLConnection) url1.openConnection();
			conn1.setDoOutput(true);
			conn1.setRequestMethod("POST");
			conn1.setRequestProperty("Content-Type",
					"application/x-www-form-urlencoded");

			if (conn1.getResponseCode() != 200) {
				log.error("Failed : HTTP error code : "
						+ conn1.getResponseCode());
			} else {

				BufferedReader br1 = new BufferedReader(new InputStreamReader(
						(conn1.getInputStream())));

				log.info("Output from Server .... \n");
				String output1;
				while ((output1 = br1.readLine()) != null) {
					log.info("task updatedd");
					log.info(output1);

				}

			}

			conn1.disconnect();

		} catch (MalformedURLException e) {

			log.error("error in credit task creation  " + e.getMessage());

		} catch (IOException e) {

			log.error("error in credit task creation  " + e.getMessage());

		}

	}

	public static String gettodoistUser(String assginUser) throws JSONException {

		assignUser = "5528089";

		return assignUser;

	}

	public static void createTasksCredit(JSONObject json) throws JSONException,
			TaskCreationException {
		String taskid = "0";
		String creditFailedtask = "";
		String assignUser = "";
		String projectId = "0";
		String opprunityId = "0";
		try {
			opprunityId = json.get("id").toString();
			projectId = ReadConfigurationFile
					.getTodoistLeadProjectID(opprunityId);
			if (projectId == null)
				throw new TaskCreationException(
						"Error  todoist project id not found Id ");
			if (projectId.isEmpty())
				throw new TaskCreationException(
						"Error  todoist project id not found Id ");
			token = ReadConfigurationFile.getUserToken();

			creditFailedtask = "We attempted to pull credit for "
					+ json.getString("applicantName")
					+ " however, the credit inquiry failed. Please check the formatting and completeness of the applicant address.  If it is correct,  Please contact "
					+ json.getString("applicantName")
					+ " to verify the accuracy of their Birthhdate, Social Insurance Number and Address."
					+ " Once you are confident of their address formatting, birthday and Social Insurance Number, please change the Opportunity Stage to Partial Application and then  back to Completed App to trigger the retrieval of credit and subsequent task creation.  ";

		

			List<ProjectDetails> listPorjects = ReadConfigurationFile.getProjectList(projectId);
			for (Iterator iterator = listPorjects.iterator(); iterator.hasNext();) {
				ProjectDetails projectDetails = (ProjectDetails) iterator
						.next();
				assignUser = projectDetails.getAssignedUser();
				projectId=projectDetails.getProjectId();
			}
			Logger.debug("project before Creating Credit Tasks : " + projectId
					+ " Token : " + token);
			taskid = createTasks(creditFailedtask, projectId, token.trim());

			taskUpdated(taskid, "-1", assignUser);

		} catch (Exception e) {
			throw new TaskCreationException(
					"Error  getting in creating credit tasks in Todoist Pojecet for OpporunityID= "
							+ opprunityId, e);
		}

		

		
	}

}
