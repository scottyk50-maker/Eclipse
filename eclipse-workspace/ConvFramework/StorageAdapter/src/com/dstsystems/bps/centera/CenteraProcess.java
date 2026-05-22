package com.dstsystems.bps.centera;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.OutputStream;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.dstsystems.bps.exceptions.CenteraException;
import com.dstsystems.bps.exceptions.FileChannelDigestException;
import com.dstsystems.bps.streams.FileChannelOutputDigestStream;
import com.filepool.fplibrary.FPClip;
import com.filepool.fplibrary.FPFileInputStream;
import com.filepool.fplibrary.FPLibraryConstants;
import com.filepool.fplibrary.FPLibraryException;
import com.filepool.fplibrary.FPPool;
import com.filepool.fplibrary.FPTag;

public class CenteraProcess {
	
	static Logger logger = Logger.getLogger(CenteraProcess.class.getName());
	
	public CenteraProcess(final String appName, final String appVersion, final String poolAddress, final String peaFilePath) throws CenteraException{
		SetupAndConnect(appName, appVersion, poolAddress, peaFilePath);
	}	
	
	private void SetupAndConnect(final String appName, final String appVersion, final String poolAddress, final String peaFilePath) throws CenteraException{
		String connectString = "";
		_appName = appName;
		_appVersion = appVersion;
		try {
			FPPool.setGlobalOption(FPLibraryConstants.FP_OPTION_OPENSTRATEGY,	FPLibraryConstants.FP_LAZY_OPEN);
			FPPool.RegisterApplication(appName, appVersion);
			connectString = poolAddress + "?" + peaFilePath;
			_vPool = new FPPool(connectString);
			logger.log(Level.FINE, "Connect to Pool: " + connectString);
		} catch (FPLibraryException e) {
			Close();
			String msg = "Failed to Initialize Centera: " + connectString;
			logger.log(Level.SEVERE,msg + "\n" + e.getMessage());
			throw new CenteraException(msg);
		}
		catch(Exception e){
			String msg = "Unknown Error occured, Failed to Initialize Centera: " + connectString;
			logger.log(Level.SEVERE,msg + "\n" + e.getMessage());
			throw new CenteraException(msg);
		}
		
	}
	
	public CenteraMsg AuditedDelete(final String clipId, final String auditString, final boolean privDelete){
		CenteraMsg cenMsg = new CenteraMsg();
		String msg;
		
		if(clipId == null || clipId.length()==0){
			msg = "Parameter clipId can not be null or blank";
			cenMsg.setFailed(msg);
			return cenMsg;
		}
		
		if(auditString == null || auditString.length()==0){
			msg = "Parameter auditString can not be null or blank";
			cenMsg.setFailed(msg);
			return cenMsg;
		}
		
		
		/*
		 * const FPLong inOptions
Specify one of the following options:
• FP_OPTION_DEFAULT_OPTIONS — Specify this option if you are
not performing a privileged deletion.
• FP_OPTION_DELETE_PRIVILEGED — Delete the C-Clip even if
the retention period has not expired. You must specify an
inReason string when performing a privileged deletion. Note
that a Compliance Edition Plus model never allows a
privileged deletion.
		 * 
		 */
		long deleteOption = privDelete ? 
							FPLibraryConstants.FP_OPTION_DELETE_PRIVILEGED:
							FPLibraryConstants.FP_OPTION_DEFAULT_OPTIONS;
		
		try {
			FPClip.AuditedDelete(_vPool, clipId, auditString, deleteOption);
		} catch (FPLibraryException e) {
			msg = String.format("AuditedDelete Exception for Clip <%s> Error<%d>\n%s", clipId,e.getErrorCode(), e.getErrorString());
			logger.log(Level.SEVERE,e.getErrorString());
			cenMsg.setFailed("Failed to AuditedDelete Clip: " + e.getErrorString());
		}
		catch(Exception e){
			msg = String.format("Unknown AuditedDelete Exception for Clip <%s> \n%s", clipId,e.getMessage());
			logger.log(Level.SEVERE,msg);
			cenMsg.setFailed(msg);
		}
		
		return cenMsg;
	}
	
	
	
