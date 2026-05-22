package com.dstsystems.bps.destlocaft.database;

public class DBConstants {

		
	public static final int SCHEMA_NM = 1;
	public static final int BATCH_ID = 2;
	public static final int PROCESS_FLAG = 3;
	public static final int ACTION_FLAG = 4;
	public static final int PHYS_FILE_ID = 5;
	public static final int FRM_DEST_ID = 6;
	public static final int FRM_LOC = 7;
	public static final int TO_DEST_ID = 8;
	public static final int TO_LOC = 9;
	
	public static final int DEST_ID = 1;
	
	public static final int SP_RTRV_INPT_POS_SCHEMA_NM = 1;
	public static final int SP_RTRV_INPT_POS_BATCH_ID = 2;
	public static final int SP_RTRV_INPT_POS_PROCCESS_FLAG = 3;
	public static final int SP_RTRV_INPT_POS_PHYS_FILE_ID = 4;
	
		
	public static final int MIGRATE_UPD_POS_FRM_RET_CD = 1;
	public static final int MIGRATE_UPD_POS_FRM_RET_MSG = 2;
	public static final int MIGRATE_UPD_POS_FRM_BEG_TS = 3;
	public static final int MIGRATE_UPD_POS_FRM_END_TS = 4;
	public static final int MIGRATE_UPD_POS_FRM_FILE_SZ = 5;
	public static final int MIGRATE_UPD_POS_FRM_CHECKSUM = 6;
	public static final int MIGRATE_UPD_POS_TO_RET_CD = 7;
	public static final int MIGRATE_UPD_POS_TO_RET_MSG = 8;
	public static final int MIGRATE_UPD_POS_TO_BEG_TS = 9;
	public static final int MIGRATE_UPD_POS_TO_FILE_SZ = 10;
	public static final int MIGRATE_UPD_POS_TO_CHECKSUM = 11;
	public static final int MIGRATE_UPD_POS_SB_FRMT_CD = 12;
	public static final int MIGRATE_UPD_POS_SCHEMA_NM = 13;
	public static final int MIGRATE_UPD_POS_BATCH_ID = 14;
	public static final int MIGRATE_UPD_POS_FRM_DEST_ID = 15;
	public static final int MIGRATE_UPD_POS_PHYS_FILE_ID = 16;
	
	public static final int VDRS_UPD_POS_FRM_RET_CD = 1;
	public static final int VDRS_UPD_POS_FRM_RET_MSG = 2;
	public static final int VDRS_UPD_POS_FRM_BEG_TS = 3;
	public static final int VDRS_UPD_POS_FRM_END_TS = 4;
	public static final int VDRS_UPD_POS_FRM_FILE_SZ = 5;
	public static final int VDRS_UPD_POS_SB_FRMT_CD = 6;
	public static final int VDRS_UPD_POS_FRM_CHECKSUM = 7;
	public static final int VDRS_UPD_POS_SCHEMA_NM = 8;
	public static final int VDRS_UPD_POS_BATCH_ID = 9;
	public static final int VDRS_UPD_POS_FRM_DEST_ID = 10;
	public static final int VDRS_UPD_POS_PHYS_FILE_ID = 11;
	
	public final static String UPDATE_MIGRATE_ORA = 
			"UPDATE "
					+ "%s "
				+ "SET "
				+ "FRM_RET_CD = ?,"
				+ "FRM_RET_MSG = ?,"
				+ "FRM_BEG_TS = ?,"
				+ "FRM_END_TS = ?,"
				+ "FRM_FILE_SZ  = ?,"
				+ "FRM_CHECKSUM = ?,"
				+ "TO_RET_CD = ?,"
				+ "TO_RET_MSG = ?,"
				+ "TO_BEG_TS = ?,"
				+ "TO_FILE_SZ  = ?,"
				+ "TO_CHECKSUM = ?,"
				+ "SB_FRMT_CD  = ?,"
				+ "PROCESS_FLAG = 'Y', "
				+ "TO_END_TS = current_timestamp "
		+ "WHERE "
			+ "SCHEMA_NM = ? AND "
			+ "BATCH_ID = ? AND "
			+ "FRM_DEST_ID = ? AND "
			+ "PHYS_FILE_ID = ?";
	
