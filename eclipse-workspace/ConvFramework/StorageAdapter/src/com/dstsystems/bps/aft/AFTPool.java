package com.dstsystems.bps.aft;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AFTPool {
	
	public AFTPool(){
		
	}
	
	public AFTPool(final List<AFTConfig> configList){
		AddConfig(configList);
	}
	
	public AFTPool(final AFTConfig config){
		AddConfig(config);
	}
	
	public AFTPool(final String host, final int port, final String destId){
		_configMap.put(destId, new AFTConfig(host,port,destId,7,1));
	}
	
	public void AddConfig(final AFTConfig config){
		_configMap.put(config.get_DestId(), config);
	}
	
	public void AddConfig(final List<AFTConfig> configList){
		if(configList != null){
			for(AFTConfig config:configList){
				AddConfig(config);
			}	
		}
	}
	
	public final AFTConfig GetConfig(final String destId){
		return _configMap.get(destId);
	}
	
	public void RemoveConfig(final String destId){
		_configMap.remove(destId);
	}
	
	private Map<String,AFTConfig> _configMap = new HashMap<String,AFTConfig>();
}
