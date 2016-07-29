package war.vaadin;

import org.slf4j.LoggerFactory;

import com.couchbase.client.java.document.json.JsonObject;
import com.debortoliwines.openerp.api.OpeneERPApiException;
import com.google.gwt.json.client.JSONObject;

import war.couchbase.CombinedRecommendation;
import war.couchbase.CouchbaseData;
import war.couchbase.MarketingNotes;
import war.couchbase.MarketingNotesOperation;
import war.couchbase.OrignalDetails;
import war.couchbase.RecommendDetails;
import war.couchbase.Recommendation;
import war.couchbase.UwAppAllAlgo;
import war.couchbase.dao.CouchbaseDaoServiceException;
import war.couchbase.dao.service.CouchBaseService;
import war.stagelead.RestCall;
import war.stagelead.StageLead;
import war.syml.TestDevCRM;
import war.util.JsonConvertion;

import java.io.IOException;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.sql.SQLException;
import java.util.Map;
import java.util.Set;

import javax.validation.constraints.AssertTrue;

import org.apache.xmlrpc.XmlRpcException;
import org.codehaus.jettison.json.JSONException;
import org.junit.Assert;
import org.junit.Ignore;
import org.junit.Test;

public class ProposalClientTest {

	private static org.slf4j.Logger logger = LoggerFactory.getLogger(ProposalClientTest.class);
	
	CouchBaseService service = new CouchBaseService();
	
	CouchbaseData data = new CouchbaseData();
	UwAppAllAlgo algo = data.getDataFromCouchbase("3584");
	Set<MarketingNotes> marketingNotes = algo.getMarketingNotes();
	Map<String, String> mapofNotesContents = MarketingNotesOperation.getNoteContentByNotes(marketingNotes);
	MarketingNotesOperation operation = new MarketingNotesOperation();
	
	TestDevCRM test = new TestDevCRM();
	@Ignore
	@Test
	public void testgetFormLayoutforOriginalPositive() {

		Assert.assertNotNull("The marketing not should not be null" + marketingNotes);

		Assert.assertFalse("should not be ", marketingNotes.size() == 0);

		Assert.assertTrue(mapofNotesContents.containsKey("OriginalDetails"));

		String originavalue = mapofNotesContents.get("OriginalDetails");
		OrignalDetails originaldetaials = JsonConvertion.fromJsonforOriginalDetails(originavalue);

		Assert.assertNotNull("The null value should not come:", originaldetaials);

	}

	@Ignore
	@Test

	public void testgetFormLayoutforOriginalNegative() {

		Assert.assertNotNull("The marketing not should not be null" + marketingNotes);
		Assert.assertTrue("should not be ", marketingNotes.size() > 0);

		Map<String, String> mapofNotesContents = MarketingNotesOperation.getNoteContentByNotes(marketingNotes);
		Assert.assertFalse(mapofNotesContents.containsKey("originalDetails"));

		String originavalue = mapofNotesContents.get("OriginalDetails");
		OrignalDetails originaldetaials = JsonConvertion.fromJsonforOriginalDetails(originavalue);

		Assert.assertNotEquals("string", originaldetaials.getProductID());

	}
	@Ignore
	@Test
	public void testAllLabelDisplay() {
		Map<String, String> mapofNotesContents = MarketingNotesOperation.getNoteContentByNotes(marketingNotes);
		Assert.assertFalse(mapofNotesContents.size() == 0);

		Assert.assertTrue(mapofNotesContents.containsKey("Instructions"));
		Assert.assertTrue(mapofNotesContents.containsKey("OriginalDesired"));
		Assert.assertTrue(mapofNotesContents.containsKey("HelpingAchieve"));
		Assert.assertTrue(mapofNotesContents.containsKey("WhySense"));
		Assert.assertTrue(mapofNotesContents.containsKey("DebtRestructure"));
		Assert.assertTrue(mapofNotesContents.containsKey("Highlights"));
		Assert.assertTrue(mapofNotesContents.containsKey("Options"));
		Assert.assertTrue(mapofNotesContents.containsKey("CombinedTable"));
		Assert.assertTrue(mapofNotesContents.containsKey("Notes"));
		Assert.assertTrue(mapofNotesContents.containsKey("FixedRecommendations"));

	}
	@Ignore
	@Test
	public void testCompanyNameifexists() throws XmlRpcException, OpeneERPApiException {
		/**
		 * this class for use locally only for developmnet
		 * HttpsConnectionCase.getCompanyName(oppertunutyId);
		 */

		String companyName = test.getCompanyName("3584");
		Assert.assertNotNull(companyName);
		Assert.assertEquals("WFG", companyName);
		Assert.assertTrue(mapofNotesContents.containsKey("RecommendDetails"));

		/**
		 * checking if company name is wfg then RecommendDetails productid and
		 * productid1 should not be null
		 */

		RecommendDetails recdetails = new RecommendDetails();
		recdetails = JsonConvertion.fromJsonforRecommendDetails(mapofNotesContents.get("RecommendDetails"));
		Assert.assertNotNull(recdetails.getProductID(), recdetails.getProductID1());
		Assert.assertNotEquals(recdetails.getProductID(), recdetails.getProductID1());
	}

