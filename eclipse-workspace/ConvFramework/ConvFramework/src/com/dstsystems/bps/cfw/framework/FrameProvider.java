package com.dstsystems.bps.cfw.framework;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;


import com.dstsystems.bps.cfw.pub.exceptions.ShutdownException;
import com.dstsystems.bps.cfw.pub.interfaces.IConfig;
import com.dstsystems.bps.cfw.pub.interfaces.IPopulateProcessItems;
import com.dstsystems.bps.cfw.pub.interfaces.IProcessItem;



public class FrameProvider {
	
	static Logger logger = Logger.getLogger(FrameProvider.class.getName());
	
	public FrameProvider(final String configPath, final String configClassPath) throws ShutdownException{
		_queue = new LinkedBlockingQueue<Object>();
		_shutdownFlag = new AtomicBoolean(false);
		_wrkfutureList = new ArrayList<WorkFutureTracker>();
		//LoadDBConfiguration(configPath);
		LoadConfiguration(configPath,configClassPath );
		if (!ValidateLicense(_config.get_license())) {
			throw new ShutdownException("License Is Expired");
		}
		_threadCount = _config.get_threadCount();
		_executorService = Executors.newFixedThreadPool(_threadCount);
		_startTime = 0;
		
		InstanciateProvider();
	}
	
	private void LoadConfiguration(final String configPath, final String classPath)throws ShutdownException {
		JAXBContext jaxbContext = null;
		File file = null;//= new File("config.xml");
		try {
			file = new File(configPath);
			if(!file.isFile()){
				String msg = String.format("Failed to load ConfigFile <%s>", configPath);
				logger.log(Level.SEVERE, msg);
				throw new ShutdownException(msg);
			}
		
			_config = (IConfig) Class.forName(classPath).newInstance();
			jaxbContext = JAXBContext.newInstance(_config.getClass());
			Unmarshaller jaxbUnmarshaller = jaxbContext.createUnmarshaller();
			_config= (IConfig) jaxbUnmarshaller.unmarshal(file);
			
		} catch (JAXBException | ClassNotFoundException | InstantiationException | IllegalAccessException  e) {
			logger.log(Level.SEVERE, e.toString());
			throw new ShutdownException("Failed to Load Configuration Class: " + classPath);
			
		} catch(Exception e){
			throw new ShutdownException(e);
		}
		
		
	}
	
	/*
	private void LoadDBConfiguration(final String configPath)
			throws ShutdownException {
		FrameXMLConfig frameConfig = LoadCreds(configPath);
		if (!ValidateLicense(frameConfig.get_license())) {
			throw new ShutdownException("License Is Expired");
		}
		try {
			_dbConfig = new DBConfiguration(frameConfig.get_jdbcDriver(),
					frameConfig.get_jdbcURL(), frameConfig.get_dbUserId(),
					frameConfig.get_dbPwd(), frameConfig.get_configSchema(),
					frameConfig.get_configTable(), frameConfig.get_configId());

			_frameDBConfig = new FrameDBConfig(_dbConfig);
		} catch (DBConfigNotFoundException e) {
			throw new ShutdownException(e);
		}

	}
	*/
		
	private boolean ValidateLicense(final String licenseKey){
		boolean bRtrn = false;
		try 
		{
			SimpleDateFormat formatter;
		
			formatter = new SimpleDateFormat("yyyy-MM-dd");
			Date licenseDate = formatter.parse(licenseKey);
			Date currDate = new Date(System.currentTimeMillis());
			bRtrn = currDate.before(licenseDate);
				
		} catch (Exception e) {
			bRtrn = false;
		}
				
		return bRtrn;
	}
	private void InstanciateProvider() throws ShutdownException{

		String providerClass="";
		try {
			providerClass = _config.get_providerClass();
			
			logger.log(Level.INFO, "Creating Instance of Provider: " + providerClass);
			_provider = (IPopulateProcessItems) Class.forName(providerClass).newInstance();
			
			_provider.StartupInitialize(_config);
		} catch (InstantiationException | IllegalAccessException | ClassNotFoundException e) {
			throw new ShutdownException("Failed to Load Provider Class: " + providerClass);
		}catch(Exception e){
			throw new ShutdownException(e);
		}
	}
	
