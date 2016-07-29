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
import com.syml.proposalRefinance.couchbase.CouchbaseData;
import com.syml.proposalRefinance.couchbase.UwAppAllAlgo;
import com.syml.proposalRefinance.couchbase.dao.CouchbaseDaoServiceException;
import com.syml.proposalRefinance.couchbase.dao.service.CouchBaseService;

public class CouchbaseDatatest {
	static Logger logger = LoggerFactory.getLogger(CouchBaseService.class);
	CouchbaseData data = new CouchbaseData();
	CouchBaseService service = new CouchBaseService();

	@Test
	public void getDataCouchbaseTestTrue() throws CouchbaseDaoServiceException {

		JsonObject json = data.getDataCouchbase("4406");

		Assert.assertNotNull(json);
		Assert.assertEquals("4406", json.get("opportunityID"));

	}

	@Test
	public void getDataCouchbaseTestfalse() throws CouchbaseDaoServiceException {
		JsonObject json = data.getDataCouchbase("2222");
		Assert.assertNull(json);
	}

	@Test
	public void testgetDataFromCouchbaseTrue()
			throws CouchbaseDaoServiceException, JsonParseException, JsonMappingException, IOException {
		String oppid = "3584";
		UwAppAllAlgo algopojo = new UwAppAllAlgo();
		String algo = service.getCouhbaseDataByKey("uwapps2couchbase_allProductAlgoJSON_" + oppid).toString();
		
		ObjectMapper mapper = new ObjectMapper();

		algopojo = mapper.readValue(algo, UwAppAllAlgo.class);
		Assert.assertNotNull(algo, UwAppAllAlgo.class);
		Assert.assertNotEquals("3584", algopojo.getOpportunityID());
}

}
