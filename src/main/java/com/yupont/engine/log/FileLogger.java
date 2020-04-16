package com.yupont.engine.log;

import java.util.List;

/**
 * 文件流方式实现规则日志打印
 * @author fenghuaigang
 * @date 2019/12/31 17:43
 */
public class FileLogger extends AbstractTranLog{

    public FileLogger(String logName) {
        super(logName);
    }

    /**
     *  批量写入
     * @methodName writeFlush
     * @param logDetail 日志详情
     * @return
     * @author fenghuaigang
     * @date 2020/3/4
     */
    @Override
    protected void writeFlush(List<String> logDetail) {
        FileAppenderUtil.appendLog(logName,logDetail.toArray(new String[]{}));
    }

    /**
     *  写入日志
     * @methodName write
     * @param formatAppendLog 日志
     * @return
     * @author fenghuaigang
     * @date 2020/3/4
     */
    @Override
    protected void write(String formatAppendLog) {
        FileAppenderUtil.appendLog(logName,formatAppendLog);
    }
    
    /** 
     *  读取日志内容
     * @methodName getLogContent
     * @param 
     * @return 日志内容
     * @author fenghuaigang
     * @date 2020/3/4
     */
    @Override
    public Object getLogContent() {
        return FileAppenderUtil.readLogMapByLogFileName(logName);
    }
}
