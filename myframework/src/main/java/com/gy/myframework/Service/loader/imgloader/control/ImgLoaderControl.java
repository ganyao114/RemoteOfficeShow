package com.gy.myframework.Service.loader.imgloader.control;

import android.widget.ImageView;

import com.gy.myframework.Service.loader.imgloader.IImageLoader;

/**
 * Created by gy on 2016/1/28.
 */
public class ImgLoaderControl implements IImageLoader{
    private IImageLoader imageLoader;

    public ImgLoaderControl(IImageLoader imageLoader) {
        this.imageLoader = imageLoader;
    }
    @Override
    public void ShowImg(String src, ImageView view) {
        imageLoader.ShowImg(src,view);
    }

    public void setLoader(IImageLoader imageLoader){
        this.imageLoader = imageLoader;
    }
}
