package com.yupont.engine.context;

import com.lmax.disruptor.EventFactory;
import lombok.extern.slf4j.Slf4j;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

/**
 * 适用创建任务事件的工厂,支持泛型，业务类仅需继承该抽象类，添加对应的任务实体即可
 * @author fenghuaigang
 * @date 2020/4/13
 */
@Slf4j
public abstract class JobEventFactory<T> implements EventFactory {

    private Class<T> entityClass;

    public JobEventFactory() {
        Type genType = getClass().getGenericSuperclass();
        Type[] params = ((ParameterizedType) genType).getActualTypeArguments();
        entityClass = (Class) params[0];
    }

    @Override
    public T newInstance() {
        try {
            return entityClass.newInstance();
        } catch (InstantiationException e) {
            log.error("工厂-{} 提供类实例化异常：{}",getClass(),e.getMessage());
        } catch (IllegalAccessException e) {
            log.error("工厂-{} 提供类实例化异常：{}",getClass(),e.getMessage());
        }
        return null;
    }
}
