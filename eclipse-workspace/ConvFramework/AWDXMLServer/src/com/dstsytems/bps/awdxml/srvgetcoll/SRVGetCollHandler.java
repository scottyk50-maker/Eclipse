package com.dstsytems.bps.awdxml.srvgetcoll;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;


public class SRVGetCollHandler extends DefaultHandler {

	public SRVGetCollHandler(){
		_sbBuff = new StringBuffer();
		
	}
	
	@Override
	public void startDocument() {
		_awdCollection = new AWDCollection();
	}

	@Override
	public void endDocument() {
		//_awdCollection = new AWDCollection();
	}
	
	public AWDCollection get_awdCollection() {
		return _awdCollection;
	}

	@Override
	public void startElement(String uri, String localName, String qName,
			Attributes attributes) throws SAXException {
		switch (qName) {
		case SRVGetColl.COLLECTION:
			String collection_id = attributes.getValue("id");
			_awdCollection.setCollectionId(collection_id);
			break;
		case SRVGetColl.PHYS_FILE:
			_awdPage = new AWDPage();
			String physFileId = attributes.getValue("id");
			_awdPage.set_physFileId(physFileId);
			break;
		case SRVGetColl.SEQUENCE:
			_bsequence = true;
			break;
		case SRVGetColl.FILE_FORMAT:
			_bfileFormat = true;
			break;
		case SRVGetColl.URL:
			_sbBuff.delete(0, _sbBuff.length());
			_bURL = true;
			break;
		case SRVGetColl.USERID:
			_buserId = true;
			break;
		case SRVGetColl.ERROR_ELM:
			_sbError = new StringBuffer();
			_bProcessError = true;
			_awdCollection.set_errorOccured(true);
			break;
		
		case SRVGetColl.CODE:
			//If an error is not present
			if(_bProcessError == false){
				_bresponseCode = true;	
			}
			break;
		/*
		case SRVGetColl.TEXT:
			_berrorText = true;
			break;
		
		case SRVGetColl.ANNOTATION:
			//String annoId = attributes.getValue("id");
			String annoId = attributes.getValue(0);
			_awdPage.set_annId(annoId);
			_awdPage.set_hasAnnotation(true);
			
			//String annRev = attributes.getValue("CurrentRevision");
			String annRev = attributes.getValue(1);
			_awdPage.set_annRevision(annRev);
			_bAnnotation = true;
			break;
		case SRVGetColl.ANNO_FRMT:
			_bAnnoFrmt = true;
		*/
		}
	}

	@Override
	public void characters(char ch[], int start, int length)
			throws SAXException {
		if (_bsequence) {
			_awdPage.set_fileSeqNr(Integer.parseInt(new String(ch, start, length)));
			_bsequence = false;
		} else if (_bfileFormat) {
			_awdPage.set_origFileFormat(new String(ch, start, length));
			_bfileFormat = false;
		} else if (_bURL) {
			_sbBuff.append(ch, start, length);
		} 
		else if (_buserId) {
			_awdCollection.set_userId(new String(ch, start, length));
			_buserId = false;
		}else if (_bProcessError) {
			_sbBuff.append(ch, start, length);
			//awdCollection.set_errorCode(-99);
			//String tmp = new String(ch, start, length);
			
			//_awdCollection.set_errorCode(Integer.parseInt(new String(ch, start, length)));
			//_berrorCode = false;
		}
		else if (_bresponseCode) {
			_awdCollection.set_errorCode(Integer.parseInt(new String(ch, start, length)));
			_bresponseCode = false;
		}
		/*
		else if (_berrorText) {
			_awdCollection.set_errorMsg(new String(ch, start, length));
			_berrorText = false;
		}
		/*
		/*
		else if(_bAnnoFrmt){
			_awdPage.set_annFormat(new String(ch, start, length));
			_bAnnoFrmt = false;
		}
		*/
	}

	@Override
	public void endElement(String uri, String localName, String qName)
			throws SAXException {
		if (qName.equalsIgnoreCase(SRVGetColl.PHYS_FILE)) {
			_awdCollection.AddAWDPage(_awdPage);
		}else if(qName.equalsIgnoreCase(SRVGetColl.URL)){
			_awdPage.set_fileUrl(_sbBuff.toString());
			/*
			if(_bAnnotation){
				_awdPage.set_annUrl(_sbBuff.toString());
			}
			else{
				_awdPage.set_fileUrl(_sbBuff.toString());	
			}
			*/
			_bURL = false;
		}
		
		else if(qName.equalsIgnoreCase(SRVGetColl.ERROR_ELM)){
			if(_sbError != null){
				_awdCollection.set_errorMsg(_sbError.toString());
				_awdCollection.set_errorCode(-98);
				_sbError.setLength(0);
			}
			else
			{
				_awdCollection.set_errorMsg("Unknown Error Occurred");
				_awdCollection.set_errorCode(-99);
			}
			_sbBuff.setLength(0);
		}
		else if(qName.equalsIgnoreCase(SRVGetColl.CODE) && _bProcessError){
			_sbError.append(String.format("Code = <%s> ", _sbBuff.toString()));
			_sbBuff.setLength(0);
		}
		else if(qName.equalsIgnoreCase(SRVGetColl.TEXT) && _bProcessError){
			_sbError.append(String.format("Text = <%s> ", _sbBuff.toString()));
			_sbBuff.setLength(0);
		}
		else if(qName.equalsIgnoreCase(SRVGetColl.TASK) && _bProcessError){
			_sbError.append(String.format("Task = <%s> ", _sbBuff.toString()));
			_sbBuff.setLength(0);
		}
		
		
		/*
		else if(qName.equalsIgnoreCase(SRVGetColl.ANNOTATION)){
			_bAnnotation = false;
		}
		*/
	}

	private AWDCollection _awdCollection;
	private AWDPage _awdPage;
	private boolean _bsequence = false;
	private boolean _bfileFormat = false;
	private boolean _bfileIndexCount = false;
	private boolean _bURL = false;
	private boolean _bProcessError = false;
	private boolean _buserId = false;
	private boolean _berrorCode = false;
	private boolean _bresponseCode = false;
	private boolean _berrorText = false;
	//private boolean _bAnnotation = false;
	//private boolean _bAnnoFrmt = false;
	private StringBuffer _sbBuff;
	private StringBuffer _sbError;

}