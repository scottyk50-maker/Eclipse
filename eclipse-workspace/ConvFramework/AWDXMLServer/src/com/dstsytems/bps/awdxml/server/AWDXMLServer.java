package com.dstsytems.bps.awdxml.server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.Date;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;

import jodd.util.Base64;

import com.dstsytems.bps.awdxml.exceptions.AWDXMLException;



public class AWDXMLServer {
	public AWDXMLServer(final String secret) throws AWDXMLException{
		_useSigAuth = true;
		_secret = secret;
		CreateHMAC(_secret); 
	}
	
	public AWDXMLServer() throws AWDXMLException{
		_useSigAuth = false; 
	}

	public AWDXMLResponse SendXMLRequest(final String url, final String xmlRequest, final String remoteAWDUserId) throws AWDXMLException{
		HttpURLConnection conn = null;
		OutputStreamWriter writer = null;
		InputStream in = null;
		//BufferedReader in = null;
		AWDXMLResponse resp = null;
		
		try {
			conn = CreateHttpConnection(url,remoteAWDUserId );
			
			writer = new OutputStreamWriter(conn.getOutputStream(), "UTF-8");
		 	if (writer != null) {
				writer.write(xmlRequest);
				writer.flush();
				writer.close();
				
				in = conn.getInputStream();
				//in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
				
				
				/*
				byte[] bk = new byte[1024];
				int k = 0;
				
				while ((k = in.read(bk, 0, bk.length)) != -1) {
					sbResponse.append(new String(bk, 0, k));
				}
				*/
				
				StringBuilder sbResponse = new StringBuilder();
				int responseCd = conn.getResponseCode();
				
				int ch;
			    while ((ch = in.read()) != -1) {
			    	sbResponse.append((char) ch);
			    }
				
				resp = new AWDXMLResponse(sbResponse.toString(), responseCd);
				
			}
	
		} catch (IOException e) {
			System.out.println(e.getMessage());
			String msg = "Failed to execute xmlRequest - " + xmlRequest;
			ConnectionCleanup(conn, writer, in);
			throw new AWDXMLException(msg);
		}
		
		return resp;
	}
	
	private void CreateHMAC(final String secret) throws AWDXMLException{
		try {
			_hmac = Mac.getInstance( ALGORITHM );
			final SecretKeySpec secret_key = new SecretKeySpec( secret.getBytes(), ALGORITHM );
			_hmac.init( secret_key );
		} catch (NoSuchAlgorithmException | InvalidKeyException e) {
			System.out.println(e.getMessage());
			String msg = "Failed to Create HMAC B2B Signing Object";
			throw new AWDXMLException(msg);
		}
		

	}
	
	private String B2BSign(final String userId) throws Exception{
		
		final String message = Long.toString( new Date().getTime() ) + sep + userId;
		final String hash = new String( Base64.encodeToString( ( _hmac.doFinal( message.getBytes()))));
		return hash + sep + message; 
	}
	
	private HttpURLConnection CreateHttpConnection(final String url, final String remoteAWDUserId) throws AWDXMLException{
		URL awdURL;
		HttpURLConnection conn = null;
		try {
			awdURL = new URL(url);
			conn = (HttpURLConnection) awdURL.openConnection();
			conn.setRequestMethod("POST");
			conn.setUseCaches(false);
			conn.setDoInput(true);
			conn.setDoOutput(true);
			conn.setRequestProperty("accept-charset", "UTF-8");
			conn.setRequestProperty("content-type", "text/xml");
			conn.setRequestProperty("REMOTE_USER", remoteAWDUserId);
			if(_useSigAuth){
				String sig = B2BSign(remoteAWDUserId);
				conn.setRequestProperty("B2BSignature", sig);
			}
						
			conn.setConnectTimeout(60000); // 1 minute
			conn.setReadTimeout(60000); // 10 minutes
		} catch (MalformedURLException e) {
			ConnectionCleanup(conn, null, null);
			String msg = "Invalid URL: " + url;
			throw new AWDXMLException(msg);
		} catch (IOException e) {
			ConnectionCleanup(conn, null, null);
			String msg = "Failed to create HttpURLConnection: " + url;
			throw new AWDXMLException(msg);
		}
		catch(Exception e){
			ConnectionCleanup(conn, null, null);
			String msg = "Unknown Exception occured - " + e.getMessage();
			throw new AWDXMLException(msg);
		}
		return conn;
		
	}
	
	private void ConnectionCleanup(HttpURLConnection conn, OutputStreamWriter writer, InputStream in ){
		try{
			if (conn != null) {
				conn.disconnect();
			}
			
			if(writer != null){
				writer.close();
			}
			
			if(in != null){
				in.close();
			}
			
		}catch(Exception e){
			//Not much you can do anyway
		}
		finally{
			conn = null;
			writer = null;
			in = null;
		}
		
	}

	
	private Mac _hmac;
	private static final String sep = ":";
	private static final String ALGORITHM = "HmacSHA512";
	private String _secret;
	private boolean _useSigAuth;
	
}
