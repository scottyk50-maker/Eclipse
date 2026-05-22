update DEST_DASD set PROCFLAG='N', NBYTDC = null, CHECKSUM=null, response_cd=null, response_msg=null,UPLOAD_TM=null,end_tm=null;


CREATE TABLE DT40903.DEST_DASD 
( 
	PHYS_FILE_ID FOR COLUMN PHYS_00001 CHAR(14) CCSID 37 NOT NULL , 
	BEG_DESTINATION_ID FOR COLUMN BEG_D00001 CHAR(12) CCSID 37 NOT NULL , 
	BEG_LOCATOR FOR COLUMN BEG_L00001 VARCHAR(128) ALLOCATE(54) CCSID 37 NOT NULL , 
	END_DESTINATION_ID FOR COLUMN END_D00001 CHAR(12) CCSID 37 NOT NULL , 
	END_LOCATOR FOR COLUMN END_L00001 VARCHAR(128) ALLOCATE(32) CCSID 37 NOT NULL , 
	PROCFLAG CHAR(1) CCSID 37 NOT NULL , 
	NBYTDC INTEGER DEFAULT NULL , 
	CHECKSUM CHAR(128) CCSID 37 DEFAULT NULL , 
	RESPONSE_CD FOR COLUMN RESPO00001 INTEGER DEFAULT NULL , 
	RESPONSE_MSG FOR COLUMN RESPO00002 CHAR(128) CCSID 37 DEFAULT NULL , 
	UPLOAD_TM INTEGER DEFAULT NULL , 
	END_TM TIMESTAMP DEFAULT NULL , 
	CONSTRAINT DT40903.PK_DEST_HCP PRIMARY KEY( PHYS_FILE_ID ) 
);

CREATE TABLE DT40903.CONFIG_DASD
(
	CONFIG_ID INT NOT NULL,
	DATANAME CHAR(16) CCSID 37 NOT NULL ,
	DATATYPE INT NOT NULL,
	DATAVALUE CHAR(128) CCSID 37 NOT NULL ,
	CONSTRAINT DT40903.PK_CONFIG_DASD PRIMARY KEY(CONFIG_ID, DATANAME)
);

delete from DT40903.CONFIG_DASD;

insert into dt40903.CONFIG_DASD values(0,'threadCount',2,'1');
insert into dt40903.CONFIG_DASD values(0,'providerSleep',3,'5000');
insert into dt40903.CONFIG_DASD values(0,'providerClass',0,'com.dstsystems.bps.fw.impl.ipopulateprocessitems.PopulateItemsWithLocators');
insert into dt40903.CONFIG_DASD values(0,'consumerClass',0,'com.dstsystems.bps.fw.impl.iprocessor.DASD2DASD');


insert into dt40903.CONFIG_DASD values(1,'jdbcDriver',0,'com.ibm.as400.access.AS400JDBCDriver');
insert into dt40903.CONFIG_DASD values(1,'jdbcURL',0,'jdbc:as400://10.193.204.19;naming=system;errors=full');
insert into dt40903.CONFIG_DASD values(1,'dbUserId',0,'DT40903');
insert into dt40903.CONFIG_DASD values(1,'dbPwd',1,'p1e4vIsFCMxe3vuIeMrMwQ==');
insert into dt40903.CONFIG_DASD values(1,'schema',0,'DT40903');
insert into dt40903.CONFIG_DASD values(1,'tableNm',0,'DEST_DASD');
insert into dt40903.CONFIG_DASD values(1,'bgnDestId',0,'A');
insert into dt40903.CONFIG_DASD values(1,'provRetrieveCnt',2,'1');


