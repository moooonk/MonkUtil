/**
 * 
 */
package com.monk.util.threadpool;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;


import com.monk.util.network.udp.netty.ServerScheduled;


/**
 * @author huangguanlin
 *
 * 2018年4月8日
 */
public class SelfCheckThreadPool {
	private String name;
	private int workerCount;
	private TaskWorker[] workerArr;
	private CountDownLatch doneSignal;
	private int queueSize;

	public SelfCheckThreadPool(String name, int workerCount, int queueSize) {
		this.name = name;
		this.workerCount = workerCount;
		this.initWorkers(queueSize);
		this.queueSize = queueSize;
		ServerScheduled.instance.getExecutor().scheduleWithFixedDelay(() -> {
			for(int i = 0; i < workerCount; i++){
				if(!workerArr[i].selfCheck()){
					replaceThread(i);
				}
			}
		}, 0, 1000, TimeUnit.MILLISECONDS);
	}

	private void initWorkers(int queueSize) {
		this.workerArr = new TaskWorker[this.workerCount];

		for (int i = 0; i < this.workerCount; ++i) {
			this.workerArr[i] = new TaskWorker(String.format("%s-%s", new Object[]{this.name, Integer.valueOf(i + 1)}),
					queueSize, 1000);
			this.workerArr[i].start();
		}
	}

	public boolean inTaskWorker(int distributeKey) {
		TaskWorker worker = this.workerArr[distributeKey];
		return Thread.currentThread().getId() == worker.getId();
	}

	public boolean acceptTask(int distributeKey, IWorkerTask task) {
		TaskWorker worker = this.workerArr[distributeKey];
		return worker.acceptTask(task);
	}

	public String getName() {
		return this.name;
	}

	public int getWorkerCount() {
		return this.workerCount;
	}

	public int getTaskQueueSize() {
		int total = 0;
		TaskWorker[] arg1 = this.workerArr;
		int arg2 = arg1.length;

		for (int arg3 = 0; arg3 < arg2; ++arg3) {
			TaskWorker w = arg1[arg3];
			total += w.getTaskQueueSize();
		}

		return total;
	}

	public void shutdown() {
		this.doneSignal = new CountDownLatch(this.workerArr.length);
		TaskWorker[] arg0 = this.workerArr;
		int arg1 = arg0.length;

		for (int arg2 = 0; arg2 < arg1; ++arg2) {
			TaskWorker w = arg0[arg2];
			w.shutdown(this.doneSignal);
		}

	}

	public boolean awaitTermination(long timeout, TimeUnit unit) {
		try {
			boolean e = this.doneSignal.await(timeout, unit);
			return e;
		} catch (InterruptedException arg7) {
			
		} finally {
			
		}

		return false;
	}

	public void shutdownNow() throws InterruptedException {
		TaskWorker[] arg0 = this.workerArr;
		int arg1 = arg0.length;

		int arg2;
		TaskWorker t;
		for (arg2 = 0; arg2 < arg1; ++arg2) {
			t = arg0[arg2];
			t.shutdownNow();
		}

		arg0 = this.workerArr;
		arg1 = arg0.length;

		for (arg2 = 0; arg2 < arg1; ++arg2) {
			t = arg0[arg2];
			t.join();
		}
	}
	
	private void replaceThread(int index){
		TaskWorker worker = workerArr[index];
		TaskWorker newWorker = new TaskWorker(String.format("%s-%s", new Object[]{this.name, Integer.valueOf(index + 1)}), queueSize, 1000, worker.stopAndGetTaskQueue());
		workerArr[index] = newWorker;
		newWorker.start();
		worker.shutdownNow();
	}
	
	public static void main(String[] args) {
		SelfCheckThreadPool pool = new SelfCheckThreadPool("monk", 1, 1000);
		pool.acceptTask(0, new IWorkerTask(){

			@Override
			public void doRun() {
				while(true){
					System.out.println("monk");
				}
			}
			
		});
		pool.acceptTask(0, new IWorkerTask(){

			@Override
			public void doRun() {
				System.out.println("monk2");
			}
			
		});
	}
	
}
