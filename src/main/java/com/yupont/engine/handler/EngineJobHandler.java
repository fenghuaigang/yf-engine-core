package com.yupont.engine.handler;

import com.yupont.engine.log.EngineLog;
import com.yupont.engine.env.JobContext;
import com.yupont.engine.log.FileLogger;

/**
 * @author fenghuaigang
 * @date 2020/4/14
 */
public abstract class EngineJobHandler extends AbstractJobHandler<JobContext>{

    @Override
    protected void doBefore(JobContext jobContext) {
        logStart(getLogger(jobContext));
    }

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
}
