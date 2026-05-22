package com.dstsystems.bps.destlocaft.iprocessor;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;




import com.dstsystems.bps.aft.AFTConfig;
import com.dstsystems.bps.aft.AFTMsg;
import com.dstsystems.bps.aft.ConvAFT;
import com.dstsystems.bps.cfw.pub.exceptions.RestartConsumerException;
import com.dstsystems.bps.cfw.pub.exceptions.ShutdownException;
import com.dstsystems.bps.cfw.pub.interfaces.IConfig;
import com.dstsystems.bps.cfw.pub.interfaces.IProcessItem;
import com.dstsystems.bps.cfw.pub.interfaces.IProcessor;
import com.dstsystems.bps.destlocaft.config.DestLocAFTConfig;
import com.dstsystems.bps.destlocaft.database.DestLocDAO;
import com.dstsystems.bps.destlocaft.iprocessitem.DestLocProcessItem;
import com.dstsystems.bps.exceptions.AFT_FW_Exception;


public class DestLocAFTProcessor implements IProcessor{
	static Logger logger = Logger.getLogger(DestLocAFTProcessor.class.getName());

	@Override
	public void StartupInitialize(IConfig config) throws ShutdownException {
		_config = (DestLocAFTConfig)config;
		_dao = new DestLocDAO(	_config.get_dbType(), 	_config.get_jdbcDriver(),
				_config.get_jdbcURL(), 	_config.get_dbUserId(),
				_config.get_dbPwd(), 	_config.get_provRetrieveCnt(),
				_config.get_schemaTableNm());

		LoadAFTConfigMap();
		ValidateDBDestIDValues();
	}
	
	private void LoadAFTConfigMap() throws ShutdownException{
		List<AFTConfig> aftConfigList = (List<AFTConfig>) _config.get_aftConfigList();
		
		_aftConfigMap = new HashMap<String, AFTConfig>();
		for(AFTConfig aftConfig:aftConfigList){
			_aftConfigMap.put(aftConfig.get_DestId(), aftConfig);
		}
		ValidateDBDestIDValues();
	}
	
	private void ValidateDBDestIDValues() throws ShutdownException{
		String msg;

		try {
			ArrayList<String> dbDestIdList = _dao.GetDestIdList();
			
			for(String destId:dbDestIdList){
				if(!_aftConfigMap.containsKey(destId)){
					msg = String.format("Failed to find AFT::DestinationId<%s> in aftConfigMap", destId);
					throw new ShutdownException(msg);
				}
			}
		}
		catch(ShutdownException e){
			throw e;
		}
		catch(Exception e){
			msg = "Unknown Exception Occured in ValidateDBDestIDValues";
			logger.log( Level.SEVERE, msg);
			logger.log( Level.SEVERE, e.getMessage() );
			throw new ShutdownException(msg);
		}
	}

	

	@Override
	public void ShutdownCleanup() {
		_dao.Disconnect();
		_dao = null;
		_config = null;
	}

	@Override
	public void ProcessQueueObject(IProcessItem processItem)
			throws ShutdownException, RestartConsumerException {
		
		DestLocProcessItem pi = (DestLocProcessItem)processItem;
		
		logger.log( Level.INFO, "Processing " + pi.get_fromLocator());
		String action = pi.get_actionFlag().toUpperCase();
		switch(action){
			
			case EXISTS: /*Exists*/ 
			case DELETE: /*Delete*/
			case RETRIEVE: /*Retrieve*/
			case STORE: /*Store*/
			case VERIFY: /*Verify*/

				logger.log(Level.INFO,String.format("Action <%s> PhysFileId<%s>", action,pi.get_physFileId()));
				VDRS_Action(pi, action,true);
				break;
			
			case MIGRATE: /*Migrate From one AFT to another*/
				logger.log(Level.INFO,"Migrate Action PhysFileId: " + pi.get_physFileId());
				MigrateAction(pi);
				break;
			
			default:
				String msg = String.format("Action <%s> Not Supported for PhysFileID <%s>",action,pi.get_physFileId());
				logger.log(Level.SEVERE,msg);
				pi.set_frmRespCode(-99);
				pi.set_frmRespMsg(msg);
				DefaultTime(pi);
				VDRSActDBUpdate(pi);
				break;		
		}
		
	}
	
