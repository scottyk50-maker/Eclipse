package com.dstsystems.bps.hcp;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.net.ssl.HttpsURLConnection;

public class HCPMsg {
	
	public HCPMsg(final HttpsURLConnection conn) throws IOException{
		_reponseCd = conn.getResponseCode();
		_responseMsg = conn.getResponseMessage();
		_map = conn.getHeaderFields();
	}
	
	public HCPMsg(final int responseCd, final String responseMsg){
		_reponseCd = responseCd;
		_responseMsg = responseMsg;
		_map = new HashMap<String, List<String>>();
	}
	
	public final Map<String, List<String>> GetResponseHeaders(){
		return _map;
	}
	public final int GetHeaderSize(){
		return _map.size();
	}
	
	public final String GetResponseMsg(){
		return _responseMsg;
	}
	
	public final int GetResponseCode(){
		return _reponseCd;
	}
	
	public final String GetChecksumResponse(){
		String checksum="";
		if(_map.containsKey("X-HCP-Hash")){
			checksum = _map.get("X-HCP-Hash").toString();
			int startPos = checksum.indexOf(' ');
			int endPos = checksum.indexOf(']');
			checksum = checksum.substring(startPos, endPos);
			//checksum = _map.get("X-HCP-Hash").toString();
			
		}
		return checksum;
	}
	
	public long get_fileSize() {
		return _fileSize;
	}

	public void set_fileSize(final long fileSize) {
		this._fileSize = fileSize;
	}

	
	public long get_uploadTm() {
		return _uploadTm;
	}

	public void set_uploadTm(long _uploadTm) {
		this._uploadTm = _uploadTm;
	}

	public void PrintHeaderFields(){
		for (Entry<String, List<String>> entry : _map.entrySet())
		{
			System.out.println("------" + entry.getKey() + "----------------");
			for(String field:entry.getValue()){
				System.out.println(field);
			}
		}
	}
	private long _fileSize;
	private long _uploadTm;
	private final Map<String, List<String>>  _map;
	private int _reponseCd;
	private String _responseMsg;
	public static final int    SHUTDOWN_ERROR     	= -998;

}
