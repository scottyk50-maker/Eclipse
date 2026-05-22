package com.dstsystems.bps.destlocaft.database;

import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Collection;
import java.util.IllegalFormatException;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.dstsystems.bps.cfw.pub.dao.CFWBaseDAO;
import com.dstsystems.bps.cfw.pub.exceptions.ShutdownException;
import com.dstsystems.bps.cfw.pub.interfaces.IProcessItem;
import com.dstsystems.bps.convdb.database.ConvDB;
import com.dstsystems.bps.convdb.database.FieldTypeValue;
import com.dstsystems.bps.convdb.database.Recordset;
import com.dstsystems.bps.convdb.exceptions.ConvDBException;
import com.dstsystems.bps.destlocaft.config.DestLocAFTConfig;
import com.dstsystems.bps.destlocaft.iprocessitem.DestLocProcessItem;



public class DestLocDAO extends CFWBaseDAO{
	static Logger logger = Logger.getLogger(DestLocDAO.class.getName());
	
	public DestLocDAO(final String dbType, final String jdbcDriver,
			final String jdbcURL, final String jdbcUsr, final String jdbcPWD,
			final int fetchSize,
			final String schemaTableNm) throws ShutdownException{
		
		
		super(dbType, jdbcDriver, jdbcURL, jdbcUsr, jdbcPWD, fetchSize, schemaTableNm );
		_lastPhysFileId = "";
		try{
			rsMigrate = createCallablePreparedStatement(GetMigrate_UpdateSql());
			rsUpdateVRSD = createCallablePreparedStatement(GetVDRS_UpdateSql());	
		}
		catch(Exception e){
			throw new ShutdownException(e.getMessage());
		}
		
	}
	

	public Collection<IProcessItem> GetNextQueueItems(DestLocAFTConfig config) throws ShutdownException{
		ArrayList<IProcessItem> processList = new ArrayList<IProcessItem>();
		String msg;
		String sql = "";
		Recordset rs = null;
		try {
			sql = GetPopulateItemsSQL(config);
			
			
			rs = ExecuteQuery(sql);
			if (rs ==null || rs.rset == null) {
				RecordsetCleanup(rs);
				throw new ShutdownException("rs.rset is null, SQL Error");
			}
			
			while (rs.rset.next()) {
				String actionFlag =rs.rset.getString(DBConstants.ACTION_FLAG);
				_lastPhysFileId = rs.rset.getString(DBConstants.PHYS_FILE_ID);
				String fromDestId = rs.rset.getString(DBConstants.FRM_DEST_ID);
				String fromLoc = rs.rset.getString(DBConstants.FRM_LOC);
				String toDestId = rs.rset.getString(DBConstants.TO_DEST_ID);
				String toLoc = rs.rset.getString(DBConstants.TO_LOC);
				
				DestLocProcessItem pi = new DestLocProcessItem(	config.get_schema(), config.get_batchId(), config.get_processFlag(), actionFlag, _lastPhysFileId, fromDestId, fromLoc, toDestId, toLoc);
				
				logger.log( Level.FINE, pi.toString());	
				
				processList.add(pi);
				if(0 == processList.size()){
					_lastPhysFileId = "";
				}
			}
			
			RecordsetCleanup(rs);
			
		} catch (ConvDBException e) {
			RecordsetCleanup(rs);
			throw new ShutdownException(e);
		}
		catch(SQLException e){
			msg ="SQLException to retrieve Process Records: "	+ sql;
			logger.log( Level.SEVERE, msg + "\n" + e.getMessage() );
			RecordsetCleanup(rs);
			throw new ShutdownException(msg);
		}
		catch(Exception e){
			msg = "Unknown Exception Occured, Throwing ShutdownException: " + sql;
			logger.log( Level.SEVERE, msg + "\n" + e.getMessage() );
			RecordsetCleanup(rs);
			throw new ShutdownException(msg);
		}
		return processList;
	}
	