	public final static String UPDATE_MIGRATE_DB2 = 
			"UPDATE "
					+ "%s "
				+ "SET "
				+ "FRM_RET_CD = ?,"
				+ "FRM_RET_MSG = ?,"
				+ "FRM_BEG_TS = ?,"
				+ "FRM_END_TS = ?,"
				+ "FRM_FILE_SZ  = ?,"
				+ "FRM_CHECKSUM = ?,"
				+ "TO_RET_CD = ?,"
				+ "TO_RET_MSG = ?,"
				+ "TO_BEG_TS = ?,"
				+ "TO_FILE_SZ  = ?,"
				+ "TO_CHECKSUM = ?,"
				+ "SB_FRMT_CD  = ?,"
				+ "PROCESS_FLAG = 'Y', "
				+ "TO_END_TS = current_timestamp "
		+ "WHERE "
			+ "SCHEMA_NM = ? AND "
			+ "BATCH_ID = ? AND "
			+ "FRM_DEST_ID = ? AND "
			+ "PHYS_FILE_ID = ?";
	
	public final static String UPDATE_VDRS_DB2 = 
			"UPDATE "
					+ "%s "
				+ "SET "
					+ "FRM_RET_CD = ?,"
					+ "FRM_RET_MSG = ?,"
					+ "FRM_BEG_TS = ?,"
					+ "FRM_END_TS = ?,"
					+ "FRM_FILE_SZ  = ?,"
					+ "SB_FRMT_CD  = ?,"
					+ "FRM_CHECKSUM = ?,"
					+ "PROCESS_FLAG = 'Y', "
					+ "TO_END_TS = current_timestamp "
				+ "WHERE "
					+ "SCHEMA_NM = ? AND "
					+ "BATCH_ID = ? AND "
					+ "FRM_DEST_ID = ? AND "
					+ "PHYS_FILE_ID = ?";
	
	public final static String UPDATE_VDRS_ORA = 
			"UPDATE "
					+ "%s "
				+ "SET "
					+ "FRM_RET_CD = ?,"
					+ "FRM_RET_MSG = ?,"
					+ "FRM_BEG_TS = ?,"
					+ "FRM_END_TS = ?,"
					+ "FRM_FILE_SZ  = ?,"
					+ "SB_FRMT_CD  = ?,"
					+ "FRM_CHECKSUM = ?,"
					+ "PROCESS_FLAG = 'Y', "
					+ "TO_END_TS = current_timestamp(3) "
				+ "WHERE "
					+ "SCHEMA_NM = ? AND "
					+ "BATCH_ID = ? AND "
					+ "FRM_DEST_ID = ? AND "
					+ "PHYS_FILE_ID = ?";
	
	
	public final static String GetDestId_DB2 = 
			"WITH FIND_DEST as"
			+ "("
				+ "SELECT "
					+ "COUNT(*),"
					+ "FRM_DEST_ID AS DESTID "
				+ "FROM "
					+ "%s " 
				+ "GROUP BY " 
					+ "FRM_DEST_ID "
				+ "UNION ALL "
				+ "SELECT "
					+ "COUNT(*),"
					+ "TO_DEST_ID AS DESTID " 
				+ "FROM "
					+ "%s " 
				+ "WHERE "
					+ "TO_DEST_ID IS NOT NULL "
				+ "GROUP BY "
					+ "TO_DEST_ID"
			+ ")SELECT DISTINCT TRIM(DESTID) AS DEST_ID FROM FIND_DEST FOR READ ONLY WITH UR";
	
	public final static String GetDestId_ORA = 
			"WITH FIND_DEST as"
			+ "("
				+ "SELECT "
					+ "COUNT(*),"
					+ "FRM_DEST_ID AS DESTID "
				+ "FROM "
					+ "%s " 
				+ "GROUP BY " 
					+ "FRM_DEST_ID "
				+ "UNION ALL "
				+ "SELECT "
					+ "COUNT(*),"
					+ "TO_DEST_ID AS DESTID " 
				+ "FROM "
					+ "%s " 
				+ "WHERE "
					+ "TO_DEST_ID IS NOT NULL "
				+ "GROUP BY "
					+ "TO_DEST_ID"
			+ ")SELECT DISTINCT TRIM(DESTID) AS DEST_ID FROM FIND_DEST";
	
