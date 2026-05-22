package com.dstsystems.bps.aft;

import java.io.File;
import java.nio.file.Paths;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.dstsystems.awd.aft.AFTClient;
import com.dstsystems.awd.aft.AFTException;
import com.dstsystems.awd.aft.IPException;
import com.dstsystems.bps.exceptions.AFT_FW_Exception;
import com.dstsystems.convchksum.exceptions.ConvChkSumException;
import com.dstsystems.convchksum.processs.ConvCheckSum;


public class ConvAFT {
	static Logger logger = Logger.getLogger(ConvAFT.class.getName());
	
	
	public ConvAFT(final boolean enableTracing, final AFTConfig aftConfig) throws AFT_FW_Exception{
		this._enableTracing = enableTracing;
		this._aftConfig = aftConfig;
		_fileActionFlags = aftConfig.get_fileActionFlags();
		_chkAlgoIndex = aftConfig.get_chkSumAlgoIndex();
		Connect();
		InitializeSnowbound();
	}
	
	public void Connect()throws AFT_FW_Exception{
		Close();
		try {
			_aft = new AFTClient(_aftConfig.get_host(), _aftConfig.get_port());
			
			_aft.setTracing(_enableTracing);
		} catch (IPException e) {
			String msg;
			msg = String.format("Failed to create AFTClient: %s",_aftConfig.toString());
			logger.log(Level.SEVERE, msg);
			throw new AFT_FW_Exception(msg);
		}
	}
	
	public void Close(){
		if(_aft != null){
			try {
				if(_aft.isConnected()){
					_aft.closeConnection();	
				}
			} catch (Exception e) {
				String msg = String.format("Failed to Close Connection %s", e.getMessage());
				logger.log(Level.SEVERE, msg);
			}
			finally{
				_aft = null;
			}
		}
	}
	
	public AFTMsg RetrieveDocument(final String aftLocator, final String localFilePath){
		int iRtrn = 0;
		AFTMsg aftMsg= new AFTMsg();
		String msg;
		try {
			
			iRtrn = _aft.retrieveDocument(aftLocator, localFilePath);
			aftMsg.SetRtrnCd(iRtrn);
			
			/*
			 * No reason to test all of the flags if bits are turned off.
			 */
			if(_fileActionFlags > 0 && iRtrn == 0){
				ProcessActionFlags(aftMsg, localFilePath);
			}
			
		} catch (AFTException e) {

			msg = String.format("AFTException Failed to Retrieve AFTLocator<%s> localFilePath<%s>",aftLocator,localFilePath);
			
			logger.log(Level.SEVERE, msg);
			logger.log(Level.SEVERE, e.toString());
			aftMsg.SetRestartErr(msg);
		}
		catch(Exception e){
			msg = String.format("Unknown Exception: Failed to Retrieve AFTLocator<%s> localFilePath<%s>",aftLocator,localFilePath);
			logger.log(Level.SEVERE, msg);
			logger.log(Level.SEVERE, e.getMessage());
			aftMsg.SetShutdownErr(msg);
		}
		return aftMsg;
	}
	
	private void ProcessActionFlags(AFTMsg aftMsg, final String imagePath){
		// GET_FILE_SIZE = 1;  // 0001
		// GET_CHECKSUM   = 2;  // 0010
		// GET_FILE_FRMT = 4;  // 0100
		long fileSize = -1;
		String checkSum="";
		int imgFrmt = SB_NOT_PROCESSED;
		
		 if ((_fileActionFlags & GET_FILE_SIZE) == GET_FILE_SIZE){
			 fileSize = GetFileSize(imagePath);
			 aftMsg.set_fileSize(fileSize);
		 }
		 
		 if ((_fileActionFlags & GET_CHECKSUM) == GET_CHECKSUM){
			 checkSum = CalculateChecksum(imagePath);
			 aftMsg.set_checksum(checkSum);
		 }
		 
		 if ((_fileActionFlags & GET_FILE_FRMT) == GET_FILE_FRMT){
			 imgFrmt = GetSBImgFrmtCode(imagePath);
			 aftMsg.set_imageFormat(imgFrmt);
		 }
		 String msg = String.format("fileActionFlags <%d> fileSize <%d> Checksum <%s> imgFormat <%s>",_fileActionFlags,fileSize, checkSum,imgFrmt);
		 logger.log(Level.INFO, msg);
	}
	
	public void InitializeSnowbound() throws AFT_FW_Exception{
		try {
			 _snowbound = new Snow.Snowbnd();
		} catch (Exception e) {
			String msg ="Failed to initialize Snow.Snowbnd()";
			logger.log(Level.SEVERE, msg);
			logger.log(Level.SEVERE, e.getMessage());
			throw new AFT_FW_Exception(msg);
		}
		
	}
	
	private int GetSBImgFrmtCode(final String imagePath){
		int imageType = SB_ERROR;

		try {
			
			imageType = _snowbound.IMGLOW_get_filetype(imagePath);
		}	
		catch(Exception e){
		
			String msg ="Unknown Exception retrieving Snowbound Image Format: " + imagePath;
			logger.log(Level.SEVERE, msg);
			logger.log(Level.SEVERE, e.getMessage());
		}
		
		return imageType;
	}