	public ArrayList<String> GetDestIdList() throws ShutdownException{
		ArrayList<String> destIdList = new ArrayList<String>();
		String sql="";
		String sqlFrmt = "";
		Recordset rs;
		try{
			sqlFrmt = get_dbType() == DB2?DBConstants.GetDestId_DB2:DBConstants.GetDestId_ORA;
			
			sql=String.format(get_dbType() == DB2?DBConstants.GetDestId_DB2:DBConstants.GetDestId_ORA, get_schemaTableNm(),get_schemaTableNm());
			logger.log(Level.FINE,sql);
			
			rs = ExecuteQuery(sql);
			
			if (rs ==null || rs.rset == null) {
				RecordsetCleanup(rs);
				throw new ShutdownException("rs.rset is null, SQL Error");
			}
			
			while (rs.rset.next()) {
				destIdList.add(rs.rset.getString(DBConstants.DEST_ID));
				
			}
			RecordsetCleanup(rs);
			
		}catch(IllegalFormatException e){
			String msg = String.format("Failed to format GetDestID SQL for dbType <%s> %s", get_dbType(), sqlFrmt);
			logger.log(Level.SEVERE,msg);
			logger.log(Level.SEVERE,e.getMessage());
			throw new ShutdownException(msg);
		} catch (ConvDBException e) {
			String msg = e.getMessage();
			logger.log(Level.SEVERE, msg);
			throw new ShutdownException(msg);
		} catch (SQLException e) {
			String msg = String.format("SQL Error while processing GetDestID SQL for dbType <%s> %s", get_dbType(), sql);
			logger.log(Level.SEVERE,msg);
			logger.log(Level.SEVERE,e.getMessage());
			throw new ShutdownException(msg);
		}
		catch (Exception e) {
			String msg = String.format("Unknown SQL Error while processing GetDestID SQL for dbType <%s> %s", get_dbType(), sql);
			logger.log(Level.SEVERE,msg);
			logger.log(Level.SEVERE,e.getMessage());
			throw new ShutdownException(msg);
		}

		return destIdList;
	}
	
	private String GetPopItemsPhysFrmtr(){
		String sqlFrmter = ""; 
		switch(get_dbType()){
			case DB2:
				sqlFrmter = DBConstants.PopItemsPhys_DB2;
				break;
			case ORACLE:
				sqlFrmter = DBConstants.PopItemsPhys_ORA;
				break;
			default:
				//Should never get here because dbType is validated in the Constructor
				sqlFrmter = "";
				break;
		}
		return sqlFrmter; 
	}
	
	private String GetPopItemsFrmtr() throws ShutdownException{
		String sqlFrmter = ""; 
		switch(get_dbType()){
			case DB2:
				sqlFrmter = DBConstants.PopItems_DB2;
				break;
			case ORACLE:
				sqlFrmter = DBConstants.PopItems_ORA;
				break;
			default:
				//Should never get here because dbType is validated in the Constructor of CFWBaseDAO
				String msg = "Unknown dbType: " + get_dbType(); 
				throw new ShutdownException(msg);
		}
		return sqlFrmter; 
	}
	
	private String GetPopulateItemsSQL(DestLocAFTConfig cfg) throws ShutdownException{
		String sql;
		
		String SqlFrmter;
		try{
			if (_lastPhysFileId.length() > 0) {
				SqlFrmter = GetPopItemsPhysFrmtr();
				sql = String.format( SqlFrmter, 
							get_schemaTableNm(), 	cfg.get_schema(), 	cfg.get_batchId(),
							cfg.get_processFlag(), 	_lastPhysFileId,	get_fetchSize());
			} else {
				SqlFrmter = GetPopItemsFrmtr();
				sql = String.format(SqlFrmter, 
							get_schemaTableNm(),	cfg.get_schema(), 	cfg.get_batchId(),
							cfg.get_processFlag(), 	get_fetchSize());
			}
		}catch(IllegalFormatException e){
			String msg = String.format("Invalid SQL FRMTR dbType <%s> ", get_dbType());
			logger.log(Level.SEVERE,msg + e.getMessage());
			throw new ShutdownException(msg);
		}
		catch(Exception e){
			String msg = String.format("Unknown Exception: SQL FRMTR dbType <%s> ", get_dbType());
			logger.log(Level.SEVERE,msg + e.getMessage());
			throw new ShutdownException(msg);
		}
		logger.log(Level.INFO,sql);
		return sql;
	}
		
