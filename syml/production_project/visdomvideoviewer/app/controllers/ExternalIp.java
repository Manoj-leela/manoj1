package controllers;

import java.net.InetAddress;
import play.Logger;

public class ExternalIp {
	private static org.slf4j.Logger Logger = play.Logger.underlying();
	
	private static String URL = "http://automation.whatismyip.com";
	
	public static void main(String[] args) {
		ExternalIp ipGetter = new ExternalIp();
		Logger.debug(ipGetter.getIpAddress());
	}
	
	 private InetAddress thisIp;

	  private String thisIpAddress;

	  private void setIpAdd()
	  {
	    try
	    {
	       InetAddress thisIp = InetAddress.getLocalHost();
	       thisIpAddress = thisIp.getHostAddress().toString();
	    }
	    catch(Exception e){
	    	 Logger.error("Error occured in setIpAddress"+e.getMessage());
	    }
	  }

	  protected String getIpAddress()
	  {
	     setIpAdd();
	     return thisIpAddress;
	  }
}
