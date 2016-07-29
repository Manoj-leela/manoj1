package controllers;

import static play.data.Form.form;

import java.io.IOException;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;

import com.fasterxml.jackson.databind.JsonNode;
import com.syml.infrastracture.CouchBaseOperation;

import play.*;
import play.data.DynamicForm;
import play.mvc.*;
import views.html.*;

public class Application extends Controller {


	private static org.slf4j.Logger logger = play.Logger.underlying();

    // -- Actions
  
    /**
     * Home page
     */
    public static Result index() {
        return ok(
        		login.render("")
        );
    }
  
    
    public static Result backToForm() {
        if(session().get("test")!=null&&session().get("test").equalsIgnoreCase("test")&&checkSession()){

    	return ok(
           		index.render("")

           );
        }else{
        	return ok(
               		login.render("Session is Expired")

               );	
        }
       /* return ok(
            index.render(form(Login.class))
        );*/
    }
    /**
     * Handles the form submission.
     */
    
    public static Result loginSubmit(){
        DynamicForm dynamicForm = form().bindFromRequest();
         String userName=null;
         String password=null;
         userName=dynamicForm.get("userName");
         password=dynamicForm.get("password");
         boolean tryedLogin=checkTryedForLogin();
         
         logger.debug("inside login method ");
         if(tryedLogin){
         if(userName==null||userName.isEmpty()||userName.equals("")||password==null||password.isEmpty()||password.equals("")){
    		 loginFailed();
             logger.info("inside login failed ");

        	 return ok(
             		login.render("Please Enter  Proper UserName And Password")

             );
         }else{
             logger.debug("login sucessfulll ------------- ");

        CouchBaseOperation couOperation=	 new CouchBaseOperation();
        	 boolean success=couOperation.getLogin(userName, password);
        	 if(success){
        	session().put("test", "test");
        	GregorianCalendar cal=new GregorianCalendar();
        	Date date=cal.getTime();
        session().put("userTime", date.getTime()+"");
        try{
    JSONObject  json=new JSONObject();
		json.put("try", 0);
		json.put("time", 0);
		couOperation.storeDataInCouchBase("tryLogin", json);
        }catch(Exception e){
        	logger.error("Error occured while login "+e.getMessage());	  	
        }
        		 return ok(
                   		index.render("")

                   );
        	 }else{
        		 loginFailed();
        		 
                 logger.debug("login failed  ------------- ");

        		 return ok(
                  		login.render("Please Enter  Proper UserName And Password")

                  );
        	 }
        	
         }
        
         }else{
        	 
             logger.debug("Your Account Locked for 15 minutes ");

        	 return ok(
               		login.render("Your Account Locked for 15 minutes")

               ); 
         }
    }
    
    
    
    public static Result adduser() {
    	
        logger.debug("inside add user method  ");

        if(session().get("test")!=null&&session().get("test").equalsIgnoreCase("test")&&checkSession()){
        	GregorianCalendar cal=new GregorianCalendar();
        	Date date=cal.getTime();
        session().put("userTime", date.getTime()+"");
        DynamicForm form = form().bindFromRequest();
   String token=form.get("token");
   
   if(token!=null&&!token.isEmpty()){
            boolean sucess=false;
         sucess=   new CouchBaseOperation().addingUser(token);
         String result="";
         if(sucess){
        	 
             logger.debug("User Data Added Sucessfully");

        	 result="User Data Added Sucessfully";
         }else{
             logger.debug("Please Enter Proper Token");

        	 result="Please Enter Proper Token";
         }
            return ok(
                hello.render(result)
            );
   }else{
	   return ok(
               hello.render("Please Enter Proper Token")
           );
   }
        } else{
        	return ok(
               		login.render("Session is Expired")

               );	
        }
    }
    
    
    public static Result updateUser() {
        logger.debug("inside update user method ");

        if(session().get("test")!=null&&session().get("test").equalsIgnoreCase("test")&&checkSession()){
        	GregorianCalendar cal=new GregorianCalendar();
        	Date date=cal.getTime();
        session().put("userTime", date.getTime()+"");
        DynamicForm form = form().bindFromRequest();
        
           String token=form.get("token");
           if(token!=null&&!token.isEmpty()){
               logger.debug("User Data Updated Sucessfully ");

            new CouchBaseOperation().updateUser( token);
            return ok(
                hello.render("User Data Updated Sucessfully ")
            );
           }else{
        	   return ok(
                       hello.render("Please Enter Proper token ")
                   );
           }
        
        } else{
        	return ok(
               		login.render("Session is Expired")

               );	
        }
    }

