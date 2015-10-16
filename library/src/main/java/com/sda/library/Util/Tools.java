package com.sda.library.Util;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;

/**
 * Created by scorpio on 15/10/16.
 */
public class Tools {

    /**
     * 显示对话框
     *
     * @param context
     * @param msg
     */
    public static void msgbox(Context context, String msg) {
        new AlertDialog.Builder((context)).setTitle("提示").setMessage(msg)
                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialoginterface, int i) {
                    }
                }).show();
    }
}
