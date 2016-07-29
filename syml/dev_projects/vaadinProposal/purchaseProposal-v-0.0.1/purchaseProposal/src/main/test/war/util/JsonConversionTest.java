package war.util;

import java.util.Map;

import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;
import org.junit.Assert;
import org.junit.Ignore;
import org.junit.Test;

import com.syml.purchaseProposal.couchbase.CouchbaseData;
import com.syml.purchaseProposal.couchbase.MarketingNotesOperation;
import com.syml.purchaseProposal.couchbase.OrignalDetails;
import com.syml.purchaseProposal.couchbase.RecommendDetails;
import com.syml.purchaseProposal.couchbase.UwAppAllAlgo;
import com.syml.purchaseProposal.couchbase.dao.CouchbaseDaoServiceException;
import com.syml.purchaseProposal.couchbase.dao.ProposalException;
import com.syml.purchaseProposal.util.JsonConvertion;

public class JsonConversionTest {
	CouchbaseData data = new CouchbaseData();
	@Ignore
	@Test
	public void fromJsonforOriginalDetailsTestFalse() throws CouchbaseDaoServiceException, ProposalException{
	UwAppAllAlgo algo = data.getDataFromCouchbase("3584");
	
	Map<String,String> mapofNotesContents = MarketingNotesOperation.getNoteContentByNotes(algo.getMarketingNotes());
	Assert.assertTrue(mapofNotesContents.containsKey("OriginalDetails"));
	OrignalDetails details =JsonConvertion.fromJsonforOriginalDetails(mapofNotesContents.get("OriginalDetails"));
	Assert.assertNotNull(details);
	
	Assert.assertNotEquals("3333333", details.getProductID());
	}
	@Ignore
	@Test
	public void fromJsonforRecommendDetailsTestFalse() throws ProposalException, CouchbaseDaoServiceException{

		UwAppAllAlgo algo = data.getDataFromCouchbase("3584");
		
		Map<String,String> mapofNotesContents = MarketingNotesOperation.getNoteContentByNotes(algo.getMarketingNotes());
		Assert.assertTrue(mapofNotesContents.containsKey("OriginalDetails"));
		RecommendDetails rec =JsonConvertion.fromJsonforRecommendDetails(mapofNotesContents.get("OriginalDetails"));
		Assert.assertNotNull(rec);
		
		Assert.assertNotEquals("11111", rec.getProductID());
	}
	@Test
	
	public void getJsonObjectTest() throws JSONException{
		JSONObject value =JsonConvertion.getJsonObject("{\"propertyValue\":\"800,000.00\"}");
		Assert.assertNotNull(value.get("propertyValue"));
	}
	@Test
	public void getJsonObjectStringTest() throws JSONException, ProposalException{
	OrignalDetails original = new OrignalDetails();
	original.setAmortization("50");
	original.setInsuranceAmount("55");
	original.setDownpaymentEquity("34343");
	
		String value =JsonConvertion.getJsonObject(original);
		Assert.assertNotNull(value);
	}
	
}
