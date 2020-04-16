package com.yupont.engine.actuator;


import com.lmax.disruptor.ExceptionHandler;
import com.yupont.engine.callback.Callback;
import com.yupont.engine.handler.JobHandler;

import java.util.concurrent.TimeUnit;


/**
 * 启动器
 * @author fenghuaigang
 * @date 2020/4/13
 */
public interface Actuator<T> {

    /** 
     *  启动执行器
     * @methodName run       
     * @param 
     * @return
     * @author fenghuaigang 
     * @date 2020/4/14 
     */
    void start();
    /** 
     *  停止任务,循环阻塞等待直至所有handler处理完毕
     * @methodName shutdown
     * @param 
     * @return
     * @author fenghuaigang 
     * @date 2020/4/14 
     */
    void shutdown();

    /**
     *  停止任务，指定等待时间
     * @methodName shutdown
     * @param timeout
     * @param timeUnit
     * @return
     * @author fenghuaigang
     * @date 2020/4/16
     */
    void shutdown(long timeout, TimeUnit timeUnit);

    /** 
     *  添加要处理的业务数据
     * @methodName addContext
     * @param t 存储业务数据的bean
     * @return  
     * @author fenghuaigang 
     * @date 2020/4/14 
     */
    Actuator addContext(T t);

    /** 
     *  添加回调方法
     * @methodName addCallBack       
     * @param callbacks 实现callback的实例类
     * @return
     * @author fenghuaigang 
     * @date 2020/4/14 
     */
    Actuator addCallBack(Callback<T>... callbacks);

    /** 
     *  添加业务步骤
     * @methodName addNextHandlers       
     * @param jobHandlers 引擎任务处理实现类
     * @return  
     * @author fenghuaigang 
     * @date 2020/4/14 
     */
    Actuator addNextHandlers(JobHandler<T>... jobHandlers);

    /** 
     *  添加异常处理
     * @methodName addExceptionHandler       
     * @param exceptionHandler
     * @return
     * @author fenghuaigang 
     * @date 2020/4/16 
     */
    Actuator addExceptionHandler(ExceptionHandler<T> exceptionHandler);
}
