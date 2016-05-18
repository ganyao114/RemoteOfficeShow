package com.gy.myframework.Service.cache;

/**
 * Created by gy on 2015/11/5.
 */
public interface IDiskCache<K, V> {
    public boolean put(K key, V value);

    public V get(K key);

    public void clear();
}