	public void Disconnect(){
		if (rsMigrate != null) {
		rsMigrate.close();
		}
		if (rsUpdateVRSD != null) {
		rsUpdateVRSD.close();
		}
		DBDisconnect();
	}

	public void MigrateActDBUpdate(final DestLocProcessItem pi) throws ShutdownException{
		String sql="";
		try {
			ArrayList<FieldTypeValue> fieldList = new ArrayList<FieldTypeValue>();
			
			
			
			fieldList.add(new FieldTypeValue( 	
					pi.get_frmRespCode(),
					ConvDB.TYPE_INT, 
					DBConstants.VDRS_UPD_POS_FRM_RET_CD));
			
			fieldList.add(new FieldTypeValue( 	
					pi.get_frmRespMsg(),
					ConvDB.TYPE_STRING, 
					DBConstants.MIGRATE_UPD_POS_FRM_RET_MSG));
			
			fieldList.add(new FieldTypeValue( 	
					pi.get_frmBegTs(),
					ConvDB.TYPE_TIMESTAMP, 
					DBConstants.MIGRATE_UPD_POS_FRM_BEG_TS));
			
			fieldList.add(new FieldTypeValue( 	
					pi.get_frmEndTs(),
					ConvDB.TYPE_TIMESTAMP, 
					DBConstants.MIGRATE_UPD_POS_FRM_END_TS));
			
			fieldList.add(new FieldTypeValue( 	
					pi.get_frmFileSize(),
					ConvDB.TYPE_LONG, 
					DBConstants.MIGRATE_UPD_POS_FRM_FILE_SZ));
			
			fieldList.add(new FieldTypeValue( 	
					pi.get_frmCheckSum(),
					ConvDB.TYPE_STRING, 
					DBConstants.MIGRATE_UPD_POS_FRM_CHECKSUM));
			
			fieldList.add(new FieldTypeValue( 	
					pi.get_toRespCode(),
					ConvDB.TYPE_INT, 
					DBConstants.MIGRATE_UPD_POS_TO_RET_CD));
			
			fieldList.add(new FieldTypeValue( 	
					pi.get_toRespMsg(),
					ConvDB.TYPE_STRING, 
					DBConstants.MIGRATE_UPD_POS_TO_RET_MSG));
			
			fieldList.add(new FieldTypeValue( 	
					pi.get_toBegTs(),
					ConvDB.TYPE_TIMESTAMP, 
					DBConstants.MIGRATE_UPD_POS_TO_BEG_TS));
			
			fieldList.add(new FieldTypeValue( 	
					pi.get_toFileSize(),
					ConvDB.TYPE_LONG, 
					DBConstants.MIGRATE_UPD_POS_TO_FILE_SZ));
			
			fieldList.add(new FieldTypeValue( 	
					pi.get_toCheckSum(),
					ConvDB.TYPE_STRING, 
					DBConstants.MIGRATE_UPD_POS_TO_CHECKSUM));
			
			fieldList.add(new FieldTypeValue( 	
					pi.get_snowBndFrmtCode(),
					ConvDB.TYPE_INT, 
					DBConstants.MIGRATE_UPD_POS_SB_FRMT_CD));
			
			fieldList.add(new FieldTypeValue(
					pi.get_schemaNm(),
					ConvDB.TYPE_STRING,
					DBConstants.MIGRATE_UPD_POS_SCHEMA_NM));

			fieldList.add(new FieldTypeValue(
					pi.get_batchId(),
					ConvDB.TYPE_INT,
					DBConstants.MIGRATE_UPD_POS_BATCH_ID));

			fieldList.add(new FieldTypeValue(
					pi.get_fromDestId(),
					ConvDB.TYPE_STRING,
					DBConstants.MIGRATE_UPD_POS_FRM_DEST_ID));

			fieldList.add(new FieldTypeValue( 	
					pi.get_physFileId(),
					ConvDB.TYPE_STRING, 
					DBConstants.MIGRATE_UPD_POS_PHYS_FILE_ID));
						
			int rows = ExecutePSUpdate(fieldList, rsMigrate);
			logger.log(Level.INFO, "Updated Row Count: " + rows);
		
		} catch (ConvDBException e) {
			String msg = e.getMessage();
			logger.log(Level.SEVERE, msg);
			throw new ShutdownException(msg);
		}
		catch(Exception e){
			String msg = String.format("Unknown Exception in UpdateStatus For dbType <%s> <%s>", get_dbType(), sql);
			logger.log(Level.SEVERE,msg);
			logger.log(Level.SEVERE,e.getMessage());
			throw new ShutdownException(msg);
		}
	}
	
