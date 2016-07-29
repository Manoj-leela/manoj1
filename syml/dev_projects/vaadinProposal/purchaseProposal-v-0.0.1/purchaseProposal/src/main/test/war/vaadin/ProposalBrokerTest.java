package war.vaadin;

import java.util.Map;
import java.util.Set;

import org.codehaus.jettison.json.JSONException;
import org.junit.Assert;
import org.junit.Ignore;
import org.junit.Test;

import com.syml.purchaseProposal.couchbase.BrokerService;
import com.syml.purchaseProposal.couchbase.CombinedRecommendation;
import com.syml.purchaseProposal.couchbase.CouchbaseData;
import com.syml.purchaseProposal.couchbase.MarketingNotes;
import com.syml.purchaseProposal.couchbase.MarketingNotesOperation;
import com.syml.purchaseProposal.couchbase.OrignalDetails;
import com.syml.purchaseProposal.couchbase.RecommendDetails;
import com.syml.purchaseProposal.couchbase.Recommendation;
import com.syml.purchaseProposal.couchbase.UwAppAllAlgo;
import com.syml.purchaseProposal.couchbase.dao.CouchbaseDaoServiceException;
import com.syml.purchaseProposal.couchbase.dao.ProposalException;
import com.syml.purchaseProposal.util.JsonConvertion;

public class ProposalBrokerTest {

	CouchbaseData data = new CouchbaseData();

	BrokerService service = new BrokerService();
	Map<String, String> mapofNotesContents = null;

	@Ignore
	@Test
	public void saveOriginalDetailsTest() throws CouchbaseDaoServiceException, ProposalException, JSONException {
		UwAppAllAlgo algo = data.getDataFromCouchbase("3584");
		Assert.assertNotNull(algo);
		Set<MarketingNotes> notes = algo.getMarketingNotes();

		Assert.assertTrue(notes.size() > 0);
		mapofNotesContents = MarketingNotesOperation.getNoteContentByNotes(notes);
		OrignalDetails original = JsonConvertion.fromJsonforOriginalDetails(mapofNotesContents.get("OriginalDetails"));
		Assert.assertNotNull(original);
		original.setAmortization("90");
		service.saveOriginalDetails(original, algo);
		Assert.assertTrue(original.getAmortization().equals("90"));
	}

	@Ignore
	@Test
	public void saveRecommendDetailsForCombineTest()
			throws CouchbaseDaoServiceException, ProposalException, JSONException {
		UwAppAllAlgo algo = data.getDataFromCouchbase("3584");
		Assert.assertNotNull(algo);
		Set<MarketingNotes> notes = algo.getMarketingNotes();

		Assert.assertTrue(notes.size() > 0);
		mapofNotesContents = MarketingNotesOperation.getNoteContentByNotes(notes);
		RecommendDetails recommendDetails = JsonConvertion
				.fromJsonforRecommendDetails(mapofNotesContents.get("RecommendDetails"));
		Assert.assertNotNull(recommendDetails);

		recommendDetails.setAmortization("90");

		service.saveRecommendDetailsForCombine(recommendDetails, algo);

		Assert.assertTrue(recommendDetails.getAmortization().equals("90"));
	}

	@Ignore
	@Test
	public void saveRecommendDetailsForSingleTest()
			throws CouchbaseDaoServiceException, ProposalException, JSONException {
		UwAppAllAlgo algo = data.getDataFromCouchbase("3584");
		Assert.assertNotNull(algo);
		Set<MarketingNotes> notes = algo.getMarketingNotes();

		Assert.assertTrue(notes.size() > 0);
		mapofNotesContents = MarketingNotesOperation.getNoteContentByNotes(notes);
		RecommendDetails recommendDetails = JsonConvertion
				.fromJsonforRecommendDetails(mapofNotesContents.get("RecommendDetails"));
		Assert.assertNotNull(recommendDetails);

		recommendDetails.setAmortization("90");

		service.saveRecommendDetailsForCombine(recommendDetails, algo);

		Assert.assertTrue(recommendDetails.getAmortization().equals("90"));
	}

	@Ignore
	@Test
	public void deletebyidRecommendationTest() throws CouchbaseDaoServiceException, JSONException, ProposalException {
		UwAppAllAlgo algo = data.getDataFromCouchbase("3584");

		Set<Recommendation> rec = algo.getRecommendations();
		Assert.assertTrue(rec.size() > 0);
		service.deletebyidRecommendation("1957", algo);
		Set<Recommendation> rec1 = algo.getRecommendations();
		Assert.assertTrue(rec1.size() < rec.size());

	}

	@Ignore
	@Test
	public void deletebyidCombineRecommendationTest()
			throws CouchbaseDaoServiceException, JSONException, ProposalException {
		UwAppAllAlgo algo = data.getDataFromCouchbase("3584");
		boolean checkid = false;

		Set<CombinedRecommendation> rec = algo.getCombinedRecommendation();
		Assert.assertTrue(rec.size() > 0);
		service.deletebyidCombineRecommendation("1891", algo);
		for (CombinedRecommendation combine : rec) {
			if (combine.getBaseProductID().equals("1891"))
				;
			checkid = true;
		}

		Assert.assertTrue(checkid == false);
	}

	@Ignore
	@Test
	public void updateRecommendationTest() throws CouchbaseDaoServiceException, JSONException, ProposalException {
		UwAppAllAlgo algo = data.getDataFromCouchbase("3584");

		Recommendation recomdation = new Recommendation();
		recomdation.setProductID("1886");
		recomdation.setTerm("45");
		String termValue = "";

		service.updateRecommendation(recomdation, algo);

		Set<Recommendation> rec = algo.getRecommendations();
		for (Recommendation combine : rec) {
			if (combine.getProductID().equals("1886"))
				;
			termValue = combine.getTerm();
		}

		Assert.assertNotEquals(termValue, "45");

	}

	@Test
	public void updateCombinedRecommendationTest()
			throws CouchbaseDaoServiceException, JSONException, ProposalException {
		UwAppAllAlgo algo = data.getDataFromCouchbase("3584");

		CombinedRecommendation recomdation = new CombinedRecommendation();
		recomdation.setBaseProductID("1891");
		recomdation.setBaseTerm("45");
		String termValue = "";

		service.updateCombinedRecommendation(recomdation, algo);

		Set<CombinedRecommendation> rec = algo.getCombinedRecommendation();
		for (CombinedRecommendation combine : rec) {
			if (combine.getBaseProductID().equals("1891"))
				;
			termValue = combine.getBaseTerm();
		}

		Assert.assertNotEquals(termValue, "45");

	}

}
