package com.gy.myframework.UI.customwidget.autoloadimgview;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.widget.ImageView;

import com.gy.myframework.R;
import com.gy.myframework.Service.loader.imgloader.strategy.mystrategy.entity.ImgLoadConfigs;
import com.gy.myframework.Service.loader.imgloader.strategy.mystrategy.impl.ImageLoader;

/**
 * Created by gy on 2015/12/6.
 */
public class AutoLoadImgView extends ImageView{

    private ImageLoader imageLoader;
    private String url;
    private ImgLoadConfigs config;

    public AutoLoadImgView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        config = new ImgLoadConfigs();
        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.AutoLoadImgView, defStyleAttr, 0);
        int loadsrc = a.getResourceId(R.styleable.AutoLoadImgView_loadimgsrc,R.drawable.loading);
        config.setLoadImgsrc(loadsrc);
        url = a.getString(R.styleable.AutoLoadImgView_defaulturl);
        imageLoader = new ImageLoader(context,config);
        imageLoader.ShowImg(url,this);
    }

    public AutoLoadImgView(Context context, AttributeSet attrs) {
        this(context, attrs,0);
    }

    public AutoLoadImgView(Context context) {
        this(context,null);
    }

    public void ShowImg(String url){
        imageLoader.ShowImg(url,this);
    }


}
