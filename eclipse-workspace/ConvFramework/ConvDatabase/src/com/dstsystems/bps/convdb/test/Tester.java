package com.dstsystems.bps.convdb.test;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.sql.Clob;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.TreeMap;
import java.util.logging.Level;

import com.dstsystems.bps.convdb.database.ConvDB;
import com.dstsystems.bps.convdb.database.FieldTypeValue;


public class Tester {
	
	public class DBConstants{
		
	
	public static final int CLOB_UPD_POS_PROC_FLG = 1;
	public static final int VDRS_UPD_POS_FRM_RET_MSG = 2;
	public static final int VDRS_UPD_POS_FRM_BEG_TS = 3;
	public static final int VDRS_UPD_POS_FRM_END_TS = 4;
	public static final int VDRS_UPD_POS_FRM_FILE_SZ = 5;
	public static final int VDRS_UPD_POS_SB_FRMT_CD = 6;
	public static final int VDRS_UPD_POS_FRM_CHECKSUM = 7;
	public static final int VDRS_UPD_POS_PHYS_FILE_ID = 8;
	};
	
	public static ArrayList<FieldTypeValue> PopulateFieldList(final ConvDB db,final String objectIdValue,final String metaFilePath) throws Exception{
		
		String processFlagValue = "Y";
		int processFlagPos = 1;
		int responseCodeValue=0;
		int responseCodePos=2;
		String responseMsgValue="qwe";
		int responseMsgPos=3;
		int objectIdPos=4;
		
		
		
		//responseMsgValue = GetTestMetaData(metaFilePath);
		//System.out.println("responseMsgValue.Length(): " + responseMsgValue.length());
		
		ArrayList<FieldTypeValue> fieldList = new ArrayList<FieldTypeValue>();
		
		fieldList.add(new FieldTypeValue(processFlagValue, ConvDB.TYPE_STRING, processFlagPos));

		fieldList.add(new FieldTypeValue(responseCodeValue, ConvDB.TYPE_INT, responseCodePos));
		
		Clob clob = db.GetClob();
		clob.setString(1, responseMsgValue);
	
		
		fieldList.add(new FieldTypeValue(clob, ConvDB.TYPE_CLOB, responseMsgPos));

		fieldList.add(new FieldTypeValue(objectIdValue, ConvDB.TYPE_STRING, objectIdPos));

		return fieldList;
	}
	
	private static String GetTestMetaData(final String metaFilePath) throws Exception{
		
	    
	    	File file = new File(metaFilePath);
	    	System.out.println("Path created: " + file.getAbsolutePath());	
	    	
	    	BufferedReader reader = Files.newBufferedReader(file.toPath(),StandardCharsets.UTF_8); 
	    	String line;
	    	
	    	StringBuilder sb = new StringBuilder();
	    	while ((line = reader.readLine()) != null) {
	    		sb.append(line);
	        }
	    	return sb.toString();
	}
	
	public static String Get26StrTS(final Timestamp ts){
		String s = ts.toString();
		
		while(s.length() !=26){
			s+="0";
		}
		return s.replaceAll(" ", "-").replaceAll(":", ".");
	}
	
	public static void main(String[] args) {
		
		String s = "2005-02-25 11:50:11.579410";
		Timestamp ts = Timestamp.valueOf(s);
		String s_ts;
		
		//String s = "2005-02-25X11:50:11.579410";
		
				StringBuilder sbTS = new StringBuilder(ts.toString());
				while(sbTS.length() !=26){
					sbTS.append("0");
				}
				
				sbTS.setCharAt(10, ' ');
				sbTS.setCharAt(13, ':');
				sbTS.setCharAt(16, ':');
				
				
		System.out.println(sbTS.toString());
		
		if(true){
			return;
		}
		
		
		/*
		 * 
create table hart_otf.clob_tst 
(
OBJECTID CHAR(12) NOT NULL , 
PROC_FLAG CHAR(1) NOT NULL DEFAULT 'N' , 
RESPONSE_CD INT DEFAULT NULL , 
RESPONSE_MSG CLOB(2147483647) DEFAULT NULL , 
CONSTRAINT HART_OTF.PK_clob_tst  PRIMARY KEY( OBJECTID ) ) ; 
); 
		 * 
		 * 
		 */
		
		
		
		
		
		
		String sql = "UPDATE hart_otf/clob_tst SET PROC_FLAG=?, RESPONSE_CD = ?, RESPONSE_MSG = ? WHERE OBJECTID = ?";
		
		
		
		
		String jdbcDriver;
		//jdbcDriver = "oracle.jdbc.driver.OracleDriver";
		jdbcDriver = "com.ibm.as400.access.AS400JDBCDriver";
	    String jdbcURL;
	    //jdbcURL = "jdbc:oracle:thin:@10.193.245.126:1521:awd";
	    jdbcURL = "jdbc:as400://ws2p1;naming=system;errors=full";
	    
	    String dbUserId = "DT40903";
	    String dbPwd = "oct6oct";
	    String objectId;
	    //objectId = "less_32k";
	    objectId = "great_32k";
	     
		String metafilePath = "./config/largefile.dat";
	    
		
		
		ConvDB db = null;
		
		try{
			db = new ConvDB();
			db.Connect(jdbcDriver,jdbcURL, dbUserId, dbPwd);
			
			ArrayList<FieldTypeValue> fieldList = PopulateFieldList(db,objectId,metafilePath);
			int rows = db.ExcecuteUpdate(fieldList, sql);
			String msg = String.format("Rows updated<%d> for objectId<%s>", rows, objectId);
			System.out.println(msg);
			db.Disconnect();
		}
		catch(Exception e){
			System.out.println( e.getMessage());
			
		}
		finally{
			if( db != null){
				db.Disconnect();
			}
			db= null;
		}

	}

}
