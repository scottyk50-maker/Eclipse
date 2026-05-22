package com.dstsystems.bps.cfw.pub.config;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlTransient;
import javax.xml.bind.annotation.XmlType;

import com.dstsystems.bps.cfw.pub.interfaces.IConfig;
import com.dstsystems.bps.cipher.AESCipher;

@XmlTransient
@XmlType(factoryMethod="CreateCFWBaseConfig")
public class CFWBaseConfig  implements IConfig{
	
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
			//logger.log( Level.SEVERE, "Failed to decrypt license key");
		}
		finally
		{
			cipher = null;
		}
		
		return license;
	}

	@XmlElement
	public void set_license(String _license) {
		this._license = _license;
	}
	
	public int get_threadCount() {
		return _threadCount;
	}
	
	@XmlElement
	public void set_threadCount(int _threadCount) {
		this._threadCount = _threadCount;
	}
	public long get_providerSleep() {
		return _providerSleep;
	}
	
	@XmlElement
	public void set_providerSleep(long _providerSleep) {
		this._providerSleep = _providerSleep;
	}
	public String get_providerClass() {
		return _providerClass;
	}
	
	@XmlElement
	public void set_providerClass(String _providerClass) {
		this._providerClass = _providerClass;
	}
	public String get_consumerClass() {
		return _consumerClass;
	}
	
	@XmlElement
	public void set_consumerClass(String _consumerClass) {
		this._consumerClass = _consumerClass;
	}
		
	public static CFWBaseConfig CreateCFWBaseConfig(){
		return new CFWBaseConfig();
	}
	
	public boolean is_endNoRecFound() {
		return _endNoRecFound;
	}

	@XmlElement
	public void set_endNoRecFound(boolean endNoRecFound) {
		this._endNoRecFound = endNoRecFound;
	}
	
	private int _threadCount;
	private long _providerSleep;
	private String _providerClass;
	private String _consumerClass;
	private String _license;
	private boolean _endNoRecFound;
	
	
}
