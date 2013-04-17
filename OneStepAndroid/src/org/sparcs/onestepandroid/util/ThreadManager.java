package org.sparcs.onestepandroid.util;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public enum ThreadManager {
	INSTANCE;
	private int NUMBER_OF_CORES =
            Runtime.getRuntime().availableProcessors();
	private final int KEEP_ALIVE_TIME = 1;
	private final TimeUnit KEEP_ALIVE_TIME_UNIT = TimeUnit.SECONDS;
	
	private final ThreadPoolExecutor threadPoolExecutor;
	private final BlockingQueue<Runnable> workQueue;
	ThreadManager() {
		workQueue = new LinkedBlockingQueue<Runnable>();
		threadPoolExecutor = new ThreadPoolExecutor(
                NUMBER_OF_CORES,       // Initial pool size
                NUMBER_OF_CORES,       // Max pool size
                KEEP_ALIVE_TIME,
                KEEP_ALIVE_TIME_UNIT,
                workQueue);
	}
	
	public ThreadPoolExecutor getPoolExcecutor() {
		return threadPoolExecutor;
	}
}
