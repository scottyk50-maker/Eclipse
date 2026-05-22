package com.dstsystems.bps.destlocaft.config;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

import com.dstsystems.bps.aft.AFTConfig;
import com.dstsystems.bps.cfw.pub.config.CFWBaseConfig;
import com.dstsystems.bps.cfw.pub.exceptions.ShutdownException;
import com.dstsystems.bps.cipher.AESCipher;

@XmlRootElement
@XmlType(
    //factoryClass=SampleConfigFactory.class,
    factoryMethod="CreateConfig")
public class DestLocAFTConfig extends CFWBaseConfig{

	static Logger logger = Logger.getLogger(DestLocAFTConfig.class.getName());
	
	public static DestLocAFTConfig CreateConfig() {
	      return new DestLocAFTConfig();
	}
	
	public List<AFTConfig> get_aftConfigList() {
		return _aftConfigList;
	}

	@XmlElementWrapper(name="aftServers")
	@XmlElement(name="aftServer")
	public void set_aftConfigList(List<AFTConfig> _aftConfigList) {
		this._aftConfigList = _aftConfigList;
	}

	public String get_dasdStartPath() {
		return _dasdStartPath;
	}

	public void set_dasdStartPath(String _dasdStartPath) {
		this._dasdStartPath = _dasdStartPath;
	}

	public String get_jdbcDriver() {
		return _jdbcDriver;
	}

	public void set_jdbcDriver(String _jdbcDriver) {
		this._jdbcDriver = _jdbcDriver;
	}

	public String get_jdbcURL() {
		return _jdbcURL;
	}

	public void set_jdbcURL(String _jdbcURL) {
		this._jdbcURL = _jdbcURL;
	}

	public String get_dbUserId() {
		return _dbUserId;
	}

	public void set_dbUserId(String _dbUserId) {
		this._dbUserId = _dbUserId;
	}

