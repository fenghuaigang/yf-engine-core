package com.yupont.engine.actuator;

import com.lmax.disruptor.EventFactory;
import com.lmax.disruptor.RingBuffer;
import com.yupont.engine.env.JobContext;
import org.springframework.beans.BeanUtils;

import java.util.concurrent.ExecutorService;


/**
 * @author fenghuaigang
 * @date 2020/4/14
 */
public class EngineActuator extends AbstractActuator<JobContext>{

    public EngineActuator(EventFactory<JobContext> eventFactory) {
        super(eventFactory);
    }

    public EngineActuator(EventFactory<JobContext> eventFactory, ExecutorService executorService) {
        super(eventFactory, executorService);
    }

    @Override
    public Actuator addContext(JobContext jobContext) {
        RingBuffer<JobContext> ringBuffer = disruptor.getRingBuffer();
        long next = ringBuffer.next();
        JobContext context = ringBuffer.get(next);
        BeanUtils.copyProperties(jobContext,context);
        ringBuffer.publish(next);
        return this;
    }


}
