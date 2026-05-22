package com.dstsystems.bps.cfw.framework;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.dstsystems.bps.cfw.pub.exceptions.RestartConsumerException;
import com.dstsystems.bps.cfw.pub.exceptions.ShutdownException;
import com.dstsystems.bps.cfw.pub.interfaces.IConfig;
import com.dstsystems.bps.cfw.pub.interfaces.IProcessItem;
import com.dstsystems.bps.cfw.pub.interfaces.IProcessor;



public class FrameConsumer implements Runnable {
	static Logger logger = Logger.getLogger(FrameConsumer.class.getName());
	
	public FrameConsumer(BlockingQueue<Object> queue, AtomicBoolean shutdownFlag, final IConfig config)throws ShutdownException{
		_queue = queue;
		_shutdownFlag = shutdownFlag;
		_stop=false;
		_config = config;
		_restartCount = 0;
		InstanciateConsumer();
		
	}
	
	private void InstanciateConsumer() throws ShutdownException{
		String consumerClassName="";
		
		try {
			consumerClassName = (String) _config.get_consumerClass();
			logger.log(Level.INFO, "Creating Instance of Consumer: " + consumerClassName);
			_consumer = (IProcessor) Class.forName(consumerClassName).newInstance();
			_consumer.StartupInitialize(_config);
		} catch (InstantiationException | IllegalAccessException | ClassNotFoundException e) {
			throw new ShutdownException("Failed to Load Consumer Class: " + consumerClassName);
		}
		catch(Exception e){
			throw new ShutdownException(e);
		}
		
	}
	
	@Override
	public void run() {
			
		while(!_stop && !_shutdownFlag.get()){
			try{
				_consumer.ProcessQueueObject((IProcessItem)_queue.take());
			} catch (InterruptedException e) {
				logger.log(Level.SEVERE, "Interrupt Received, Settings Stop = True");
				_stop = true;
			}catch(RestartConsumerException e){
				logger.log(Level.SEVERE, "Retart Request Received: " + e.getMessage());
				_consumer.ShutdownCleanup();
				boolean createSuccess = false;
				while(_restartCount++ < 10){
					try{
						InstanciateConsumer();
						createSuccess = true;
						 break;
					}catch(ShutdownException ex){
						String msg = String.format("Failed to Restart Consumer<%d>: %s",_restartCount,ex.getMessage());
						logger.log(Level.SEVERE,msg);
						if(_consumer != null){
							_consumer.ShutdownCleanup();
						}
						_consumer = null;
					}
					try {
						Thread.sleep((_config.get_providerSleep() *60));
					} catch (InterruptedException e1) {	}
					
				}
				if(!createSuccess){
					ShutdownCleanup();
				}
			}
			catch (ShutdownException e) {
				logger.log(Level.SEVERE, "Shutdown Request Received: " + e.getMessage());
				ShutdownCleanup();
			}
		}
		_consumer.ShutdownCleanup();
	}
		
	public void ShutdownCleanup() {
		logger.log(Level.INFO, "Received ShutdownCleanup command");
		_stop = true;
		_shutdownFlag.set(true);
		
	}
	
	
	private final BlockingQueue<Object> _queue;
	private final AtomicBoolean _shutdownFlag;
	IConfig _config;
	private boolean _stop;
	private IProcessor _consumer;
	int _restartCount;
	
}
