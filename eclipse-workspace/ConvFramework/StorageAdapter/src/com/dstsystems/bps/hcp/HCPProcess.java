package com.dstsystems.bps.hcp;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.CookieHandler;
import java.net.CookieManager;
import java.net.ProtocolException;
import java.net.URL;
import java.nio.file.Files;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLSession;

import com.dstsystems.bps.exceptions.HCPException;

import org.apache.commons.codec.binary.Base64;

public class HCPProcess {
	static Logger logger = Logger.getLogger(HCPProcess.class.getName());

	public HCPProcess(	final String trustStorePath,	final String trustStorePwd,		final String validHosts,
						final String userId, 			final String pwd,				final String nameSpace,
						final String tenant, 			final String repository) throws HCPException {

		CookieManager cookieManager = new CookieManager();
		CookieHandler.setDefault(cookieManager);
		SetupTrustStore(trustStorePath, trustStorePwd);
		_validHosts = validHosts;
		login(userId, pwd);
		_baseURL = buildBaseURL(nameSpace, tenant, repository);

	}
	
	private void SetupTrustStore(final String trustStorePath, final String trustStorePwd) throws HCPException{
		if(trustStorePath.isEmpty() || trustStorePwd.isEmpty()){
			throw new HCPException("Either trust store path or password is invalid");
		}
		File file = new File(trustStorePath);
		if(! file.isFile()){
			throw new HCPException("Invalid File Path: " + trustStorePath);
		}
		String path = file.getAbsolutePath();
		System.setProperty( "javax.net.ssl.trustStore",  path);
		System.setProperty( "javax.net.ssl.trustStorePassword", trustStorePwd );
	}
	
		
	private final String buildBaseURL( final String sNamespace, final String sTenant, final String sRepository )
	{
		StringBuilder strURL = new StringBuilder();
		strURL.append("https://");
		strURL.append(sNamespace);
		strURL.append(".");
		strURL.append(sTenant);
		strURL.append(".");
		strURL.append(sRepository);
		return strURL.toString();
	}
	
	private void login(final String userID, final String password) throws HCPException {
		//private static final String AUTH_FRMT = "HCP %s:%s"
		StringBuilder sbAuth = new StringBuilder();
		sbAuth.append("HCP ");
		
		sbAuth.append(toBase64Encoding(userID));
		sbAuth.append(":");
		sbAuth.append(toMD5Digest(password));
		_auth = sbAuth.toString();
		logger.log(Level.INFO, "HCP Auth: " + _auth);
		sbAuth=null;
	}

	public HCPMsg UploadDocument(final String srcPath, final String uploadPath, final boolean ovrdValHsts) {
		HCPMsg hcpMsg = null;
		HttpsURLConnection conn = null;
		String msg;
		long fileSize=0;
		try {

			conn = CreateHttpsConnection(uploadPath,false);
			SetRequestProperties("PUT", conn, ovrdValHsts);
			fileSize =LoadOutputStreamfromFile(srcPath,conn.getOutputStream());
 			conn.connect();
			hcpMsg = new HCPMsg(conn);
			//hcpMsg.PrintHeaderFields();
			ConnectionCleanup(conn, null, null);
			
		} catch (final HCPException e) {
			hcpMsg = new HCPMsg(HCPMsg.SHUTDOWN_ERROR, e.getMessage());
		} catch (final IOException e) {
			msg = "HCP UploadDocument Failed to Find local: " + srcPath;
			logger.log(Level.SEVERE, msg + "\n" +  e.getMessage());
			hcpMsg = new HCPMsg(HCPMsg.SHUTDOWN_ERROR, msg);
		}
		catch(final Exception e){
			hcpMsg = new HCPMsg(HCPMsg.SHUTDOWN_ERROR, "Unknown Exception: " + e.getMessage());
		}
		finally{
			hcpMsg.set_fileSize(fileSize);
			ConnectionCleanup(conn, null, null);
		}
		return hcpMsg;
	}
	
	private HttpsURLConnection CreateHttpsConnection(final String urlFilePath,final boolean stopRedirects) throws HCPException{
		URL uri = null;
		HttpsURLConnection conn = null;
		String urlPath;
		
		urlPath = urlFilePath.startsWith("/")?_baseURL+ urlFilePath:_baseURL+ "/" + urlFilePath;
		logger.log(Level.INFO, "HCPUpload: " + urlPath);
		try {
			uri = new URL(urlPath);
			if(!stopRedirects){
				HttpsURLConnection.setFollowRedirects(false);
			}
			conn = (HttpsURLConnection) uri.openConnection();
		} catch (final IOException e) {
			ConnectionCleanup(conn, null, null);
			throw new HCPException("GetHttpsConnection Failed, Failed to Create Https Connection");
		}
		return conn;
	}
	
