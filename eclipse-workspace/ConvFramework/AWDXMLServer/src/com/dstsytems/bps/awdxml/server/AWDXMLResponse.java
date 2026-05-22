package com.dstsytems.bps.awdxml.server;

public class AWDXMLResponse {

	public AWDXMLResponse(String xmlResponse, int responseCd) {
		_responseCd = responseCd;
		_xmlResponse = xmlResponse;
		
	}
	
	public int get_responseCd() {
		return _responseCd;
	}
	public void set_responseCd(int responseCd) {
		_responseCd = responseCd;
	}
	public String get_xmlResponse() {
		return _xmlResponse;
	}
	public void set_xmlResponse(String xmlResponse) {
		_xmlResponse = xmlResponse;
	}
	
	private String _xmlResponse;
	private int _responseCd;
}
