package com.dstsystems.bps.aft;

public class AFTConfig  {

	public AFTConfig() {
		
	}


	/**
	 * @param _host
	 * @param _port
	 * @param _DestId
	 * @param _fileActionFlags
	 * @param _chkSumAlgoIndex
	 */
	public AFTConfig(String _host, int _port, String _DestId,
			int _fileActionFlags, int _chkSumAlgoIndex) {
		this._host = _host;
		this._port = _port;
		this._DestId = _DestId;
		this._fileActionFlags = _fileActionFlags;
		this._chkSumAlgoIndex = _chkSumAlgoIndex;
	}

	public String get_host() {
		return _host;
	}
	
	public void set_host(String _host) {
		this._host = _host;
	}
	
	public int get_port() {
		return _port;
	}
	
	public void set_port(int _port) {
		this._port = _port;
	}
	
	public String get_DestId() {
		return _DestId;
	}

	public void set_DestId(String _DestId) {
		this._DestId = _DestId;
	}
	
	public int get_fileActionFlags() {
		return _fileActionFlags;
	}

	public void set_fileActionFlags(int _fileActionFlags) {
		this._fileActionFlags = _fileActionFlags;
	}

	public int get_chkSumAlgoIndex() {
		return _chkSumAlgoIndex;
	}

	public void set_chkSumAlgoIndex(int _chkSumAlgoIndex) {
		this._chkSumAlgoIndex = _chkSumAlgoIndex;
	}
	
	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("AFTConfig [_host=");
		builder.append(_host);
		builder.append(", _port=");
		builder.append(_port);
		builder.append(", _DestId=");
		builder.append(_DestId);
		builder.append(", _fileActionFlags=");
		builder.append(_fileActionFlags);
		builder.append(", _chkSumAlgoIndex=");
		builder.append(_chkSumAlgoIndex);
		builder.append("]");
		return builder.toString();
	}
	
	private String _host;
	private int _port;
	private String _DestId;
	private int _fileActionFlags;
	private int _chkSumAlgoIndex;
	
	
	
	
}