insert into dt40903.DEST_DASD values('0001','A','D:\AFTGETS\AFT_IMAGES\tst1.tif','B', 'D:\AFTGETS\AFT_IMAGES\DASD2DASD\tst1.tif','N',null,null,null,null,null,null);
insert into dt40903.DEST_DASD values('0002','A','D:\AFTGETS\AFT_IMAGES\tst2.tif','B', 'D:\AFTGETS\AFT_IMAGES\DASD2DASD\tst2.tif','N',null,null,null,null,null,null);
insert into dt40903.DEST_DASD values('0003','A','D:\AFTGETS\AFT_IMAGES\tst3.tif','B', 'D:\AFTGETS\AFT_IMAGES\DASD2DASD\tst3.tif','N',null,null,null,null,null,null);
insert into dt40903.DEST_DASD values('0004','A','D:\AFTGETS\AFT_IMAGES\tst4.tif','B', 'D:\AFTGETS\AFT_IMAGES\DASD2DASD\tst4.tif','N',null,null,null,null,null,null);
insert into dt40903.DEST_DASD values('0005','A','D:\AFTGETS\AFT_IMAGES\tst5.tif','B', 'D:\AFTGETS\AFT_IMAGES\DASD2DASD\tst5.tif','N',null,null,null,null,null,null);
insert into dt40903.DEST_DASD values('0006','A','D:\AFTGETS\AFT_IMAGES\tst6.tif','B', 'D:\AFTGETS\AFT_IMAGES\DASD2DASD\tst6.tif','N',null,null,null,null,null,null);
insert into dt40903.DEST_DASD values('0007','A','D:\AFTGETS\AFT_IMAGES\tst7.tif','B', 'D:\AFTGETS\AFT_IMAGES\DASD2DASD\tst7.tif','N',null,null,null,null,null,null);
insert into dt40903.DEST_DASD values('0008','A','D:\AFTGETS\AFT_IMAGES\tst8.tif','B', 'D:\AFTGETS\AFT_IMAGES\DASD2DASD\tst8.tif','N',null,null,null,null,null,null);
insert into dt40903.DEST_DASD values('0009','A','D:\AFTGETS\AFT_IMAGES\tst9.tif','B', 'D:\AFTGETS\AFT_IMAGES\DASD2DASD\tst9.tif','N',null,null,null,null,null,null);
insert into dt40903.DEST_DASD values('0010','A','D:\AFTGETS\AFT_IMAGES\tst10.tif','B','D:\AFTGETS\AFT_IMAGES\DASD2DASD\tst10.tiff','N',null,null,null,null,null,null);
insert into dt40903.DEST_DASD values('0011','A','D:\AFTGETS\AFT_IMAGES\tst11.tif','B','D:\AFTGETS\AFT_IMAGES\DASD2DASD\tst11.tiff','N',null,null,null,null,null,null);
insert into dt40903.DEST_DASD values('0012','A','D:\AFTGETS\AFT_IMAGES\tst12.tif','B','D:\AFTGETS\AFT_IMAGES\DASD2DASD\tst12.tiff','N',null,null,null,null,null,null);
insert into dt40903.DEST_DASD values('0013','A','D:\AFTGETS\AFT_IMAGES\tst13.tif','B','D:\AFTGETS\AFT_IMAGES\DASD2DASD\tst13.tiff','N',null,null,null,null,null,null);
insert into dt40903.DEST_DASD values('0014','A','D:\AFTGETS\AFT_IMAGES\tst14.tif','B','D:\AFTGETS\AFT_IMAGES\DASD2DASD\tst14.tiff','N',null,null,null,null,null,null);
insert into dt40903.DEST_DASD values('0015','A','D:\AFTGETS\AFT_IMAGES\tst15.tif','B','D:\AFTGETS\AFT_IMAGES\DASD2DASD\tst15.tiff','N',null,null,null,null,null,null);
insert into dt40903.DEST_DASD values('0016','A','D:\AFTGETS\AFT_IMAGES\tst16.tif','B','D:\AFTGETS\AFT_IMAGES\DASD2DASD\tst16.tiff','N',null,null,null,null,null,null);
insert into dt40903.DEST_DASD values('0017','A','D:\AFTGETS\AFT_IMAGES\tst17.tif','B','D:\AFTGETS\AFT_IMAGES\DASD2DASD\tst17.tiff','N',null,null,null,null,null,null);



















insert into dt40903.CONFIG_DATA values(1,'schema',0,'DT40903');
insert into dt40903.CONFIG_DATA values(1,'tableNm',0,'DEST_DASD');
insert into dt40903.CONFIG_DATA values(1,'bgnDestId',0,'A');
insert into dt40903.CONFIG_DATA values(1,'provRetrieveCnt',2,'1');
insert into dt40903.CONFIG_DATA values(1,'hcpTenant',0,'LINCDEV');
insert into dt40903.CONFIG_DATA values(1,'hcpNameSpace',0,'IMAGES');
insert into dt40903.CONFIG_DATA values(1,'hcpRepository',0,'pdc-hcpdev.dstcorp.net/rest/Greg/Test/seven');
insert into dt40903.CONFIG_DATA values(1,'hcpValidHosts',0,'dstcorp.net');
insert into dt40903.CONFIG_DATA values(1,'hcpUserId',0,'Admin');
insert into dt40903.CONFIG_DATA values(1,'hcpPwd',1,'FVPAL1LFwY3key1exEaJ2Q==');


