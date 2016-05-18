package com.gy.myframework.IOC.Service.event.proxy;

/**
 * Created by gy on 2015/12/21.
 */
public class EventRunnable implements Runnable{

    private IEventProxy proxy;
    private Object arg;

    public EventRunnable(IEventProxy proxy,Object object) {
        this.proxy = proxy;
        arg = object;
    }

    @Override
    public void run() {
        proxy.onEvent(arg);
    }
}
