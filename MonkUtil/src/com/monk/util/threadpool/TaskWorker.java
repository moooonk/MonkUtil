/**
 * 
 */
package com.monk.util.threadpool;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.CountDownLatch;

/**
 * @author huangguanlin
 *
 * 2018年4月8日
 */
public class TaskWorker extends Thread{
	private volatile boolean running = true;
	private volatile boolean shotdowning = false;
	private BlockingQueue<IWorkerTask> taskQueue;
	private volatile long currentTaskStartTime;
	private volatile IWorkerTask currentTask;
	private long taskTimeOut;

	public TaskWorker(String name, int queueSize, long taskTimeOut) {
		this(name, queueSize, taskTimeOut, new ArrayBlockingQueue<>(queueSize));
	}
	
	public TaskWorker(String name, int queueSize, long taskTimeOut, BlockingQueue<IWorkerTask> taskQueue) {
		super(name);
		this.taskQueue = taskQueue;
		this.setDaemon(true);
		this.taskTimeOut = Math.max(taskTimeOut, 1000);
	}

	public void run() {
		while (this.running) {
			try {
				currentTask = (IWorkerTask) this.taskQueue.take();
				currentTaskStartTime = System.currentTimeMillis();
				currentTask.run();
				currentTask = null;
			} catch (InterruptedException arg1) {
				if (this.running) {
					
				}
			} catch (Throwable arg2) {
				
			}
		}

	}

	public boolean acceptTask(IWorkerTask task) {
		if (this.shotdowning) {
			

			return false;
		} else {
			boolean ok = this.taskQueue.offer(task);
			if (!ok) {
				
			}

			return ok;
		}
	}

	public int getTaskQueueSize() {
		return this.taskQueue.size();
	}

	public void shutdown(CountDownLatch doneSignal) {
		this.shotdowning = true;
		if (!this.putLastTask(doneSignal)) {
			this.doLastTask(doneSignal);
		}
	}

	private boolean putLastTask(CountDownLatch doneSignal) {
      return this.taskQueue.offer(null);
   }

	private void doLastTask(CountDownLatch doneSignal) {
		this.running = false;
		doneSignal.countDown();
	}

	public void shutdownNow() {
		this.shotdowning = true;
		this.running = false;
		this.interrupt();
		this.stop();
	}
	
	public boolean selfCheck(){
		if(currentTask != null){
			if(System.currentTimeMillis() - currentTaskStartTime > taskTimeOut){
				return false;
			}
		}
		return true;
	}
	
	public BlockingQueue<IWorkerTask> stopAndGetTaskQueue(){
		this.running = false;
		return taskQueue;
	}
	
}
