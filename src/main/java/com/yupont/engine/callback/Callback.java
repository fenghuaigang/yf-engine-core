package com.yupont.engine.callback;

import com.yupont.engine.handler.JobHandler;

/**
 * 回调方法
 *
 * @author fenghuaigang
 * @date 2020/4/14
 */
public interface Callback<T> extends JobHandler<T> {

    @Override
    default void onEvent(T event, long sequence, boolean endOfBatch) throws Exception {
        onEvent(event);
    }

    @Override
    default void onEvent(T event) throws Exception {
        try {
            call(event);
        } catch (Exception e) {
            throw e;
        }
    }

    /**
     * 回调
     * @param t
     * @return
     * @methodName call
     * @author fenghuaigang
     * @date 2020/4/14
     */
    void call(T t);
}
