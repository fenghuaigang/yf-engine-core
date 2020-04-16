package com.yupont.engine.exception;

import com.lmax.disruptor.ExceptionHandler;
import lombok.extern.slf4j.Slf4j;

/**
 * @author fenghuaigang
 * @date 2020/4/16
 */
@Slf4j
public class EngineExceptionHandler<T> implements ExceptionHandler<T> {

    @Override
    public void handleEventException(Throwable ex, long sequence, T event) {
        log.error("handler error[seq-{}]:{}",sequence,ex);
        log.error("current params:{}",event);
    }

    @Override
    public void handleOnStartException(Throwable ex) {
        log.error("handler start error:{}",ex);
    }

    @Override
    public void handleOnShutdownException(Throwable ex) {
        log.error("handler shutdown error:{}",ex);
    }
}
