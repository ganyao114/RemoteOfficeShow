package com.gy.myframework.Model.net.http;

import com.gy.myframework.Model.net.http.entity.UploadProgress;

/**
 * Created by gy on 2016/2/25.
 */
public interface IUploadCallBack {
    public void onProgress(UploadProgress progress);
}
