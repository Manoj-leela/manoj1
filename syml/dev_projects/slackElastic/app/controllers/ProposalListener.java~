package controllers;

public class ProposalListener extends Thread {
private static org.slf4j.Logger logger = Logger.underlying();
	@Override
	public void run() {
		// TODO Auto-generated method stub
		while(true){

			System.out.println("dddddddddddddddddddd");
			try {
			String proposal=	HttpClientForElastic.getproposal();
			if(proposal!=null){
			String documentList="{\"text\":"+proposal+"}";
			RestCall restcall = new RestCall(documentList);
			restcall.start();}
			} catch (Exception e) {
				logger.error("error while in ");
			}
	}

		}


}
