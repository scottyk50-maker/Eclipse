package com.dstsystems.bps.hcp.tester;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import com.dstsystems.bps.exceptions.HCPException;
import com.dstsystems.bps.hcp.HCPMsg;
//import com.dstsystems.bps.hcp.apache.HCPProcess;
import com.dstsystems.bps.hcp.HCPProcess;

public class HCPTester {

	public static void main(String[] args) {
		
		//End of life

		//The Commons HttpClient project is now end of life, and is no longer being developed. 
		//It has been replaced by the Apache HttpComponents project in its HttpClient and 
		//HttpCore modules, which offer better performance and more flexibility. 
		
		//D:\source\conversionTeam\build\jdk1.8.0_45\bin\keytool -import -alias IMAGES.LINCDEV.pdc-hcpdev.dstcorp.net -file hcpdev.cer -keystore hcpdev.jks
		 
		
		String trustStorePath = "D:/HCP_Testing/Certs/hcpdev.jks";
		String trustStorePwd = "hcp12345";
		String hcpValidHosts = "dstcorp.net";
		String hcpUserId = "Admin";
		String hcpPwd = "hcp12345";
		//String hcpTenant = "LINCDEV";
		//String hcpNameSpace = "IMAGES";
		String hcpRepository = "pdc-hcpdev.dstcorp.net/rest/Greg";
		String hcpTenant = "IMAGES";
		String hcpNameSpace = "LINCDEV";

		//https://images.lincdev.pdc-hcpdev.dstcorp.net/browser/content_input
		
		String srcPath = "D:/AFTGETS/AFT_IMAGES/tst1.tif";
		//srcPath = "d:/downloads/oepe-12.1.2.1.1-kepler-distro-win32-x86_64.zip";
		DateFormat df = new SimpleDateFormat("dd_MM_yyyy_HH_mm_ss");
		String trgLocator = df.format(new Date()) + "/tst1.tiff";
		
						
		try {
			HCPProcess hcp = new HCPProcess(trustStorePath, trustStorePwd ,hcpValidHosts, hcpUserId, hcpPwd, hcpTenant, hcpNameSpace, hcpRepository);
			
			HCPMsg hcpMsg = hcp.UploadDocument(srcPath, trgLocator, true);
			String msg = String.format("ResponseCD <%s> MSG <%s>", hcpMsg.GetResponseCode(),hcpMsg.GetResponseMsg() );
			System.out.println(msg);
			
			hcpMsg.PrintHeaderFields();
			System.out.println(hcpMsg.GetChecksumResponse());
			
			
		} catch (HCPException e) {
			System.out.println(e.getMessage());
		}
		catch(Exception e){
			System.out.println("Unknown Exception\n" + e.getMessage());
		}

	}

}
