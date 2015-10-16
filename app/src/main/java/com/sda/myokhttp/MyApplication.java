package com.sda.myokhttp;

import android.app.Application;

import com.sda.library.HttpCore.MyHttp;

/**
 * Created by scorpio on 15/10/15.
 */
public class MyApplication extends Application {
    public MyHttp client ;
    DataControl pdc ;
    String url = "http://m.ch999.com/app/ProductSearch.aspx";
    @Override
    public void onCreate() {
        super.onCreate();
        client = new MyHttp();
        client.init(getApplicationContext(),url);
        pdc = new DataControl(getApplicationContext());
    }
}
