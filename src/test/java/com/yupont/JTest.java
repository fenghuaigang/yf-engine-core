package com.yupont;

import com.yupont.engine.actuator.EngineActuator;
import com.yupont.engine.callback.Callback;
import com.yupont.engine.context.EngineJobConextFactory;
import com.yupont.engine.env.JobContext;
import com.yupont.engine.exception.EngineExceptionHandler;
import com.yupont.engine.handler.EngineJobHandler;
import org.junit.Test;

import java.util.Date;
import java.util.concurrent.TimeUnit;

/**
 * @author fenghuaigang
 * @date 2020/4/16
 */
public class JTest {

    @Test
    public void test1(){
        EngineActuator engineActuator = new EngineActuator(new EngineJobConextFactory());
        JobContext<User> userJobContext = new JobContext<>();
        User user = new User();
        user.setId("uuid");
        user.setName("测试名称");
        userJobContext.setT(user);
        userJobContext.setJobId("ID-1-1");


        engineActuator.addNextHandlers(new EngineJobHandler() {
            @Override
            protected void handle(JobContext jobContext) {
                jobContext.setStart(new Date());
                System.out.println(1/0);
                System.out.println(jobContext);
            }
            @Override
            public String getHandlerName() {
                return "JobHandler";
            }
        }).addCallBack(new Callback<JobContext<User>>() {
            @Override
            public void call(JobContext<User> userJobContext) {

                System.out.println(userJobContext);

            }
            @Override
            public String getHandlerName() {
                return "call";
            }
        }).addExceptionHandler(new EngineExceptionHandler());
        engineActuator.start();
        engineActuator.addContext(userJobContext);
        engineActuator.shutdown();
        try {
            TimeUnit.SECONDS.sleep(60);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }finally {

        }
    }

}
