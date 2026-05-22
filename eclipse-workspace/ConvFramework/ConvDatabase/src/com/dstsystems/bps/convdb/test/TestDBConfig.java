package com.dstsystems.bps.convdb.test;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;


@XmlRootElement
public class TestDBConfig {

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
	public void set_jdbcURL(String _jdbcURL) {
		this._jdbcURL = _jdbcURL;
	}
	public String get_dbUserId() {
		return _dbUserId;
	}
	
	@XmlElement
	public void set_dbUserId(String _dbUserId) {
		this._dbUserId = _dbUserId;
	}
	public String get_dbPwd() {
		return _dbPwd;
	}
	
	@XmlElement
	public void set_dbPwd(String _dbPwd) {
		this._dbPwd = _dbPwd;
	}

	public static void main(String[] args) {
		TestDBConfig config = new TestDBConfig();
		config.set_dbUserId("DT40903");
		config.set_dbPwd("Password");
		config.set_jdbcURL("_jdbcURL");
		config.set_jdbcDriver("_jdbcDriver");
		
		JAXBContext jaxbContext;
		try {
			jaxbContext = JAXBContext.newInstance(TestDBConfig.class);
			Marshaller marshaller = jaxbContext.createMarshaller();
	        marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
	        marshaller.marshal(config, System.out);
		} catch (JAXBException e) {
			e.printStackTrace();
		}
	}
	
	private String _jdbcDriver;
	private String _jdbcURL;
	private String _dbUserId;
	private String _dbPwd;
}