	public CenteraMsg Delete(final String clipId){
		CenteraMsg cenMsg = new CenteraMsg();
		String msg;
		
		if(clipId == null || clipId.length()==0){
			msg = "Parameter clipId can not be null or blank";
			cenMsg.setFailed(msg);
			return cenMsg;
		}
		try {
			FPClip.Delete(_vPool, clipId);
			cenMsg.setSuccess();
			
		} catch (FPLibraryException e) {
			msg = String.format("Delete Exception for Clip <%s> Error<%d>\n%s", clipId,e.getErrorCode(), e.getErrorString());
			logger.log(Level.SEVERE,e.getErrorString());
			cenMsg.setFailed("Failed to delete Clip: " + e.getErrorString());
		}
		catch(Exception e){
			msg = String.format("Unknown Delete Exception for Clip <%s> \n%s", clipId,e.getMessage());
			logger.log(Level.SEVERE,msg);
			cenMsg.setFailed(msg);
		}
		
		return cenMsg;
	}
	
	public String StoreDocument(final String filePath, long retentionPeriod) throws CenteraException{
		
		String clipID = "";
		String CLIP_NAME = "Clip";
		//String VENDOR_NAME = "EMC";
		String TAG_NAME = "Data";
		String msg;
		
		try {

			// create a new named C-Clip
			FPClip theClip = new FPClip(_vPool, CLIP_NAME);
			
			// It's a good practice to write out vendor, application and version info
			//theClip.setDescriptionAttribute("app-vendor", VENDOR_NAME);
			theClip.setDescriptionAttribute("app-name", _appName);
			theClip.setDescriptionAttribute("app-version", _appVersion);

			// It's a good idea to explicitly set retention period.  For more info
			// on retention periods and classes see ManageRetention example.
			//theClip.setRetentionPeriod(retentionPeriod);

			FPFileInputStream inputStream = new FPFileInputStream(new File(filePath));

			FPTag topTag = theClip.getTopTag();

			FPTag newTag = new FPTag(topTag, TAG_NAME);

			topTag.Close();

			// Blob size is written to clip, so  lets just write out filename.
			newTag.setAttribute("filename", filePath);

			// write the binary data for this tag to the Centera
			newTag.BlobWrite(inputStream);

			clipID = theClip.Write();
			System.out.println(
				"Clip stored. The returned clip ID is " + clipID + ".");

			inputStream.close();
			newTag.Close();
			theClip.Close();

		} catch (FileNotFoundException e) {
			msg = "Could not open file: " + filePath;
			logger.log(Level.SEVERE,msg);
			throw new CenteraException(msg);
					
		} catch (IOException e) {
			msg = "Error reading from file: " + filePath;
			logger.log(Level.SEVERE,msg);
			throw new CenteraException(msg);
		} catch (FPLibraryException e) {
			msg = "Centera Upload Error for writing file: " + filePath;
			logger.log(Level.SEVERE,msg);
			throw new CenteraException(msg);
		}

		return clipID;
	}
	
	public CenteraMsg ClipExists(final String clipId){
		FPClip clip = null;
		CenteraMsg centeraMsg = new CenteraMsg();
		try {
			clip = new FPClip(_vPool, clipId, FPLibraryConstants.FP_OPEN_FLAT);
			centeraMsg.setSuccess();
			centeraMsg.set_fileSize(clip.getTotalSize());
		} catch (FPLibraryException e){
			String msg = "Failed to retrieve clip: " + clipId;
			centeraMsg.setFailed(msg);
			logger.log(Level.INFO,msg);
		}
		catch(Exception e){
			String msg = "Unknown Exception - " + e.getMessage();
			centeraMsg.setShutdown(msg);
			logger.log(Level.INFO,msg);
		}
		
		finally{
			Cleanup(clip, null);
		}
		return centeraMsg;
		
	}
	
