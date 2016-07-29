package com.syml.purchaseProposal.vaadin;

import java.io.IOException;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.sql.SQLException;

import java.util.Set;

import org.apache.xmlrpc.XmlRpcException;
import org.codehaus.jettison.json.JSONException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.debortoliwines.openerp.api.OpeneERPApiException;
import com.syml.purchaseProposal.couchbase.CombinedRecommendation;
import com.syml.purchaseProposal.couchbase.MarketingNotesOperation;
import com.syml.purchaseProposal.couchbase.OrignalDetails;
import com.syml.purchaseProposal.couchbase.RecommendDetails;
import com.syml.purchaseProposal.couchbase.Recommendation;
import com.syml.purchaseProposal.couchbase.UwAppAllAlgo;
import com.syml.purchaseProposal.couchbase.dao.CouchbaseDaoServiceException;
import com.syml.purchaseProposal.couchbase.dao.ProposalException;
import com.syml.purchaseProposal.openerp.HttpsConnectionCase;
import com.syml.purchaseProposal.stagelead.RestCall;
import com.syml.purchaseProposal.stagelead.StageLead;
import com.vaadin.data.util.BeanItem;
import com.vaadin.data.util.BeanItemContainer;

import com.vaadin.shared.ui.grid.HeightMode;
import com.vaadin.ui.Form;
import com.vaadin.ui.Grid;
import com.vaadin.ui.Grid.HeaderCell;
import com.vaadin.ui.Grid.HeaderRow;

public class CleintService {
	private static final Logger logger = LoggerFactory.getLogger(CleintService.class);

	public void Originalsubmitcall(UwAppAllAlgo algo, String ipaddress, Form ff) throws ProposalException {

		MarketingNotesOperation marketingOperation = new MarketingNotesOperation();
		logger.debug("inside original Details");
		try {
			BeanItem<OrignalDetails> recBean = (BeanItem<OrignalDetails>) ff.getItemDataSource();
			logger.debug("OriginaltDetails {} " + recBean.getBean());
			OrignalDetails ori = recBean.getBean();

			updateProduct(String.valueOf(algo.getOpportunityID()), ori.getProductID());

			marketingOperation.updateOriginalDetailsInOpenErp(algo);
			marketingOperation.storeSelectOriginalDetails(algo, ori, ipaddress);

		} catch (NullPointerException e) {
			logger.error("error while submitting the originalDetails {}", e.getMessage());
			throw new ProposalException("Error in Original Submit Call", e);
		}

	}

	public void RecommendationDetailSubmitCall(UwAppAllAlgo algo, String ipaddress, Form ff) throws ProposalException {
		MarketingNotesOperation marketingOperation = new MarketingNotesOperation();
		try {
			logger.debug("inside RecommendDetailas Details");

			BeanItem<RecommendDetails> recBean = (BeanItem<RecommendDetails>) ff.getItemDataSource();

			RecommendDetails recommedetails = recBean.getBean();
			updateProduct(String.valueOf(algo.getOpportunityID()), recommedetails.getProductID());

			marketingOperation.updateRecomendationDetailsInOpenErp(algo);
			marketingOperation.storeSelectRecommnedation(algo, recommedetails, ipaddress);

		} catch (NullPointerException e) {
			logger.error("error while storing in couchbase:" + e.getMessage());
			throw new ProposalException("error in Recommendation Submit Call", e);
		}

	}

	public void RecommendationSingleSubmitCall(UwAppAllAlgo algo, String ipaddress, Form ff) throws ProposalException {
		MarketingNotesOperation marketingOperation = new MarketingNotesOperation();
		try {

			logger.debug("coming the recommendattion {} ");

			BeanItem<Recommendation> recBean = (BeanItem<Recommendation>) ff.getItemDataSource();
			Recommendation rec = recBean.getBean();

			logger.debug("while on submit get theproduct " + rec.getProductID());
			updateProduct(String.valueOf(algo.getOpportunityID()), rec.getProductID());
			marketingOperation.updateRecomendationDetailsInOpenErp(algo);
			marketingOperation.storeSelectRecommnedationForSingle(algo, rec, ipaddress);
		} catch (ProposalException e) {
			logger.error("RecommendationSingleSubmitCall in coucbase " + e.getMessage());
			throw new ProposalException("Recommendation Single Submit Call  in Client", e);

		}
	}