	public static Result getTasks() throws JSONException, IOException,
			org.json.JSONException {
		
		
        logger.debug("inside getTasks method ");

		JsonNode json = request().body().asJson();
		if (json == null) {
			return badRequest("Expecting Json data");
		} else {

			logger.debug("Json Data" + json);
			try {

	/*			for (int i = 0; i < jsonArray.length(); i++) {*/

					JSONObject jsonData = new JSONObject(json.toString()) ;
                    logger.debug("json data----------------"+jsonData);
					String eventName = (String) jsonData.get("event_name");
					JSONObject jsonEventdata = (JSONObject) jsonData
							.get("event_data");
					String taskId = new Integer((int) jsonEventdata.get("id"))
							.toString();
					int opprtunityId=0;
					
					try{
						String val[]=null;
						String projectName=null;
						switch (eventName) {
						case "project:added":
							 projectName=jsonEventdata.get("name")
							.toString();
				 val=projectName.split("_");
					logger.debug("vals "+val[2]);
					opprtunityId=new Integer(val[2]);
					jsonData.put("crmId", opprtunityId);
							break;

						case "project:updated":
							 projectName=jsonEventdata.get("name")
							.toString();
					 val=projectName.split("_");
					logger.debug("vals "+val[2]);
					opprtunityId=new Integer(val[2]);
					jsonData.put("crmId", opprtunityId);			
					      break;

						case "project:deleted":
							 projectName=jsonEventdata.get("name")
							.toString();
					val=projectName.split("_");
					opprtunityId=new Integer(val[2]);
					jsonData.put("crmId", opprtunityId);
							break;

						case "project:archived":
							 projectName=jsonEventdata.get("name")
							.toString();
				    val=projectName.split("-");
					opprtunityId=new Integer(val[2]);
					jsonData.put("crmId", opprtunityId);
					break;
					
					default:
						break;
						}
						
					}catch(Exception e){
						logger.error("Error occured while project created & pdated "+e.getMessage());	  			
					}
					String userId=null;
				try{
					userId=new Integer((int) jsonData.get("user_id")).toString();
				}catch(Exception e){
					logger.error("Error occured while project updated "+e.getMessage());	  			
				}
				new CouchBaseOperation().splitEvents(eventName, taskId,
							jsonData,userId);
				
			} catch (Exception e) {
				logger.error("Error occured while project operations in couchbase"+e.getMessage());

			}

			return ok("Hello ");

		}
	}
	
	public static Result getUserDetails(){
		
        logger.debug("inside getUserDetails method ");

        if(session().get("test")!=null&&session().get("test").equalsIgnoreCase("test")&&checkSession()){
        	GregorianCalendar cal=new GregorianCalendar();
        	Date date=cal.getTime();
        session().put("userTime", date.getTime()+"");
		List<controllers.User> user=new CouchBaseOperation().getUsers();
		if(user.isEmpty()){
		try{
			Thread.sleep(6000);
		}catch(Exception e){
			logger.error("Error occured while processing getUserDetails"+e.getMessage());			
		}
		user=new CouchBaseOperation().getUsers();
		
		if(user.isEmpty()){
			try{
				Thread.sleep(6000);
			}catch(Exception e){
				logger.error("Error occured while Threadsleeping"+e.getMessage());	
			}
			user=new CouchBaseOperation().getUsers();
		}
		}
		return ok(list.render(user));
        } else{
        	return ok(
               		login.render("Session is Expired")

               );	
        }
	}
	
	public static Result editUser(String userID){
        if(session().get("test")!=null&&session().get("test").equalsIgnoreCase("test")&&checkSession()){
        	GregorianCalendar cal=new GregorianCalendar();
        	Date date=cal.getTime();
        session().put("userTime", date.getTime()+"");
User user=new CouchBaseOperation().getPerticulerUser(userID);
		return ok(edit.render(user));
		
        } else{
        	return ok(
               		login.render("Session is Expired")

               );	
        }
	}

	public static Result deleteUser(String userID){
        if(session().get("test")!=null&&session().get("test").equalsIgnoreCase("test")&&checkSession()){
        	GregorianCalendar cal=new GregorianCalendar();
        	Date date=cal.getTime();
        session().put("userTime", date.getTime()+"");
boolean sucess;
sucess=new CouchBaseOperation().deleteUser(userID);
if(sucess){
	
    logger.debug("Todoist user Detials Deleted sucessfully ");

	return ok(
       		hello.render("Todoist user Detials Deleted sucessfully")

       );	
}else{
    logger.debug("error in deleting Todoist user detials");

	return ok(
       		hello.render("error in deleting Todoist user detials")

       );	
}
		
        } else{
        	return ok(
               		login.render("Session is Expired")

               );	
        }
	}
	
	
	
	
	
	public static Result logout(){
		try{
		session().clear();
		return ok(
	            login.render("")
	        );
		}catch(Exception e){
			 logger.error("Error occured while processing of logout"+e.getMessage());
			return ok(login.render(""));
		}
	}
	  
