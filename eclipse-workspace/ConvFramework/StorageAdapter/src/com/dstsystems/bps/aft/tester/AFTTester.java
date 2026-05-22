package com.dstsystems.bps.aft.tester;

import java.sql.Timestamp;

import com.dstsystems.awd.aft.AFTClient;

import com.dstsystems.bps.aft.AFTConfig;
import com.dstsystems.bps.aft.AFTMsg;
import com.dstsystems.bps.aft.ConvAFT;

import com.dstsystems.convchksum.processs.ConvCheckSum;

public class AFTTester {
	
	public static void AFTGet(final String srcLoc, final String destLoc){
		ConvAFT convAFT = null;
		String host="10.193.221.119";
		int port = 2000;
		
		try{
			convAFT = new ConvAFT(true, new AFTConfig(host,port,"DestId",7,ConvCheckSum.ALGO_SHA_256));
			convAFT.set_fileActionFlags(7);
			AFTMsg aftMsg = convAFT.RetrieveDocument(srcLoc, destLoc);
			convAFT.Close();
			
		}
		catch(Exception e){
			System.out.println(e.getMessage());
		}
	}
	
	
	public static void main(String[] args) throws Exception {

		ConvAFT convAFT = null;
		String host="localhost";
		int port = 2000;
		
		String check = "";
		if(check == null || check.length()==0){
			System.out.println("String is null");
			return;
		}
		
		
		AFTClient _aft = null;
		
		try {
			_aft = new AFTClient(host, port);

			_aft.setTracing(true);
			String locator = "tst1.tif";
			int size = _aft.fileLength(locator);
			String msg = String.format("File <%s> size<%d>", locator,size);
			System.out.println(msg);
			
		} catch (Exception e) {
			System.out.println(e.toString());
			
		}
		
		if(true)
			return;
		
		AFTGet("RetrvTest/tst1.tif","D:/AFTGETS/AFT_IMAGES/RetrvTest/tst1.pulled");
		
		if(true){
			return;
		}
		
		
		
		Timestamp toBegTs = new java.sql.Timestamp(java.lang.System.currentTimeMillis());
		System.out.println(toBegTs);
		//String s = String.format("%s",toBegTs);
		String s = toBegTs.toString();
		
		System.out.println(s);
		while(s.length() !=26){
			s+="0";
		}
		System.out.println(s.replaceAll(" ", "-").replaceAll(":", "."));
		
		if(true){
			return;
		}
		
		/*
		 * public ConvAFT(final boolean enableTracing, final AFTConfig aftConfig) throws AFT_FW_Exception{
		this._enableTracing = enableTracing;
		this._aftConfig = aftConfig;
		Connect();
		 */
		try{
			AFTConfig aftConfig = new AFTConfig(host,port,"2",7,ConvCheckSum.ALGO_SHA_256);
			convAFT = new ConvAFT(true, aftConfig);
			
			//aftMsg = convAFT.DeleteDocument(locator);
			
		}catch(Exception e){
			System.out.println(e.getMessage());
		}
		
		
	}
	
	

}
