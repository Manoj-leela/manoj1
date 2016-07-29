package controllers;

import java.io.IOException;

import net.iharder.Base64;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import play.mvc.Controller;
import play.mvc.Result;
import views.html.index;

import com.couchbase.client.java.document.json.JsonArray;
import com.couchbase.client.java.document.json.JsonObject;
import com.syml.couchbase.dao.CouchbaseServiceException;
import com.syml.couchbase.dao.service.CouchBaseService;

public class Application extends Controller {

	CouchBaseService service = null;

	static Logger log = LoggerFactory.getLogger(Application.class);

	public static Result index() {
		return ok(index.render("Your new application is ready."));
	}

	
	/**
	 * Get pdfdata on key doc_Disclosures_   and Disclosures_
	 * @return
	 * @throws IOException
	 */
	public static Result getPdfData() throws IOException {

		log.debug("(.) inside getPdfData() in Application controller ");

		
		String couchBaseKey = request().getQueryString("id");

		play.Logger.info("Couchbas Document Key " + request().getQueryString("id"));

		if (couchBaseKey.contains("doc_Disclosures_")) {
			play.Logger.info("insid doc_Disclosures_-------------------");

			return getPdfIfdocDisclosuresExist(couchBaseKey);

		} else if (couchBaseKey.contains("Disclosures_")) {
			play.Logger.info("insid Disclosures_   -------------------");
			return getPdfIfDisclosuresExist(couchBaseKey);
		}else if(!couchBaseKey.contains("abcdefghijklman")){
			return getPdfIfdocDisclosuresExist(couchBaseKey);

		}

		return ok("CouchBase key Not Found");

	}

	
	public static Result getPdfIfdocDisclosuresExist(String couchBaseKey) {

		play.Logger.debug("(.) getPdfIfdocDisclosuresExist controller class");
		JsonObject json = null;
		CouchBaseService service = new CouchBaseService();
		try {
			json = service.getCouhbaseDataByKey(couchBaseKey);
		} catch (CouchbaseServiceException e) {
			play.Logger.error("The document not found on this key" + e.getMessage());
		}
		if (json != null) {
			String pdfdata = json.get("attachement").toString();
			if (pdfdata == null) {
				return ok(index.render("Data is Not in Pdf Format"));

			} else {
				return ok(index.render(pdfdata));

			}

		} else {
			return ok(index.render("Data is Not Pdf Format"));
		}

	}

	public static Result getPdfIfDisclosuresExist(String couchBaseKey) {
		play.Logger.debug("(.) getPdfIfDisclosuresExist controller class");
		JsonObject json = null;
		CouchBaseService service = new CouchBaseService();
		try {
			json = service.getCouhbaseDataByKey(couchBaseKey);
		} catch (CouchbaseServiceException e) {
			play.Logger.error("The document not found on this key" + e.getMessage());
		}

		if (json != null) {

			String pdfdata = null;

			try {
				JsonArray onjObject = (JsonArray) json.get("Mortgage Brokerage DisclosuresPdffile");
				byte[] data = new byte[onjObject.size()];
				for (int i = 0; i < onjObject.size(); i++) {
					Integer dd = (Integer) onjObject.get(i);
					data[i] = dd.byteValue();
				}

				pdfdata = Base64.encodeBytes(data);

			} catch (Exception e) {
				play.Logger.error("Error converting byteArray to base64 " + e);
			}
			if (pdfdata == null) {
				return ok(index.render("Data is Not in Pdf Format"));

			} else {
				return ok(index.render(pdfdata));

			}

		} else {
			return ok(index.render("Data is Not Pdf Format"));
		}
	}

}
