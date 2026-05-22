package com.dstsystems.bps.cfw.pub.interfaces;

import com.dstsystems.bps.cfw.pub.exceptions.ShutdownException;

public interface IDBConfiguration {
	public void DBConnect(final String dbServer, final String dbUserId, final String dbPwd, final String schema, final String configTableNm, final int configId) throws ShutdownException;
}
