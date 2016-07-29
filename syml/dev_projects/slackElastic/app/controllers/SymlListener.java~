package controllers;

import controllers.HttpClientForElastic;

public class SymlListener extends Thread {

	@Override
	public void run() {
		
		while(true){

			
			try {
			String syml1get=	HttpClientForElastic.getsyml1();
			if(syml1get!=null){
			String documentList="{\"text\":"+syml1get+"}";
			RestCall restcall = new RestCall(documentList);
			restcall.start();}
			} catch (Exception e) {
				
			}
	}

		}
}
