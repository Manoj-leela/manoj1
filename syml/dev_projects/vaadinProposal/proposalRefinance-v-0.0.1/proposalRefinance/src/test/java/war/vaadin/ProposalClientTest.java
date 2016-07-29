package war.vaadin;

import org.slf4j.LoggerFactory;

import com.couchbase.client.java.document.json.JsonObject;
import com.debortoliwines.openerp.api.OpeneERPApiException;
import com.syml.proposalRefinance.couchbase.CombinedRecommendation;
import com.syml.proposalRefinance.couchbase.CouchbaseData;
import com.syml.proposalRefinance.couchbase.MarketingNotes;
import com.syml.proposalRefinance.couchbase.MarketingNotesOperation;
import com.syml.proposalRefinance.couchbase.OrignalDetails;
import com.syml.proposalRefinance.couchbase.RecommendDetails;
import com.syml.proposalRefinance.couchbase.Recommendation;
import com.syml.proposalRefinance.couchbase.UwAppAllAlgo;
import com.syml.proposalRefinance.couchbase.dao.CouchbaseDaoServiceException;
import com.syml.proposalRefinance.couchbase.dao.ProposalException;
import com.syml.proposalRefinance.couchbase.dao.service.CouchBaseService;
import com.syml.proposalRefinance.openerp.TestDevCRM;
import com.syml.proposalRefinance.stagelead.RestCall;
import com.syml.proposalRefinance.stagelead.StageLead;
import com.syml.proposalRefinance.util.JsonConvertion;

import java.io.IOException;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.sql.SQLException;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collector;

import org.apache.xmlrpc.XmlRpcException;
import org.codehaus.jettison.json.JSONException;
import org.junit.Assert;
import org.junit.Ignore;
import org.junit.Test;

public class ProposalClientTest {

	private static org.slf4j.Logger logger = LoggerFactory.getLogger(ProposalClientTest.class);

	CouchBaseService service = new CouchBaseService();

	CouchbaseData data = new CouchbaseData();
	MarketingNotesOperation operation = new MarketingNotesOperation();

	TestDevCRM test = new TestDevCRM();

	@Ignore
	@Test
	public void testgetFormLayoutforOriginalPositive() throws ProposalException, CouchbaseDaoServiceException {
		UwAppAllAlgo algo = data.getDataFromCouchbase("3584");

		Set<MarketingNotes> marketingNotes = algo.getMarketingNotes();
		Map<String, String> mapofNotesContents = MarketingNotesOperation.getNoteContentByNotes(marketingNotes);

		Assert.assertNotNull("The marketing not should not be null" + marketingNotes);

		Assert.assertFalse("should not be ", marketingNotes.size() == 0);

		Assert.assertTrue(mapofNotesContents.containsKey("OriginalDetails"));

		String originavalue = mapofNotesContents.get("OriginalDetails");
		OrignalDetails originaldetaials = JsonConvertion.fromJsonforOriginalDetails(originavalue);

		Assert.assertNotNull("The null value should not come:", originaldetaials);

	}

	@Ignore
	@Test

	public void testgetFormLayoutforOriginalNegative() throws ProposalException, CouchbaseDaoServiceException {
		UwAppAllAlgo algo = data.getDataFromCouchbase("3584");

		Set<MarketingNotes> marketingNotes = algo.getMarketingNotes();
		Map<String, String> mapofNotesContents = MarketingNotesOperation.getNoteContentByNotes(marketingNotes);

		Assert.assertNotNull("The marketing not should not be null" + marketingNotes);
		Assert.assertTrue("should not be ", marketingNotes.size() > 0);

		Assert.assertFalse(mapofNotesContents.containsKey("originalDetails"));

		String originavalue = mapofNotesContents.get("OriginalDetails");
		OrignalDetails originaldetaials = JsonConvertion.fromJsonforOriginalDetails(originavalue);

		Assert.assertNotEquals("string", originaldetaials.getProductID());

	}

