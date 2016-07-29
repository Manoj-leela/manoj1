package com.bizsyml.utill;
import org.apache.commons.codec.binary.Base64;
/*
 *   Class to do Base64 Encoding.
 */

public class Base64Encode {

	public static String encode(String strToEncode){
		byte[] encoded = Base64.encodeBase64(strToEncode.getBytes()); 
	     return (new String(encoded));
	     }
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		String orig = RingCentralConstants.APP_KEY + ":" + RingCentralConstants.APP_SECRET;
		String encoded = Base64Encode.encode(orig);
		System.out.println("Encoded String is " + encoded);

	}

}
