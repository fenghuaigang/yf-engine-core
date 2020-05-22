package com.yupont.engine.actuator;


import com.lmax.disruptor.ExceptionHandler;
import com.yupont.engine.callback.Callback;
import com.yupont.engine.handler.JobHandler;

import java.util.concurrent.TimeUnit;


/**
 * 启动器
 *
 * @author fenghuaigang
 * @date 2020/4/13
 */
public interface Actuator<T> {

    /**
     * 启动执行器
     *
     * @param
     * @return
     * @methodName run
     * @author fenghuaigang
     * @date 2020/4/14
     */
    void start();

    /**
     * 当前线程停止任务,阻塞等待直至所有handler处理完毕
     *
     * @param
     * @return
     * @methodName shutdown
     * @author fenghuaigang
     * @date 2020/4/14
     */
    void shutdown();

    /**
     * 当前线程停止任务，阻塞指定等待时间
     *
     * @param timeout 等待时间
     * @param timeUnit 时间单位
     * @return
     * @methodName shutdown
     * @author fenghuaigang
     * @date 2020/4/16
     */
    void shutdown(long timeout, TimeUnit timeUnit);

    /**
     * 停止任务。异步方式停止Actuator，等待内部处理事件完毕
     *
     * @param
     * @return
     * @methodName shutdownAsync
     * @author fenghuaigang
     * @date 2020/5/21
     */
    default void shutdownAsync() {
        new Thread(()->shutdown()).start();
    }


    /**
     * 停止任务。异步方式停止Actuator，指定等待时间
     * @param timeout 等待时间
     * @param timeUnit 时间单位
     * @return
     * @methodName shutdownAsync
     * @author fenghuaigang
     * @date 2020/5/21
     */
    default void shutdownAsync(long timeout, TimeUnit timeUnit) {
        new Thread(()->shutdown(timeout, timeUnit)).start();
    }

    /**
     * 添加要处理的业务数据
     *
     * @param t 存储业务数据的bean
     * @return
     * @methodName addContext
     * @author fenghuaigang
     * @date 2020/4/14
     */
    Actuator addContext(T t);

    /**
     * 添加回调方法
     *
     * @param callbacks 实现callback的实例类
     * @return
     * @methodName addCallBack
     * @author fenghuaigang
     * @date 2020/4/14
     */
    Actuator addCallBack(Callback<T>... callbacks);

    /**
     * 添加业务步骤
     *
     * @param jobHandlers 引擎任务处理实现类
     * @return
     * @methodName addNextHandlers
     * @author fenghuaigang
     * @date 2020/4/14
     */
    Actuator addNextHandlers(JobHandler<T>... jobHandlers);

    /**
     * 添加异常处理
     *
     * @param exceptionHandler
     * @return
     * @methodName addExceptionHandler
     * @author fenghuaigang
     * @date 2020/4/16
     */
    Actuator addExceptionHandler(ExceptionHandler<T> exceptionHandler);
}