	public CenteraMsg RetrieveByClipId(String clipId, OutputStream out) throws CenteraException{
		FPClip clip = null;
		FPTag topTag = null;
		CenteraMsg centeraMsg = new CenteraMsg();
		try {
			clip = new FPClip(_vPool, clipId, FPLibraryConstants.FP_OPEN_FLAT);
			centeraMsg.set_fileSize(clip.getTotalSize());
			if(centeraMsg.get_fileSize() > 0 ){
				topTag = clip.getTopTag();
				topTag.BlobRead(out);
				centeraMsg.setSuccess();	
			}
			else{
				centeraMsg.setZeroByteFile();
			}
		} catch (FPLibraryException e){
			String msg = "Failed to retrieve clip: " + clipId;
			centeraMsg.setFailed(msg);
			logger.log(Level.SEVERE,msg + "\n" + e.getErrorString() + e.getMessage());
			
		}
		catch( IOException e) {
			String msg = "Failed to load output for clip: " + clipId;
			centeraMsg.setShutdown(msg);
			logger.log(Level.SEVERE,msg + "\n" + e.getMessage());
		}
		catch(Exception e){
			String msg = "Unknown Exception Occured for clip: " + clipId;
			centeraMsg.setShutdown(msg);
			logger.log(Level.SEVERE,msg + "\n" + e.getMessage());
		}
		finally{
			Cleanup(clip, topTag);
		}
		return centeraMsg;
		
	}
	
		
	public CenteraMsg DigestRetrieveByClipId(final String clipId, final String outputFilePath, final String algorithm){
		FPClip clip = null;
		FPTag topTag = null;
		FileChannelOutputDigestStream out = null;
		CenteraMsg centeraMsg = new CenteraMsg();
		try {
			
			clip = new FPClip(_vPool, clipId, FPLibraryConstants.FP_OPEN_FLAT);
			long fileSize = clip.getTotalSize();
			topTag = clip.getTopTag();
			out = new FileChannelOutputDigestStream(algorithm, outputFilePath, fileSize );
			topTag.BlobRead(out);
			centeraMsg.setSuccess();
			centeraMsg.set_checkSum(out.GetChecksum());
			centeraMsg.set_fileSize(out.GetFileByteSize());
					
		} catch (FPLibraryException e){
			
			String msg = "Failed to retrieve clip: " + clipId;
			centeraMsg.setFailed(msg);
		}
		catch( IOException e) {
			String msg = "Failed to load output for clip: " + clipId;
			centeraMsg.setShutdown(msg);
		}
		catch(FileChannelDigestException e){
			String msg = String.format("Failed processing output file <%s> - %s", outputFilePath, e.getMessage());
			centeraMsg.setShutdown(msg);
		}
		catch(Exception e){
			String msg = String.format("Unknown Centera Exception: %s", e.getMessage());
			centeraMsg.setShutdown(msg);
		}
		finally{
			Cleanup(clip, topTag);
			CleanupStream(out);
		}
		return centeraMsg;

	}
	
	private void CleanupStream(FileChannelOutputDigestStream out){
		if(out != null){
			out.close();
			out = null;
		}
	}
	
	public long FileSizeByClipId(final String clipId) throws CenteraException{
		FPClip clip = null;
		long fileSize = 0;
		try {
			clip = new FPClip(_vPool, clipId, FPLibraryConstants.FP_OPEN_FLAT);
			fileSize = clip.getTotalSize();
		} catch (FPLibraryException e) {
			Cleanup(clip, null);
			throw new CenteraException("Failed to retrieve Clip: " + clipId);
		}
		finally{
			Cleanup(clip, null);
		}
		return fileSize;
	}

	public void Close() throws CenteraException{
		try {
			if(_vPool!=null){
				_vPool.Close();
			}
		} catch (FPLibraryException e) {
			throw new CenteraException("Cant close vPool connection");
		}
		finally{
			_vPool = null;	
		}
	}
	
	private void Cleanup(FPClip clip, FPTag tag){
		if(tag !=null){
			try {
				tag.Close();
			} catch (FPLibraryException e) {}
			finally{
				tag = null;
			}
		}
		
		if(clip !=null){
			try {
				clip.Close();
			} catch (FPLibraryException e) {}
			finally{
			clip = null;
			}
		}		
	}
	
		
	private FPPool _vPool;
	private String _appName;
	private String _appVersion;
}
