package com.dstsystems.bps.centera.tester;

import com.dstsystems.bps.centera.CenteraMsg;
import com.dstsystems.bps.centera.CenteraProcess;
import com.dstsystems.bps.exceptions.CenteraException;
import com.dstsystems.bps.streams.FileChannelOutputDigestStream;

public class Tester {
//STOREWITHCRC
	public static void main(String[] args) {
		
		String appName="Dev Test";
		String appVersion="3.1";
		String poolAddress = "10.193.85.23";
		//String peaFilePath = "C:\\Centera31Tools\\pdPalmer-AWD.pea";
		String peaFilePath = "D:/Centera_SDK_Windows_2000-5.0-Win64Dev8/peafiles/Palmer-tstnew.pea";
		//String logCfgPath ="D:/source/workspaces/Center2HCP/Centera2HCP/config/logging.properties";
		String filePath = "D:\\AFTGETS\\AFT_IMAGES\\tst1.tif";
		String clip = "FKP0S33A79VTJeF9SF0C7CF2PCNG41A5V6B6AK0Q5836LR087JO9Q";
		String clipId = "";
		String savePath="D:\\AFTGETS\\AFT_IMAGES\\tst1_search_1.tif";
		//String savePath=args[0];
		char action ='s';
		try {
			CenteraProcess cp = new CenteraProcess(appName,appVersion,poolAddress,peaFilePath);
			//FKP0S33A79VTJeF9SF0C7CF2PCNG41A5V6B6AK0Q5836LR087JO9Q
			switch(action){
			case 's':
				clipId = cp.StoreDocument(filePath,0);
				System.out.println(clipId);
				break;
			case 'r':
				CenteraMsg cMsg = cp.DigestRetrieveByClipId(clipId, savePath, FileChannelOutputDigestStream.ALGO_SHA_256);
				String msg = String.format("Status=<%d>\nMsg=<%s>\nChecksum=<%s>\n", cMsg.get_status(),cMsg.get_statusMsg(),cMsg.get_checkSum());
				System.out.println(msg);
				break;
			case 'v':
				cMsg = cp.ClipExists(clip);
				msg = String.format("Status=<%d>\nMsg=<%s>\nChecksum=<%s>\n", cMsg.get_status(),cMsg.get_statusMsg(),cMsg.get_checkSum());
				System.out.println(msg);
				break;
			default:
				break;
			}
			
			
			cp.Close();
		} catch (CenteraException e) {
			System.out.println(e.getMessage());
		}

	}

}
