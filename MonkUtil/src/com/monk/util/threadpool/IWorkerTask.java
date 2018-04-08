/**
 * 
 */
package com.monk.util.threadpool;

/**
 * @author huangguanlin
 *
 * 2018年4月8日
 */
public abstract class IWorkerTask implements Runnable{

	
	
	@Override
	public void run() {
		doRun();
	}
	
	public abstract void doRun();

}
