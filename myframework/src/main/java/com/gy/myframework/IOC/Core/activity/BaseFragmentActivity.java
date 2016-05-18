package com.gy.myframework.IOC.Core.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentActivity;

import com.gy.myframework.IOC.UI.view.viewinject.impl.ViewInjectAll;
import com.gy.myframework.IOC.Model.net.http.impl.HttpInjectUtil;
import com.gy.myframework.IOC.Service.thread.impl.InjectAsycTask;

/**
 * Created by gy on 2015/11/30.
 */
public abstract class BaseFragmentActivity extends FragmentActivity{

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ViewInjectAll.getInstance().inject(this);
        InjectAsycTask.getInstance().inject(this);
        HttpInjectUtil.getInstance().inject(this);
    }

    @Override
    protected void onDestroy() {
        InjectAsycTask.getInstance().remove(this);
        HttpInjectUtil.getInstance().remove(this);
        ViewInjectAll.getInstance().remove(this);
        super.onDestroy();
    }

}
