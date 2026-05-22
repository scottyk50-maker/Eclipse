package com.dstsytems.bps.awdxml.tester;

import java.io.IOException;
import java.io.StringReader;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;

import java.util.List;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import oracle.jdbc.OracleCallableStatement;









import com.dstsystems.awd.aft.AFTClient;
import com.dstsystems.awd.aft.AFTData;
import com.dstsystems.awd.aft.AFTException;
import com.dstsystems.awd.aft.IPException;
import com.dstsytems.bps.awdxml.exceptions.AWDXMLException;
import com.dstsytems.bps.awdxml.server.AWDXMLResponse;
import com.dstsytems.bps.awdxml.server.AWDXMLServer;
import com.dstsytems.bps.awdxml.srvgetcoll.AWDCollection;
import com.dstsytems.bps.awdxml.srvgetcoll.AWDPage;
import com.dstsytems.bps.awdxml.srvgetcoll.SRVGetColl;
import com.dstsytems.bps.awdxml.srvgetcoll.SRVGetCollHandler;

public class Tester {
	
	
	
	public void SP_ORATESTER() throws SQLException, ClassNotFoundException{
		
		
		String driver_class = "oracle.jdbc.driver.OracleDriver";
		String connect_string = "jdbc:oracle:thin:@10.193.245.132:1521:awd";
		String query = "{call GETObjectId(?,?,?,?,?)}";
		
		Connection conn;
        Class.forName(driver_class);
        conn = DriverManager.getConnection(connect_string, "custom", "custom");
	    CallableStatement cstmt = conn.prepareCall(query);
	        
		cstmt.setString(1, "1999");
		cstmt.setString(2, "2015");
		cstmt.setInt(3, 10); 
		cstmt.setString(4, "");
		//cstmt.setString(4, "002000000001");
		cstmt.registerOutParameter(5, oracle.jdbc.OracleTypes.CURSOR);
	        	        
		cstmt.execute();
		ResultSet rs = ((OracleCallableStatement) cstmt).getCursor(5);

	    while (rs.next ()){
        	String msg = String.format("OBJECTID <%s>", rs.getString (1));
        	System.out.println( msg);
        }
	        
	      
        try { rs.close(); } catch (Exception ex) {}
        try { cstmt.close(); } catch (Exception ex) {}
        try { conn.close(); } catch (Exception ex) {}
	    	    
	}
 
	public void Gen_Oracle_TEST()throws SQLException, ClassNotFoundException{
		String driver_class = "oracle.jdbc.driver.OracleDriver";
		String connect_string = "jdbc:oracle:thin:@10.193.245.80:1521:awd";
		
	    
        Connection conn;

        Class.forName(driver_class);
        
        conn = DriverManager.getConnection(connect_string, "AWDPOWNER", "awd");
      
        
        String query = "{call GETObjectId( ?,?)}";
        CallableStatement cstmt = conn.prepareCall(query);
        cstmt.setString(1, "002000000003");
        cstmt.registerOutParameter(2, oracle.jdbc.OracleTypes.CURSOR);
        //cstmt.registerOutParameter(2, java.sql.Types.REF_CURSOR);
                
        //ResultSet rs = cstmt.executeQuery();
        ResultSet rs = ((OracleCallableStatement) cstmt).getCursor(2);
        
        
        while (rs.next ()){
        	System.out.println( rs.getString (1) );
        }

/*        
        while (rs.next ()){
        	String msg = String.format("%s %s %s", rs.getString (1), rs.getString (2), rs.getString (3) );
        	System.out.println( msg);
        }
  */        
        try { rs.close(); } catch (Exception ex) {}
        try { cstmt.close(); } catch (Exception ex) {}
        try { conn.close(); } catch (Exception ex) {}
    	    

		
	}
	public void SP_DB2_TESTER() throws SQLException, ClassNotFoundException{
		
		
    	//String driver_class = "com.ibm.as400.access.AS400JDBCDriver";
    		      
		//String connect_string = "jdbc:as400://10.193.204.19;naming=system;errors=full";

		String driver_class = "oracle.jdbc.driver.OracleDriver";
		//String connect_string = "jdbc:oracle:thin:@10.193.245.80:1521:awd";
		String connect_string = "jdbc:oracle:thin:@10.193.245.132:1521:awd";
		
	    
        Connection conn;

        Class.forName(driver_class);
        
        conn = DriverManager.getConnection(connect_string, "DT40903", "@utumn0");
        
      

        String query = "{call GETObjectId( ?,?,?,?)}";
        CallableStatement cstmt = conn.prepareCall(query);
        cstmt.setString(1, "2004-03-03 00:00:00.300001");
        cstmt.setString(2, "2004-03-03 00:00:00.300009");
        
        /*
         * Provider Retrieve Count
        */
        cstmt.setInt(3, 10);          
        /*
         * Last DocumentId Processed
         */
        //cstmt.setString(4, "MU020105.AAA");
        cstmt.setString(4, "");
        
        ResultSet rs = cstmt.executeQuery();
        //cstmt.registerOutParameter(1,OracleTypes.CURSOR);
        //cstmt.execute();
       // ResultSet rset = (ResultSet)cstmt.getObject(1);

        while (rs.next ()){
        	System.out.println( rs.getString (1) );
        }
          
        try { rs.close(); } catch (Exception ex) {}
        try { cstmt.close(); } catch (Exception ex) {}
        try { conn.close(); } catch (Exception ex) {}
    	    
    }


