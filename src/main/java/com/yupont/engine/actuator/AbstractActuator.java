package com.yupont.engine.actuator;

import com.alibaba.fastjson.JSON;
import com.lmax.disruptor.BlockingWaitStrategy;
import com.lmax.disruptor.EventFactory;
import com.lmax.disruptor.EventTranslatorOneArg;
import com.lmax.disruptor.ExceptionHandler;
import com.lmax.disruptor.dsl.Disruptor;
import com.lmax.disruptor.dsl.EventHandlerGroup;
import com.lmax.disruptor.dsl.ProducerType;
import com.yupont.engine.callback.Callback;
import com.yupont.engine.env.ThreadContext;
import com.yupont.engine.exception.EngineExceptionHandler;
import com.yupont.engine.handler.JobHandler;
import com.yupont.engine.handler.ReleaseHandler;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.BeanUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * @author fenghuaigang
 * @date 2020/4/14
 */
@Slf4j
public abstract class AbstractActuator<T> implements Actuator<T> {

    protected List<List<JobHandler<T>>> jobList = new ArrayList<>(16);
    protected List<Callback<T>> callbackList = new ArrayList<>(16);
    protected ExecutorService executorService;

    protected Disruptor<T> disruptor;
    protected EventFactory<T> eventFactory;
    protected ExceptionHandler<T> exceptionHandler;

    public AbstractActuator(EventFactory<T> eventFactory) {
        this.eventFactory = eventFactory;
    }

    public AbstractActuator(EventFactory<T> eventFactory, ExecutorService executorService) {
        this.eventFactory = eventFactory;
        this.executorService = executorService;
    }

    public class Translator implements EventTranslatorOneArg<T, T> {
        @Override
        public void translateTo(T event, long sequence, T arg0) {
            BeanUtils.copyProperties(arg0, event);
        }
    }

    protected Translator translator = new Translator();

    @Override
    public void start() {
        if (executorService == null) {
            log.error("未自定义线程池，使用常规线程");
            executorService = ThreadContext.getExecutorService();
        }
        if (CollectionUtils.isEmpty(jobList)) {
            log.error("未设置任务处理实现Handler类");
            return;
        }
        int ringBufferSize = 1024 * 1024;
        disruptor = new Disruptor<>(eventFactory, ringBufferSize, executorService, ProducerType.SINGLE, new BlockingWaitStrategy());
        EventHandlerGroup<T> tEventHandlerGroup = disruptor.handleEventsWith((event, sequence, endOfBatch) -> {
            log.info("Actuator任务参数：{}", JSON.toJSONString(event));
        });
        //处理任务逻辑
        for (List<JobHandler<T>> jobHandlers : jobList) {
            if (jobHandlers != null) {
                tEventHandlerGroup = tEventHandlerGroup.then((JobHandler<T>[]) jobHandlers.toArray());
            }
        }
        //任务执行回调
        if (CollectionUtils.isNotEmpty(callbackList)) {
            for (Callback<T> tCallback : callbackList) {
                tEventHandlerGroup = tEventHandlerGroup.then(tCallback);
            }
        }
        if (exceptionHandler != null) {
            disruptor.setDefaultExceptionHandler(exceptionHandler);
        }else{
            disruptor.setDefaultExceptionHandler(new EngineExceptionHandler<>());
        }
        //释放资源
        tEventHandlerGroup.then(new ReleaseHandler<>());
        disruptor.start();
    }

    @Override
    public Actuator addContext(T t) {
        disruptor.publishEvent(translator,t);
        return this;
    }

    @Override
    public void shutdown() {
        try {
            log.info("Actuator启动器停止运行...");
            disruptor.shutdown();
        } catch (Exception e) {
            log.info("Actuator启动器停止异常：{}", e.getMessage());
        }
    }

    @Override
    public void shutdown(long timeout, TimeUnit timeUnit) {
        try {
            log.info("Actuator启动器停止运行...");
            disruptor.shutdown(timeout, timeUnit);
        } catch (Exception e) {
            log.info("Actuator启动器停止异常：{}", e.getMessage());
        }
    }

    @Override
    public Actuator addCallBack(Callback<T>... callbacks) {
        callbackList.addAll(Arrays.asList(callbacks));
        return this;
    }

    @Override
    public Actuator addNextHandlers(JobHandler<T>... jobHandlers) {
        jobList.add(Arrays.asList(jobHandlers));
        return this;
    }

    @Override
    public Actuator addExceptionHandler(ExceptionHandler<T> exceptionHandler) {
        this.exceptionHandler = exceptionHandler;
        return this;
    }
}
