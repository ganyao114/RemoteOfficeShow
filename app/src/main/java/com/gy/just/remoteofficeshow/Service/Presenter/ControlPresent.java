package com.gy.just.remoteofficeshow.Service.Presenter;

import android.content.Context;
import android.os.Message;

import com.gy.just.remoteofficeshow.Service.Flag.ControlFlag;
import com.gy.myframework.IOC.Model.net.http.annotation.InjectHttp;
import com.gy.myframework.IOC.Model.net.http.annotation.InjectHttpCallBack;
import com.gy.myframework.IOC.Model.net.http.entity.HttpConnectMode;
import com.gy.myframework.IOC.Model.net.http.entity.HttpRunMode;
import com.gy.myframework.IOC.Model.net.http.impl.HttpInjectUtil;
import com.gy.myframework.IOC.Service.event.annotation.InjectEvent;
import com.gy.myframework.IOC.Service.event.entity.EventThreadType;
import com.gy.myframework.IOC.Service.event.impl.EventPoster;
import com.gy.myframework.MVP.Presenter.Presenter;
import com.gy.myframework.MVP.View.context.entity.ContextChangeEvent;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by gy on 2016/3/2.
 */
public class ControlPresent extends Presenter{


    @Override
    protected void onContextChanged(ContextChangeEvent event) {

    }

    @Override
    public void OnPresentInited(Context context) {
        EventPoster.getInstance().regist(this);
    }


    @InjectHttp(url = "http://www.jjust.top/ShowPPT/servlet/ChangePPTServlet",mode = HttpRunMode.Async,connectmode = HttpConnectMode.Post)
    public Serializable control(String string){
        MainPresenter.HTML = "http://www.jjust.top/ShowPPT/Rooms/" + MainPresenter.session.getId() +"/" + string + ".png";
        EventPoster.getInstance().post("ShowHtml",MainPresenter.HTML);
        return string;
    }

    @InjectHttpCallBack("control")
    public void onControl(Message message){

    }

    @InjectEvent(EventThreadType.MainThread)
    public void controlPPT(ControlFlag flag){
        Map<String,String> pars = new HashMap<String,String>();
        pars.put("mdo",flag.getAction());
        Map<String,String> headers = new HashMap<String,String>();
        headers.put("Referer","ShowPPT/room.jsp");
        headers.put("Cookie","roomID=" + MainPresenter.session.getId());
        HttpInjectUtil.getInstance().post(this,"control",pars,headers);
    }

    @Override
    public void DestoryPresent() {
        super.DestoryPresent();
        //Toast.makeText(getContext(),"PresentDestory",Toast.LENGTH_LONG).show();
        EventPoster.getInstance().unregist(this);
    }
}
