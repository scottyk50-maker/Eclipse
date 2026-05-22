package com.dstsystems.bps.convdb.test;

import java.io.File;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;

import com.dstsystems.bps.convdb.database.ConvDB;



public class ConnectTest {
	static Logger logger = Logger.getLogger(ConnectTest.class.getName());
	
	public static void main(String[] args) {
		logger.log(Level.INFO,"Starting Connection Test");
		
		if(args.length !=1){
			logger.log(Level.SEVERE,"ConnectTest <TestDBConfig file Path>");
			return;
		}
		
		ConnectTest ct = new ConnectTest();
		logger.log(Level.INFO,"Starting Test for " + args[0]);
		
		String msg = ct.DBConnectionTest(args[0])?"Test Passed":"Test Failed";
		
		logger.log(Level.INFO,msg);
	}
	
	
	public boolean DBConnectionTest(final String configPath){
		boolean bRtrn = false;
		TestDBConfig config = LoadConfiguration(configPath);
		
		if(config == null){
			logger.log(Level.SEVERE,"TestDBConfig is null");
					return false;
		}
		
		String msg = String.format("\njdbcDriver<%s>\njdbcURL<%s>\ndbUser<%s>\ndbPwd<%s>", config.get_jdbcDriver(), config.get_jdbcURL(), config.get_dbUserId(), config.get_dbPwd() );
		logger.log(Level.INFO, msg);
		ConvDB db = null;
		try{
			db = new ConvDB();
			db.Connect(config.get_jdbcDriver(), config.get_jdbcURL(), config.get_dbUserId(), config.get_dbPwd());
			db.Disconnect();
			bRtrn = true;
		}
		catch(Exception e){
			logger.log(Level.SEVERE,"Failed to Connection to DB\n" + e.getMessage());
			bRtrn = false;
		}
		finally{
			if( db != null){
				db.Disconnect();
			}
			db= null;
		}
		
		
		return bRtrn;
	}
	
	private TestDBConfig LoadConfiguration(final String configPath){
		JAXBContext jaxbContext = null;
		File file = null;
		TestDBConfig config = null;
		try {
			file = new File(configPath);
			if(!file.isFile()){
				String msg = String.format("Failed to load ConfigFile, Path not a File <%s>", configPath);
				logger.log(Level.SEVERE, msg);
				return null;
			}
		
			jaxbContext = JAXBContext.newInstance(TestDBConfig.class);
			
			Unmarshaller jaxbUnmarshaller = jaxbContext.createUnmarshaller();
			config= (TestDBConfig) jaxbUnmarshaller.unmarshal(file);
			
		} catch (JAXBException  e) {
			logger.log(Level.SEVERE,"Failed to Load Configuration Class: " + configPath);
			logger.log(Level.SEVERE,e.getMessage());
			config = null;
		} catch(Exception e){
			logger.log(Level.SEVERE,"Unhandled Exception, Failed to Load Configuration Class: " + configPath);
			logger.log(Level.SEVERE,e.getMessage());
			config = null;
		}
		return config;
		
		
	}
}