	public String get_dbPwd() {
		AESCipher cipher;
		String pwd;
		
		try 
		{
			cipher = new AESCipher();
			pwd =  cipher.decrypt(_dbPwd);
		} 
		catch (Exception e1) 
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

	public void set_dbPwd(String _dbPwd) {
		this._dbPwd = _dbPwd;
	}

	public String get_spRtrvDefiniton() {
		return _spRtrvDefiniton;
	}

	public void set_spRtrvDefiniton(String _spRtrvDefiniton) {
		this._spRtrvDefiniton = _spRtrvDefiniton;
	}

	public String get_spUpdtDefiniton() {
		return _spUpdtDefiniton;
	}

	public void set_spUpdtDefiniton(String _spUpdtDefiniton) {
		this._spUpdtDefiniton = _spUpdtDefiniton;
	}

	public String get_schema() {
		return _schema;
	}

	public void set_schema(String _schema) {
		this._schema = _schema;
	}

	public int get_batchId() {
		return _batchId;
	}

	public void set_batchId(int _batchId) {
		this._batchId = _batchId;
	}

	public boolean is_useOracleCursor() {
		return _useOracleCursor;
	}

	public void set_useOracleCursor(boolean _useOracleCursor) {
		this._useOracleCursor = _useOracleCursor;
	}
	
	public String get_processFlag() {
		return _processFlag;
	}

	public void set_processFlag(String _processFlag) {
		this._processFlag = _processFlag;
	}
	
	public String get_spGetDestIdDefinition() {
		return _spGetDestIdDefinition;
	}

	public void set_spGetDestIdDefinition(String _spGetDestIdDefinition) {
		this._spGetDestIdDefinition = _spGetDestIdDefinition;
	}
	
	public boolean is_enableAFTLogging() {
		return _enableAFTLogging;
	}

	public void set_enableAFTLogging(boolean _enableAFTLogging) {
		this._enableAFTLogging = _enableAFTLogging;
	}
	
	public String get_spUpdDestLoc_VerifyAction() {
		return _spUpdDestLoc_VerifyAction;
	}

	public void set_spUpdDestLoc_VerifyAction(String _spUpdDestLoc_VerifyAction) {
		this._spUpdDestLoc_VerifyAction = _spUpdDestLoc_VerifyAction;
	}
	
	public static void CreateXML(){
		DestLocAFTConfig config = new DestLocAFTConfig();
		//Framework inherited configuration items
		config.set_license("U2Wi4UK4OXWHHGstW+XRiw==");
		config.set_providerClass("com.dstsystems.bps.convimage.ipopulateprocessitems.PopulateItemsWithDocIds");
		config.set_consumerClass("com.dstsystems.bps.convimage.iprocessor.ConvImageProcessor");
		config.set_threadCount(1);
		config.set_providerSleep(1000);
				
		/*
		 * Database Information
		*/
		config.set_jdbcDriver("oracle.jdbc.driver.OracleDriver");
		config.set_jdbcURL("jdbc:oracle:thin:@10.193.245.132:1521:awd");
		config.set_dbUserId("custom");
		config.set_dbPwd("U2Wi4UK4OXWHHGstW+XRiw==");
		config.set_spRtrvDefiniton("{call GETObjectId(?,?,?,?,?)}");
		config.set_spUpdtDefiniton("{call insertSrvGetCollResults(?,?,?)}");
		
		
		/*
		 * AFTConfig Information 
		*/
		
		config.set_dasdStartPath("_dasdStartPath");
		
		ArrayList<AFTConfig> aftList = new ArrayList<AFTConfig>();
		for(int i=1;i<=3;i++){
			String host = String.format("Host_%d", i);
			String destId = String.format("DestId_%d", i);
			AFTConfig aftConfig = new AFTConfig(host, i,destId,1,1);
			aftList.add(aftConfig);
		}
		config.set_aftConfigList(aftList);
		
		JAXBContext jaxbContext;
		try {
			jaxbContext = JAXBContext.newInstance(DestLocAFTConfig.class);
			Marshaller marshaller = jaxbContext.createMarshaller();
	        marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
	        marshaller.marshal(config, System.out);
	       
		} catch (JAXBException e) {
			e.printStackTrace();
		}
	}
	
	public static void ValidateXML(final String configPath){
		JAXBContext jaxbContext = null;
		File file = null;//= new File("config.xml");
		try {
			file = new File(configPath);
			if(!file.isFile()){
				String msg = String.format("Failed to load ConfigFile <%s>", configPath);
				logger.log(Level.SEVERE, msg);
				throw new ShutdownException(msg);
			}
					jaxbContext = JAXBContext.newInstance(DestLocAFTConfig.class);
			Unmarshaller jaxbUnmarshaller = jaxbContext.createUnmarshaller();
			DestLocAFTConfig config= (DestLocAFTConfig) jaxbUnmarshaller.unmarshal(file);
			System.out.println(config.toString());
		} catch (JAXBException  e) {
			System.out.println(e.getMessage());
			
		} catch(Exception e){
			System.out.println(e.getMessage());
		}
	}
	
	public static void main(String[] args) {
		
		boolean createXML = false;
		String xmlPath = "./config/DestLocAFT.xml";
		
		if(createXML){
			CreateXML();
		}
		else{
			ValidateXML(xmlPath);
		}
		
		
	}
	
	public String get_dbType() {
		return _dbType;
	}

	public void set_dbType(String _dbType) {
		this._dbType = _dbType;
	}

	public int get_provRetrieveCnt() {
		return _provRetrieveCnt;
	}

	public void set_provRetrieveCnt(int _provRetrieveCnt) {
		this._provRetrieveCnt = _provRetrieveCnt;
	}
	
	public String get_schemaTableNm() {
		return _schemaTableNm;
	}

	public void set_schemaTableNm(String _schemaTableNm) {
		this._schemaTableNm = _schemaTableNm;
	}
	
	public String get_trRetrievePath() {
		return _trRetrievePath;
	}

	public void set_trRetrievePath(String _trRetrievePath) {
		this._trRetrievePath = _trRetrievePath;
	}
	
		
	@Override
	public String toString() {
		return String
				.format("DestLocAFTConfig [_dasdStartPath=%s, _jdbcDriver=%s, _jdbcURL=%s, _dbUserId=%s, _dbPwd=%s, _spRtrvDefiniton=%s, _spUpdtDefiniton=%s, _schema=%s, _batchId=%s]",
						_dasdStartPath, _jdbcDriver, _jdbcURL, _dbUserId,
						_dbPwd, _spRtrvDefiniton, _spUpdtDefiniton, _schema,
						_batchId);
	}
	
	
	
	
	/*
	 * AFT Settings Settings
	*/
	private List<AFTConfig> _aftConfigList;
	
	/*
	 * AFT Information
	*/
	private String _dasdStartPath;
	
	
	/*
	 * Database Information
	*/
	private String _dbType;
	private String _schemaTableNm;
	private String _jdbcDriver;
	private String _jdbcURL;
	private String _dbUserId;
	private String _dbPwd;
	
	
	private boolean _useOracleCursor;
	private String _spRtrvDefiniton;
	private String _spUpdtDefiniton;
	private String _spGetDestIdDefinition;
	private String _spUpdDestLoc_VerifyAction;
	
	/*
	* Processing Information
	*/
	private String _schema;
	private int	_batchId;
	private String _processFlag;
	private int _provRetrieveCnt;
	private int _fileActionFlags;
	private String _sbConfigPath;
	private String _trRetrievePath;
	
	private boolean _enableAFTLogging;

	

	

	

	
	

	
	
}
