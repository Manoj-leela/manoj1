package helper;

import java.net.MalformedURLException;
import java.net.URL;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.List;

import play.Logger;
import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import org.apache.xmlrpc.XmlRpcException;
import org.apache.xmlrpc.client.XmlRpcClient;
import org.apache.xmlrpc.client.XmlRpcClientConfigImpl;

public class HttpsConnectionCase {

	private static org.slf4j.Logger logger = play.Logger.underlying();
	
	public static void main(String[] args) throws NoSuchAlgorithmException, KeyManagementException, MalformedURLException {
		TrustManager[] trustManagers = new TrustManager[] {
				new X509TrustManager() {
					public X509Certificate[] getAcceptedIssuers() { return null; }
					public void checkServerTrusted(X509Certificate[] chain, String authType) throws CertificateException {}
					public void checkClientTrusted(X509Certificate[] chain, String authType) throws CertificateException {}
				}
		};
		SSLContext sslContext = SSLContext.getInstance("SSL");
		sslContext.init(null, trustManagers, new java.security.SecureRandom());

		XmlRpcClient xmlrpcDb = new XmlRpcClient();
		XmlRpcClientConfigImpl xmlrpcConfigDb = new XmlRpcClientConfigImpl();
		xmlrpcConfigDb.setServerURL(new URL("https","dev-crm.visdom.ca", 443,"/xmlrpc/db"));

		xmlrpcDb.setConfig(xmlrpcConfigDb);

		try {
			//Retrieve databases only
			List<Object> params = new ArrayList<Object>();
			Object result = xmlrpcDb.execute("list", params);
			Object[] a = (Object[]) result;
			List<String> res = new ArrayList<String>();
			for (int i = 0; i < a.length; i++) {
				if (a[i] instanceof String) {
					res.add( (String)a[i] );
				}
			}
		logger.debug(">>> Databases : " + res.toString());
		} catch (XmlRpcException e) {
			logger.error(">>> XmlException Error while retrieving OpenERP Databases: "+e.getMessage());
		} catch (Exception e) {
			logger.error(">>> Error while retrieving OpenERP Databases: "+e.getMessage());
	  }
	}
}
