package org.openmrs.module.hieproviderhelper;



import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.StringReader;
import java.io.StringWriter;
import java.net.URL;
import java.security.KeyManagementException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateException;

import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManagerFactory;
import javax.xml.bind.DatatypeConverter;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.TransformerFactoryConfigurationError;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;

public class HIEconnector {

	public static String username = "test";
	public static String password = "test";

	// public static String hostname = "https://localhost:5000/";
	public static String hostname = "https://hie.jembi.org:5000/";

	public static SSLSocketFactory sslFactory;

	// For localhost testing only, remove this static block if not
	// using a server on localhost
	/*
	static {
		javax.net.ssl.HttpsURLConnection
				.setDefaultHostnameVerifier(new javax.net.ssl.HostnameVerifier() {

					public boolean verify(String hostname,
							javax.net.ssl.SSLSession sslSession) {
						if (hostname.equals("localhost")) {
							return true;
						}
						return false;
					}
				});
	}
	*/

	public HIEconnector() throws KeyStoreException, NoSuchAlgorithmException,
			CertificateException, IOException, KeyManagementException {
		// Get the key store that includes self-signed cert as a "trusted"
		// entry.
		InputStream keyStoreStream = HIEconnector.class
				.getResourceAsStream("/web/module/resources/truststore.jks");

		// Load the keyStore
		KeyStore keyStore = KeyStore.getInstance(KeyStore.getDefaultType());
		keyStore.load(keyStoreStream, "Jembi#123".toCharArray());
		keyStoreStream.close();

		TrustManagerFactory tmf = TrustManagerFactory
				.getInstance(TrustManagerFactory.getDefaultAlgorithm());
		tmf.init(keyStore);

		SSLContext ctx = SSLContext.getInstance("TLS");
		ctx.init(null, tmf.getTrustManagers(), null);

		// set SSL Factory to be used for all HTTPS connections
		sslFactory = ctx.getSocketFactory();
	}

	private static void addHTTPBasicAuthProperty(HttpsURLConnection conn) {
		String userpass = username + ":" + password;
		String basicAuth = "Basic "
				+ new String(DatatypeConverter.printBase64Binary(userpass
						.getBytes()));
		conn.setRequestProperty("Authorization", basicAuth);
	}

	private static String convertInputStreamToString(InputStream is)
			throws IOException {
		// Buffer the result into a string
		BufferedReader rd = new BufferedReader(new InputStreamReader(is));
		StringBuilder sb = new StringBuilder();
		String line;
		while ((line = rd.readLine()) != null) {
			sb.append(line + "\n");
		}
		rd.close();
		return sb.toString();
	}

	private static void prettyPrintXML(String xml)
			throws TransformerFactoryConfigurationError, TransformerException {
		Transformer transformer = TransformerFactory.newInstance()
				.newTransformer();
		transformer.setOutputProperty(OutputKeys.INDENT, "yes");
		transformer
				.setOutputProperty("{xml.apache.org/xslt}indent-amount", "2");
		StreamResult result = new StreamResult(new StringWriter());
		StreamSource source = new StreamSource(new StringReader(xml));
		transformer.transform(source, result);
		String xmlString = result.getWriter().toString();
		System.out.println(xmlString);
	}

	public String callQueryFacility() throws IOException,
			TransformerFactoryConfigurationError, TransformerException {
		// Setup connection
		URL url = new URL(hostname + "/ws/rest/v1/alerts");
		HttpsURLConnection conn = (HttpsURLConnection) url.openConnection();

		// This is important to get the connection to use our trusted
		// certificate
		conn.setSSLSocketFactory(sslFactory);

		addHTTPBasicAuthProperty(conn);

		// Test response code
		if (conn.getResponseCode() != 200) {
			throw new IOException(conn.getResponseMessage());
		}

		String result = convertInputStreamToString(conn.getInputStream());

		conn.disconnect();

		return result;
	}

	public String callGetFacility() throws IOException,
			TransformerFactoryConfigurationError, TransformerException {
		// Setup connection
		URL url = new URL(hostname + "ws/rest/v1/facility/1234567890");
		HttpsURLConnection conn = (HttpsURLConnection) url.openConnection();

		// This is important to get the connection to use our trusted
		// certificate
		conn.setSSLSocketFactory(sslFactory);

		addHTTPBasicAuthProperty(conn);

		// Test response code
		if (conn.getResponseCode() != 200) {
			throw new IOException(conn.getResponseMessage());
		}

		String result = convertInputStreamToString(conn.getInputStream());

		conn.disconnect();


		return result;
	}

	public void run() throws IOException,
			KeyManagementException, KeyStoreException,
			NoSuchAlgorithmException, CertificateException,
			TransformerFactoryConfigurationError, TransformerException {

		System.out.println("===========================");
		System.out.println("RHEA Client Mock running...");
		System.out.println("===========================\n");

		HIEconnector cm = new HIEconnector();

		System.out.println("Calling Query Facility interface...");
		long startTime = System.nanoTime();
		String result = cm.callQueryFacility();
		long endTime = System.nanoTime();
		double elapsedTimeInSec = (endTime - startTime) / 1000000000D;
		System.out.println("Response recieved in " + elapsedTimeInSec + " seconds.");
		System.out.println("=== Start response body ===");
		prettyPrintXML(result);
		System.out.println("=== End response body ===\n");

		System.out.println("Calling Get Facility interface...");
		startTime = System.nanoTime();
		result = cm.callGetFacility();
		endTime = System.nanoTime();
		elapsedTimeInSec = (endTime - startTime) / 1000000000D;
		System.out.println("Response recieved in " + elapsedTimeInSec + " seconds.");
		System.out.println("=== Start response body ===");
		prettyPrintXML(result);
		System.out.println("=== End response body ===\n");

		System.out.println("===========================");
		System.out.println("RHEA Client Mock Completed.");
		System.out.println("===========================\n");

	}

}