	@Ignore
	@Test
	public void testAllLabelDisplay() throws CouchbaseDaoServiceException {
		UwAppAllAlgo algo = data.getDataFromCouchbase("3584");

		Set<MarketingNotes> marketingNotes = algo.getMarketingNotes();
		Map<String, String> mapofNotesContents = MarketingNotesOperation.getNoteContentByNotes(marketingNotes);

		Assert.assertTrue(mapofNotesContents.size() > 0);

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
	public void testAllLabelDisplayfalse() throws CouchbaseDaoServiceException {
		UwAppAllAlgo algo = data.getDataFromCouchbase("3584");

		Set<MarketingNotes> marketingNotes = algo.getMarketingNotes();
		Map<String, String> mapofNotesContents = MarketingNotesOperation.getNoteContentByNotes(marketingNotes);

		Assert.assertTrue(mapofNotesContents.size() > 0);

		Assert.assertFalse(mapofNotesContents.containsKey("Instructionss"));
		Assert.assertFalse(mapofNotesContents.containsKey("Original Desiredd"));
		Assert.assertFalse(mapofNotesContents.containsKey("Helping Achievee"));
		Assert.assertFalse(mapofNotesContents.containsKey("WhySenssde"));
		Assert.assertFalse(mapofNotesContents.containsKey("DebtRestructusdre"));
		Assert.assertFalse(mapofNotesContents.containsKey("Highlightswe"));
		Assert.assertFalse(mapofNotesContents.containsKey("Options12"));
		Assert.assertFalse(mapofNotesContents.containsKey("Combined Table"));
		Assert.assertFalse(mapofNotesContents.containsKey("Note s"));
		Assert.assertFalse(mapofNotesContents.containsKey("Fixed Recommendations"));

	}

	@Ignore
	@Test
	public void testCompanyNameifexists()
			throws XmlRpcException,  ProposalException, CouchbaseDaoServiceException, OpeneERPApiException {
		/**
		 * this class for use locally only for developmnet
		 * HttpsConnectionCase.getCompanyName(oppertunutyId);
		 */

		String companyName = test.getCompanyName("3584");
		Assert.assertNotNull(companyName);
		Assert.assertEquals("WFG", companyName);

		/**
		 * checking if company name is wfg then RecommendDetails productid and
		 * productid1 should not be null
		 */
		UwAppAllAlgo algo = data.getDataFromCouchbase("3584");

		Set<MarketingNotes> marketingNotes = algo.getMarketingNotes();
		Map<String, String> mapofNotesContents = MarketingNotesOperation.getNoteContentByNotes(marketingNotes);

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
	public void testCompanyNameifNotexists()
			throws XmlRpcException, OpeneERPApiException, ProposalException, CouchbaseDaoServiceException {
		// this class for use locally only
		TestDevCRM test = new TestDevCRM();
		UwAppAllAlgo algo = data.getDataFromCouchbase("3584");

		Set<MarketingNotes> marketingNotes = algo.getMarketingNotes();
		Map<String, String> mapofNotesContents = MarketingNotesOperation.getNoteContentByNotes(marketingNotes);
		String companyName = test.getCompanyName("4319");
		Assert.assertEquals("", companyName);

		Assert.assertTrue(mapofNotesContents.containsKey("RecommendDetails"));

		
		RecommendDetails recdetails = new RecommendDetails();
		recdetails = JsonConvertion.fromJsonforRecommendDetails(mapofNotesContents.get("RecommendDetails"));
		Assert.assertNotNull("The product id should not be null", recdetails.getProductID());
	}

	/*
	 * @Ignore
	 * 
	 * @Test public void testSubmitforOriginalDetails() {
	 * 
	 *//**
		 * The function on click Textbox and table row selected Once clicked any
		 * on them either table row or Textbox the other will not be able to
		 * select Once Selected the Following functon will call
		 * 1)Originalsubmitcall() 2)RecommendationDetailSubmitCall()
		 * 3)RecommendationSingleSubmitCall() 4)RecommendationCombinedSubmitCall
		 * 
		 * @throws CouchbaseDaoServiceException
		 * @throws ProposalException
		 * 
		 *//*
		 * MarketingNotesOperation operation = new MarketingNotesOperation();
		 * OrignalDetails originalDetails
		 * =JsonConvertion.fromJsonforOriginalDetails(mapofNotesContents.get(
		 * "OriginalDetails")); originalDetails.setProductID("1889");
		 * Assert.assertNotNull(originalDetails);
		 * operation.storeSelectOriginalDetails(algo,
		 * originalDetails,"127.0.0.1"); }
		 */
	@Ignore
	@Test
	public void testSubmitforRecommendationDetails() throws CouchbaseDaoServiceException, ProposalException {
		UwAppAllAlgo algo = data.getDataFromCouchbase("3584");

		Set<MarketingNotes> marketingNotes = algo.getMarketingNotes();
		Map<String, String> mapofNotesContents = MarketingNotesOperation.getNoteContentByNotes(marketingNotes);

		RecommendDetails recommendationDetails = JsonConvertion
				.fromJsonforRecommendDetails(mapofNotesContents.get("RecommendDetails"));
		recommendationDetails.setProductID("1891");
		Assert.assertNotNull(recommendationDetails);
		operation.storeSelectRecommnedation(algo, recommendationDetails, "127.0.0.1");
		JsonObject json = service.getCouhbaseDataByKey("SelectRec_3584");
		logger.debug("the value from couchbase selectRec_3584" + json.getArray("Recommendations").get(0).toString());

		JsonObject value = (JsonObject) json.getArray("Recommendations").get(0);
		Assert.assertEquals("1891", value.get("selectedProductID"));

	}

	@Ignore
	@Test
	public void teststoreSelectRecommnedationForSingle() throws CouchbaseDaoServiceException, ProposalException {
		UwAppAllAlgo algo = data.getDataFromCouchbase("3584");

		Recommendation rec = new Recommendation();
		rec.setProductID("1891");
		operation.storeSelectRecommnedationForSingle(algo, rec, "127.0.0.1");
		JsonObject json = service.getCouhbaseDataByKey("SelectRec_3584");
		JsonObject value = (JsonObject) json.getArray("Recommendations").get(0);
		Assert.assertEquals("Single", value.get("productType"));
	}
	@Ignore
	@Test
	public void teststoreSelectRecommnedationForSingleFalse() throws CouchbaseDaoServiceException, ProposalException {
		UwAppAllAlgo algo = data.getDataFromCouchbase("3584");

		Recommendation rec = new Recommendation();
		rec.setProductID("2222");
		
		try{
			operation.storeSelectRecommnedationForSingle(algo, rec, "127.0.0.1");
		}catch(ProposalException e){
			Assert.fail("this 2222 product id not present in couchbase ");
		}
		
	}

	@Ignore
	@Test
	public void teststoreSelectRecommnedationForCombine() throws CouchbaseDaoServiceException, XmlRpcException,
			OpeneERPApiException, JSONException, ProposalException {
		UwAppAllAlgo algo = data.getDataFromCouchbase("3584");

		CombinedRecommendation rec = new CombinedRecommendation();
		rec.setBaseProductID("1891");
		operation.storeSelectRecommnedationForCombined(algo, rec, "127.0.0.1");
		JsonObject json = service.getCouhbaseDataByKey("SelectRec_3584");
		JsonObject value = (JsonObject) json.getArray("Recommendations").get(0);
		Assert.assertEquals("Combine", value.get("productType"));

		String companyName = test.getCompanyName("3584");
		Assert.assertEquals("WFG", companyName);
	}

	@Test
	public void teststoreSelectRecommnedationForCombineFalse() throws CouchbaseDaoServiceException, XmlRpcException,
			OpeneERPApiException, JSONException, ProposalException {
		UwAppAllAlgo algo = data.getDataFromCouchbase("3584");

		CombinedRecommendation rec = new CombinedRecommendation();
		rec.setBaseProductID("1889");

		try {
			operation.storeSelectRecommnedationForCombined(algo, rec, "127.0.0.1");

		} catch (ProposalException e) {
			Assert.fail("the this product is not present in ;couchbase for storing to select_Rec");
		}

	}

	@Ignore
	@Test
	public void testRecommendations() throws CouchbaseDaoServiceException {
		UwAppAllAlgo algo = data.getDataFromCouchbase("3584");

		Set<Recommendation> rec = algo.getRecommendations();

		Assert.assertNotNull(rec);
		Assert.assertFalse(rec.isEmpty());

	}

	@Ignore
	@Test
	public void testCombineTable() throws CouchbaseDaoServiceException {
		UwAppAllAlgo algo = data.getDataFromCouchbase("3584");

		Set<MarketingNotes> marketingNotes = algo.getMarketingNotes();
		

		Set<CombinedRecommendation> combine = algo.getCombinedRecommendation();
		Assert.assertFalse(combine.size() == 0);

	}

	@Ignore
	@Test
	public void updateProductTest() throws XmlRpcException, OpeneERPApiException {
		try {
			TestDevCRM.updateProduct("3584", "1889");
			Assert.fail("error in update the product ");
		} catch (XmlRpcException | OpeneERPApiException e) {
			logger.error("error update product" + e.getMessage());
		}
	}

	@Ignore
	@Test
	public void updateDealTest() throws XmlRpcException, OpeneERPApiException {

		try {
			TestDevCRM.updateDeal("Marketing", "field", "3584");
			Assert.fail("error in update the Deal ");
		} catch (XmlRpcException | OpeneERPApiException e) {
			logger.error("error update deattest" + e.getMessage());
		}
	}

	@Ignore
	@Test
	public void tesPostSelectionChange() throws Exception {

		try {
			TestDevCRM.chnageStageToPostSelction("3584");
			Assert.fail("error in chnageStageToPostSelction ");
		} catch (XmlRpcException | OpeneERPApiException e) {
			logger.error("error chnageStageToPostSelction" + e.getMessage());
		}

	}

	@Ignore
	@Test
	public void testgetcrmLeadFrompostgress() throws SQLException, IOException {
		StageLead lead = new StageLead();
		String leadvalue = lead.getcrmLeadFrompostgress("3584");

		Assert.assertNotNull(leadvalue);

	}

	@Ignore
	@Test
	public void testRestCallOnStageMail()
			throws SQLException, IOException, KeyManagementException, NoSuchAlgorithmException {
		StageLead lead = new StageLead();
		String leadvalue = lead.getcrmLeadFrompostgress("3584");

		RestCall restcall = new RestCall(leadvalue);
		try {
			restcall.restcallTostagMail(leadvalue);
			Assert.fail("the error while call RestCall");
		} catch (KeyManagementException | NoSuchAlgorithmException | IOException e) {
			logger.error("error while post to stage mail " + e.getMessage());
		}
	}

	@Ignore
	@Test
	public void testJsonConversion() throws JSONException, ProposalException, CouchbaseDaoServiceException {
		UwAppAllAlgo algo = data.getDataFromCouchbase("3584");

		Set<MarketingNotes> marketingNotes = algo.getMarketingNotes();
		Map<String, String> mapofNotesContents = MarketingNotesOperation.getNoteContentByNotes(marketingNotes);

		String originavalue = mapofNotesContents.get("OriginalDetails");
		OrignalDetails originaldetails = JsonConvertion.fromJsonforOriginalDetails(originavalue);
		logger.debug("" + originaldetails);
		originaldetails.setAmortization("30");
		originaldetails.setDownpaymentEquity("2222222");

		JsonConvertion.getJsonObject(originaldetails);

	}

}