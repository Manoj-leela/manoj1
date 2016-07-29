package com.syml.purchaseProposal.vaadin;

import java.util.Map;
import java.util.Set;

import org.apache.xmlrpc.XmlRpcException;
import org.slf4j.LoggerFactory;

import com.debortoliwines.openerp.api.OpeneERPApiException;
import com.syml.purchaseProposal.couchbase.Comparison;
import com.syml.purchaseProposal.couchbase.CouchbaseData;
import com.syml.purchaseProposal.couchbase.MarketingNotes;
import com.syml.purchaseProposal.couchbase.MarketingNotesOperation;
import com.syml.purchaseProposal.couchbase.UwAppAllAlgo;
import com.syml.purchaseProposal.couchbase.dao.CouchbaseDaoServiceException;
import com.syml.purchaseProposal.couchbase.dao.ProposalException;
import com.syml.purchaseProposal.openerp.HttpsConnectionCase;
import com.syml.purchaseProposal.openerp.TestDevCRM;
import com.syml.purchaseProposal.util.JsonConvertion;
import com.vaadin.ui.CssLayout;

@SuppressWarnings("serial")
public class ProposalReferal extends CssLayout {
	private static org.slf4j.Logger logger = LoggerFactory.getLogger(ProposalReferal.class);

	Map<String, String> mapofNotesContents = null;

	ReferralService referralService = null;
	VaadinFormUtil formUtil = null;

	public ProposalReferal(String opp) throws CouchbaseDaoServiceException, ProposalException {

		setSizeUndefined();

		CouchbaseData data = new CouchbaseData();
		UwAppAllAlgo algo = null;
		referralService = new ReferralService();
		formUtil = new VaadinFormUtil();
		try {
			algo = data.getDataFromCouchbase(opp);
		} catch (NullPointerException e) {
			logger.debug("not be null  in  UwAppAlgo class " + e.getMessage());
		}
		Set<MarketingNotes> marketingNotes = algo.getMarketingNotes();
		mapofNotesContents = MarketingNotesOperation.getNoteContentByNotes(marketingNotes);

		setSizeFull();
		setWidth("85%");

		setStyleName("csslayoutformargin");

		settopbarLayout();

		SetApplicantNames(algo);

		setLable(algo);

		getFormLayoutforOriginal(opp);
		if(mapofNotesContents.containsKey("HelpingAchieve")){
			setForHelpingLabel();	
		}
		
		labelforOurRecommendation();
		if (getCompanyName(opp).equals("WFG")) {
			getFormLayoutforRecommendationcombine(opp);
		} else {
			getFormLayoutforRecommendationSingle(opp);
		}

		if (mapofNotesContents.containsKey("DebtRestructure")) {
			setLableForDebt();
		}
		if (mapofNotesContents.containsKey("Options")) {
			setOptiontLabel();
		}

		if (algo.getRecommendations().stream().anyMatch(rec -> rec.getMortgageType().equals("2"))) {
			setLableforVaible();
			settableVarible(algo);

		}

		if (algo.getRecommendations().stream().anyMatch(rec -> rec.getMortgageType().equals("3"))) {
			setLableforFixed();
			settableFixed(algo);

		}

		if (algo.getCombinedRecommendation() != null) {
			setLableforCombinedTable();
			settableForCombined(algo);
		}

		setLabelforNotes();

	}

	public void SetApplicantNames(UwAppAllAlgo algoNames) {
		addComponent(formUtil.getApplicantNames(algoNames));
	}

	public void labelforOurRecommendation() {
		addComponent(formUtil.getLabelForOurRecommendation());

	}

	public void settopbarLayout() {
		addComponent(formUtil.getTopBarDisplay());
	}

	public void setLableforFixed() {
		addComponent(formUtil.getLabelForFixed(mapofNotesContents.get("FixedRecommendations")));
	}

	public void setLableforCombinedTable() {
		addComponent(formUtil.getLableforCombinedTable(mapofNotesContents.get("CombinedTable")));
	}

	public void settableForCombined(UwAppAllAlgo algo) throws CouchbaseDaoServiceException {
		addComponent(referralService.getTableForCombine(algo.getCombinedRecommendation()));
	}

	public void setLable(UwAppAllAlgo algo) throws ProposalException {
		try {
			addComponent(formUtil.getLabelForInstructionAndOriginal(algo));
		} catch (CouchbaseDaoServiceException e1) {
			logger.error("error occur In Instruction label value ");
			throw new ProposalException("The Instruction should no be null", e1);
		}
	}

	public void setLableforVaible() {
		addComponent(formUtil.getLabelForVariable(mapofNotesContents.get("VariableRecommendations")));
	}

	public void getFormLayoutforOriginal(String opportunity) throws ProposalException {
		addComponent(referralService.getOriginalFormLayout(mapofNotesContents.get("OriginalDetails")));
	}

	public void getFormLayoutforRecommendationcombine(String opportunity) throws ProposalException {
		addComponent(referralService.getCombineFormLayout(mapofNotesContents.get("RecommendDetails")));
	}

	public void getFormLayoutforRecommendationSingle(String opportunity) throws ProposalException {
		addComponent(referralService.getSingleFormLayout(mapofNotesContents.get("RecommendDetails")));
	}

	public void settableVarible(UwAppAllAlgo algo) {
		addComponent(referralService.getTableForVarible(algo.getRecommendations()));
	}

	public void settableFixed(UwAppAllAlgo algo) throws CouchbaseDaoServiceException {
		referralService.getTableForFixed(algo.getRecommendations());
	}

	public void setForHelpingLabel() {
		addComponent(formUtil.getLabelForHelping(mapofNotesContents.get("HelpingAchieve")));
	}

	/*
	 * public void setLableforMakeSense() {
	 * addComponent(formUtil.getLabelForMakeSense(mapofNotesContents.get(
	 * "WhySense").split("\n")));
	 * 
	 * }
	 */
	public void setLableForDebt() {
		addComponent(formUtil.getLabelForDebt(mapofNotesContents.get("DebtRestructure").split("\n")));
	}

	/*
	 * public void labelforComparison() {
	 * addComponent(formUtil.getLabelForComparison()); }
	 */

	/*
	 * public void setHighLightLabel() {
	 * addComponent(formUtil.getLabelForHighLight(mapofNotesContents.get(
	 * "Highlights").split("\n"))); }
	 */

	public void setOptiontLabel() {
		addComponent(formUtil.getLabelForOption(mapofNotesContents.get("Options")));

	}

	public void setLabelforNotes() throws ProposalException {
		addComponent(formUtil.getLabelForNotes(mapofNotesContents.get("Notes").split("\n")));

	}

	private String getCompanyName(String oppid) throws ProposalException {
		TestDevCRM test = new TestDevCRM();

		String companyName = null;

		try {
			companyName = HttpsConnectionCase.getCompanyName(oppid);
		} catch (XmlRpcException | OpeneERPApiException e) {

			logger.error("xml Rpc Exception (){} : " + e.getMessage());
			throw new ProposalException("get Company error in Referral", e);

		}

		/*
		 * try { companyName = test.getCompanyName(oppid);
		 * 
		 * } catch (XmlRpcException e1) {
		 * 
		 * logger.error("xml Rpc Exception (){} : " + e1.getMessage()); } catch
		 * (OpeneERPApiException e1) { logger.error(
		 * "openerp error thrown here {} : " + e1.getMessage()); }
		 */
		return companyName;

	}

}
