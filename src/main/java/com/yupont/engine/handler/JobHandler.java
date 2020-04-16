package com.yupont.engine.handler;

import com.lmax.disruptor.EventHandler;
import com.lmax.disruptor.WorkHandler;

/**
 * 引擎任务处理
 * @author fenghuaigang
 * @date 2020/4/14
 */
public interface JobHandler<T> extends WorkHandler<T>, EventHandler<T> {

    /** 
     *  任务处理类型名称
     * @methodName getHandlerName
     * @param 
     * @return  
     * @author fenghuaigang 
     * @date 2020/4/14 
     */
    String getHandlerName();

}
