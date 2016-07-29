package infrastructure;

import infrastracture.couchbase.CouchBaseOperation;
import infrastracture.odoo.OdooException;
import infrastracture.odoo.OdooHelpler;

import java.util.ArrayList;

import org.junit.Assert;
import org.junit.Ignore;
import org.junit.Test;

import com.couchbase.client.java.document.json.JsonObject;
import com.debortoliwines.openerp.api.Session;
import com.syml.client.ClientSurvey;
import com.syml.client.ClientSurveyException;
import com.syml.couchbase.dao.CouchbaseServiceException;
import com.syml.couchbase.dao.service.CouchBaseService;

import controllers.Client;



public class CouchBaseOperationTest {
ClientSurvey clientSurvey = new ClientSurvey();
CouchBaseService couchBaseService = new CouchBaseService();
CouchBaseOperation couchBaseOperation = new CouchBaseOperation();
OdooHelpler odooHelpler = new OdooHelpler();
	@Ignore
	@Test
	public void teststoreDataInCouchBase() throws CouchbaseServiceException{
		String key = "survey_123";
		
		JsonObject jsonObject = JsonObject.create();
		jsonObject.put("name", "manoj");
		jsonObject.put("age", "26");
		
		
				
		couchBaseOperation.storeDataInCouchBase(key, jsonObject);
		jsonObject =couchBaseService.getCouhbaseDataByKey(key);
		
		Assert.assertNotNull(jsonObject);
		Assert.assertEquals("manoj", jsonObject.get("name").toString());
		
	}
	
	@Ignore
	@Test 
	public void testGetDesignDoc() throws CouchbaseServiceException, ClientSurveyException{
		ArrayList<Client> clientList =clientSurvey.getListOfClientSurvey("4405");
		
		Assert.assertTrue(clientList.size()>0);
	}
	
	@Ignore
	@Test
	public void testgetReferralSurveyFromCouchbase() throws CouchbaseServiceException, ClientSurveyException{
		clientSurvey.getListOfClientSurvey("4405");
	}
	@Ignore
	@Test
	public void testremoveDesginDocumentById() throws CouchbaseServiceException{
			couchBaseOperation.removeDesginDocumentById("4405");
	}
	
	@Test
	public void testOdooConnection() throws CouchbaseServiceException, OdooException{
		Session session=null;
		session = odooHelpler.getOdooConnection();
		Assert.assertNull(session);
	}
	
}
