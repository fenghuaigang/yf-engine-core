package com.yupont.engine.actuator;

import com.lmax.disruptor.EventFactory;
import com.yupont.engine.env.JobContext;

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

}
