package com.dstsystems.bps.convdb.database;

import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.logging.Level;
import java.util.logging.Logger;


public class Recordset 
{
	static Logger logger = Logger.getLogger(Recordset.class.getName());
	
	public PreparedStatement preparedstmt = null;
	public ResultSet rset = null;
	public CallableStatement cstmt = null;
	
	public void close()
	{
		if (rset != null)
		{
			try { 
				rset.close(); 
			} catch (Exception ex) {
				logger.log( Level.SEVERE, ex.getMessage());
			}
			finally{
				//Can't do much anywayz
				rset = null;		
			}
			
		}
	
		if(preparedstmt !=null)
		{
			try { 
				preparedstmt.close(); 
			} catch (Exception ex) {
				logger.log( Level.SEVERE, ex.getMessage());
			}
			finally{
				preparedstmt = null;		
			}
		}
		
		if(cstmt !=null)
		{
			try { 
				cstmt.close(); 
			} catch (Exception ex) {
				logger.log( Level.SEVERE, ex.getMessage());
			}
			finally{
				cstmt = null;		
			}
		}
	}
}
