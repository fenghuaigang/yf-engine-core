package com.yupont.engine.env;

import jodd.util.concurrent.ThreadFactoryBuilder;

import java.util.concurrent.*;

/**
 * @author fenghuaigang
 * @date 2020/4/14
 */
public class ThreadContext {

    private static ExecutorService executorService;

    private ThreadContext(){}

    public static synchronized ExecutorService getExecutorService(){
        if(executorService == null){
            // 1、应用线程环境耗时操作主要集中在IO处理上，相反计算反而较少，线程池大小取cpu核数*2
            int availableProcessors = Runtime.getRuntime().availableProcessors();
            // 2、构造一个定长线程池，可控制线程最大并发数，超出的线程会在队列中
            ThreadFactory namedThreadFactory = new ThreadFactoryBuilder().setNameFormat("common-actuator-pool").get();
            executorService = new ThreadPoolExecutor(availableProcessors, availableProcessors * 2, 0L, TimeUnit.MILLISECONDS,
                    new LinkedBlockingQueue<>(1024), namedThreadFactory, new ThreadPoolExecutor.AbortPolicy());
        }
        return executorService;
    }

}
