package com.dstsystems.bps.destlocaft.iprocessitem;

import java.sql.Timestamp;

import com.dstsystems.bps.aft.ConvAFT;
import com.dstsystems.bps.cfw.pub.interfaces.IProcessItem;

public class DestLocProcessItem implements IProcessItem{

	
	/**
	 * @param _schemaNm
	 * @param _batchId
	 * @param _processFlag
	 * @param _actionFlag
	 * @param _physFileId
	 * @param _fromDestId
	 * @param _fromLocator
	 * @param _toDestId
	 * @param _toLocator
	 */
	public DestLocProcessItem(	String schemaNm, 	int batchId, 		String processFlag, 
								String actionFlag, 	String physFileId, 	String fromDestId, 
								String fromLocator, String toDestId, 	String toLocator) {
		_schemaNm = schemaNm;
		_batchId = batchId;
		_processFlag = processFlag;
		_actionFlag = actionFlag;
		_physFileId = physFileId;
		_fromDestId = fromDestId;
		_fromLocator = fromLocator;
		_toDestId = toDestId;
		_toLocator = toLocator;
		
		/*
		 * Init to default value(s)
		 */
		InitDefaultValues();
		
	}


	public DestLocProcessItem(){
		InitDefaultValues();
	}
	
	public void InitDefaultValues(){
		_toRespCode = -1;
		_toRespMsg = "";
		_frmRespCode = -1;
		_frmRespMsg = "";
		_frmCheckSum = "";
		_frmFileSize = -1;
		_toCheckSum = "";
		_toFileSize = -1;
		_snowBndFrmtCode = ConvAFT.SB_NOT_PROCESSED;
	}
	
	public String get_schemaNm() {
		return _schemaNm;
	}
	public void set_schemaNm(String _schemaNm) {
		this._schemaNm = _schemaNm;
	}
	public int get_batchId() {
		return _batchId;
	}
	public void set_batchId(int _batchId) {
		this._batchId = _batchId;
	}
	public String get_processFlag() {
		return _processFlag;
	}
	public void set_processFlag(String _processFlag) {
		this._processFlag = _processFlag;
	}
	public String get_actionFlag() {
		return _actionFlag;
	}
	public void set_actionFlag(String _actionFlag) {
		this._actionFlag = _actionFlag;
	}
	public String get_physFileId() {
		return _physFileId;
	}
	public void set_physFileId(String _physFileId) {
		this._physFileId = _physFileId;
	}
	public String get_fromDestId() {
		return _fromDestId;
	}
	public void set_fromDestId(String _fromDestId) {
		this._fromDestId = _fromDestId;
	}
	public String get_fromLocator() {
		return _fromLocator;
	}
	public void set_fromLocator(String _fromLocator) {
		this._fromLocator = _fromLocator;
	}
	public String get_toDestId() {
		return _toDestId;
	}
	public void set_toDestId(String _toDestId) {
		this._toDestId = _toDestId;
	}
	public String get_toLocator() {
		return _toLocator;
	}
	public void set_toLocator(String _toLocator) {
		this._toLocator = _toLocator;
	}

	public int get_frmRespCode() {
		return _frmRespCode;
	}


	public void set_frmRespCode(int _frmRespCode) {
		this._frmRespCode = _frmRespCode;
	}


	public String get_frmRespMsg() {
		return _frmRespMsg;
	}


	public void set_frmRespMsg(String _frmRespMsg) {
		this._frmRespMsg = _frmRespMsg;
	}


	public int get_toRespCode() {
		return _toRespCode;
	}


	public void set_toRespCode(int _toRespCode) {
		this._toRespCode = _toRespCode;
	}


	public String get_toRespMsg() {
		return _toRespMsg;
	}


	public void set_toRespMsg(String _toRespMsg) {
		this._toRespMsg = _toRespMsg;
	}

	public Timestamp get_frmBegTs() {
		return _frmBegTs;
	}

	public void set_frmBegTsNow(){
		this._frmBegTs = new java.sql.Timestamp(java.lang.System.currentTimeMillis());
	}
	
	public void set_frmBegTs(final Timestamp _frmBegTs) {
		this._frmBegTs = _frmBegTs;
	}


	public Timestamp get_frmEndTs() {
		return _frmEndTs;
	}


	public void set_frmEndTsNow(){
		this._frmEndTs = new java.sql.Timestamp(java.lang.System.currentTimeMillis());
	}
	
	
	public void set_frmEndTs(final Timestamp _frmEndTs) {
		this._frmEndTs = _frmEndTs;
	}


	public Timestamp get_toBegTs() {
		return _toBegTs;
	}

	public void set_toBegTsNow(){
		this._toBegTs = new java.sql.Timestamp(java.lang.System.currentTimeMillis());
	}
	
	public void set_toBegTs(final Timestamp _toBegTs) {
		this._toBegTs = _toBegTs;
	}


	public Timestamp get_toEndTs() {
		return _toEndTs;
	}

