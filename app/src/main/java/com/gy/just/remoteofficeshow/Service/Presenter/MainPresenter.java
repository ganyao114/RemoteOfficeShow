package com.gy.just.remoteofficeshow.Service.Presenter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Message;
import android.widget.Toast;

import com.gy.just.remoteofficeshow.Model.entity.SessionBean;
import com.gy.just.remoteofficeshow.Service.ActionMonitor.ISenserActionCallBack;
import com.gy.just.remoteofficeshow.Service.ActionMonitor.SenserActionProcessor;
import com.gy.just.remoteofficeshow.Service.Flag.ControlFlag;
import com.gy.just.remoteofficeshow.Service.Flag.DissmissProgress;
import com.gy.just.remoteofficeshow.Service.Flag.OnBtClick;
import com.gy.just.remoteofficeshow.Service.Flag.ShowProgressView;
import com.gy.just.remoteofficeshow.Service.Flag.UploadFlag;
import com.gy.just.remoteofficeshow.Service.Flag.Uploaded;
import com.gy.just.remoteofficeshow.View.Activity.ControlActivity;
import com.gy.just.remoteofficeshow.View.Activity.MainActivity;
import com.gy.just.remoteofficeshow.View.Activity.SelectFileActivity;
import com.gy.myframework.IOC.Model.net.http.annotation.InjectHttp;
import com.gy.myframework.IOC.Model.net.http.annotation.InjectHttpCallBack;
import com.gy.myframework.IOC.Model.net.http.entity.HttpConnectMode;
import com.gy.myframework.IOC.Model.net.http.entity.HttpRunMode;
import com.gy.myframework.IOC.Model.net.http.impl.HttpInjectUtil;
import com.gy.myframework.IOC.Service.event.annotation.InjectEvent;
import com.gy.myframework.IOC.Service.event.entity.EventThreadType;
import com.gy.myframework.IOC.Service.event.impl.EventPoster;
import com.gy.myframework.IOC.Service.thread.annotation.AsycTask;
import com.gy.myframework.IOC.Service.thread.impl.InjectAsycTask;
import com.gy.myframework.MVP.Presenter.Presenter;
import com.gy.myframework.MVP.View.context.entity.ActivityOnCreatedListener;
import com.gy.myframework.MVP.View.context.entity.ContextChangeEvent;
import com.gy.myframework.Model.net.http.IUploadCallBack;
import com.gy.myframework.Model.net.http.entity.UploadProgress;
import com.gy.myframework.Model.net.http.impl.MyHttpService;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by gy on 2016/2/24.
 */
public class MainPresenter extends Presenter implements ActivityOnCreatedListener,ISenserActionCallBack,IUploadCallBack{

    private SenserActionProcessor senserActionProcessor;
    private MyHttpService httpService;
    private File file;
    public static SessionBean session;
    public static String HTML;
    public static String Type;

    @Override
    public void OnPresentInited(Context context) {
        EventPoster.getInstance().regist(this);
        getActivityInter().setOnCreateListener(this);
        senserActionProcessor = new SenserActionProcessor(this,context);
        getSessionId();
    }

    @Override
    protected void onContextChanged(ContextChangeEvent contextChangeEvent) {
        Context context = contextChangeEvent.getContext();
        if (context instanceof SelectFileActivity){

        }else if (context instanceof MainActivity){

        }
    }

    @Override
    public void ActivityOnCreated(Bundle bundle, Activity activity) {
    }

