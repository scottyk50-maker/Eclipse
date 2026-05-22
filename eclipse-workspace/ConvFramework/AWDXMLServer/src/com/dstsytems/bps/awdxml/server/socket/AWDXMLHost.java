package com.dstsytems.bps.awdxml.server.socket;

import java.io.BufferedOutputStream;
import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.StringReader;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.xml.sax.InputSource;

import com.dstsytems.bps.awdxml.exceptions.AWDXMLException;
import com.dstsytems.bps.awdxml.srvgetcoll.AWDCollection;
import com.dstsytems.bps.awdxml.srvgetcoll.AWDPage;
import com.dstsytems.bps.awdxml.srvgetcoll.SRVGetColl;
import com.dstsytems.bps.awdxml.srvgetcoll.SRVGetCollHandler;

public class AWDXMLHost {
	static Logger logger = Logger.getLogger(AWDXMLHost.class.getName());
	public void Connect(final String hostName, final int port) throws AWDXMLException{
		try {
			_sock = new Socket(hostName, port);
			_do = new DataOutputStream(new BufferedOutputStream(_sock.getOutputStream()));
			_di = new DataInputStream(_sock.getInputStream());
		} catch (UnknownHostException e) {
			Cleanup();
			String msg = String.format("UnknownHostException: Host<%s> Port<%d>",hostName,port);
			throw new AWDXMLException(msg);
		} catch (IOException e) {
			Cleanup();
			String msg = String.format("Connect Failed, Error creating Streams for HOST<%s> PORT<%d>",hostName,port);
			throw new AWDXMLException(msg);
		}
		catch(Exception e){
			Cleanup();
			String msg = String.format("Connect Failed, Unknown Exception for HOST<%s> PORT<%d>",hostName,port);
			logger.log(Level.SEVERE, msg + e.getMessage());
			throw new AWDXMLException(msg);
		}
	}
	
	public void Close(){
		Cleanup();
	}
	
	private void Cleanup(){
		if(_sock != null){
			try{
				_sock.close();
			}
			catch(Exception e){}
			finally{
				_sock = null;
				_do = null;
				_di = null;
			}
		}
	}
	
	public byte[] sendRequest(final byte request[])throws AWDXMLException{
		if(_sock == null || _do == null || request == null || request.length <1){
			String msg = "AWDXMLHost Not Intialized, must call Connect method first";
			logger.log(Level.SEVERE, msg);
			throw new AWDXMLException(msg);
		}
		try {
			_do.writeInt(1);
			_do.writeInt(request.length);
			_do.writeInt(0);
			_do.write(request);
			_do.flush();
			_di.readInt();
			
			int respLen = _di.readInt();
			int iDataLen = _di.readInt();
			
			byte resp[] = new byte[respLen];
			_di.readFully(resp);
			if(iDataLen > 0)
			{
			   byte tmp[] = new byte[iDataLen];
			   _di.readFully(tmp);
			   tmp = null;
			}   
			return resp;
		} catch (IOException e) {
			String msg = "Failed to send Request" + request.toString();
			throw new AWDXMLException(msg);
		}
		catch(Exception e){
			String msg = String.format("Unknown Exception occurred while sending Request:\n %s",request.toString());
			logger.log(Level.SEVERE, msg + "\n" + e.getMessage());
			throw new AWDXMLException(msg);
		}
	}
	
	 
	 
	public static void main(String[] args) {
		//String request ="<DST xml:lang=\"en-us\"><jobName version=\"1.0\">SRVGetColl</jobName><trace>0</trace><readable>Y</readable><AWD><userID>DT40903</userID><password>QG40903</password><source id=\"2001-01-01-00.00.00.000000O01\"><collection id=\"LFC015849706\"></collection></source></AWD></DST>";
		String host = "efsjhann.dstcorp.net";
		int port =20021;
		String xmlRequest = SRVGetColl.GetRequestString("DT40903","DT40903", "2014-10-12-15.52.04.805709","O","01","LFC015849706");
		
		AWDXMLHost xml = new AWDXMLHost();
		try{
			xml.Connect(host, port);
			
			byte result[] = xml.sendRequest(xmlRequest.getBytes());
			if(result != null ){
				String sResult = new String(result);
				logger.log(Level.INFO, sResult);
				SAXParserFactory saxParserFactory = SAXParserFactory.newInstance();
				
		        SAXParser saxParser = saxParserFactory.newSAXParser();
		        SRVGetCollHandler handler = new SRVGetCollHandler();
		        
		        saxParser.parse(new InputSource(new ByteArrayInputStream(result)), handler);
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
		        }
			}
			else{
				logger.log(Level.SEVERE, "Result is null");
			}
			
		}
		catch(Exception e){
			logger.log(Level.SEVERE, e.getMessage());
		}
		finally{
			xml.Close();
		}
		
	}
	
	private Socket           _sock = null;
    private DataOutputStream _do = null;
    private DataInputStream  _di = null;

}
