package com.dstsystems.bps.streams;


import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.OutputStream;
import java.io.RandomAccessFile;
import java.math.BigInteger;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import com.dstsystems.bps.exceptions.FileChannelDigestException;

public class DigestOutputStream extends OutputStream {
	
	public static final int ALGO_MD5 = 0;
	public static final int ALGO_SHA_1 = 1;
	public static final int ALGO_SHA_256 = 2;
	
	public DigestOutputStream(final int digestType) throws FileChannelDigestException{
		_digestType=digestType;
		_digest = GetAlgorithm();
	}
	
	public void CreateOutputFile(String filePath, long length) throws FileChannelDigestException{
		try {
			Reset();
			//_fileChannel = new FileOutputStream(filePath).getChannel();
			_fileChannel = (new RandomAccessFile(filePath, "rw")).getChannel();
			_buffer = _fileChannel.map(FileChannel.MapMode.READ_WRITE, 0,length);
			
		} catch (FileNotFoundException e) {
			throw new FileChannelDigestException("Invalid File Path: " + filePath);
		}
		catch(SecurityException e){
			throw new FileChannelDigestException("Security Access Denied: " + filePath);
		}
		catch(Exception e){
			throw new FileChannelDigestException("Unknown Exception: " + e.getMessage());
		}
	}
	
	public String GetChecksum() throws FileChannelDigestException{
		//Need to format output based on digest type
		//sha-1 160 bits 40 bytes
		//sha-256 256 bits 64 bytes
		//md5 128 bits 32 bytes
		String checksum = "";
		try{
			checksum = String.format(_algoFrmts[_digestType],new BigInteger(1, _digest.digest()));
		}
		catch(Exception e){
			throw new FileChannelDigestException("Unknown Exception: " + e.getMessage());
		}
		  
		return checksum; 
	}
	
	public long GetFileByteSize() throws FileChannelDigestException{
		long size = 0;
		try {
			size = _fileChannel.position();
		} catch (IOException e) {
			throw new FileChannelDigestException("Failed to get File Size");
		} 
		return size;
		
	}
	
	private MessageDigest GetAlgorithm( ) throws FileChannelDigestException{
		
		try{
			switch(_digestType){
			case ALGO_MD5:
				return MessageDigest.getInstance(_algoTypes[ALGO_MD5]);
			case ALGO_SHA_1:
				return MessageDigest.getInstance(_algoTypes[ALGO_SHA_1]);
			case ALGO_SHA_256:	
				return MessageDigest.getInstance(_algoTypes[ALGO_SHA_256]);
				default:
					throw new FileChannelDigestException("NoSuchAlgorithm - Must be " + ALGO_MD5 + ", " + ALGO_SHA_1 + ", or " + ALGO_SHA_256);	
			}
		}
		catch(NoSuchAlgorithmException e){
			throw new FileChannelDigestException("NoSuchAlgorithm - Must be " + ALGO_MD5 + ", " + ALGO_SHA_1 + ", or " + ALGO_SHA_256);
		}
	}
		
		
	
	private void Reset(){
		if(_digest!= null){
			_digest.reset();
		}
		
		if (_fileChannel != null){
			if(_fileChannel.isOpen()){
				try {
					_fileChannel.close();
				} catch (IOException e) {}
				
			}
		}
		if(_buffer != null){
			_buffer.clear();
		}
	}
	
	/**************************************
	 http://stackoverflow.com/questions/7474766/filechannel-write-on-linux-produces-lots-of-garbage-but-not-on-mac 
	 
	 	File file = new File("fileChannelTest.log");
	    FileOutputStream fos = new FileOutputStream(file);
	    FileChannel fileChannel = fos.getChannel();
	
	    ByteBuffer bb1 = ByteBuffer.wrap("This is a log line to test!\n".getBytes());
	
	    ByteBuffer bb2 = ByteBuffer.allocateDirect(bb1.remaining());
	    bb2.put(bb1).flip();
	
	    bb2.mark();
	    long freeMemory = Runtime.getRuntime().freeMemory();
	    for (int i = 0; i < 1000000; i++) {
	        bb2.reset();
	        fileChannel.write(bb2);
	    }
	    System.out.println("Memory allocated: " + (freeMemory - Runtime.getRuntime().freeMemory()));
				 
	 
	***************************************/		
	
	
	@Override
	public synchronized void write(int b){
		_buffer.put((byte)b);
		_digest.update((byte)b);
	}
	
	@Override
	public synchronized void write(byte[] bytes, int off, int len){
		_buffer.put(bytes, off, len);
		_digest.update(bytes,0,len);
    }
	
	@Override
	public void close(){
		Reset();
	}
	
	private ByteBuffer _buffer;
	private MessageDigest _digest;
	private FileChannel _fileChannel;
	
	private int _digestType;
	private static final String[] _algoTypes = {"MD5","SHA-1","SHA-256"};
	private static final String[] _algoFrmts = {"%032x","%040x","%064x"};

}
