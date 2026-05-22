package com.dstsytems.bps.awdxml.srvgetcoll;


public class AWDPage {
	
		
	public String get_physFileId() {
		return _physFileId;
	}

	public void set_physFileId(String _physFileId) {
		this._physFileId = _physFileId;
	}

	public int get_fileSeqNr() {
		return _fileSeqNr;
	}

	public void set_fileSeqNr(int _fileSeqNr) {
		this._fileSeqNr = _fileSeqNr;
	}

	public String get_origFileFormat() {
		return _origFileFormat;
	}

	public void set_origFileFormat(String _origFileFormat) {
		this._origFileFormat = _origFileFormat;
	}

	public String get_sbFileFormat() {
		return _sbFileFormat;
	}

	public void set_sbFileFormat(String _sbFileFormat) {
		this._sbFileFormat = _sbFileFormat;
	}

	public int get_fileIndexCount() {
		return _fileIndexCount;
	}

	public void set_fileIndexCount(int _fileIndexCount) {
		this._fileIndexCount = _fileIndexCount;
	}

	public String get_fileUrl() {
		return _fileUrl;
	}

	public void set_fileUrl(String _fileUrl) {
		URLParse  parse = new URLParse(_fileUrl);
		_fileAftHost = parse.get_host();
		_fileAftPort = parse.get_port();
		_fileAftLocator = parse.get_locator();
		_fileDestId = parse.get_destId();
		parse = null;
		this._fileUrl = _fileUrl;
	}

	public String get_fileAftHost() {
		return _fileAftHost;
	}

	public void set_fileAftHost(String _fileAftHost) {
		this._fileAftHost = _fileAftHost;
	}

	public int get_fileAftPort() {
		return _fileAftPort;
	}

	public void set_fileAftPort(int _fileAftPort) {
		this._fileAftPort = _fileAftPort;
	}

	public String get_fileAftLocator() {
		return _fileAftLocator;
	}

	public void set_fileAftLocator(String _fileAftLocator) {
		this._fileAftLocator = _fileAftLocator;
	}

	public String get_fileDestId() {
		return _fileDestId;
	}

	public void set_fileDestId(String _fileDestId) {
		this._fileDestId = _fileDestId;
	}

	public int get_aftRtrnCd() {
		return _aftRtrnCd;
	}

	public void set_aftRtrnCd(int _aftRtrnCd) {
		this._aftRtrnCd = _aftRtrnCd;
	}

	public String get_annId() {
		return _annId;
	}

	public void set_annId(String _annId) {
		this._annId = _annId;
	}

	public String get_annFormat() {
		return _annFormat;
	}

	public void set_annFormat(String _annFormat) {
		this._annFormat = _annFormat;
	}

	public String get_annUrl() {
		return _annUrl;
	}

	public void set_annUrl(String _annUrl) {
		URLParse  parse = new URLParse(_annUrl);
		_annAftHost = parse.get_host();
		_annAftPort = parse.get_port();
		_annAftLocator = parse.get_locator();
		_annDestId = parse.get_destId();
		parse = null;
		this._annUrl = _annUrl;
	}

	public String get_annAftHost() {
		return _annAftHost;
	}

	public void set_annAftHost(String _annAftHost) {
		this._annAftHost = _annAftHost;
	}

	public int get_annAftPort() {
		return _annAftPort;
	}

	public void set_annAftPort(int _annAftPort) {
		this._annAftPort = _annAftPort;
	}

	public String get_annAftLocator() {
		return _annAftLocator;
	}

	public void set_annAftLocator(String _annAftLocator) {
		this._annAftLocator = _annAftLocator;
	}

	public String get_annDestId() {
		return _annDestId;
	}

	public void set_annDestId(String _annDestId) {
		this._annDestId = _annDestId;
	}
	
	public boolean is_hasAnnotation() {
		return _hasAnnotation;
	}

	public void set_hasAnnotation(boolean _hasAnnotation) {
		this._hasAnnotation = _hasAnnotation;
	}
	
	public String get_annRevision() {
		return _annRevision;
	}

	public void set_annRevision(String _annRevision) {
		this._annRevision = _annRevision;
	}

	@Override
	public String toString() {
		return String
				.format("AWDPage [_physFileId=%s, _fileSeqNr=%s, _origFileFormat=%s, _sbFileFormat=%s, _fileIndexCount=%s, _fileUrl=%s, _fileAftHost=%s, _fileAftPort=%s, _fileAftLocator=%s, _fileDestId=%s, _aftRtrnCd=%s, _annId=%s, _annRevision=%s, _annFormat=%s, _annUrl=%s, _annAftHost=%s, _annAftPort=%s, _annAftLocator=%s, _annDestId=%s, _hasAnnotation=%s]",
						_physFileId, _fileSeqNr, _origFileFormat,
						_sbFileFormat, _fileIndexCount, _fileUrl, _fileAftHost,
						_fileAftPort, _fileAftLocator, _fileDestId, _aftRtrnCd,
						_annId, _annRevision, _annFormat, _annUrl, _annAftHost,
						_annAftPort, _annAftLocator, _annDestId, _hasAnnotation);
	}

	

	//File Variables
	private String _physFileId;
	private int _fileSeqNr;
	private String _origFileFormat;
	private String _sbFileFormat;
	private int _fileIndexCount;
	private String _fileUrl;
	private String _fileAftHost;
	private int _fileAftPort;
	private String _fileAftLocator;
	private String _fileDestId;
	private int _aftRtrnCd;
		
	//Annotation Variables
	private String _annId;
	private String _annRevision;
	private String _annFormat;
	private String _annUrl;
	private String _annAftHost;
	private int _annAftPort;
	private String _annAftLocator;
	private String _annDestId;
	private boolean _hasAnnotation = false;
		
	private class URLParse {
		public URLParse(final String url){
			parseURL(url);
		}
		
		private void parseURL(final String url){
			
			
			int startPortIndex = url.indexOf(":",6);
	    	int  endPortIndex = url.indexOf("/",startPortIndex);
	    	int endLocatorIndex = url.indexOf("?",endPortIndex);
	    	int startDeviceindex = url.indexOf("&",endLocatorIndex);
	    	startDeviceindex = url.indexOf("=",startDeviceindex);
	    	int endDeviceindex = url.indexOf("&",startDeviceindex+1);
	    	_host = url.substring(6,startPortIndex);
	    	_port = Integer.parseInt(url.substring(startPortIndex + 1,endPortIndex));
	    	_locator = url.substring(endPortIndex + 1,endLocatorIndex);
	    	_destId = url.substring(startDeviceindex+1,endDeviceindex);
			
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

		public String get_locator() {
			return _locator;
		}

		public void set_locator(String _locator) {
			this._locator = _locator;
		}

		public String get_destId() {
			return _destId;
		}

		public void set_destId(String _destId) {
			this._destId = _destId;
		}
		private String _host;
		private int _port;
		private String _locator;
		private String _destId;
	}

	

	

	

	

	
	

}