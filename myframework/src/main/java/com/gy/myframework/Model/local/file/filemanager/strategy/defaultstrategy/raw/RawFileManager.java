package com.gy.myframework.Model.local.file.filemanager.strategy.defaultstrategy.raw;

import com.gy.myframework.Model.local.file.filemanager.IFileManagerCallback;

/**
 * Created by gy on 2016/2/2.
 */
public abstract class RawFileManager {
    protected IFileManagerCallback callBack;

    public void setCallBack(IFileManagerCallback callBack) {
        this.callBack = callBack;
    }
}
