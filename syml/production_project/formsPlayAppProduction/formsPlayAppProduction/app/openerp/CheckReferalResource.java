package openerp;

import java.util.ArrayList;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.debortoliwines.openerp.api.FilterCollection;
import com.debortoliwines.openerp.api.ObjectAdapter;
import com.debortoliwines.openerp.api.Row;
import com.debortoliwines.openerp.api.RowCollection;
import com.debortoliwines.openerp.api.Session;
import helper.GenericHelperClass;

public class CheckReferalResource {
	static Session openERPSession = null;
	static Logger log = LoggerFactory.getLogger(CheckReferalResource.class);

	public static void main(String[] args) {
		int i = 0;
		
		ArrayList<String> list=null;
		 list=new CheckReferalResource().findReferralSourceCode("kENDALL@VISDOM.CA");
		 System.out.println("referraL code"+list.get(0));
		 
		 System.out.println("referraL code"+list.get(1));

		 System.out.println("referraL code"+list.get(2));

		// "Raessl er ", "kENDALL@VISDOM.CA");
		System.out.println("data-0------" + i);
	}

	GenericHelperClass ghelper = new GenericHelperClass();

	public ArrayList<String> findReferralSourceCode(
			String email) {

		log.info("inside findReferralSourceCode method of CheckReferalSource class");

		openERPSession = ghelper.getOdooConnection();
		int referalCode = 0;
		int referralId = 0;
		String phoneNumber = "";
		ArrayList<String> arrayList = new ArrayList<String>();
		try {

			String referalResponse = null;

			ObjectAdapter partnerAd = openERPSession
					.getObjectAdapter("hr.applicant");

			FilterCollection filters = new FilterCollection();

			filters.add("email_from", "=", email.toLowerCase().trim());

			RowCollection partners = partnerAd.searchAndReadObject(filters,
					new String[] { "email_from" });
			log.debug("no. of referral in referral module of openerp : "
					+ partners.size());

			if (partners.size() == 0) {

				referalResponse = "We are having difficulty finding your email address in our records.  Is the above email address the one your used when you became involved in Visdom's Referral Program?  If not, please change the email address above to the email address you used when you entered the Visdom Referral Program.  In the event your email address has changed, once you complete this form, please send an email to referrals@visdom.ca with your new email address and contact information so we can ensure the appropriate referral fee is paid to you and we adjust our records accordingly.";
				log.debug(referalResponse);
				arrayList.add(phoneNumber);
				arrayList.add(referalCode + "");
				arrayList.add(referralId + "");

				return arrayList;

			} else {
				log.info("inside else i.e  referral exist");
				boolean referralSourceFound = false;
				int numberOfReferrals = 0;
				for (int k=0;k<partners.size();k++) {
					try{
					Row row=partners.get(k);
					++numberOfReferrals;
					phoneNumber = row.get("partner_phone").toString();
					}catch(Exception e){
						e.printStackTrace();
					}
				}
				if (numberOfReferrals == 1) {

					Row row = partners.get(0);
					try {
						referralId = Integer.parseInt(row.get("id").toString());
						phoneNumber = row.get("partner_phone").toString();
					} catch (Exception e) {

					}
					boolean checkReferalResource = true;
					log.debug("check referal resource value : "
							+ checkReferalResource);

					if (checkReferalResource == true) {

						referralSourceFound = true;

					} else {

						referralSourceFound = false;

					}

				}
				// end of for
				if (referralSourceFound) {

					referalResponse = "Thank you for verifying your involvement in VIsdom's Referral Program.  Once you have completed this referral form, we will send you an email to the above address to confirm the beginning of the mortgage applicaiton process for your referral.";
					log.debug("return message is : " + referalResponse);
					referalCode = 1;
					arrayList.add(phoneNumber);
					arrayList.add(referalCode + "");
					arrayList.add(referralId + "");
					return arrayList;
				} else {
					referalResponse = "We are have found your email address in our records.  However, it is associated with a different name.  Is the above first name and last name the same one you used when you became involved in Visdom's Referral Program?  If not, please send an email to referrals@visdom.ca with your current contact information so we can ensure the appropriate referral fee is paid to you and we adjust our records accordingly.";
					referalCode = 2;
					arrayList.add(phoneNumber);
					arrayList.add(referalCode + "");
					arrayList.add(referralId + "");
					return arrayList;
				}
			}// end of else
		} catch (Exception e) {
			log.error("Error while reading data from server:\n\n"
					+ e.getMessage());
		}

		arrayList.add(phoneNumber);
		arrayList.add(referalCode + "");
		arrayList.add(referralId + "");
		return arrayList;
	}
	
	public String getReferralPhoneNumber(
			String referralID) {

		log.info("inside findReferralSourceCode method of CheckReferalSource class");

		openERPSession = ghelper.getOdooConnection();
	String email="";
	String firstName="";
	String LastName="";
		String phoneNumber = "";
		try {


			ObjectAdapter partnerAd = openERPSession
					.getObjectAdapter("hr.applicant");

			FilterCollection filters = new FilterCollection();

			filters.add("id", "=", referralID);

			RowCollection partners = partnerAd.searchAndReadObject(filters,
					new String[] { "email_from","id","partner_mobile","name" });
			log.debug("no. of referral in referral module of openerp : "
					+ partners.size());

		Row referralData=partners.get(0);
		phoneNumber=(String) referralData.get("partner_mobile");
		email=(String) referralData.get("email_from");
		String name=(String) referralData.get("name");
		try{
			String arString[]=name.split("_");
			firstName=arString[0];
			LastName=arString[1];
		}catch(Exception e){
			
		}
		} catch (Exception e) {
			log.error("Error while reading data from server:\n\n"
					+ e.getMessage());
		}

	
		return phoneNumber;
	}
	
	
	public boolean matchContactIdInReferalResource(int contactId) {

		log.info("inside matchContactIdInReferalResource of CheckReferalResource class");

		log.debug("contact id is : " + contactId);

		boolean value = false;

		// openERPSession=getOdooConnection();
		try {
			ObjectAdapter hradapter = openERPSession
					.getObjectAdapter("hr.applicant");
			FilterCollection referalFileter = new FilterCollection();
			referalFileter.add("partner_id", "=", contactId);

			RowCollection hrReferral = hradapter.searchAndReadObject(
					referalFileter, new String[] { "name", "email_from",
							"partner_id" });
			log.debug("no of record in referal resource : " + hrReferral.size());

			if (hrReferral.size() == 0) {
				log.debug("no record exist with contact id");
				return value;
			} else {
				// Row row=hrReferral.get(1);

				log.debug("maching id found in referal resource with specified contact id");
				value = true;
				return value;

			}
		} catch (Exception e) {
			log.error("error is : " + e);
		}
		return value;

	}
}
