package com.yupont.engine.log;

import lombok.Getter;
import lombok.Setter;
import org.apache.commons.lang.StringUtils;
import org.redisson.api.RBucket;
import org.redisson.api.RMap;
import org.redisson.api.RedissonClient;

import java.util.List;

/**
 * redis实现日志存储解析功能
 * @author fenghuaigang
 * @date 2019/12/30
 */
public class RedisLogger extends AbstractTranLog{

    private Object lock = new Object();
    /**
     * redis键位空间,存在则用HASH类型存放日志
     */
    @Getter
    @Setter
    protected String regixSpace;

    private RedissonClient redissonClient;

    public RedisLogger(RedissonClient redissonClient) {
        super();
        this.redissonClient = redissonClient;
    }

    public RedisLogger(RedissonClient redissonClient,String logName) {
        super(logName);
        this.redissonClient = redissonClient;
    }

    public RedisLogger(RedissonClient redissonClient,String logName, String regixSpace) {
        super(logName);
        this.redissonClient = redissonClient;
        this.regixSpace = regixSpace;
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
        StringBuilder stringBuilder = new StringBuilder(4096);
        logDetail.stream().forEach(s->stringBuilder.append(s).append("\r\n"));
        if(StringUtils.isBlank(regixSpace)){
            RBucket<String> bucket = redissonClient.getBucket(logName);
            bucket.set(stringBuilder.toString());
        }else {
            RMap<String, String> rMap = redissonClient.getMap(regixSpace);
            synchronized (lock){
                String s = rMap.get(logName);
                StringBuilder log = new StringBuilder(StringUtils.isNotBlank(s)?s:"").append(stringBuilder.toString());
                rMap.put(logName,log.toString());
            }
        }
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
        if(StringUtils.isBlank(regixSpace)){
            appendString(formatAppendLog);
        }else {
            appendMapVal(formatAppendLog);
        }
    }

    /** 
     *  拼接日志
     * @methodName appendMapVal
     * @param formatAppendLog 日志内容
     * @return
     * @author fenghuaigang
     * @date 2020/3/4
     */
    private void appendMapVal(String formatAppendLog) {
        RMap<String, String> rMap = redissonClient.getMap(regixSpace);
        synchronized (logName){
            String s = rMap.get(logName);
            StringBuilder log = new StringBuilder(StringUtils.isNotBlank(s)?s:"").append(formatAppendLog);
            rMap.put(logName,log.toString());
        }
    }
    /**
     *  拼接日志
     * @methodName appendMapVal
     * @param formatAppendLog 日志内容
     * @return
     * @author fenghuaigang
     * @date 2020/3/4
     */
    private void appendString(String formatAppendLog) {
        RBucket<String> bucket = redissonClient.getBucket(logName);
        synchronized (lock){
            String s = bucket.get();
            StringBuilder log = new StringBuilder(StringUtils.isNotBlank(s)?s:"").append(formatAppendLog).append("\r\n");
            bucket.set(log.toString());
        }
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
        Object retVal = null;
        if(StringUtils.isNotBlank(regixSpace)){
            RMap<String, String> rMap = redissonClient.getMap(regixSpace);
            if(StringUtils.isNotBlank(logName)){
                retVal = rMap.get(logName);
            }else{
                retVal = rMap;
            }
        }else{
            if(StringUtils.isNotBlank(logName)){
                retVal = redissonClient.getBucket(logName).get();
            }
        }
        return retVal;
    }
}
