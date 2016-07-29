package com.biz.rest;




import play.Logger;

import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.WebResource;


	public class RestCall extends Thread{
		private static org.slf4j.Logger logger = Logger.underlying();
		
		String documentList;
		
		public RestCall(String documentList) {
			
			this.documentList = documentList;
		}

		
		@Override
		public void run() {
			// TODO Auto-generated method stub
			restcallTostagMail(documentList);
		}
		
		public  void restcallTostagMail(String documentList) {
			// TODO Auto-generated method stub
			logger.info("lead {} : "+documentList);

			try {

				Client client = Client.create();

				WebResource webResource = client
				   .resource("https://stage.visdom.ca/lead");


				ClientResponse response = webResource.type("application/json")
				   .post(ClientResponse.class, documentList);

				if (response.getStatus() != 200) {
					throw new RuntimeException("Failed : HTTP error code : "
					     + response.getStatus());
				}

				System.out.println("Output from Server .... \n");
				String output = response.getEntity(String.class);
				System.out.println(output);

			  } catch (Exception e) {

				e.printStackTrace();

			  }
			
		}


		

	
		
			   public static void main(String[] args){

String myvar = "{"+
"  \"id\": 439,"+
"  \"create_uid\": 1,"+
"  \"create_date\": \"2015-08-05 19:58:42.073921\","+
"  \"write_date\": \"2016-03-10 10:42:30.914316\","+
"  \"write_uid\": 1,"+
"  \"date_closed\": null,"+
"  \"type_id\": null,"+
"  \"color\": 1,"+
"  \"date_action_last\": null,"+
"  \"day_close\": 0,"+
"  \"active\": true,"+
"  \"street\": \"\","+
"  \"day_open\": 0,"+
"  \"contact_name\": null,"+
"  \"partner_id\": 735,"+
"  \"date_open\": null,"+
"  \"user_id\": 1,"+
"  \"opt_out\": false,"+
"  \"title\": null,"+
"  \"partner_name\": null,"+
"  \"planned_revenue\": null,"+
"  \"country_id\": null,"+
"  \"company_id\": 1,"+
"  \"priority\": \"3\","+
"  \"state\": \"open\","+
"  \"email_cc\": null,"+
"  \"date_action_next\": null,"+
"  \"type\": \"opportunity\","+
"  \"street2\": null,"+
"  \"function\": null,"+
"  \"fax\": null,"+
"  \"description\": null,"+
"  \"planned_cost\": null,"+
"  \"ref2\": null,"+
"  \"section_id\": null,"+
"  \"title_action\": \"1\","+
"  \"phone\": \"test\","+
"  \"probability\": null,"+
"  \"payment_mode\": null,"+
"  \"date_action\": null,"+
"  \"name\": \"Brad  Pitt - Pur - 246 Snowberry Dr. SW, Calgary, First, 2015-08\","+
"  \"stage_id\": 23,"+
"  \"zip\": null,"+
"  \"date_deadline\": null,"+
"  \"mobile\": \"039-589-6976\","+
"  \"ref\": null,"+
"  \"channel_id\": null,"+
"  \"state_id\": null,"+
"  \"email_from\": \"guy@visdom.ca\","+
"  \"referred\": null,"+
"  \"all_email_ids\": null,"+
"  \"marketing_auto\": null,"+
"  \"opp_info_loan_amnt\": null,"+
"  \"referral_source\": null,"+
"  \"is_commercial\": false,"+
"  \"considered_cites\": null,"+
"  \"deadline\": \"2015-08-08 19:58:42\","+
"  \"selected_product\": 1914,"+
"  \"secondary_financing_amount\": 18000,"+
"  \"is_modular_homes\": false,"+
"  \"cash_back\": 0,"+
"  \"is_grow_ops\": false,"+
"  \"client_email_rem\": false,"+
"  \"template_date\": \"2016-02-21 11:54:28\","+
"  \"final_documents\": null,"+
"  \"Amortization\": null,"+
"  \"internal_notes_final\": null,"+
"  \"other_amount\": 0,"+
"  \"assistant\": null,"+
"  \"marketing_comp_amount\": 0,"+
"  \"application_date\": \"2015-08-05 20:02:23\","+
"  \"bank_account\": 10000,"+
"  \"is_leased_land\": false,"+
"  \"is_mobile_homes\": false,"+
"  \"is_four_plex\": false,"+
"  \"looking_to\": null,"+
"  \"property_less_then_5_years\": \"1\","+
"  \"current_mortgage_type\": null,"+
"  \"is_agricultural_less_10_acres\": false,"+
"  \"sale_of_existing_amount\": 15000,"+
"  \"sewage\": \"1\","+
"  \"has_charges_behind\": false,"+
"  \"downpayment_amount\": 0,"+
"  \"qualified_check\": false,"+
"  \"internal_notes\": null,"+
"  \"renter_pay_heating\": null,"+
"  \"rate\": 2.24,"+
"  \"lawyer\": null,"+
"  \"is_construction_mortgage\": false,"+
"  \"delayed_app_task\": false,"+
"  \"opp_info_renewal_date\": \"2015-08-05\","+
"  \"borrowed_amount\": 14000,"+
"  \"purchase_price\": 800000,"+
"  \"is_cottage_rec_property\": false,"+
"  \"future_family\": \"1\","+
"  \"referred_source\": null,"+
"  \"water\": \"1\","+
"  \"desired_mortgage_type\": \"1\","+
"  \"credit_story\": null,"+
"  \"address\": \"246 Snowberry Dr. SW\","+
"  \"isUpdatedToUA\": false,"+
"  \"volume_bonus_amount\": 68.61,"+
"  \"opportunity_id\": null,"+
"  \"is_duplex\": false,"+
"  \"future_mortgage\": null,"+
"  \"property_style\": \"1\","+
"  \"estimated_valueof_home\": null,"+
"  \"heating\": \"1\","+
"  \"garage_size\": \"3\","+
"  \"insurerref\": null,"+
"  \"plus_minus_prime\": 0,"+
"  \"lender_requires_insurance\": false,"+
"  \"maximum_amount\": false,"+
"  \"morweb_filogix\": null,"+
"  \"sweat_equity_amount\": 17000,"+
"  \"current_interest_rate\": null,"+
"  \"prod_count\": 269,"+
"  \"existing_lender\": null,"+
"  \"square_footage\": 3000,"+
"  \"date_created_co_applicant\": null,"+
"  \"trainee\": null,"+
"  \"lead_followed\": false,"+
"  \"trailer_comp_amount\": 0,"+
"  \"desired_term\": \"7\","+
"  \"trigger\": null,"+
"  \"opp_info_rate\": null,"+
"  \"condition_of_financing_date\": null,"+
"  \"is_boarding_houses\": false,"+
"  \"underwriter\": null,"+
"  \"is_life_leased_property\": false,"+
"  \"date_renewed\": true,"+
"  \"approached_check\": false,"+
"  \"total_one_time_fees\": 0,"+
"  \"mls\": \"123456\","+
"  \"financial_risk_taker\": \"3\","+
"  \"TDS\": null,"+
"  \"is_fractional_interests\": false,"+
"  \"is_rental_pools\": false,"+
"  \"renovation_value\": null,"+
"  \"desired_mortgage_amount\": 674000,"+
"  \"property_taxes\": 5000,"+
"  \"expected_closing_date\": \"2016-03-23 00:00:00\","+
"  \"client_remd\": false,"+
"  \"charge_on_title\": \"1\","+
"  \"renewal_mail_date\": null,"+
"  \"term_rate\": null,"+
"  \"total_mortgage_amount\": 686132,"+
"  \"propsal_date\": \"2016-03-13 10:42:32\","+
"  \"desired_product_type\": null,"+
"  \"property_value\": 800000,"+
"  \"amortization\": 25,"+
"  \"non_income_qualifer\": false,"+
"  \"renewal_email_send\": false,"+
"  \"is_eight_plex\": false,"+
"  \"charges_behind_amount\": null,"+
"  \"personal_cash_amount\": 11000,"+
"  \"applicant_last_name\": null,"+
"  \"block\": \"3\","+
"  \"preferred_number\": \"cell\","+
"  \"is_raw_land\": false,"+
"  \"job_5_years\": \"3\","+
"  \"from_web_form\": null,"+
"  \"commitment_fee\": 0,"+
"  \"lot_size\": null,"+
"  \"opp_info_type\": null,"+
"  \"property_type\": \"1\","+
"  \"current_lender\": null,"+
"  \"lifestyle_change\": \"1\","+
"  \"is_country_residential\": false,"+
"  \"acres\": 0.3,"+
"  \"lender\": null,"+
"  \"training_associate_referral\": null,"+
"  \"monthly_rental_income\": 0,"+
"  \"process_presntedutio_check\": false,"+
"  \"private_fee\": null,"+
"  \"monthly_payment\": 2985.51,"+
"  \"total_comp_amount\": 3499.27,"+
"  \"condo_fees\": null,"+
"  \"open_closed\": \"closed\","+
"  \"lender_ref\": null,"+
"  \"op_info_type\": null,"+
"  \"mortgage_insured\": false,"+
"  \"term\": \"3\","+
"  \"closing_date\": \"2016-03-23\","+
"  \"insurerfee\": 12132,"+
"  \"lender_fee\": 0,"+
"  \"draws_required\": null,"+
"  \"apartment_style\": null,"+
"  \"is_agricultural\": false,"+
"  \"sales_associate\": null,"+
"  \"pending_application_check\": false,"+
"  \"gifted_amount\": 13000,"+
"  \"lender_name\": \"HomeTrust\","+
"  \"dup_task_created\": false,"+
"  \"is_condo\": false,"+
"  \"new_opp_users\": null,"+
"  \"work_phone\": null,"+
"  \"current_mortgage_amount\": null,"+
"  \"internal_note_property\": null,"+
"  \"down_payment_coming_from\": \"1\","+
"  \"completed_ref\": false,"+
"  \"is_uninsured_conv_2nd_home\": false,"+
"  \"client_survey\": null,"+
"  \"renewal_reminder\": \"2017-07-23 00:00:00\","+
"  \"existing_payout_penalty\": null,"+
"  \"application_no\": \"439\","+
"  \"is_a_small_centre\": false,"+
"  \"internal_note\": null,"+
"  \"ltv\": 84.25,"+
"  \"outbuildings_value\": null,"+
"  \"is_military\": false,"+
"  \"desired_amortization\": 25,"+
"  \"is_rental_property\": false,"+
"  \"welcum_email_date\": null,"+
"  \"congrats_date\": \"2016-03-26 00:00:00\","+
"  \"needs_power_attorney\": false,"+
"  \"mortgage_type\": \"3\","+
"  \"is_co_operative_housing\": false,"+
"  \"garage_type\": \"1\","+
"  \"rrsp_amount\": 12000,"+
"  \"current_mortgage_product\": null,"+
"  \"document_fields\": null,"+
"  \"concerns_addressed_check\": false,"+
"  \"city\": \"Calgary\","+
"  \"to_pages\": null,"+
"  \"is_non_conv_construction\": false,"+
"  \"is_high_ratio_2nd_home\": false,"+
"  \"lead_followup_date\": \"2015-09-09 18:40:30.990213\","+
"  \"lot\": \"4\","+
"  \"greeting_send\": false,"+
"  \"broker_fee\": 0,"+
"  \"realtor\": null,"+
"  \"referral_fee\": 700,"+
"  \"fax1\": null,"+
"  \"insurer\": null,"+
"  \"opp_info_start_date\": \"2015-08-01\","+
"  \"email_work\": null,"+
"  \"delayed_app_date\": null,"+
"  \"what_is_your_lending_goal\": \"2\","+
"  \"base_commission\": null,"+
"  \"down_payment_amount\": 126000,"+
"  \"distance_to_major_center\": null,"+
"  \"GDS\": null,"+
"  \"looking_fora\": \"2\","+
"  \"postal_code\": \"T3H2R5\","+
"  \"Township_PID\": \"5\","+
"  \"frequency\": null,"+
"  \"webform_uname\": null,"+
"  \"current_balance\": null,"+
"  \"is_floating_homes\": false,"+
"  \"verify_product\": false,"+
"  \"buy_new_vehicle\": \"5\","+
"  \"source_of_borrowing\": \"2\","+
"  \"living_in_property\": \"1\","+
"  \"is_age_restricted\": false,"+
"  \"from_pages\": null,"+
"  \"income_decreased_worried\": \"1\","+
"  \"is_six_plex\": false,"+
"  \"is_rooming_houses\": false,"+
"  \"volume_commission\": null,"+
"  \"existing_mortgage\": null,"+
"  \"current_renewal_date\": null,"+
"  \"current_monthly_payment\": null,"+
"  \"province\": \"AB\","+
"  \"web_response\": null,"+
"  \"building_funds\": null,"+
"  \"min_heat_fee\": null,"+
"  \"plan\": \"2\","+
"  \"final_lender\": 11,"+
"  \"spouse\": null,"+
"  \"lender_response\": null,"+
"  \"product_type\": null,"+
"  \"new_home_warranty\": null,"+
"  \"hr_department_id\": 1,"+
"  \"age\": 4,"+
"  \"webform_pwd\": null,"+
"  \"lead_source\": null,"+
"  \"existing_equity_amount\": 16000,"+
"  \"renewaldate\": \"2018-03-23\","+
"  \"base_comp_amount\": 3430.66,"+
"  \"percent_variable\": 0,"+
"  \"posted_rate\": 0,"+
"  \"Type\": \"Opportunity\","+
"  \"Type_Opportunity\": \"Opportunity\","+
"  \"LeadURl\": \"439\","+
"  \"LeadFullName\": \"Brad  Pitt - Pur - 246 Snowberry Dr. SW, Calgary, First, 2015-08\","+
"  \"stage_Name\": \"Commitment\","+
"  \"Type_convertedToOpportunity\": \"convertedToOpportunity\","+
"  \"ReferralName\": \"\","+
"  \"lastStageDateTime\": \"16/03/10 02:43:48\","+
"  \"Lending_Goal\": \"Purchase\","+
"  \"stageDuration\": 0,"+
"  \"Submission_Date_Time1b\": \"2016/03/10 02:43:48\","+
"  \"x_company\": null,"+
"  \"x_base_ltv\": null,"+
"  \"x_selected_recommendation\": null,"+
"  \"opportunity_street\": null,"+
"  \"lender_legal_fee\": null,"+
"  \"disbursements\": null,"+
"  \"x_bypass_proposal\": false"+
"}";
	

				   new RestCall(myvar).start();
			}
			 
	}

	
	



