package com.sda.library.HttpCore;

import android.content.Context;
import android.text.TextUtils;

import com.sda.library.Util.Logs;
import com.squareup.okhttp.Cache;
import com.squareup.okhttp.FormEncodingBuilder;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;

import java.io.File;
import java.io.IOException;
import java.net.CookieManager;
import java.net.CookiePolicy;
import java.net.URLEncoder;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.schedulers.Schedulers;

/**
 * Created by scorpio on 15/10/15.
 */
public class MyHttp {
    public  OkHttpClient client;
    public static String BASE_URL;
    public int cache_size = 10; // 默认缓存空间 10M
    public int connectTimeout = 15 ; // 默认超时时间 15 s

    public enum  Method {
        GET ,
        POST
    }


    public int getConnectTimeout() {
        return connectTimeout;
    }

    public void setConnectTimeout(int connectTimeout) {
        this.connectTimeout = connectTimeout;
    }

    /**
     * 初始化操作
     * @param context
     * @param url
     */
    public void init(Context context, String url) {
        client = new OkHttpClient();
        client.setConnectTimeout(connectTimeout, TimeUnit.SECONDS);
        client.setCookieHandler(new CookieManager(new PersistentCookieStore(context), CookiePolicy.ACCEPT_ALL));
        try {
            long httpCacheSize = cache_size * 1024 * 1024;// 10M
            File httpCacheDir = new File(context.getCacheDir(), "http");
            Class.forName("android.net.http.HttpResponseCache")
                    .getMethod("install", File.class, long.class)
                    .invoke(null, httpCacheDir, httpCacheSize);
            Cache cache = new Cache(httpCacheDir, httpCacheSize);
            client.setCache(cache);
        } catch (Exception e) {
        }

        BASE_URL = url;
    }


    /**
     * 请求核心代码
     * @param params
     * @param url
     * @return
     * @throws IOException
     */
    public ResponseData HttpCore(RequestParams params, final String url,boolean refresh,Method method) throws IOException {
        ResponseData data = new ResponseData();

        Request.Builder builder = new Request.Builder();


        if (!TextUtils.isEmpty(params.getCookie())){
            builder.addHeader("Cookie",params.getCookie());
        }
        if (params.isRefresh){
            builder.addHeader("Cache-Control","max-age=0");
        }
        if (method == Method.GET){
            builder.url(url + "?" + params.toString());
        } else {
            FormEncodingBuilder form_params = new FormEncodingBuilder();
            for (Map.Entry<String, String> entry : params.mStringParams.entrySet()) {
                String key = (URLEncoder.encode(entry.getKey(), params.UTF8));
                String value = (URLEncoder.encode(entry.getValue(),params.UTF8));
                form_params.add(key,value);
            }
            builder.url(url);
            builder.post(form_params.build());
        }
        Request request = builder.build();
        Logs.Debug("client request=="+request.toString());
        try {
            Response response = client.newCall(request).execute();
            if (response.isSuccessful()) {
                data.setData(response.body().string()+"");
                data.setHeader(response.code());
                Logs.Debug("code==" + response.code());
                return data;
            } else {
                data.setThrowable(new Throwable("CODE =" + response.code() + response.toString()));
                return data;
            }
        } catch (Exception e) {
            data.setThrowable(new Throwable(e.toString()));
            return data;
        }

    }


    /**
     * RxJAVA 事件绑定
     * @param request
     * @param url
     * @return
     */
    public Observable<ResponseData> obs(final RequestParams request, final String url, final boolean refresh, final Method m) {
        return Observable.create(new Observable.OnSubscribe<ResponseData>() {
            @Override
            public void call(Subscriber<? super ResponseData> subscriber) {
                try {
                    subscriber.onNext(HttpCore(request, getAbsoluteUrl(url),refresh,m));
                } catch (IOException e) {
                    e.printStackTrace();
                    subscriber.onError(e);
                }
                subscriber.onCompleted();
            }
        }).subscribeOn(Schedulers.io());
    }


    /**
     * post 请求方法
     * @param context
     * @param request
     * @param url
     * @param handler
     */
    public void post(Context context,String url , RequestParams request, final TextHandler handler) {
        obs(request, url,true,Method.POST).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<ResponseData>() {
                    @Override
                    public void call(ResponseData o) {
                        if (o.getThrowable() != null) {
                            handler.onFailure(o.getThrowable());
                        } else {
                            handler.onSuccess(o.getHeader(),o.getData());
                        }
                    }
                });
    }


    public  void get(Context context, String url,RequestParams request, final TextHandler handler) {
        obs(request, url,true,Method.GET).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<ResponseData>() {
                    @Override
                    public void call(ResponseData o) {
                        if (o.getThrowable() != null) {
                            handler.onFailure(o.getThrowable());
                        } else {
                            handler.onSuccess(o.getHeader(),o.getData());
                        }
                    }
                });
    }



    /**
     * 获取请求链接
     * @param relativeUrl
     * @return
     */
    public static String getAbsoluteUrl(String relativeUrl) {
        if (relativeUrl.startsWith("http")) {
            return relativeUrl;
        }
        return BASE_URL + relativeUrl;
    }
}
