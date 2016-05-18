package com.gy.myframework.Service.loader.imgloader.strategy.mystrategy.interfaceimpl;

import android.graphics.Bitmap;
import android.os.Handler;

import com.gy.myframework.Exception.model.net.http.HttpServiceException;
import com.gy.myframework.Service.loader.imgloader.strategy.mystrategy.IThreadInterface;
import com.gy.myframework.Service.loader.imgloader.strategy.mystrategy.ImgLoadThreadCallBack;
import com.gy.myframework.Service.loader.imgloader.strategy.mystrategy.entity.PhotoToLoad;
import com.gy.myframework.Service.loader.imgloader.strategy.mystrategy.impl.ImageLoader;
import com.gy.myframework.Service.thread.pool.impl.MyWorkThreadQueue;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;


/**
 * Created by gy on 2015/11/7.
 */
public class LoadThread implements IThreadInterface {

    private static LoadThread loadThread;

    public static LoadThread getInterface() {
        if (loadThread == null)
            loadThread = new LoadThread();
        return loadThread;
    }

    @Override
    public void AddTask(Runnable runnable) {
        MyWorkThreadQueue.AddTask(runnable);
    }

    @Override
    public Runnable getLoadRunnable(Handler handler, PhotoToLoad photoToLoad, ImgLoadThreadCallBack callBack) {
        return new ImageLoadThread(handler, photoToLoad, callBack, this);
    }

    @Override
    public Bitmap getBitmap(String url, File f) throws Exception {
        try {
            Bitmap bitmap = null;
            URL imageUrl = new URL(url);
            HttpURLConnection conn = (HttpURLConnection) imageUrl
                    .openConnection();
            conn.setConnectTimeout(30000);
            conn.setReadTimeout(30000);
            conn.setInstanceFollowRedirects(true);
            int responseCode = conn.getResponseCode();
            if (responseCode != HttpURLConnection.HTTP_OK) {
                throw new HttpServiceException("连接服务器出错");
            }
            InputStream is = conn.getInputStream();
            OutputStream os = new FileOutputStream(f);

            ImageLoader.CopyStream(is, os);
            os.close();
            bitmap = ImageLoader.decodeFile(f);
            return bitmap;
        } catch (Exception ex) {
            ex.printStackTrace();
            return null;
        }
    }
}
