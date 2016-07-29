package infrastracture.couchbase;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;

import com.couchbase.client.java.Bucket;
import com.couchbase.client.java.bucket.BucketManager;
import com.couchbase.client.java.document.json.JsonObject;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.syml.couchbase.dao.CouchbaseServiceException;
import com.syml.couchbase.dao.CouchbaseUtil;
import com.syml.couchbase.dao.service.CouchBaseService;

public class CouchBaseOperation {
	
	private static org.slf4j.Logger logger = play.Logger.underlying();
	
	
	ObjectMapper object = new ObjectMapper();
	CouchBaseService couchBaseService = new CouchBaseService();
	CouchbaseUtil couchbaseUtil = new CouchbaseUtil();

	
/**
 * To store survEy data into CouchBASE 
 * @param key
 * @param data
 * @throws CouchbaseServiceException
 */
	public void storeDataInCouchBase(String key, JsonObject data)throws CouchbaseServiceException {

		logger.info("(.) inside storeDataInCouchBase method of CouchBaseOperation class");

		data.put("Submission_Date_Time1b", getSubmissionDate());
		try {
			logger.info("The data storing by key-->" + key);
			couchBaseService.storeDataToCouchbase(key, data);
		} catch (CouchbaseServiceException e) {
			throw new  CouchbaseServiceException("error  storing data in couchbase for key :  "+ key , e);
		}

	}
	/**
	 * To delet design document from couchbase 	
	 * @param key
	 * @throws CouchbaseServiceException 
	 * @throws CouchbaseDaoServiceException
	 */
	public void removeDesginDocumentById(String key) throws CouchbaseServiceException {
		logger.debug("(.) inside removeDesignDocumentById ");
		CouchbaseUtil couchbaseUtil = new CouchbaseUtil();
		Bucket bucket = couchbaseUtil.getCouchbaseClusterConnection();
		BucketManager bucketManager = bucket.bucketManager();
		bucketManager.removeDesignDocument("client_" + key);
		
	}
	/**
	 * To get Current with correct format 
	 * @return
	 */
	private String getSubmissionDate() {
		DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
		Calendar cal = Calendar.getInstance();
		String currentDateTime = (dateFormat.format(cal.getTime()));
		return currentDateTime;
	}                


	
}
