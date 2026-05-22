package com.dstsytems.bps.awdxml.srvgetcoll;

import java.util.ArrayList;
import java.util.List;

public class AWDCollection {
	
	public AWDCollection() {
		_awdPageList = new ArrayList<AWDPage>();
		_errorOccured = false;
	}

	public List<AWDPage> getAWDPages(){
		return _awdPageList;
	}
	
	public void AddAWDPage(final AWDPage awdPage){
		_awdPageList.add(awdPage);
	}
	
	public String getCollectionId() {
		return _collectionId;
	}

	public void setCollectionId(String collectionId) {
		_collectionId = collectionId;
	}

	public String get_userId() {
		return _userId;
	}

	public void set_userId(String _userId) {
		this._userId = _userId;
	}
	
	public int get_errorCode() {
		return _errorCode;
	}

	public void set_errorCode(int _errorCode) {
		this._errorCode = _errorCode;
	}

	public String get_errorMsg() {
		return _errorMsg;
	}

	public void set_errorMsg(String _errorMsg) {
		this._errorMsg = _errorMsg;
	}

	public boolean get_errorOccured() {
		return _errorOccured;
	}

	public void set_errorOccured(boolean _errorOccured) {
		this._errorOccured = _errorOccured;
	}

	private List<AWDPage> _awdPageList;
	private String _collectionId;
	private String _userId;
	private int _errorCode=0;
	private String _errorMsg="";
	private boolean _errorOccured;
	
}