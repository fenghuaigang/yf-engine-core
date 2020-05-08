package com.yupont.engine.env;

import com.alibaba.fastjson.JSONObject;
import com.yupont.engine.log.EngineLog;
import lombok.Data;

import java.util.Date;


/**
 * 封装任务参数
 * @author fenghuaigang
 * @date 2020/4/14
 */
@Data
public class JobContext<T> {
    /**
     * 提供日志写入实现
     */
    private EngineLog engineLog;

    /**
     * 任务ID
     */
    private String jobId;

    /**
     * 上下文传递参数
     */
    private JSONObject context = new JSONObject();

    /**
     * 业务参数
     */
    private T t;

    /**
     * 启动时间
     */
    private Date start;

    /**
     * 结束时间
     */
    private Date end;
}
