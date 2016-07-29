package helper;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class FormatDateString {
	private static final Logger LOGGER = LoggerFactory.getLogger(GenericHelperClass.class);
	public String getFormattedDateString(String dateStr) {
		
		System.out.println("Inside formatDateString ");
		LOGGER.debug("COMMING  date string is "+dateStr);
		DateFormat formatter = new SimpleDateFormat("E MMM dd HH:mm:ss Z yyyy");
		Date date = null;
		try {
			date = (Date) formatter.parse(dateStr);
		} catch (ParseException e) {
			LOGGER.error("Error when parsing string to date.");
		}
		LOGGER.debug("converted date is : "+date);
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		String formatedDate = (cal.get(Calendar.MONTH) + 1) + "/"
				+ cal.get(Calendar.DATE) + "/" + cal.get(Calendar.YEAR);
		LOGGER.debug("formatedDate : " + formatedDate);
		System.out.println(">>>>>>>>"+formatedDate);
		return formatedDate;
	}
	public static void main(String[] args) {
		FormatDateString obj=new FormatDateString();
		obj.getFormattedDateString("Thu Jan 01 00:00:00 IST 2015");
	}
}
