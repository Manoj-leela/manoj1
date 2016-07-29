package com.syml.purchaseProposal.couchbase;

import java.util.Iterator;
import java.util.Set;

import org.codehaus.jettison.json.JSONException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.syml.purchaseProposal.couchbase.dao.CouchbaseDaoServiceException;
import com.syml.purchaseProposal.couchbase.dao.ProposalException;
import com.syml.purchaseProposal.util.JsonConvertion;
import com.syml.purchaseProposal.vaadin.OriginalDetailForm;
import com.syml.purchaseProposal.vaadin.RecommendationDetailForSingleForm;
import com.syml.purchaseProposal.vaadin.RecommendationDetailForm;
import com.vaadin.data.Container;
import com.vaadin.data.Item;
import com.vaadin.data.fieldgroup.FieldGroup.CommitEvent;
import com.vaadin.data.fieldgroup.FieldGroup.CommitException;
import com.vaadin.data.fieldgroup.FieldGroup.CommitHandler;
import com.vaadin.data.util.BeanItemContainer;
import com.vaadin.data.util.GeneratedPropertyContainer;
import com.vaadin.data.util.PropertyValueGenerator;
import com.vaadin.data.validator.RegexpValidator;
import com.vaadin.shared.ui.grid.HeightMode;
import com.vaadin.ui.Grid;
import com.vaadin.ui.Grid.HeaderCell;
import com.vaadin.ui.Grid.HeaderRow;
import com.vaadin.ui.renderers.ButtonRenderer;

public class BrokerService {
	CouchbaseData data = new CouchbaseData();
	private static final Logger logger = LoggerFactory.getLogger(BrokerService.class);

	public void saveOriginalDetails(OrignalDetails details, UwAppAllAlgo algo) throws JSONException, ProposalException {
		logger.debug("<<<<<<<<<<<<<<<<<<inside save OriginalDetails>>>>>>>>>>>>>>>");
		Set<MarketingNotes> notes = algo.getMarketingNotes();

		for (MarketingNotes marketingNotes : notes) {
			if (marketingNotes.getNoteName().equals("OriginalDetails")) {
				logger.debug("marketing notecontent befre:" + marketingNotes.getNoteContent());
				try {
					marketingNotes.setNoteContent(JsonConvertion.getJsonObject(details));

				} catch (JSONException e) {
					logger.error("json Exception while mapping" + e.getMessage());
				}
				logger.debug("marketing noteconent after:" + marketingNotes.getNoteContent());
			}
		}

		data.updateUwappCouchbase(algo);

	}

	public void saveRecommendDetailsForCombine(RecommendDetails recDetails, UwAppAllAlgo algo)
			throws JSONException, ProposalException {

		logger.debug("<<<<<<<<<<<<<<<<<<inside save RecommendDetails >Combine>>>>>>>>>>>>>>");
		Set<MarketingNotes> notes = algo.getMarketingNotes();

		for (MarketingNotes marketingNotes : notes) {
			if (marketingNotes.getNoteName().equals("RecommendDetails")) {
				logger.debug("marketing notecontent befre:" + marketingNotes.getNoteContent());
				try {
					marketingNotes.setNoteContent(JsonConvertion.getJsonObject(recDetails));

				} catch (JSONException e) {
					logger.error("json Exception while mapping" + e.getMessage());
				}
				logger.debug("marketing noteconent after:" + marketingNotes.getNoteContent());
			}
		}

		data.updateUwappCouchbase(algo);

	}

	public void setOriginalDetailsStyleNamewithBorder(OriginalDetailForm originalStyleform, String stylename) {
		logger.debug("inside OriginalDetailForm style name change" + stylename);
		originalStyleform.amortization.setStyleName(stylename);
		originalStyleform.downpaymentEquity.setStyleName(stylename);
		originalStyleform.insuranceAmount.setStyleName(stylename);
		originalStyleform.interestRate.setStyleName(stylename);
		originalStyleform.lender.setStyleName(stylename);
		originalStyleform.mortgageAmount.setStyleName(stylename);
		originalStyleform.mortgageTerm.setStyleName(stylename);
		originalStyleform.mortgageType.setStyleName(stylename);
		originalStyleform.paymentAmount.setStyleName(stylename);
		originalStyleform.propertyValue.setStyleName(stylename);
		originalStyleform.totalMortgage.setStyleName(stylename);

	}

