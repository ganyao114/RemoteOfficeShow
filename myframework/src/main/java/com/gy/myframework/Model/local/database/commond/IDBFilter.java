package com.gy.myframework.Model.local.database.commond;

/**
 * Created by gy on 2016/1/23.
 */
public interface IDBFilter {
    public IDBFilter from(String tabName);
    public IDBFilter where();
}
