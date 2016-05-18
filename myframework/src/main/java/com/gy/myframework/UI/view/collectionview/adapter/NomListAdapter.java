package com.gy.myframework.UI.view.collectionview.adapter;

import android.content.Context;

import com.gy.myframework.UI.view.collectionview.IAdapterCallBack;
import com.gy.myframework.UI.view.collectionview.viewholder.IViewHolder;

import java.util.List;

/**
 * Created by gy on 2015/11/13.
 */
public class NomListAdapter<T> extends CommonAdapter {

    private IAdapterCallBack callBack;

    public NomListAdapter(Context context, List list, int layoutid, IAdapterCallBack callBack) {
        super(context, list, layoutid);
        this.callBack = callBack;
    }

    @Override
    protected void getMyview(IViewHolder holder, int position) {
        callBack.adapterCall(holder, position);
    }

    public void setList(List<T> list){
        this.list = list;
    }

    public List<T> getList(){
        return this.list;
    }
}
