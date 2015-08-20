package com.samuelnotes.commonlibs.utils;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.atomic.AtomicInteger;
public class MThreadFactory {
	
	/** 任务执行器. */
	public static Executor mExecutorService = null;
	
	/** 保存线程数量 . */
	private static final int CORE_POOL_SIZE = 4;
	
	/** 最大线程数量 . */
    private static final int MAXIMUM_POOL_SIZE = 8;
    
    /** 活动线程数量 . */
    private static final int KEEP_ALIVE = 8;
    
    /** 线程池队列的默认大小  */ 
    private static final int QUEUE_DEFSIZE = 8;

    /** 线程工厂 . */
    private static final ThreadFactory mThreadFactory = new ThreadFactory() {
    	/// 自增计数器 
        private final AtomicInteger mCount = new AtomicInteger(1);

        public Thread newThread(Runnable r) {
            return new Thread(r, "CurThread #" + mCount.getAndIncrement());
        }
    };

    /** 队列. */
    private static final BlockingQueue<Runnable> mPoolWorkQueue =  new LinkedBlockingQueue<Runnable>(QUEUE_DEFSIZE);
    
    /**
	 * 根据CPU的core数来 get executor .
	 */
    public static Executor getExecutorService() { 
        if (mExecutorService == null) { 
//        	int numCores = Runtime.getRuntime().availableProcessors();
//        	mExecutorService = new ThreadPoolExecutor(numCores * CORE_POOL_SIZE,numCores * MAXIMUM_POOL_SIZE,numCores * KEEP_ALIVE,
//                    TimeUnit.SECONDS, mPoolWorkQueue, mThreadFactory);
        	mExecutorService  = Executors.newFixedThreadPool(CORE_POOL_SIZE, mThreadFactory);
        }
        return mExecutorService;
    } 
	
	
}
