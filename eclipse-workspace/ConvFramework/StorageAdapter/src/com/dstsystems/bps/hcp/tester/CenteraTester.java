package com.dstsystems.bps.hcp.tester;

import com.dstsystems.bps.centera.CenteraProcess;
import com.dstsystems.bps.exceptions.CenteraException;

public class CenteraTester {

	public static void main(String[] args) {
		//public CenteraProcess(final String appName, final String appVersion, final String poolAddress, final String peaFilePath) throws CenteraException{
		/*
		 * <_centeraAppName>Centera2HCP</_centeraAppName>
    <_centeraAppVersion>1.1</_centeraAppVersion>
		 */
		String appName = "CenteraTester";
		String appVersion = "1.1";
		String poolAddress = "10.193.85.23";
		String peaFilePath = "D:\\Centera_SDK_Windows_2000-5.0-Win64Dev8\\peafiles\\Palmer-tstnew.pea";
		String imagePath = "D:/AFTGETS/AFT_IMAGES/tst1.tif";
		CenteraProcess centeraProcess = null;
		try {
			centeraProcess = new CenteraProcess(appName, appVersion, poolAddress, peaFilePath);
			String clip = centeraProcess.StoreDocument(imagePath, 1000);
			System.out.println("Created Clip: " + clip);
			
		} catch (CenteraException e) {
			System.out.println(e.getMessage());
		}
		finally{
			try {
				if(centeraProcess != null){
					centeraProcess.Close();
				}
					
			} catch (CenteraException e) {
				System.out.println(e.getMessage());
			}
		}
		

	}

}
