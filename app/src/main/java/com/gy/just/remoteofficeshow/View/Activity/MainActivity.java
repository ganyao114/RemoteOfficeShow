package com.gy.just.remoteofficeshow.View.Activity;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ObjectAnimator;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.gy.just.remoteofficeshow.Model.entity.SessionBean;
import com.gy.just.remoteofficeshow.R;
import com.gy.just.remoteofficeshow.Service.Flag.DissmissProgress;
import com.gy.just.remoteofficeshow.Service.Flag.ShowProgressView;
import com.gy.just.remoteofficeshow.Service.Flag.Uploaded;
import com.gy.just.remoteofficeshow.Service.Presenter.MainPresenter;
import com.gy.just.remoteofficeshow.View.ProgressShow.ProgressView;
import com.gy.myframework.IOC.Service.event.annotation.InjectEvent;
import com.gy.myframework.IOC.Service.event.entity.EventThreadType;
import com.gy.myframework.IOC.Service.event.impl.EventPoster;
import com.gy.myframework.IOC.UI.view.viewinject.annotation.ContentView;
import com.gy.myframework.IOC.UI.view.viewinject.annotation.OnClick;
import com.gy.myframework.IOC.UI.view.viewinject.annotation.ViewInject;
import com.gy.myframework.MVP.Presenter.IPresenterCallBack;
import com.gy.myframework.MVP.View.context.activity.BaseAppCompactActivity;
import com.gy.myframework.Model.net.http.impl.MyHttpService;

import java.util.ArrayList;
import java.util.List;

@ContentView(R.layout.activity_main)
public class MainActivity extends BaseAppCompactActivity {

    private TabLayout mTabLayout;
    @ViewInject(R.id.dl_main_drawer)
    private DrawerLayout mDrawerLayout;
    //@ViewInject(R.id.tabs)
    private ViewPager mViewPager;
    private ProgressView progressView;
    @ViewInject(R.id.sessionid)
    private TextView sessionid;
    @ViewInject(R.id.sessionpass)
    private TextView sessionpass;
    @ViewInject(R.id.sessionshow)
    private LinearLayout sessionShowLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EventPoster.getInstance().regist(this);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        final ActionBar ab = getSupportActionBar();
        ab.setHomeAsUpIndicator(R.drawable.ic_menu);
        ab.setDisplayHomeAsUpEnabled(true);

        NavigationView navigationView =
                (NavigationView) findViewById(R.id.nv_main_navigation);
        if (navigationView != null) {
            setupDrawerContent(navigationView);
        }


        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (MainPresenter.HTML == null) {
                    Snackbar.make(view, "未上传文档....", Snackbar.LENGTH_LONG)
                            .setAction("Action", null).show();
                    return;
                }
                Snackbar.make(view, "打开控制....", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
                EventPoster.getInstance().broadcast(new Uploaded());
            }
        });
    }

    @Override
    protected IPresenterCallBack getPresent() {
        return new MainPresenter();
    }

    private void setupViewPager() {
        List<String> titles = new ArrayList<>();
        titles.add("Page One");
        titles.add("Page Two");
        titles.add("Page Three");
        mTabLayout.addTab(mTabLayout.newTab().setText(titles.get(0)));
        mTabLayout.addTab(mTabLayout.newTab().setText(titles.get(1)));
        mTabLayout.addTab(mTabLayout.newTab().setText(titles.get(2)));
        List<Fragment> fragments = new ArrayList<>();
   /*     fragments.add(new ListFragment());
        fragments.add(new ListFragment());
        fragments.add(new ListFragment());
        FragmentAdapter adapter =
                new FragmentAdapter(getSupportFragmentManager(), fragments, titles);
        mViewPager.setAdapter(adapter);
        mTabLayout.setupWithViewPager(mViewPager);
        mTabLayout.setTabsFromPagerAdapter(adapter);
        */
    }

    private void setupDrawerContent(NavigationView navigationView) {
        navigationView.setNavigationItemSelectedListener(
                new NavigationView.OnNavigationItemSelectedListener() {
                    @Override
                    public boolean onNavigationItemSelected(MenuItem menuItem) {
                        menuItem.setChecked(true);
                        mDrawerLayout.closeDrawers();
                        return true;
                    }
                });
    }

    public void openSelectFileView(){
        ObjectAnimator animator = ObjectAnimator.ofFloat(mDrawerLayout, "translationZ", 20, 0);
        animator.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                startActivity(new Intent(MainActivity.this, SelectFileActivity.class));
            }
        });
        animator.start();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        switch (id){
            case android.R.id.home:
                mDrawerLayout.openDrawer(GravityCompat.START);
                return true;

        }

        return super.onOptionsItemSelected(item);
    }

    @OnClick(R.id.selectfile)
    public void onSelectFile(View view){
        if (MainPresenter.session == null)
            Toast.makeText(this,"未获取到房间号请稍等...",Toast.LENGTH_LONG).show();
        else
            openSelectFileView();
    }

    @InjectEvent(EventThreadType.MainThread)
    public void showProgress(ShowProgressView arg){
        progressView.init();
    }

    @InjectEvent(EventThreadType.MainThread)
    public void dissmissProgress(DissmissProgress arg){
        if (progressView != null)
            progressView.onComplete();
    }


    @InjectEvent(EventThreadType.MainThread)
    public void setProgressView(MyHttpService service){
        if (progressView == null)
            progressView = new ProgressView(this);
        service.setUploadCallBack(progressView);
    }

    @InjectEvent(EventThreadType.MainThread)
    public void showSession(SessionBean sessionBean){
        sessionid.setText("  房间ID:"+ sessionBean.getId()+"  ");
        sessionpass.setText("  房间密码:" + sessionBean.getPasswd()+"  ");
    }



    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventPoster.getInstance().unregist(this);
        //Toast.makeText(this,"Destoryed",Toast.LENGTH_LONG).show();
    }
}
