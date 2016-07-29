package controllers;


import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.util.Scanner;

import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;



import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONObject;

import com.jayway.jsonpath.JsonPath;

import play.Logger;

@SuppressWarnings("deprecation")
public class HttpClientForElastic {
	private static org.slf4j.Logger logger = Logger.underlying();
    public static void main(String[] args) {}
    
    
    @SuppressWarnings("deprecation")
	public static String  getsyml1(){
    	
    	System.out.println("test ------------------------");
    	String extra=null;
    	String output;
        String path="conf/syml1.txt", filecontent="";
        HttpClientForElastic apacheHttpClientPost = new HttpClientForElastic();
        try {
            @SuppressWarnings("resource")
			DefaultHttpClient httpClient = new DefaultHttpClient();
            HttpPost postRequest = new HttpPost("http://52.1.244.89:9200/syml1/_search");
            filecontent=apacheHttpClientPost.readFileContent(path);
            logger.info("filecontent {}",filecontent);
            StringEntity input = new StringEntity(filecontent);
            input.setContentType("application/json");
            postRequest.setEntity(input);
            HttpResponse response = httpClient.execute(postRequest);
            if (response.getStatusLine().getStatusCode() != 200) {
                throw new RuntimeException("Failed : HTTP error code : " + response.getStatusLine().getStatusCode());
            }
            BufferedReader br = new BufferedReader(new InputStreamReader((response.getEntity().getContent())));
            
            
            logger.info("Output from Server .... \n");
            System.out.println("Output from Server .... \n");
            while ((output = br.readLine()) != null) {
            			
            			extra=output;
            			//logger.info(">>>>>>> output {}",output);
                
            }
            JSONObject json = new JSONObject(extra);
           	logger.info("Oextra"+extra);
           	extra=  json.getJSONObject("hits").getJSONArray("hits").getJSONObject(0).getJSONObject("_source").toString();
           	extra =json.quote(extra);
         
            httpClient.getConnectionManager().shutdown();
        } catch (MalformedURLException e) {
        	logger.error("MalformedURLException ",e.getMessage());
         
        } catch (IOException e) {
        	logger.error("IOException" ,e.getMessage());
          
        }
    return extra;
    }
    
	public static String  getproposal(){
    	
    	System.out.println("test ------------------------");
    	String extra=null;
    	String output;
        String path="conf/proposal.txt", filecontent="";
        HttpClientForElastic apacheHttpClientPost = new HttpClientForElastic();
        try {
            @SuppressWarnings("resource")
			DefaultHttpClient httpClient = new DefaultHttpClient();
            HttpPost postRequest = new HttpPost("http://52.1.244.89:9200/proposal/_search");
            filecontent=apacheHttpClientPost.readFileContent(path);
            logger.info("filecontent {}",filecontent);
            StringEntity input = new StringEntity(filecontent);
            input.setContentType("application/json");
            postRequest.setEntity(input);
            HttpResponse response = httpClient.execute(postRequest);
            if (response.getStatusLine().getStatusCode() != 200) {
                throw new RuntimeException("Failed : HTTP error code : " + response.getStatusLine().getStatusCode());
            }
            BufferedReader br = new BufferedReader(new InputStreamReader((response.getEntity().getContent())));
            
            
            logger.info("Output from Server .... \n");
            System.out.println("Output from Server .... \n");
            while ((output = br.readLine()) != null) {
            			
            			extra=output;
            			//logger.info(">>>>>>> output {}",output);
                
            }
            JSONObject json = new JSONObject(extra);
           	logger.info("Oextra"+extra);
           	extra=  json.getJSONObject("hits").getJSONArray("hits").getJSONObject(0).getJSONObject("_source").toString();
           	extra =json.quote(extra);
         
            httpClient.getConnectionManager().shutdown();
        } catch (MalformedURLException e) {
        	logger.error("MalformedURLException ",e.getMessage());
         
        } catch (IOException e) {
        	logger.error("IOException" ,e.getMessage());
          
        }
    return extra;
    }
    
