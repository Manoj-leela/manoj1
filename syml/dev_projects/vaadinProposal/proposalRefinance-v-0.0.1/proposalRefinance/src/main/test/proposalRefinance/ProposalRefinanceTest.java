package proposalRefinance;

import org.junit.Assume;
import org.junit.Test;

import com.vaadin.server.VaadinRequest;

import war.MyUI;


public class ProposalRefinanceTest { 
	
	VaadinRequest request ;
	
	//MyUI myui = request.getParameter(parameter);
	
	public String  getUrlAction(VaadinRequest request){
		
		
		return request.getParameter("action");
	}
	
	
	@Test
	public void testUrlAction()
	{
		request.getParameter("action");
		Assume.assumeNotNull(request.getParameter("action"));
		
	}

}
