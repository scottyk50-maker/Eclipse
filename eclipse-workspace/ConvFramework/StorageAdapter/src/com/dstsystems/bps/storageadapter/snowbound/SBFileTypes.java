package com.dstsystems.bps.storageadapter.snowbound;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;



public class SBFileTypes {

	public SBFileTypes(String configPath) throws SnowBoundFileTypeException {
		_fileTypeMap = new HashMap<Integer, String>();
		LoadConfigFile(configPath);
	}

	private void LoadConfigFile(String configPath) throws SnowBoundFileTypeException {
		BufferedReader br = null;
		String line = "";
		String cvsSplitBy = ",";
		try {
			br = new BufferedReader(new FileReader(configPath));

			while ((line = br.readLine()) != null) {
				String[] imageType = line.split(cvsSplitBy);
				_fileTypeMap.put(Integer.parseInt(imageType[0]), imageType[1]);
			}
		} catch (Exception e) {
			close(br);
			throw new SnowBoundFileTypeException("Failed to load: "	+ configPath);
		} finally {
			close(br);
		}
	}

	private void close(BufferedReader br) {
		if (br != null) {
			try {
				br.close();
			} catch (IOException e) { /* Can't do much anywayz */
			} finally {
				br = null;
			}
		}
	}

	public String GetFileExtention(final int fileType) throws SnowBoundFileTypeException {
		String extn = _fileTypeMap.get(fileType);
		if (extn == null) {
			throw new SnowBoundFileTypeException("Snowbound FileType not found: "
					+ fileType);
		}
		return extn;
	}

	HashMap<Integer, String> _fileTypeMap;
}
