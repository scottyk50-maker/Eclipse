package com.dstsystems.bps.aft;

public class AFTMsg {
	
	public void SetRtrnCd(int RtrnCd)
	{
		_rtrnCd = RtrnCd;
		//reset _customMsg because it can only be set by calling func SetCustomErr
		_customMsg = "";
		_checksum = "";
		_imageFormat = -998;
		_fileSize = -1;
		
	}
	
	public void SetCustomErr(String msg)
	{
		_rtrnCd = CUSTOM_ERROR;
		_customMsg = msg;
	}
	
	public void SetShutdownErr(String msg)
	{
		_rtrnCd = SHUTDOWN_ERROR;
		_customMsg = msg;
	}
	
	public void SetRestartErr(String msg){
		_rtrnCd = RESTART_ERROR;
		_customMsg = msg;
	}
	
	public int GetRtrnCd()
	{
		return _rtrnCd;
	}
	
	public String GetAFTMsg(){
		return GetAFTMessage();
	}
	
	public String get_checksum() {
		return _checksum;
	}

	public void set_checksum(String _checksum) {
		this._checksum = _checksum;
	}

	public int get_imageFormat() {
		return _imageFormat;
	}

	public void set_imageFormat(final int _imageFormat) {
		this._imageFormat = _imageFormat;
	}

	public long get_fileSize() {
		return _fileSize;
	}

	public void set_fileSize(long _fileSize) {
		this._fileSize = _fileSize;
	}
	
	private String GetAFTMessage(){
		String msg;
		switch(_rtrnCd){
		case SUCCESS:
			msg = "SUCCESS";
			break;
		case ERROR_NO_SUCCESS:
			msg = "ERROR_NO_SUCCESS";
			break;
		case ERROR_FILE_NOT_FOUND:
			msg = "ERROR_FILE_NOT_FOUND";
			break;
		case ERROR_PATH_NOT_FOUND:
			msg = "ERROR_PATH_NOT_FOUND";
			break;
		case ERROR_ACCESS_DENIED:
			msg = "ERROR_ACCESS_DENIED";
			break;
		case ERROR_BAD_COMMAND:
			msg = "ERROR_BAD_COMMAND";
			break;
		case ERROR_CRC:
			msg = "ERROR_CRC";
			break;
		case ERROR_BAD_LENGTH:
			msg = "ERROR_BAD_LENGTH";
			break;
		case ERROR_FILE_EXISTS:
			msg = "ERROR_FILE_EXISTS";
			break;
		case ERROR_PATH_BUSY:
			msg = "ERROR_PATH_BUSY";
			break;
		case ERROR_WHITELIST:
			msg = "ERROR_WHITELIST";
			break;
		case RETURN_NEVER_SET:
			msg = "RETURN_NEVER_SET";
			break;
		case CUSTOM_ERROR:
		case SHUTDOWN_ERROR:
		case RESTART_ERROR:
			msg = _customMsg;
			break;
		default:
			msg = "Unknown Error";
		}
		return msg;
	}
	private int _rtrnCd;
	private String _customMsg;
	private String _checksum;
	private int _imageFormat;
	private long _fileSize;
	
	
	//----------------------------------
	public static final int  	SUCCESS              = 0;
	public static final int    	ERROR_NO_SUCCESS     = 1;
	public static final int    	ERROR_FILE_NOT_FOUND = 2;
	public static final int    	ERROR_PATH_NOT_FOUND = 3;
	public static final int    	ERROR_ACCESS_DENIED  = 5;
	public static final int    	ERROR_BAD_COMMAND    = 22;
	public static final int    	ERROR_CRC            = 23;
	public static final int    	ERROR_BAD_LENGTH     = 24;
	public static final int    	ERROR_FILE_EXISTS    = 80;
	public static final int    	ERROR_PATH_BUSY      = 148;
	public static final int    	ERROR_WHITELIST      = 188;
	public static final int    	RETURN_NEVER_SET     = 998;
	public static final int    	CUSTOM_ERROR   		 = -999;
	public static final int    	SHUTDOWN_ERROR		 = -998;
	public static final int		RESTART_ERROR		 = -997;
	

}
