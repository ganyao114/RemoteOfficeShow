package com.gy.myframework.IOC.Core.activity;

import android.app.Activity;
import android.os.Bundle;

import com.gy.myframework.IOC.UI.view.viewinject.impl.ViewInjectAll;
import com.gy.myframework.IOC.Model.net.http.impl.HttpInjectUtil;
import com.gy.myframework.IOC.Service.thread.impl.InjectAsycTask;

public abstract class BaseActivity extends Activity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
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
