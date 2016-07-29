package controllers;

import static play.data.Form.form;

import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.net.InetAddress;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Properties;

import javax.imageio.ImageIO;

import net.iharder.Base64;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.common.PDRectangle;
import org.apache.pdfbox.pdmodel.edit.PDPageContentStream;
import org.apache.pdfbox.pdmodel.font.PDFont;
import org.apache.pdfbox.pdmodel.font.PDType1Font;
import org.apache.pdfbox.pdmodel.graphics.xobject.PDPixelMap;
import org.apache.pdfbox.pdmodel.graphics.xobject.PDXObjectImage;
import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;

import com.couchbase.client.java.document.json.JsonObject;
import com.debortoliwines.openerp.api.FilterCollection;
import com.debortoliwines.openerp.api.ObjectAdapter;
import com.debortoliwines.openerp.api.Row;
import com.debortoliwines.openerp.api.RowCollection;
import com.debortoliwines.openerp.api.Session;
import com.sendwithus.SendWithUsExample;
import com.syml.couchbase.dao.service.CouchBaseService;

import couchbase.CouchBaseOperation;
import helper.GenericHelperClass;
import helper.OntraPortOperation;
import openerp.ReferalToCreateReferralResoursce;
import address.splitAddress.Address;
import pdfGeneration.PdfTableCreation;
import pdfGeneration.VisdomReferralPdfGeneration;
import play.Logger;
import play.data.DynamicForm;
import play.mvc.Controller;
import play.mvc.Result;
import views.html.*;

@SuppressWarnings("unused")
public class VisdomReferral extends Controller {

	public static Result bookmark() {
		return ok(visdomreferral3.render(""));

	}

	public static Result referral() {
		return ok(visdomferral.render(""));

	}

	public static Result wfgV() {

		return ok(wfgV.render(""));
	}

	public static Result wfg() {

		return ok(wfg.render(""));
	}

	public static Result referralV() {

		return ok(visdomferralV.render(""));

	}

	public static Result realtor() {

		return ok(realtor.render(""));

	}

	public static Result realtorV() {

		return ok(realtorV.render(""));

	}

	public static Result professional() {

		return ok(professional.render(""));

	}

	public static Result professionalV() {

		return ok(professionalV.render(""));

	}

	public static Result planner() {

		return ok(planner.render(""));

	}

	public static Result plannerV() {

		return ok(plannerV.render(""));

	}

	public static Result client() {

		return ok(client.render(""));

	}

