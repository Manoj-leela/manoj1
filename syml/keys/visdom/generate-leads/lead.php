<?php
//defined( '_JEXEC' ) or die( 'Restricted access' );
   
    include('xmlrpc.inc');
    $name="unknown";
    if($_POST['name']){
    $name = $_POST['name'];}
    $emailid = $_POST['email'];
    
    $phoneno= $_POST['phoneno'];
    //$brokername= "";
    //if($_POST['broker']){
    //$brokername= $_POST['broker'];}
    if($emailid){
		
		$arrayVal = array(
		'name'=>new xmlrpcval($name, "string") ,
		'email'=>new xmlrpcval($emailid , "string"),
		'phone'=>new xmlrpcval($phoneno , "string")
		
		);
		
		/* Code to create dbo object*/
		define( '_JEXEC', 1 );
		define('JPATH_BASE', dirname(dirname(__FILE__) ));  
		define( 'DS', '/' );
        require_once ( JPATH_BASE .DS.'includes'.DS.'defines.php' );
		require_once ( JPATH_BASE .DS.'includes'.DS.'framework.php' );
		require_once ( JPATH_BASE .DS.'libraries'.DS.'joomla'.DS.'factory.php' );
		$mainframe =JFactory::getApplication('site');
		 $session = JFactory::getSession();
		//print_r($session);
		//echo $session->get('applynow');exit;
		if($session->get('applynow')){
			//echo "working"; exit;
		
        $jconfig   = new JConfig();
	    
        $db= JFactory::getDbo();
		
		$lead = new Lead();
		$lead->genLead($db,$arrayVal,$name,$emailid,$phoneno);
			
			$query = $db->getQuery(true);
		    $table=$db->quoteName('g21yj_openerp_config');
		    $query->clear();
		    $query->select($db->quoteName(array('state','email','send_mails_to_user','text_to_send')));
			$query->from($db->quoteName('g21yj_openerp_config'));
			
			$db->setQuery($query);
			
			
			$resultsets = $db->loadObjectList();
			
		    foreach($resultsets as $resultset){
		    if($resultset->state == 1){
				if($resultset->email){
					$mails = new mails();
					$mails->admin($resultset,$jconfig,$name,$emailid,$phoneno);
				}
				if($resultset->send_mails_to_user == "true"){
					$mails = new mails();
					$text=$resultset->text_to_send;
					
					$mails->user($name,$emailid,$jconfig,$text);
				}
				
				
			}
		}
		
        
	   	
	   }
	
 	  else{
              echo "Restricted access";
           }
	$session->clear('applynow');
	}
	else{
		 header('Location:../index.php/'); 
	}  

    class Lead{
		function genLead($db,$arrayVal,$name,$emailid,$phoneno)
		{
			$query = $db->getQuery(true);
		    $table=$db->quoteName('g21yj_openerp_config');
		  
		    $query->clear();
		    $query->select($db->quoteName(array('state','database_name', 'user_id', 'password', 'url','email','send_mails_to_user','text_to_send')));
			$query->from($db->quoteName('g21yj_openerp_config'));
			
			$db->setQuery($query);
			
			$configs = $db->loadObjectList();
			
			foreach($configs as $config){
				if($config->state == 1){
					$client = new xmlrpc_client($config->url);
					$db=$config->database_name;
					$user_id = $config->user_id;
					$password=$config->password;
					}
			}
					
					$key_applicant = array(new xmlrpcval(array(new xmlrpcval('email' , "string"), // here we pass col name 			
				new xmlrpcval('=',"string"), // here we pass condition 
				new xmlrpcval($emailid,"string")),"array"), // value it may be int or string 
				);		
				
					$msg = new xmlrpcmsg('execute');
					$msg->addParam(new xmlrpcval($db, "string"));  
					$msg->addParam(new xmlrpcval($user_id, "int"));	
					$msg->addParam(new xmlrpcval($password, "string"));	
					$msg->addParam(new xmlrpcval('res.partner', "string")); 
					$msg->addParam(new xmlrpcval("search", "string"));
					$msg->addParam(new xmlrpcval($key_applicant, "array"));
					$response_ids = $client->send($msg); 	
					if ($response_ids->faultCode())
						return 'Error: '.$response_ids->faultString();
					else{
						$ids=$response_ids->value()->scalarval();
						
						$searchItems=array();
						foreach($ids as $id)
						{
							$searchItems[]=$id->me['int'];
						}
						$partner_id = '';
						if($searchItems){
						$partner_id = $searchItems[0];
						}
						
					}
				if($partner_id == ''){
					$msg = new xmlrpcmsg('execute');
					$msg->addParam(new xmlrpcval($db, "string"));   //database 
					$msg->addParam(new xmlrpcval($user_id, "int"));		// user id	
					$msg->addParam(new xmlrpcval($password, "string"));	// password
					$msg->addParam(new xmlrpcval("res.partner", "string")); 
					$msg->addParam(new xmlrpcval("create", "string"));
					$msg->addParam(new xmlrpcval($arrayVal, "struct"));
            
					

					$resp = $client->send($msg);

					if ($resp->faultCode()){

						echo 'Error: '.$resp->faultString();
					}
					else if(isset($resp->val->me['int'])){
						$idss = $resp->val->me['int'];
						
						$leadVal = array(
						'name'=>new xmlrpcval($name, "string") ,
						'email_from'=>new xmlrpcval($emailid , "string"),
						'phone'=>new xmlrpcval($phoneno , "string"),
						
						'partner_id'=>new xmlrpcval($idss , "int")
						);
						$msg = new xmlrpcmsg('execute');
						$msg->addParam(new xmlrpcval($db, "string"));   //database 
						$msg->addParam(new xmlrpcval($user_id, "int"));		// user id	
						$msg->addParam(new xmlrpcval($password, "string"));	// password
						$msg->addParam(new xmlrpcval("crm.lead", "string")); 
						$msg->addParam(new xmlrpcval("create", "string"));
						$msg->addParam(new xmlrpcval($leadVal, "struct"));
						
						$resp = $client->send($msg);

						if ($resp->faultCode()){

							echo 'Error: '.$resp->faultString();
						}
						else{
							echo "<p style = 'color:#fff;'>Thank you for your interest in using Visdom for your mortgage needs.To assist you better, a Visdom Mortgage Associate will contact you shortly to more fully understand your goals and situation. Following this contact, they will guide you through the process of finding the right mortgage for you.</p>";
						}
						}
					else
						return 'no id found !';
					
				}
				else{
						$leadVal = array(
						'name'=>new xmlrpcval($name, "string") ,
						'email_from'=>new xmlrpcval($emailid , "string"),
						'phone'=>new xmlrpcval($phoneno , "string"),
						
						'partner_id'=>new xmlrpcval($partner_id , "int")
						);
						$msg = new xmlrpcmsg('execute');
						$msg->addParam(new xmlrpcval($db, "string"));   //database 
						$msg->addParam(new xmlrpcval($user_id, "int"));		// user id	
						$msg->addParam(new xmlrpcval($password, "string"));	// password
						$msg->addParam(new xmlrpcval("crm.lead", "string")); 
						$msg->addParam(new xmlrpcval("create", "string"));
						$msg->addParam(new xmlrpcval($leadVal, "struct"));
						
						$resp = $client->send($msg);

						if ($resp->faultCode()){

							echo 'Error: '.$resp->faultString();
						}
						else{
							echo "<p style = 'color:#fff;'>Thank you for your interest in using Visdom for your mortgage needs.To assist you better, a Visdom Mortgage Associate will contact you shortly to more fully understand your goals and situation. Following this contact, they will guide you through the process of finding the right mortgage for you.</p>";
						}
				}
					
					
					
            
			
      }
      
  }
      
  class mails{
    function user($name,$emailid,$jconfig,$text){
			
			$to=$emailid;
			$fromEmail  = $jconfig->mailfrom;
			$fromName  = $jconfig->fromname;
			$subject   = "Re:Apply Us";
									
			$headers = "From: ".$fromName." ".$jconfig->mailfrom." ";
									
			$body = "Dear ".$name.","."<br/>".$text;
									
									
			$return = JFactory::getMailer()->sendMail($fromEmail, $headers, $to, $subject, $body,1);
									
			
			if ($return !== true){
				return new JException(JText::_('COM_JSECURE_MAIL_FAILED'), 500);
			}	
			else{
			
			}
	  }
	  
	  function admin($resultset,$jconfig,$name,$emailid,$phoneno){
	    $adminemailids =$resultset->email;
		$adminemailids=explode(",",$adminemailids);
		foreach($adminemailids as $adminemailid){
									
			$to=$adminemailid;
			$fromEmail  = $jconfig->mailfrom;
			$fromName  = $jconfig->fromname;
			$subject   = "Re:Apply Us";
									
			$headers = "From: ".$fromName." ".$jconfig->mailfrom." ";
									
			$body = "<p>"."User Name: ".$name."</p>"."<p>"."Email Id: ".$emailid."</p>"."<p>"."Phone Number: ".$phoneno."</p>";
									
									
			$return = JFactory::getMailer()->sendMail($fromEmail, $headers, $to, $subject, $body,1);
									
			
			if($return !== true){
				return new JException(JText::_('COM_JSECURE_MAIL_FAILED'), 500);
			}
		 }
		  
	  }
  }
?>

