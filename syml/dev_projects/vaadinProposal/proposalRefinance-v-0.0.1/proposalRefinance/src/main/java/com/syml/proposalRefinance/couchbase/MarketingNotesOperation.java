package com.syml.proposalRefinance.couchbase;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import org.apache.xmlrpc.XmlRpcException;
import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.debortoliwines.openerp.api.OpeneERPApiException;
import com.syml.proposalRefinance.couchbase.dao.CouchbaseDaoServiceException;
import com.syml.proposalRefinance.couchbase.dao.ProposalException;
import com.syml.proposalRefinance.openerp.HttpsConnectionCase;
import com.syml.proposalRefinance.openerp.TestDevCRM;
import com.syml.proposalRefinance.util.JsonConvertion;

public class MarketingNotesOperation {
	private static final Logger logger = LoggerFactory.getLogger(MarketingNotesOperation.class);
	// private static org.slf4j.Logger logger =
	// LoggerFactory.getLogger(MarketingNotesOperation.class);
	CouchbaseData data = new CouchbaseData();

	public static ArrayList<MaximumPurchase> getListofMax(Set<MarketingNotes> marketsnotes) {

		ArrayList<MaximumPurchase> listofmax = new ArrayList<MaximumPurchase>();

		for (MarketingNotes marketingNotes : marketsnotes) {
			if (marketingNotes.getNoteName().equalsIgnoreCase("MaxMortgageTable")) {
				logger.debug("MaxMortgageTable inside :");

				try {
					JSONObject js = JsonConvertion.getJsonObject(marketingNotes.getNoteContent().toString());

					MaximumPurchase ps = JsonConvertion
							.fromJsontoMaximumPurchase(js.get("variableMortgage(House)").toString());
					ps.setKey("variableMortgage(House)");
					listofmax.add(ps);

				} catch (Exception e) {
					logger.error("error on variableMortgage while getting from json : " + e.getMessage());
				}

				try {
					JSONObject js = JsonConvertion.getJsonObject(marketingNotes.getNoteContent().toString());
					MaximumPurchase ps = JsonConvertion
							.fromJsontoMaximumPurchase(js.get("fixedMortgageHouse").toString());
					ps.setKey("fixedMortgageHouse");
					listofmax.add(ps);

				} catch (Exception e1) {
					logger.error("error on variableMortgage while getting from json : " + e1.getMessage());
				}

				try {
					JSONObject js = JsonConvertion.getJsonObject(marketingNotes.getNoteContent().toString());

					MaximumPurchase ps = JsonConvertion
							.fromJsontoMaximumPurchase(js.get("VariableMortgageCondo").toString());
					ps.setKey("VariableMortgageCondo");
					listofmax.add(ps);

				} catch (Exception e1) {
					logger.error("error on variableMortgage while getting from json : " + e1.getMessage());
				}
				try {

					JSONObject js = JsonConvertion.getJsonObject(marketingNotes.getNoteContent().toString());

					MaximumPurchase ps = JsonConvertion
							.fromJsontoMaximumPurchase(js.get("FixedMortgageCondo").toString());
					ps.setKey("FixedMortgageCondo");
					listofmax.add(ps);

				} catch (Exception e1) {
					logger.error("error on variableMortgage while getting from json : " + e1.getMessage());
				}
			}

		}
		return listofmax;
	}

	public static ArrayList<MarketingNotes> getListofCombineTabe(Set<MarketingNotes> marketsnotes) {

		ArrayList<MarketingNotes> listofcombinedTable = new ArrayList<MarketingNotes>();

		for (MarketingNotes marketingNotes : marketsnotes) {
			if (marketingNotes.getNoteName().equalsIgnoreCase("CombinedTable")) {
				String marketing = marketingNotes.getNoteContent();
				listofcombinedTable.add(marketingNotes);

				logger.info("for " + marketing);

			}

		}
		return listofcombinedTable;
	}

	public static ArrayList<CombinedRecommendation> getlistofCombinedRecommendation(
			Set<CombinedRecommendation> combineRec) {

		ArrayList<CombinedRecommendation> listofcombinedRec = new ArrayList<CombinedRecommendation>();

		for (CombinedRecommendation combine : combineRec) {

			String ammor = combine.getAdditionalAmortization();
			Double doubletotal = Double.parseDouble(combine.getTotalPayment());

			logger.info("doubletotal : " + doubletotal);

			String com1 = String.format("%.2f", doubletotal);
			logger.info("com1 : " + com1);
			System.out.println(com1);
			combine.setTotalPayment(com1);

			listofcombinedRec.add(combine);

			// Logger.info("combine recommended : "+listofcombinedRec.size());

		}
		return listofcombinedRec;

	}

