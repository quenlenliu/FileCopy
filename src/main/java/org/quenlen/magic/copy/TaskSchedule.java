package org.quenlen.magic.copy;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;


class TaskSchedule {

    private  BlockingQueue mWorkQueue = new LinkedBlockingDeque();
    private  ThreadPoolExecutor mThreadPool;
    private static int MAX_THREAD_SIZE = 16;
    private static int CORE_THREAD_SIZE = 8;

    public TaskSchedule(int desireThreadSize) {
        if (desireThreadSize < 0) {
            desireThreadSize = 0;
        }
        MAX_THREAD_SIZE =  Runtime.getRuntime().availableProcessors();
        CORE_THREAD_SIZE = desireThreadSize > MAX_THREAD_SIZE ? MAX_THREAD_SIZE : desireThreadSize;
        System.out.println("Core Thread Size: " + CORE_THREAD_SIZE);
        mThreadPool = new ThreadPoolExecutor(CORE_THREAD_SIZE, MAX_THREAD_SIZE, 20L, TimeUnit.SECONDS, mWorkQueue);
        mThreadPool.allowCoreThreadTimeOut(true);
    }

    public  void execute(final ITask task) {
        mThreadPool.execute(task);
    }


    public void cancel() {
        mWorkQueue.clear();
    }

    public  void shutdown() {
        cancel();
        mThreadPool.shutdown();
    }
}
