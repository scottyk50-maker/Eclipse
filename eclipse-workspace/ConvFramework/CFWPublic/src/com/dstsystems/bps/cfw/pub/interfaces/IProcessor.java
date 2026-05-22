package com.dstsystems.bps.cfw.pub.interfaces;

import com.dstsystems.bps.cfw.pub.exceptions.RestartConsumerException;
import com.dstsystems.bps.cfw.pub.exceptions.ShutdownException;


public interface IProcessor  {
	//public void StartupInitialize(final DBConfiguration dbConfig)throws ShutdownException;
	public void StartupInitialize(final IConfig config)throws ShutdownException;
	public void ShutdownCleanup();
	public void ProcessQueueObject(final IProcessItem processItem) throws RestartConsumerException,ShutdownException;
}