	/*
	 * @Test public void
	 */
@Ignore
	@Test
	public void testCompanyNameifNotexists() throws XmlRpcException, OpeneERPApiException {
		// this class for use locally only
		TestDevCRM test = new TestDevCRM();
		// for develpemnet
		// HttpsConnectionCase.getCompanyName(oppertunutyId);
		String companyName = test.getCompanyName("4319");
		Assert.assertEquals("", companyName);

		Assert.assertTrue(mapofNotesContents.containsKey("RecommendDetails"));

		// checking if company name is wfg then RecommendDetails productid and
		// productid1 should not be null
		RecommendDetails recdetails = new RecommendDetails();
		recdetails = JsonConvertion.fromJsonforRecommendDetails(mapofNotesContents.get("RecommendDetails"));
		Assert.assertNotNull("The product id should not be null", recdetails.getProductID());
	}
/*@Ignore
	@Test
public void testSubmitforOriginalDetails() {

		*//**
		 * The function on click Textbox and table row selected
		 * Once clicked any on them either table row or Textbox the other  will not be able to select
		 * Once Selected the Following functon will call 
		 * 1)Originalsubmitcall()
		 * 2)RecommendationDetailSubmitCall()
		 * 3)RecommendationSingleSubmitCall()
		 * 4)RecommendationCombinedSubmitCall
		 * @throws CouchbaseDaoServiceException 
		
		 *//*
		MarketingNotesOperation operation = new MarketingNotesOperation();
		OrignalDetails  originalDetails =JsonConvertion.fromJsonforOriginalDetails(mapofNotesContents.get("OriginalDetails"));
		originalDetails.setProductID("1889");
		Assert.assertNotNull(originalDetails);
		operation.storeSelectOriginalDetails(algo, originalDetails,"127.0.0.1");
}*/
@Ignore
	@Test
	public void testSubmitforRecommendationDetails() throws CouchbaseDaoServiceException{

		
		
		RecommendDetails  recommendationDetails =JsonConvertion.fromJsonforRecommendDetails(mapofNotesContents.get("RecommendDetails"));
		recommendationDetails.setProductID("1891");
		Assert.assertNotNull(recommendationDetails);
		operation.storeSelectRecommnedation(algo,recommendationDetails,"127.0.0.1");
		JsonObject json = service.getCouhbaseDataByKey("SelectRec_3584");
		logger.debug("the value from couchbase selectRec_3584"+json.getArray("Recommendations").get(0).toString());
	
		JsonObject value =(JsonObject) json.getArray("Recommendations").get(0);
		Assert.assertEquals(recommendationDetails.getProductID(),value.get("selectedProductID"));
		
}
@Ignore
	@Test
	public void teststoreSelectRecommnedationForSingle() throws CouchbaseDaoServiceException{
		
		Recommendation rec = new Recommendation();
		rec.setProductID("1891");
		operation.storeSelectRecommnedationForSingle(algo,rec,"127.0.0.1");
		JsonObject json = service.getCouhbaseDataByKey("SelectRec_3584");
		JsonObject value =(JsonObject) json.getArray("Recommendations").get(0);
		Assert.assertEquals("Single",value.get("productType"));
	}
	
	
	
