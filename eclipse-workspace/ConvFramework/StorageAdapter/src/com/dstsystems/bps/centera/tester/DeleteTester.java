package com.dstsystems.bps.centera.tester;

import com.dstsystems.bps.centera.CenteraMsg;
import com.dstsystems.bps.centera.CenteraProcess;
import com.dstsystems.bps.exceptions.CenteraException;

public class DeleteTester {

	public static void main(String[] args) {
	
		String appName="Dev Test";
		String appVersion="3.1";
		String poolAddress = "10.193.85.23";
		//String peaFilePath = "C:\\Centera31Tools\\pdPalmer-AWD.pea";
		String peaFilePath = "D:/Centera_SDK_Windows_2000-5.0-Win64Dev8/peafiles/Palmer-tstnew.pea";
		String auditString = "DT40903 Delete";
		boolean privDelete = false;
		
		String clipId="xxxxxxxxxx";
		
		try {
			CenteraProcess cp = new CenteraProcess(appName,appVersion,poolAddress,peaFilePath);
			CenteraMsg cenMsg = cp.Delete(clipId);
			String result = String.format("Status<%d> %s", cenMsg.get_status(), cenMsg.get_statusMsg());
			System.out.println(result);
			
			cenMsg = cp.ClipExists(clipId);
			result = String.format("Status<%d> %s", cenMsg.get_status(), cenMsg.get_statusMsg());
			System.out.println(result);
			
			
			
		} catch (CenteraException e) {
			e.printStackTrace();
		}
		

	}

}
