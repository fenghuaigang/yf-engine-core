package com.yupont.engine.log;

import lombok.Getter;
import org.apache.commons.collections.CollectionUtils;
import org.slf4j.helpers.FormattingTuple;
import org.slf4j.helpers.MessageFormatter;

import java.util.ArrayList;
import java.util.List;

/**
 * 抽象日志实现
 * @author fenghuaigang
 * @date 2019/12/30 14:53
 */
public abstract class AbstractTranLog implements EngineLog {


    /**
     * 日志名：可以是文件路径，可以是其他标识空间地址的名称
     */
    @Getter
    protected String logName;
    /**
     * 日志写入模式
     */
    protected LogType logType;
    /**
     * 批量写入的缓存
     */
    protected List<String> logContainer;
    /**
     *  构造函数、开启日志
     * @methodName AbstractTranLog
     * @param
     * @return
     * @author fenghuaigang
     * @date 2020/2/25
     */
    public AbstractTranLog() {
        startLog();
    }
    /**
     *  构造函数、指定文件名
     * @methodName AbstractTranLog
     * @param
     * @return
     * @author fenghuaigang
     * @date 2020/2/25
     */
    public AbstractTranLog(String logName) {
        this.logName = logName;
        startLog();
    }

    /**
     *  写入日志
     * @methodName log
     * @param appendLogPattern 格式化日志
     * @param appendLogArguments 通配符匹配参数
     * @return
     * @author fenghuaigang
     * @date 2020/3/4
     */
    @Override
    public void log(String appendLogPattern, Object... appendLogArguments) {
        FormattingTuple ft = MessageFormatter.arrayFormat(appendLogPattern, appendLogArguments);
        String appendLog = ft.getMessage();
        StackTraceElement callInfo = new Throwable().getStackTrace()[2];
        logDetail(callInfo, appendLog,true);
    }

    @Override
    public void logNoBr(String appendLogPattern, Object... appendLogArguments) {
        FormattingTuple ft = MessageFormatter.arrayFormat(appendLogPattern, appendLogArguments);
        String appendLog = ft.getMessage();
        StackTraceElement callInfo = new Throwable().getStackTrace()[2];
        logDetail(callInfo, appendLog,false);
    }


    @Override
    public void setLogName(String logName) {
        this.logName = logName;
    }

    /**
     *  启动日志
     *  初始化容器
     * @methodName startLog
     * @param
     * @return
     * @author fenghuaigang
     * @date 2020/3/4
     */
    @Override
    public void startLog() {
        if(logType == null){
            logType = LogType.BATCH;
        }
        if(LogType.BATCH.equals(logType)){
            //初始化容器
            if(CollectionUtils.isEmpty(logContainer)){
                logContainer = new ArrayList<>(1024);
            }
        }
    }

    /**
     *  结束日志、刷新缓存
     * @methodName endLog
     * @param
     * @return
     * @author fenghuaigang
     * @date 2020/3/4
     */
    @Override
    public void endLog() {
        flush();
    }

    /**
     *  刷新缓存
     * @methodName flush
     * @param
     * @return
     * @author fenghuaigang
     * @date 2020/3/4
     */
    @Override
    public void flush() {
        if(LogType.BATCH.equals(logType)){
            writeFlush(logContainer);
            logContainer.clear();
        }
    }

    /**
     *  一次性写入日志
     * @methodName writeFlush
     * @param logDetail 日志记录集合
     * @return
     * @author fenghuaigang
     * @date 2020/3/10
     */
    protected abstract void writeFlush(List<String> logDetail);

    /**
     *   实时写入日志
     * @methodName write
     * @param formatAppendLog 日志记录
     * @return
     * @author fenghuaigang
     * @date 2020/3/10
     */
    protected abstract void write(String formatAppendLog);

    /**
     *  打印日志详情，不同logType（初始化指定）模式采用批量写入或单次写入方式
     * @methodName logDetail
     * @param callInfo  StackTraceElement对象，用于创建格式化日志，例如：xxx:{} , 其中占位符{} 接收
     * @param appendLog 日志
     * @param needBr 是否需要换行
     * @return
     * @author fenghuaigang
     * @date 2020/2/25
     */
    private void logDetail(StackTraceElement callInfo, String appendLog,boolean needBr) {
        String formatAppendLog = createLogString(callInfo, appendLog, needBr);
        if(LogType.BATCH.equals(logType)){
            logContainer.add(formatAppendLog);
        }else{
            write(formatAppendLog);
        }
    }

    /**
     *  创建符合UI展示的格式化日志
     * @methodName createLogString
     * @param callInfo  StackTraceElement对象，用于创建格式化日志，例如：xxx:{} , 其中占位符{} 接收
     * @param appendLog 日志
     * @param needBr 是否需要换行
     * @return
     * @author fenghuaigang
     * @date 2020/2/25
     */
    private String createLogString(StackTraceElement callInfo, String appendLog, boolean needBr) {
        StringBuilder stringBuilder = new StringBuilder(512);
        if(needBr){
            stringBuilder.append("</br>")
                    .append(getCurrentFormatTime())
                    .append( " [")
                    .append(callInfo.getFileName())
                    .append("#")
                    .append(callInfo.getMethodName())
                    .append("]-[")
                    .append(callInfo.getLineNumber())
                    .append("] ");
        }
        stringBuilder.append(appendLog != null ? appendLog : "");
        return stringBuilder.toString();
    }

    /**
     *  设置日志写入模式
     * @methodName setLogType
     * @param logType
     * @return
     * @author fenghuaigang
     * @date 2020/2/25
     */
    @Override
    public void setLogType(LogType logType) {
        this.logType = logType;
    }

    @Override
    public LogType getLogType() {
        return logType;
    }

}
