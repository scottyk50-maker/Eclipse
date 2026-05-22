package com.dstsystems.convchksum.processs;

import java.io.IOException;
import java.io.InputStream;
import java.math.BigInteger;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.dstsystems.convchksum.exceptions.ConvChkSumException;

public class ConvCheckSum {
	
	static Logger logger = Logger.getLogger(ConvCheckSum.class.getName());
	
	public ConvCheckSum() throws ConvChkSumException{
		_algoIndex = ConvCheckSum.ALGO_SHA_256;
		if(!ValidateAlgoIndex(_algoIndex)){
			throw new ConvChkSumException("Invalid Algorithm");
		}
	}

	
	public ConvCheckSum(final int algorithm) throws ConvChkSumException{
		_algoIndex = algorithm;
		if(!ValidateAlgoIndex(algorithm)){
			throw new ConvChkSumException("Invalid Algorithm");
		}
	}
	
	private boolean ValidateAlgoIndex(final int algoIndex) throws ConvChkSumException{
		boolean bRtrn = false;
		try{
			if(algoIndex>= ALGO_MD5 && algoIndex<=ALGO_SHA_256){
				_msgDigest = MessageDigest.getInstance(_ALGORITHMS[algoIndex]);
				bRtrn = true;
			}else{
				bRtrn = false;
			}
		
		} catch (NoSuchAlgorithmException e) {
			throw new ConvChkSumException("Invalid Algorithm");
		}
		catch(Exception e){
			throw new ConvChkSumException("Invalid Algorithm");
		}
		return _msgDigest!=null && bRtrn;
	}

	public String CalculateCheckSum(Path path) throws ConvChkSumException{
		String result="";
		if(path == null){
			throw new ConvChkSumException("Path input parameter cannot be null");
		}
        InputStream in = null;		
		try{
			in = Files.newInputStream(path);
			result = CalculateCheckSum(in);
            InputStreamcleanup(in);			
		}catch(IOException e){
			throw new ConvChkSumException("Failed to Creat InputStream");
		}
        catch(ConvChkSumException e){
            InputStreamcleanup(in);
            throw e;
        }
		return result;
	}
	
    private void InputStreamcleanup(InputStream in){
        try{
               if(in != null){
                     in.close();
               }
               
        }
        catch(Exception e ){
			String msg;
			logger.log(Level.SEVERE, "Failed to delete temporary file.");
			logger.log(Level.SEVERE, e.getMessage() );
        }
		
	}
	
	public String CalculateCheckSum(final InputStream in) throws ConvChkSumException{
		String msg;
		String result = "";
		if(in == null){
			throw new ConvChkSumException("InputStream cannot be null"); 
		}
		try {
			byte[] dataBytes = new byte[2048];
			int nread = 0; 
		    
		    while ((nread = in.read(dataBytes)) != -1) {
		    	_msgDigest.update(dataBytes, 0, nread);
		    };
		    
		    result = GetChecksum();
		} catch (Exception e) {
			msg = String.format("Failed to create checksum: %s", e.getMessage());
			throw new ConvChkSumException(msg);
		}
		return result;
	}
	
	public String GetChecksum() throws ConvChkSumException{
		
		String chkSum;
		
		try{
			chkSum = String.format(_ALGO_FRMTS[_algoIndex],new BigInteger(1, _msgDigest.digest())).toUpperCase();
			_msgDigest.reset();
		}
		catch(Exception e){
			throw new ConvChkSumException("Failed to calculate Convert Checksum");
		}
		return chkSum;
	}

	public static void main(String[] args){
		Path source = Paths.get("D:/AFTGETS/AFT_IMAGES/AC001108.AAF");
		ConvCheckSum cs = null;
		try{
			cs = new ConvCheckSum(ConvCheckSum.ALGO_SHA_256);
			System.out.println(cs.CalculateCheckSum(source));
		}
		catch(Exception e){
			System.out.println();
		}
	}
	

	MessageDigest _msgDigest = null;

	private static final String[] _ALGORITHMS = {"MD5","SHA-1","SHA-256"};
	
	//Need to format output based on digest type
	//sha-1 160 bits 40 bytes
	//sha-256 256 bits 64 bytes
	//md5 128 bits 32 bytes
	private static final String[] _ALGO_FRMTS = { "%032x", "%040x","%064x"};
	private int _algoIndex;
	
	public static final int ALGO_MD5 = 0;
	public static final int ALGO_SHA_1 = 1;
	public static final int ALGO_SHA_256 = 2;
}
