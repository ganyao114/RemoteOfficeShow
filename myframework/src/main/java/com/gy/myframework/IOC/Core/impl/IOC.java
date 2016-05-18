package com.gy.myframework.IOC.Core.impl;

import android.app.Application;

import com.gy.myframework.IOC.Core.entity.InstrumentationBean;

/**
 * Created by gy on 2015/11/18.
 */
public class IOC {

    private Application application;
    private static IOC ioc;
    private InstrumentationBean instrumentation;
    private Thread intThread;

    public static IOC getInstance() {
        synchronized (IOC.class) {
            if (ioc == null) {
                ioc = new IOC();
            }
        }
        return ioc;
    }

    public Application getApplication() {
        return application;
    }

    public void init(Application application) {
        this.application = application;
        /*
        //读取配置
        intThread = new Thread(new IntThread());
        intThread.start();
        ResourceFactory.setRclass(application);
        Object mainThread = KernelObject.declaredGet(application.getBaseContext(), "mMainThread");
        Field instrumentationField = KernelReflect.declaredField(mainThread.getClass(), "mInstrumentation");
        instrumentation = new InstrumentationBean();
        KernelObject.copy(KernelReflect.get(mainThread, instrumentationField), instrumentation);
        KernelReflect.set(mainThread, instrumentationField, instrumentation);

        */
    }
}
