package com.sda.myokhttp;

import android.content.Context;

import com.sda.library.HttpCore.DataResponse;
import com.sda.library.HttpCore.RequestParams;
import com.sda.library.HttpCore.TextHandler;
import com.sda.library.Util.Logs;

/**
 * Created by scorpio on 15/10/15.
 */
public class DataControl {

    public static MyApplication app;

    public DataControl(Context context) {
        app = (MyApplication) context.getApplicationContext();
    }

    /**
     * 测试
     *
     * @param context
     * @param url
     * @param dr
     */
    public static void Test1(Context context, String url,final DataResponse dr) {
        RequestParams params = new RequestParams();
        params.setCookie("cityid=39");
        params.put("act","GetHome");
        params.put("cityid", "39");
        app.client.post(context, "", params, new TextHandler() {
            @Override
            public void onSuccess(int header, Object response) {
                dr.onSucc(response.toString());
            }

            @Override
            public void onFailure(Throwable error) {
                Logs.Debug(error.toString());
            }
        });

    }
}
