package com.dstsystems.bps.centera;

public class CenteraMsg {
	
	public CenteraMsg(){
		this._status = 0;
		this._fileSize = 0;
		this._checkSum = "";
		this._statusMsg = "";
	}
	public CenteraMsg(final int status, final long fileSize, final String checkSum, final String statusMsg){
		this._status = status;
		this._fileSize = fileSize;
		this._checkSum = checkSum;
		this._statusMsg = statusMsg;
	}
	
	public void setSuccess(){
		_status = 0;
		_statusMsg = SUCCESS;
	}
	
	public void setFailed(final String msg){
		_status = CUSTOM_ERROR;
		_statusMsg = msg;
	}
	
	public void setZeroByteFile(){
		_status = ZERO_BTYE;
		_statusMsg ="Zero Byte File";
	}
	public void setShutdown(final String msg){
		_status = SHUTDOWN_ERROR;
		_statusMsg = msg;
	}
	
	
	public int get_status() {
		return _status;
	}
	public void set_status(final int status) {
		this._status = status;
	}
	public long get_fileSize() {
		return _fileSize;
	}
	public void set_fileSize(final long fileSize) {
		this._fileSize = fileSize;
	}
	public String get_checkSum() {
		//return "2020202020202020202020202020202020202020202020202020202020202020202020202020202020202020202020202020202020202020202020202020202020202020202020202020202020202020202020202020202020202020202020202020202020202020202020202020202020202020202020202020202020202020";
		return _checkSum;
	}
	public void set_checkSum(final String checkSum) {
		this._checkSum = checkSum;
	}
	public String get_statusMsg() {
		return _statusMsg;
	}
	public void set_statusMsg(final String statusMsg) {
		this._statusMsg = statusMsg;
	}
	
	public long get_uploadTm() {
		return _uploadTm;
	}
	public void set_uploadTm(long _uploadTm) {
		this._uploadTm = _uploadTm;
	}

	private int _status;
	private long _fileSize;
	private String _checkSum = "";
	private String _statusMsg;
	private long _uploadTm;
	public static final int    SHUTDOWN_ERROR     	= -998;
	public static final int    CUSTOM_ERROR     	= -999;
	public static final int    ZERO_BTYE     		= -800;
	private static String SUCCESS = "SUCCESS";
	
	
}