	public void RecommendationCombinedSubmitCall(UwAppAllAlgo algo, String ipaddress, Form ff)
			throws ProposalException {
		try {
			MarketingNotesOperation marketingOperation = new MarketingNotesOperation();
			logger.debug("coming the CombinedValue as true {} ");

			BeanItem<CombinedRecommendation> recBean = (BeanItem<CombinedRecommendation>) ff.getItemDataSource();
			CombinedRecommendation rec = recBean.getBean();

			logger.debug("while on submit get theproduct " + rec.getBaseProductID());
			updateProduct(String.valueOf(algo.getOpportunityID()), rec.getBaseProductID());

			marketingOperation.updateRecomendationDetailsInOpenErp(algo);
			marketingOperation.storeSelectRecommnedationForCombined(algo, rec, ipaddress);
		} catch (CouchbaseDaoServiceException | JSONException e) {
			logger.error("RecommendationCombinedSubmitCall error occured " + e.getMessage());
			throw new ProposalException("Recommendation Combined Submit Call in Client", e);
		}
	}

	public void callStageLead(String oppId) throws ProposalException {
		StageLead lead = new StageLead();
		String crmdata = "";
		try {
			crmdata = lead.getcrmLeadFrompostgress(oppId);
			RestCallForStageLead(crmdata);
		} catch (SQLException | IOException e) {
			logger.error("sql exception " + e.getMessage());
			throw new ProposalException("error in callStage Lead", e);

		}
	}

	private void RestCallForStageLead(String crmdata) throws ProposalException {
		logger.debug("inside restcall method()");
		RestCall restcall = null;
		restcall = new RestCall(crmdata);
		try {
			restcall.restcallTostagMail(crmdata);
		} catch (KeyManagementException | NoSuchAlgorithmException | IOException e) {
			logger.error("error restcall to stageMail" + e.getMessage());
			throw new ProposalException("For stage Lead Rest Call error Occur", e);
		}
	}

	private void updateProduct(String oppid, String productid) throws ProposalException {
		try {
			// TestDevCRM.updateProduct(oppid, productid);

			HttpsConnectionCase.updateProduct(oppid, productid);

		} catch (XmlRpcException | OpeneERPApiException e) {
			logger.error("error in  Updating  Product" + e.getMessage());
			throw new ProposalException("update product error", e);
		}

	}

	public BeanItemContainer<Recommendation> getVaribleBean(Set<Recommendation> rec) {
		logger.debug("inside getVaribleBean method ()>>>>>>>>>>>>>>>>>>>>>>>>>>...." + rec.size());
		BeanItemContainer<Recommendation> stars = null;
		try {
			stars = new BeanItemContainer<Recommendation>(Recommendation.class);
			for (Recommendation variable : rec) {
				if (variable.getMortgageType().equals("2")) {
					variable.setMaximumAmortization(ConvertIntToSTring(variable.getMaximumAmortization()));

					stars.addBean(variable);
				}
			}
		} catch (NullPointerException e) {
			logger.error("The variable Recommendation should not be null  " + e.getMessage());
		}

		return stars;
	}

	public BeanItemContainer<Recommendation> getFixedBean(Set<Recommendation> rec) {
		logger.debug("inside getFixedBean method ()>>>>>>>>>>>>>>>>>>>>>>>>>>...." + rec.size());
		BeanItemContainer<Recommendation> stars = null;
		try {
			stars = new BeanItemContainer<Recommendation>(Recommendation.class);
			for (Recommendation variable : rec) {
				if (variable.getMortgageType().equals("3")) {
					variable.setMaximumAmortization(ConvertIntToSTring(variable.getMaximumAmortization()));

					stars.addBean(variable);
				}
			}
		} catch (NullPointerException e) {
			logger.error("The variable Recommendation should not be null  " + e.getMessage());
		}

		return stars;
	}

