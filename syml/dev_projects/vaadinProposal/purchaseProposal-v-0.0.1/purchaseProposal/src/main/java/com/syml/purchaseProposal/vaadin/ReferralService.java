package com.syml.purchaseProposal.vaadin;

import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.syml.purchaseProposal.couchbase.CombinedRecommendation;
import com.syml.purchaseProposal.couchbase.OrignalDetails;
import com.syml.purchaseProposal.couchbase.RecommendDetails;
import com.syml.purchaseProposal.couchbase.Recommendation;
import com.syml.purchaseProposal.couchbase.dao.ProposalException;
import com.syml.purchaseProposal.util.JsonConvertion;
import com.vaadin.data.fieldgroup.FieldGroup;
import com.vaadin.data.util.BeanItem;
import com.vaadin.data.util.BeanItemContainer;
import com.vaadin.shared.ui.grid.HeightMode;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Grid;
import com.vaadin.ui.Grid.HeaderCell;
import com.vaadin.ui.Grid.HeaderRow;
import com.vaadin.ui.VerticalLayout;

public class ReferralService {
	private static final Logger logger = LoggerFactory.getLogger(ReferralService.class);

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

	private String ConvertIntToSTring(String value) throws NumberFormatException {
		int intValue = 0;
		intValue = (int) Double.parseDouble(value);

		return String.valueOf(intValue);
	}

	public void setGridConfig(Grid table, BeanItemContainer<Recommendation> recommendationBean) {
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
		table.setImmediate(true);
		logger.debug("inside the Column Config:");
		table.setColumns("lender", "product", "term", "maximumAmortization", "payment", "interestRate"
		/* "initialforApproval" */);

		table.getColumn("maximumAmortization").setHeaderCaption("Amortization");
		table.getColumn("product").setHeaderCaption("Product Name");
		return table;
	}

	public void setCombineGridConfig(Grid table, BeanItemContainer<CombinedRecommendation> recommendationBean) {
		table.setContainerDataSource(recommendationBean);
		table.setWidth("100%");
		table.setHeightMode(HeightMode.ROW);
		table.setHeightByRows(getGridSize(table));
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

	public VerticalLayout getOriginalFormLayout(String originalValue) {
		VerticalLayout verticallayut = new VerticalLayout();
		verticallayut.setMargin(true);
		OriginalDetailForm form = new OriginalDetailForm();
		form.setMargin(true);
		form.setSpacing(true);
		form.setStyleName("loppp");
		OrignalDetails original = new OrignalDetails();

		try {
			original = JsonConvertion.fromJsonforOriginalDetails(originalValue);
		} catch (ProposalException e) {
			logger.error("The original Details should not be null", e);
		}
		FieldGroup binder = new FieldGroup();
		BeanItem<OrignalDetails> item = new BeanItem<OrignalDetails>(original);
		binder.setItemDataSource(item);
		binder.bindMemberFields(form);

		verticallayut.addComponent(form);
		verticallayut.setComponentAlignment(form, Alignment.MIDDLE_CENTER);

		return verticallayut;
	}

	public VerticalLayout getCombineFormLayout(String combineValue) {
		VerticalLayout verticallayut = new VerticalLayout();
		RecommendationDetailForm recommendationform = new RecommendationDetailForm();
		verticallayut.setMargin(true);
		recommendationform.setMargin(true);
		recommendationform.setSpacing(true);
		recommendationform.setStyleName("loppp");
		RecommendDetails recdetails = new RecommendDetails();
		try {
			recdetails = JsonConvertion.fromJsonforRecommendDetails(combineValue);
		} catch (ProposalException e) {
			logger.error("error combine Recommendaton should not be null", e);
			// throw new ProposalException();
		}
		FieldGroup binder = new FieldGroup();
		binder.setReadOnly(true);
		BeanItem<RecommendDetails> item = new BeanItem<RecommendDetails>(recdetails);
		binder.setItemDataSource(item);
		binder.bindMemberFields(recommendationform);
		verticallayut.addComponent(recommendationform);
		verticallayut.setComponentAlignment(recommendationform, Alignment.MIDDLE_CENTER);
		return verticallayut;
	}

	public VerticalLayout getSingleFormLayout(String singleValue) {
		RecommendationDetailForSingleForm singleRecommendationFform = new RecommendationDetailForSingleForm();

		VerticalLayout verticallayut = new VerticalLayout();
		verticallayut.setMargin(true);

		singleRecommendationFform.setMargin(true);
		singleRecommendationFform.setSpacing(true);
		singleRecommendationFform.setStyleName("loppp");

		RecommendDetails recdetails = new RecommendDetails();
		try {
			recdetails = JsonConvertion.fromJsonforRecommendDetails(singleValue);
		} catch (ProposalException e) {
			logger.error("Single Recommenation should not be null", e);
		}

		FieldGroup binder = new FieldGroup();
		binder.setReadOnly(true);
		BeanItem<RecommendDetails> item = new BeanItem<RecommendDetails>(recdetails);
		binder.setItemDataSource(item);
		binder.bindMemberFields(singleRecommendationFform);

		verticallayut.addComponent(singleRecommendationFform);
		verticallayut.setComponentAlignment(singleRecommendationFform, Alignment.MIDDLE_CENTER);
		return verticallayut;

	}

	VerticalLayout getTableForVarible(Set<Recommendation> rec) {
		VerticalLayout lay = new VerticalLayout();
		lay.setMargin(true);
		lay.setHeight("50%");
		lay.setWidth("100%");
		BeanItemContainer<Recommendation> beanVariable = getVaribleBean(rec);
		Grid table = new Grid("", beanVariable);
		setGridConfig(table, beanVariable);
		getGridColumnConfig(table);
		lay.addComponent(table);
		lay.setComponentAlignment(table, Alignment.MIDDLE_CENTER);
		return lay;
	}

	VerticalLayout getTableForFixed(Set<Recommendation> rec) {
		VerticalLayout lay = new VerticalLayout();
		lay.setMargin(true);

		BeanItemContainer<Recommendation> beanFixed = getFixedBean(rec);
		Grid table = new Grid("", beanFixed);
		setGridConfig(table, beanFixed);
		getGridColumnConfig(table);

		lay.addComponent(table);

		return lay;

	}

	VerticalLayout getTableForCombine(Set<CombinedRecommendation> rec) {
		logger.debug("inside combine table");

		VerticalLayout lay = new VerticalLayout();
		lay.setMargin(true);
		lay.setSizeFull();
		BeanItemContainer<CombinedRecommendation> combineBean = getCombinedBean(rec);

		Grid table = new Grid("", combineBean);
		setCombineGridConfig(table, combineBean);

		table.setEditorEnabled(false);
		getCombineGridColumnConfig(table);

		lay.addComponent(table);
		lay.setComponentAlignment(table, Alignment.MIDDLE_CENTER);
		return lay;
	}
}