	public static Result GetUser(){
	    if(session().get("test")!=null&&session().get("test").equalsIgnoreCase("test")&&checkSession()){
	    	GregorianCalendar cal=new GregorianCalendar();
        	Date date=cal.getTime();
        session().put("userTime", date.getTime()+"");
	    	JSONObject json=null;
	    	String name="";
	    	String password="";
	    	CouchBaseOperation couhbase=  	new CouchBaseOperation();

	    	try{
	    		json=new JSONObject(couhbase.getDocument("DataFromTodoist").toString());
	    	
	    	}catch(Exception e){
	    		logger.error("Error occured while processing GetUser"+e.getMessage());		
	    	}
	    	if(json!=null){
	    		try{
	    		name=json.get("username").toString();
	    		password=couhbase.decryptPassword(json.get("password").toString());
	    		   try{
	    		       password=password.substring(6, password.length());
	    		    		   }catch(Exception e){
	    		               logger.error("Error occured while processing GetUser"+e.getMessage());			   
	    		    		   }
	    		}catch(Exception e){
	    			logger.error("Error occured while processing GetUser"+e.getMessage());	
	    		}
	    		return ok(
	          			updateuser.render("",name,password)

	               );
	    	}else{
	    		 return ok(
	    	                hello.render("error in fetching user detials")

	    	        );
	    	}
	    }else{
	   	 return ok(
	     		login.render("Session is Expired")

	     );
	}

		
	}

	public static Result updateLoginUser(){
		
		
	    if(session().get("test")!=null&&session().get("test").equalsIgnoreCase("test")&&checkSession()){
	    	GregorianCalendar cal=new GregorianCalendar();
        	Date date=cal.getTime();
        session().put("userTime", date.getTime()+"");
		  DynamicForm dynamicForm = form().bindFromRequest();
	       String userName=null;
	       String password=null;
	       userName=dynamicForm.get("userName");
	       password=dynamicForm.get("password");
	    		
	       if(userName==null||userName.isEmpty()||userName.equals("")||password==null||password.isEmpty()||password.equals("")){
	      	 return ok(
	      			updateuser.render("Please Enter  Proper Email And Password","","")

	           );
	       }else{
	      	 
	      	 new CouchBaseOperation().updateAdminDetailsCouhbase(userName, password);
	      
	   	 return ok(
	                hello.render("User  Detials Udated Sucessfully")

	        );
	}}else{
		 return ok(
	        		login.render("Session is Expired")

	        );
	 }}
	
	public static boolean checkSession(){
		 String previousTick = session().get("userTime");
		 logger.debug("seseion cehcick"+previousTick);
		 boolean sessoinCheck=false;
	        if (previousTick != null && !previousTick.equals("")) {
	            long previousT = Long.valueOf(previousTick);
	            GregorianCalendar calendar=new GregorianCalendar();
	            Date date=calendar.getTime();
	            long currentT = date.getTime();
	            long timeout = Long.valueOf(Play.application().configuration().getString("sessionTimeout")) * 1000 * 60;
	           
	            logger.debug("sesssion time given "+timeout);
	            logger.debug("sesssion cuurenttime  "+currentT);
	            logger.debug("deferrencein time"+(currentT - previousT));
	            if ((currentT - previousT) > timeout) {
	                // session expired
	                session().clear();
	                return sessoinCheck;
	            } else{
	            	return true;
	            }
	        }
	        return sessoinCheck;
	}
	
	public static boolean checkTryedForLogin(){
		CouchBaseOperation couchbase=new CouchBaseOperation();
		JSONObject json=null;
		boolean sucess=false;
		try{
			json=new JSONObject(couchbase.getDocument("tryLogin").toString());
		}catch(Exception e){
			logger.error("Error occured while processing checkTryedForLogin"+e.getMessage());
		}
		try{
		if(json!=null){
			GregorianCalendar cal=new GregorianCalendar();
Date date=cal.getTime();
logger.debug("date.getTime() "+date.getTime() +" "+json.get("time").toString() +" greter "+(date.getTime()>Long.parseLong(json.get("time").toString())));
			if(date.getTime()>Long.parseLong(json.get("time").toString())){
				return true;
			}else{
				
				return sucess;
			}
		
		}else{
			json=new JSONObject();
			json.put("try", 0);
			json.put("time", 0);
			couchbase.storeDataInCouchBase("tryLogin", json);
			return true;
		}
		
		}catch(Exception e){
			logger.error("Error occured while processing checkTryedForLogin"+e.getMessage());
		}
		return sucess;
		
	}
	
	public static void  loginFailed(){
	
			CouchBaseOperation couchbase=new CouchBaseOperation();
			JSONObject json=null;
			try{
				json=new JSONObject(couchbase.getDocument("tryLogin").toString());
			}catch(Exception e){
				logger.error("Error occured while loginFailed"+e.getMessage());		
			}
			try{
			if(json!=null){
				int i=0;
				i=Integer.parseInt(json.get("try").toString());
				if(i==10){
					GregorianCalendar cal=new GregorianCalendar();
					Date date=cal.getTime();
					json.put("time", date.getTime()+900000);
				}
					json.put("try", i+1);
					
					couchbase.storeDataInCouchBase("tryLogin", json);
				}else{
					json=new JSONObject();
					json.put("try", 0);
					json.put("time", 0);
					couchbase.storeDataInCouchBase("tryLogin", json);
				
					
				}
			
			}catch(Exception e){
				logger.error("Error occured while loginFailed"+e.getMessage());			
			}
	

	}
	
public static void main(String[] args) {
	logger.debug("",checkTryedForLogin());
}
}