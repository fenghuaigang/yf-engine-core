package com.yupont.engine.log;

import org.apache.commons.lang.time.DateFormatUtils;

import java.util.Date;

/**
 * 自定义规则日志接口
 * @author fenghuaigang
 * @date 2019/12/30 14:50
 */
public interface EngineLog {
    /**
     * 日志模式
     * @author fenghuaigang
     * @date 2020/03/23 14:50
     */
    enum LogType{
        /**实时写入*/
        STREAM,
        /**批量写入*/
        BATCH
    }

    /**
     *  设置日志模式
     * @methodName setLogType
     * @param logType 日志模式
     * @return
     * @author fenghuaigang
     * @date 2020/3/6
     */
    void setLogType(LogType logType);
    /**
     *  获取当前日志模式
     * @methodName getLogType
     * @param
     * @return 日志模式
     * @author fenghuaigang
     * @date 2020/3/6
     */
    LogType getLogType();

    /**
     * @description 设置日志名
     * @methodName setLogName
     * @param logName 日志文件名称
     * @return void
     * @author fenghuaigang
     * @date 2020/1/3
     */
    void setLogName(String logName);

    /**
     *  开始记录日志
     * @methodName startLog
     * @param
     * @return
     * @author fenghuaigang
     * @date 2020/3/6
     */
    void startLog();

    /**
     *  结束记录日志
     * @methodName endLog
     * @param
     * @return
     * @author fenghuaigang
     * @date 2020/3/6
     */
    void endLog();

    /**
     *  写入日志，携带Br换行符
     * @methodName log
     * @param appendLogPattern 日志内容 格式例如 "aaa {} bbb {} ccc"
     * @param appendLogArguments  日志占位符对应传递参数 格式例如 "111, true"
     * @return
     * @author fenghuaigang
     * @date 2020/3/6
     */
    void log(String appendLogPattern, Object... appendLogArguments);
    /**
     *  写入日志，不携带Br换行符
     * @methodName logNoBr
     * @param appendLogPattern
     * @param appendLogArguments
     * @return
     * @author fenghuaigang
     * @date 2020/5/18
     */
    void logNoBr(String appendLogPattern, Object... appendLogArguments);
    /**
     *  刷新缓存，写入目标地址
     * @methodName flush
     * @param
     * @return
     * @author fenghuaigang
     * @date 2020/3/6
     */
    void flush();

    /**
     *  获取日志内容
     * @methodName getLogContent
     * @param
     * @return 日志内容
     * @author fenghuaigang
     * @date 2020/1/7
     */
    Object getLogContent();

    /**
     *  获取当前时间 yyyyMMddHHmmss
     * @methodName getCurrentTimeStr
     * @param
     * @return  时间yyyyMMddHHmmss
     * @author fenghuaigang
     * @date 2020/4/14
     */
    default String getCurrentTimeStr() {
        return DateFormatUtils.format(new Date(), "yyyyMMddHHmmss");
    }

    /**
     *  获取标准化时间格式 yyyy-MM-dd HH:mm:ss
     * @methodName getCurrentFormatTime
     * @param
     * @return 时间yyyy-MM-dd HH:mm:ss
     * @author fenghuaigang
     * @date 2020/4/14
     */
    default String getCurrentFormatTime(){
        return DateFormatUtils.format(new Date(), "yyyy-MM-dd HH:mm:ss");
    }
}
