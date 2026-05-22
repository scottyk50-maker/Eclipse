package com.dstsystems.bps.cfw.framework;

import java.util.concurrent.Future;



public class WorkFutureTracker {
	private final FrameConsumer _worker;
	private final Future<?> _future;

	
	
	public WorkFutureTracker(FrameConsumer worker, Future<?> f) {
		_worker = worker;
		_future = f;
	}

	public void stopWorkerTread() {
		_worker.ShutdownCleanup();
		
		_future.cancel(true);
		//http://stackoverflow.com/questions/1252190/how-to-wait-for-a-set-of-threads-to-complete
	}
}