	public static Map<String, String> getNoteContentByNotes(Set<MarketingNotes> rec) {
		logger.debug("inside marketing notes  {} ", rec.size());
		Map<String, String> hmap = new HashMap<>();
		try {
			for (MarketingNotes market : rec) {

				if (market.getNoteName().equalsIgnoreCase("Instructions")) {

					hmap.put("Instructions", market.getNoteContent());

				} else if (market.getNoteName().equals("VariableRecommendations")) {

					hmap.put("VariableRecommendations", market.getNoteContent());
				} else if (market.getNoteName().equals("FixedRecommendations")) {

					hmap.put("FixedRecommendations", market.getNoteContent());
				} else if (market.getNoteName().equals("Goals")) {

					hmap.put("Goals", market.getNoteContent());
				} else if (market.getNoteName().equals("OriginalDesired")) {

					hmap.put("OriginalDesired", market.getNoteContent());
				} else if (market.getNoteName().equals("HelpingAchieve")) {

					hmap.put("HelpingAchieve", market.getNoteContent());
				} else if (market.getNoteName().equals("OriginalDetails")) {

					hmap.put("OriginalDetails", market.getNoteContent());
				} else if (market.getNoteName().equals("RecommendDetails")) {

					hmap.put("RecommendDetails", market.getNoteContent());
				} else if (market.getNoteName().equals("DebtRestructure")) {

					hmap.put("DebtRestructure", market.getNoteContent());
				} else if (market.getNoteName().equals("WhySense")) {

					hmap.put("WhySense", market.getNoteContent());
				} else if (market.getNoteName().equals("Highlights")) {

					hmap.put("Highlights", market.getNoteContent());
				} else if (market.getNoteName().equals("Options")) {

					hmap.put("Options", market.getNoteContent());
				} else if (market.getNoteName().equals("CombinedTable")) {

					hmap.put("CombinedTable", market.getNoteContent());
				}

				else if (market.getNoteName().equals("Notes")) {

					hmap.put("Notes", market.getNoteContent());
				} else if (market.getNoteName().equals("Comparision")) {

					hmap.put("Comparision", market.getNoteContent());
				}

				else if (market.getNoteName().equals("CompareDetails")) {

					hmap.put("CompareDetails", market.getNoteContent());
				}

			}
		} catch (NullPointerException e) {

			logger.error("MarketingNotes Content >>" + e.getMessage());
		}

		return hmap;

	}

	public void updateOriginalDetailsInOpenErp(UwAppAllAlgo algo) {
		Set<MarketingNotes> marketingsNotes = algo.getMarketingNotes();

		for (MarketingNotes marketingNotes : marketingsNotes) {
			if (marketingNotes.getNoteName().equalsIgnoreCase("OriginalDetails")) {
				try {

					HttpsConnectionCase.updateDeal(marketingNotes.getNoteName(), marketingNotes.getNoteContent(),
							String.valueOf(algo.getOpportunityID()));
				} catch (Exception e) {
					logger.error("OpenERP Connection  failed" + e.getMessage());
				}
				logger.debug("MarketingNotes" + marketingNotes.getNoteName());

				try {
					logger.debug("------------inside try block ------chnageStageToPostSelction------");

					HttpsConnectionCase.chnageStageToPostSelction(String.valueOf(algo.getOpportunityID()));

				} catch (XmlRpcException | OpeneERPApiException e) {
					logger.error("OpenERP Connection  failed" + e.getMessage());

				} catch (Exception e) {
					logger.error("error while changeto postSelection" + e.getMessage());
				}

			}
		}

	}

	public void updateRecomendationDetailsInOpenErp(UwAppAllAlgo algo) {
		Set<MarketingNotes> marketingsNotes = algo.getMarketingNotes();

		for (MarketingNotes marketingNotes : marketingsNotes) {
			if (marketingNotes.getNoteName().equalsIgnoreCase("RecommendDetails")) {
				try {

					TestDevCRM.updateDeal(marketingNotes.getNoteName(), marketingNotes.getNoteContent(),
							String.valueOf(algo.getOpportunityID()));
					HttpsConnectionCase.updateDeal(marketingNotes.getNoteName(), marketingNotes.getNoteContent(),
							String.valueOf(algo.getOpportunityID()));
				} catch (Exception e) {
					logger.error("OpenERP Connection  failed" + e.getMessage());
				}
				logger.debug("MarketingNotes" + marketingNotes.getNoteName());

				try {
					logger.debug("------------inside try block ------chnageStageToPostSelction------");
					// TestDevCRM.chnageStageToPostSelction(oppid);
					HttpsConnectionCase.chnageStageToPostSelction(String.valueOf(algo.getOpportunityID()));

				} catch (XmlRpcException | OpeneERPApiException e) {
					logger.error("OpenERP Connection  failed" + e.getMessage());

				} catch (Exception e) {
					logger.error("error while changeto postSelection" + e.getMessage());
				}

			}
		}

	}

