package com.dstsystems.bps.streams;

import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.RandomAccessFile;
import java.math.BigInteger;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import com.dstsystems.bps.exceptions.FileChannelDigestException;

public class FileChannelOutputDigestStream extends OutputStream {
			
			public FileChannelOutputDigestStream(String algorithm, String outFilePath, long blobSize) throws FileChannelDigestException{
				FileSetup(outFilePath, blobSize);
				DigestSetup(algorithm);
			}

			

			
			
			private void FileSetup(String outFilePath,  long blobSize) throws FileChannelDigestException{
				try {
					_fileChannel = (new RandomAccessFile(outFilePath, "rw")).getChannel();
				} catch (FileNotFoundException e) {
					throw new FileChannelDigestException("Invalid file Path - " + outFilePath); 
				}
				try {
					_buffer = _fileChannel.map(FileChannel.MapMode.READ_WRITE, 0, blobSize);
				} catch (IOException e) {
					throw new FileChannelDigestException("Failed to create internal File Buffer");
				}
				
			}
			private void DigestSetup(final String algorithm) throws FileChannelDigestException{
				try{
					String match = "(?i).*" + ALGO_MD5 + "|" + ALGO_SHA_1 + "|" + ALGO_SHA_256;
					if(!algorithm.matches(match)){
						throw new FileChannelDigestException("NoSuchAlgorithm - Must be " + ALGO_MD5 + ", " + ALGO_SHA_1 + ", or " + ALGO_SHA_256);
					}
//					if(!algorithms.matches(ALGO_MD5 +"|" + ALGO_SHA_1 + "|" + ALGO_SHA_256 )){
//						throw new FileChannelDigestException("NoSuchAlgorithm - Must be " + ALGO_MD5 + ", " + ALGO_SHA_1 + ", or " + ALGO_SHA_256); 
//					}
					_digest = MessageDigest.getInstance(algorithm);
				} catch (NoSuchAlgorithmException e) {
					throw new FileChannelDigestException("NoSuchAlgorithmEception - " + algorithm);
				}
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
			
			public long CopyTo(final String savePath) throws FileChannelDigestException {
				FileChannel destChannel = null;
				long fileSize = 0;
				try {
					destChannel = new FileOutputStream(savePath).getChannel();
					fileSize = _fileChannel.size();
					destChannel.transferFrom(_fileChannel, 0, fileSize);
				}catch (IOException e) {
					throw new FileChannelDigestException("Failed to Copy File to : " + savePath);
				}
				finally{
					try{destChannel.close();}catch(Exception e){}
				}
				
				return fileSize;
			}
			
			public DataInputStream CreateDataInputStreamFromOutput() throws FileChannelDigestException {
				DataInputStream dis = null;
				try{
					
					dis=new DataInputStream(new ByteArrayInputStream(_buffer.array()));	
				}
				catch(Exception e ){
					throw new FileChannelDigestException("Failed to Create InputStream");
				}
				
				return dis;
			}
			public String GetChecksum() throws FileChannelDigestException{
				//Need to format output based on digest type
				//sha-1 160 bits 40 bytes
				//sha-256 256 bits 64 bytes
				//md5 128 bits 32 bytes
				String frmt;
				switch (_digest.getAlgorithm()){
					case ALGO_MD5:
						frmt = "%032x";
						break;
					case ALGO_SHA_1:
						frmt = "%040x";
						break;
					case ALGO_SHA_256:
						frmt = "%064x";
						break;
					default: 
						throw new FileChannelDigestException("Invalid Checksum Type: " + _digest.getAlgorithm());
				}
				return String.format(frmt,new BigInteger(1, _digest.digest())).toUpperCase();
			}
			
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
				if(_fileChannel != null){
					try {
						_fileChannel.close();
					} catch (IOException e) {
						
					}
					finally{
						_fileChannel = null;	
					}
				}
				if(_buffer != null){
					_buffer.clear();
					_buffer = null;
				}
				
			}
			
						
			private ByteBuffer _buffer;
			private MessageDigest _digest;
			private FileChannel _fileChannel;
			//private String _algorithm;
			//private static final String[] algoTypes = {"MD5","SHA-1","SHA-256"};
				
			
			public static final String ALGO_MD5 = "MD5";
			public static final String ALGO_SHA_1 = "SHA-1";
			public static final String ALGO_SHA_256 = "SHA-256";
			
		}
