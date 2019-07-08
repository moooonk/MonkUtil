/**
 * 
 */
package com.monk.util.network;

import java.util.concurrent.ScheduledThreadPoolExecutor;

/**
 * @author huangguanlin
 * 简易线程池demo
 * 2016年12月23日
 */
public class ServerScheduled {
	public static ServerScheduled instance = new ServerScheduled();
	
	private ScheduledThreadPoolExecutor executor;
	
	private ServerScheduled(){
		executor = new ScheduledThreadPoolExecutor(1);
	}
	
	public ScheduledThreadPoolExecutor getExecutor(){
		return executor;
	}
	
	public void shutdownNow() {
		executor.shutdownNow();
	}

}
