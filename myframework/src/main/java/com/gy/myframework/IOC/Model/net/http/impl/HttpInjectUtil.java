package com.gy.myframework.IOC.Model.net.http.impl;

import android.os.Bundle;
import android.os.Message;

import com.gy.myframework.IOC.Core.invoke.DynamicHandler;
import com.gy.myframework.IOC.Model.net.http.annotation.InjectHttp;
import com.gy.myframework.IOC.Model.net.http.annotation.InjectHttpCallBack;
import com.gy.myframework.IOC.Model.net.http.entity.HttpInjectBean;
import com.gy.myframework.IOC.Service.thread.handler.BaseHandler;
import com.gy.myframework.IOC.Service.thread.handler.IHandler;
import com.gy.myframework.Model.net.http.IHttpDealCallBack;
import com.gy.myframework.Model.net.http.impl.MyHttpService;
import com.gy.myframework.Service.thread.pool.impl.MyWorkThreadQueue;
import com.gy.myframework.Service.thread.templet.HttpThreadTemplet;
import com.gy.myframework.Service.thread.templet.configs.HttpTheadConfigBean;

import java.io.Serializable;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.HashMap;
import java.util.Map;
import java.util.WeakHashMap;

/**
 * Created by gy on 2015/11/25.
 */
public class HttpInjectUtil {

    private static HttpInjectUtil injectUtil;

    private Map<Class<?>,Map<String, HttpInjectBean>> injectBeanMap;

    private Class<?> Dailinter = IHttpDealCallBack.class;

    private String Proxymethod = "dealReturn";

    private String handlerProxyMethod = "handlePost";

    private Class<?> handlerinter = IHandler.class;

    public static HttpInjectUtil getInstance() {
        synchronized (HttpInjectUtil.class) {
            if (injectUtil == null)
                injectUtil = new HttpInjectUtil();
        }
        return injectUtil;
    }

    private HttpInjectUtil() {
        injectBeanMap = new WeakHashMap<Class<?>,Map<String, HttpInjectBean>>();
    }

    public void inject(Object object) {
        if (injectBeanMap.containsKey(object.getClass()))
            return;
        injectBeanMap.put(object.getClass(),new HashMap<String, HttpInjectBean>());
        Class<?> template = object.getClass();
        while (template != null && template != Object.class) {
            // 过滤掉基类 因为基类是不包含注解的
            if (template.getName().equals("android.app.Activity") || template.getName().equals("android.support.v4.app.FragmentActivity") || template.getName().equals("android.support.v4.app.Fragment") || template.getName().equals("android.app.Fragment")) {
                break;
            }
            injectDailMethod(template,object);
            injectCallBack(template,object);
            template = template.getSuperclass();
        }

    }


    private void injectDailMethod(Class<?> clazz, Object object){
        Method[] methods = clazz.getDeclaredMethods();
        for (Method method : methods) {
            InjectHttp methodinject = method.getAnnotation(InjectHttp.class);
            if (methodinject != null)
                doInjectDailMethod(method, object,methodinject);
        }
    }

    private void doInjectDailMethod(Method method, Object object, InjectHttp methodinject) {
        String key = method.getName();
        if (injectBeanMap.get(object.getClass()).containsKey(key))
            return;
        DynamicHandler handler = new DynamicHandler(object);
        handler.addMethod(Proxymethod, method);
        Object bussnessproxy = Proxy.newProxyInstance(Dailinter.getClassLoader(), new Class<?>[]{Dailinter}, handler);
        MyHttpService service = new MyHttpService((IHttpDealCallBack)bussnessproxy);
        service.setUrl(methodinject.url());
        HttpInjectBean injectBean = new HttpInjectBean();
        injectBean.setService(service);
        injectBean.setUrl(methodinject.url());
        injectBean.setConnectMode(methodinject.connectmode());
        injectBean.setRunMode(methodinject.mode());
        injectBeanMap.get(object.getClass()).put(key,injectBean);
    }

    private void injectCallBack(Class<?> clazz, Object object) {
        Method[] methods = clazz.getDeclaredMethods();
        for (Method method : methods) {
            InjectHttpCallBack methodinject = method.getAnnotation(InjectHttpCallBack.class);
            if (methodinject != null)
                doInjectCallBackMethod(method, object,methodinject);
        }
    }

    private void doInjectCallBackMethod(Method method, Object object, InjectHttpCallBack methodinject) {
        String key = methodinject.value();
        if (!injectBeanMap.get(object.getClass()).containsKey(key))
            return;
        final HttpInjectBean injectBean = injectBeanMap.get(object.getClass()).get(key);
        DynamicHandler handler = new DynamicHandler(object);
        handler.addMethod(handlerProxyMethod, method);
        Object bussnessproxy = Proxy.newProxyInstance(handlerinter.getClassLoader(), new Class<?>[]{handlerinter}, handler);
        BaseHandler baseHandler = new BaseHandler((IHandler)bussnessproxy);
        HttpThreadTemplet httpThreadTemplet = new HttpThreadTemplet(baseHandler,injectBean.getService()) {
            @Override
            protected void OnRun() throws Exception {
                Serializable result = httpService.getDataPost();
                injectBean.setResult(result);
                Message msg = new Message();
                Bundle data = new Bundle();
                data.putSerializable("result",result);
                msg.what = 1;
                msg.setData(data);
                handler.sendMessage(msg);
            }

            @Override
            protected HttpTheadConfigBean SetConfig() {
                HttpTheadConfigBean configBean = new HttpTheadConfigBean
                        (false, 0, "连接超时", "加载超时","加载错误");
                return configBean;
            }

            @Override
            public Serializable dealReturn(String result) {
                return null;
            }
        };
        injectBean.setHttpThreadTemplet(httpThreadTemplet);
    }

    public void post(Object object,String key,Map<String,String> params,Map<String,String> headers) {
        if (injectBeanMap.get(object.getClass()) == null)
            return;
        HttpInjectBean injectBean = injectBeanMap.get(object.getClass()).get(key);
        injectBean.getService().setParams(params);
        injectBean.getService().setHeaders(headers);
        MyWorkThreadQueue.AddTask(injectBean.getHttpThreadTemplet());
    }

    public void remove(Object object) {
        injectBeanMap.remove(object.getClass());
        System.gc();
    }

    public Serializable getResult(Object object,String key){
        return injectBeanMap.get(object.getClass()).get(key).getResult();
    }
}