	private String CalculateChecksum(final String imagePath){
		String chkValue = "";
		ConvCheckSum convCheckSum;
		String msg;
				
		try {
			convCheckSum = new ConvCheckSum(_chkAlgoIndex);
			chkValue = convCheckSum.CalculateCheckSum(Paths.get(imagePath));
		} catch (ConvChkSumException e) {
			chkValue = "FAILED";
			logger.log(Level.SEVERE, e.getMessage());
		}
		catch(Exception e){
			chkValue = "FAILED";
			msg = "Unknown message while calculating Checksum for " + imagePath;
			logger.log(Level.SEVERE, msg);
			logger.log(Level.SEVERE, e.getMessage());
		}
		finally{
			convCheckSum = null;
		}
		
		return chkValue;
	}
	
	private long GetFileSize(final String imagePath){
		long fileSize = SB_ERROR;
		File file = null;
		try{
			file = new File(imagePath);
			if(file.isFile()){
				fileSize = file.length();
			}
			else{
				String msg = String.format("imagePath is not a File <%s>",imagePath);
				logger.log(Level.SEVERE, msg);
				fileSize = SB_ERROR;
			}
	
		}catch(Exception e){
			String msg = String.format("File to determine file size for <%s>",imagePath);
			
			logger.log(Level.SEVERE, msg);
			logger.log(Level.SEVERE, e.toString());
			fileSize = SB_ERROR;
		}
		finally{
			file = null;
		}
		
		return fileSize;
	}
	
	public AFTMsg VerifyDocument(final String aftLocator){
		int iRtrn = 0;
		AFTMsg aftMsg= new AFTMsg();
		String msg;
		try {
			iRtrn = _aft.fileExists(aftLocator);
			aftMsg.SetRtrnCd(iRtrn);
		} catch (AFTException e) {

			msg = String.format("AFTException Failed to Verify aftLocator<%s>",aftLocator);
			logger.log(Level.SEVERE, msg);
			logger.log(Level.SEVERE, e.toString());
			aftMsg.SetShutdownErr(msg);
		}
		catch(Exception e){
			msg = String.format("Unknown Exception: Failed to Verify aftLocator<%s>",aftLocator);
			logger.log(Level.SEVERE, msg);
			logger.log(Level.SEVERE, e.getMessage());
			aftMsg.SetShutdownErr(msg);
		}
		
		return aftMsg;
	}

	public AFTMsg StoreDocument(final String localFilePath, final String aftLocator){
		int iRtrn = 0;
		AFTMsg aftMsg= new AFTMsg();
		String msg;
		try {
			
			iRtrn = _aft.storeDocument(localFilePath,aftLocator);
			aftMsg.SetRtrnCd(iRtrn);
			ProcessActionFlags(aftMsg,localFilePath);
			
		} catch (AFTException e) {
			msg = String.format("AFTException Failed to Store AFTLocator<%s> localFilePath<%s>",aftLocator,localFilePath);
			logger.log(Level.SEVERE, msg);
			logger.log(Level.SEVERE, e.toString());
			aftMsg.SetRestartErr(msg);
		}
		catch(Exception e){
			msg = String.format("Unknown Exception: Failed to Store AFTLocator<%s> localFilePath<%s>",aftLocator,localFilePath);
			logger.log(Level.SEVERE, msg);
			logger.log(Level.SEVERE, e.getMessage());
			aftMsg.SetShutdownErr(msg);
		}
		return aftMsg;
	}
	
	public AFTMsg DeleteDocument(final String aftLocator){
		int iRtrn = 0;
		AFTMsg aftMsg= new AFTMsg();
		String msg;
		try {
			
			iRtrn = _aft.deleteDocument(aftLocator);
			aftMsg.SetRtrnCd(iRtrn);
		} catch (AFTException e) {

			msg = String.format("AFTException Failed to Delete AFTLocator<%s>",aftLocator);
			
			logger.log(Level.SEVERE, msg);
			logger.log(Level.SEVERE, e.toString());
			aftMsg.SetShutdownErr(msg);
		}
		catch(Exception e){
			msg = String.format("Unknown Exception: Failed to Delete AFTLocator<%s>",aftLocator);
			logger.log(Level.SEVERE, msg);
			logger.log(Level.SEVERE, e.getMessage());
			aftMsg.SetShutdownErr(msg);
		}
		return aftMsg;
	}
	
	public int get_fileActionFlags() {
		return _fileActionFlags;
	}

	public void set_fileActionFlags(int _fileActionFlags) {
		this._fileActionFlags = _fileActionFlags;
	}

	public int get_chkAlgoIndex() {
		return _chkAlgoIndex;
	}

	public void set_chkAlgoIndex(int _chkAlgoIndex) {
		this._chkAlgoIndex = _chkAlgoIndex;
	}
	
	private AFTClient _aft = null;
	private boolean _enableTracing;
	private AFTConfig _aftConfig;
	private int _fileActionFlags;
	private int _chkAlgoIndex; 
	//private SBFileTypes _sbFileTypes = null;
	Snow.Snowbnd _snowbound = null;
	
	public static final int GET_FILE_SIZE = 1;  // 0001
	public static final int GET_CHECKSUM   = 2;  // 0010
	public static final int GET_FILE_FRMT = 4;  // 0100
	public static final int SB_NOT_PROCESSED = -998;
	public static final int SB_ERROR = -999;
		
	
	}
