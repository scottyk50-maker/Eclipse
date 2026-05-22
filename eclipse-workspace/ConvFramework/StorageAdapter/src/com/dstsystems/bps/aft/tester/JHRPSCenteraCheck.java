package com.dstsystems.bps.aft.tester;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.TreeMap;

import com.dstsystems.bps.aft.AFTConfig;
import com.dstsystems.bps.aft.AFTMsg;
import com.dstsystems.bps.aft.ConvAFT;

public class JHRPSCenteraCheck {

	public static ArrayList<String> GetLocators(String filePath) throws Exception{
		ArrayList<String> locList = null; 
		
		try{
			File file = new File(filePath);
			BufferedReader reader = Files.newBufferedReader(file.toPath(),StandardCharsets.UTF_8); 
			locList = new ArrayList<String>();
			String line;
	    	while ((line = reader.readLine()) != null) {
	    		locList.add(line);
	    	}
		}
		catch(Exception e){
			System.out.println(e.getMessage());
			throw e;
		}
		
		
		return locList;
	}
	
	public static void main(String[] args) throws Exception {
		ConvAFT convAFT = null;
		//String host="DSKCJHAAFTPR.dstcorp.net";
		//int port = 30501;
		
		//String inputFile = "D:/jh-rps/inputCenteraFile.txt";
		//String outputFile = "D:/jh-rps/outputCentera.txt";
		String host = args[0];
		int port = Integer.valueOf(args[1]);
		String inputFile = args[2];
		String outputFile = args[3];
		
		String argmsg = String.format("Host=%s\nPort=%d\ninputFile=%s\noutputFile=%s",host,port,inputFile,outputFile);
		System.out.println(argmsg);
		
		ArrayList<String> locList = GetLocators(inputFile);
		FileWriter fw = new FileWriter(outputFile, true);
		BufferedWriter bw = new BufferedWriter(fw);
		PrintWriter out = new PrintWriter(bw);
		
			    
		try{
			AFTConfig aftConfig = new AFTConfig(host,port,"6",1,1);
			convAFT = new ConvAFT(false, aftConfig);
			int counter = 1;
			for(String loc:locList){
				String[] values = loc.split(",");
				String physFileId = values[0];
				String locator = values[1];
				AFTMsg msg = convAFT.VerifyDocument(locator);
				String outputText = String.format("%s^%d^%s",physFileId,msg.GetRtrnCd(),msg.GetAFTMsg());
				System.out.println(counter++ + " - " + outputText);
				out.println(outputText);
				out.flush();
			}
		}catch(Exception e){
			System.out.println(e.getMessage());
		}
		finally {
			if(out != null){
				out.flush();
			    try{out.close();}catch(Exception e){}
			}
			if(bw != null){
			    try{bw.close();}catch(Exception e){}
			}
			
			if(fw != null){
			    try{fw.close();}catch(Exception e){}
			}
			if(convAFT != null){
				try{convAFT.Close();}catch(Exception e){}
			}
		}
	}

}