	public void VDRS_DBUpdate(final DestLocProcessItem pi) throws ShutdownException{
		String sql="";
		try {
			ArrayList<FieldTypeValue> fieldList = new ArrayList<FieldTypeValue>();
			fieldList.add(new FieldTypeValue( 	
											pi.get_frmRespCode(),
											ConvDB.TYPE_INT, 
											DBConstants.VDRS_UPD_POS_FRM_RET_CD));
			
			fieldList.add(new FieldTypeValue(
											pi.get_frmRespMsg(),
											ConvDB.TYPE_STRING, 
											DBConstants.VDRS_UPD_POS_FRM_RET_MSG));
			
			fieldList.add(new FieldTypeValue(
											pi.get_frmBegTs(),
											ConvDB.TYPE_TIMESTAMP,
											DBConstants.VDRS_UPD_POS_FRM_BEG_TS));
			
			fieldList.add(new FieldTypeValue(
											pi.get_frmEndTs(),
											ConvDB.TYPE_TIMESTAMP,
											DBConstants.VDRS_UPD_POS_FRM_END_TS));
			
			fieldList.add(new FieldTypeValue(
											pi.get_frmFileSize(),
											ConvDB.TYPE_LONG, 
											DBConstants.VDRS_UPD_POS_FRM_FILE_SZ));
			
			fieldList.add(new FieldTypeValue(
											pi.get_snowBndFrmtCode(),
											ConvDB.TYPE_INT, 
											DBConstants.VDRS_UPD_POS_SB_FRMT_CD));
			
			fieldList.add(new FieldTypeValue(
											pi.get_frmCheckSum(),
											ConvDB.TYPE_STRING, 
											DBConstants.VDRS_UPD_POS_FRM_CHECKSUM));

			fieldList.add(new FieldTypeValue(
											pi.get_schemaNm(),
											ConvDB.TYPE_STRING,
											DBConstants.VDRS_UPD_POS_SCHEMA_NM));

			fieldList.add(new FieldTypeValue(
											pi.get_batchId(),
											ConvDB.TYPE_INT,
											DBConstants.VDRS_UPD_POS_BATCH_ID));

			fieldList.add(new FieldTypeValue(
											pi.get_fromDestId(),
											ConvDB.TYPE_STRING,
											DBConstants.VDRS_UPD_POS_FRM_DEST_ID));
			
			fieldList.add(new FieldTypeValue(
											pi.get_physFileId(),
											ConvDB.TYPE_STRING,
											DBConstants.VDRS_UPD_POS_PHYS_FILE_ID));

			int rows = ExecutePSUpdate(fieldList, rsUpdateVRSD);		
			logger.log(Level.INFO, "Updated Row Count: " + rows);
		} catch (ConvDBException e) {
			String msg = e.getMessage();
			logger.log(Level.SEVERE, msg);
			throw new ShutdownException(msg);
		}
		catch(Exception e){
			String msg = String.format("Unknown Exception in UpdateStatus For dbType <%s> <%s>", get_dbType(), sql);
			logger.log(Level.SEVERE,msg);
			logger.log(Level.SEVERE,e.getMessage());
			throw new ShutdownException(msg);
		}
	}
	