	public static void main(String[] args) throws AWDXMLException {
		System.out.println("Top of Tester");
		Tester test = new Tester();
		boolean brun1 = false;
		
		if (brun1){
			try {
				test.SP_ORATESTER();
			} catch (ClassNotFoundException | SQLException e) {
				System.out.println(e.getMessage());
				e.printStackTrace();
			}
			//return;
		}
		
		boolean brun2 = false;
		if(brun2){
			
		
		try {
			test.SP_DB2_TESTER();
			//return;
		} catch (Exception e) {
			System.out.println(e.getMessage());
			e.printStackTrace();
			
		}
		}
		
		String secret = "secretsecretsecretsecretsecretsecretsecretsecretsecretsecretsecret";
		
		//AWDXMLServer awdXML = new AWDXMLServer(secret);
		AWDXMLServer awdXML = new AWDXMLServer();

		
		//003000000001
		/*
		 * <DST xml:lang="en-US"><jobName version="1.0">SRVGetColl</jobName><readable>Y</readable><trace>N</trace><AWD><userID>DSTSETUP</userID><source><collection id="q12000000001"><error><code>62000</code><text>Collection does not exist.</text><task>ATSK0152</task></error></collection><error><code>62000</code><text>Collection does not exist.</text><task>ATSK0116</task></error></source></AWD><jobVersion>1.0</jobVersion><jobReturn jobName="SRVGetColl"><taskName/><description/><value>0</value></jobReturn></DST>
		 */
	//	2014-10-12-15.52.04.805709	LFC015849706	
	

		String xmlRequest = SRVGetColl.GetRequestString("DT40903", "2014-10-12-15.52.04.805709","O","01","LFC015849706");
		
		//2001-01-01-00.00.00.000001	002000000003
		//2002-01-01-00.00.00.000001	002000000001
		//xmlRequest = SRVGetColl.GetRequestString("DT40903", "2002-01-01-00.00.00.000001", "O", "01", "002000000001");
		//xmlRequest = "<DST xml:lang=\"en-US\"><jobName version=\"1.0\">SRVGetColl</jobName><readable>Y</readable><trace>0</trace><AWD><userID>DT40903</userID><password></password><source id=\"2015-10-23-13.20.58.346500O01\"><collection id=\"063000052824\"></collection></source></AWD></DST>";
		//xmlRequest = "<DST xml:lang=\"en-US\"><jobName version=\"1.0\">SRVGetColl</jobName><readable>Y</readable><trace>0</trace><AWD><userID>DT40903</userID><password></password><source><collection id=\"063000052824\"></collection></source></AWD></DST>";
		
		xmlRequest = "<DST xml:lang=\"en-US\"><jobName version=\"1.0\">SRVGetColl</jobName><readable>Y</readable><trace>0</trace><AWD><userID>DT40903</userID><source id=\"2014-10-12-15.52.04.805709O01\"><collection id=\"LFC015849706\"></collection></source></AWD></DST>";
		  //xmlRequest = "<DST xml:lang=\"en-US\"><jobName version=\"1.0\">SRVGetColl</jobName><readable>Y</readable><trace>0</trace><AWD><userID>DT40903</userID><source ><collection id=\"LFC015849706\"></collection></source></AWD></DST>";
		
		//String url = "http://efsjhann.dstcorp.net:20011/awdServer/b2b/awdServer";
		String url = "";
		url="http://170.40.50.39:20001/awdServer/b2b/awdServer";
		//String url = "http://10.193.245.80/awdServer/b2b/awdServer";
		url = "http://10.193.244.158/awdServer/b2b/awdServer";
		//url = "http://AWDACI.DSTCORP.NET:20001/awdServer/b2b/awdServer";
		url = "http://efsjhann.dstcorp.net:20021/awdServer/b2b/awdServer";
		//String tempAFTLocation = "d:/temp/aft/";
		/*
		 * 
		 	secretsecretsecretsecretsecretsecretsecretsecretsecretsecretsecret
			abcdefghijklmnopqrstuvwxyzabcdefghijklmnopqrstuvwxyzabcdefghijkl
			1234567890123456789012345678901234567890123456789012345678901234
		 */
		//http://10.193.245.80/awd/portal/index.html?servletcontext=awdServer/b2b
		//http://10.193.245.80/SP80.10.80.0.6/awd/portal/index.html?servletcontext=awdServer/b2b
		//http://HTTPServerHostName/awd/portal/index.html?servletcontext=awdServer/b2b
		/*
		http://www.rastermaster.com/RasterMaster%20Java%20manual/WebHelp/rastermasterjava.htm#D0141-00/chsnowbndmethods.htm#chsnowbndmethods_629572963_405712%3FTocPath%3D19%2520-%2520Class%2520Snow.Snowbnd%2520Methods|_____74
		 */
		
		
/*		
		SBFileTypes sbFileTypes = null;
		
		try {
			sbFileTypes = new SBFileTypes("./config/SB_FILE_TYPES.dat");
			sbFileTypes.GetFileExtention(9);
		} catch (SnowBoundFileTypeException e1) {
			System.out.println(e1.getMessage());
			System.out.println("SBFileTypes SETUP Failed");
			return;
		}
	*/
		
		try {
			System.out.println(xmlRequest);
			AWDXMLResponse resp = awdXML.SendXMLRequest(url, xmlRequest, "DT40903");
			 
			SAXParserFactory saxParserFactory = SAXParserFactory.newInstance();
			
		        SAXParser saxParser = saxParserFactory.newSAXParser();
		        SRVGetCollHandler handler = new SRVGetCollHandler();
		        System.out.println(resp.get_xmlResponse());
		        saxParser.parse(new InputSource(new StringReader(resp.get_xmlResponse())), handler);
		        AWDCollection coll = handler.get_awdCollection();
		        System.out.println("Collection = " + coll.getCollectionId());
		        System.out.println("Response UserID = " + coll.get_userId());
		        if(coll.get_errorOccured()){
		        	String msg = String.format("Code <%d> %s", coll.get_errorCode(), coll.get_errorMsg());
		        	System.out.println(msg);
		        	return;
		        }
		        List<AWDPage> pageList = coll.getAWDPages();
		        //print employee information
		        
		        for(AWDPage page : pageList){
		        	
		        	System.out.println(page.toString());
		        /*
		        	boolean bRun2 = false;
		        	if(bRun2){
			        	AFTClient aftClient = null;
			        	aftClient = new AFTClient(page.get_fileAftHost(),page.get_fileAftPort());
			        	aftClient.login(coll.get_userId(),"");
			        	
			        	aftClient.setTracing(false);
			        	//String aftSaveLoc = tempAFTLocation+page.get_physFileId();
			        	String locator = page.get_fileAftLocator();
			        	if(aftClient.getConnection() != null){
			        		System.out.println("Connection Not Null");
			        	}
			        	else{
			        		System.out.println("Connection is NULL");
			        	}
			        		
			        	if (AFTData.SUCCESS == aftClient.fileExists(locator)){
			        		System.out.println(locator + " FOUND");
			        	}else{
			        		System.out.println(locator + " NOT FOUND");
			        	}
			        	aftClient.closeConnection();
			        	*/
			        	/*
			        	int iRtrn = aftClient.retrieveDocument(page.get_fileAftLocator(),aftSaveLoc);
			        	page.set_aftRtrnCd(iRtrn);
			        	
			        	aftClient.closeConnection();
			        	if(iRtrn == 0){
			        		Snow.Snowbnd snow = new Snow.Snowbnd();
			        		
			        		int imageType = snow.IMGLOW_get_filetype(aftSaveLoc);
			        		String extn = sbFileTypes.GetFileExtention(imageType);
			        		page.set_sbFileFormat(extn);
			        		
			        	}
			        	System.out.println(page.toString());
			        	
		        }
		        */
		       }
			} catch (IOException e) {
		        System.out.println(e.getMessage());
		    } catch (SAXException e) {
		    	System.out.println(e.getMessage());
			} catch (ParserConfigurationException e) {
				System.out.println(e.getMessage());
			} catch (AWDXMLException e) {
				System.out.println(e.getMessage());
			}
		/*	
		catch (IPException e) {
				System.out.println(e.toString());
			} catch (AFTException e) {
				System.out.println(e.toString());
			}
			*/ 
			//catch (SnowBoundFileTypeException e) {
			//	System.out.println(e.toString());
			//}
		
	}
}
