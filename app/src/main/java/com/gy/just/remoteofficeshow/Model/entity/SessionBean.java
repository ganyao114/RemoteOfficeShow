package com.gy.just.remoteofficeshow.Model.entity;

import java.io.Serializable;

/**
 * Created by gy on 2016/2/26.
 */
public class SessionBean implements Serializable{
    private String id;
    private String passwd;
    private String cookies;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getPasswd() {
        return passwd;
    }

    public void setPasswd(String passwd) {
        this.passwd = passwd;
    }

    public String getCookies() {
        return cookies;
    }

    public void setCookies(String cookies) {
        this.cookies = cookies;
    }

    @Override
    public String toString() {
        return "SessionBean{" +
                "id='" + id + '\'' +
                ", passwd='" + passwd + '\'' +
                '}';
    }
}