	@Ignore
	@Test
	public void teststoreSelectRecommnedationForCombine() throws CouchbaseDaoServiceException, XmlRpcException, OpeneERPApiException{
		
		CombinedRecommendation rec = new CombinedRecommendation();
		rec.setBaseProductID("1884");
		operation.storeSelectRecommnedationForCombined(algo,rec,"127.0.0.1");
		JsonObject json = service.getCouhbaseDataByKey("SelectRec_3584");
		JsonObject value =(JsonObject) json.getArray("Recommendations").get(0);
		Assert.assertEquals("Single",value.get("productType"));
		
		String companyName =test.getCompanyName("3584");
		Assert.assertEquals("WFG", companyName);
	}
	
	
	
@Ignore
	@Test
	public void testRecommendations() {
		Set<Recommendation> rec = algo.getRecommendations();

		Assert.assertNotNull(rec);
		Assert.assertFalse(rec.isEmpty());

	}
@Ignore
	@Test
	public void testCombineTable() {
		Set<CombinedRecommendation> combine = algo.getCombinedRecommendation();
		Assert.assertFalse(combine.size() == 0);

	}


@Ignore
@Test
public void  updateProductTest() throws XmlRpcException, OpeneERPApiException{
	try{
		test.updateProduct("3584", "1889");
		Assert.fail("error in update the product ");
		}catch(XmlRpcException| OpeneERPApiException e){logger.error("error update product"+e.getMessage());}
}
@Ignore
@Test
public void  updateDealTest() throws XmlRpcException, OpeneERPApiException{
	
	try{
	test.updateDeal("Marketing", "field", "3584");
	Assert.fail("error in update the Deal ");
	}catch(XmlRpcException| OpeneERPApiException e){logger.error("error update deattest"+e.getMessage());}
}
@Ignore
@Test
public void tesPostSelectionChange() throws Exception{
	
	try{
		test.chnageStageToPostSelction("3584");
		Assert.fail("error in chnageStageToPostSelction ");
		}catch(XmlRpcException| OpeneERPApiException e){logger.error("error chnageStageToPostSelction"+e.getMessage());}

	
}


@Ignore
@Test
public void testgetcrmLeadFrompostgress() throws SQLException, IOException{
	StageLead lead =new StageLead();
	String leadvalue =lead.getcrmLeadFrompostgress("3584");
	
	Assert.assertNotNull(leadvalue);
	
}
@Ignore
@Test 
public void testRestCallOnStageMail() throws SQLException, IOException, KeyManagementException, NoSuchAlgorithmException{
	StageLead lead =new StageLead();
	String leadvalue =lead.getcrmLeadFrompostgress("3584");
	
	RestCall restcall = new RestCall(leadvalue);
	try{
		restcall.restcallTostagMail(leadvalue);
		Assert.fail("the error while call RestCall");
	}catch(KeyManagementException |NoSuchAlgorithmException |IOException e){
		logger.error("error while post to stage mail "+e.getMessage());
	}
}


@Test 
public void testJsonConversion() throws JSONException {
	
	String originavalue = mapofNotesContents.get("OriginalDetails");
	OrignalDetails originaldetails = JsonConvertion.fromJsonforOriginalDetails(originavalue);
	logger.debug(""+originaldetails);
	originaldetails.setAmortization("30");
	originaldetails.setDownpaymentEquity("2222222");
	
	
	JsonConvertion.getJsonObject(originaldetails);

	

}


}