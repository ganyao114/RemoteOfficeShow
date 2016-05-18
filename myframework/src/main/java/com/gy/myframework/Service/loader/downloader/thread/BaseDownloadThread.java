package com.gy.myframework.Service.loader.downloader.thread;

import com.gy.myframework.Service.loader.downloader.IDownThread;
import com.gy.myframework.Service.loader.downloader.entity.DownloadThreadRes;

/**
 * Created by gy on 2015/11/14.
 */
public class BaseDownloadThread implements Runnable, IDownThread {

    private DownloadThreadRes dwres;

    @Override
    public void run() {
        synchronized (dwres) {

        }
    }


    @Override
    public void changeRes(DownloadThreadRes res) {
        synchronized (dwres) {
            this.dwres = res;
        }
    }
}
