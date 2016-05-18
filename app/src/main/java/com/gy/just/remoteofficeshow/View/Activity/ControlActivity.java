package com.gy.just.remoteofficeshow.View.Activity;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.KeyEvent;

import com.gy.just.remoteofficeshow.R;
import com.gy.just.remoteofficeshow.Service.Flag.ControlFlag;
import com.gy.just.remoteofficeshow.Service.Presenter.ControlPresent;
import com.gy.just.remoteofficeshow.Service.Presenter.MainPresenter;
import com.gy.just.remoteofficeshow.View.Adapter.FragmentAdapter;
import com.gy.just.remoteofficeshow.View.Fragment.ControlFragment;
import com.gy.just.remoteofficeshow.View.Fragment.ViewFragment;
import com.gy.myframework.IOC.Service.event.impl.EventPoster;
import com.gy.myframework.IOC.UI.view.viewinject.annotation.ContentView;
import com.gy.myframework.IOC.UI.view.viewinject.annotation.ViewInject;
import com.gy.myframework.MVP.Presenter.IPresenterCallBack;
import com.gy.myframework.MVP.View.context.activity.BaseAppCompactActivity;

import java.util.ArrayList;
import java.util.List;

@ContentView(R.layout.activity_control_layout)
public class ControlActivity extends BaseAppCompactActivity{

    @ViewInject(R.id.tabs2)
    private TabLayout mTabLayout;
    @ViewInject(R.id.viewpager)
    private ViewPager mViewPager;
    private ControlFlag flag;

    public ControlActivity(){
        Log.e("gy", "dadwjioahdiahwo");
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        flag = new ControlFlag();
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        final ActionBar ab = getSupportActionBar();
        ab.setHomeAsUpIndicator(R.drawable.ic_menu);
        ab.setDisplayHomeAsUpEnabled(true);
        setupViewPager();
    }

    @Override
    protected IPresenterCallBack getPresent() {
        return new ControlPresent();
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (!(MainPresenter.Type.equals("ppt")||MainPresenter.Type.equals("pptx")))
            return super.onKeyDown(keyCode,event);
        switch (keyCode){
            case KeyEvent.KEYCODE_VOLUME_DOWN:
                flag.setAction("next");
                break;
            case KeyEvent.KEYCODE_VOLUME_UP:
                flag.setAction("pre");
                break;
            default:
                return super.onKeyDown(keyCode,event);
        }
        EventPoster.getInstance().broadcast(flag);
        return true;
    }

    private void setupViewPager() {
        //mTabLayout = (TabLayout) findViewById(R.id.tabs);
        List<String> titles = new ArrayList<>();
        titles.add("控制");
        titles.add("视图");
        mTabLayout.addTab(mTabLayout.newTab().setText(titles.get(0)));
        mTabLayout.addTab(mTabLayout.newTab().setText(titles.get(1)));
        List<Fragment> fragments = new ArrayList<>();
        fragments.add(new ControlFragment());
        fragments.add(new ViewFragment());
        FragmentAdapter adapter =
                new FragmentAdapter(getSupportFragmentManager(), fragments, titles);
        mViewPager.setAdapter(adapter);
        mTabLayout.setupWithViewPager(mViewPager);
        mTabLayout.setTabsFromPagerAdapter(adapter);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

}
