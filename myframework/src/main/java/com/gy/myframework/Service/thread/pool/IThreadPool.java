package com.gy.myframework.Service.thread.pool;

import com.gy.myframework.Service.thread.pool.configs.ThreadPoolConfigs;

/**
 * Created by gy on 2015/11/6.
 */
public interface IThreadPool {
    public void submit(Runnable r);

    public void poolstart();

    public void poolstop();

    public void setconfigs(ThreadPoolConfigs configs);
}
