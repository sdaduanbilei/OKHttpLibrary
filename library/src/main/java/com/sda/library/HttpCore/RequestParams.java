package com.sda.library.HttpCore;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.Map;
import java.util.TreeMap;

/**
 * Created by scorpio on 15/9/24.
 */
public class RequestParams {

    public static final String UTF8 = "UTF-8";

    public static Map<String, String> mStringParams;

    public  String cookie ;

    public boolean isRefresh = false ;


    public boolean isRefresh() {
        return isRefresh;
    }

    public void setIsRefresh(boolean isRefresh) {
        this.isRefresh = isRefresh;
    }

    public String getCookie() {
        return cookie;
    }

    public void setCookie(String cookie) {
        this.cookie = cookie;
    }

    public RequestParams() {
        this.mStringParams = new TreeMap<String, String>();
    }


    public RequestParams put(String key, String value) {
        mStringParams.put(key, value);
        return this;
    }

    static byte[] encodeParameters() {
        StringBuffer encodedParams = new StringBuffer();
        try {
            for (Map.Entry<String, String> entry : mStringParams.entrySet()) {
                if (encodedParams.length() > 0) {
                    encodedParams.append("&");
                }

                encodedParams.append(URLEncoder.encode(entry.getKey(), UTF8));
                encodedParams.append("=");
                encodedParams.append(URLEncoder.encode(entry.getValue(),UTF8));
            }
            return encodedParams.toString().getBytes(UTF8);
        } catch (UnsupportedEncodingException uee) {
            throw new RuntimeException("Encoding not supported: " + UTF8, uee);
        }
    }

    @Override
    public String toString(){
        return new String(encodeParameters());
    }
}
