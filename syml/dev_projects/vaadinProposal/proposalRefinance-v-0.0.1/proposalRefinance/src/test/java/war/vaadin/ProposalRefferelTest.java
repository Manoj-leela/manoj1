package war.vaadin;

import java.util.Map;
import java.util.Set;

import org.apache.xmlrpc.XmlRpcException;
import org.junit.Assert;
import org.junit.Ignore;
import org.junit.Test;
import org.slf4j.LoggerFactory;

import com.debortoliwines.openerp.api.OpeneERPApiException;
import com.syml.proposalRefinance.couchbase.CouchbaseData;
import com.syml.proposalRefinance.couchbase.MarketingNotes;
import com.syml.proposalRefinance.couchbase.MarketingNotesOperation;
import com.syml.proposalRefinance.couchbase.OrignalDetails;
import com.syml.proposalRefinance.couchbase.RecommendDetails;
import com.syml.proposalRefinance.couchbase.UwAppAllAlgo;
import com.syml.proposalRefinance.couchbase.dao.CouchbaseDaoServiceException;
import com.syml.proposalRefinance.couchbase.dao.ProposalException;
import com.syml.proposalRefinance.couchbase.dao.service.CouchBaseService;
import com.syml.proposalRefinance.openerp.TestDevCRM;
import com.syml.proposalRefinance.util.JsonConvertion;

public class ProposalRefferelTest {

	private static org.slf4j.Logger logger = LoggerFactory.getLogger(ProposalRefferelTest.class);

	CouchBaseService service = new CouchBaseService();
	CouchbaseData data = new CouchbaseData();
	TestDevCRM test = new TestDevCRM();

	@Ignore
	@Test
	public void testgetFormLayoutforOriginalPositive() throws ProposalException, CouchbaseDaoServiceException {
		UwAppAllAlgo algo = data.getDataFromCouchbase("3584");
		Set<MarketingNotes> marketingNotes = algo.getMarketingNotes();
		Map<String, String> mapofNotesContents = MarketingNotesOperation.getNoteContentByNotes(marketingNotes);

		Assert.assertNotNull("The marketing not should not be null" + marketingNotes);

		Assert.assertTrue("should not be ", marketingNotes.size() > 0);

		Assert.assertTrue(mapofNotesContents.containsKey("OriginalDetails"));

		String originavalue = mapofNotesContents.get("OriginalDetails");
		OrignalDetails originaldetaials = JsonConvertion.fromJsonforOriginalDetails(originavalue);

		Assert.assertNotNull("The null value should not come:", originaldetaials);
		Assert.assertEquals("1889", originaldetaials.getProductID());

	}

	@Ignore
	@Test

	public void testgetFormLayoutforOriginalNegative() throws ProposalException, CouchbaseDaoServiceException {
		UwAppAllAlgo algo = data.getDataFromCouchbase("3584");
		Set<MarketingNotes> marketingNotes = algo.getMarketingNotes();
		Assert.assertNull(marketingNotes);

		Map<String, String> mapofNotesContents = MarketingNotesOperation.getNoteContentByNotes(marketingNotes);

		Assert.assertNotNull("The marketing not should not be null" + marketingNotes);
		Assert.assertTrue("should not be ", marketingNotes.size() > 0);

		Assert.assertFalse(mapofNotesContents.containsKey("originalDetailsss"));

		String originavalue = mapofNotesContents.get("OriginalDetailss");
		Assert.assertNull(originavalue);
	}

	@Ignore
	@Test
	public void testAllLabelDisplay() throws CouchbaseDaoServiceException {
		UwAppAllAlgo algo = data.getDataFromCouchbase("3584");
		Set<MarketingNotes> marketingNotes = algo.getMarketingNotes();
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
	public void testAllLabelDisplayFalse() throws CouchbaseDaoServiceException {
		UwAppAllAlgo algo = data.getDataFromCouchbase("3584");
		Set<MarketingNotes> marketingNotes = algo.getMarketingNotes();
		Map<String, String> mapofNotesContents = MarketingNotesOperation.getNoteContentByNotes(marketingNotes);

		Assert.assertFalse(mapofNotesContents.size() == 0);

		Assert.assertFalse(mapofNotesContents.containsKey("Instructionss"));
		Assert.assertFalse(mapofNotesContents.containsKey("OriginalDesireds"));
		Assert.assertFalse(mapofNotesContents.containsKey("HelpingAchievee"));
		Assert.assertFalse(mapofNotesContents.containsKey("WhySenses"));
		Assert.assertFalse(mapofNotesContents.containsKey("DebtRestructuree"));
		Assert.assertFalse(mapofNotesContents.containsKey("Highlightss"));
		Assert.assertFalse(mapofNotesContents.containsKey("Optionss"));
		Assert.assertFalse(mapofNotesContents.containsKey("CombinedTablee"));
		Assert.assertFalse(mapofNotesContents.containsKey("Notess"));
		Assert.assertFalse(mapofNotesContents.containsKey("FixedRecommendationsa"));

	}

	@Ignore
	@Test
	public void testCompanyNameifexists()
			throws XmlRpcException, OpeneERPApiException, ProposalException, CouchbaseDaoServiceException {
		/**
		 * this class for use locally only for developmnet
		 * HttpsConnectionCase.getCompanyName(oppertunutyId);
		 */
		UwAppAllAlgo algo = data.getDataFromCouchbase("3584");
		Set<MarketingNotes> marketingNotes = algo.getMarketingNotes();
		Map<String, String> mapofNotesContents = MarketingNotesOperation.getNoteContentByNotes(marketingNotes);

		String companyName = test.getCompanyName("3584");
		Assert.assertNotNull(companyName);
		Assert.assertEquals("WFG", companyName);

	}

	/*
	 * @Test public void
	 */
	@Ignore
	@Test
	public void testCompanyNameifNotexists()
			throws XmlRpcException, OpeneERPApiException, ProposalException, CouchbaseDaoServiceException {

		TestDevCRM test = new TestDevCRM();
		// for develpemnet
		// HttpsConnectionCase.getCompanyName(oppertunutyId);
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
}