	public void storeSelectOriginalDetails(UwAppAllAlgo algo, OrignalDetails ori, String ipaddress) throws ProposalException {
		logger.debug("inside the storeSelectOriginalDetails " + ori.getProductID());
		boolean checkId = false;
		Set<Recommendation> setRec = algo.getRecommendations();

		if (setRec == null) {
			throw new NullPointerException("Recommendation should not be null :" + setRec.size());
		}
		logger.info("the size of " + setRec.size());
		for (Recommendation recommendation : setRec) {

			if (recommendation.getProductID().equals(ori.getProductID())) {
				checkId = true;
				data.insertDataInCouchbase(recommendation, algo, ipaddress, ori.getPropertyValue(),
						ori.getDownpaymentEquity());
				break;
			}
			
		}
		if (checkId == false) {
			throw new ProposalException("The product id is not present");

		}
	}

	public void storeSelectRecommnedation(UwAppAllAlgo algo, RecommendDetails recommedetails, String ipaddress)
			throws ProposalException {
		boolean checkId = false;
		logger.debug("inside the storeSelectRecommnedation" + recommedetails.getProductID());
		Set<Recommendation> setRec = algo.getRecommendations();
		for (Recommendation recommendation : setRec) {
			if (recommendation.getProductID().equals(recommedetails.getProductID())) {
				checkId = true;
				data.insertDataInCouchbase(recommendation, algo, ipaddress, recommedetails.getPropertyValue(),
						recommedetails.getDownpaymentEquity());

			}
		}
		if (checkId == false) {
			throw new ProposalException("The product id is not present");

		}

	}

	public void storeSelectRecommnedationForSingle(UwAppAllAlgo algo, Recommendation rec, String ipaddress)
			throws ProposalException {
		boolean checkId = false;
		String proertyvalue = "";
		String downpayment = "";
		Set<Recommendation> setRec = algo.getRecommendations();

		Set<MarketingNotes> marketingsNotes = algo.getMarketingNotes();

		for (MarketingNotes marketingNotes : marketingsNotes) {
			if (marketingNotes.getNoteName().equalsIgnoreCase("RecommendDetails")) {
				logger.debug("MarketingNotes" + marketingNotes.getNoteName());

				try {
					RecommendDetails recdetials = JsonConvertion
							.fromJsonforRecommendDetails(marketingNotes.getNoteContent());

					proertyvalue = recdetials.getPropertyValue();
					downpayment = recdetials.getDownpaymentEquity();
					logger.info("proertyvalue{} " + proertyvalue);
					logger.info("downpayment{} " + downpayment);

				} catch (NullPointerException e) {
					logger.error("error while in marketings Nots" + e.getMessage());
				}
			}
		}
		for (Recommendation recommendation : setRec) {

			logger.debug("inside for loop {} " + recommendation.getProductID());

			if (recommendation.getProductID().equalsIgnoreCase(rec.getProductID())) {
				checkId = true;
				data.insertDataInCouchbase(recommendation, algo, ipaddress, proertyvalue, downpayment);
				break;

			}
			if (checkId == false) {
				throw new ProposalException("This product is not present iin couchbase");
			}

		}

	}

	public void storeSelectRecommnedationForCombined(UwAppAllAlgo algo, CombinedRecommendation rec, String ipaddress)
			throws ProposalException, CouchbaseDaoServiceException, JSONException {
		boolean checkId = false;

		Set<CombinedRecommendation> combineRec = algo.getCombinedRecommendation();
		for (CombinedRecommendation recommendation : combineRec) {

			logger.debug("inside for loop {} " + recommendation.getBaseProductID());

			if (recommendation.getBaseProductID().equalsIgnoreCase(rec.getBaseProductID())) {
				checkId = true;
				data.insertDataInCouchbase(recommendation, String.valueOf(algo.getOpportunityID()), ipaddress);
				break;

			}
			if (checkId == false) {
				throw new ProposalException("The product id is not present");

			}

		}
	}

}