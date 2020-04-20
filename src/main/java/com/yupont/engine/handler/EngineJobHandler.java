package com.yupont.engine.handler;

import com.yupont.engine.log.EngineLog;
import com.yupont.engine.env.JobContext;
import com.yupont.engine.log.FileLogger;

/**
 * 提供抽象的任务处理实现，任何继承该类的处理类，默认携带根据任务ID生成的日志文件
 * @author fenghuaigang
 * @date 2020/4/14
 */
public abstract class EngineJobHandler extends AbstractJobHandler<JobContext>{
    /**
     * 前置处理：写入日志
     * @methodName doBefore
     * @param jobContext 事件
     * @return
     * @author fenghuaigang
     * @date 2020/1/6
     */
    @Override
    protected void doBefore(JobContext jobContext) {
        logStart(getLogger(jobContext));
    }
    /**
     * 后置处理：输出日志
     * @methodName doLast
     * @param jobContext 事件
     * @return
     * @author fenghuaigang
     * @date 2020/1/6
     */
    @Override
    protected void doLast(JobContext jobContext) {
        logEnd(getLogger(jobContext));
    }

    /**
     *  获取当前配置日志写入工具
     * @methodName getLogger
     * @param jobContext 接收的计算结果数据
     * @return 日志写入工具
     * @author fenghuaigang
     * @date 2020/3/11
     */
    protected EngineLog getLogger(JobContext jobContext){
        if(jobContext.getEngineLog() == null){
            jobContext.setEngineLog(new FileLogger(jobContext.getJobId()));
        }
        return  jobContext.getEngineLog();
    }

    /**
     *  提供默认的处理类名
     * @methodName getHandlerName
     * @param
     * @return
     * @author fenghuaigang
     * @date 2020/4/20
     */
    @Override
    public String getHandlerName() {
        return this.getClass().getSimpleName();
    }
}