	/*
	private FrameXMLConfig LoadCreds(final String configPath, final String classPath)throws ShutdownException {
		FrameXMLConfig frameConfig = null;

		try {

			File file = new File(configPath);
			if(file.isFile()){
				System.out.println(file.getAbsolutePath());
			}
			JAXBContext jaxbContext = JAXBContext
					.newInstance(FrameXMLConfig.class);

			Unmarshaller jaxbUnmarshaller = jaxbContext.createUnmarshaller();
			frameConfig = (FrameXMLConfig) jaxbUnmarshaller.unmarshal(file);

		} catch (JAXBException e) {
			System.out.println(e.getMessage());
			String msg = String.format("Failed to load FrameConfig <%s>", configPath);
			logger.log(Level.SEVERE, msg);
			throw new ShutdownException(msg);
		}
		return frameConfig;
	}
	*/
	public void StartProvider(){

		_startTime = new Date().getTime();
		String msg;
		
		try{
			LaunchWorkers();
			
			while (_provider.KeepLoading() && !_shutdownFlag.get()){
				try{
					if (_queue.isEmpty()) {
						Collection<IProcessItem> coll = _provider.LoadQueue();
						if(coll.isEmpty()){
								logger.log(Level.INFO, "LoadQueue did not return any ProcessItems");
								if(_config.is_endNoRecFound()){
									_shutdownFlag.set(true);	
								}
						}
						else{
							_queue.addAll(coll);	
						}
						
						coll = null;
					} else {
						msg = "Queue Count: " + Integer.toString(_queue.size());
						// logger.log(Level.INFO, msg);
					}
					Thread.sleep(_config.get_providerSleep());	
				}
				catch (InterruptedException e) {
					msg = "Thread Interrupted";
					logger.log(Level.INFO, msg);
					_shutdownFlag.set(true);
					continue;
				}catch(UnsupportedOperationException | ClassCastException | NullPointerException | IllegalArgumentException | IllegalStateException e){
					logger.log(Level.SEVERE, "Critical Framework Error, Provider.LoadQueue Implementation provided Invalid data to the Queue");
					_shutdownFlag.set(true);
				}
				catch(ShutdownException e){
					throw new ShutdownException(e);
				}
				catch(Exception e){
					logger.log(Level.SEVERE, "Critical Uncaught Exception, Sending Shutdown Flag");
					_shutdownFlag.set(true);
				}
			}
		}catch(ShutdownException e){
			logger.log(Level.SEVERE,e.getMessage());
		}
		_provider.ShutdownCleanup();
		ShutdownThreadPool();
	}
	
	private void ShutdownThreadPool(){
		for(WorkFutureTracker wf: _wrkfutureList){
			wf.stopWorkerTread();
		}
		_executorService.shutdown();
		try {
			_executorService.awaitTermination(30, TimeUnit.SECONDS);
		} catch (InterruptedException e) {
			logger.log(Level.SEVERE,"awaitTermination Timeout occurred before Threads could be stopeed");	
		}

		while (!_executorService.isTerminated()) {
			// Wait until all threads are stopped
		}
		logger.log(Level.INFO, "All Worker Threads Stopped\n");
	}
	
	private void LaunchWorkers() throws ShutdownException {
		for (int i = 1; i <= _threadCount; i++) {
			//Runnable worker = InstanciateConsumer();
			Runnable worker = new FrameConsumer( _queue, _shutdownFlag, _config);
			Future<?> f = _executorService.submit(worker);
			WorkFutureTracker wf = new WorkFutureTracker( (FrameConsumer) worker, f);
			_wrkfutureList.add(wf);
		}
		
		String msg = String.format("Succesfully Created %d Workers\n", _wrkfutureList.size());
		logger.log(Level.INFO, msg);
	}

	/**********	
	private Runnable InstanciateConsumer() throws ShutdownException{
		String consumerClassName = _frameConfig.get_consumerClass();
		Runnable worker = null;
		try {
			
			worker = (ConsumerProcess) Class.forName(consumerClassName).newInstance();
			((ConsumerProcess) worker).set_queue(_queue);
			((ConsumerProcess) worker).set_shutdownFlag(_shutdownFlag);
		} catch (InstantiationException | IllegalAccessException | ClassNotFoundException e) {
			throw new ShutdownException("Failed to Load Consumer Class: " + consumerClassName);
		}
		return worker;
		
		
		String consumerClassName = _frameConfig.get_consumerClass();
		Constructor<?> con = null;
		Runnable worker = null;
		try {
			con = Class.forName(consumerClassName).getConstructor(
					BlockingQueue.class, AtomicBoolean.class);
			worker = (ConsumerProcess) con.newInstance( _queue, _shutdownFlag);
		}
		catch(ClassNotFoundException e){
			throw new ShutdownException("Failed to find Worker Class: " + consumerClassName);
		}
		catch (	NoSuchMethodException | SecurityException		| InstantiationException | 
				IllegalAccessException| IllegalArgumentException| InvocationTargetException e) {
			throw new ShutdownException("Failed to Launch Worker Class: " + consumerClassName);
		}
		return worker;
		
	
	}
	***/
		
	public static void main(String[] args) {
		FrameProvider provider = null;
		if(args.length !=2){
			logger.log(Level.SEVERE, "Usage: Framework <Path to Config.xml> <Config ClassPath>");
			return;
		}
		try {
		
			System.out.println(String.format("Path <%s> ConfigClassPath<%s>",args[0],args[1]));
			provider = new FrameProvider(args[0], args[1]);
			provider.StartProvider();

		} catch (ShutdownException e) {
			logger.log(Level.SEVERE, e.getMessage());
		}
	}
	
	//http://blog.bdoughan.com/2011/06/ignoring-inheritance-with-xmltransient.html
	//http://blog.bdoughan.com/2010/11/jaxb-and-inheritance-using-substitution.html
	//http://blog.bdoughan.com/2010/11/jaxb-and-inheritance-using-xsitype.html
	//http://stackoverflow.com/questions/10267743/how-do-i-make-an-abstract-class-work-with-jaxb
	
	
	private ExecutorService _executorService;
	private final BlockingQueue<Object> _queue;
	private ArrayList<WorkFutureTracker> _wrkfutureList;
	private AtomicBoolean _shutdownFlag;
	private final int _threadCount;
	//FrameConfig _frameConfig;
	long _startTime;
	private IPopulateProcessItems _provider;
	private IConfig _config;
	//DBConfiguration _dbConfig;
	//FrameDBConfig _frameDBConfig;
	
}