    private String readFileContent(String pathname) throws IOException {

        File file = new File(pathname);
        StringBuilder fileContents = new StringBuilder((int)file.length());
        Scanner scanner = new Scanner(file);
        String lineSeparator = System.getProperty("line.separator");

        try {
            while(scanner.hasNextLine()) {        
                fileContents.append(scanner.nextLine() + lineSeparator);
            }
            return fileContents.toString();
        } finally {
            scanner.close();
        }
    }


	public static String getTaskCreationListener() {
    	
    	System.out.println("test ------------------------");
    	String extra=null;
    	String output;
        String path="conf/taskcreation.txt", filecontent="";
        HttpClientForElastic apacheHttpClientPost = new HttpClientForElastic();
        try {
            @SuppressWarnings("resource")
			DefaultHttpClient httpClient = new DefaultHttpClient();
            HttpPost postRequest = new HttpPost("http://52.1.244.89:9200/taskcreationapps/_search");
            filecontent=apacheHttpClientPost.readFileContent(path);
            logger.info("filecontent {}",filecontent);
            StringEntity input = new StringEntity(filecontent);
            input.setContentType("application/json");
            postRequest.setEntity(input);
            HttpResponse response = httpClient.execute(postRequest);
            if (response.getStatusLine().getStatusCode() != 200) {
                throw new RuntimeException("Failed : HTTP error code : " + response.getStatusLine().getStatusCode());
            }
            BufferedReader br = new BufferedReader(new InputStreamReader((response.getEntity().getContent())));
            
            
            logger.info("Output from Server .... \n");
            System.out.println("Output from Server .... \n");
            while ((output = br.readLine()) != null) {
            			
            			extra=output;
            			//logger.info(">>>>>>> output {}",output);
                
            }
            JSONObject json = new JSONObject(extra);
           	logger.info("Oextra"+extra);
           	extra=  json.getJSONObject("hits").getJSONArray("hits").getJSONObject(0).getJSONObject("_source").toString();
           	extra =json.quote(extra);
         
            httpClient.getConnectionManager().shutdown();
        } catch (MalformedURLException e) {
        	logger.error("MalformedURLException ",e.getMessage());
         
        } catch (IOException e) {
        	logger.error("IOException" ,e.getMessage());
          
        }
    return extra;
    }


	public static String getCrmSync() {
    	
    	System.out.println("test ------------------------");
    	String extra=null;
    	String output;
        String path="conf/crm_symc.txt", filecontent="";
        HttpClientForElastic apacheHttpClientPost = new HttpClientForElastic();
        try {
            @SuppressWarnings("resource")
			DefaultHttpClient httpClient = new DefaultHttpClient();
            HttpPost postRequest = new HttpPost("http://52.1.244.89:9200/crmsync_couchbase/_search");
            filecontent=apacheHttpClientPost.readFileContent(path);
            logger.info("filecontent {}",filecontent);
            StringEntity input = new StringEntity(filecontent);
            input.setContentType("application/json");
            postRequest.setEntity(input);
            HttpResponse response = httpClient.execute(postRequest);
            if (response.getStatusLine().getStatusCode() != 200) {
                throw new RuntimeException("Failed : HTTP error code : " + response.getStatusLine().getStatusCode());
            }
            BufferedReader br = new BufferedReader(new InputStreamReader((response.getEntity().getContent())));
            
            
            logger.info("Output from Server .... \n");
            System.out.println("Output from Server .... \n");
            while ((output = br.readLine()) != null) {
            			
            			extra=output;
            			//logger.info(">>>>>>> output {}",output);
                
            }
            JSONObject json = new JSONObject(extra);
           	logger.info("Oextra"+extra);
           	extra=  json.getJSONObject("hits").getJSONArray("hits").getJSONObject(0).getJSONObject("_source").toString();
           	extra =json.quote(extra);
         
            httpClient.getConnectionManager().shutdown();
        } catch (MalformedURLException e) {
        	logger.error("MalformedURLException ",e.getMessage());
         
        } catch (IOException e) {
        	logger.error("IOException" ,e.getMessage());
          
        }
    return extra;
    }
    
    
}