	private String GetMigrate_UpdateSql() throws ShutdownException{
		String sql = "";
		try{
			
			switch(get_dbType()){
				case DB2:
					sql = String.format(DBConstants.UPDATE_MIGRATE_DB2, get_schemaTableNm());
					break;
				case ORACLE:
					sql = String.format(DBConstants.UPDATE_MIGRATE_ORA, get_schemaTableNm());
					break;
				default:
					//Should never get here because dbType is validated in the Constructor of CFWBaseDAO
					String msg = "Unknown dbType: " + get_dbType(); 
					throw new ShutdownException(msg);
			}
		}catch(Exception e){
			String msg = "Unknown Exception in GetMigrate_UpdateSql for dbType: " + get_dbType();
			logger.log(Level.SEVERE,e.getMessage());
			throw new ShutdownException(msg);
		}
		return sql;
	}
	
	private String GetVDRS_UpdateSql() throws ShutdownException{
		
		String sql = "";
		try{
			
			switch(get_dbType()){
				case DB2:
					sql = String.format(DBConstants.UPDATE_VDRS_DB2, get_schemaTableNm());
					break;
				case ORACLE:
					sql = String.format(DBConstants.UPDATE_VDRS_ORA, get_schemaTableNm());
					break;
				default:
					//Should never get here because dbType is validated in the Constructor of CFWBaseDAO
					String msg = "Unknown dbType: " + get_dbType(); 
					throw new ShutdownException(msg);
			}
		}catch(Exception e){
			String msg = "Unknown Exception in GetVDRS_UpdateSql for dbType: " + get_dbType();
			logger.log(Level.SEVERE,e.getMessage());
			throw new ShutdownException(msg);
		}
		return sql; 
	}
	
	/*************************************
	private String GetVDRS_UpdateSql(final DestLocProcessItem pi) throws ShutdownException{
		String sql = "";
		try{
			int frmRespCd = pi.get_frmRespCode();
			String frmRespMsg = pi.get_frmRespMsg();
			String frmBegTs = Get26StrTS(pi.get_frmBegTs());
			String frmEndTs = Get26StrTS(pi.get_frmEndTs());
			String toBegTs = Get26StrTS(pi.get_toBegTs());
			String toEndTs = Get26StrTS(pi.get_toEndTs());
			String physFileId = pi.get_physFileId();
			int sbImgFrmtCode = pi.get_snowBndFrmtCode();
			long fileSize = pi.get_frmFileSize();
			String checkSum = pi.get_frmCheckSum();
		
			sql = String.format(DBConstants.UPDATE_VDRS_FRMTR, get_schemaTableNm(),
					frmRespCd, frmRespMsg, frmBegTs, frmEndTs, toBegTs,
					toEndTs,fileSize,sbImgFrmtCode,checkSum, physFileId);
		}
		catch(IllegalFormatException e){
			String msg = String.format("Invalid Update FRMTR dbType <%s> <%s>", get_dbType(), sql);
			logger.log(Level.SEVERE, msg);
			logger.log(Level.SEVERE, e.getMessage());
			throw new ShutdownException(msg);
		}
		catch(Exception e){
			String msg = String.format("Invalid Update FRMTR dbType <%s> <%s>", get_dbType(), sql);
			logger.log(Level.SEVERE, msg);
			logger.log(Level.SEVERE, e.getMessage());
			throw new ShutdownException(msg);
		}
		logger.log(Level.INFO,sql);
		return sql;
	}
	******************************************/
	/*
	public String Get26StrFrmTS(final Timestamp ts){
		
		return String26FromTimestamp(ts);
	}
	*/
	//public static final String DB2 = "DB2";
	//private static final String DB2_MATCH = "^(?i)(" + DB2 + ")";
	
	//public static final String ORACLE = "ORACLE";
	//private static final String ORA_MATCH = "^(?i)(" + ORACLE +")";
	
	//private String _dbType;
	//private int _fetchSize;
	//private String _schemaTableNm;
	
	private String _lastPhysFileId;
	Recordset rsMigrate; // = new Recordset();
	Recordset rsUpdateVRSD; // = new Recordset();
	
	
}
