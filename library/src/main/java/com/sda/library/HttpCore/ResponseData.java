package com.sda.library.HttpCore;

import java.io.Serializable;

/**
 * Created by scorpio on 15/10/16.
 */
public class ResponseData implements Serializable{

    private String data ;
    private Throwable throwable ;
    private int header ;


    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    public Throwable getThrowable() {
        return throwable;
    }

    public void setThrowable(Throwable throwable) {
        this.throwable = throwable;
    }

    public int getHeader() {
        return header;
    }

    public void setHeader(int header) {
        this.header = header;
    }
}
