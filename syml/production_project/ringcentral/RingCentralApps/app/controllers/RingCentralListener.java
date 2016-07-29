package controllers;

import java.util.ArrayList;
import java.util.List;

import play.mvc.Result;

import com.bizsyml.utill.PhoneCall;
import com.bizsyml.utill.RingOut;
import com.bizsyml.utill.RingStatusObject;
import com.bizsyml.utill.Token;

public class RingCentralListener extends Thread{
	
	public static List<String> errMsg  ;
	
	@Override
	public void run() {
		while(true){
		// TODO Auto-generated method stub
		boolean isFormValid = true;
		if(isFormValid){
		errMsg = new ArrayList<String>();
			try {
				Double duration = PhoneCall.getDuration();
				
				
				System.out.println("Duration "+duration);
				
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				errMsg.add(e.getMessage());
			}
			try {

				SendSMS sms=new SendSMS();
				sms.sendSms();
				
				System.out.println("Sms Sended Succesfully");
				
				
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				errMsg.add(e.getMessage());
			}
			
			
		}
		/*if (isFormValid) {
						try{
							
							// Generate Access Token
							String  accessToken= Token.getInstance().getAccess_token();
						
							//System.out.println(Caller);
							//System.out.println(Callee);
							RingStatusObject statusObject = RingOut.MakeCall(accessToken,"","" );
						
						}
						catch(Exception e){
							e.printStackTrace();
							errMsg.add(e.getMessage());
						}

		}*/
		try{
		Thread.sleep(300000);
		}catch (Exception e) {
e.printStackTrace();		}
		}
					
	}
	
	

}