	public void setRecommendationDetailsStyleNamewithBorder(RecommendationDetailForm recommendationform,
			String stylename) {

		logger.debug("inside RecommendationDetailForm style name change" + stylename);

		recommendationform.propertyValue.setStyleName(stylename);
		recommendationform.downpaymentEquity.setStyleName(stylename);
		recommendationform.mortgageAmount.setStyleName(stylename);
		recommendationform.mortgageAmount1.setStyleName(stylename);
		recommendationform.amortization.setStyleName(stylename);
		recommendationform.amortization1.setStyleName(stylename);
		recommendationform.mortgageType.setStyleName(stylename);
		recommendationform.mortgageType1.setStyleName(stylename);
		recommendationform.mortgageTerm.setStyleName(stylename);
		recommendationform.mortgageTerm1.setStyleName(stylename);
		recommendationform.paymentAmount.setStyleName(stylename);
		recommendationform.paymentAmount1.setStyleName(stylename);
		recommendationform.interestRate.setStyleName(stylename);
		recommendationform.interestRate1.setStyleName(stylename);
		recommendationform.lender.setStyleName(stylename);
		recommendationform.totalPayment.setStyleName(stylename);
		recommendationform.totalMortgageAmount.setStyleName(stylename);
	}

	public void setRecommendationDetailsStyleNamewithBorderSingle(RecommendationDetailForSingleForm single,
			String stylename) {
		logger.debug("inside RecommendationDetailForm style name change for Single" + stylename);
		single.propertyValue.setStyleName(stylename);
		single.downpaymentEquity.setStyleName(stylename);
		single.mortgageAmount.setStyleName(stylename);
		single.amortization.setStyleName(stylename);
		single.mortgageType.setStyleName(stylename);
		single.mortgageTerm.setStyleName(stylename);
		single.paymentAmount.setStyleName(stylename);
		single.interestRate.setStyleName(stylename);
		single.lender.setStyleName(stylename);
		single.totalMortgage.setStyleName(stylename);

	}

	public void saveRecommendDetailsForSingle(RecommendDetails recDetails, UwAppAllAlgo algo)
			throws JSONException, ProposalException {

		logger.debug("<<<<<<<<<<<<<<<<<<inside save RecommendDetails Single>>>>>>>>>>>>>>>");
		Set<MarketingNotes> notes = algo.getMarketingNotes();

		for (MarketingNotes marketingNotes : notes) {
			if (marketingNotes.getNoteName().equals("RecommendDetails")) {
				logger.debug("marketing notecontent befre:" + marketingNotes.getNoteContent());

				marketingNotes.setNoteContent(JsonConvertion.getJsonForRecommendDetailsSingle(recDetails).toString());

				logger.debug("marketing noteconent after:" + marketingNotes.getNoteContent());
			}
		}

		data.updateUwappCouchbase(algo);

	}

	public void deletebyidRecommendation(String id, UwAppAllAlgo algo) throws ProposalException, JSONException {
		logger.debug("inside the Recommendaton Delete  by Id " + id);
		Set<Recommendation> recomendtionList = algo.getRecommendations();
		boolean productIdExist = false;
		logger.info("size : " + recomendtionList.size());
		for (Iterator<Recommendation> iterator = recomendtionList.iterator(); iterator.hasNext();) {
			Recommendation recommendation = (Recommendation) iterator.next();

			if (recommendation.getProductID().equals(id)) {
				productIdExist = true;
				logger.info("after" + recommendation.getProductID());
				algo.getRecommendations().remove(recommendation);
				break;
			}
		}
		if (productIdExist == false) {
			throw new ProposalException("The given id is  not present  in couchbase");

		}
		data.updateUwappCouchbase(algo);
	}

	public void updateRecommendation(Recommendation rec, UwAppAllAlgo algo) throws JSONException, ProposalException {

		logger.debug("++ inside updateRecommendation()++ "+rec.getProductID());

		try {

			Set<Recommendation> recomendtionList = algo.getRecommendations();

			for (Iterator<Recommendation> iterator = recomendtionList.iterator(); iterator.hasNext();)

			{

				Recommendation recommendation = (Recommendation) iterator.next();
				if (recommendation.getProductID().equals(rec.getProductID())) {
					algo.getRecommendations().remove(recommendation);
					break;
				}

			}

		} catch (NullPointerException e) {
			logger.error("error", e);
		}

		algo.getRecommendations().add(rec);

		data.updateUwappCouchbase(algo);

	}

	public void deletebyidCombineRecommendation(String id, UwAppAllAlgo algo)
			throws JSONException, CouchbaseDaoServiceException, ProposalException {

		logger.debug("++ inside Combine Recommendation deleteProductById()++" + id);
		boolean productIdExist = false;

		Set<CombinedRecommendation> recomendtionList = algo.getCombinedRecommendation();

		for (Iterator<CombinedRecommendation> iterator = recomendtionList.iterator(); iterator.hasNext();)

		{

			CombinedRecommendation recommendation = (CombinedRecommendation) iterator.next();
			if (recommendation.getBaseProductID().equals(id)) {
				productIdExist = true;
				algo.getCombinedRecommendation().remove(recommendation);
				break;
			}

		}
		

		if(productIdExist==false){
			throw new ProposalException("The given Combine Recommendation product id is  not present  in couchbase");
		}

		data.updateUwappCouchbase(algo);

	}

