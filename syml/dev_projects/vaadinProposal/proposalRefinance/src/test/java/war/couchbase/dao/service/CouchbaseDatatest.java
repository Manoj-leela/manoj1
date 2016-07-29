package war.couchbase.dao.service;
import java.io.IOException;

import org.junit.Assert;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.couchbase.client.java.document.json.JsonObject;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import war.couchbase.CouchbaseData;
import war.couchbase.UwAppAllAlgo;
import war.couchbase.dao.CouchbaseDaoServiceException;
public class CouchbaseDatatest {
	static	Logger logger=LoggerFactory.getLogger(CouchBaseService.class);
	CouchbaseData  data = new CouchbaseData();
	CouchBaseService service =new CouchBaseService();
	
	@Test
	public void getDataCouchbaseTest() throws CouchbaseDaoServiceException{
		
		JsonObject json =data.getDataCouchbase("4406");
		
		Assert.assertNotNull(json);
	}
	
@Test 
public void testgetDataFromCouchbase() throws CouchbaseDaoServiceException, JsonParseException, JsonMappingException, IOException{
	String oppid="3584";
	UwAppAllAlgo algopojo = new UwAppAllAlgo();
	 String algo =service.getCouhbaseDataByKey("uwapps2couchbase_allProductAlgoJSON_"+oppid ).toString();
		//Assert.assertEquals("not null",algo);
		
		ObjectMapper mapper = new ObjectMapper();
		
		algopojo=mapper.readValue(algo, UwAppAllAlgo.class);
		Assert.assertNotNull(algo,UwAppAllAlgo.class);
		Assert.assertNotEquals(algo,UwAppAllAlgo.class);
		Assert.assertNull(algopojo.getApplicantsID());
		
		
}




}
