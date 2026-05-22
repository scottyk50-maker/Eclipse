Select 
  distinct(w00.Objectid) --, wg2.collection_id, Wg8.Phys_File_Id, Wg8.Object_Format
  --w00.Objectid
From 
  W00u999s W00
Inner Join Wg2u999s Wg2 On
(
  Wg2.Collection_Id = W00.Objectid
)
Inner Join Wg8u999s Wg8 On
(
wg8.phys_file_id = Wg2.Phys_File_Id
)
--Inner Join Y09u999s Y09 On
--(
--  y09.docid = w00.objectid
--)
order by W00.Objectid desc;



create or replace 
Procedure           ProviderGetCollIDs
(
  pObjformat In Varchar2 Default 'Unknwn' 
, Pcollection_Id In Varchar2  
, P_CURSOR OUT types.GenCursor  
) AS 
BEGIN
  If pCollection_Id Is Not Null Then
    OPEN P_CURSOR FOR 
      With Drvrsql As
      (
        Select 
          Count(*),
          Wg2.Collection_Id 
        From 
          Awdpowner.Wg8u999s Wg8
        Inner Join Awdpowner.Wg2u999s Wg2 On
        (
          Wg2.phys_file_id = Wg8.Phys_File_Id
        )
        Where 
          Wg8.Object_Format =pObjformat AND wg2.collection_id > pCollection_Id
        Group By 
          Wg2.Collection_Id 
        Order By 
          Wg2.Collection_Id
    )
    select 
      collection_id 
    From 
      Drvrsql;
  ELSE 
      OPEN P_CURSOR FOR 
      With Drvrsql As
      (
        Select 
          Count(*),
          Wg2.Collection_Id 
        From 
          Awdpowner.Wg8u999s Wg8
        Inner Join Awdpowner.Wg2u999s Wg2 On
        (
          Wg2.phys_file_id = Wg8.Phys_File_Id
        )
        Where 
          Wg8.Object_Format =pObjformat 
        Group By 
          Wg2.Collection_Id 
        Order By 
          Wg2.Collection_Id
      )
      select 
        collection_id 
      From 
        Drvrsql;
  END IF;
  
END ProviderGetCollIDs;


Insert into AWDPOWNER.WG2U999S (COLLECTION_ID,COLLECTION_SEQ_NBR,DATA_REVISION,PHYS_FILE_ID,PHYS_FILE_INDX,PHYS_FILE_PAR_COLL) values ('GLP_COL_1',1,0,'GLP_PHYS_1_1',0, '              ');
Insert into AWDPOWNER.WG2U999S (COLLECTION_ID,COLLECTION_SEQ_NBR,DATA_REVISION,PHYS_FILE_ID,PHYS_FILE_INDX,PHYS_FILE_PAR_COLL) values ('GLP_COL_1',2,0,'GLP_PHYS_1_2',0, '              ');
Insert into AWDPOWNER.WG3U999S (COLLECTION_ID,COLLECTION_SEQ_NBR,COLLECTION_VERSION,DATA_REVISION,PHYS_FILE_ID,PHYS_FILE_INDX,PHYS_FILE_PAR_COLL) values ('012000000001  ',1,1,0,'012000000002  ',0,'              ');

Insert into AWDPOWNER.WG8U999S (PHYS_FILE_ID,DATA_REVISION,INDEX_COUNT,OBJECT_FORMAT,PHYS_FILE_CRDATTIM,PHYS_FILE_CRUSER,INTERNAL_BOX_ID,STORED_PAGENUM) values ('GLP_PHYS_1_1  ',0,1,'tif','2012-09-19-18.39.37.804020','DSTSETUP',0,0);
Insert into AWDPOWNER.WG8U999S (PHYS_FILE_ID,DATA_REVISION,INDEX_COUNT,OBJECT_FORMAT,PHYS_FILE_CRDATTIM,PHYS_FILE_CRUSER,INTERNAL_BOX_ID,STORED_PAGENUM) values ('GLP_PHYS_1_2  ',0,1,'tif','2015-05-11-14.00.05.244080','DSTSETUP',0,0);

Insert into AWDPOWNER.WH0U999S (PHYS_FILE_ID,DESTINATION_ID,DATA_REVISION,LOCATOR,AVAILABLE_FLAG) values ('GLP_PHYS_1_1  ','1',0,'awd/glp/tst1.tif','Y');
Insert into AWDPOWNER.WH0U999S (PHYS_FILE_ID,DESTINATION_ID,DATA_REVISION,LOCATOR,AVAILABLE_FLAG) values ('GLP_PHYS_1_2  ','1',0,'awd/glp/tst2.tif','Y');



SET PATH "QSYS","QSYS2","SYSPROC","SYSIBMADM","DT40903" ; 
 drop PROCEDURE DT40903.SPGETOBJECTID;
CREATE PROCEDURE DT40903.SPGETOBJECTID ( IN P_START_TS TIMESTAMP, IN P_END_TS TIMESTAMP ) 
	DYNAMIC RESULT SETS 1 
	LANGUAGE SQL 
	SPECIFIC DT40903.SPGETOBJECTID 
	NOT DETERMINISTIC 
	MODIFIES SQL DATA 
	CALLED ON NULL INPUT 
	SET OPTION  ALWBLK = *ALLREAD , 
	ALWCPYDTA = *OPTIMIZE , 
	COMMIT = *NONE , 
	CLOSQLCSR = *ENDMOD , 
	DECRESULT = (31, 31, 00) , 
	DFTRDBCOL = *NONE , 
	DYNDFTCOL = *NO , 
	DYNUSRPRF = *USER , 
	SRTSEQ = *HEX   
	BEGIN 
DECLARE RESULT_SET_1 CURSOR WITH RETURN TO CLIENT FOR 
WITH UNIQUE AS(
SELECT COUNT(*), OBJECTID FROM W00U999S  where  CRDATTIM BETWEEN P_START_TS AND P_END_TS group by OBJECTID
)
SELECT OBJECTID as COLLECTION_ID FROM UNIQUE FOR READ ONLY WITH UR; 
OPEN RESULT_SET_1 ; 
END  ; 
  
COMMENT ON PARAMETER SPECIFIC PROCEDURE DT40903.SPGETOBJECTID 
( P_START_TS IS 'Start Timestamp of Search' , 
	P_END_TS IS 'End Timestamp of Search' ) ;

call  DT40903.SPGETOBJECTID('2004-03-03 00:00:00.300001', '2004-03-03 00:00:00.300001');







