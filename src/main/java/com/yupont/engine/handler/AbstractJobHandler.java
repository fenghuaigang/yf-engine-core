package com.yupont.engine.handler;

import com.yupont.engine.log.EngineLog;
import lombok.extern.slf4j.Slf4j;

/**
 * @author fenghuaigang
 * @date 2020/4/14
 */
@Slf4j
public abstract class AbstractJobHandler<T> implements JobHandler<T>{

    @Override
    public void onEvent(T event, long sequence, boolean endOfBatch) throws Exception {
        this.onEvent(event);
    }
    /**
     *  实现Distruptor封装handler处理流程
     *  1、可做执行前环境参数初始化
     *  2、主要处理逻辑
     *  3、后续处理
     * @methodName onEvent
     * @param event 抽象的事件定义
     * @return
     * @author fenghuaigang
     * @date 2020/2/14
     */
    @Override
    public void onEvent(T event) throws Exception {
        if(event == null){
            log.error("- {} -: event 事件不能为空",getHandlerName());
            throw new NullPointerException(String.format("- s% -: event 事件不能为空",getHandlerName()));
        }
        //1、可做执行前环境参数初始化
        log.info("- {} -执行前置;入参：{}",getHandlerName(),event);
        doBefore(event);
        //2、主要处理逻辑
        log.info("- {} -执行中;入参：{}",getHandlerName(),event);
        handle(event);
        log.info("- {} -执行中;出参：{}",getHandlerName(),event);
        //3、后续处理
        log.info("- {} -执行后置;入参：{}",getHandlerName(),event);
        doLast(event);
        log.info("- {} -执行完毕",getHandlerName());
    }
    /**
     *  提供默认的日志打印开启方法
     * @methodName logStart
     * @param engineLog
     * @return
     * @author fenghuaigang
     * @date 2020/2/14
     */
    protected void logStart(EngineLog engineLog){
        engineLog.startLog();
        engineLog.logNoBr("<START:{}>",getHandlerName());
    }

    /**
     *  提供默认的日志打印关闭方法
     * @methodName logEnd
     * @param engineLog
     * @return
     * @author fenghuaigang
     * @date 2020/2/14
     */
    protected void logEnd(EngineLog engineLog){
        engineLog.log("<END:{}>",getHandlerName());
        engineLog.flush();
    }

    /**
     * 前置
     * @methodName doBefore
     * @param t 事件
     * @return
     * @author fenghuaigang
     * @date 2020/1/6
     */
    protected abstract void doBefore(T t);

    /**
     * 后置执行
     * @methodName doLast
     * @param t 事件
     * @return
     * @author fenghuaigang
     * @date 2020/1/6
     */
    protected abstract void doLast(T t);

    /**
     *  抽象方法
     * @methodName handle
     * @param t 事件
     * @return
     * @author fenghuaigang
     * @date 2020/3/23
     */
    protected abstract void handle(T t);

}
