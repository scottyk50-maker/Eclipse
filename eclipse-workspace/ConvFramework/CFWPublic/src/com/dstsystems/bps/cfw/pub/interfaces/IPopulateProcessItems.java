package com.dstsystems.bps.cfw.pub.interfaces;

import java.util.Collection;

import com.dstsystems.bps.cfw.pub.exceptions.ShutdownException;


public interface IPopulateProcessItems  {
	public boolean KeepLoading();
	public Collection<IProcessItem> LoadQueue( ) throws ShutdownException;
	public void StartupInitialize(final IConfig config)throws ShutdownException;
	public void ShutdownCleanup();
}
