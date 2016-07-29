package controllers;

public class TaskCreationListener extends Thread {

	@Override
	public void run() {
		// TODO Auto-generated method stub
		while(true){

			
			try {
			String taskCreation=	HttpClientForElastic.getTaskCreationListener();
			if(taskCreation!=null){
			String documentList="{\"text\":"+taskCreation+"}";
			RestCall restcall = new RestCall(documentList);
			restcall.start();}
			} catch (Exception e) {
				
			}
	}

		}


}
