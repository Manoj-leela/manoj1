package com.syml.mortgageapplication.impl;

import java.util.ArrayList;
import java.util.List;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import com.syml.couchbasehelper.CouchbaseServiceHelper;
import com.syml.hibernate.dao.PostGressDaoServiceException;

import controllers.Assetes;
import controllers.Opportunity;

public class MortgageApplication10PageServiceTest {

	CouchbaseServiceHelper couchbaseServiceHelper = new CouchbaseServiceHelper();
	MortgageApplicationPageTenService mTenService = new MortgageApplicationPageTenService();
	Opportunity opportunity = null;

	@Before
	public void loadingOpportunityData()
			throws MortgageApplicationPageServiceException {
		if (opportunity == null) {
			opportunity = couchbaseServiceHelper.getCouhbaseDataByKey(4591);
		}

	}
	
	@Test
	public void testListingofAssetsIfAssetNotSelected()
			throws PostGressDaoServiceException {

		String[] asset = new String[] {};
		Opportunity opportun = null;
		opportunity.getApplicants().get(0).setAssetList(new  ArrayList<Assetes>());
		opportun = mTenService.listAssets(opportunity, asset, null, null, null,
				null, null);
		List<Assetes> lsiAssetes = opportun.getApplicants().get(0)
				.getAssetList();

		Assert.assertNotNull("Assets list object should not be null ",
				lsiAssetes);
		Assert.assertTrue("Asset list should be empty  ",
				lsiAssetes.size() == 0);
	}

	@Test
	public void testListingOfAssets() throws PostGressDaoServiceException {
		Opportunity opportun = null;
		String[] assetsName = { "BankAccount", "Vehical", "Investment" };

		String[] descriptionName = { "Bank Account Description ",
				"Vehical Description ", "Investment Description " };

		String[] value = { "2443.0", "53523.0", "2352.0" };

		String[] desgnation = { "Joint", "Joint", "Joint" };
		String type = "RRSPs";

		String assetType = "RRSPTSFA";
		mTenService.deleteAssest(opportunity);
		opportun = mTenService.listAssets(opportunity, assetsName,
				descriptionName, value, desgnation, type, assetType);

		Assert.assertNotNull("opportunity object shuold not be null ", opportun);

		List<Assetes> listOfAssets = opportun.getApplicants().get(0)
				.getAssetList();

		Assert.assertNotNull("assets object should not be null ", listOfAssets);
		Assert.assertTrue("List of assets size should be Three ",
				listOfAssets.size() >= 3);

		for (int i = 0; i < 3; i++) {

			Assetes assetes = listOfAssets.get(i);
			Assert.assertEquals("Assettype   should be=" + type, type,
					assetes.getType());
			Assert.assertEquals("AssetDescription  should be = "
					+ descriptionName[i], descriptionName[i],
					assetes.getDescription());
			Assert.assertEquals("AssetValue  should  be =" + value[i],
					value[i], assetes.getValue().toString());
			Assert.assertEquals("AssertOwnerShip should be=" + desgnation[i],
					desgnation[i], assetes.getOwnerShip());

		}

	}

	@Test
	public void toTestStoredAssets()
			throws MortgageApplicationPageServiceException,
			PostGressDaoServiceException {
		Opportunity opportunityy = null;
		String[] assetsName = { "BankAccount", "Vehical", "Investment" };

		String[] descriptionName = { "Bank Account Description ",
				"Vehical Description ", "Investment Description " };

		String[] value = { "2443.0", "53523.0", "2352.0" };

		String[] desgnation = { "Joint", "Joint", "Joint" };
		String type = "RRSPs";

		String assetType = "RRSPTSFA";
		mTenService.deleteAssest(opportunity);

		mTenService.listAssets(opportunity, assetsName, descriptionName, value,
				desgnation, type, assetType);
		mTenService.createAssests(opportunity);
		opportunityy = couchbaseServiceHelper.getCouhbaseDataByKey(4591);
		Assert.assertNotNull("Opportunity Object should not be Null ",
				opportunityy);
		List<Assetes> assets = opportunityy.getApplicants().get(0)
				.getAssetList();

		Assert.assertNotNull("List of assets should not be null  ", assets);

		Assert.assertTrue("List of assets size should be Three ",
				assets.size() >= 3);

		for (int i = 0; i < 3; i++) {

			Assetes assetes = assets.get(i);
			Assert.assertEquals("Assettype   should be=" + type, type,
					assetes.getType());
			Assert.assertEquals("AssetDescription  should be = "
					+ descriptionName[i], descriptionName[i],
					assetes.getDescription());
			Assert.assertEquals("AssetValue  should  be =" + value[i],
					value[i], assetes.getValue().toString());
			Assert.assertEquals("AssertOwnerShip should be=" + desgnation[i],
					desgnation[i], assetes.getOwnerShip());

		}

	}

}
