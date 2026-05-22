package com.dstsystems.bps.destlocaft.ipopulateprocessitems;

import java.util.ArrayList;
import java.util.Collection;
import java.util.logging.Level;
import java.util.logging.Logger;


import com.dstsystems.bps.cfw.pub.exceptions.ShutdownException;
import com.dstsystems.bps.cfw.pub.interfaces.IConfig;
import com.dstsystems.bps.cfw.pub.interfaces.IPopulateProcessItems;
import com.dstsystems.bps.cfw.pub.interfaces.IProcessItem;
import com.dstsystems.bps.destlocaft.config.DestLocAFTConfig;
import com.dstsystems.bps.destlocaft.database.DestLocDAO;



public class PopulateItemsWithDestLoc implements IPopulateProcessItems {
	static Logger logger = Logger.getLogger(PopulateItemsWithDestLoc.class
			.getName());

	@Override
	public boolean KeepLoading() {
		return _keepLoading;
	}

	@Override
	public Collection<IProcessItem> LoadQueue() throws ShutdownException {
		ArrayList<IProcessItem> processList = new ArrayList<IProcessItem>();
		String msg;
		try {
			processList = (ArrayList<IProcessItem>) _dao.GetNextQueueItems(_config);
		} catch (ShutdownException e) {
			logger.log(Level.SEVERE, e.getMessage());
			throw e;
		} catch (Exception e) {
			msg = "Unknown Exception Occured LoadQueue, Throwing ShutdownException";
			logger.log(Level.SEVERE, msg);
			logger.log(Level.SEVERE, e.getMessage());
			throw new ShutdownException(msg);
		}
		return processList;
	}

		
	@Override
	public void StartupInitialize(IConfig config) throws ShutdownException {
		_config = (DestLocAFTConfig) config;
		
		_dao = new DestLocDAO(	_config.get_dbType(), 	_config.get_jdbcDriver(),
								_config.get_jdbcURL(), 	_config.get_dbUserId(),
								_config.get_dbPwd(), 	_config.get_provRetrieveCnt(),
								_config.get_schemaTableNm());
		_keepLoading = true;
	}
	
	

	@Override
	public void ShutdownCleanup() {
		_dao.Disconnect();
		_config = null;
		_keepLoading = false;
	}

	private boolean _keepLoading;
	private DestLocAFTConfig _config;
	private DestLocDAO _dao;

}