	public void updateCombinedRecommendation(CombinedRecommendation rec, UwAppAllAlgo algo)
			throws JSONException, ProposalException {

		logger.debug("++ inside updateCombinedRecommendation()++");

		try {

			Set<CombinedRecommendation> recomendtionList = algo.getCombinedRecommendation();

			for (Iterator<CombinedRecommendation> iterator = recomendtionList.iterator(); iterator.hasNext();)

			{

				CombinedRecommendation recommendation = (CombinedRecommendation) iterator.next();
				if (recommendation.getBaseProductID().equals(rec.getBaseProductID())) {
					algo.getCombinedRecommendation().remove(recommendation);
					break;
				}

			}

		} catch (NullPointerException e) {
			logger.error("error in null CombinedRecommendation", e);
		}

		data.updateUwappCouchbase(algo);

	}

	public String ConvertIntToSTring(String value) throws NumberFormatException {
		int intValue = 0;
		intValue = (int) Double.parseDouble(value);

		return String.valueOf(intValue);
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
	


	public BeanItemContainer<CombinedRecommendation> getCombinedBean(Set<CombinedRecommendation> rec) {
		logger.debug("inside getCombinedBean method ()>>>>>>>>>>>>>>>>>>>>>>>>>>...." + rec.size());
		BeanItemContainer<CombinedRecommendation> combined = null;
		try {
			combined = new BeanItemContainer<CombinedRecommendation>(CombinedRecommendation.class);
			for (CombinedRecommendation variable : rec) {

				combined.addBean(variable);
			}

		} catch (NullPointerException e) {
			logger.error("The variable Recommendation should not be null  " + e.getMessage());
		}

		return combined;
	}

	public GeneratedPropertyContainer getButton(BeanItemContainer<Recommendation> recommedationContainer) {
		GeneratedPropertyContainer gpc = new GeneratedPropertyContainer(recommedationContainer);
		gpc.addGeneratedProperty("delete", new PropertyValueGenerator<String>() {

			@Override
			public String getValue(Item item, Object itemId, Object propertyId) {
				Recommendation rec = ((Recommendation) itemId);
				//// String rec2 = rec.getProductID();
				return "Delete";
			}

			@Override
			public Class<String> getType() {
				return String.class;
			}
		});
		gpc.addGeneratedProperty("update", new PropertyValueGenerator<String>() {

			@Override
			public String getValue(Item item, Object itemId, Object propertyId) {
				return "Update"; // The caption
			}

			@Override
			public Class<String> getType() {
				return String.class;
			}
		});

		return gpc;
	}

	public void setGridConfig(Grid table) {

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

	public void removeRecommendationOnClickDelete(Container.Indexed indexed, Grid table, UwAppAllAlgo algo) {
		table.getColumn("delete").setRenderer(new ButtonRenderer(event -> {

			Object itemId = event.getItemId();
			Recommendation rec12 = (Recommendation) itemId;

			try {
				deletebyidRecommendation(rec12.getProductID(), algo);
			} catch (Exception e) {

			}

			indexed.removeItem(itemId);

			table.getContainerDataSource().getItem(event.getItemId());
		}));
	}

	public GeneratedPropertyContainer getButtonForCombine(
			BeanItemContainer<CombinedRecommendation> recommedationContainer) {
		GeneratedPropertyContainer gpc = new GeneratedPropertyContainer(recommedationContainer);
		gpc.addGeneratedProperty("delete", new PropertyValueGenerator<String>() {

			@Override
			public String getValue(Item item, Object itemId, Object propertyId) {
				CombinedRecommendation rec = ((CombinedRecommendation) itemId);
				//// String rec2 = rec.getProductID();
				return "Delete";
			}

			@Override
			public Class<String> getType() {
				return String.class;
			}
		});
		gpc.addGeneratedProperty("update", new PropertyValueGenerator<String>() {

			@Override
			public String getValue(Item item, Object itemId, Object propertyId) {
				return "Update"; // The caption
			}

			@Override
			public Class<String> getType() {
				return String.class;
			}
		});

		return gpc;
	}

	public void updateRecommendationOnClickUpdate(Grid table, UwAppAllAlgo algo) {
		table.getColumn("update").setRenderer(new ButtonRenderer(event -> {

		}));
		table.getEditorFieldGroup().addCommitHandler(new CommitHandler() {
			@Override
			public void preCommit(CommitEvent commitEvent) throws CommitException {
				table.getEditedItemId();

			}

			@Override
			public void postCommit(CommitEvent commitEvent) throws CommitException {
				Recommendation rec = (Recommendation) table.getEditedItemId();

				try {
					updateRecommendation(rec, algo);
				} catch (JSONException | ProposalException e) {
					logger.error("update in Recommendation Error: " + e.getMessage());

				}
			}
		});
	}

	public void setGridColumnConfigForCombine(Grid table) {
		table.setColumns("baseLender", "baseProduct", "additionalProduct", "baseTerm", "additionalTerm",
				"baseAmortization", "additionalAmortization", "baseMortgageAmount", "additionalMortgageAmount",
				"basePayment", "additionalPayment", "baseInterestRate", "additionalInterestRate", "totalPayment",
				"delete", "update");

		table.getColumn("baseAmortization").getEditorField()
				.addValidator(new RegexpValidator("^[0-9]+$", "Base Amortization Should be in Decimal Number"));
		table.getColumn("additionalAmortization").getEditorField()
				.addValidator(new RegexpValidator("^[0-9]+$", "Additional Amortization  Should be Decimal Number"));
		table.getColumn("baseMortgageAmount").getEditorField().addValidator(
				new RegexpValidator("[0-9]+(\\.[0-9]{1,2})$", "Base Mortage Amount Should be Decimal Number"));
		table.getColumn("additionalMortgageAmount").getEditorField().addValidator(
				new RegexpValidator("[0-9]+(\\.[0-9]{1,2})$", "Additional Mortage Amount Should be Decimal Number"));
		table.getColumn("basePayment").getEditorField()
				.addValidator(new RegexpValidator("[0-9]+(\\.[0-9]{1,2})$", "Base Payment Should be Decimal Number"));
		table.getColumn("additionalPayment").getEditorField().addValidator(
				new RegexpValidator("[0-9]+(\\.[0-9]{1,2})$", "Additional Payment Should be Decimal Number"));

		table.getColumn("baseInterestRate").getEditorField().addValidator(
				new RegexpValidator("[0-9]+(\\.[0-9]{1,2})$", "Base Interest Rate Should be Decimal Number"));
		table.getColumn("additionalInterestRate").getEditorField().addValidator(
				new RegexpValidator("[0-9]+(\\.[0-9]{1,2})$", "Additional Interest Rate Should be Decimal Number"));
		table.getColumn("totalPayment").getEditorField()
				.addValidator(new RegexpValidator("[0-9]+(\\.[0-9]{1,2})$", "Total Payment Should be Decimal Number"));
		setColumnNameForCombine(table);
	}

	private void setColumnNameForCombine(Grid table) {
		HeaderRow extraHeader = table.prependHeaderRow();
		HeaderCell productjoin = extraHeader.join("baseProduct", "additionalProduct");
		productjoin.setText("Product");
		table.getColumn("baseLender").setHeaderCaption("Lender");
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
		table.getColumn("totalPayment").setHeaderCaption("Total Mortgage");
	}

	public void setGridColumnConfig(Grid table) {
		table.setImmediate(true);
		table.setColumns("lender", "product", "term", "maximumAmortization", "payment", "interestRate",
				/* "initialforApproval", */ "delete", "update");
		table.getColumn("maximumAmortization").setHeaderCaption("Amortization");
		table.getColumn("product").setHeaderCaption("Product Name");
		table.getColumn("term").getEditorField().addValidator(new RegexpValidator("^[0-9]+$", "Term Should be Number"));
		table.getColumn("maximumAmortization").getEditorField()
				.addValidator(new RegexpValidator("^[0-9]+$", "Amortization  should be in Number"));
		table.getColumn("payment").getEditorField()
				.addValidator(new RegexpValidator("[0-9]+(\\.[0-9]{1,2})$", "Payment should be in Decimal Number"));
		table.getColumn("interestRate").getEditorField()
				.addValidator(new RegexpValidator("[0-9]+(\\.[0-9]{1,2})$", "Interest Rate in Decimal Number"));

		// table.getColumn("initialforApproval").setHeaderCaption("Initial");

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

	public void removeCombineRecommendationOnClickDelete(Container.Indexed indexed, Grid table, UwAppAllAlgo algo) {
		table.getColumn("delete").setRenderer(new ButtonRenderer(event -> {
			Object itemId = event.getItemId();
			CombinedRecommendation rec12 = (CombinedRecommendation) itemId;
			logger.debug(">>>>>>>>>>>>>>>>>>>>>>>>>>" + rec12.getBaseProductID());

			try {
				deletebyidCombineRecommendation(rec12.getBaseProductID(), algo);
			} catch (Exception e) {
				// throw new ProposalException();
			}
			indexed.removeItem(itemId);
			table.getContainerDataSource().getItem(event.getItemId());
		}));
	}
}
