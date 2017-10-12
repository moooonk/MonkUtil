/**
 * 
 */
package com.monk.util.configreader;

import org.aeonbits.owner.ConfigFactory;


/**
 * @author huangguanlin
 *
 * 2016年12月23日
 */
public class CoreConfig {
	public static final CoreConfig instance = new CoreConfig();
	
	private ServerConfig config;
	
	private CoreConfig(){
		config = ConfigFactory.create(ServerConfig.class);
	}

	public ServerConfig getConfig() {
		return config;
	}

}
