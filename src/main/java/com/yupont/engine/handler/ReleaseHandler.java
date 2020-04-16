package com.yupont.engine.handler;

import lombok.extern.slf4j.Slf4j;

/**
 * 资源释放处理类
 * @author fenghuaigang
 * @date 2019/12/5 12:07
 */
@Slf4j
public class ReleaseHandler<T> extends AbstractJobHandler<T> {

    /**
     *  前置方法
     * @methodName doBefore       
     * @param t 泛型类
     * @return  
     * @author fenghuaigang 
     * @date 2020/3/11 
     */
    @Override
    protected void doBefore(T t) {
    }

    /** 
     *  后置方法
     * @methodName doLast
     * @param t 泛型类
     * @return  
     * @author fenghuaigang 
     * @date 2020/3/11 
     */
    @Override
    protected void doLast(T t) {
    }
    
    /** 
     *  释放资源
     * @methodName handle
     * @param t
     * @return
     * @author fenghuaigang
     * @date 2020/2/18
     */
    @Override
    protected void handle(T t) {
        log.info("release resource...");
        t = null;
    }

    @Override
    public String getHandlerName() {
        return this.getClass().getSimpleName();
    }
}
