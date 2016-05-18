package com.gy.just.remoteofficeshow.View.Fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.gy.just.remoteofficeshow.R;
import com.gy.just.remoteofficeshow.Service.Flag.ControlFlag;
import com.gy.just.remoteofficeshow.Service.Presenter.MainPresenter;
import com.gy.myframework.IOC.Core.fragment.BaseFragmentV4;
import com.gy.myframework.IOC.Service.event.impl.EventPoster;
import com.gy.myframework.IOC.UI.view.viewinject.annotation.ContentView;
import com.gy.myframework.IOC.UI.view.viewinject.annotation.OnClick;
import com.gy.myframework.IOC.UI.view.viewinject.annotation.ViewInject;

/**
 * Created by gy on 2016/3/21.
 */
@ContentView(R.layout.activity_control)
public class ControlFragment extends BaseFragmentV4{

    private ControlFlag flag;
    @ViewInject(R.id.sessionid1)
    private TextView sessionid;
    @ViewInject(R.id.sessionpass1)
    private TextView sessionpd;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        flag = new ControlFlag();
        CheckType();
        sessionid.setText("  房间ID:"+ MainPresenter.session.getId()+"  ");
        sessionpd.setText("  房间密码:" + MainPresenter.session.getPasswd()+"  ");
        return view;
    }


    private void CheckType() {
        if (MainPresenter.Type.equals("ppt")||MainPresenter.Type.equals("pptx"))
            return;
        view.findViewById(R.id.pre).setVisibility(View.GONE);
        view.findViewById(R.id.next).setVisibility(View.GONE);
    }

    @OnClick({R.id.pre,R.id.next})
    public void OnClick(View v){

        switch (v.getId()){
            case R.id.pre:
                flag.setAction("pre");
                break;
            case R.id.next:
                flag.setAction("next");
                break;
        }
        EventPoster.getInstance().broadcast(flag);
    }



    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }
}
