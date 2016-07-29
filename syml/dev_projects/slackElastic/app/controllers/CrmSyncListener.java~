package controllers;

public class CrmSyncListener  extends Thread{

	@Override
	public void run() {
		// TODO Auto-generated method stub
		while(true){

			System.out.println("dddddddddddddddddddd");
			try {
			String crmListener=	HttpClientForElastic.getCrmSync();
			if(crmListener!=null){
			String documentList="{\"text\":"+crmListener+"}";
			RestCall restcall = new RestCall(documentList);
			restcall.start();}
			} catch (Exception e) {
				
			}
	}

		}


}
