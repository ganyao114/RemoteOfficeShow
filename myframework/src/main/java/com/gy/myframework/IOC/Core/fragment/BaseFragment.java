package com.gy.myframework.IOC.Core.fragment;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.gy.myframework.IOC.UI.view.viewinject.impl.ViewInjectAll;

/**
 * Created by gy on 2015/11/30.
 */
public abstract class BaseFragment extends Fragment{
    private View view;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if (view == null)
            ViewInjectAll.getInstance().inject(this,inflater,null);
        return view;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        ViewInjectAll.getInstance().remove(this);
    }
}
