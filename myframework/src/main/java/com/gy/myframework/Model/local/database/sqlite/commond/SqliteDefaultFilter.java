package com.gy.myframework.Model.local.database.sqlite.commond;

import com.gy.myframework.Model.local.database.commond.IDBFilter;

/**
 * Created by gy on 2016/1/23.
 */
public class SqliteDefaultFilter implements IDBFilter{

    protected String tabName;

    private SqliteDefaultFilter(String tabName){
        tabName = tabName;
    }

    @Override
    public IDBFilter from(String tabName) {
        return new SqliteDefaultFilter(tabName);
    }

    @Override
    public IDBFilter where() {
        return null;
    }
}
