package com.dstsytems.bps.awdxml.srvgetcoll;


public class SRVGetColl {

	public static final String GetRequestString(final String awdUsrId, final String crdattim, final String recordcd, final String crnode, final String collectionId){
		return String.format(REQUEST_FRMT, awdUsrId,crdattim,recordcd,crnode,collectionId);
		//return String.format(REQUEST_FRMT, awdUsrId,collectionId); 
	}
	
	private static String ROT13Encrypt(String s)
	   {
	       StringBuffer sb = new StringBuffer();
	       byte b[] = s.getBytes();
	       byte iLast = 0;
	       byte bFirst = 0;
	       for(int i = 0; i < b.length; i++)
	       {
	           iLast = 0;
	           if((b[i] >= 'A' && b[i]<='Z'))
	           {    
	                   iLast = 'Z';
	                   bFirst = 'A';
	           }        
	           else
	           if((b[i] >= 'a' && b[i]<='z'))
	           {    
	                  iLast =  'z';
	                  bFirst = 'a';
	           }       
	           if(iLast > 0)
	           {
	               int b1 = (int)(b[i]+13);
	               if(b1 > iLast)
	               {
	                   b[i] = (byte)(bFirst+ (b1-iLast)-1);
	               }
	               else
	                   b[i] = (byte)b1;
	           }
	           sb.append((char)b[i]);
	       }
	       return sb.toString();
	   }
	
	public static final String GetRequestString(final String awdUsrId, final String awdPWD, final String crdattim, final String recordcd, final String crnode, final String collectionId){
		return String.format(_COLLECTION_TEMPLATE, awdUsrId, ROT13Encrypt(awdPWD), crdattim,recordcd,crnode,collectionId);
	}
		
	
	private static final String REQUEST_FRMT = "<DST xml:lang=\"en-US\"><jobName version=\"1.0\">SRVGetColl</jobName><readable>Y</readable><trace>0</trace><AWD><userID>%s</userID><password></password><source id=\"%s%s%s\"><collection id=\"%s\"></collection></source></AWD></DST>";
	//private static final String REQUEST_FRMT = "<DST xml:lang=\"en-US\"><jobName version=\"1.0\">SRVGetColl</jobName><readable>Y</readable><trace>0</trace><AWD><userID>%s</userID><password></password><source><collection id=\"%s\"></collection></source></AWD></DST>";
	
	protected static final String _COLLECTION_TEMPLATE = 
		       "<DST xml:lang=\"en-us\">" +
		          "<jobName version=\"1.0\">SRVGetColl</jobName>" +
		          "<trace>0</trace>" +
		          "<readable>Y</readable>" +
		          "<AWD>" +
		             "<userID>%s</userID>" +
		             "<password>%s</password>" +
		             "<source id=\"%s%s%s\">" +
		                "<collection id=\"%s\"></collection>" +
		             "</source>" +
		          "</AWD>" +
		       "</DST>";
	
	
	public static final String COLLECTION = "collection";
	public static final String PHYS_FILE = "file";
	public static final String SEQUENCE = "sequence";
	public static final String FILE_FORMAT = "fileFormat";
	public static final String FILE_INDEX_CNT = "fileIndexCount";
	public static final String URL = "URL";
	public static final String USERID = "userID";
	public static final String ERROR_ELM = "error";
	public static final String CODE = "code";
	public static final String TEXT = "text";
	public static final String TASK = "task";
	public static final String ANNOTATION = "annotation";
	public static final String ANNO_FRMT = "annotationFormat";

}
