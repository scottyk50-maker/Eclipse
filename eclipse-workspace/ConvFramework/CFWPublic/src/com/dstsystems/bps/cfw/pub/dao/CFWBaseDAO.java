package com.dstsystems.bps.cfw.pub.dao;

import java.sql.Clob;
import java.sql.PreparedStatement;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.dstsystems.bps.cfw.pub.exceptions.ShutdownException;
import com.dstsystems.bps.convdb.database.ConvDB;
import com.dstsystems.bps.convdb.database.FieldTypeValue;
import com.dstsystems.bps.convdb.database.Recordset;
import com.dstsystems.bps.convdb.exceptions.ConvDBException;


public abstract class CFWBaseDAO {
	static Logger logger = Logger.getLogger(CFWBaseDAO.class.getName());
	
	/*
	 * String driver = _config.get_jdbcDriver();
		String url = _config.get_jdbcURL();
		String usr = _config.get_dbUserId();
		String pwd = _config.get_dbPwd();
	 */
	
	protected CFWBaseDAO(final String dbType, final String jdbcDriver,
			final String jdbcURL, final String jdbcUsr, final String jdbcPWD,
			final int fetchSize,
			final String schemaTableNm) throws ShutdownException {

		if (dbType != null && !dbType.isEmpty()) {
			if (dbType.matches(DB2_MATCH)) {
				_dbType = DB2;
			} else if (dbType.matches(ORA_MATCH)) {
				_dbType = ORACLE;
			} else if (dbType.matches(SQLSRVR_MATCH)) {
				_dbType = SQLSERVER;
			}else {
				String msg = String
						.format("Invalid Database Type <%s>", dbType);
				throw new ShutdownException(msg);
			} 
		}
		else {
			String msg = ("dbType cannot be null or blank");
			throw new ShutdownException(msg);
		}

		if (fetchSize < 1) {
			String msg = String.format(
					"fetchSize <%d> Must be Greater than Zero", fetchSize);
			throw new ShutdownException(msg);

		} else {
			_fetchSize = fetchSize;
		}

		if (schemaTableNm != null && !schemaTableNm.isEmpty()) {
			_schemaTableNm = schemaTableNm;
		} else {
			String msg = ("schemaTableNm cannot be null or blank");
			throw new ShutdownException(msg);
		}
		_db = new ConvDB();
		DBConnect(jdbcDriver, jdbcURL, jdbcUsr, jdbcPWD);

	}
	
	private void DBConnect(final String jdbcDriver, final String jdbcURL,
			final String jdbcUsr, final String jdbcPWD) throws ShutdownException {
		try {
			_db.Connect(jdbcDriver, jdbcURL, jdbcUsr, jdbcPWD);
		} catch (ConvDBException e) {
			throw new ShutdownException(e);
		}
		catch(Exception e){
			String msg = String.format("Unknown Exception in DBConnect jdbcDriver<%s> jdbcURL<%s> jdbcUsr<%s>", jdbcDriver, jdbcURL, jdbcUsr);
			logger.log(Level.SEVERE,msg + e.getMessage());
			throw new ShutdownException(msg);
		}
	}
	
	protected void DBDisconnect(){
		 try { 
			 if(_db != null){
				 _db.Disconnect();		  
			 }
		} catch (Exception e) {
			logger.log(Level.SEVERE,e.getMessage());
		}
		
		_db = null;
	}
	
	protected Recordset ExecuteQuery(final String sql) throws ConvDBException{
		Recordset rs = null;
		try{
			rs = _db.ExecuteQuery(sql);	
		}
		catch (ConvDBException e) {
			RecordsetCleanup(rs);
			throw e;
		}
		return rs;
	}
	
	protected void SQLExecute(final String sql) throws ConvDBException{
		_db.SQLExecute(sql);
	}
	
	protected int ExcecuteUpdate(final ArrayList<FieldTypeValue> fieldList, final String sql) throws ConvDBException{
		
		return _db.ExcecuteUpdate(fieldList, sql);
	}

	protected int ExecutePSUpdate(final ArrayList<FieldTypeValue> fieldList, Recordset rs) throws ConvDBException{
		
		return _db.ExecutePSUpdate(fieldList, rs);
	}
	
	protected void RecordsetCleanup(Recordset rs){
		 try { rs.close(); } catch (Exception ex) {}
	}
	
	protected String get_dbType() {
		return _dbType;
	}
	protected int get_fetchSize() {
		return _fetchSize;
	}
	protected String get_schemaTableNm() {
		return _schemaTableNm;
	}
	
	protected Clob CreateClob() throws ConvDBException{
		Clob clob = null;
		String msg;
		
		try{
			clob = _db.GetClob();
			return clob;
		}
		catch(ConvDBException e){
			logger.log(Level.SEVERE,e.getMessage());
			throw e;
		}
		catch(Exception e){
			msg = String.format("Unknown Exception in CreateClob For dbType <%s>", get_dbType());
			logger.log(Level.SEVERE,msg);
			logger.log(Level.SEVERE,e.getMessage());
			throw new ConvDBException(msg);
		}
		
		
	}

	protected Recordset createCallablePreparedStatement(String sql) throws ConvDBException {
		Recordset rs = null;
		String msg;
		
		try{
			rs = _db.CreateCallablePreparedStatement(sql);
			return rs;
		} catch(ConvDBException e){
			logger.log(Level.SEVERE,e.getMessage());
			throw e;
		}
		catch(Exception e){
			msg = String.format("Unknown Exception in createCallablePreparedStatement For dbType <%s>", get_dbType());
			logger.log(Level.SEVERE,msg);
			logger.log(Level.SEVERE,e.getMessage());
			throw new ConvDBException(msg);
		}
		
	}
	
	protected String StrTs2Str(final String stringTS){
		StringBuilder sbTS = new StringBuilder(stringTS);
		while(sbTS.length() !=26){
			sbTS.append("0");
		}
		
		sbTS.setCharAt(10, ' ');
		sbTS.setCharAt(13, ':');
		sbTS.setCharAt(16, ':');
		
		return sbTS.toString();
		
	}
	
	protected String Timestamp2String(final Timestamp ts){
		//String s = "2005-02-25 11:50:11.579410";
		
		StringBuilder sbTS = new StringBuilder(ts.toString());
		while(sbTS.length() !=26){
			sbTS.append("0");
		}
		
		sbTS.setCharAt(10, ' ');
		sbTS.setCharAt(13, ':');
		sbTS.setCharAt(16, ':');
		
		return sbTS.toString();
		
	}
	
	protected String Timestamp2StringISO(final Timestamp ts){
		String s = ts.toString();
		
		while(s.length() !=26){
			s+="0";
		}
		return s.replaceAll(" ", "-").replaceAll(":", ".");
	}
	
	public static final String DB2 = "DB2";
	private static final String DB2_MATCH = "^(?i)(" + DB2 + ")";
	
	public static final String ORACLE = "ORACLE";
	private static final String ORA_MATCH = "^(?i)(" + ORACLE +")";
	
	public static final String SQLSERVER = "SQLSERVER";
	private static final String SQLSRVR_MATCH = "^(?i)(" + SQLSERVER +")";
	
	private String _dbType;
	private int _fetchSize;
	private String _schemaTableNm;
	private ConvDB _db;
	
}
