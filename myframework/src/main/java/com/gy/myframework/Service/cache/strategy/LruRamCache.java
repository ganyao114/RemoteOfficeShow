package com.gy.myframework.Service.cache.strategy;

import com.gy.myframework.Service.cache.IRamCache;

/**
 * Created by gy on 2015/11/5.
 */
public class LruRamCache<K, V> implements IRamCache<K, V> {
    @Override
    public boolean put(K key, V value) {
        return false;
    }

    @Override
    public V get(K key) {
        return null;
    }

    @Override
    public void clear() {

    }

    @Override
    public void setlimit(long size) {

    }
}