	private void SetRequestProperties(final String httpType, HttpsURLConnection conn, boolean ovrdValHsts) throws HCPException{
		try {
			conn.setRequestMethod(httpType.toUpperCase());
			conn.setRequestProperty("Authorization", _auth);
			conn.setRequestProperty("Host", _baseURL);
			conn.setRequestProperty("Date", getDateTime());
			conn.setConnectTimeout(CONNECT_TIMEOUT);
			conn.setReadTimeout(READ_TIMEOUTT);
			
			if(ovrdValHsts){
				conn.setHostnameVerifier(ValidateHosts());
			}
			
			switch(httpType.toUpperCase()){
			case "POST":
			case "PUT":
				conn.setDoInput(true);
				conn.setDoOutput(true);
				
				//chunklen - The number of bytes to write in each chunk. 
				//If chunklen is less than or equal to zero, a default value will be used. (4203L)
				int bufferSize = 1024 * 8;
				conn.setChunkedStreamingMode(bufferSize);
				conn.setUseCaches(false);
				break;
			case "GET":
				conn.setRequestProperty("x-hcp-pretty-print", "true");
				break;
			case "HEAD":
				break;
			}
		} catch (ProtocolException e) {
			throw new HCPException("SetRequestProperties Failed, Failed to set RequestMethod:" + httpType);
		}
			
	
	}
	
	private String getDateTime() {
		final SimpleDateFormat sdf = new SimpleDateFormat(DATE_FORMAT);
		sdf.setTimeZone(TimeZone.getTimeZone("GMT"));
		return sdf.format(new Date());
	}
	
	private HostnameVerifier ValidateHosts() {
		HostnameVerifier hv = new HostnameVerifier() {
			@Override
			public boolean verify(String hostname, SSLSession session) {
				String peerHost = session.getPeerHost();
				//peerHost.matches("(?i).*dstcorp.net.*");
				return peerHost.toLowerCase().endsWith(_validHosts)?true:false;	
			}
		};
		return hv;
	}
	
	private void ConnectionCleanup(HttpsURLConnection conn, InputStream in, OutputStream out) {
		if (conn != null) {
			conn.disconnect();
			conn = null;
		}
		if(in != null){
			try {
				in.close();
			} catch (IOException e) {
				//not much we can do anyways
			}
			finally{
				in = null;
			}
		}
		if(out != null){
			try {
				out.close();
			} catch (IOException e) {
				//not much we can do anyways
			}
			finally{
				out = null;
			}
		}
		
		
	}
	
private long LoadOutputStreamfromFile(final String srcPath, OutputStream requestOutputStream) throws HCPException{
		
		long fileSize = 0;
		try {
			
			File file = new File(srcPath);
			if(! file.isFile()){
				throw new HCPException("Invalid File: " + srcPath);
			}
			fileSize = file.length();
			if(fileSize< 1L){
				throw new HCPException("Zero Bytes: " + srcPath);
			}
			
			CopyFile2RequestStream(file, requestOutputStream);
			return fileSize;

		} catch (final IOException e) {
			throw new HCPException("Failed to load: " + srcPath);
		}

	}
	
	private void CopyFile2RequestStream(File file, OutputStream requestOutputStream) throws IOException {
		Files.copy(file.toPath(), requestOutputStream);
		requestOutputStream.flush();
		requestOutputStream.close();
	}
	
	private String toBase64Encoding(String sInStr) {
		byte separator[] = {};
		Base64 B64Encoder = new Base64(80, separator);
		return new String(B64Encoder.encode(sInStr.getBytes()));
	}

	private String toMD5Digest(String sInStr) throws HCPException {
		StringBuffer mOutDigest = new StringBuffer("");

		MessageDigest pMD = null;
		try {
			pMD = MessageDigest.getInstance("MD5");

			byte pDigest[] = pMD.digest(sInStr.getBytes());

			// Convert to string.
			for (int i = 0; i < pDigest.length; i++) {

				// Add a leading zero if necessary. The toHexString does not do
				// it.
				if (0 == (0xF0 & pDigest[i]))
					mOutDigest.append("0");

				mOutDigest.append(Integer.toHexString(0xFF & pDigest[i]));
			}
		} catch (final NoSuchAlgorithmException e) {
			throw new HCPException("Auth Failed, Failed to create passwordhash");
		}

		return mOutDigest.toString();
	}

	private final String _baseURL;
	private String _auth;
	private static final String DATE_FORMAT = "EEE, dd MMM yyyy HH:mm:ss z";
	
	//Need to be configurable at some point
	private final String _validHosts;
	private static final int CONNECT_TIMEOUT = 60000;
	private static final int READ_TIMEOUTT = 600000;
}