    //请求会话ID
    @InjectHttp(url = "http://www.jjust.top/ShowPPT/servlet/GetRoomIDServlet",mode = HttpRunMode.Async,connectmode = HttpConnectMode.Post)
    public Serializable getSessionId(String result){
        SessionBean resultbean = new SessionBean();
        JSONObject object = null;
        try {
            object = new JSONObject(result);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        String roomid = null;
        String pd = null;
        if (object!=null){
            try {
                roomid = object.getString("roomID");
                pd = object.getString("pd");
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        resultbean.setId(roomid);
        resultbean.setPasswd(pd);
        return resultbean;
    }

    @InjectHttpCallBack("getSessionId")
    public void onReceiveID(Message message){
        switch (message.what){
            case 0:

                break;
            case 1:
                session = (SessionBean) message.getData().getSerializable("result");
                showSession();
                break;
        }
    }

    @AsycTask
    public void uploadThread(){
        Type = getEnd(file);
        try {
            if (httpService == null) {
                httpService = new MyHttpService();
                httpService.setUrl("http://www.jjust.top/ShowPPT/servlet/GetRecServlet");
                setCallback();
            }
            Map<String,String> headers = new HashMap<String,String>();
            Map<String,String> par = new HashMap<String,String>();
            headers.put("Cookie", "roomID=" + session.getId());
            headers.put("Referer", "ShowPPT/room.jsp");
            par.put("room_id",session.getId());
            par.put("room_pd", session.getPasswd());
            httpService.setHeaders(headers);
            httpService.setParams(par);
            EventPoster.getInstance().broadcast(new ShowProgressView());
            Thread.currentThread().sleep(500);
            String str = httpService.upload(file);
            JSONObject object = new JSONObject(str);
            HTML = "http://www.jjust.top/ShowPPT/"+object.getString("url");
            Type = object.getString("type");
            // Log.e("gy",Type);
        } catch (Exception e) {
            e.printStackTrace();
            EventPoster.getInstance().broadcast(new DissmissProgress());
        }
        EventPoster.getInstance().broadcast(new DissmissProgress());
        if (Type == null)
            Type = getEnd(file);
        EventPoster.getInstance().broadcast(new Uploaded());
    }

    private String getEnd(File file){
        String fName = file.getName();
        String end = fName
                .substring(fName.lastIndexOf(".") + 1, fName.length())
                .toLowerCase();
        return end;
    }


    @InjectEvent(EventThreadType.MainThread)
    public void onBtClick(OnBtClick arg){
        getSessionId();
    }

    @InjectEvent(EventThreadType.MainThread)
    public void setupLoadFile(File file){
        this.file = file;
    }

    @InjectEvent(EventThreadType.MainThread)
    public void startControl(Uploaded flag){
        Intent intent = new Intent();
        intent.setClass(getContext(), ControlActivity.class);
        getContext().startActivity(intent);
    }

    @Override
    public void onPhoneShaked() {
        if (getCurPresenter() != this)
            return;
        Toast.makeText(getContext(),"正在获取...",Toast.LENGTH_LONG).show();
        getSessionId();
    }

    @Override
    public void onPhoneShakeLeft() {
        //Toast.makeText(getContext(), "left...", Toast.LENGTH_LONG).show();
        ControlFlag flag = new ControlFlag();
        flag.setAction("next");
        EventPoster.getInstance().broadcast(flag);
    }

    @Override
    public void onPhoneShakeRight() {
        //Toast.makeText(getContext(),"right...",Toast.LENGTH_LONG).show();
        ControlFlag flag = new ControlFlag();
        flag.setAction("pre");
        EventPoster.getInstance().broadcast(flag);
    }

    private void getSessionId() {
        HttpInjectUtil.getInstance().post(this, "getSessionId", null, null);
    }

    @InjectEvent(EventThreadType.MainThread)
    public void doUpload(UploadFlag uploadFlag){
        if (file == null)
            return;
        Toast.makeText(getContext(),"正在上传...",Toast.LENGTH_LONG).show();
        InjectAsycTask.getInstance().Start(this, "uploadThread");
    }

    @Override
    public void onProgress(UploadProgress progress) {
        EventPoster.getInstance().broadcast(progress);
    }

    private void showSession(){
        EventPoster.getInstance().broadcast(session);
    }

    @Override
    public void DestoryPresent() {
        super.DestoryPresent();
        EventPoster.getInstance().unregist(this);
    }

    private void setCallback(){
        EventPoster.getInstance().broadcast(httpService);
    }
}
