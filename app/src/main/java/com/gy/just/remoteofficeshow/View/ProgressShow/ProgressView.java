package com.gy.just.remoteofficeshow.View.ProgressShow;

import android.app.ProgressDialog;
import android.content.Context;

import com.gy.myframework.Model.net.http.IUploadCallBack;
import com.gy.myframework.Model.net.http.entity.UploadProgress;

/**
 * Created by gy on 2016/2/28.
 */
public class ProgressView implements IUploadCallBack{
    public int i = 0;
    private Context context;
    private ProgressDialog dialog;

    public ProgressView(Context context) {
        this.context = context;
    }

    public void init(){
        if (dialog == null) {
            dialog = new ProgressDialog(context);
            dialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
            dialog.setMessage("上传文档...");
            dialog.setCancelable(false);
        }
        dialog.show();
    }

    @Override
    public void onProgress(UploadProgress progress) {
        dialog.setProgress(progress.getPersent());
    }

    public void onComplete(){
        dialog.dismiss();
    }

}
