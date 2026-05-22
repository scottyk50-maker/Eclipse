package com.dstsystems.bps.cfw.config;

import java.io.File;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import com.dstsystems.bps.cipher.AESCipher;
import com.dstsystems.bps.cipher.exception.keyPhraseException;


@XmlRootElement(name="FrameConfig")
public class FrameXMLConfig {
	static Logger logger = Logger.getLogger(FrameXMLConfig.class.getName());
	public FrameXMLConfig(){
		
	}
	
	public String get_jdbcDriver() {
		return _jdbcDriver;
	}

	@XmlElement
	public void set_jdbcDriver(String _jdbcDriver) {
		this._jdbcDriver = _jdbcDriver;
	}

	
	public String get_jdbcURL() {
		return _jdbcURL;
	}
	
	@XmlElement
	public void set_jdbcURL(String jdbcURL) {
		this._jdbcURL = jdbcURL;
	}

	public String get_dbUserId() {
		return _dbUserId;
	}
	
	@XmlElement
	public void set_dbUserId(String userId) {
		this._dbUserId = userId;
	}

	public String get_dbPwd() {
		AESCipher cipher;
		String pwd;
		
		try 
		{
			cipher = new AESCipher();
			pwd =  cipher.decrypt(_dbPwd);
		} 
		catch (IOException | keyPhraseException e1) 
		{
			pwd = _dbPwd;
			logger.log( Level.SEVERE, "Failed to decrypt database Password\n" + e1.getMessage() );
		}
		finally
		{
			cipher = null;
		}
		
		return pwd;
		
		
	}

	@XmlElement
	public void set_dbPwd(String dbPwd) {
		this._dbPwd = dbPwd;
	}

	public String get_configSchema() {
		return _configSchema;
	}

	@XmlElement
	public void set_configSchema(String configSchema) {
		this._configSchema = configSchema;
	}

	public String get_configTable() {
		return _configTable;
	}

	@XmlElement
	public void set_configTable(String configTable) {
		this._configTable = configTable;
	}
	
	public String get_license() {
		AESCipher cipher;
		String license;
		
		try 
		{
			cipher = new AESCipher("FRAMEWORK");
			license =  cipher.decrypt(_license);
		} 
		catch (Exception e1) 
		{
			license = _license;
			logger.log( Level.SEVERE, "Failed to decrypt license key");
		}
		finally
		{
			cipher = null;
		}
		
		return license;
	}

	@XmlElement
	public void set_license(String license) {
		this._license = license;
	}

	
	public int get_configId(){
		return _configId;
	}
	
	@XmlElement
	public void set_configId(int configId){
		this._configId = configId;
	}

	public static void main(String[] args) {
		 
		FrameXMLConfig config= new FrameXMLConfig();
		config.set_jdbcDriver("com.ibm.as400.access.AS400JDBCDriver");
		config.set_jdbcURL("jdbc:as400://10.193.204.19;naming=system;errors=full");
	    config.set_dbUserId("DT40903");
	    config.set_dbPwd("p1e4vIsFCMxe3vuIeMrMwQ==");
	    config.set_configSchema("DT40903");
	    config.set_configTable("CONFIG_DATA");
	    config.set_license("RDMbOXUjtToW6SOx7NxrNg==");
	    config.set_configId(1);
	    
	    		
		
		
	    
	  try {
 
		File file = new File("./config/sample.xml");
		JAXBContext jaxbContext = JAXBContext.newInstance(FrameXMLConfig.class);
		Marshaller jaxbMarshaller = jaxbContext.createMarshaller();
 
		// output pretty printed
		jaxbMarshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
 
		jaxbMarshaller.marshal(config, file);		
		jaxbMarshaller.marshal(config, System.out);
 
	      } catch (JAXBException e) {
		e.printStackTrace();
	      } 
	}
	
	private String _jdbcDriver;
	private String _jdbcURL;
	private String _dbUserId;
	private String _dbPwd;
	private String _configSchema;
	private String _configTable;
	private String _license;
	private int _configId;


}
