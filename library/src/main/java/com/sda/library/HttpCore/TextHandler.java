package com.sda.library.HttpCore;

/**
 * Created by scorpio on 15/10/15.
 */
public interface TextHandler {

     void onSuccess(int header,Object response);

     void onFailure(Throwable error);
}
