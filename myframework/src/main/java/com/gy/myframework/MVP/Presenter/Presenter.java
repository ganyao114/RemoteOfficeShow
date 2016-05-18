package com.gy.myframework.MVP.Presenter;

import android.app.Activity;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.view.View;

import com.gy.myframework.IOC.Model.net.http.impl.HttpInjectUtil;
import com.gy.myframework.IOC.Service.thread.impl.InjectAsycTask;
import com.gy.myframework.MVP.View.context.activity.IActivity;
import com.gy.myframework.MVP.View.context.entity.ContextChangeEvent;

import java.lang.ref.WeakReference;
import java.util.HashMap;

/**
 * Created by gy on 2015/12/28.
 */
public abstract class Presenter implements IPresenterCallBack{

    private static Presenter curPresenter;
    private WeakReference<Context> curContextRef;
    private static HashMap<Class,Presenter> prensentStack;

    public Presenter() {
        curPresenter = this;
        InjectAsycTask.getInstance().inject(this);
        HttpInjectUtil.getInstance().inject(this);
    }

    @Override
    public void OnPresentSeted(Context context) {

        Context preContext = null;

        if (curContextRef !=null)
            preContext = curContextRef.get();

        this.curContextRef = new WeakReference<Context>(context);

        if (preContext == null){
            OnPresentInited(context);
            return;
        }

        ContextChangeEvent event = new ContextChangeEvent();
        if (preContext instanceof Activity){
            if (context instanceof Activity)
                event.setAction(ContextChangeEvent.ACTIVITY_ACTIVITY);
            else if (preContext instanceof Service)
                event.setAction(ContextChangeEvent.ACTIVITY_SERVICE);
        }else if (preContext instanceof Service){
            if (context instanceof Activity)
                event.setAction(ContextChangeEvent.SERVICE_ACTIVITY);
            else if (preContext instanceof Service)
                event.setAction(ContextChangeEvent.SERVICE_SERVICE);
        }
        event.setContext(context);
        onContextChanged(event);
    }

    @Override
    public void DestoryPresent() {
        HttpInjectUtil.getInstance().remove(this);
        InjectAsycTask.getInstance().remove(this);
    }

    protected abstract void onContextChanged(ContextChangeEvent event);

    @Override
    public Context getContext() {
        return curContextRef.get();
    }

    public Activity getActivityRaw(){
        return (Activity) getContext();
    }

    public IActivity getActivityInter(){
        return (IActivity) getActivityRaw();
    }

    public Service getServiceRaw(){
        return (Service) getContext();
    }

    protected  <T extends View> T getView(int ViewId){
        return getActivityInter().getView(ViewId);
    }

    protected void startActivity(Intent intent){
        //OnActivityChangeBefore();
        getActivityRaw().startActivity(intent);
    }

    public static Presenter getCurPresenter() {
        return curPresenter;
    }

    public static Presenter getPresent(Class clazz){
        return prensentStack.get(clazz);
    }

    public void putPresent(Presenter presenter){
        prensentStack.put(presenter.getClass(),presenter);
    }

}
