package com.gy.just.remoteofficeshow.Service.Flag;

import java.io.Serializable;

/**
 * Created by gy on 2016/3/4.
 */
public class ControlFlag implements Serializable{
    private String action;

    public String getAction() {
        return action;
    }

    public void setAction(String action) {
        this.action = action;
    }
}