	private AFTConfig GetAFTConfig(final String destId){
		AFTConfig config = null;
		if(_aftConfigMap.containsKey(destId)){
			config = _aftConfigMap.get(destId);
		}
		return config;
	}
	
	
	private void MigrateAction(DestLocProcessItem pi) throws ShutdownException, RestartConsumerException{
	
		String aftRtrvPath = pi.get_fromLocator();
		String aftStorePath = pi.get_toLocator();
		String aftRtrvDestId = pi.get_fromDestId();
		String aftStoreDestId = pi.get_toDestId();
		
		DestLocProcessItem wrkProcItem = new DestLocProcessItem();
		/*
		 *Local Working path for migrated file 
		*/
		String wrkFilePath = GetLocalWokringPath(pi);
		
		wrkProcItem.set_fromDestId(aftRtrvDestId);
		wrkProcItem.set_fromLocator(aftRtrvPath);
		wrkProcItem.set_toLocator(wrkFilePath);
		pi.set_frmBegTsNow();		
		VDRS_Action(wrkProcItem, RETRIEVE, false);
		
		/*
		 *Get the values from the retrieve 
		 */
		pi.set_frmRespCode(wrkProcItem.get_frmRespCode());
		pi.set_frmRespMsg(wrkProcItem.get_frmRespMsg());
		pi.set_frmCheckSum(wrkProcItem.get_frmCheckSum());
		pi.set_frmFileSize(wrkProcItem.get_frmFileSize());
		pi.set_snowBndFrmtCode(wrkProcItem.get_snowBndFrmtCode());
		pi.set_frmEndTsNow();
		
		/*
		 *Set the Store Values 
		 */
		if(AFTMsg.SUCCESS == pi.get_frmRespCode()){
			wrkProcItem.set_fromDestId(aftStoreDestId);
			wrkProcItem.set_fromLocator(wrkFilePath);
			wrkProcItem.set_toDestId(aftStoreDestId);
			wrkProcItem.set_toLocator(aftStorePath);
			pi.set_toBegTsNow();
			VDRS_Action(wrkProcItem, STORE,false);
			DeleteLocalFile(wrkFilePath);
		}
		else{
			pi.set_toBegTsNow();
			wrkProcItem.set_toRespCode(AFTMsg.CUSTOM_ERROR);
			wrkProcItem.set_toRespMsg("Retrieve Failed, Cann't Perform Store");
			wrkProcItem.set_toFileSize(-1);
			wrkProcItem.set_toCheckSum("");
		}
				
		/*
		 *Get the values from the Store 
		*/
		pi.set_toRespCode(wrkProcItem.get_frmRespCode());
		pi.set_toRespMsg(wrkProcItem.get_frmRespMsg());
		pi.set_toFileSize(wrkProcItem.get_frmFileSize());
		pi.set_toCheckSum(wrkProcItem.get_frmCheckSum());
		
		MigrateActDBUpdate(pi);
	}

	private final String GetLocalWokringPath(final DestLocProcessItem pi){
		StringBuilder sb = new StringBuilder();
		String tempPath = _config.get_dasdStartPath().replace("\\", "/");
		sb.append(tempPath);
		if(false == tempPath.endsWith("/")){
			sb.append("/");
		}
		sb.append(pi.get_physFileId());
		return sb.toString();
	}
	
	private void VDRS_Action(DestLocProcessItem pi, final String action, final boolean dbUpdate) throws ShutdownException, RestartConsumerException{
		String msg = "";
		AFTMsg aftMsg = null;
		ConvAFT convAFT = null;
		String strSlash = "\\";
		String strLocalFilename = "";
		
		try {
			pi.set_frmBegTsNow();
			
			convAFT = GetAFTConnection(pi.get_fromDestId());
			/*
			 * GetAFTConnection Should throw exception but lets check for it to be sure
			*/
			if(convAFT == null){
				
				msg = "Failed to Find AFTConfig: " + pi.get_fromDestId();
				throw new RestartConsumerException(msg);
			}else{
				
				if(action.equalsIgnoreCase(EXISTS)){
					aftMsg = convAFT.VerifyDocument(pi.get_fromLocator());
				}
				else if(action.equalsIgnoreCase(DELETE)){
					aftMsg = convAFT.DeleteDocument(pi.get_fromLocator());
				}
				else if(action.equalsIgnoreCase(RETRIEVE)){
					aftMsg = convAFT.RetrieveDocument(pi.get_fromLocator(), pi.get_toLocator());
				}
				else if(action.equalsIgnoreCase(VERIFY)){
//					aftMsg = convAFT.RetrieveDocument(pi.get_fromLocator(), _config.get_trRetrievePath());
					if (_config.get_trRetrievePath().endsWith("\\")) {
						strSlash = "";
					} 
					strLocalFilename = String.format("%s%s%s_%s", _config.get_trRetrievePath(), strSlash, pi.get_physFileId(), pi.get_fromDestId() );
					aftMsg = convAFT.RetrieveDocument(pi.get_fromLocator(), strLocalFilename);	
					DeleteLocalFile(strLocalFilename);					
				}
				else if(action.equalsIgnoreCase(STORE)){
					aftMsg = convAFT.StoreDocument(pi.get_fromLocator(), pi.get_toLocator());
				}
				else{
					aftMsg = new AFTMsg();
					msg = "Invalid Action Code: " + action;
					aftMsg.SetCustomErr(msg);
					DefaultTime(pi);
				}
												
				int rtnCode = aftMsg.GetRtrnCd();
				
				convAFT.Close();
				convAFT = null;
				
				switch(rtnCode){
					case AFTMsg.SHUTDOWN_ERROR:
						throw new ShutdownException(aftMsg.GetAFTMsg());
					case AFTMsg.RESTART_ERROR:
						throw new RestartConsumerException(aftMsg.GetAFTMsg());
					default:
						pi.set_frmRespCode(rtnCode);
						pi.set_frmRespMsg(aftMsg.GetAFTMsg());
						pi.set_frmEndTsNow();
						pi.set_frmCheckSum(aftMsg.get_checksum());
						pi.set_frmFileSize(aftMsg.get_fileSize());
						pi.set_snowBndFrmtCode(aftMsg.get_imageFormat());
						pi.set_frmEndTsNow();
				}
				
				/*
				 * Added no DB Update passthrough so the Migrate Action can perform it's own DB Update
				 */
				if(dbUpdate){
					VDRSActDBUpdate(pi);	
				}
			}
		} catch (AFT_FW_Exception e) {
			throw new RestartConsumerException(e);
		}catch(Exception e){
			if (aftMsg.GetRtrnCd() == AFTMsg.RESTART_ERROR) {
				throw new RestartConsumerException(e);			
			}
			else 
			{
				if(convAFT != null){
					convAFT.Close();
					convAFT = null;
				}
				msg = String.format("Unknown Exception in VDRS_Action Action<%s> PhysFileId<%s>", action, pi.get_physFileId());
				logger.log(Level.SEVERE,msg);
				logger.log(Level.SEVERE, e.getMessage());
				throw new ShutdownException(msg);
			}
		}finally {
		}
	}
	