	public static Result clientV() {

		return ok(clientV.render(""));

	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	public static Result visdomReferral() {
		DynamicForm dynamicForm = form().bindFromRequest();

		String role = dynamicForm.get("role");
		String name = dynamicForm.get("fname");
		String lastname = dynamicForm.get("lname");
		String email = dynamicForm.get("email");
		String phoneNumber = dynamicForm.get("phone");
		String nameOfBuilderYouWork = dynamicForm.get("broker");
		String compnesation = dynamicForm.get("feeTo");
		String companyName = dynamicForm.get("company");

		// String AreYouLicensedAsRealtor = req.getParameter("q27_areYou");
		String nameOfTheBrokerageWhereYouWork = dynamicForm.get("broker");
		String AddressOfTheBrokerageWhereYouWork = dynamicForm
				.get("formatted_address");
		String ip = dynamicForm.get("ip");

		JSONObject jsonDataOfIp = null;

		String userIp = "";
		String latitude = "";
		String longitude = "";

		try {
			jsonDataOfIp = new JSONObject(ip);
			userIp = jsonDataOfIp.getString("ip");
			latitude = jsonDataOfIp.getString("latitude");
			longitude = jsonDataOfIp.getString("longitude");
			Logger.debug("ip----------------" + ip);
			Logger.debug("latitude----------------" + latitude);
			Logger.debug("longitude----------------" + longitude);

		} catch (NullPointerException e) {

		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		String brokerPhoneNumber = dynamicForm.get("brokerPhone");
		String WhoIsThebrokerThatManagesTheBrokerageWhereYouWork = dynamicForm
				.get("brokerMange");
		String referrer = dynamicForm.get("refer");

		String addressArray[] = request().body().asFormUrlEncoded()
				.get("formatted_address");
		String addressFromArray = "";
		if (addressArray != null) {
			for (int i = 0; i < addressArray.length; i++) {
				addressFromArray = addressArray[i];
			}

		}
		HashMap<String, String> address = new Address()
				.getProperAddressTwo(addressFromArray);
		Logger.debug("nameOfTheBrokerageWhereYouWork  "
				+ nameOfTheBrokerageWhereYouWork
				+ " AddressOfTheBrokerageWhereYouWork " + addressFromArray);
		Logger.debug("brokerPhoneNumber  " + brokerPhoneNumber
				+ " WhoIsThebrokerThatManagesTheBrokerageWhereYouWork "
				+ WhoIsThebrokerThatManagesTheBrokerageWhereYouWork);

		Logger.debug("inside second visdom referrel servlet");

		Logger.debug("both forms are filled by same person");
		System.out.println("role " + role);
		Logger.info("Role" + role);

		Logger.info("First Name" + name);

		Logger.info("Last Name" + lastname);

		Logger.info("Email" + email);
		Logger.info("phone number" + phoneNumber);
		Logger.info(" Name of Builder" + nameOfBuilderYouWork);

		Logger.info("compnesation is Given " + compnesation);

		Logger.info("realtorBrokerage Work address "
				+ AddressOfTheBrokerageWhereYouWork);

		Logger.info("name of the brokarage where you work"
				+ nameOfTheBrokerageWhereYouWork);
		Logger.info("realtorBroker Phone" + brokerPhoneNumber);
		Logger.info("company name" + companyName);

		Logger.info("WhoIsThebrokerThatManagesTheBrokerageWhereYouWork"
				+ WhoIsThebrokerThatManagesTheBrokerageWhereYouWork);
		Logger.info("Who Referrer To Visdom " + referrer);

		// creating objrcts of supporting classes
		ReferalToCreateReferralResoursce referalToCreateReferralResoursce = new ReferalToCreateReferralResoursce();
		GenericHelperClass gHelperClass = new GenericHelperClass();
		String roleCompany = "";
		try {
			if (role.equalsIgnoreCase("Realtor")) {
				compnesation = "Company";

			} else if (role.equalsIgnoreCase("Professional")) {

				compnesation = "Direct to Myself";
			}
		} catch (NullPointerException e) {
			Logger.error("null pointer exception when getting role data " + e);
		}

		// store value in hashmap to send data to second and third form
		// storeSubmitReferalDataInCouchBase()
		HashMap data = new HashMap();
		data.put("firstname", name);
		data.put("lastName", lastname);
		data.put("email", email);
		data.put("phoneNumber", phoneNumber);
		data.put("companyname", referalToCreateReferralResoursce
				.getCompanyName(nameOfTheBrokerageWhereYouWork, companyName));
		data.put("companyAddress", addressFromArray);

		data.put("compansation", compnesation);
		data.put("companyPhoneNumber", brokerPhoneNumber);
		data.put("broker_Manages_urwork",
				WhoIsThebrokerThatManagesTheBrokerageWhereYouWork);
		data.put("referrer_Tovisdom", referrer);
		data.put("RefrralProgress", "50%");
		data.put("ip", userIp);
		data.put("latitude", latitude);
		data.put("longitude", longitude);
		int referralExsist = 0;

		// storing value to hash map to send data to thrird form
		HashMap message = new HashMap();
		int referrelID = 0;
		int contactId = 0;
		try {

			int referrerResPartnerId = 0;

			// email
			contactId = gHelperClass.lookupContactID(email, name, lastname,
					"res.partner");

			// Getting the partnerId of the one who referrer to visdom

			referrerResPartnerId = referalToCreateReferralResoursce
					.getExsistingResPartnerId(referrer);

			if (contactId == 0) {
				Logger.debug("if contact(res.partner) and referrel(hr.applicant) not exsist");
				// creating new contact and returning the contactid
				contactId = gHelperClass.createContact(name, lastname, email,
						"res.partner");
				Logger.info("Contact is created with this Id " + contactId);
				// creating new referrer and returning the referrelId
				referrelID = gHelperClass.createRefereal(name, lastname, email,
						contactId, phoneNumber, referrer, role,
						referalToCreateReferralResoursce.getCompanyName(

						nameOfTheBrokerageWhereYouWork, companyName),
						brokerPhoneNumber, referrerResPartnerId, address,
						"hr.applicant");
				Logger.info("Referrer is created with this Id " + referrelID);
			} else {

				Logger.debug("if contact is Exsist");

				// Checking contact is exsist in referrerl Resource
				// based on firstname or last name or fullname and
				// returning the referrelid if exsist else returning 0
				referrelID = gHelperClass.lookupReferalResource(contactId,
						"hr.applicant");
				if (referrelID == 0) {
					Logger.debug("if contact(res.partner) is Exsist");

					// inserting data to referrla resoursce and
					// returning referral id
					referrelID = gHelperClass.createRefereal(name, lastname,
							email, contactId, phoneNumber, referrer, role,
							referalToCreateReferralResoursce.getCompanyName(

							nameOfTheBrokerageWhereYouWork, companyName),
							brokerPhoneNumber, referrerResPartnerId, address,
							"hr.applicant");
					Logger.info("Referrer is created with this Id "
							+ referrelID);

				} else {

					referralExsist = 1;

					Logger.info("Referrer is exsist  with this Id "
							+ referrelID);
					// updating the referrer and returning referrel id
					message = referalToCreateReferralResoursce
							.updateReferrralDetails(name, lastname, email,
									contactId, phoneNumber, referrer, role,
									referalToCreateReferralResoursce
											.getCompanyName(

											nameOfTheBrokerageWhereYouWork,
													companyName),
									brokerPhoneNumber, referrerResPartnerId,
									address, "hr.applicant");
					Logger.info("Referrer details are updted  based on this Id "
							+ message.get("referrelId"));
				}

			}

			String formType = "Visdom_Referrel";

			CouchBaseService storeData = new CouchBaseService();
			String key = formType + "_" + referrelID;

			JsonObject jsonObject=JsonObject.from(data);
			jsonObject.put("FormType", formType);
			storeData.storeDataToCouchbase(key, jsonObject);

		} catch (Exception e) {
			// TODO: handle exception
			Logger.error("erro in visdomReferralFormone Controller  " + e);

		}

		return ok(visdomreferral2.render(role, referrelID, referralExsist));
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	public static Result visdomReferralForm2() {

		String formType = "Visdom_Referrel";

		DynamicForm dynamicForm = form().bindFromRequest();

		String role = dynamicForm.get("role");

		String referrelId = dynamicForm.get("referrelID");
		String areUsingtouchScreenDevice = dynamicForm.get("touchScreen");
		String signature = dynamicForm.get("typedName");
		String elctornicSign = dynamicForm.get("sign");
		int referralExsist = 0;
		try {
			referralExsist = Integer.parseInt(dynamicForm.get("exsist"));
		} catch (Exception e) {
			Logger.error("error parsing to intrger" + e);
		}
		BufferedImage image = null;
		String pdffilepath = "";
		Logger.debug("touchScreen data ----" + dynamicForm.get("touchScreen"));
		Logger.debug("agree data ----" + dynamicForm.get("agree"));
		Logger.debug("dynanmic data ----" + dynamicForm.get("typedName"));
		Logger.debug("referral exist data ----" + dynamicForm.get("exsist"));

		Logger.debug("Role :" + role);
		Logger.debug("ReferrelId :" + referrelId);
		Logger.debug("Are You Using Touch Screen Device :"
				+ areUsingtouchScreenDevice);
		Logger.debug("Typed Signature :" + signature);
		SendWithUsExample sendwithus = new SendWithUsExample();
		String ip = "";
		String roleid = "";
		int ontraPortRoleId = 0;
		try {
			if (role.equalsIgnoreCase("Realtor")) {
				roleid = "realtor";
				ontraPortRoleId = 8;

			} else if (role.equalsIgnoreCase("Financial Planner")) {

				roleid = "financial_planner";
				ontraPortRoleId = 7;
			} else if (role.equalsIgnoreCase("Client")) {

				roleid = "client";
				ontraPortRoleId = 6;
			}

			else {
				roleid = "other";

			}
			CouchBaseService storeData = new CouchBaseService();
			String key = formType + "_" + referrelId;

			JsonObject json = null;
			try {
				json = storeData.getCouhbaseDataByKey(key);

				ip = json.getString("ip");

			} catch (Exception e) {
				Logger.error("error in getting the couhbase data" + e);
				// try for second time to gett data
				try {
					json = storeData.getCouhbaseDataByKey(key);

					ip = json.getString("ip");
				} catch (Exception e1) {
					Logger.error("error in getting the couhbase data" + e1);
					// Todo send mail if fails second time also to get couhbase
					// data
				}

			}

			GenericHelperClass glHelperClass = new GenericHelperClass();
			Properties prop = null;
			try {
				prop = glHelperClass.readConfigfile();

			} catch (Exception e) {

			}
			try {

				Logger.debug("ip addresss old page ------------. :" + ip);

				Session opSession = glHelperClass.getOdooConnection();
				ObjectAdapter resPartner = opSession
						.getObjectAdapter("hr.applicant");
				FilterCollection filterCollection = new FilterCollection();
				filterCollection.add("id", "=", referrelId);
				RowCollection row = resPartner.searchAndReadObject(
						filterCollection, new String[] { "email_from", "name",
								"partner_id", "stage_id", "sign_ip",
								"agreement_date", "role", "id" });
				Row row1 = row.get(0);
				Logger.debug("The referrer detalas for to change stage "
						+ row1.get("name"));
				Date date = Calendar.getInstance().getTime();
				Logger.debug("date  " + date);
				row1.put("sign_ip", ip);
				row1.put("agreement_date", date);
				row1.put("role", roleid);
				row1.put("stage_id", prop.getProperty("visdomreferralStageId"));
				// row1.put("stage_id", 37);
				resPartner.writeObject(row1, true);
				Logger.debug("Referrer  stage changed to involved");
			} catch (Exception e) {
				Logger.error("error in Referrer  stage changed to involved" + e);
			}

			try {

				if (areUsingtouchScreenDevice.equals("yes")) {

					/* Logger.debug(" elecotronic signature  is "+sign ); */

					String newString = elctornicSign.substring(22);
					// System.out.println(newString);
					try {
						byte[] decodedBytes = Base64.decode(newString);
						if (decodedBytes == null) {
							/* Logger.debug("decodedBytes  is null"); */
						}

						image = ImageIO.read(new ByteArrayInputStream(
								decodedBytes));
					} catch (IOException e) {
						Logger.error("failed to read into image");
					}
					pdffilepath = VisdomReferralPdfGenerationMethod(
							json.getString("firstname"),
							areUsingtouchScreenDevice, "", role, image);
					sendwithus.sendTOReferralAttachment(
							json.getString("firstname"),
							"http://forms.visdom.ca/clientrefV?referralId="
									+ referrelId, json.getString("email"),
							pdffilepath);
				} else {
					/* Logger.debug("typed signature  is "+sign ); */

					pdffilepath = VisdomReferralPdfGenerationMethod(
							json.getString("firstname"),
							areUsingtouchScreenDevice, signature, role, image);
					sendwithus.sendTOReferralAttachment(
							json.getString("firstname"),
							"http://forms.visdom.ca/clientrefV?referralId="
									+ referrelId, json.getString("email"),
							pdffilepath);
				}

			} catch (Exception e) {
				Logger.error("error in mailing sending " + e);
			}
			try {

				Path path = Paths.get(pdffilepath);
				byte[] data1 = Files.readAllBytes(path);

				HashMap hashdata = new HashMap();
				String encodeData = net.iharder.Base64.encodeBytes(data1);
				hashdata.put("attachement", encodeData);
				JsonObject jsonObject=JsonObject.from(hashdata);
				jsonObject.put("FormType","visdomReferral");
				storeData.storeDataToCouchbase("doc_ReferralAgreemetfile_"
						+ referrelId, jsonObject);

			} catch (Exception e) {
				Logger.error("error in storing referral agreement datat to couhbase"
						+ e);
			}
			try {
				File file = new File(pdffilepath);
				file.delete();
			} catch (Exception e) {
				Logger.error("error in deleting pdf file " + e);
			}

			if (json != null) {
				json.put("role", role);
				json.put("ip", ip);
				json.put("AreUsingTouchScreenDEvice", areUsingtouchScreenDevice);
				json.put("RefrralProgress", "100%");

				json.put("mytypedName", signature);
				json.put("ReferralAgreemetfile_id", "doc_ReferralAgreemetfile_"
						+ referrelId);

				json.put("Submission_Date_Time1b", new Date().toString());
				json.put("Type_referral", "Type_referral");

			}

			// send referral fname,referral lastname, email,
			// addrees(city,state,country
			// ,pincode),referredBy,role,referralsoursceid,referalAgreementUrl
			// Todo call onthraport restcall------------------------------

			String firstname = null;
			String lastname = null;
			String email = null;
			String phone = null;
			String companyName = null;
			String companyAddress = null;
			String companyPhoneNumber = null;
			String referrer_Tovisdom = null;

			String agreementURL = "https://doc.visdom.ca/getid?id=doc_ReferralAgreemetfile_"
					+ referrelId;

			String address1 = null;
			String city = null;
			String postalcode = null;

			try {
				firstname = json.getString("firstname");
				lastname = json.getString("lastName");
				email = json.getString("email");
				phone = json.getString("phoneNumber");
				companyName = json.getString("companyname");
				companyAddress = json.getString("companyAddress");

				companyPhoneNumber = json.getString("companyPhoneNumber");
				referrer_Tovisdom = json.getString("referrer_Tovisdom");
				HashMap<String, String> address = new Address()
						.getProperAddressTwo(companyAddress);

				if (!address.isEmpty()) {
					address1 = address.get("address1");
					city = address.get("city");

					postalcode = address.get("postalcode");
				}

			} catch (NullPointerException ex) {
				Logger.error(ex.getMessage());

			}
			new OntraPortOperation().createContact(firstname, lastname, email,
					phone, ontraPortRoleId, companyName, referrer_Tovisdom,
					referrelId, companyPhoneNumber, agreementURL, address1,
					city, postalcode);
			JSONObject jsonTableData = new JSONObject();
			try {
				jsonTableData.put("name", firstname + " " + lastname);
				jsonTableData.put("phonenumber", phone);
				jsonTableData.put("email", email);

				jsonTableData.put("referrdBy", referrer_Tovisdom);

			} catch (Exception e) {
				// TODO Auto-generated catch block
				Logger.error("error in parsing json " + e);
			}
			new openerp.RestCallClass(jsonTableData.toString()).start();

			// here
			try {
				storeData.storeDataToCouchbase(key, json);
			} catch (Exception e) {
				Logger.error("error in storeing referral data in couhbase " + e);
			}

		} catch (Exception e) {
			Logger.error("error in visdomreferral form" + e);
		}

		if (referralExsist == 0) {
			return ok(leadsucess
					.render("Thank you for becoming involved in the Visdom Referral Program. We have sent a copy of the referral agreement to the email provided. In the event you did not see it please check your spam folder in case your provider accidentally miscategorized it"));
		} else {
			return ok(leadsucess
					.render("Thank you for your participation in the Visdom Referral Program.  Your information has been successfully updated. We have sent a copy of the referral agreement to the email provided. In the event you did not see it please check your spam folder in case your provider accidentally miscategorized it"));

		}

	}

	public static void main(String[] args) throws IOException {
		String pdfPath = VisdomReferralPdfGenerationMethod("James", "no", "James",
				"Professional", null);
		new SendWithUsExample().sendTOReferralAttachment("James",
				"https://forms.visdom.ca/clientrefV?referralId=850&utm_swu=9551",
				"james.y.kim7@gmail.com", pdfPath);

	}

	public static String VisdomReferralPdfGenerationMethod(String referralName,
			String areusingTouchScreenDevice, String myTypedName, String role,
			BufferedImage image) {

		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");

		GregorianCalendar calendar = (GregorianCalendar) GregorianCalendar
				.getInstance();

		/* Logger.debug("insdie Referreral  Agreement  Pdf generation"); */
		String file = "/home/venkateshm/Desktop/";
		/*
		 * String outputFileName=file+"Referral_Agreement_"
		 * +referralName+"_"+format.format(calendar.getTime())+".pdf";
		 */
		GenericHelperClass genericHelperClass = new GenericHelperClass();
		String outputFileName = genericHelperClass.readConfigfile()
				.getProperty("pdfPath")
				+ "ReferralAgreement_"
				+ referralName
				+ "_" + format.format(calendar.getTime()) + ".pdf";
		/*
		 * String outputFileName = file + "RerralAgreement_" + referralName +
		 * "_" + format.format(calendar.getTime()) + ".pdf";
		 */
		Properties prop = new Properties();
		try {

			prop.load(VisdomReferralPdfGeneration.class.getClassLoader()
					.getResourceAsStream("visdomReferral.properties"));
			Logger.debug("referralName " + referralName
					+ "areusingTouchScreenDevice " + areusingTouchScreenDevice
					+ " myTypedName" + myTypedName + "role " + role + " image"
					+ image);
			Logger.debug("prop " + prop.size());
			prop.load(VisdomReferral.class.getClassLoader()
					.getResourceAsStream("visdomReferral.properties"));

			PDDocument document = new PDDocument();
			PDPage page1 = new PDPage(PDPage.PAGE_SIZE_A4);

			PDRectangle rect = page1.getMediaBox();
			document.addPage(page1);

			PDFont fontplain = PDType1Font.TIMES_ROMAN;
			PDFont fontbold = PDType1Font.HELVETICA_BOLD;
			// PDFont fontItalic = PDType1Font.HELVETICA_OBLIQUE;
			// PDFont fontMono = PDType1Font.COURIER;

			PDPageContentStream cos = new PDPageContentStream(document, page1);

			int line = 0;

			cos.beginText();
			cos.setFont(fontbold, 12);
			cos.moveTextPositionByAmount(240, rect.getHeight() - 50 * (++line));
			cos.drawString("Mortgage Referral Agreement");
			cos.endText();
			int line2 = 0;
			cos.beginText();
			cos.setFont(fontplain, 10);
			cos.moveTextPositionByAmount(25, rect.getHeight() - 100);
			cos.drawString(prop.getProperty("referral1"));
			cos.endText();
			line2 = line2 + 10;

			cos.beginText();
			cos.setFont(fontplain, 10);
			cos.moveTextPositionByAmount(25, rect.getHeight() - 100 - line2);
			cos.drawString(prop.getProperty("referral2"));
			cos.endText();
			line2 = line2 + 15;

			cos.beginText();
			cos.setFont(fontplain, 10);
			cos.moveTextPositionByAmount(20, rect.getHeight() - 100 - line2);
			cos.drawString(prop.getProperty("referral3"));
			cos.endText();
			line2 = line2 + 15;

			cos.beginText();
			cos.setFont(fontplain, 10);
			cos.moveTextPositionByAmount(20, rect.getHeight() - 100 - line2);
			cos.drawString(prop.getProperty("referral4"));
			cos.endText();
			line2 = line2 + 10;

			cos.beginText();
			cos.setFont(fontplain, 10);
			cos.moveTextPositionByAmount(20, rect.getHeight() - 100 - line2);
			cos.drawString(prop.getProperty("referral5"));
			cos.endText();
			line2 = line2 + 10;

			cos.beginText();
			cos.setFont(fontplain, 10);
			cos.moveTextPositionByAmount(20, rect.getHeight() - 100 - line2);
			cos.drawString(prop.getProperty("referral6"));
			cos.endText();
			line2 = line2 + 20;

			cos.beginText();
			cos.setFont(fontplain, 10);
			cos.moveTextPositionByAmount(30, rect.getHeight() - 100 - line2);
			cos.drawString(prop.getProperty("referral7"));
			cos.endText();
			line2 = line2 + 15;

			cos.beginText();
			cos.setFont(fontplain, 10);
			cos.moveTextPositionByAmount(20, rect.getHeight() - 100 - line2);
			cos.drawString(prop.getProperty("referral8"));
			cos.endText();
			line2 = line2 + 15;

			cos.beginText();
			cos.setFont(fontplain, 10);
			cos.moveTextPositionByAmount(20, rect.getHeight() - 100 - line2);
			cos.drawString(prop.getProperty("referral8"));
			cos.endText();
			line2 = line2 + 15;

			cos.beginText();
			cos.setFont(fontplain, 10);
			cos.moveTextPositionByAmount(20, rect.getHeight() - 100 - line2);
			cos.drawString(prop.getProperty("referral9"));
			cos.endText();
			line2 = line2 + 10;

			cos.beginText();
			cos.setFont(fontplain, 10);
			cos.moveTextPositionByAmount(20, rect.getHeight() - 100 - line2);
			cos.drawString(prop.getProperty("referral10"));
			cos.endText();
			line2 = line2 + 15;

			cos.beginText();
			cos.setFont(fontplain, 10);
			cos.moveTextPositionByAmount(20, rect.getHeight() - 100 - line2);
			cos.drawString(prop.getProperty("referral11"));
			cos.endText();
			line2 = line2 + 15;

			cos.beginText();
			cos.setFont(fontplain, 10);
			cos.moveTextPositionByAmount(20, rect.getHeight() - 100 - line2);
			cos.drawString(prop.getProperty("referral12"));
			cos.endText();
			line2 = line2 + 10;

			cos.beginText();
			cos.setFont(fontplain, 10);
			cos.moveTextPositionByAmount(20, rect.getHeight() - 100 - line2);
			cos.drawString(prop.getProperty("referral13"));
			cos.endText();
			line2 = line2 + 10;

			cos.beginText();
			cos.setFont(fontplain, 10);
			cos.moveTextPositionByAmount(20, rect.getHeight() - 100 - line2);
			cos.drawString(prop.getProperty("referral14"));
			cos.endText();
			line2 = line2 + 15;

			cos.beginText();
			cos.setFont(fontplain, 10);
			cos.moveTextPositionByAmount(20, rect.getHeight() - 100 - line2);
			cos.drawString(prop.getProperty("referral15"));
			cos.endText();
			line2 = line2 + 15;

			cos.beginText();
			cos.setFont(fontplain, 10);
			cos.moveTextPositionByAmount(20, rect.getHeight() - 100 - line2);
			cos.drawString(prop.getProperty("referral16"));
			cos.endText();
			line2 = line2 + 10;

			cos.beginText();
			cos.setFont(fontplain, 10);
			cos.moveTextPositionByAmount(20, rect.getHeight() - 100 - line2);
			cos.drawString(prop.getProperty("referral17"));
			cos.endText();
			line2 = line2 + 10;

			cos.beginText();
			cos.setFont(fontplain, 10);
			cos.moveTextPositionByAmount(20, rect.getHeight() - 100 - line2);
			cos.drawString(prop.getProperty("referral18"));
			cos.endText();
			line2 = line2 + 10;

			cos.beginText();
			cos.setFont(fontplain, 10);
			cos.moveTextPositionByAmount(20, rect.getHeight() - 100 - line2);
			cos.drawString(prop.getProperty("referral19"));
			cos.endText();
			line2 = line2 + 15;

			cos.beginText();
			cos.setFont(fontplain, 10);
			cos.moveTextPositionByAmount(20, rect.getHeight() - 100 - line2);
			cos.drawString(prop.getProperty("referral20"));
			cos.endText();
			line2 = line2 + 10;

			cos.beginText();
			cos.setFont(fontplain, 10);
			cos.moveTextPositionByAmount(20, rect.getHeight() - 100 - line2);
			cos.drawString(prop.getProperty("referral21"));
			cos.endText();
			line2 = line2 + 15;

			cos.beginText();
			cos.setFont(fontplain, 10);
			cos.moveTextPositionByAmount(20, rect.getHeight() - 100 - line2);
			cos.drawString(prop.getProperty("referral22"));
			cos.endText();
			line2 = line2 + 10;

			cos.beginText();
			cos.setFont(fontplain, 10);
			cos.moveTextPositionByAmount(20, rect.getHeight() - 100 - line2);
			cos.drawString(prop.getProperty("referral23"));
			cos.endText();
			line2 = line2 + 10;

			cos.beginText();
			cos.setFont(fontplain, 10);
			cos.moveTextPositionByAmount(20, rect.getHeight() - 100 - line2);
			cos.drawString(prop.getProperty("referral24"));
			cos.endText();
			line2 = line2 + 10;

			cos.beginText();
			cos.setFont(fontplain, 10);
			cos.moveTextPositionByAmount(20, rect.getHeight() - 100 - line2);
			cos.drawString(prop.getProperty("referral25"));
			cos.endText();
			line2 = line2 + 15;

			cos.beginText();
			cos.setFont(fontplain, 10);
			cos.moveTextPositionByAmount(20, rect.getHeight() - 100 - line2);
			cos.drawString(prop.getProperty("referral26"));
			cos.endText();
			line2 = line2 + 10;

			cos.beginText();
			cos.setFont(fontplain, 10);
			cos.moveTextPositionByAmount(20, rect.getHeight() - 100 - line2);
			cos.drawString(prop.getProperty("referral27"));
			cos.endText();
			line2 = line2 + 15;

			cos.beginText();
			cos.setFont(fontplain, 10);
			cos.moveTextPositionByAmount(20, rect.getHeight() - 100 - line2);
			cos.drawString(prop.getProperty("referral28"));
			cos.endText();
			line2 = line2 + 15;

			cos.beginText();
			cos.setFont(fontplain, 10);
			cos.moveTextPositionByAmount(20, rect.getHeight() - 100 - line2);
			cos.drawString(prop.getProperty("referral29"));
			cos.endText();
			line2 = line2 + 15;

			cos.beginText();
			cos.setFont(fontplain, 10);
			cos.moveTextPositionByAmount(20, rect.getHeight() - 100 - line2);
			cos.drawString(prop.getProperty("referral30"));
			cos.endText();
			line2 = line2 + 10;

			cos.beginText();
			cos.setFont(fontplain, 10);
			cos.moveTextPositionByAmount(20, rect.getHeight() - 100 - line2);
			cos.drawString(prop.getProperty("referral31"));
			cos.endText();
			line2 = line2 + 15;

			cos.beginText();
			cos.setFont(fontplain, 10);
			cos.moveTextPositionByAmount(20, rect.getHeight() - 100 - line2);
			cos.drawString(prop.getProperty("referral32"));
			cos.endText();
			line2 = line2 + 10;

			cos.beginText();
			cos.setFont(fontplain, 10);
			cos.moveTextPositionByAmount(20, rect.getHeight() - 100 - line2);
			cos.drawString(prop.getProperty("referral33"));
			cos.endText();
			line2 = line2 + 10;

			cos.beginText();
			cos.setFont(fontplain, 10);
			cos.moveTextPositionByAmount(20, rect.getHeight() - 100 - line2);
			cos.drawString(prop.getProperty("referral34"));
			cos.endText();
			line2 = line2 + 10;

			cos.beginText();
			cos.setFont(fontplain, 10);
			cos.moveTextPositionByAmount(20, rect.getHeight() - 100 - line2);
			cos.drawString(prop.getProperty("referral35"));
			cos.endText();
			line2 = line2 + 10;

			cos.beginText();
			cos.setFont(fontplain, 10);
			cos.moveTextPositionByAmount(20, rect.getHeight() - 100 - line2);
			cos.drawString(prop.getProperty("referral36"));
			cos.endText();
			line2 = line2 + 10;

			cos.beginText();
			cos.setFont(fontplain, 10);
			cos.moveTextPositionByAmount(20, rect.getHeight() - 100 - line2);
			cos.drawString(prop.getProperty("referral37"));
			cos.endText();
			line2 = line2 + 20;

			cos.beginText();
			cos.setFont(fontplain, 10);
			cos.moveTextPositionByAmount(30, rect.getHeight() - 100 - line2);
			cos.drawString(prop.getProperty("referral38"));
			cos.endText();
			line2 = line2 + 15;

			cos.beginText();
			cos.setFont(fontplain, 10);
			cos.moveTextPositionByAmount(20, rect.getHeight() - 100 - line2);
			cos.drawString(prop.getProperty("referral39"));
			cos.endText();
			line2 = line2 + 10;

			cos.beginText();
			cos.setFont(fontplain, 10);
			cos.moveTextPositionByAmount(20, rect.getHeight() - 100 - line2);
			cos.drawString(prop.getProperty("referral40"));
			cos.endText();
			line2 = line2 + 15;

			cos.beginText();
			cos.setFont(fontplain, 10);
			cos.moveTextPositionByAmount(20, rect.getHeight() - 100 - line2);
			cos.drawString(prop.getProperty("referral41"));
			cos.endText();
			line2 = line2 + 10;

			cos.beginText();
			cos.setFont(fontplain, 10);
			cos.moveTextPositionByAmount(20, rect.getHeight() - 100 - line2);
			cos.drawString(prop.getProperty("referral42"));
			cos.endText();
			line2 = line2 + 20;

			cos.beginText();
			cos.setFont(fontplain, 10);
			cos.moveTextPositionByAmount(30, rect.getHeight() - 100 - line2);
			cos.drawString(prop.getProperty("referral43"));
			cos.endText();
			line2 = line2 + 15;

			cos.beginText();
			cos.setFont(fontplain, 10);
			cos.moveTextPositionByAmount(20, rect.getHeight() - 100 - line2);
			cos.drawString(prop.getProperty("referral44"));
			cos.endText();
			line2 = line2 + 10;

			cos.beginText();
			cos.setFont(fontplain, 10);
			cos.moveTextPositionByAmount(20, rect.getHeight() - 100 - line2);
			cos.drawString(prop.getProperty("referral45"));
			cos.endText();
			line2 = line2 + 15;

			cos.beginText();
			cos.setFont(fontplain, 10);
			cos.moveTextPositionByAmount(20, rect.getHeight() - 100 - line2);
			cos.drawString(prop.getProperty("referral46"));
			cos.endText();
			line2 = line2 + 15;

			cos.beginText();
			cos.setFont(fontplain, 10);
			cos.moveTextPositionByAmount(20, rect.getHeight() - 100 - line2);
			cos.drawString(prop.getProperty("referral47"));
			cos.endText();
			line2 = line2 + 10;

			cos.beginText();
			cos.setFont(fontplain, 10);
			cos.moveTextPositionByAmount(20, rect.getHeight() - 100 - line2);
			cos.drawString(prop.getProperty("referral48"));
			cos.endText();
			line2 = line2 + 10;

			cos.beginText();
			cos.setFont(fontplain, 10);
			cos.moveTextPositionByAmount(20, rect.getHeight() - 100 - line2);
			cos.drawString(prop.getProperty("referral49"));
			cos.endText();
			line2 = line2 + 15;

			cos.beginText();
			cos.setFont(fontplain, 10);
			cos.moveTextPositionByAmount(20, rect.getHeight() - 100 - line2);
			cos.drawString(prop.getProperty("referral50"));
			cos.endText();
			line2 = line2 + 15;

			cos.beginText();
			cos.setFont(fontplain, 10);
			cos.moveTextPositionByAmount(20, rect.getHeight() - 100 - line2);
			cos.drawString(prop.getProperty("referral51"));
			cos.endText();
			line2 = line2 + 10;

			cos.beginText();
			cos.setFont(fontplain, 10);
			cos.moveTextPositionByAmount(20, rect.getHeight() - 100 - line2);
			cos.drawString(prop.getProperty("referral52"));
			cos.endText();
			line2 = line2 + 15;

			cos.beginText();
			cos.setFont(fontplain, 10);
			cos.moveTextPositionByAmount(20, rect.getHeight() - 100 - line2);
			cos.drawString(prop.getProperty("referral53"));
			cos.endText();
			line2 = line2 + 10;

			cos.beginText();
			cos.setFont(fontplain, 10);
			cos.moveTextPositionByAmount(20, rect.getHeight() - 100 - line2);
			cos.drawString(prop.getProperty("referral54"));
			cos.endText();
			line2 = line2 + 10;

			cos.beginText();
			cos.setFont(fontplain, 10);
			cos.moveTextPositionByAmount(20, rect.getHeight() - 100 - line2);
			cos.drawString(prop.getProperty("referral55"));
			cos.endText();
			line2 = line2 + 10;

			cos.beginText();
			cos.setFont(fontplain, 10);
			cos.moveTextPositionByAmount(20, rect.getHeight() - 100 - line2);
			cos.drawString(prop.getProperty("referral56"));
			cos.endText();
			line2 = line2 + 15;
			cos.close();

			PDPage page2 = new PDPage(PDPage.PAGE_SIZE_A4);
			document.addPage(page2);
			cos = new PDPageContentStream(document, page2);
			line2 = 0;
			cos.beginText();
			cos.setFont(fontplain, 10);
			cos.moveTextPositionByAmount(20, rect.getHeight() - 70 - line2);
			cos.drawString(prop.getProperty("referral57"));
			cos.endText();
			line2 = line2 + 10;

			cos.beginText();
			cos.setFont(fontplain, 10);
			cos.moveTextPositionByAmount(20, rect.getHeight() - 70 - line2);
			cos.drawString(prop.getProperty("referral58"));
			cos.endText();
			line2 = line2 + 10;

			cos.beginText();
			cos.setFont(fontplain, 10);
			cos.moveTextPositionByAmount(20, rect.getHeight() - 70 - line2);
			cos.drawString(prop.getProperty("referral59"));
			cos.endText();
			line2 = line2 + 10;

			/*
			 * cos.beginText(); cos.setFont(fontplain, 10);
			 * cos.moveTextPositionByAmount(20,rect.getHeight() -100-line2);
			 * cos.drawString(prop.getProperty("referral60")); cos.endText();
			 * line2=line2+10;
			 */

			cos.beginText();
			cos.setFont(fontplain, 10);
			cos.moveTextPositionByAmount(20, rect.getHeight() - 70 - line2);
			cos.drawString(prop.getProperty("referral61"));
			cos.endText();
			line2 = line2 + 10;

			cos.beginText();
			cos.setFont(fontplain, 10);
			cos.moveTextPositionByAmount(20, rect.getHeight() - 70 - line2);
			cos.drawString(prop.getProperty("referral62"));
			cos.endText();
			line2 = line2 + 10;

			cos.beginText();
			cos.setFont(fontplain, 10);
			cos.moveTextPositionByAmount(20, rect.getHeight() - 70 - line2);
			cos.drawString(prop.getProperty("referral63"));
			cos.endText();
			line2 = line2 + 10;

			cos.beginText();
			cos.setFont(fontplain, 10);
			cos.moveTextPositionByAmount(20, rect.getHeight() - 70 - line2);
			cos.drawString(prop.getProperty("referral64"));
			cos.endText();
			line2 = line2 + 10;

			cos.beginText();
			cos.setFont(fontplain, 10);
			cos.moveTextPositionByAmount(20, rect.getHeight() - 70 - line2);
			cos.drawString(prop.getProperty("referral65"));
			cos.endText();
			line2 = line2 + 15;

			cos.beginText();
			cos.setFont(fontplain, 10);
			cos.moveTextPositionByAmount(20, rect.getHeight() - 70 - line2);
			cos.drawString(prop.getProperty("referral66"));
			cos.endText();
			line2 = line2 + 10;

			cos.beginText();
			cos.setFont(fontplain, 10);
			cos.moveTextPositionByAmount(20, rect.getHeight() - 70 - line2);
			cos.drawString(prop.getProperty("referral67"));
			cos.endText();
			line2 = line2 + 15;

			cos.beginText();
			cos.setFont(fontplain, 10);
			cos.moveTextPositionByAmount(30, rect.getHeight() - 70 - line2);
			cos.drawString(prop.getProperty("referral68"));
			cos.endText();
			line2 = line2 + 15;

			cos.beginText();
			cos.setFont(fontplain, 10);
			cos.moveTextPositionByAmount(20, rect.getHeight() - 70 - line2);
			cos.drawString(prop.getProperty("referral69"));
			cos.endText();
			line2 = line2 + 10;

			cos.beginText();
			cos.setFont(fontplain, 10);
			cos.moveTextPositionByAmount(20, rect.getHeight() - 70 - line2);
			cos.drawString(prop.getProperty("referral70"));
			cos.endText();
			line2 = line2 + 15;

			cos.beginText();
			cos.setFont(fontplain, 10);
			cos.moveTextPositionByAmount(20, rect.getHeight() - 70 - line2);
			cos.drawString(prop.getProperty("referral71"));
			cos.endText();
			line2 = line2 + 10;

			cos.beginText();
			cos.setFont(fontplain, 10);
			cos.moveTextPositionByAmount(20, rect.getHeight() - 70 - line2);
			cos.drawString(prop.getProperty("referral72"));
			cos.endText();
			line2 = line2 + 15;

			cos.beginText();
			cos.setFont(fontplain, 10);
			cos.moveTextPositionByAmount(20, rect.getHeight() - 70 - line2);
			cos.drawString(prop.getProperty("referral73"));
			cos.endText();
			line2 = line2 + 10;

			cos.beginText();
			cos.setFont(fontplain, 10);
			cos.moveTextPositionByAmount(20, rect.getHeight() - 70 - line2);
			cos.drawString(prop.getProperty("referral74"));
			cos.endText();
			line2 = line2 + 10;

			cos.beginText();
			cos.setFont(fontplain, 10);
			cos.moveTextPositionByAmount(20, rect.getHeight() - 70 - line2);
			cos.drawString(prop.getProperty("referral75"));
			cos.endText();
			line2 = line2 + 15;

			cos.beginText();
			cos.setFont(fontplain, 10);
			cos.moveTextPositionByAmount(20, rect.getHeight() - 70 - line2);
			cos.drawString(prop.getProperty("referral76"));
			cos.endText();
			line2 = line2 + 10;

			cos.beginText();
			cos.setFont(fontplain, 10);
			cos.moveTextPositionByAmount(20, rect.getHeight() - 70 - line2);
			cos.drawString(prop.getProperty("referral77"));
			cos.endText();
			line2 = line2 + 10;

			cos.beginText();
			cos.setFont(fontplain, 10);
			cos.moveTextPositionByAmount(20, rect.getHeight() - 70 - line2);
			cos.drawString(prop.getProperty("referral78"));
			cos.endText();
			line2 = line2 + 20;

			cos.beginText();
			cos.setFont(fontplain, 10);
			cos.moveTextPositionByAmount(30, rect.getHeight() - 70 - line2);
			cos.drawString(prop.getProperty("referral79"));
			cos.endText();
			line2 = line2 + 15;

			cos.beginText();
			cos.setFont(fontplain, 10);
			cos.moveTextPositionByAmount(20, rect.getHeight() - 70 - line2);
			cos.drawString(prop.getProperty("referral80"));
			cos.endText();
			line2 = line2 + 10;

			cos.beginText();
			cos.setFont(fontplain, 10);
			cos.moveTextPositionByAmount(20, rect.getHeight() - 70 - line2);
			cos.drawString(prop.getProperty("referral81"));
			cos.endText();
			line2 = line2 + 10;

			cos.beginText();
			cos.setFont(fontplain, 10);
			cos.moveTextPositionByAmount(20, rect.getHeight() - 70 - line2);
			cos.drawString(prop.getProperty("referral82"));
			cos.endText();
			line2 = line2 + 10;

			cos.beginText();
			cos.setFont(fontplain, 10);
			cos.moveTextPositionByAmount(20, rect.getHeight() - 70 - line2);
			cos.drawString(prop.getProperty("referral83"));
			cos.endText();
			line2 = line2 + 15;

			cos.beginText();
			cos.setFont(fontplain, 10);
			cos.moveTextPositionByAmount(30, rect.getHeight() - 70 - line2);
			cos.drawString(prop.getProperty("referral84"));
			cos.endText();
			line2 = line2 + 15;

			cos.beginText();
			cos.setFont(fontplain, 10);
			cos.moveTextPositionByAmount(20, rect.getHeight() - 70 - line2);
			cos.drawString(prop.getProperty("referral85"));
			cos.endText();
			line2 = line2 + 10;

			cos.beginText();
			cos.setFont(fontplain, 10);
			cos.moveTextPositionByAmount(20, rect.getHeight() - 70 - line2);
			cos.drawString(prop.getProperty("referral86"));
			cos.endText();
			line2 = line2 + 15;

			cos.beginText();
			cos.setFont(fontplain, 10);
			cos.moveTextPositionByAmount(20, rect.getHeight() - 70 - line2);
			cos.drawString(prop.getProperty("referral87"));
			cos.endText();
			line2 = line2 + 10;

			cos.beginText();
			cos.setFont(fontplain, 10);
			cos.moveTextPositionByAmount(20, rect.getHeight() - 70 - line2);
			cos.drawString(prop.getProperty("referral88"));
			cos.endText();
			line2 = line2 + 15;

			cos.beginText();
			cos.setFont(fontplain, 10);
			cos.moveTextPositionByAmount(20, rect.getHeight() - 70 - line2);
			cos.drawString(prop.getProperty("referral89"));
			cos.endText();
			line2 = line2 + 10;

			cos.beginText();
			cos.setFont(fontplain, 10);
			cos.moveTextPositionByAmount(20, rect.getHeight() - 70 - line2);
			cos.drawString(prop.getProperty("referral90"));
			cos.endText();
			line2 = line2 + 10;

			cos.beginText();
			cos.setFont(fontplain, 10);
			cos.moveTextPositionByAmount(20, rect.getHeight() - 70 - line2);
			cos.drawString(prop.getProperty("referral91"));
			cos.endText();
			line2 = line2 + 10;

			cos.beginText();
			cos.setFont(fontplain, 10);
			cos.moveTextPositionByAmount(20, rect.getHeight() - 70 - line2);
			cos.drawString(prop.getProperty("referral92"));
			cos.endText();
			line2 = line2 + 10;

			cos.beginText();
			cos.setFont(fontplain, 10);
			cos.moveTextPositionByAmount(20, rect.getHeight() - 70 - line2);
			cos.drawString(prop.getProperty("referral93"));
			cos.endText();
			line2 = line2 + 15;

			cos.beginText();
			cos.setFont(fontplain, 10);
			cos.moveTextPositionByAmount(20, rect.getHeight() - 70 - line2);
			cos.drawString(prop.getProperty("referral94"));
			cos.endText();
			line2 = line2 + 10;

			cos.beginText();
			cos.setFont(fontplain, 10);
			cos.moveTextPositionByAmount(20, rect.getHeight() - 70 - line2);
			cos.drawString(prop.getProperty("referral95"));
			cos.endText();
			line2 = line2 + 10;

			cos.beginText();
			cos.setFont(fontplain, 10);
			cos.moveTextPositionByAmount(20, rect.getHeight() - 70 - line2);
			cos.drawString(prop.getProperty("referral96"));
			cos.endText();
			line2 = line2 + 15;

			cos.beginText();
			cos.setFont(fontplain, 10);
			cos.moveTextPositionByAmount(20, rect.getHeight() - 70 - line2);
			cos.drawString(prop.getProperty("referral97"));
			cos.endText();
			line2 = line2 + 10;

			cos.beginText();
			cos.setFont(fontplain, 10);
			cos.moveTextPositionByAmount(20, rect.getHeight() - 70 - line2);
			cos.drawString(prop.getProperty("referral98"));
			cos.endText();
			line2 = line2 + 15;

			cos.beginText();
			cos.setFont(fontplain, 10);
			cos.moveTextPositionByAmount(20, rect.getHeight() - 70 - line2);
			cos.drawString(prop.getProperty("referral99"));
			cos.endText();
			line2 = line2 + 10;

			cos.beginText();
			cos.setFont(fontplain, 10);
			cos.moveTextPositionByAmount(20, rect.getHeight() - 70 - line2);
			cos.drawString(prop.getProperty("referral70"));
			cos.endText();
			line2 = line2 + 20;

			cos.beginText();
			cos.setFont(fontplain, 10);
			cos.moveTextPositionByAmount(20, rect.getHeight() - 70 - line2);
			cos.drawString(prop.getProperty("referral101"));
			cos.endText();
			line2 = line2 + 15;

			cos.beginText();
			cos.setFont(fontplain, 10);
			cos.moveTextPositionByAmount(20, rect.getHeight() - 70 - line2);
			cos.drawString(prop.getProperty("referral102"));
			cos.endText();
			line2 = line2 + 10;

			cos.beginText();
			cos.setFont(fontplain, 10);
			cos.moveTextPositionByAmount(20, rect.getHeight() - 70 - line2);
			cos.drawString(prop.getProperty("referral103"));
			cos.endText();
			line2 = line2 + 10;

			cos.beginText();
			cos.setFont(fontplain, 10);
			cos.moveTextPositionByAmount(20, rect.getHeight() - 70 - line2);
			cos.drawString(prop.getProperty("referral104"));
			cos.endText();
			line2 = line2 + 15;

			cos.beginText();
			cos.setFont(fontplain, 10);
			cos.moveTextPositionByAmount(20, rect.getHeight() - 70 - line2);
			cos.drawString(prop.getProperty("referral105"));
			cos.endText();
			line2 = line2 + 10;

			cos.beginText();
			cos.setFont(fontplain, 10);
			cos.moveTextPositionByAmount(20, rect.getHeight() - 70 - line2);
			cos.drawString(prop.getProperty("referral106"));
			cos.endText();
			line2 = line2 + 15;

			cos.beginText();
			cos.setFont(fontplain, 10);
			cos.moveTextPositionByAmount(20, rect.getHeight() - 70 - line2);
			cos.drawString(prop.getProperty("referral107"));
			cos.endText();
			line2 = line2 + 10;

			cos.beginText();
			cos.setFont(fontplain, 10);
			cos.moveTextPositionByAmount(20, rect.getHeight() - 70 - line2);
			cos.drawString(prop.getProperty("referral108"));
			cos.endText();
			line2 = line2 + 10;

			cos.beginText();
			cos.setFont(fontplain, 10);
			cos.moveTextPositionByAmount(20, rect.getHeight() - 70 - line2);
			cos.drawString(prop.getProperty("referral109"));
			cos.endText();
			line2 = line2 + 10;

			cos.beginText();
			cos.setFont(fontplain, 10);
			cos.moveTextPositionByAmount(20, rect.getHeight() - 70 - line2);
			cos.drawString(prop.getProperty("referral110"));
			cos.endText();
			line2 = line2 + 15;

			cos.beginText();
			cos.setFont(fontplain, 10);
			cos.moveTextPositionByAmount(20, rect.getHeight() - 70 - line2);
			cos.drawString(prop.getProperty("referral111"));
			cos.endText();
			line2 = line2 + 15;

			cos.beginText();
			cos.setFont(fontplain, 10);
			cos.moveTextPositionByAmount(20, rect.getHeight() - 70 - line2);
			cos.drawString(prop.getProperty("referral112"));
			cos.endText();
			line2 = line2 + 10;

			cos.beginText();
			cos.setFont(fontplain, 10);
			cos.moveTextPositionByAmount(20, rect.getHeight() - 70 - line2);
			cos.drawString(prop.getProperty("referral113"));
			cos.endText();
			line2 = line2 + 20;

			cos.beginText();
			cos.setFont(fontplain, 10);
			cos.moveTextPositionByAmount(30, rect.getHeight() - 70 - line2);
			cos.drawString(prop.getProperty("referral114"));
			cos.endText();
			line2 = line2 + 15;

			cos.beginText();
			cos.setFont(fontplain, 10);
			cos.moveTextPositionByAmount(20, rect.getHeight() - 70 - line2);
			cos.drawString(prop.getProperty("referral115"));
			cos.endText();
			line2 = line2 + 10;

			cos.beginText();
			cos.setFont(fontplain, 10);
			cos.moveTextPositionByAmount(20, rect.getHeight() - 70 - line2);
			cos.drawString(prop.getProperty("referral116"));
			cos.endText();
			line2 = line2 + 15;

			cos.beginText();
			cos.setFont(fontplain, 10);
			cos.moveTextPositionByAmount(20, rect.getHeight() - 70 - line2);
			cos.drawString(prop.getProperty("referral117"));
			cos.endText();
			line2 = line2 + 10;

			cos.beginText();
			cos.setFont(fontplain, 10);
			cos.moveTextPositionByAmount(20, rect.getHeight() - 70 - line2);
			cos.drawString(prop.getProperty("referral118"));
			cos.endText();
			line2 = line2 + 15;

			cos.close();

			PDPage page3 = new PDPage(PDPage.PAGE_SIZE_A4);
			document.addPage(page3);
			cos = new PDPageContentStream(document, page3);

			line2 = 0;
			cos.beginText();
			cos.setFont(fontplain, 10);
			cos.moveTextPositionByAmount(20, rect.getHeight() - 70 - line2);
			cos.drawString(prop.getProperty("referral119"));
			cos.endText();
			line2 = line2 + 10;

			cos.beginText();
			cos.setFont(fontplain, 10);
			cos.moveTextPositionByAmount(20, rect.getHeight() - 70 - line2);
			cos.drawString(prop.getProperty("referral120"));
			cos.endText();
			line2 = line2 + 10;

			cos.beginText();
			cos.setFont(fontplain, 10);
			cos.moveTextPositionByAmount(20, rect.getHeight() - 70 - line2);
			cos.drawString(prop.getProperty("referral121"));
			cos.endText();
			line2 = line2 + 15;

			cos.beginText();
			cos.setFont(fontplain, 10);
			cos.moveTextPositionByAmount(20, rect.getHeight() - 70 - line2);
			cos.drawString(prop.getProperty("referral122"));
			cos.endText();
			line2 = line2 + 10;

			cos.beginText();
			cos.setFont(fontplain, 10);
			cos.moveTextPositionByAmount(20, rect.getHeight() - 70 - line2);
			cos.drawString(prop.getProperty("referral123"));
			cos.endText();
			line2 = line2 + 15;

			cos.beginText();
			cos.setFont(fontplain, 10);
			cos.moveTextPositionByAmount(20, rect.getHeight() - 70 - line2);
			cos.drawString(prop.getProperty("referral124"));
			cos.endText();
			line2 = line2 + 10;

			cos.beginText();
			cos.setFont(fontplain, 10);
			cos.moveTextPositionByAmount(20, rect.getHeight() - 70 - line2);
			cos.drawString(prop.getProperty("referral125"));
			cos.endText();
			line2 = line2 + 10;

			cos.beginText();
			cos.setFont(fontplain, 10);
			cos.moveTextPositionByAmount(20, rect.getHeight() - 70 - line2);
			cos.drawString(prop.getProperty("referral126"));
			cos.endText();
			line2 = line2 + 15;

			cos.beginText();
			cos.setFont(fontplain, 10);
			cos.moveTextPositionByAmount(20, rect.getHeight() - 70 - line2);
			cos.drawString(prop.getProperty("referral127"));
			cos.endText();
			line2 = line2 + 10;

			cos.beginText();
			cos.setFont(fontplain, 10);
			cos.moveTextPositionByAmount(20, rect.getHeight() - 70 - line2);
			cos.drawString(prop.getProperty("referral128"));
			cos.endText();
			line2 = line2 + 15;

			cos.beginText();
			cos.setFont(fontplain, 10);
			cos.moveTextPositionByAmount(20, rect.getHeight() - 70 - line2);
			cos.drawString(prop.getProperty("referral129"));
			cos.endText();
			line2 = line2 + 15;

			cos.beginText();
			cos.setFont(fontplain, 10);
			cos.moveTextPositionByAmount(20, rect.getHeight() - 70 - line2);
			cos.drawString(prop.getProperty("referral130"));
			cos.endText();
			line2 = line2 + 10;

			cos.beginText();
			cos.setFont(fontplain, 10);
			cos.moveTextPositionByAmount(20, rect.getHeight() - 70 - line2);
			cos.drawString(prop.getProperty("referral131"));
			cos.endText();
			line2 = line2 + 10;

			cos.beginText();
			cos.setFont(fontplain, 10);
			cos.moveTextPositionByAmount(20, rect.getHeight() - 70 - line2);
			cos.drawString(prop.getProperty("referral132"));
			cos.endText();
			line2 = line2 + 10;

			cos.beginText();
			cos.setFont(fontplain, 10);
			cos.moveTextPositionByAmount(20, rect.getHeight() - 70 - line2);
			cos.drawString(prop.getProperty("referral133"));
			cos.endText();
			line2 = line2 + 15;

			cos.beginText();
			cos.setFont(fontplain, 10);
			cos.moveTextPositionByAmount(20, rect.getHeight() - 70 - line2);
			cos.drawString(prop.getProperty("referral134"));
			cos.endText();
			line2 = line2 + 10;

			cos.beginText();
			cos.setFont(fontplain, 10);
			cos.moveTextPositionByAmount(20, rect.getHeight() - 70 - line2);
			cos.drawString(prop.getProperty("referral135"));
			cos.endText();
			line2 = line2 + 15;

			cos.beginText();
			cos.setFont(fontplain, 10);
			cos.moveTextPositionByAmount(20, rect.getHeight() - 70 - line2);
			cos.drawString(prop.getProperty("referral136"));
			cos.endText();
			line2 = line2 + 10;

			cos.beginText();
			cos.setFont(fontplain, 10);
			cos.moveTextPositionByAmount(20, rect.getHeight() - 70 - line2);
			cos.drawString(prop.getProperty("referral137"));
			cos.endText();
			line2 = line2 + 15;

			cos.beginText();
			cos.setFont(fontplain, 10);
			cos.moveTextPositionByAmount(20, rect.getHeight() - 70 - line2);
			cos.drawString(prop.getProperty("referral138"));
			cos.endText();
			line2 = line2 + 10;

			cos.beginText();
			cos.setFont(fontplain, 10);
			cos.moveTextPositionByAmount(20, rect.getHeight() - 70 - line2);
			cos.drawString(prop.getProperty("referral139"));
			cos.endText();
			line2 = line2 + 10;

			cos.beginText();
			cos.setFont(fontplain, 10);
			cos.moveTextPositionByAmount(20, rect.getHeight() - 70 - line2);
			cos.drawString(prop.getProperty("referral140"));
			cos.endText();
			line2 = line2 + 10;

			cos.beginText();
			cos.setFont(fontplain, 10);
			cos.moveTextPositionByAmount(20, rect.getHeight() - 70 - line2);
			cos.drawString(prop.getProperty("referral142"));
			cos.endText();
			line2 = line2 + 10;

			cos.beginText();
			cos.setFont(fontplain, 10);
			cos.moveTextPositionByAmount(20, rect.getHeight() - 70 - line2);
			cos.drawString(prop.getProperty("referral143"));
			cos.endText();
			line2 = line2 + 10;

			cos.beginText();
			cos.setFont(fontplain, 10);
			cos.moveTextPositionByAmount(20, rect.getHeight() - 70 - line2);
			cos.drawString(prop.getProperty("referral144"));
			cos.endText();
			line2 = line2 + 10;

			cos.beginText();
			cos.setFont(fontplain, 10);
			cos.moveTextPositionByAmount(20, rect.getHeight() - 70 - line2);
			cos.drawString(prop.getProperty("referral145"));
			cos.endText();
			line2 = line2 + 10;

			cos.beginText();
			cos.setFont(fontplain, 10);
			cos.moveTextPositionByAmount(20, rect.getHeight() - 70 - line2);
			cos.drawString(prop.getProperty("referral146"));
			cos.endText();
			line2 = line2 + 10;

			cos.beginText();
			cos.setFont(fontplain, 10);
			cos.moveTextPositionByAmount(20, rect.getHeight() - 70 - line2);
			cos.drawString(prop.getProperty("referral147"));
			cos.endText();
			line2 = line2 + 15;

			cos.beginText();
			cos.setFont(fontplain, 10);
			cos.moveTextPositionByAmount(20, rect.getHeight() - 70 - line2);
			cos.drawString(prop.getProperty("referral148"));
			cos.endText();
			line2 = line2 + 10;

			cos.beginText();
			cos.setFont(fontplain, 10);
			cos.moveTextPositionByAmount(20, rect.getHeight() - 70 - line2);
			cos.drawString(prop.getProperty("referral149"));
			cos.endText();
			line2 = line2 + 10;

			cos.beginText();
			cos.setFont(fontplain, 10);
			cos.moveTextPositionByAmount(20, rect.getHeight() - 70 - line2);
			cos.drawString(prop.getProperty("referral150"));
			cos.endText();
			line2 = line2 + 10;

			cos.beginText();
			cos.setFont(fontplain, 10);
			cos.moveTextPositionByAmount(20, rect.getHeight() - 70 - line2);
			cos.drawString(prop.getProperty("referral151"));
			cos.endText();
			line2 = line2 + 10;

			cos.beginText();
			cos.setFont(fontplain, 10);
			cos.moveTextPositionByAmount(20, rect.getHeight() - 70 - line2);
			cos.drawString(prop.getProperty("referral152"));
			cos.endText();
			line2 = line2 + 10;

			cos.beginText();
			cos.setFont(fontplain, 10);
			cos.moveTextPositionByAmount(20, rect.getHeight() - 70 - line2);
			cos.drawString(prop.getProperty("referral153"));
			cos.endText();
			line2 = line2 + 10;

			cos.beginText();
			cos.setFont(fontplain, 10);
			cos.moveTextPositionByAmount(20, rect.getHeight() - 70 - line2);
			cos.drawString(prop.getProperty("referral154"));
			cos.endText();
			line2 = line2 + 10;

			cos.beginText();
			cos.setFont(fontplain, 10);
			cos.moveTextPositionByAmount(20, rect.getHeight() - 70 - line2);
			cos.drawString(prop.getProperty("referral155"));
			cos.endText();
			line2 = line2 + 15;

			cos.beginText();
			cos.setFont(fontplain, 10);
			cos.moveTextPositionByAmount(20, rect.getHeight() - 70 - line2);
			cos.drawString(prop.getProperty("referral156"));
			cos.endText();
			line2 = line2 + 10;

			cos.beginText();
			cos.setFont(fontplain, 10);
			cos.moveTextPositionByAmount(20, rect.getHeight() - 70 - line2);
			cos.drawString(prop.getProperty("referral157"));
			cos.endText();
			line2 = line2 + 15;

			cos.beginText();
			cos.setFont(fontplain, 10);
			cos.moveTextPositionByAmount(20, rect.getHeight() - 70 - line2);
			cos.drawString(prop.getProperty("referral158"));
			cos.endText();
			line2 = line2 + 10;

			cos.beginText();
			cos.setFont(fontplain, 10);
			cos.moveTextPositionByAmount(20, rect.getHeight() - 70 - line2);
			cos.drawString(prop.getProperty("referral159"));
			cos.endText();
			line2 = line2 + 10;

			cos.beginText();
			cos.setFont(fontplain, 10);
			cos.moveTextPositionByAmount(20, rect.getHeight() - 70 - line2);
			cos.drawString(prop.getProperty("referral160"));
			cos.endText();
			line2 = line2 + 20;

			cos.beginText();
			cos.setFont(fontplain, 10);
			cos.moveTextPositionByAmount(20, rect.getHeight() - 70 - line2);
			cos.drawString(prop.getProperty("referral161"));
			cos.endText();
			line2 = line2 + 20;

			cos.beginText();
			cos.setFont(fontplain, 10);
			cos.moveTextPositionByAmount(20, rect.getHeight() - 70 - line2);
			cos.drawString(prop.getProperty("referral162"));
			cos.endText();
			line2 = line2 + 15;

			cos.beginText();
			cos.setFont(fontplain, 10);
			cos.moveTextPositionByAmount(30, rect.getHeight() - 70 - line2);
			cos.drawString(prop.getProperty("referral163"));
			cos.endText();
			line2 = line2 + 15;

			cos.beginText();
			cos.setFont(fontplain, 10);
			cos.moveTextPositionByAmount(20, rect.getHeight() - 70 - line2);
			cos.drawString(prop.getProperty("referral164"));
			cos.endText();
			line2 = line2 + 10;

			cos.beginText();
			cos.setFont(fontplain, 10);
			cos.moveTextPositionByAmount(20, rect.getHeight() - 70 - line2);
			cos.drawString(prop.getProperty("referral165"));
			cos.endText();
			line2 = line2 + 10;

			cos.beginText();
			cos.setFont(fontplain, 10);
			cos.moveTextPositionByAmount(20, rect.getHeight() - 70 - line2);
			cos.drawString(prop.getProperty("referral166"));
			cos.endText();
			line2 = line2 + 15;

			cos.beginText();
			cos.setFont(fontplain, 10);
			cos.moveTextPositionByAmount(30, rect.getHeight() - 70 - line2);
			cos.drawString(prop.getProperty("referral167"));
			cos.endText();
			line2 = line2 + 15;

			cos.beginText();
			cos.setFont(fontplain, 10);
			cos.moveTextPositionByAmount(20, rect.getHeight() - 70 - line2);
			cos.drawString(prop.getProperty("referral168"));
			cos.endText();
			line2 = line2 + 10;

			cos.beginText();
			cos.setFont(fontplain, 10);
			cos.moveTextPositionByAmount(20, rect.getHeight() - 70 - line2);
			cos.drawString(prop.getProperty("referral169"));
			cos.endText();
			line2 = line2 + 15;

			cos.beginText();
			cos.setFont(fontplain, 10);
			cos.moveTextPositionByAmount(30, rect.getHeight() - 70 - line2);
			cos.drawString(prop.getProperty("referral170"));
			cos.endText();
			line2 = line2 + 15;

			cos.beginText();
			cos.setFont(fontplain, 10);
			cos.moveTextPositionByAmount(20, rect.getHeight() - 70 - line2);
			cos.drawString(prop.getProperty("referral171"));
			cos.endText();
			line2 = line2 + 10;

			cos.beginText();
			cos.setFont(fontplain, 10);
			cos.moveTextPositionByAmount(20, rect.getHeight() - 70 - line2);
			cos.drawString(prop.getProperty("referral172"));
			cos.endText();
			line2 = line2 + 10;

			cos.beginText();
			cos.setFont(fontplain, 10);
			cos.moveTextPositionByAmount(20, rect.getHeight() - 70 - line2);
			cos.drawString(prop.getProperty("referral173"));
			cos.endText();
			line2 = line2 + 15;

			cos.beginText();
			cos.setFont(fontplain, 10);
			cos.moveTextPositionByAmount(20, rect.getHeight() - 70 - line2);
			cos.drawString(prop.getProperty("referral174"));
			cos.endText();
			line2 = line2 + 10;

			cos.beginText();
			cos.setFont(fontplain, 10);
			cos.moveTextPositionByAmount(20, rect.getHeight() - 70 - line2);
			cos.drawString(prop.getProperty("referral175"));
			cos.endText();
			line2 = line2 + 15;

			cos.beginText();
			cos.setFont(fontplain, 10);
			cos.moveTextPositionByAmount(20, rect.getHeight() - 70 - line2);
			cos.drawString(prop.getProperty("referral176"));
			cos.endText();
			line2 = line2 + 10;

			cos.beginText();
			cos.setFont(fontplain, 10);
			cos.moveTextPositionByAmount(20, rect.getHeight() - 70 - line2);
			cos.drawString(prop.getProperty("referral177"));
			cos.endText();
			line2 = line2 + 15;

			cos.beginText();
			cos.setFont(fontplain, 10);
			cos.moveTextPositionByAmount(20, rect.getHeight() - 70 - line2);
			cos.drawString(prop.getProperty("referral178"));
			cos.endText();
			line2 = line2 + 10;

			cos.beginText();
			cos.setFont(fontplain, 10);
			cos.moveTextPositionByAmount(20, rect.getHeight() - 70 - line2);
			cos.drawString(prop.getProperty("referral179"));
			cos.endText();
			line2 = line2 + 15;

			cos.beginText();
			cos.setFont(fontplain, 10);
			cos.moveTextPositionByAmount(20, rect.getHeight() - 70 - line2);
			cos.drawString(prop.getProperty("referral180"));
			cos.endText();
			line2 = line2 + 10;

			cos.beginText();
			cos.setFont(fontplain, 10);
			cos.moveTextPositionByAmount(20, rect.getHeight() - 70 - line2);
			cos.drawString(prop.getProperty("referral181"));
			cos.endText();
			line2 = line2 + 15;
			cos.close();

			PDPage page4 = new PDPage(PDPage.PAGE_SIZE_A4);
			document.addPage(page4);
			cos = new PDPageContentStream(document, page4);
			line2 = 0;
			cos.beginText();
			cos.setFont(fontplain, 10);
			cos.moveTextPositionByAmount(20, rect.getHeight() - 70 - line2);
			cos.drawString(prop.getProperty("referral182"));
			cos.endText();
			line2 = line2 + 15;

			cos.beginText();
			cos.setFont(fontplain, 10);
			cos.moveTextPositionByAmount(30, rect.getHeight() - 70 - line2);
			cos.drawString(prop.getProperty("referral183"));
			cos.endText();
			line2 = line2 + 20;

			cos.beginText();
			cos.setFont(fontplain, 10);
			cos.moveTextPositionByAmount(20, rect.getHeight() - 70 - line2);
			cos.drawString(prop.getProperty("referral184"));
			cos.endText();
			line2 = line2 + 10;

			cos.beginText();
			cos.setFont(fontplain, 10);
			cos.moveTextPositionByAmount(20, rect.getHeight() - 70 - line2);
			cos.drawString(prop.getProperty("referral185"));
			cos.endText();
			line2 = line2 + 10;

			cos.beginText();
			cos.setFont(fontplain, 10);
			cos.moveTextPositionByAmount(20, rect.getHeight() - 70 - line2);
			cos.drawString(prop.getProperty("referral186"));
			cos.endText();
			line2 = line2 + 10;

			cos.beginText();
			cos.setFont(fontplain, 10);
			cos.moveTextPositionByAmount(20, rect.getHeight() - 70 - line2);
			cos.drawString(prop.getProperty("referral187"));
			cos.endText();
			line2 = line2 + 15;

			cos.beginText();
			cos.setFont(fontplain, 10);
			cos.moveTextPositionByAmount(30, rect.getHeight() - 70 - line2);
			cos.drawString(prop.getProperty("referral188"));
			cos.endText();
			line2 = line2 + 10;

			cos.beginText();
			cos.setFont(fontplain, 10);
			cos.moveTextPositionByAmount(20, rect.getHeight() - 70 - line2);
			cos.drawString(prop.getProperty("referral189"));
			cos.endText();
			line2 = line2 + 10;

			cos.beginText();
			cos.setFont(fontplain, 10);
			cos.moveTextPositionByAmount(20, rect.getHeight() - 70 - line2);
			cos.drawString(prop.getProperty("referral190"));
			cos.endText();
			line2 = line2 + 25;
			line2 = line2 + 20;
			cos.beginText();
			cos.setFont(fontplain, 10);
			cos.moveTextPositionByAmount(20, rect.getHeight() - 70 - line2);
			cos.drawString("Schedule B");
			cos.endText();
			line2 = line2 + 15;

			cos.beginText();
			cos.setFont(fontplain, 10);
			cos.moveTextPositionByAmount(200, rect.getHeight() - 70 - line2);
			cos.drawString("REFERRAL FEE SCHEDULE");
			cos.endText();
			line2 = line2 + 20;

			if (role.equals("Client") || role.equals("Customer")) {

				String[][] content = {
						{ " Minimum Mortgage Value", " Maximum Mortgage Value",
								"    Referral Fee", "  Renewal Fee" },
						{ "$50,000.00", "$200,000.00", "$50.00", "$25.00" },
						{ "$200,001.00", "$350,000.00", "$125.00", "$50.00" },
						{ "$350,001.00", "$600,000.00", "$250.00", "$125.00" },
						{ "$600,001.00", "$900,000.00", "$350.00", "$175.00" },
						{ "$900,001.00", "$3,000,000.00", "$500.00", "$250.00" } };

				PdfTableCreation.PdfTableCreationMethod(page4, cos,
						rect.getHeight() - 70 - line2, 20, content);

			} else if (role.equals("Realtor")
					|| role.equals("Financial Planner")
					|| role.equals("Builder") || role.equals("Professional")) {

				String[][] content1 = {
						{ " Minimum Mortgage Value", " Maximum Mortgage Value",
								"    Referral Fee", "  Renewal Fee" },
						{ "$50,000.00", "$200,000.00", "$100.00", "$50.00" },
						{ "$200,001.00", "$350,000.00", "$250.00", "$100.00" },
						{ "$350,001.00", "$600,000.00", "$500.00", "$225.00" },
						{ "$600,001.00", "$900,000.00", "$750.00", "$350.00" },
						{ "$900,001.00", "$3,000,000.00", "$1,000.00",
								"$500.00" } };

				PdfTableCreation.PdfTableCreationMethod(page4, cos,
						rect.getHeight() - 70 - line2, 20, content1);
			}

			line2 = line2 + 170;
			cos.beginText();
			cos.setFont(fontbold, 10);
			cos.moveTextPositionByAmount(60, rect.getHeight() - 70 - line2);
			cos.drawString("Referrer Signature :");
			cos.endText();
			line2 = line2 + 110;

			if (areusingTouchScreenDevice.equalsIgnoreCase("yes")) {
				try {
					// BufferedImage awtImage = ImageIO.read(new
					// File("simple.jpg"));
					PDXObjectImage ximage = new PDPixelMap(document, image);
					float scale = 0.5f; // alter this value to set the image
										// size
					cos.drawXObject(ximage, 50, rect.getHeight() - 70 - line2,
							ximage.getWidth() * scale, ximage.getHeight()
									* scale);
				} catch (Exception fnfex) {
					Logger.debug("No image for you");
				}
				cos.close();
			} else {

				cos.beginText();
				cos.setFont(fontbold, 10);
				cos.moveTextPositionByAmount(90, rect.getHeight() - 70 - line2
						+ 80);
				cos.drawString(myTypedName);
				cos.endText();
				line2 = line2 + 20;

				cos.beginText();
				cos.setFont(fontplain, 10);
				cos.moveTextPositionByAmount(60, rect.getHeight() - 70 - line2
						+ 70);
				cos.drawString("My typed name above serves as my electronic signature for the above agreement.");
				cos.endText();
				cos.close();
			}

			/*
			 * System.out.println("daoument "+document.toString()+"ddd"+document.
			 * getCurrentAccessPermission
			 * ()+"do "+document.getDocumentInformation() +"doc ");
			 */
			Logger.debug("OutPut FileName********" + outputFileName);
			document.save(outputFileName);
			Logger.debug("OutPut FileName&&&&&&&&&&&" + outputFileName);

			document.close();
			System.out
					.println("OutPut FileName##############" + outputFileName);

			try {
				File fl = new File(outputFileName);
				Logger.debug("size " + fl.exists() + ""
						+ (file.length() / 1024));
			} catch (Exception e) {

			}
			/* Logger.debug("The path pdf file created"); */
			// return outputFileName;
		} catch (Exception e) {
			e.printStackTrace();

		}

		return outputFileName;

	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	public static Result wfg1() {
		DynamicForm dynamicForm = form().bindFromRequest();

		String role = dynamicForm.get("role");
		String name = dynamicForm.get("fname");
		String lastname = dynamicForm.get("lname");
		String email = dynamicForm.get("email");
		String phoneNumber = dynamicForm.get("phone");
		String nameOfBuilderYouWork = dynamicForm.get("broker");
		String compnesation = dynamicForm.get("feeTo");
		String companyName = dynamicForm.get("company");

		// String AreYouLicensedAsRealtor = req.getParameter("q27_areYou");
		String nameOfTheBrokerageWhereYouWork = dynamicForm.get("broker");
		String AddressOfTheBrokerageWhereYouWork = dynamicForm
				.get("formatted_address");
		String ip = dynamicForm.get("ip");

		JSONObject jsonDataOfIp = null;

		String userIp = "";
		String latitude = "";
		String longitude = "";

		try {
			jsonDataOfIp = new JSONObject(ip);
			userIp = jsonDataOfIp.getString("ip");
			latitude = jsonDataOfIp.getString("latitude");
			longitude = jsonDataOfIp.getString("longitude");
			Logger.debug("ip----------------" + ip);
			Logger.debug("latitude----------------" + latitude);
			Logger.debug("longitude----------------" + longitude);

		} catch (NullPointerException e) {

		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		String brokerPhoneNumber = dynamicForm.get("brokerPhone");
		String WhoIsThebrokerThatManagesTheBrokerageWhereYouWork = dynamicForm
				.get("brokerMange");
		String referrer = dynamicForm.get("refer");

		String addressArray[] = request().body().asFormUrlEncoded()
				.get("formatted_address");
		String addressFromArray = "";
		if (addressArray != null) {
			for (int i = 0; i < addressArray.length; i++) {
				addressFromArray = addressArray[i];
			}

		}
		HashMap<String, String> address = new Address()
				.getProperAddressTwo(addressFromArray);
		Logger.debug("nameOfTheBrokerageWhereYouWork  "
				+ nameOfTheBrokerageWhereYouWork
				+ " AddressOfTheBrokerageWhereYouWork " + addressFromArray);
		Logger.debug("brokerPhoneNumber  " + brokerPhoneNumber
				+ " WhoIsThebrokerThatManagesTheBrokerageWhereYouWork "
				+ WhoIsThebrokerThatManagesTheBrokerageWhereYouWork);

		Logger.debug("inside second visdom referrel servlet");

		Logger.debug("both forms are filled by same person");
		System.out.println("role " + role);
		Logger.info("Role" + role);

		Logger.info("First Name" + name);

		Logger.info("Last Name" + lastname);

		Logger.info("Email" + email);
		Logger.info("phone number" + phoneNumber);
		Logger.info(" Name of Builder" + nameOfBuilderYouWork);

		Logger.info("compnesation is Given " + compnesation);

		Logger.info("realtorBrokerage Work address "
				+ AddressOfTheBrokerageWhereYouWork);

		Logger.info("name of the brokarage where you work"
				+ nameOfTheBrokerageWhereYouWork);
		Logger.info("realtorBroker Phone" + brokerPhoneNumber);
		Logger.info("company name" + companyName);

		Logger.info("WhoIsThebrokerThatManagesTheBrokerageWhereYouWork"
				+ WhoIsThebrokerThatManagesTheBrokerageWhereYouWork);
		Logger.info("Who Referrer To Visdom " + referrer);

		// creating objrcts of supporting classes
		ReferalToCreateReferralResoursce referalToCreateReferralResoursce = new ReferalToCreateReferralResoursce();
		GenericHelperClass gHelperClass = new GenericHelperClass();
		String roleCompany = "";
		try {
			if (role.equalsIgnoreCase("Realtor")) {
				compnesation = "Company";

			} else if (role.equalsIgnoreCase("Professional")) {

				compnesation = "Direct to Myself";
			}
		} catch (NullPointerException e) {
			Logger.error("null pointer exception when getting role data " + e);
		}

		// store value in hashmap to send data to second and third form
		// storeSubmitReferalDataInCouchBase()
		HashMap data = new HashMap();
		data.put("firstname", name);
		data.put("lastName", lastname);
		data.put("email", email);
		data.put("phoneNumber", phoneNumber);
		data.put("companyname", referalToCreateReferralResoursce
				.getCompanyName(nameOfTheBrokerageWhereYouWork, companyName));
		data.put("companyAddress", addressFromArray);

		data.put("compansation", compnesation);
		data.put("companyPhoneNumber", brokerPhoneNumber);
		data.put("broker_Manages_urwork",
				WhoIsThebrokerThatManagesTheBrokerageWhereYouWork);
		data.put("referrer_Tovisdom", referrer);
		data.put("RefrralProgress", "50%");
		data.put("ip", userIp);
		data.put("latitude", latitude);
		data.put("longitude", longitude);
		int referralExsist = 0;

		// storing value to hash map to send data to thrird form
		HashMap message = new HashMap();
		int referrelID = 0;
		int contactId = 0;
		try {

			int referrerResPartnerId = 0;

			// Getting the partnerId of the one who referrer to visdom

			referrerResPartnerId = referalToCreateReferralResoursce
					.getExsistingResPartnerId(referrer);

			// retrving the contact id based on firstname lastname and
			// email
			contactId = gHelperClass.lookupContactID(email, name, lastname,
					"res.partner");

			// if contact id is not exsist for firstname and lastname
			if (contactId == 0) {
				// creating new contact and returning the contactid
				contactId = gHelperClass.createContact(name, lastname, email,
						"res.partner");
				Logger.info("Contact is created with this Id " + contactId);
				// creating new referrer and returning the referrelId
				referrelID = gHelperClass.createRefereal(name, lastname, email,
						contactId, phoneNumber, referrer, role,
						referalToCreateReferralResoursce.getCompanyName(

						nameOfTheBrokerageWhereYouWork, companyName),
						brokerPhoneNumber, referrerResPartnerId, address,
						"hr.applicant");
				Logger.info("Referrer is created with this Id " + referrelID);

			} else {

				// Checking contact is exsist in referrerl Resource
				// based on firstname or last name or fullname and
				// returning the referrelid if exsist else returning 0
				referrelID = gHelperClass.lookupReferalResource(contactId,
						"hr.applicant");

				if (referrelID == 0) {
					Logger.debug("if contact(res.partner) is Exsist");

					// inserting data to referrla resoursce and
					// returning referral id
					referrelID = gHelperClass.createRefereal(name, lastname,
							email, contactId, phoneNumber, referrer, role,
							referalToCreateReferralResoursce.getCompanyName(

							nameOfTheBrokerageWhereYouWork, companyName),
							brokerPhoneNumber, referrerResPartnerId, address,
							"hr.applicant");
					Logger.info("Referrer is created with this Id "
							+ referrelID);

				} else {

					Logger.info("Referrer is exsist  with this Id "
							+ referrelID);
					// updating the referrer and returning referrel id
					message = referalToCreateReferralResoursce
							.updateReferrralDetails(name, lastname, email,
									contactId, phoneNumber, referrer, role,
									referalToCreateReferralResoursce
											.getCompanyName(

											nameOfTheBrokerageWhereYouWork,
													companyName),
									brokerPhoneNumber, referrerResPartnerId,
									address, "hr.applicant");
					Logger.info("Referrer details are updted  based on this Id "
							+ message.get("referrelId"));
				}

			}

			String formType = "Visdom_Referrel";

			CouchBaseService storeData = new CouchBaseService();
			String key = formType + "_" + referrelID;

			JsonObject jsonObject=JsonObject.from(data);
			jsonObject.put("FormType", formType);
			storeData.storeDataToCouchbase(key, jsonObject);

		} catch (Exception e) {
			Logger.error("erro in visdomReferralFormone Controller  " + e);

		}

		return ok(wfg2.render(role, referrelID, referralExsist));
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	public static Result wfg2() {

		String formType = "Visdom_Referrel";

		DynamicForm dynamicForm = form().bindFromRequest();

		String role = dynamicForm.get("role");

		String referrelId = dynamicForm.get("referrelID");
		String areUsingtouchScreenDevice = dynamicForm.get("touchScreen");
		String signature = dynamicForm.get("typedName");
		String elctornicSign = dynamicForm.get("sign");
		int referralExsist = 0;
		try {
			referralExsist = Integer.parseInt(dynamicForm.get("exsist"));
		} catch (Exception e) {
			Logger.error("error parsing to intrger" + e);
		}
		BufferedImage image = null;
		String pdffilepath = "";
		Logger.debug("touchScreen data ----" + dynamicForm.get("touchScreen"));
		Logger.debug("agree data ----" + dynamicForm.get("agree"));
		Logger.debug("dynanmic data ----" + dynamicForm.get("typedName"));
		Logger.debug("referral exist data ----" + dynamicForm.get("exsist"));

		Logger.debug("Role :" + role);
		Logger.debug("ReferrelId :" + referrelId);
		Logger.debug("Are You Using Touch Screen Device :"
				+ areUsingtouchScreenDevice);
		Logger.debug("Typed Signature :" + signature);
		SendWithUsExample sendwithus = new SendWithUsExample();
		String ip = "";
		String roleid = "";
		int ontraPortRoleId = 0;
		try {
			if (role.equalsIgnoreCase("Realtor")) {
				roleid = "realtor";
				ontraPortRoleId = 8;

			} else if (role.equalsIgnoreCase("Financial Planner")) {

				roleid = "financial_planner";
				ontraPortRoleId = 7;
			} else if (role.equalsIgnoreCase("Client")) {

				roleid = "client";
				ontraPortRoleId = 6;
			}

			else {
				roleid = "other";

			}
			CouchBaseService storeData = new CouchBaseService();
			String key = formType + "_" + referrelId;

			JsonObject json = null;
			try {
				json = storeData.getCouhbaseDataByKey(key);

				ip = json.getString("ip");

			} catch (Exception e) {
				Logger.error("error in getting the couhbase data" + e);
				// try for second time to gett data
				try {
					json = storeData.getCouhbaseDataByKey(key);

					ip = json.getString("ip");
				} catch (Exception e1) {
					Logger.error("error in getting the couhbase data" + e1);
					// Todo send mail if fails second time also to get couhbase
					// data
				}

			}

			GenericHelperClass glHelperClass = new GenericHelperClass();
			Properties prop = null;
			try {
				prop = glHelperClass.readConfigfile();

			} catch (Exception e) {

			}
			try {

				Logger.debug("ip addresss old page ------------. :" + ip);

				Session opSession = glHelperClass.getOdooConnection();
				ObjectAdapter resPartner = opSession
						.getObjectAdapter("hr.applicant");
				FilterCollection filterCollection = new FilterCollection();
				filterCollection.add("id", "=", referrelId);
				RowCollection row = resPartner.searchAndReadObject(
						filterCollection, new String[] { "email_from", "name",
								"partner_id", "stage_id", "sign_ip",
								"agreement_date", "role", "id" });
				Row row1 = row.get(0);
				Logger.debug("The referrer detalas for to change stage "
						+ row1.get("name"));
				Date date = Calendar.getInstance().getTime();
				Logger.debug("date  " + date);
				row1.put("sign_ip", ip);
				row1.put("agreement_date", date);
				row1.put("role", roleid);
				row1.put("stage_id", prop.getProperty("visdomreferralStageId"));
				// row1.put("stage_id", 37);
				resPartner.writeObject(row1, true);
				Logger.debug("Referrer  stage changed to involved");
			} catch (Exception e) {
				Logger.error("error in Referrer  stage changed to involved" + e);
			}

			try {

				if (areUsingtouchScreenDevice.equals("yes")) {

					/* Logger.debug(" elecotronic signature  is "+sign ); */

					String newString = elctornicSign.substring(22);
					// System.out.println(newString);
					try {
						byte[] decodedBytes = Base64.decode(newString);
						if (decodedBytes == null) {
							/* Logger.debug("decodedBytes  is null"); */
						}

						image = ImageIO.read(new ByteArrayInputStream(
								decodedBytes));
					} catch (IOException e) {
						Logger.error("failed to read into image");
					}
					pdffilepath = VisdomReferralPdfGenerationMethodWfg(
							json.getString("firstname"),
							areUsingtouchScreenDevice, "", role, image);
					sendwithus.sendTOReferralAttachment(
							json.getString("firstname"),
							"http://forms.visdom.ca/clientrefV?referralId="
									+ referrelId, json.getString("email"),
							pdffilepath);
				} else {
					/* Logger.debug("typed signature  is "+sign ); */

					pdffilepath = VisdomReferralPdfGenerationMethodWfg(
							json.getString("firstname"),
							areUsingtouchScreenDevice, signature, role, image);
					sendwithus.sendTOReferralAttachment(
							json.getString("firstname"),
							"http://forms.visdom.ca/clientrefV?referralId="
									+ referrelId, json.getString("email"),
							pdffilepath);
				}

			} catch (Exception e) {
				Logger.error("error in mailing sending " + e);
			}
			try {

				Path path = Paths.get(pdffilepath);
				byte[] data1 = Files.readAllBytes(path);

				HashMap hashdata = new HashMap();
				String encodeData = net.iharder.Base64.encodeBytes(data1);
				hashdata.put("attachement", encodeData);
				JsonObject jsonObject=JsonObject.from(hashdata);
				jsonObject.put("FormType", "visdomReferral");
				storeData.storeDataToCouchbase("doc_ReferralAgreemetfile_"
						+ referrelId, jsonObject);

			} catch (Exception e) {
				Logger.error("error in storing referral agreement datat to couhbase"
						+ e);
			}
			try {
				File file = new File(pdffilepath);
				file.delete();
			} catch (Exception e) {
				Logger.error("error in deleting pdf file " + e);
			}

			if (json != null) {
				json.put("role", role);
				json.put("ip", ip);
				json.put("AreUsingTouchScreenDEvice", areUsingtouchScreenDevice);
				json.put("RefrralProgress", "100%");

				json.put("mytypedName", signature);
				json.put("ReferralAgreemetfile_id", "doc_ReferralAgreemetfile_"
						+ referrelId);

				json.put("Submission_Date_Time1b", new Date().toString());
				json.put("Type_referral", "Type_referral");

			}

			// send referral fname,referral lastname, email,
			// addrees(city,state,country
			// ,pincode),referredBy,role,referralsoursceid,referalAgreementUrl
			// Todo call onthraport restcall------------------------------

			String firstname = null;
			String lastname = null;
			String email = null;
			String phone = null;
			String companyName = null;
			String companyAddress = null;
			String companyPhoneNumber = null;
			String referrer_Tovisdom = null;

			String agreementURL = "https://doc.visdom.ca/getid?id=doc_ReferralAgreemetfile_"
					+ referrelId;

			String address1 = null;
			String city = null;
			String postalcode = null;

			try {
				firstname = json.getString("firstname");
				lastname = json.getString("lastName");
				email = json.getString("email");
				phone = json.getString("phoneNumber");
				companyName = json.getString("companyname");
				companyAddress = json.getString("companyAddress");

				companyPhoneNumber = json.getString("companyPhoneNumber");
				referrer_Tovisdom = json.getString("referrer_Tovisdom");
				HashMap<String, String> address = new Address()
						.getProperAddressTwo(companyAddress);

				if (!address.isEmpty()) {
					address1 = address.get("address1");
					city = address.get("city");

					postalcode = address.get("postalcode");
				}

			} catch (NullPointerException ex) {
				Logger.error(ex.getMessage());

			}
			new OntraPortOperation().createContact(firstname, lastname, email,
					phone, ontraPortRoleId, companyName, referrer_Tovisdom,
					referrelId, companyPhoneNumber, agreementURL, address1,
					city, postalcode);
			JSONObject jsonTableData = new JSONObject();
			try {
				jsonTableData.put("name", firstname + " " + lastname);
				jsonTableData.put("phonenumber", phone);
				jsonTableData.put("email", email);

				jsonTableData.put("referrdBy", referrer_Tovisdom);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				Logger.error("error in parsing json " + e);
			}
			new openerp.RestCallClass(jsonTableData.toString()).start();

			// here
			try {
				storeData.storeDataToCouchbase(key, json);
			} catch (Exception e) {
				Logger.error("error in storeing referral data in couhbase " + e);
			}

		} catch (Exception e) {
			Logger.error("error in visdomreferral form" + e);
		}

		if (referralExsist == 0) {
			return ok(leadsucess
					.render("Thank you for becoming involved in the Visdom Referral Program. We have sent a copy of the referral agreement to the email provided. In the event you did not see it please check your spam folder in case your provider accidentally miscategorized it"));
		} else {
			return ok(leadsucess
					.render("Thank you for your participation in the Visdom Referral Program.  Your information has been successfully updated. We have sent a copy of the referral agreement to the email provided. In the event you did not see it please check your spam folder in case your provider accidentally miscategorized it"));

		}

	}

	public static String VisdomReferralPdfGenerationMethodWfg(
			String referralName, String areusingTouchScreenDevice,
			String myTypedName, String role, BufferedImage image) {

		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");

		GregorianCalendar calendar = (GregorianCalendar) GregorianCalendar
				.getInstance();

		/* Logger.debug("insdie Referreral  Agreement  Pdf generation"); */
		String file = "/home/venkateshm/Desktop/";
		/*
		 * String outputFileName=file+"Referral_Agreement_"
		 * +referralName+"_"+format.format(calendar.getTime())+".pdf";
		 */
		GenericHelperClass genericHelperClass = new GenericHelperClass();
		String outputFileName = genericHelperClass.readConfigfile()
				.getProperty("pdfPath")
				+ "ReferralAgreement_"
				+ referralName
				+ "_" + format.format(calendar.getTime()) + ".pdf";
		/*
		 * String outputFileName = file + "RerralAgreement_" + referralName +
		 * "_" + format.format(calendar.getTime()) + ".pdf";
		 */
		Properties prop = new Properties();
		try {

			prop.load(VisdomReferralPdfGeneration.class.getClassLoader()
					.getResourceAsStream("visdomReferral.properties"));
			Logger.debug("referralName " + referralName
					+ "areusingTouchScreenDevice " + areusingTouchScreenDevice
					+ " myTypedName" + myTypedName + "role " + role + " image"
					+ image);
			Logger.debug("prop " + prop.size());
			prop.load(VisdomReferral.class.getClassLoader()
					.getResourceAsStream("visdomReferral.properties"));

			PDDocument document = new PDDocument();
			PDPage page1 = new PDPage(PDPage.PAGE_SIZE_A4);

			PDRectangle rect = page1.getMediaBox();
			document.addPage(page1);

			PDFont fontplain = PDType1Font.TIMES_ROMAN;
			PDFont fontbold = PDType1Font.HELVETICA_BOLD;
			// PDFont fontItalic = PDType1Font.HELVETICA_OBLIQUE;
			// PDFont fontMono = PDType1Font.COURIER;

			PDPageContentStream cos = new PDPageContentStream(document, page1);

			int line = 0;

			cos.beginText();
			cos.setFont(fontbold, 12);
			cos.moveTextPositionByAmount(240, rect.getHeight() - 50 * (++line));
			cos.drawString("Mortgage Referral Agreement");
			cos.endText();
			int line2 = 0;
			cos.beginText();
			cos.setFont(fontplain, 10);
			cos.moveTextPositionByAmount(25, rect.getHeight() - 100);
			cos.drawString(prop.getProperty("referral1"));
			cos.endText();
			line2 = line2 + 10;

			cos.beginText();
			cos.setFont(fontplain, 10);
			cos.moveTextPositionByAmount(25, rect.getHeight() - 100 - line2);
			cos.drawString(prop.getProperty("referral2"));
			cos.endText();
			line2 = line2 + 15;

			cos.beginText();
			cos.setFont(fontplain, 10);
			cos.moveTextPositionByAmount(20, rect.getHeight() - 100 - line2);
			cos.drawString(prop.getProperty("referral3"));
			cos.endText();
			line2 = line2 + 15;

			cos.beginText();
			cos.setFont(fontplain, 10);
			cos.moveTextPositionByAmount(20, rect.getHeight() - 100 - line2);
			cos.drawString(prop.getProperty("referral4"));
			cos.endText();
			line2 = line2 + 10;

			cos.beginText();
			cos.setFont(fontplain, 10);
			cos.moveTextPositionByAmount(20, rect.getHeight() - 100 - line2);
			cos.drawString(prop.getProperty("referral5"));
			cos.endText();
			line2 = line2 + 10;

			cos.beginText();
			cos.setFont(fontplain, 10);
			cos.moveTextPositionByAmount(20, rect.getHeight() - 100 - line2);
			cos.drawString(prop.getProperty("referral6"));
			cos.endText();
			line2 = line2 + 20;

			cos.beginText();
			cos.setFont(fontplain, 10);
			cos.moveTextPositionByAmount(30, rect.getHeight() - 100 - line2);
			cos.drawString(prop.getProperty("referral7"));
			cos.endText();
			line2 = line2 + 15;

			cos.beginText();
			cos.setFont(fontplain, 10);
			cos.moveTextPositionByAmount(20, rect.getHeight() - 100 - line2);
			cos.drawString(prop.getProperty("referral8"));
			cos.endText();
			line2 = line2 + 15;

			cos.beginText();
			cos.setFont(fontplain, 10);
			cos.moveTextPositionByAmount(20, rect.getHeight() - 100 - line2);
			cos.drawString(prop.getProperty("referral8"));
			cos.endText();
			line2 = line2 + 15;

			cos.beginText();
			cos.setFont(fontplain, 10);
			cos.moveTextPositionByAmount(20, rect.getHeight() - 100 - line2);
			cos.drawString(prop.getProperty("referral9"));
			cos.endText();
			line2 = line2 + 10;

			cos.beginText();
			cos.setFont(fontplain, 10);
			cos.moveTextPositionByAmount(20, rect.getHeight() - 100 - line2);
			cos.drawString(prop.getProperty("referral10"));
			cos.endText();
			line2 = line2 + 15;

			cos.beginText();
			cos.setFont(fontplain, 10);
			cos.moveTextPositionByAmount(20, rect.getHeight() - 100 - line2);
			cos.drawString(prop.getProperty("referral11"));
			cos.endText();
			line2 = line2 + 15;

			cos.beginText();
			cos.setFont(fontplain, 10);
			cos.moveTextPositionByAmount(20, rect.getHeight() - 100 - line2);
			cos.drawString(prop.getProperty("referral12"));
			cos.endText();
			line2 = line2 + 10;

			cos.beginText();
			cos.setFont(fontplain, 10);
			cos.moveTextPositionByAmount(20, rect.getHeight() - 100 - line2);
			cos.drawString(prop.getProperty("referral13"));
			cos.endText();
			line2 = line2 + 10;

			cos.beginText();
			cos.setFont(fontplain, 10);
			cos.moveTextPositionByAmount(20, rect.getHeight() - 100 - line2);
			cos.drawString(prop.getProperty("referral14"));
			cos.endText();
			line2 = line2 + 15;

			cos.beginText();
			cos.setFont(fontplain, 10);
			cos.moveTextPositionByAmount(20, rect.getHeight() - 100 - line2);
			cos.drawString(prop.getProperty("referral15"));
			cos.endText();
			line2 = line2 + 15;

			cos.beginText();
			cos.setFont(fontplain, 10);
			cos.moveTextPositionByAmount(20, rect.getHeight() - 100 - line2);
			cos.drawString(prop.getProperty("referral16"));
			cos.endText();
			line2 = line2 + 10;

			cos.beginText();
			cos.setFont(fontplain, 10);
			cos.moveTextPositionByAmount(20, rect.getHeight() - 100 - line2);
			cos.drawString(prop.getProperty("referral17"));
			cos.endText();
			line2 = line2 + 10;

			cos.beginText();
			cos.setFont(fontplain, 10);
			cos.moveTextPositionByAmount(20, rect.getHeight() - 100 - line2);
			cos.drawString(prop.getProperty("referral18"));
			cos.endText();
			line2 = line2 + 10;

			cos.beginText();
			cos.setFont(fontplain, 10);
			cos.moveTextPositionByAmount(20, rect.getHeight() - 100 - line2);
			cos.drawString(prop.getProperty("referral19"));
			cos.endText();
			line2 = line2 + 15;

			cos.beginText();
			cos.setFont(fontplain, 10);
			cos.moveTextPositionByAmount(20, rect.getHeight() - 100 - line2);
			cos.drawString(prop.getProperty("referral20"));
			cos.endText();
			line2 = line2 + 10;

			cos.beginText();
			cos.setFont(fontplain, 10);
			cos.moveTextPositionByAmount(20, rect.getHeight() - 100 - line2);
			cos.drawString(prop.getProperty("referral21"));
			cos.endText();
			line2 = line2 + 15;

			cos.beginText();
			cos.setFont(fontplain, 10);
			cos.moveTextPositionByAmount(20, rect.getHeight() - 100 - line2);
			cos.drawString(prop.getProperty("referral22"));
			cos.endText();
			line2 = line2 + 10;

			cos.beginText();
			cos.setFont(fontplain, 10);
			cos.moveTextPositionByAmount(20, rect.getHeight() - 100 - line2);
			cos.drawString(prop.getProperty("referral23"));
			cos.endText();
			line2 = line2 + 10;

			cos.beginText();
			cos.setFont(fontplain, 10);
			cos.moveTextPositionByAmount(20, rect.getHeight() - 100 - line2);
			cos.drawString(prop.getProperty("referral24"));
			cos.endText();
			line2 = line2 + 10;

			cos.beginText();
			cos.setFont(fontplain, 10);
			cos.moveTextPositionByAmount(20, rect.getHeight() - 100 - line2);
			cos.drawString(prop.getProperty("referral25"));
			cos.endText();
			line2 = line2 + 15;

			cos.beginText();
			cos.setFont(fontplain, 10);
			cos.moveTextPositionByAmount(20, rect.getHeight() - 100 - line2);
			cos.drawString(prop.getProperty("referral26"));
			cos.endText();
			line2 = line2 + 10;

			cos.beginText();
			cos.setFont(fontplain, 10);
			cos.moveTextPositionByAmount(20, rect.getHeight() - 100 - line2);
			cos.drawString(prop.getProperty("referral27"));
			cos.endText();
			line2 = line2 + 15;

			cos.beginText();
			cos.setFont(fontplain, 10);
			cos.moveTextPositionByAmount(20, rect.getHeight() - 100 - line2);
			cos.drawString(prop.getProperty("referral28"));
			cos.endText();
			line2 = line2 + 15;

			cos.beginText();
			cos.setFont(fontplain, 10);
			cos.moveTextPositionByAmount(20, rect.getHeight() - 100 - line2);
			cos.drawString(prop.getProperty("referral29"));
			cos.endText();
			line2 = line2 + 15;

			cos.beginText();
			cos.setFont(fontplain, 10);
			cos.moveTextPositionByAmount(20, rect.getHeight() - 100 - line2);
			cos.drawString(prop.getProperty("referral30"));
			cos.endText();
			line2 = line2 + 10;

			cos.beginText();
			cos.setFont(fontplain, 10);
			cos.moveTextPositionByAmount(20, rect.getHeight() - 100 - line2);
			cos.drawString(prop.getProperty("referral31"));
			cos.endText();
			line2 = line2 + 15;

			cos.beginText();
			cos.setFont(fontplain, 10);
			cos.moveTextPositionByAmount(20, rect.getHeight() - 100 - line2);
			cos.drawString(prop.getProperty("referral32"));
			cos.endText();
			line2 = line2 + 10;

			cos.beginText();
			cos.setFont(fontplain, 10);
			cos.moveTextPositionByAmount(20, rect.getHeight() - 100 - line2);
			cos.drawString(prop.getProperty("referral33"));
			cos.endText();
			line2 = line2 + 10;

			cos.beginText();
			cos.setFont(fontplain, 10);
			cos.moveTextPositionByAmount(20, rect.getHeight() - 100 - line2);
			cos.drawString(prop.getProperty("referral34"));
			cos.endText();
			line2 = line2 + 10;

			cos.beginText();
			cos.setFont(fontplain, 10);
			cos.moveTextPositionByAmount(20, rect.getHeight() - 100 - line2);
			cos.drawString(prop.getProperty("referral35"));
			cos.endText();
			line2 = line2 + 10;

			cos.beginText();
			cos.setFont(fontplain, 10);
			cos.moveTextPositionByAmount(20, rect.getHeight() - 100 - line2);
			cos.drawString(prop.getProperty("referral36"));
			cos.endText();
			line2 = line2 + 10;

			cos.beginText();
			cos.setFont(fontplain, 10);
			cos.moveTextPositionByAmount(20, rect.getHeight() - 100 - line2);
			cos.drawString(prop.getProperty("referral37"));
			cos.endText();
			line2 = line2 + 20;

			cos.beginText();
			cos.setFont(fontplain, 10);
			cos.moveTextPositionByAmount(30, rect.getHeight() - 100 - line2);
			cos.drawString(prop.getProperty("referral38"));
			cos.endText();
			line2 = line2 + 15;

			cos.beginText();
			cos.setFont(fontplain, 10);
			cos.moveTextPositionByAmount(20, rect.getHeight() - 100 - line2);
			cos.drawString(prop.getProperty("referral39"));
			cos.endText();
			line2 = line2 + 10;

			cos.beginText();
			cos.setFont(fontplain, 10);
			cos.moveTextPositionByAmount(20, rect.getHeight() - 100 - line2);
			cos.drawString(prop.getProperty("referral40"));
			cos.endText();
			line2 = line2 + 15;

			cos.beginText();
			cos.setFont(fontplain, 10);
			cos.moveTextPositionByAmount(20, rect.getHeight() - 100 - line2);
			cos.drawString(prop.getProperty("referral41"));
			cos.endText();
			line2 = line2 + 10;

			cos.beginText();
			cos.setFont(fontplain, 10);
			cos.moveTextPositionByAmount(20, rect.getHeight() - 100 - line2);
			cos.drawString(prop.getProperty("referral42"));
			cos.endText();
			line2 = line2 + 20;

			cos.beginText();
			cos.setFont(fontplain, 10);
			cos.moveTextPositionByAmount(30, rect.getHeight() - 100 - line2);
			cos.drawString(prop.getProperty("referral43"));
			cos.endText();
			line2 = line2 + 15;

			cos.beginText();
			cos.setFont(fontplain, 10);
			cos.moveTextPositionByAmount(20, rect.getHeight() - 100 - line2);
			cos.drawString(prop.getProperty("referral44"));
			cos.endText();
			line2 = line2 + 10;

			cos.beginText();
			cos.setFont(fontplain, 10);
			cos.moveTextPositionByAmount(20, rect.getHeight() - 100 - line2);
			cos.drawString(prop.getProperty("referral45"));
			cos.endText();
			line2 = line2 + 15;

			cos.beginText();
			cos.setFont(fontplain, 10);
			cos.moveTextPositionByAmount(20, rect.getHeight() - 100 - line2);
			cos.drawString(prop.getProperty("referral46"));
			cos.endText();
			line2 = line2 + 15;

			cos.beginText();
			cos.setFont(fontplain, 10);
			cos.moveTextPositionByAmount(20, rect.getHeight() - 100 - line2);
			cos.drawString(prop.getProperty("referral47"));
			cos.endText();
			line2 = line2 + 10;

			cos.beginText();
			cos.setFont(fontplain, 10);
			cos.moveTextPositionByAmount(20, rect.getHeight() - 100 - line2);
			cos.drawString(prop.getProperty("referral48"));
			cos.endText();
			line2 = line2 + 10;

			cos.beginText();
			cos.setFont(fontplain, 10);
			cos.moveTextPositionByAmount(20, rect.getHeight() - 100 - line2);
			cos.drawString(prop.getProperty("referral49"));
			cos.endText();
			line2 = line2 + 15;

			cos.beginText();
			cos.setFont(fontplain, 10);
			cos.moveTextPositionByAmount(20, rect.getHeight() - 100 - line2);
			cos.drawString(prop.getProperty("referral50"));
			cos.endText();
			line2 = line2 + 15;

			cos.beginText();
			cos.setFont(fontplain, 10);
			cos.moveTextPositionByAmount(20, rect.getHeight() - 100 - line2);
			cos.drawString(prop.getProperty("referral51"));
			cos.endText();
			line2 = line2 + 10;

			cos.beginText();
			cos.setFont(fontplain, 10);
			cos.moveTextPositionByAmount(20, rect.getHeight() - 100 - line2);
			cos.drawString(prop.getProperty("referral52"));
			cos.endText();
			line2 = line2 + 15;

			cos.beginText();
			cos.setFont(fontplain, 10);
			cos.moveTextPositionByAmount(20, rect.getHeight() - 100 - line2);
			cos.drawString(prop.getProperty("referral53"));
			cos.endText();
			line2 = line2 + 10;

			cos.beginText();
			cos.setFont(fontplain, 10);
			cos.moveTextPositionByAmount(20, rect.getHeight() - 100 - line2);
			cos.drawString(prop.getProperty("referral54"));
			cos.endText();
			line2 = line2 + 10;

			cos.beginText();
			cos.setFont(fontplain, 10);
			cos.moveTextPositionByAmount(20, rect.getHeight() - 100 - line2);
			cos.drawString(prop.getProperty("referral55"));
			cos.endText();
			line2 = line2 + 10;

			cos.beginText();
			cos.setFont(fontplain, 10);
			cos.moveTextPositionByAmount(20, rect.getHeight() - 100 - line2);
			cos.drawString(prop.getProperty("referral56"));
			cos.endText();
			line2 = line2 + 15;
			cos.close();

			PDPage page2 = new PDPage(PDPage.PAGE_SIZE_A4);
			document.addPage(page2);
			cos = new PDPageContentStream(document, page2);
			line2 = 0;
			cos.beginText();
			cos.setFont(fontplain, 10);
			cos.moveTextPositionByAmount(20, rect.getHeight() - 70 - line2);
			cos.drawString(prop.getProperty("referral57"));
			cos.endText();
			line2 = line2 + 10;

			cos.beginText();
			cos.setFont(fontplain, 10);
			cos.moveTextPositionByAmount(20, rect.getHeight() - 70 - line2);
			cos.drawString(prop.getProperty("referral58"));
			cos.endText();
			line2 = line2 + 10;

			cos.beginText();
			cos.setFont(fontplain, 10);
			cos.moveTextPositionByAmount(20, rect.getHeight() - 70 - line2);
			cos.drawString(prop.getProperty("referral59"));
			cos.endText();
			line2 = line2 + 10;

			/*
			 * cos.beginText(); cos.setFont(fontplain, 10);
			 * cos.moveTextPositionByAmount(20,rect.getHeight() -100-line2);
			 * cos.drawString(prop.getProperty("referral60")); cos.endText();
			 * line2=line2+10;
			 */

			cos.beginText();
			cos.setFont(fontplain, 10);
			cos.moveTextPositionByAmount(20, rect.getHeight() - 70 - line2);
			cos.drawString(prop.getProperty("referral61"));
			cos.endText();
			line2 = line2 + 10;

			cos.beginText();
			cos.setFont(fontplain, 10);
			cos.moveTextPositionByAmount(20, rect.getHeight() - 70 - line2);
			cos.drawString(prop.getProperty("referral62"));
			cos.endText();
			line2 = line2 + 10;

			cos.beginText();
			cos.setFont(fontplain, 10);
			cos.moveTextPositionByAmount(20, rect.getHeight() - 70 - line2);
			cos.drawString(prop.getProperty("referral63"));
			cos.endText();
			line2 = line2 + 10;

			cos.beginText();
			cos.setFont(fontplain, 10);
			cos.moveTextPositionByAmount(20, rect.getHeight() - 70 - line2);
			cos.drawString(prop.getProperty("referral64"));
			cos.endText();
			line2 = line2 + 10;

			cos.beginText();
			cos.setFont(fontplain, 10);
			cos.moveTextPositionByAmount(20, rect.getHeight() - 70 - line2);
			cos.drawString(prop.getProperty("referral65"));
			cos.endText();
			line2 = line2 + 15;

			cos.beginText();
			cos.setFont(fontplain, 10);
			cos.moveTextPositionByAmount(20, rect.getHeight() - 70 - line2);
			cos.drawString(prop.getProperty("referral66"));
			cos.endText();
			line2 = line2 + 10;

			cos.beginText();
			cos.setFont(fontplain, 10);
			cos.moveTextPositionByAmount(20, rect.getHeight() - 70 - line2);
			cos.drawString(prop.getProperty("referral67"));
			cos.endText();
			line2 = line2 + 15;

			cos.beginText();
			cos.setFont(fontplain, 10);
			cos.moveTextPositionByAmount(30, rect.getHeight() - 70 - line2);
			cos.drawString(prop.getProperty("referral68"));
			cos.endText();
			line2 = line2 + 15;

			cos.beginText();
			cos.setFont(fontplain, 10);
			cos.moveTextPositionByAmount(20, rect.getHeight() - 70 - line2);
			cos.drawString(prop.getProperty("referral69"));
			cos.endText();
			line2 = line2 + 10;

			cos.beginText();
			cos.setFont(fontplain, 10);
			cos.moveTextPositionByAmount(20, rect.getHeight() - 70 - line2);
			cos.drawString(prop.getProperty("referral70"));
			cos.endText();
			line2 = line2 + 15;

			cos.beginText();
			cos.setFont(fontplain, 10);
			cos.moveTextPositionByAmount(20, rect.getHeight() - 70 - line2);
			cos.drawString(prop.getProperty("referral71"));
			cos.endText();
			line2 = line2 + 10;

			cos.beginText();
			cos.setFont(fontplain, 10);
			cos.moveTextPositionByAmount(20, rect.getHeight() - 70 - line2);
			cos.drawString(prop.getProperty("referral72"));
			cos.endText();
			line2 = line2 + 15;

			cos.beginText();
			cos.setFont(fontplain, 10);
			cos.moveTextPositionByAmount(20, rect.getHeight() - 70 - line2);
			cos.drawString(prop.getProperty("referral73"));
			cos.endText();
			line2 = line2 + 10;

			cos.beginText();
			cos.setFont(fontplain, 10);
			cos.moveTextPositionByAmount(20, rect.getHeight() - 70 - line2);
			cos.drawString(prop.getProperty("referral74"));
			cos.endText();
			line2 = line2 + 10;

			cos.beginText();
			cos.setFont(fontplain, 10);
			cos.moveTextPositionByAmount(20, rect.getHeight() - 70 - line2);
			cos.drawString(prop.getProperty("referral75"));
			cos.endText();
			line2 = line2 + 15;

			cos.beginText();
			cos.setFont(fontplain, 10);
			cos.moveTextPositionByAmount(20, rect.getHeight() - 70 - line2);
			cos.drawString(prop.getProperty("referral76"));
			cos.endText();
			line2 = line2 + 10;

			cos.beginText();
			cos.setFont(fontplain, 10);
			cos.moveTextPositionByAmount(20, rect.getHeight() - 70 - line2);
			cos.drawString(prop.getProperty("referral77"));
			cos.endText();
			line2 = line2 + 10;

			cos.beginText();
			cos.setFont(fontplain, 10);
			cos.moveTextPositionByAmount(20, rect.getHeight() - 70 - line2);
			cos.drawString(prop.getProperty("referral78"));
			cos.endText();
			line2 = line2 + 20;

			cos.beginText();
			cos.setFont(fontplain, 10);
			cos.moveTextPositionByAmount(30, rect.getHeight() - 70 - line2);
			cos.drawString(prop.getProperty("referral79"));
			cos.endText();
			line2 = line2 + 15;

			cos.beginText();
			cos.setFont(fontplain, 10);
			cos.moveTextPositionByAmount(20, rect.getHeight() - 70 - line2);
			cos.drawString(prop.getProperty("referral80"));
			cos.endText();
			line2 = line2 + 10;

			cos.beginText();
			cos.setFont(fontplain, 10);
			cos.moveTextPositionByAmount(20, rect.getHeight() - 70 - line2);
			cos.drawString(prop.getProperty("referral81"));
			cos.endText();
			line2 = line2 + 10;

			cos.beginText();
			cos.setFont(fontplain, 10);
			cos.moveTextPositionByAmount(20, rect.getHeight() - 70 - line2);
			cos.drawString(prop.getProperty("referral82"));
			cos.endText();
			line2 = line2 + 10;

			cos.beginText();
			cos.setFont(fontplain, 10);
			cos.moveTextPositionByAmount(20, rect.getHeight() - 70 - line2);
			cos.drawString(prop.getProperty("referral83"));
			cos.endText();
			line2 = line2 + 15;

			cos.beginText();
			cos.setFont(fontplain, 10);
			cos.moveTextPositionByAmount(30, rect.getHeight() - 70 - line2);
			cos.drawString(prop.getProperty("referral84"));
			cos.endText();
			line2 = line2 + 15;

			cos.beginText();
			cos.setFont(fontplain, 10);
			cos.moveTextPositionByAmount(20, rect.getHeight() - 70 - line2);
			cos.drawString(prop.getProperty("referral85"));
			cos.endText();
			line2 = line2 + 10;

			cos.beginText();
			cos.setFont(fontplain, 10);
			cos.moveTextPositionByAmount(20, rect.getHeight() - 70 - line2);
			cos.drawString(prop.getProperty("referral86"));
			cos.endText();
			line2 = line2 + 15;

			cos.beginText();
			cos.setFont(fontplain, 10);
			cos.moveTextPositionByAmount(20, rect.getHeight() - 70 - line2);
			cos.drawString(prop.getProperty("referral87"));
			cos.endText();
			line2 = line2 + 10;

			cos.beginText();
			cos.setFont(fontplain, 10);
			cos.moveTextPositionByAmount(20, rect.getHeight() - 70 - line2);
			cos.drawString(prop.getProperty("referral88"));
			cos.endText();
			line2 = line2 + 15;

			cos.beginText();
			cos.setFont(fontplain, 10);
			cos.moveTextPositionByAmount(20, rect.getHeight() - 70 - line2);
			cos.drawString(prop.getProperty("referral89"));
			cos.endText();
			line2 = line2 + 10;

			cos.beginText();
			cos.setFont(fontplain, 10);
			cos.moveTextPositionByAmount(20, rect.getHeight() - 70 - line2);
			cos.drawString(prop.getProperty("referral90"));
			cos.endText();
			line2 = line2 + 10;

			cos.beginText();
			cos.setFont(fontplain, 10);
			cos.moveTextPositionByAmount(20, rect.getHeight() - 70 - line2);
			cos.drawString(prop.getProperty("referral91"));
			cos.endText();
			line2 = line2 + 10;

			cos.beginText();
			cos.setFont(fontplain, 10);
			cos.moveTextPositionByAmount(20, rect.getHeight() - 70 - line2);
			cos.drawString(prop.getProperty("referral92"));
			cos.endText();
			line2 = line2 + 10;

			cos.beginText();
			cos.setFont(fontplain, 10);
			cos.moveTextPositionByAmount(20, rect.getHeight() - 70 - line2);
			cos.drawString(prop.getProperty("referral93"));
			cos.endText();
			line2 = line2 + 15;

			cos.beginText();
			cos.setFont(fontplain, 10);
			cos.moveTextPositionByAmount(20, rect.getHeight() - 70 - line2);
			cos.drawString(prop.getProperty("referral94"));
			cos.endText();
			line2 = line2 + 10;

			cos.beginText();
			cos.setFont(fontplain, 10);
			cos.moveTextPositionByAmount(20, rect.getHeight() - 70 - line2);
			cos.drawString(prop.getProperty("referral95"));
			cos.endText();
			line2 = line2 + 10;

			cos.beginText();
			cos.setFont(fontplain, 10);
			cos.moveTextPositionByAmount(20, rect.getHeight() - 70 - line2);
			cos.drawString(prop.getProperty("referral96"));
			cos.endText();
			line2 = line2 + 15;

			cos.beginText();
			cos.setFont(fontplain, 10);
			cos.moveTextPositionByAmount(20, rect.getHeight() - 70 - line2);
			cos.drawString(prop.getProperty("referral97"));
			cos.endText();
			line2 = line2 + 10;

			cos.beginText();
			cos.setFont(fontplain, 10);
			cos.moveTextPositionByAmount(20, rect.getHeight() - 70 - line2);
			cos.drawString(prop.getProperty("referral98"));
			cos.endText();
			line2 = line2 + 15;

			cos.beginText();
			cos.setFont(fontplain, 10);
			cos.moveTextPositionByAmount(20, rect.getHeight() - 70 - line2);
			cos.drawString(prop.getProperty("referral99"));
			cos.endText();
			line2 = line2 + 10;

			cos.beginText();
			cos.setFont(fontplain, 10);
			cos.moveTextPositionByAmount(20, rect.getHeight() - 70 - line2);
			cos.drawString(prop.getProperty("referral70"));
			cos.endText();
			line2 = line2 + 20;

			cos.beginText();
			cos.setFont(fontplain, 10);
			cos.moveTextPositionByAmount(20, rect.getHeight() - 70 - line2);
			cos.drawString(prop.getProperty("referral101"));
			cos.endText();
			line2 = line2 + 15;

			cos.beginText();
			cos.setFont(fontplain, 10);
			cos.moveTextPositionByAmount(20, rect.getHeight() - 70 - line2);
			cos.drawString(prop.getProperty("referral102"));
			cos.endText();
			line2 = line2 + 10;

			cos.beginText();
			cos.setFont(fontplain, 10);
			cos.moveTextPositionByAmount(20, rect.getHeight() - 70 - line2);
			cos.drawString(prop.getProperty("referral103"));
			cos.endText();
			line2 = line2 + 10;

			cos.beginText();
			cos.setFont(fontplain, 10);
			cos.moveTextPositionByAmount(20, rect.getHeight() - 70 - line2);
			cos.drawString(prop.getProperty("referral104"));
			cos.endText();
			line2 = line2 + 15;

			cos.beginText();
			cos.setFont(fontplain, 10);
			cos.moveTextPositionByAmount(20, rect.getHeight() - 70 - line2);
			cos.drawString(prop.getProperty("referral105"));
			cos.endText();
			line2 = line2 + 10;

			cos.beginText();
			cos.setFont(fontplain, 10);
			cos.moveTextPositionByAmount(20, rect.getHeight() - 70 - line2);
			cos.drawString(prop.getProperty("referral106"));
			cos.endText();
			line2 = line2 + 15;

			cos.beginText();
			cos.setFont(fontplain, 10);
			cos.moveTextPositionByAmount(20, rect.getHeight() - 70 - line2);
			cos.drawString(prop.getProperty("referral107"));
			cos.endText();
			line2 = line2 + 10;

			cos.beginText();
			cos.setFont(fontplain, 10);
			cos.moveTextPositionByAmount(20, rect.getHeight() - 70 - line2);
			cos.drawString(prop.getProperty("referral108"));
			cos.endText();
			line2 = line2 + 10;

			cos.beginText();
			cos.setFont(fontplain, 10);
			cos.moveTextPositionByAmount(20, rect.getHeight() - 70 - line2);
			cos.drawString(prop.getProperty("referral109"));
			cos.endText();
			line2 = line2 + 10;

			cos.beginText();
			cos.setFont(fontplain, 10);
			cos.moveTextPositionByAmount(20, rect.getHeight() - 70 - line2);
			cos.drawString(prop.getProperty("referral110"));
			cos.endText();
			line2 = line2 + 15;

			cos.beginText();
			cos.setFont(fontplain, 10);
			cos.moveTextPositionByAmount(20, rect.getHeight() - 70 - line2);
			cos.drawString(prop.getProperty("referral111"));
			cos.endText();
			line2 = line2 + 15;

			cos.beginText();
			cos.setFont(fontplain, 10);
			cos.moveTextPositionByAmount(20, rect.getHeight() - 70 - line2);
			cos.drawString(prop.getProperty("referral112"));
			cos.endText();
			line2 = line2 + 10;

			cos.beginText();
			cos.setFont(fontplain, 10);
			cos.moveTextPositionByAmount(20, rect.getHeight() - 70 - line2);
			cos.drawString(prop.getProperty("referral113"));
			cos.endText();
			line2 = line2 + 20;

			cos.beginText();
			cos.setFont(fontplain, 10);
			cos.moveTextPositionByAmount(30, rect.getHeight() - 70 - line2);
			cos.drawString(prop.getProperty("referral114"));
			cos.endText();
			line2 = line2 + 15;

			cos.beginText();
			cos.setFont(fontplain, 10);
			cos.moveTextPositionByAmount(20, rect.getHeight() - 70 - line2);
			cos.drawString(prop.getProperty("referral115"));
			cos.endText();
			line2 = line2 + 10;

			cos.beginText();
			cos.setFont(fontplain, 10);
			cos.moveTextPositionByAmount(20, rect.getHeight() - 70 - line2);
			cos.drawString(prop.getProperty("referral116"));
			cos.endText();
			line2 = line2 + 15;

			cos.beginText();
			cos.setFont(fontplain, 10);
			cos.moveTextPositionByAmount(20, rect.getHeight() - 70 - line2);
			cos.drawString(prop.getProperty("referral117"));
			cos.endText();
			line2 = line2 + 10;

			cos.beginText();
			cos.setFont(fontplain, 10);
			cos.moveTextPositionByAmount(20, rect.getHeight() - 70 - line2);
			cos.drawString(prop.getProperty("referral118"));
			cos.endText();
			line2 = line2 + 15;

			cos.close();

			PDPage page3 = new PDPage(PDPage.PAGE_SIZE_A4);
			document.addPage(page3);
			cos = new PDPageContentStream(document, page3);

			line2 = 0;
			cos.beginText();
			cos.setFont(fontplain, 10);
			cos.moveTextPositionByAmount(20, rect.getHeight() - 70 - line2);
			cos.drawString(prop.getProperty("referral119"));
			cos.endText();
			line2 = line2 + 10;

			cos.beginText();
			cos.setFont(fontplain, 10);
			cos.moveTextPositionByAmount(20, rect.getHeight() - 70 - line2);
			cos.drawString(prop.getProperty("referral120"));
			cos.endText();
			line2 = line2 + 10;

			cos.beginText();
			cos.setFont(fontplain, 10);
			cos.moveTextPositionByAmount(20, rect.getHeight() - 70 - line2);
			cos.drawString(prop.getProperty("referral121"));
			cos.endText();
			line2 = line2 + 15;

			cos.beginText();
			cos.setFont(fontplain, 10);
			cos.moveTextPositionByAmount(20, rect.getHeight() - 70 - line2);
			cos.drawString(prop.getProperty("referral122"));
			cos.endText();
			line2 = line2 + 10;

			cos.beginText();
			cos.setFont(fontplain, 10);
			cos.moveTextPositionByAmount(20, rect.getHeight() - 70 - line2);
			cos.drawString(prop.getProperty("referral123"));
			cos.endText();
			line2 = line2 + 15;

			cos.beginText();
			cos.setFont(fontplain, 10);
			cos.moveTextPositionByAmount(20, rect.getHeight() - 70 - line2);
			cos.drawString(prop.getProperty("referral124"));
			cos.endText();
			line2 = line2 + 10;

			cos.beginText();
			cos.setFont(fontplain, 10);
			cos.moveTextPositionByAmount(20, rect.getHeight() - 70 - line2);
			cos.drawString(prop.getProperty("referral125"));
			cos.endText();
			line2 = line2 + 10;

			cos.beginText();
			cos.setFont(fontplain, 10);
			cos.moveTextPositionByAmount(20, rect.getHeight() - 70 - line2);
			cos.drawString(prop.getProperty("referral126"));
			cos.endText();
			line2 = line2 + 15;

			cos.beginText();
			cos.setFont(fontplain, 10);
			cos.moveTextPositionByAmount(20, rect.getHeight() - 70 - line2);
			cos.drawString(prop.getProperty("referral127"));
			cos.endText();
			line2 = line2 + 10;

			cos.beginText();
			cos.setFont(fontplain, 10);
			cos.moveTextPositionByAmount(20, rect.getHeight() - 70 - line2);
			cos.drawString(prop.getProperty("referral128"));
			cos.endText();
			line2 = line2 + 15;

			cos.beginText();
			cos.setFont(fontplain, 10);
			cos.moveTextPositionByAmount(20, rect.getHeight() - 70 - line2);
			cos.drawString(prop.getProperty("referral129"));
			cos.endText();
			line2 = line2 + 15;

			cos.beginText();
			cos.setFont(fontplain, 10);
			cos.moveTextPositionByAmount(20, rect.getHeight() - 70 - line2);
			cos.drawString(prop.getProperty("referral130"));
			cos.endText();
			line2 = line2 + 10;

			cos.beginText();
			cos.setFont(fontplain, 10);
			cos.moveTextPositionByAmount(20, rect.getHeight() - 70 - line2);
			cos.drawString(prop.getProperty("referral131"));
			cos.endText();
			line2 = line2 + 10;

			cos.beginText();
			cos.setFont(fontplain, 10);
			cos.moveTextPositionByAmount(20, rect.getHeight() - 70 - line2);
			cos.drawString(prop.getProperty("referral132"));
			cos.endText();
			line2 = line2 + 10;

			cos.beginText();
			cos.setFont(fontplain, 10);
			cos.moveTextPositionByAmount(20, rect.getHeight() - 70 - line2);
			cos.drawString(prop.getProperty("referral133"));
			cos.endText();
			line2 = line2 + 15;

			cos.beginText();
			cos.setFont(fontplain, 10);
			cos.moveTextPositionByAmount(20, rect.getHeight() - 70 - line2);
			cos.drawString(prop.getProperty("referral134"));
			cos.endText();
			line2 = line2 + 10;

			cos.beginText();
			cos.setFont(fontplain, 10);
			cos.moveTextPositionByAmount(20, rect.getHeight() - 70 - line2);
			cos.drawString(prop.getProperty("referral135"));
			cos.endText();
			line2 = line2 + 15;

			cos.beginText();
			cos.setFont(fontplain, 10);
			cos.moveTextPositionByAmount(20, rect.getHeight() - 70 - line2);
			cos.drawString(prop.getProperty("referral136"));
			cos.endText();
			line2 = line2 + 10;

			cos.beginText();
			cos.setFont(fontplain, 10);
			cos.moveTextPositionByAmount(20, rect.getHeight() - 70 - line2);
			cos.drawString(prop.getProperty("referral137"));
			cos.endText();
			line2 = line2 + 15;

			cos.beginText();
			cos.setFont(fontplain, 10);
			cos.moveTextPositionByAmount(20, rect.getHeight() - 70 - line2);
			cos.drawString(prop.getProperty("referral138"));
			cos.endText();
			line2 = line2 + 10;

			cos.beginText();
			cos.setFont(fontplain, 10);
			cos.moveTextPositionByAmount(20, rect.getHeight() - 70 - line2);
			cos.drawString(prop.getProperty("referral139"));
			cos.endText();
			line2 = line2 + 10;

			cos.beginText();
			cos.setFont(fontplain, 10);
			cos.moveTextPositionByAmount(20, rect.getHeight() - 70 - line2);
			cos.drawString(prop.getProperty("referral140"));
			cos.endText();
			line2 = line2 + 10;

			cos.beginText();
			cos.setFont(fontplain, 10);
			cos.moveTextPositionByAmount(20, rect.getHeight() - 70 - line2);
			cos.drawString(prop.getProperty("referral142"));
			cos.endText();
			line2 = line2 + 10;

			cos.beginText();
			cos.setFont(fontplain, 10);
			cos.moveTextPositionByAmount(20, rect.getHeight() - 70 - line2);
			cos.drawString(prop.getProperty("referral143"));
			cos.endText();
			line2 = line2 + 10;

			cos.beginText();
			cos.setFont(fontplain, 10);
			cos.moveTextPositionByAmount(20, rect.getHeight() - 70 - line2);
			cos.drawString(prop.getProperty("referral144"));
			cos.endText();
			line2 = line2 + 10;

			cos.beginText();
			cos.setFont(fontplain, 10);
			cos.moveTextPositionByAmount(20, rect.getHeight() - 70 - line2);
			cos.drawString(prop.getProperty("referral145"));
			cos.endText();
			line2 = line2 + 10;

			cos.beginText();
			cos.setFont(fontplain, 10);
			cos.moveTextPositionByAmount(20, rect.getHeight() - 70 - line2);
			cos.drawString(prop.getProperty("referral146"));
			cos.endText();
			line2 = line2 + 10;

			cos.beginText();
			cos.setFont(fontplain, 10);
			cos.moveTextPositionByAmount(20, rect.getHeight() - 70 - line2);
			cos.drawString(prop.getProperty("referral147"));
			cos.endText();
			line2 = line2 + 15;

			cos.beginText();
			cos.setFont(fontplain, 10);
			cos.moveTextPositionByAmount(20, rect.getHeight() - 70 - line2);
			cos.drawString(prop.getProperty("referral148"));
			cos.endText();
			line2 = line2 + 10;

			cos.beginText();
			cos.setFont(fontplain, 10);
			cos.moveTextPositionByAmount(20, rect.getHeight() - 70 - line2);
			cos.drawString(prop.getProperty("referral149"));
			cos.endText();
			line2 = line2 + 10;

			cos.beginText();
			cos.setFont(fontplain, 10);
			cos.moveTextPositionByAmount(20, rect.getHeight() - 70 - line2);
			cos.drawString(prop.getProperty("referral150"));
			cos.endText();
			line2 = line2 + 10;

			cos.beginText();
			cos.setFont(fontplain, 10);
			cos.moveTextPositionByAmount(20, rect.getHeight() - 70 - line2);
			cos.drawString(prop.getProperty("referral151"));
			cos.endText();
			line2 = line2 + 10;

			cos.beginText();
			cos.setFont(fontplain, 10);
			cos.moveTextPositionByAmount(20, rect.getHeight() - 70 - line2);
			cos.drawString(prop.getProperty("referral152"));
			cos.endText();
			line2 = line2 + 10;

			cos.beginText();
			cos.setFont(fontplain, 10);
			cos.moveTextPositionByAmount(20, rect.getHeight() - 70 - line2);
			cos.drawString(prop.getProperty("referral153"));
			cos.endText();
			line2 = line2 + 10;

			cos.beginText();
			cos.setFont(fontplain, 10);
			cos.moveTextPositionByAmount(20, rect.getHeight() - 70 - line2);
			cos.drawString(prop.getProperty("referral154"));
			cos.endText();
			line2 = line2 + 10;

			cos.beginText();
			cos.setFont(fontplain, 10);
			cos.moveTextPositionByAmount(20, rect.getHeight() - 70 - line2);
			cos.drawString(prop.getProperty("referral155"));
			cos.endText();
			line2 = line2 + 15;

			cos.beginText();
			cos.setFont(fontplain, 10);
			cos.moveTextPositionByAmount(20, rect.getHeight() - 70 - line2);
			cos.drawString(prop.getProperty("referral156"));
			cos.endText();
			line2 = line2 + 10;

			cos.beginText();
			cos.setFont(fontplain, 10);
			cos.moveTextPositionByAmount(20, rect.getHeight() - 70 - line2);
			cos.drawString(prop.getProperty("referral157"));
			cos.endText();
			line2 = line2 + 15;

			cos.beginText();
			cos.setFont(fontplain, 10);
			cos.moveTextPositionByAmount(20, rect.getHeight() - 70 - line2);
			cos.drawString(prop.getProperty("referral158"));
			cos.endText();
			line2 = line2 + 10;

			cos.beginText();
			cos.setFont(fontplain, 10);
			cos.moveTextPositionByAmount(20, rect.getHeight() - 70 - line2);
			cos.drawString(prop.getProperty("referral159"));
			cos.endText();
			line2 = line2 + 10;

			cos.beginText();
			cos.setFont(fontplain, 10);
			cos.moveTextPositionByAmount(20, rect.getHeight() - 70 - line2);
			cos.drawString(prop.getProperty("referral160"));
			cos.endText();
			line2 = line2 + 20;

			cos.beginText();
			cos.setFont(fontplain, 10);
			cos.moveTextPositionByAmount(20, rect.getHeight() - 70 - line2);
			cos.drawString(prop.getProperty("referral161"));
			cos.endText();
			line2 = line2 + 20;

			cos.beginText();
			cos.setFont(fontplain, 10);
			cos.moveTextPositionByAmount(20, rect.getHeight() - 70 - line2);
			cos.drawString(prop.getProperty("referral162"));
			cos.endText();
			line2 = line2 + 15;

			cos.beginText();
			cos.setFont(fontplain, 10);
			cos.moveTextPositionByAmount(30, rect.getHeight() - 70 - line2);
			cos.drawString(prop.getProperty("referral163"));
			cos.endText();
			line2 = line2 + 15;

			cos.beginText();
			cos.setFont(fontplain, 10);
			cos.moveTextPositionByAmount(20, rect.getHeight() - 70 - line2);
			cos.drawString(prop.getProperty("referral164"));
			cos.endText();
			line2 = line2 + 10;

			cos.beginText();
			cos.setFont(fontplain, 10);
			cos.moveTextPositionByAmount(20, rect.getHeight() - 70 - line2);
			cos.drawString(prop.getProperty("referral165"));
			cos.endText();
			line2 = line2 + 10;

			cos.beginText();
			cos.setFont(fontplain, 10);
			cos.moveTextPositionByAmount(20, rect.getHeight() - 70 - line2);
			cos.drawString(prop.getProperty("referral166"));
			cos.endText();
			line2 = line2 + 15;

			cos.beginText();
			cos.setFont(fontplain, 10);
			cos.moveTextPositionByAmount(30, rect.getHeight() - 70 - line2);
			cos.drawString(prop.getProperty("referral167"));
			cos.endText();
			line2 = line2 + 15;

			cos.beginText();
			cos.setFont(fontplain, 10);
			cos.moveTextPositionByAmount(20, rect.getHeight() - 70 - line2);
			cos.drawString(prop.getProperty("referral168"));
			cos.endText();
			line2 = line2 + 10;

			cos.beginText();
			cos.setFont(fontplain, 10);
			cos.moveTextPositionByAmount(20, rect.getHeight() - 70 - line2);
			cos.drawString(prop.getProperty("referral169"));
			cos.endText();
			line2 = line2 + 15;

			cos.beginText();
			cos.setFont(fontplain, 10);
			cos.moveTextPositionByAmount(30, rect.getHeight() - 70 - line2);
			cos.drawString(prop.getProperty("referral170"));
			cos.endText();
			line2 = line2 + 15;

			cos.beginText();
			cos.setFont(fontplain, 10);
			cos.moveTextPositionByAmount(20, rect.getHeight() - 70 - line2);
			cos.drawString(prop.getProperty("referral171"));
			cos.endText();
			line2 = line2 + 10;

			cos.beginText();
			cos.setFont(fontplain, 10);
			cos.moveTextPositionByAmount(20, rect.getHeight() - 70 - line2);
			cos.drawString(prop.getProperty("referral172"));
			cos.endText();
			line2 = line2 + 10;

			cos.beginText();
			cos.setFont(fontplain, 10);
			cos.moveTextPositionByAmount(20, rect.getHeight() - 70 - line2);
			cos.drawString(prop.getProperty("referral173"));
			cos.endText();
			line2 = line2 + 15;

			cos.beginText();
			cos.setFont(fontplain, 10);
			cos.moveTextPositionByAmount(20, rect.getHeight() - 70 - line2);
			cos.drawString(prop.getProperty("referral174"));
			cos.endText();
			line2 = line2 + 10;

			cos.beginText();
			cos.setFont(fontplain, 10);
			cos.moveTextPositionByAmount(20, rect.getHeight() - 70 - line2);
			cos.drawString(prop.getProperty("referral175"));
			cos.endText();
			line2 = line2 + 15;

			cos.beginText();
			cos.setFont(fontplain, 10);
			cos.moveTextPositionByAmount(20, rect.getHeight() - 70 - line2);
			cos.drawString(prop.getProperty("referral176"));
			cos.endText();
			line2 = line2 + 10;

			cos.beginText();
			cos.setFont(fontplain, 10);
			cos.moveTextPositionByAmount(20, rect.getHeight() - 70 - line2);
			cos.drawString(prop.getProperty("referral177"));
			cos.endText();
			line2 = line2 + 15;

			cos.beginText();
			cos.setFont(fontplain, 10);
			cos.moveTextPositionByAmount(20, rect.getHeight() - 70 - line2);
			cos.drawString(prop.getProperty("referral178"));
			cos.endText();
			line2 = line2 + 10;

			cos.beginText();
			cos.setFont(fontplain, 10);
			cos.moveTextPositionByAmount(20, rect.getHeight() - 70 - line2);
			cos.drawString(prop.getProperty("referral179"));
			cos.endText();
			line2 = line2 + 15;

			cos.beginText();
			cos.setFont(fontplain, 10);
			cos.moveTextPositionByAmount(20, rect.getHeight() - 70 - line2);
			cos.drawString(prop.getProperty("referral180"));
			cos.endText();
			line2 = line2 + 10;

			cos.beginText();
			cos.setFont(fontplain, 10);
			cos.moveTextPositionByAmount(20, rect.getHeight() - 70 - line2);
			cos.drawString(prop.getProperty("referral181"));
			cos.endText();
			line2 = line2 + 15;
			cos.close();

			PDPage page4 = new PDPage(PDPage.PAGE_SIZE_A4);
			document.addPage(page4);
			cos = new PDPageContentStream(document, page4);
			line2 = 0;
			cos.beginText();
			cos.setFont(fontplain, 10);
			cos.moveTextPositionByAmount(20, rect.getHeight() - 70 - line2);
			cos.drawString(prop.getProperty("referral182"));
			cos.endText();
			line2 = line2 + 15;

			cos.beginText();
			cos.setFont(fontplain, 10);
			cos.moveTextPositionByAmount(30, rect.getHeight() - 70 - line2);
			cos.drawString(prop.getProperty("referral183"));
			cos.endText();
			line2 = line2 + 20;

			cos.beginText();
			cos.setFont(fontplain, 10);
			cos.moveTextPositionByAmount(20, rect.getHeight() - 70 - line2);
			cos.drawString(prop.getProperty("referral184"));
			cos.endText();
			line2 = line2 + 10;

			cos.beginText();
			cos.setFont(fontplain, 10);
			cos.moveTextPositionByAmount(20, rect.getHeight() - 70 - line2);
			cos.drawString(prop.getProperty("referral185"));
			cos.endText();
			line2 = line2 + 10;

			cos.beginText();
			cos.setFont(fontplain, 10);
			cos.moveTextPositionByAmount(20, rect.getHeight() - 70 - line2);
			cos.drawString(prop.getProperty("referral186"));
			cos.endText();
			line2 = line2 + 10;

			cos.beginText();
			cos.setFont(fontplain, 10);
			cos.moveTextPositionByAmount(20, rect.getHeight() - 70 - line2);
			cos.drawString(prop.getProperty("referral187"));
			cos.endText();
			line2 = line2 + 15;

			cos.beginText();
			cos.setFont(fontplain, 10);
			cos.moveTextPositionByAmount(30, rect.getHeight() - 70 - line2);
			cos.drawString(prop.getProperty("referral188"));
			cos.endText();
			line2 = line2 + 10;

			cos.beginText();
			cos.setFont(fontplain, 10);
			cos.moveTextPositionByAmount(20, rect.getHeight() - 70 - line2);
			cos.drawString(prop.getProperty("referral189"));
			cos.endText();
			line2 = line2 + 10;

			cos.beginText();
			cos.setFont(fontplain, 10);
			cos.moveTextPositionByAmount(20, rect.getHeight() - 70 - line2);
			cos.drawString(prop.getProperty("referral190"));
			cos.endText();
			line2 = line2 + 25;
			line2 = line2 + 20;
			cos.beginText();
			cos.setFont(fontplain, 10);
			cos.moveTextPositionByAmount(20, rect.getHeight() - 70 - line2);
			cos.drawString("Schedule B");
			cos.endText();
			line2 = line2 + 15;

			cos.beginText();
			cos.setFont(fontplain, 10);
			cos.moveTextPositionByAmount(200, rect.getHeight() - 70 - line2);
			cos.drawString("REFERRAL FEE SCHEDULE");
			cos.endText();
			line2 = line2 + 20;

			String[][] content1 = {
					{ " Minimum Mortgage Value", " Maximum Mortgage Value",
							"    Referral Fee", "  Renewal Fee" },
					{ "$50,000.00", "$200,000.00", "$42.00", "$21.00" },
					{ "$200,001.00", "$350,000.00", "$105.00", "$42.00" },
					{ "$350,001.00", "$600,000.00", "$210.00", "$95.00" },
					{ "$600,001.00", "$900,000.00", "$315.00", "$147.00" },
					{ "$900,001.00", "$3,000,000.00", "$420.00", "$210.00" } };

			PdfTableCreation.PdfTableCreationMethod(page4, cos,
					rect.getHeight() - 70 - line2, 20, content1);

			line2 = line2 + 170;
			cos.beginText();
			cos.setFont(fontbold, 10);
			cos.moveTextPositionByAmount(60, rect.getHeight() - 70 - line2);
			cos.drawString("Referrer Signature :");
			cos.endText();
			line2 = line2 + 110;

			if (areusingTouchScreenDevice.equalsIgnoreCase("yes")) {
				try {
					// BufferedImage awtImage = ImageIO.read(new
					// File("simple.jpg"));
					PDXObjectImage ximage = new PDPixelMap(document, image);
					float scale = 0.5f; // alter this value to set the image
										// size
					cos.drawXObject(ximage, 50, rect.getHeight() - 70 - line2,
							ximage.getWidth() * scale, ximage.getHeight()
									* scale);
				} catch (Exception fnfex) {
					Logger.debug("No image for you");
				}
				cos.close();
			} else {

				cos.beginText();
				cos.setFont(fontbold, 10);
				cos.moveTextPositionByAmount(90, rect.getHeight() - 70 - line2
						+ 80);
				cos.drawString(myTypedName);
				cos.endText();
				line2 = line2 + 20;

				cos.beginText();
				cos.setFont(fontplain, 10);
				cos.moveTextPositionByAmount(60, rect.getHeight() - 70 - line2
						+ 70);
				cos.drawString("My typed name above serves as my electronic signature for the above agreement.");
				cos.endText();
				cos.close();
			}

			/*
			 * System.out.println("daoument "+document.toString()+"ddd"+document.
			 * getCurrentAccessPermission
			 * ()+"do "+document.getDocumentInformation() +"doc ");
			 */
			Logger.debug("OutPut FileName********" + outputFileName);
			document.save(outputFileName);
			Logger.debug("OutPut FileName&&&&&&&&&&&" + outputFileName);

			document.close();
			System.out
					.println("OutPut FileName##############" + outputFileName);

			try {
				File fl = new File(outputFileName);
				Logger.debug("size " + fl.exists() + ""
						+ (file.length() / 1024));
			} catch (Exception e) {

			}
			/* Logger.debug("The path pdf file created"); */
			// return outputFileName;
		} catch (Exception e) {
			e.printStackTrace();

		}

		return outputFileName;

	}

}
