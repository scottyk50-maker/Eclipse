package com.dstsystems.bps.cfw.framework;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileOutputStream;
import java.io.FileInputStream;
import java.io.OutputStreamWriter;
import java.io.InputStreamReader;
import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;

import com.dstsystems.bps.cfw.pub.exceptions.ShutdownException;
import com.dstsystems.bps.cfw.pub.interfaces.IConfig;
import com.dstsystems.bps.metaparser.config.DataColumn;
import com.dstsystems.bps.metaparser.config.MetaDataParserConfig;

public class CmntBrkr {
	static Logger logger = Logger.getLogger(CmntBrkr.class.getName());

	public static int countOccurrences(String field, char value) {
	    int count = 0;
	    for (char c : field.toCharArray()) {
	        if (c == value) {
	           ++count;
	        }
	    }
	    return count;
	}
	
	public static void main(String[] args) {
		if(args.length !=2){
			logger.log(Level.SEVERE, "wrong amount of arguments <Path to Config.xml> <Config ClassPath>");
			return;
		}
		logger.log(Level.INFO, "starting cmntBrkr");
		_config = LoadConfiguration(args[0],args[1] );
		StringBuffer sb = new StringBuffer();
		BufferedReader br = null;
		ArrayList<String> fileRows = new ArrayList<String>();
		String filePath = _config.get_scrubfilePath();
		String outPath = _config.get_metafilePath();
		String tblID = _config.get_tblID();
		List<DataColumn> dataColumnList = _config.get_dataColumns();
		
		int numDelims = _config.get_delCnt();
		int delimFnd =0;
		String line;
		String cmntLine;
		String ID = null;
		String comment1;
		String comment2;
		String comment3;
		String comment4;
		char cDelim = (char)_config.get_delimeter();
		String delimeter =  Character.toString((char)_config.get_delimeter());
		char cEOL = (char)_config.get_eol();
		boolean hasHeader = _config.get_header();
		int lineCnt = 0;		

		try
		{
			br = new BufferedReader(new InputStreamReader(new FileInputStream(filePath)));
			File fout = new File(outPath);
			FileOutputStream fos = new FileOutputStream(fout);
		 
			BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(fos));			
			/**
			**Eat the Header Row
			**/
/*			if(hasHeader){
				line = br.readLine();	
				lineCnt++; 
			}
*/
			while ((line = br.readLine()) != null) {
				boolean cmnt1Done = false;
				boolean cmnt2Done = false;
				boolean cmnt3Done = false;
				boolean cmnt4Done = false;
				boolean notFinished = true;
				comment1 = "";
				comment2 = "";
				comment3 = "";
				comment4 = "";
				lineCnt++; 
				int EOLCnt = countOccurrences(line,cEOL);
				int delimCnt = countOccurrences(line,cDelim);
				String[] metaFields = line.split(delimeter,-1);
				logger.log(Level.INFO, "Split Count: " + metaFields.length);

				for(DataColumn dc :dataColumnList){
					String fieldName = dc.get_fieldName();
					String fieldValue = metaFields[dc.get_columnPos()];
					int spPos= dc.get_storedProcPos();
					int spType = dc.get_storedProcDataType();
					boolean isPrimaryKey = dc.is_PrimaryKey();
					boolean isComment = dc.is_Comment();
					boolean isClob = dc.is_Clob();
					String msg = String.format("Value <%s> spPos<%d> spType<%d> isPrimary<%b> isComment<%b>",fieldValue,spPos, spType,isPrimaryKey, isComment );
					logger.log(Level.INFO, msg);
					DataColumn dc_db = new DataColumn(fieldName,fieldValue, spPos, spType,isComment,isClob, isPrimaryKey);
					
					if(isComment){
						if(cmnt1Done){
							if(cmnt2Done){
								if(cmnt3Done){
									if(cmnt4Done){
									msg = String.format("More than 4 comment fields");
									logger.log(Level.SEVERE, msg );
									throw new Exception(msg);
									}
									else
									{
										comment4 = fieldValue;
										cmnt4Done = true;

									}
								}
								else
								{
									comment3 = fieldValue;
									cmnt3Done = true;
								}
							}
							else
							{
								comment2 = fieldValue;
								cmnt2Done = true;
							}
						}
						else
						{
							comment1 = fieldValue;
							cmnt1Done = true;
						}
					}	
					else{
						if(fieldName.equals("ID")) {
							ID = fieldValue;
						}
						else{
							msg = String.format("Unknown field passed in config (not ID or a comment): ", fieldName );
							logger.log(Level.SEVERE, msg );
							throw new Exception(msg);
						}
							
					}
				}
				
				int seqNbr = 1;
				if(cmnt1Done){
					seqNbr = buildLine(tblID, ID, seqNbr, comment1, delimeter, bw);
				}
				if(cmnt2Done){
					seqNbr = buildLine(tblID, ID, seqNbr, comment2, delimeter, bw);
				}
				if(cmnt3Done){
					seqNbr = buildLine(tblID, ID, seqNbr, comment3, delimeter, bw);
				}
				if(cmnt4Done){
					seqNbr = buildLine(tblID, ID, seqNbr, comment4, delimeter, bw);
				}
						
				
				String msg1 = String.format("wrote out line number <%d> with value of ,%s>",lineCnt, line);
				logger.log(Level.INFO, msg1  );
				
							delimFnd = 0;
					
				
			}
			br.close();
			bw.close();
		}
		catch(Exception e){
			System.out.println(e.getMessage());
			}

	}

	public static int buildLine(String table, String id, int seq, String commTx, String del, BufferedWriter bw){
	//Need to Add Loop to break apart string to 234 byte strings
		String fullLine;
		try{
			if(commTx.length() > 0){
		
			for (int i=0; i<= commTx.length(); i+=234){
				if(i + 234 > commTx.length()){
					fullLine = table + del + id + del + seq + del + commTx.substring(i);
					bw.write(fullLine);
					bw.newLine();
				}
				else
				{
					fullLine = table + del + id + del + seq + del + commTx.substring(i,i+234);
					bw.write(fullLine);
					bw.newLine();
				}
				seq++;
				}
			}
		}
		catch(Exception e){
			System.out.println(e.getMessage());
		}

		
		return seq;
}


	private static MetaDataParserConfig LoadConfiguration(final String configPath, final String classPath) {
		JAXBContext jaxbContext = null;
		File file = null;//= new File("config.xml");
		try {
			file = new File(configPath);
			if(!file.isFile()){
				String msg = String.format("Failed to load ConfigFile <%s>", configPath);
				logger.log(Level.SEVERE, msg);
				throw new ShutdownException(msg);
			}
		
			_config = (MetaDataParserConfig) Class.forName(classPath).newInstance();
			jaxbContext = JAXBContext.newInstance(_config.getClass());
			Unmarshaller jaxbUnmarshaller = jaxbContext.createUnmarshaller();
			_config= (MetaDataParserConfig) jaxbUnmarshaller.unmarshal(file);
		}	
		 catch(Exception e){
			System.out.println(e.getMessage());
		}
		return _config;
		
		
	}
	private static MetaDataParserConfig _config;

	
}