	private ConvAFT GetAFTConnection(final String destId) throws AFT_FW_Exception{
		ConvAFT convAFT = null;
		AFTConfig aftConfig = GetAFTConfig(destId);
		if(aftConfig == null){
			String msg = "Failed to find aftConfig for DestId: " + destId;
			throw new AFT_FW_Exception(msg);
		}else{
			try {
				convAFT = new ConvAFT(_config.is_enableAFTLogging(), aftConfig);
			} catch (AFT_FW_Exception e) {
				String msg = "Failed to Create ConvAFT: " + aftConfig.toString();
				logger.log(Level.SEVERE, msg);
				throw e;
			}	
		}
		
		return convAFT;
	}
	
	
	private void DefaultTime(DestLocProcessItem pi){
		pi.set_frmBegTsNow();
		pi.set_frmEndTsNow();
		pi.set_toBegTsNow();
		pi.set_toEndTsNow();
		
	}
	
	private void MigrateActDBUpdate(final DestLocProcessItem pi) throws ShutdownException{
try {
			
			_dao.MigrateActDBUpdate(pi);
		} catch (ShutdownException e) {
			throw e;
		}
		catch(Exception e){
			String msg = "Unknown Exception Occured in MigrateActDBUpdate for " + pi.toString();
			logger.log( Level.SEVERE, msg);
			logger.log( Level.SEVERE, e.getMessage());
			throw new ShutdownException(msg);
		}
	}
	
	private void VDRSActDBUpdate(final DestLocProcessItem pi) throws ShutdownException{
		
		
		try {
			
			_dao.VDRS_DBUpdate(pi);
		} catch (ShutdownException e) {
			throw e;
		}
		catch(Exception e){
			String msg = "Unknown Exception Occured in VDRSActDBUpdate for " + pi.toString();
			logger.log( Level.SEVERE, msg);
			logger.log( Level.SEVERE, e.getMessage());
			throw new ShutdownException(msg);
		}
	}
	
	private void DeleteLocalFile(String filePath) throws ShutdownException{
		File file = null;
		String msg;
		try{
			
//			file = new File(filePath);
//			if(file.isFile() == true)
//			{
//				if(!file.delete()){
//					msg = "Failed to delete local temp file: " + filePath;
//					throw new ShutdownException(msg);
//				}
//			}

			Path fileToBeDeleted = Paths.get(filePath);
			if (!Files.deleteIfExists(fileToBeDeleted))
			{
				msg = "Failed to delete local temp file: " + filePath;
				throw new ShutdownException(msg);
			}
		}catch(NullPointerException e){
			file = null;
			logger.log(Level.SEVERE, e.getMessage());
			msg = "Failed to delete local temp file: " + filePath;
			throw new ShutdownException(msg);
		}
		catch(Exception e){
			file = null;
			logger.log(Level.SEVERE, e.getMessage());
			msg = "Failed to delete local temp file: " + filePath;
			throw new ShutdownException(msg);
		}
		finally{
			file = null;
		}
	}
	
	/*
	private void CloseAFTConnection(AFTClient aft){
		if(aft != null){
			try {aft.closeConnection();} catch (Exception e) {}
		}
	}
	*/
	
	private DestLocAFTConfig _config;
	private HashMap<String, AFTConfig> _aftConfigMap;
	//private ConvDB _db;
	private DestLocDAO _dao;
	private final static String EXISTS="E";
	private final static String DELETE = "D";
	private final static String RETRIEVE = "R";
	private final static String VERIFY = "V";
	private final static String STORE = "S";
	private final static String MIGRATE = "M";
}
