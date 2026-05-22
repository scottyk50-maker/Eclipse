package com.dstsystems.bps.cfw.framework;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;

import com.dstsystems.bps.cfw.pub.exceptions.ShutdownException;
import com.dstsystems.bps.cfw.pub.interfaces.IConfig;
import com.dstsystems.bps.metaparser.config.MetaDataParserConfig;

public class ScrubData {
	static Logger logger = Logger.getLogger(ScrubData.class.getName());

	public static int countOccurrences(String field, char value) {
	    int count = 0;
	    for (char c : field.toCharArray()) {
	        if (c == value) {
	           ++count;
	        }
	    }
	    return count;
	}
	
	public static void main(String[] args) {
		if(args.length !=2){
			logger.log(Level.SEVERE, "wrong amount of arguments <Path to Config.xml> <Config ClassPath>");
			return;
		}
		_config = LoadConfiguration(args[0],args[1] );
		StringBuffer sb = new StringBuffer();
		BufferedReader br = null;
		ArrayList<String> fileRows = new ArrayList<String>();
		String filePath = _config.get_scrubfilePath();
		String outPath = _config.get_metafilePath();
		
		int numDelims = _config.get_delCnt();
		int delimFnd =0;
		String line;
		char cDelim = (char)_config.get_delimeter();
		char cEOL = (char)_config.get_eol();
		boolean hasHeader = _config.get_header();
		int lineCnt = 0;		
		try
		{
			br = new BufferedReader(new FileReader(filePath));
			File fout = new File(outPath);
			FileOutputStream fos = new FileOutputStream(fout);
		 
			BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(fos));			
			/**
			**Eat the Header Row
			**/
			if(hasHeader){
				line = br.readLine();	
				lineCnt++; 
			}
			
			while ((line = br.readLine()) != null) {
				lineCnt++; 
				int EOLCnt = countOccurrences(line,cEOL);
				int delimCnt = countOccurrences(line,cDelim);
				String msg1 = String.format("wrote out line number <%d> with value of ,%s>",lineCnt, line);
				logger.log(Level.INFO, msg1  );
				
				if(EOLCnt == 1 && delimCnt == numDelims){
				// read line that should have all columns in it
					if(delimFnd == 0)
					{
					// good full line with all columns
						bw.write(line);
						bw.newLine();
						delimFnd = 0;
					}
					else
					{
					//  whoa, full column line but we still have a partial line(s) read before
						br.close();
						bw.close();
						String msg = String.format("full line read but partial line was previously read: previous column count <%d> line number <%d> ",delimFnd,lineCnt);
						logger.log(Level.SEVERE, msg );
						throw new Exception(msg);
					}
				}
				else if (EOLCnt == 1 && delimCnt < numDelims){
					//partial line read - should be the last one before writing
						if(delimCnt + delimFnd == numDelims){	
						// finally got a full line with all columns
							sb.append(line);
							String row = sb.toString();
							bw.write(row);
							bw.newLine();
							sb.setLength(0);
							delimFnd = 0;
						}
						else if(delimCnt + delimFnd < numDelims){	
							br.close();
							bw.close();
							String msg = String.format("not enough columns Read before EOL: column count <%d> line number <%d> ",delimCnt + delimFnd,lineCnt);
							logger.log(Level.SEVERE, msg );
							throw new Exception(msg);
						}
						else if(delimCnt + delimFnd > numDelims){
							// oh no, we have too many columns
							br.close();
							bw.close();
							String msg = String.format("too many columns Reading multiple lines: column count <%d> line number <%d> ",delimCnt + delimFnd,lineCnt);
							logger.log(Level.SEVERE, msg );
							throw new Exception(msg);

						}
				}
				else if (EOLCnt == 1 && delimCnt > numDelims){
					// oh no, we have too many columns
					br.close();
					bw.close();
					String msg = String.format("too many columns Reading a single line: column count <%d> line number <%d> ",delimCnt + delimFnd,lineCnt);
					logger.log(Level.SEVERE, msg );
					throw new Exception(msg);
				}
				else if (EOLCnt == 0 && delimCnt + delimFnd < numDelims){
					// still not a full line
					sb.append(line);
					delimFnd += delimCnt;
				}
				else if (EOLCnt == 0 && delimCnt + delimFnd == numDelims){
					// not good, full line but no sign of the EOL
					sb.append(line);
					delimFnd += delimCnt;
				}
				else if (EOLCnt == 0 && delimCnt + delimFnd > numDelims){
					// oh no, we have too many columns
					br.close();
					bw.close();
					String msg = String.format("too many columns Reading multiple lines: column count <%d> line number <%d> ",delimCnt + delimFnd,lineCnt);
					logger.log(Level.SEVERE, msg );
					throw new Exception(msg);
				}
			}
			br.close(); 
			bw.close();
		}
		catch(Exception e){
			System.out.println(e.getMessage());
			}

	}
	private static MetaDataParserConfig LoadConfiguration(final String configPath, final String classPath) {
		JAXBContext jaxbContext = null;
		File file = null;//= new File("config.xml");
		try {
			file = new File(configPath);
			if(!file.isFile()){
				String msg = String.format("Failed to load ConfigFile <%s>", configPath);
				logger.log(Level.SEVERE, msg);
				throw new ShutdownException(msg);
			}
		
			_config = (MetaDataParserConfig) Class.forName(classPath).newInstance();
			jaxbContext = JAXBContext.newInstance(_config.getClass());
			Unmarshaller jaxbUnmarshaller = jaxbContext.createUnmarshaller();
			_config= (MetaDataParserConfig) jaxbUnmarshaller.unmarshal(file);
		}	
		 catch(Exception e){
			System.out.println(e.getMessage());
		}
		return _config;
		
		
	}
	private static MetaDataParserConfig _config;

	
}