insert into dt40903.CONFIG_DATA values(1,'trustStorePath',0,'D:\\HCP_Testing\\Certs\\hcpdev.jks');
insert into dt40903.CONFIG_DATA values(1,'trustStorePwd',1,'uMNj3VvxvGeB4Rw2HTEMnw==');

--<_jdbcDriver>com.ibm.as400.access.AS400JDBCDriver</_jdbcDriver>
--<_jdbcURL>jdbc:as400://10.193.204.19;naming=system;errors=full</_jdbcURL>
--<_dbUserId>DT40903</_dbUserId>
--<_dbPwd>p1e4vIsFCMxe3vuIeMrMwQ==</_dbPwd>
--
--_jdbcDriver = (String)dbConfig.GetConfigValue("jdbcDriver");
--_jdbcURL = (String)dbConfig.GetConfigValue("jdbcURL");
--_dbServer = (String) dbConfig.GetConfigValue("dbServer");
--_dbUserId = (String) dbConfig.GetConfigValue("dbUserId");
--_dbPwd = (String) dbConfig.GetConfigValue("dbPwd");
--_schema = (String) dbConfig.GetConfigValue("schema");
--_tableName = (String) dbConfig.GetConfigValue("tableNm");
--_bgnDestId = (String) dbConfig.GetConfigValue("bgnDestId");
--_provRetrieveCnt = (int) dbConfig.GetConfigValue("provRetrieveCnt");




_bgnDestId
<_appSchema>DT40903</_appSchema>
    <_appSrcTable>DEST_HCP</_appSrcTable>
    <_bgnDestId>A</_bgnDestId>
    <_provRetrieveCnt>1</_provRetrieveCnt>
    <_hcpTenant>LINCDEV</_hcpTenant>
	<_hcpNameSpace>IMAGES</_hcpNameSpace>
	<_hcpRepository>pdc-hcpdev.dstcorp.net/rest/Greg/Test/seven</_hcpRepository>
    <_hcpValidHosts>dstcorp.net</_hcpValidHosts>
    <!--<_hcpUserId>pwamcen</_hcpUserId>-->
    <!--<_hcpPwd>flBnN4Q8EPGrZvF+/nyWuA==</_hcpPwd>-->
    <_hcpUserId>Admin</_hcpUserId>
    <_hcpPwd>FVPAL1LFwY3key1exEaJ2Q==</_hcpPwd>
    <_trustStorePath>D:\\HCP_Testing\\Certs\\hcpdev.jks</_trustStorePath>
    <_trustStorePwd>uMNj3VvxvGeB4Rw2HTEMnw==</_trustStorePwd>


CREATE TABLE DT40903.DEST_DASD ( 
	PHYS_FILE_ID FOR COLUMN PHYS_00001 CHAR(14) CCSID 37 NOT NULL , 
	BEG_DESTINATION_ID FOR COLUMN BEG_D00001 CHAR(12) CCSID 37 NOT NULL , 
	BEG_LOCATOR FOR COLUMN BEG_L00001 VARCHAR(128) ALLOCATE(54) CCSID 37 NOT NULL , 
	END_DESTINATION_ID FOR COLUMN END_D00001 CHAR(12) CCSID 37 NOT NULL , 
	END_LOCATOR FOR COLUMN END_L00001 VARCHAR(128) ALLOCATE(32) CCSID 37 NOT NULL , 
	PROCFLAG CHAR(1) CCSID 37 NOT NULL , 
	NBYTDC INTEGER DEFAULT NULL , 
	CHECKSUM CHAR(128) CCSID 37 DEFAULT NULL , 
	RESPONSE_CD INTEGER DEFAULT NULL , 
	RESPONSE_MSG CHAR(128) CCSID 37 DEFAULT NULL , 
	UPLOAD_TM INTEGER DEFAULT NULL , 
	END_TM TIMESTAMP DEFAULT NULL , 
	CONSTRAINT DT40903.PK_DEST_DASD PRIMARY KEY( PHYS_FILE_ID ) ) ; 