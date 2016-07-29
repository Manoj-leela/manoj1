package com.syml.proposalRefinance.stagelead;







	import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
	import java.io.OutputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
	import java.security.cert.CertificateException;
	import java.security.cert.X509Certificate;

	import javax.net.ssl.HostnameVerifier;
	import javax.net.ssl.HttpsURLConnection;
	import javax.net.ssl.KeyManager;
	import javax.net.ssl.SSLContext;
	import javax.net.ssl.SSLSession;
	import javax.net.ssl.TrustManager;
	import javax.net.ssl.X509TrustManager;

	import org.codehaus.jettison.json.JSONException;
	import org.codehaus.jettison.json.JSONObject;
import org.slf4j.LoggerFactory;

	

	public class RestCall extends Thread{
		private static org.slf4j.Logger logger = LoggerFactory.getLogger(RestCall.class);	
		
		String documentList;
		
		public RestCall(String documentList) {
			
			this.documentList = documentList;
		}

		
		@Override
		public void run() {
			// TODO Auto-generated method stub
			try {
				restcallTostagMail(documentList);
			} catch (KeyManagementException e) {
				logger.error("error while readin"+e.getMessage());
			} catch (IOException e) {
				logger.error("error while IOException"+e.getMessage());
			} catch (NoSuchAlgorithmException e) {
				logger.error("error while NoSuchAlgorithmException "+e.getMessage());
			}
		}
		
		public  void restcallTostagMail(String documentList) throws KeyManagementException, IOException, NoSuchAlgorithmException {
			// TODO Auto-generated method stub
			logger.info("lead {} : "+documentList);

			
				
				//	URL url = new URL("https://todoist.com/API/v6/login?email=assistant@visdom.ca&password=Visdom1234");
					//URL url = new URL("http:localhost:9000/doclist");
				//SSLSocketFactory sslsocketfactory = (SSLSocketFactory) SSLSocketFactory.getDefault();

				 // configure the SSLContext with a TrustManager
				  SSLContext ctx;
					
					ctx = SSLContext.getInstance("TLS");
				
		        ctx.init(new KeyManager[0], new TrustManager[] {(TrustManager) new DefaultTrustManager()}, new SecureRandom());
		        SSLContext.setDefault(ctx);

		        URL url = new URL("https://dev-stage.visdom.ca/lead");
		        HttpsURLConnection conn = (HttpsURLConnection) url.openConnection();
		        conn.setHostnameVerifier(new HostnameVerifier() {
		            @Override
		            public boolean verify(String arg0, SSLSession arg1) {
		                return true;
		            }

					
		        });
		        
		        conn.setDoOutput(true);
				conn.setRequestMethod("POST");
				conn.setRequestProperty("Content-Type", "application/json");
				
				
			

				String input=documentList.toString();
			OutputStream os = conn.getOutputStream();
					os.write(input.getBytes());
					os.flush();
					if (conn.getResponseCode() != 200) {
						throw new RuntimeException("Failed : HTTP error code : "
							+ conn.getResponseCode());
					}else{
			 
					BufferedReader br = new BufferedReader(new InputStreamReader(
							(conn.getInputStream())));
			 
					
					logger.info("Output from Server .... \n");
		                           String output;
					while ((output = br.readLine()) != null) {
						logger.debug("the output content : "+output);
				
						
						
						
					}
		                            
					}
		        logger.info("Response Code from Server : "+conn.getResponseCode());
		        conn.disconnect();
			
			
		}


		

		   private static class DefaultTrustManager implements X509TrustManager {

			       

					@Override
					public void checkClientTrusted(X509Certificate[] chain, String authType)
							throws CertificateException {
						// TODO Auto-generated method stub
						
					}

					@Override
					public void checkServerTrusted(X509Certificate[] chain, String authType)
							throws CertificateException {
						// TODO Auto-generated method stub
						
					}

					@Override
					public X509Certificate[] getAcceptedIssuers() {
						// TODO Auto-generated method stub
						return null;
					}
			    }
		
			   public static void main(String[] args) throws JSONException {
				   JSONObject jsonTableData=new JSONObject();
					jsonTableData.put("id", "Applicant_4255");
					//restCallDocumentListCreation(jsonTableData.toString());

			}
	}



