package com.gy.myframework.Service.cache.control;

import com.gy.myframework.Service.cache.IRamCache;
import com.gy.myframework.config.configs;

/**
 * Created by gy on 2015/11/5.
 */
public class RamCacheControl<K, V> implements IRamCache<K, V> {

    private IRamCache cacheStrategy;

    @Override
    public boolean put(K key, V value) {
        if (configs.isrelease)
            ;//
        cacheStrategy.put(key, value);
        return false;
    }

    @Override
    public V get(K key) {
        V v;
        v = (V) cacheStrategy.get(key);
        return v;
    }

    @Override
    public void clear() {
        cacheStrategy.clear();
    }

    @Override
    public void setlimit(long size) {
        cacheStrategy.setlimit(size);
    }
}