	public String ConvertIntToSTring(String value) throws NumberFormatException {
		int intValue = 0;
		intValue = (int) Double.parseDouble(value);

		return String.valueOf(intValue);
	}

	public BeanItemContainer<CombinedRecommendation> getCombinedBean(Set<CombinedRecommendation> rec) {
		logger.debug("inside getCombinedBean method ()>>>>>>>>>>>>>>>>>>>>>>>>>>...." + rec.size());
		BeanItemContainer<CombinedRecommendation> stars = null;
		try {
			stars = new BeanItemContainer<CombinedRecommendation>(CombinedRecommendation.class);
			for (CombinedRecommendation variable : rec) {

				stars.addBean(variable);
			}

		} catch (NullPointerException e) {
			logger.error("The variable Recommendation should not be null  " + e.getMessage());
		}

		return stars;
	}

	public void setGridConfig(Grid table, BeanItemContainer<Recommendation> recommendationBean) {
		table.setContainerDataSource(recommendationBean);
		table.setWidth("100%");
		table.setHeightMode(HeightMode.ROW);
		table.setHeightByRows(getGridSize(table));
	}

	public void setCombineGridConfig(Grid table, BeanItemContainer<CombinedRecommendation> recommendationBean) {
		table.setContainerDataSource(recommendationBean);
		table.setWidth("100%");
		table.setHeightMode(HeightMode.ROW);
		table.setHeightByRows(getGridSize(table));
	}

	private double getGridSize(Grid tableVariable) {
		double defaultRowsCount = 3.0d;
		int size = tableVariable.getContainerDataSource().size();
		double rows = (size > 0) ? size : defaultRowsCount;
		if (rows == 2.0d) {
			rows = 3.0d;
		}
		return rows;
	}

	public Grid getGridColumnConfig(Grid table) {
		logger.debug("inside the Column Config:");
		table.setColumns("lender", "product", "term", "maximumAmortization", "payment", "interestRate"
		/* "initialforApproval" */);

		table.getColumn("maximumAmortization").setHeaderCaption("Amortization");
		table.getColumn("product").setHeaderCaption("Product Name");
		return table;
	}

	public Grid getCombineGridColumnConfig(Grid tableCombined) {

		logger.debug("inside Combine Column Config ");
		tableCombined.setColumns("baseLender", "baseProduct", "additionalProduct", "baseTerm", "additionalTerm",
				"baseAmortization", "additionalAmortization", "baseMortgageAmount", "additionalMortgageAmount",
				"basePayment", "additionalPayment", "baseInterestRate", "additionalInterestRate", "totalPayment");

		HeaderRow extraHeader = tableCombined.prependHeaderRow();
		HeaderCell productjoin = extraHeader.join("baseProduct", "additionalProduct");
		productjoin.setText("Product");
		tableCombined.getColumn("baseLender").setHeaderCaption("Lender");
		HeaderCell termjoined = extraHeader.join("baseTerm", "additionalTerm");
		termjoined.setText("Term");

		HeaderCell amortizationjoined = extraHeader.join("baseAmortization", "additionalAmortization");
		amortizationjoined.setText("Amortization");

		HeaderCell mortageamountjoined = extraHeader.join("baseMortgageAmount", "additionalMortgageAmount");
		mortageamountjoined.setText("Amount");

		HeaderCell paymentjoin = extraHeader.join("basePayment", "additionalPayment");
		paymentjoin.setText("MonthlyPayment");

		HeaderCell interestJoin = extraHeader.join("baseInterestRate", "additionalInterestRate");
		interestJoin.setText("Interest Rate");

		tableCombined.getColumn("totalPayment").setHeaderCaption("Total Mortgage");
		return tableCombined;
	}

}