	public final static String PopItemsPhys_DB2 = 
			"SELECT "
				+ "trim(SCHEMA_NM) as SCHEMA_NM,"
				+ "BATCH_ID as BATCH_ID,"
				+ "trim(PROCESS_FLAG) as PROCESS_FLAG,"
				+ "trim(ACTION_FLAG) as ACTION_FLAG,"
				+ "trim(PHYS_FILE_ID) as PHYS_FILE_ID,"
				+ "trim(FRM_DEST_ID) as FRM_DEST_ID,"
				+ "trim(FRM_LOC) as FRM_LOC,"
				+ "trim(TO_DEST_ID) as TO_DEST_ID,"
				+ "trim(TO_LOC) as TO_LOC "
			+"FROM "
				+ "%s "
			+"WHERE "
				+ "SCHEMA_NM = '%s' "
				+ "AND BATCH_ID = %d "
				+ "AND PROCESS_FLAG = '%s' "
				+ "AND PHYS_FILE_ID >= '%s' "
			+ "ORDER BY "
				+ "PHYS_FILE_ID ASC "
			+ "FETCH FIRST %d ROWS ONLY FOR READ ONLY WITH UR";
	
	public final static String PopItemsPhys_ORA = 
			"SELECT "
				+ "trim(SCHEMA_NM) as SCHEMA_NM,"
				+ "BATCH_ID as BATCH_ID,"
				+ "trim(PROCESS_FLAG) as PROCESS_FLAG,"
				+ "trim(ACTION_FLAG) as ACTION_FLAG,"
				+ "trim(PHYS_FILE_ID) as PHYS_FILE_ID,"
				+ "trim(FRM_DEST_ID) as FRM_DEST_ID,"
				+ "trim(FRM_LOC) as FRM_LOC,"
				+ "trim(TO_DEST_ID) as TO_DEST_ID,"
				+ "trim(TO_LOC) as TO_LOC "
			+"FROM "
				+ "%s "
			+"WHERE "
				+ "SCHEMA_NM = '%s' "
				+ "AND BATCH_ID = %d "
				+ "AND PROCESS_FLAG = '%s' "
				+ "AND PHYS_FILE_ID >= '%s' "
				+ "AND ROWNUM <= %d "
			+ "ORDER BY "
				+ "PHYS_FILE_ID ASC";
	
	public final static String PopItems_DB2 = 
			"SELECT "
				+ "trim(SCHEMA_NM) as SCHEMA_NM,"
				+ "BATCH_ID as BATCH_ID,"
				+ "trim(PROCESS_FLAG) as PROCESS_FLAG,"
				+ "trim(ACTION_FLAG) as ACTION_FLAG,"
				+ "trim(PHYS_FILE_ID) as PHYS_FILE_ID,"
				+ "trim(FRM_DEST_ID) as FRM_DEST_ID,"
				+ "trim(FRM_LOC) as FRM_LOC,"
				+ "trim(TO_DEST_ID) as TO_DEST_ID,"
				+ "trim(TO_LOC) as TO_LOC "
			+"FROM "
				+ "%s "
			+"WHERE "
				+ "SCHEMA_NM = '%s' "
				+ "AND BATCH_ID = %d "
				+ "AND PROCESS_FLAG = '%s' "
			+ "ORDER BY "
				+ "PHYS_FILE_ID ASC "
			+ "FETCH FIRST %d ROWS ONLY FOR READ ONLY WITH UR";
	
	public final static String PopItems_ORA = 
			"SELECT "
				+ "trim(SCHEMA_NM) as SCHEMA_NM,"
				+ "BATCH_ID as BATCH_ID,"
				+ "trim(PROCESS_FLAG) as PROCESS_FLAG,"
				+ "trim(ACTION_FLAG) as ACTION_FLAG,"
				+ "trim(PHYS_FILE_ID) as PHYS_FILE_ID,"
				+ "trim(FRM_DEST_ID) as FRM_DEST_ID,"
				+ "trim(FRM_LOC) as FRM_LOC,"
				+ "trim(TO_DEST_ID) as TO_DEST_ID,"
				+ "trim(TO_LOC) as TO_LOC "
			+"FROM "
				+ "%s "
			+"WHERE "
				+ "SCHEMA_NM = '%s' "
				+ "AND BATCH_ID = %d "
				+ "AND PROCESS_FLAG = '%s' "
				+ "AND ROWNUM <= %d "
			+ "ORDER BY "
				+ "PHYS_FILE_ID ASC";
}
