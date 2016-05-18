package com.gy.just.remoteofficeshow.View.Fragment;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.CookieManager;
import android.webkit.CookieSyncManager;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.gy.just.remoteofficeshow.R;
import com.gy.just.remoteofficeshow.Service.Presenter.MainPresenter;
import com.gy.myframework.IOC.Core.fragment.BaseFragmentV4;
import com.gy.myframework.IOC.Service.event.annotation.InjectEvent;
import com.gy.myframework.IOC.Service.event.entity.EventThreadType;
import com.gy.myframework.IOC.Service.event.impl.EventPoster;
import com.gy.myframework.IOC.UI.view.viewinject.annotation.ContentView;
import com.gy.myframework.IOC.UI.view.viewinject.annotation.OnClick;
import com.gy.myframework.IOC.UI.view.viewinject.annotation.ViewInject;

import java.io.File;


/**
 * Created by gy on 2016/3/21.
 */
@ContentView(R.layout.fragment_view)
public class ViewFragment extends BaseFragmentV4{
    @ViewInject(R.id.webView)
    private WebView webView;


    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        EventPoster.getInstance().regist(this);
        initWebView();
        ShowHtml(MainPresenter.HTML);
        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    private void initWebView(){
        webView.getSettings().setJavaScriptEnabled(true);
        webView.setScrollBarStyle(WebView.SCROLLBARS_OUTSIDE_OVERLAY);
        webView.setHorizontalScrollBarEnabled(false);
        webView.getSettings().setSupportZoom(true);
        webView.getSettings().setBuiltInZoomControls(true);
        webView.setInitialScale(70);
        webView.setHorizontalScrollbarOverlay(true);
    }

    @InjectEvent(EventThreadType.MainThread)
    public void ShowHtml(String str){
        //webView.loadDataWithBaseURL(null, str, "text/html", "utf-8", null);
        webView.loadUrl(str);
        webView.getSettings().setJavaScriptEnabled(true);
        webView.getSettings().setCacheMode(WebSettings.LOAD_NO_CACHE);
        webView.setWebViewClient(new WebClient());
        //syncCookie(getActivity(),"http://www.jjust.top/ShowPPT/");
    }

    @OnClick(R.id.share)
    public void onShare(View view){
        String content = "http://www.jjust.top/ShowPPT/visitorpage.jsp?ID="+MainPresenter.session.getId()+"&PD="+MainPresenter.session.getPasswd();
        shareMsg("分享链接","分享链接",content,null);
    }

    public void shareMsg(String activityTitle, String msgTitle, String msgText,
                         String imgPath) {
        Intent intent = new Intent(Intent.ACTION_SEND);
        if (imgPath == null || imgPath.equals("")) {
            intent.setType("text/plain"); // 纯文本
        } else {
            File f = new File(imgPath);
            if (f != null && f.exists() && f.isFile()) {
                intent.setType("image/jpg");
                Uri u = Uri.fromFile(f);
                intent.putExtra(Intent.EXTRA_STREAM, u);
            }
        }
        intent.putExtra(Intent.EXTRA_SUBJECT, msgTitle);
        intent.putExtra(Intent.EXTRA_TEXT, msgText);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(Intent.createChooser(intent, activityTitle));
    }

    private void syncCookie(Context context, String url){
        try{
           // Log.d("Nat: webView.syncCookie.url", url);

            CookieSyncManager.createInstance(context);

            CookieManager cookieManager = CookieManager.getInstance();
            cookieManager.setAcceptCookie(true);
            cookieManager.removeSessionCookie();// 移除
            cookieManager.removeAllCookie();
            String oldCookie = cookieManager.getCookie(url);
            if(oldCookie != null){
                //Log.d("Nat: webView.syncCookieOutter.oldCookie", oldCookie);
            }

            StringBuilder sbCookie = new StringBuilder();
            sbCookie.append(String.format("JSESSIONID=%s", "INPUT YOUR JSESSIONID STRING"));
            sbCookie.append(String.format(";domain=%s", "INPUT YOUR DOMAIN STRING"));
            sbCookie.append(String.format(";path=%s","INPUT YOUR PATH STRING"));

            String cookieValue = sbCookie.toString();
            cookieManager.setCookie(url, cookieValue);
            CookieSyncManager.getInstance().sync();

            String newCookie = cookieManager.getCookie(url);
            if(newCookie != null){
               // Log.d("Nat: webView.syncCookie.newCookie", newCookie);
            }
        }catch(Exception e){
           // Log.e("Nat: webView.syncCookie failed", e.toString());
        }
    }


    class WebClient extends WebViewClient{
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            view.loadUrl(url);
            return true;
        }

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        EventPoster.getInstance().unregist(this);
    }
}