	public void set_toEndTsNow(){
		this._toEndTs = new java.sql.Timestamp(java.lang.System.currentTimeMillis());
	}
	
	
	public void set_toEndTs(final Timestamp _toEndTs) {
		this._toEndTs = _toEndTs;
	}

	public int get_snowBndFrmtCode() {
		return _snowBndFrmtCode;
	}


	public void set_snowBndFrmtCode(int _snowBndFrmtCode) {
		this._snowBndFrmtCode = _snowBndFrmtCode;
	}


	public String get_frmCheckSum() {
		return _frmCheckSum;
	}


	public void set_frmCheckSum(String _frmCheckSum) {
		this._frmCheckSum = _frmCheckSum;
	}


	public long get_frmFileSize() {
		return _frmFileSize;
	}


	public void set_frmFileSize(long _frmFileSize) {
		this._frmFileSize = _frmFileSize;
	}


	public String get_toCheckSum() {
		return _toCheckSum;
	}


	public void set_toCheckSum(String _toCheckSum) {
		this._toCheckSum = _toCheckSum;
	}


	public long get_toFileSize() {
		return _toFileSize;
	}


	public void set_toFileSize(long _toFileSize) {
		this._toFileSize = _toFileSize;
	}
		
	
	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("DestLocProcessItem [");
		if (_schemaNm != null) {
			builder.append("_schemaNm=");
			builder.append(_schemaNm);
			builder.append(", ");
		}
		builder.append("_batchId=");
		builder.append(_batchId);
		builder.append(", ");
		if (_processFlag != null) {
			builder.append("_processFlag=");
			builder.append(_processFlag);
			builder.append(", ");
		}
		if (_actionFlag != null) {
			builder.append("_actionFlag=");
			builder.append(_actionFlag);
			builder.append(", ");
		}
		if (_physFileId != null) {
			builder.append("_physFileId=");
			builder.append(_physFileId);
			builder.append(", ");
		}
		if (_fromDestId != null) {
			builder.append("_fromDestId=");
			builder.append(_fromDestId);
			builder.append(", ");
		}
		if (_fromLocator != null) {
			builder.append("_fromLocator=");
			builder.append(_fromLocator);
			builder.append(", ");
		}
		if (_toDestId != null) {
			builder.append("_toDestId=");
			builder.append(_toDestId);
			builder.append(", ");
		}
		if (_toLocator != null) {
			builder.append("_toLocator=");
			builder.append(_toLocator);
			builder.append(", ");
		}
		builder.append("_snowBndFrmtCode=");
		builder.append(_snowBndFrmtCode);
		builder.append(", ");
		builder.append("_frmRespCode=");
		builder.append(_frmRespCode);
		builder.append(", ");
		if (_frmRespMsg != null) {
			builder.append("_frmRespMsg=");
			builder.append(_frmRespMsg);
			builder.append(", ");
		}
		if (_frmCheckSum != null) {
			builder.append("_frmCheckSum=");
			builder.append(_frmCheckSum);
			builder.append(", ");
		}
		builder.append("_frmFileSize=");
		builder.append(_frmFileSize);
		builder.append(", _toRespCode=");
		builder.append(_toRespCode);
		builder.append(", ");
		if (_toRespMsg != null) {
			builder.append("_toRespMsg=");
			builder.append(_toRespMsg);
			builder.append(", ");
		}
		if (_toCheckSum != null) {
			builder.append("_toCheckSum=");
			builder.append(_toCheckSum);
			builder.append(", ");
		}
		builder.append("_toFileSize=");
		builder.append(_toFileSize);
		builder.append(", ");
		if (_frmBegTs != null) {
			builder.append("_frmBegTs=");
			builder.append(_frmBegTs);
			builder.append(", ");
		}
		if (_frmEndTs != null) {
			builder.append("_frmEndTs=");
			builder.append(_frmEndTs);
			builder.append(", ");
		}
		if (_toBegTs != null) {
			builder.append("_toBegTs=");
			builder.append(_toBegTs);
			builder.append(", ");
		}
		if (_toEndTs != null) {
			builder.append("_toEndTs=");
			builder.append(_toEndTs);
		}
		builder.append("]");
		return builder.toString();
	}
	
	
	private String _schemaNm;
	private int _batchId;
	private String _processFlag;
	private String _actionFlag;
	private String _physFileId;
	private String _fromDestId;
	private String _fromLocator;
	private String _toDestId;
	private String _toLocator;
	private int _snowBndFrmtCode;
	
	private int _frmRespCode;
	private String _frmRespMsg;
	private String _frmCheckSum;
	private long _frmFileSize;
	
	private int _toRespCode;
	private String _toRespMsg;
	private String _toCheckSum;
	private long _toFileSize;
	
	private Timestamp _frmBegTs;
	private Timestamp _frmEndTs;
	
	private Timestamp _toBegTs;
	private Timestamp _toEndTs;
	
	
			 
}
